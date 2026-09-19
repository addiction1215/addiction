package com.addiction.user.users.service.impl;

import com.addiction.storage.enums.BucketKind;
import com.addiction.storage.service.S3StorageService;
import com.addiction.survey.userSurveyResponse.entity.UserSurveyResponse;
import com.addiction.survey.userSurveyResponse.repository.UserSurveyResponseRepository;
import com.addiction.user.users.service.response.UserInfoResponse;
import com.addiction.user.users.service.response.UserProfileResponse;
import com.addiction.user.users.service.response.SmokingTendencyComparisonStatus;
import com.addiction.user.users.service.response.SmokingTendencyLevel;
import com.addiction.user.users.service.response.UserSmokingTendencyResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.exception.AddictionException;
import com.addiction.global.security.SecurityService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.repository.UserRepository;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.response.UserPurposeResponse;
import com.addiction.user.users.service.response.UserStartDateResponse;

import lombok.RequiredArgsConstructor;

import com.addiction.user.users.service.response.UserSimpleProfileResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadServiceImpl implements UserReadService {

	private static final String UNKNOWN_USER = "해당 회원은 존재하지 않습니다.";

	private final SecurityService securityService;
    private final S3StorageService s3StorageService;

	private final UserRepository userRepository;
    private final UserSurveyResponseRepository userSurveyResponseRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllWithPushes() {
        return userRepository.findAllWithPushes();
    }

    @Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new AddictionException(UNKNOWN_USER));
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new AddictionException(UNKNOWN_USER));
	}

	@Override
	public UserStartDateResponse findStartDate() {
		return UserStartDateResponse.createResponse(
			findById(securityService.getCurrentLoginUserInfo().getUserId())
		);
	}

	@Override
	public UserPurposeResponse findPurpose() {
		return UserPurposeResponse.createResponse(
			findById(securityService.getCurrentLoginUserInfo().getUserId())
		);
	}

	@Override
	public UserProfileResponse findProfile() {
        User user = findById(securityService.getCurrentLoginUserInfo().getUserId());
		return UserProfileResponse.createResponse(
                user,
                s3StorageService.createPresignedUrl(user.getProfileUrl(), BucketKind.USER)
		);
	}

	@Override
	public UserInfoResponse findUserInfo() {
		return UserInfoResponse.createResponse(
				findById(securityService.getCurrentLoginUserInfo().getUserId())
		);
	}

	@Override
	public UserSimpleProfileResponse findSimpleProfile() {
        User user = findById(securityService.getCurrentLoginUserInfo().getUserId());
		return UserSimpleProfileResponse.createResponse(
                user,
                s3StorageService.createPresignedUrl(user.getProfileUrl(), BucketKind.USER)
		);
	}

    @Override
    public UserSmokingTendencyResponse findSmokingTendency() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        List<UserSurveyResponse> responses = userSurveyResponseRepository.findLatestTwoByUserId(userId);

        if (responses.isEmpty()) {
            return UserSmokingTendencyResponse.noSurvey();
        }

        UserSurveyResponse latest = responses.get(0);
        int currentScore = calculateQuitMateScore(latest.getTotalScore());
        SmokingTendencyLevel currentLevel = determineLevel(currentScore);

        if (responses.size() == 1) {
            return UserSmokingTendencyResponse.builder()
                    .hasSurvey(true)
                    .hasComparison(false)
                    .rawScore(latest.getTotalScore())
                    .quitMateScore(currentScore)
                    .level(currentLevel)
                    .comparisonStatus(SmokingTendencyComparisonStatus.NOT_AVAILABLE)
                    .lastSurveyedAt(latest.getSubmittedAt())
                    .build();
        }

        UserSurveyResponse previous = responses.get(1);
        int previousScore = calculateQuitMateScore(previous.getTotalScore());
        SmokingTendencyLevel previousLevel = determineLevel(previousScore);
        int scoreChange = currentScore - previousScore;
        int levelChange = currentLevel.getRank() - previousLevel.getRank();

        return UserSmokingTendencyResponse.builder()
                .hasSurvey(true)
                .hasComparison(true)
                .rawScore(latest.getTotalScore())
                .quitMateScore(currentScore)
                .level(currentLevel)
                .previousQuitMateScore(previousScore)
                .comparisonStatus(determineComparisonStatus(levelChange, scoreChange))
                .scoreChange(scoreChange)
                .levelChange(levelChange)
                .lastSurveyedAt(latest.getSubmittedAt())
                .build();
    }

    private int calculateQuitMateScore(int rawScore) {
        return Math.round((99.0f - rawScore) / (99.0f - 21.0f) * 100);
    }

    private SmokingTendencyLevel determineLevel(int quitMateScore) {
        if (quitMateScore <= 39) {
            return SmokingTendencyLevel.SEVERE;
        }
        if (quitMateScore <= 59) {
            return SmokingTendencyLevel.MODERATE;
        }
        return SmokingTendencyLevel.MILD;
    }

    private SmokingTendencyComparisonStatus determineComparisonStatus(int levelChange, int scoreChange) {
        if (levelChange > 0) {
            return SmokingTendencyComparisonStatus.LEVEL_IMPROVED;
        }
        if (levelChange < 0) {
            return SmokingTendencyComparisonStatus.LEVEL_WORSENED;
        }
        if (scoreChange > 0) {
            return SmokingTendencyComparisonStatus.SCORE_INCREASED;
        }
        if (scoreChange < 0) {
            return SmokingTendencyComparisonStatus.SCORE_DECREASED;
        }
        return SmokingTendencyComparisonStatus.UNCHANGED;
    }
}
