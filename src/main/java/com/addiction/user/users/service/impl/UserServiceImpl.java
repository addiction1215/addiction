package com.addiction.user.users.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.addiction.global.exception.AddictionException;
import com.addiction.dailySmokingPush.service.DailySmokingPushScheduleService;
import com.addiction.user.users.service.request.*;
import com.addiction.user.users.service.response.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.security.SecurityService;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyAnswer.service.SurveyAnswerReadService;
import com.addiction.survey.surveyQuestion.enums.SurveyType;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.surveyQuestion.service.SurveyQuestionReadService;
import com.addiction.survey.surveyResult.service.SurveyResultReadService;
import com.addiction.survey.userSurveyResponse.entity.UserSurveyResponse;
import com.addiction.survey.userSurveyResponse.repository.UserSurveyResponseRepository;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.nickname.RandomNicknameGenerator;
import com.addiction.user.users.repository.UserRepository;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final SecurityService securityService;
	private final SurveyAnswerReadService surveyAnswerReadService;
	private final SurveyQuestionReadService surveyQuestionReadService;
	private final SurveyResultReadService surveyResultReadService;
	private final UserSurveyResponseRepository userSurveyResponseRepository;
	private final UserReadService userReadService;
    private final RandomNicknameGenerator randomNicknameGenerator;

	private final UserRepository userRepository;
	private final DailySmokingPushScheduleService dailySmokingPushScheduleService;

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public UserSaveResponse save(UserSaveServiceRequest userSaveServiceRequest) {
        validateDuplicateEmail(userSaveServiceRequest.getEmail());
        String nickName = randomNicknameGenerator.resolve(userSaveServiceRequest.getNickName());
		User savedUser = userRepository.save(userSaveServiceRequest.toEntity(bCryptPasswordEncoder, nickName));
		dailySmokingPushScheduleService.createDefaults(savedUser);
		return UserSaveResponse.createResponse(savedUser);
	}

	@Override
	public UserUpdateResponse update(UserUpdateServiceRequest userUpdateServiceRequest) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		user.update(
			userUpdateServiceRequest.getSex(),
			userUpdateServiceRequest.getBirthDay()
		);
		return UserUpdateResponse.createResponse(user);
	}

	@Override
	public UserUpdateSurveyResponse submitSurvey(UserUpdateSurveyServiceRequest userUpdateSurveyServiceRequest) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		List<SurveyAnswer> selectedAnswers = findAndValidateSurveyAnswers(userUpdateSurveyServiceRequest.getAnswerId());
		List<SurveyQuestion> numberInputQuestions = surveyQuestionReadService
                .findAllBySurveyTypeOrderBySortAsc(SurveyType.NUMBER);
        validateAllSurveyQuestionsAnswered(selectedAnswers, numberInputQuestions);
        int totalScore = selectedAnswers.stream().mapToInt(SurveyAnswer::getScore).sum();

        UserSurveyResponse surveyResponse = UserSurveyResponse.create(user, totalScore);

        // RADIO/CHECKBOX: 선택한 SurveyAnswer를 설문 응답 이력에 추가한다.
        for (SurveyAnswer selectedAnswer : selectedAnswers) {
            surveyResponse.addAnswer(selectedAnswer);
        }

        // NUMBER: 질문의 sort에 맞는 요청 숫자값을 설문 응답 이력에 추가한다.
        for (SurveyQuestion numberInputQuestion : numberInputQuestions) {
            Integer numericValue = numericValueFor(numberInputQuestion, userUpdateSurveyServiceRequest);
            surveyResponse.addNumericAnswer(numberInputQuestion, numericValue);
        }
        userSurveyResponseRepository.save(surveyResponse);

		user.updateSurvey(
			userUpdateSurveyServiceRequest.getPurpose(),
			totalScore,
			userUpdateSurveyServiceRequest.getCigarettePrice(),
            userUpdateSurveyServiceRequest.getCigaretteCount(),
			LocalDateTime.now()
		);

		return UserUpdateSurveyResponse.of(surveyResultReadService.findClosestScore(totalScore), totalScore);
	}

    /**
     * 프론트가 보낸 객관식 선택지 ID로 설문 답변을 조회하고 유효성을 검증한다.
     * <p>
     * 중복 선택지, 존재하지 않는 선택지, 단일 선택 질문의 복수 응답을 차례로 검증한다.
     * NUMBER 문항을 포함한 전체 설문 문항 응답 여부는 호출부의
     * {@link #validateAllSurveyQuestionsAnswered(List, List)}에서 별도로 검증한다.
     */
    private List<SurveyAnswer> findAndValidateSurveyAnswers(List<Long> answerIds) {
        Set<Long> distinctAnswerIds = new HashSet<>(answerIds);
        if (distinctAnswerIds.size() != answerIds.size()) {
            throw new AddictionException("설문 답변은 중복해서 선택할 수 없습니다.");
        }

        List<SurveyAnswer> surveyAnswers = surveyAnswerReadService.findAllByIdInWithSurveyQuestion(answerIds);
        if (surveyAnswers.size() != answerIds.size()) {
            throw new AddictionException("존재하지 않는 설문조사 답변입니다.");
        }

        validateSingleChoiceQuestions(surveyAnswers);
        return surveyAnswers;
    }

    private void validateSingleChoiceQuestions(List<SurveyAnswer> surveyAnswers) {
        // Set은 같은 질문 ID를 한 번만 보관한다.
        Set<Long> answeredSingleChoiceQuestionIds = new HashSet<>();

        for (SurveyAnswer surveyAnswer : surveyAnswers) {
            SurveyQuestion surveyQuestion = surveyAnswer.getSurveyQuestion();
            if (surveyQuestion.getSurveyType() == SurveyType.CHECKBOX) {
                continue;
            }

            // Set.add()는 새 ID를 추가하면 true, 이미 있던 ID면 false를 반환한다.
            boolean alreadyAnswered = !answeredSingleChoiceQuestionIds.add(surveyQuestion.getId());
            if (alreadyAnswered) {
                throw new AddictionException("단일 선택 설문 문항에는 하나의 답변만 선택할 수 있습니다.");
            }
        }
    }

    /**
     * 객관식 선택지 응답과 NUMBER 입력 문항을 합쳐, 현재 설문의 모든 질문에 답했는지 검증한다.
     * <p>
     * {@code selectedAnswers}에는 RADIO/CHECKBOX 선택지 응답이, {@code numberInputQuestions}에는
     * 담뱃값과 흡연량을 연결할 NUMBER 질문 정의가 들어온다. 실제 숫자값은 요청 DTO에서 전달받는다.
     */
    private void validateAllSurveyQuestionsAnswered(
            List<SurveyAnswer> selectedAnswers,
            List<SurveyQuestion> numberInputQuestions
    ) {
        validateNumericQuestionConfiguration(numberInputQuestions);

        Set<Long> answeredQuestionIds = new HashSet<>();

        for (SurveyAnswer surveyAnswer : selectedAnswers) {
            answeredQuestionIds.add(surveyAnswer.getSurveyQuestion().getId());
        }
        for (SurveyQuestion numericQuestion : numberInputQuestions) {
            answeredQuestionIds.add(numericQuestion.getId());
        }

        if (answeredQuestionIds.size() != surveyQuestionReadService.count()) {
            throw new AddictionException("모든 설문 문항에 답변해야 합니다.");
        }
    }

    private void validateNumericQuestionConfiguration(List<SurveyQuestion> numberInputQuestions) {
        if (numberInputQuestions.size() != 2) {
            throw new AddictionException("숫자 입력 설문 문항 설정이 올바르지 않습니다.");
        }

        Set<Integer> numericQuestionSorts = new HashSet<>();
        for (SurveyQuestion numericQuestion : numberInputQuestions) {
            Integer sort = numericQuestion.getSort();
            boolean isPriceOrCountQuestion = sort != null && (sort == 1 || sort == 2);

            if (!isPriceOrCountQuestion || !numericQuestionSorts.add(sort)) {
                throw new AddictionException("숫자 입력 설문 문항 설정이 올바르지 않습니다.");
            }
        }
    }

    private Integer numericValueFor(SurveyQuestion question, UserUpdateSurveyServiceRequest request) {
        return switch (question.getSort()) {
            case 1 -> request.getCigarettePrice();
            case 2 -> request.getCigaretteCount();
            default -> throw new AddictionException("숫자 입력 설문 문항 설정이 올바르지 않습니다.");
        };
    }

	@Override
	public UserUpdatePurposeResponse updatePurpose(UserUpdatePurposeServiceRequest userUpdatePurposeServiceRequest) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		user.updatePurpose(userUpdatePurposeServiceRequest.getPurpose());
		return UserUpdatePurposeResponse.createResponse(user);
	}

	@Override
	public UserUpdateProfileResponse updateProfile(UserUpdateProfileServiceRequest userUpdateProfileServiceRequest) {
        validateProfileImageUpdateRequest(userUpdateProfileServiceRequest);
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		user.updateProfile(
				userUpdateProfileServiceRequest.getNickName(),
				userUpdateProfileServiceRequest.getIntroduction(),
				userUpdateProfileServiceRequest.getSex(),
                userUpdateProfileServiceRequest.getBirthDay(),
                resolveProfileUrl(userUpdateProfileServiceRequest),
                userUpdateProfileServiceRequest.getResetProfileImage()
		);
		return UserUpdateProfileResponse.createResponse(user);
	}

	@Override
	public UserUpdateInfoResponse updateInfo(UserUpdateInfoServiceRequest userUpdateInfoServiceRequest) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		user.updateInfo(
				bCryptPasswordEncoder,
				userUpdateInfoServiceRequest.getPassword(),
				userUpdateInfoServiceRequest.getPhoneNumber(),
				userUpdateInfoServiceRequest.getEmail()
		);
		return UserUpdateInfoResponse.createResponse(user);
	}

	@Override
	public Boolean updatePassword(UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());

		validateCurrentPassword(user, userUpdatePasswordServiceRequest.getCurrentPassword());
		validateNewPassword(userUpdatePasswordServiceRequest);

		user.updateInfo(
				bCryptPasswordEncoder,
				userUpdatePasswordServiceRequest.getNewPassword(),
				user.getPhoneNumber(),
				user.getEmail()
		);
		return true;
	}

    @Override
    public void updateStartDate(Long userId, LocalDateTime lastSmokeTime) {
        User user = userReadService.findById(userId);
        user.updateStartDate(lastSmokeTime);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AddictionException("이미 존재하는 이메일입니다.");
        }
    }

    private void validateProfileImageUpdateRequest(UserUpdateProfileServiceRequest userUpdateProfileServiceRequest) {
        if (Boolean.TRUE.equals(userUpdateProfileServiceRequest.getResetProfileImage())
                && userUpdateProfileServiceRequest.getProfileUrl() != null) {
            throw new AddictionException("프로필 이미지 변경과 초기화는 동시에 요청할 수 없습니다.");
        }
    }

    private void validateCurrentPassword(User user, String currentPassword) {
        if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AddictionException("현재 비밀번호가 일치하지 않습니다.");
        }
    }

    private void validateNewPassword(UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest) {
        if (userUpdatePasswordServiceRequest.getCurrentPassword().equals(userUpdatePasswordServiceRequest.getNewPassword())) {
            throw new AddictionException("현재 비밀번호와 새 비밀번호는 같을 수 없습니다.");
        }

        if (!userUpdatePasswordServiceRequest.getNewPassword()
                .equals(userUpdatePasswordServiceRequest.getNewPasswordConfirm())) {
            throw new AddictionException("새 비밀번호 확인이 일치하지 않습니다.");
        }
    }

    private String resolveProfileUrl(UserUpdateProfileServiceRequest userUpdateProfileServiceRequest) {
        return userUpdateProfileServiceRequest.getProfileUrl();
    }

	@Override
	public Boolean withdraw() {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		user.withdraw();
        return true;
	}

}
