package com.addiction;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertHistory.entity.AlertHistory;
import com.addiction.alertHistory.entity.AlertHistoryStatus;
import com.addiction.alertHistory.repository.AlertHistoryRepository;
import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.alertSetting.entity.enums.AlertType;
import com.addiction.alertSetting.repository.AlertSettingRepository;
import com.addiction.challenge.challenge.entity.Challenge;
import com.addiction.challenge.challenge.repository.ChallengeJpaRepository;
import com.addiction.challenge.challenge.repository.ChallengeRepository;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryJpaRepository;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryRepository;
import com.addiction.common.enums.YnStatus;
import com.addiction.global.security.SecurityService;
import com.addiction.jwt.dto.LoginUserInfo;
import com.addiction.storage.service.OracleStorageService;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyAnswer.repository.SurveyAnswerRepository;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.surveyQuestion.repository.SurveyQuestionRepository;
import com.addiction.survey.surveyResult.entity.SurveyResult;
import com.addiction.survey.surveyResult.repository.SurveyResultRepository;
import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;
import com.addiction.survey.surveyResultDescription.repository.SurveyResultDescriptionRepository;
import com.addiction.user.push.entity.Push;
import com.addiction.user.push.repository.PushRepository;
import com.addiction.user.refreshToken.repository.RefreshTokenRepository;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.repository.UserCigaretteRepository;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Role;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.oauth.feign.google.GoogleApiFeignCall;
import com.addiction.user.users.oauth.feign.kakao.KakaoApiFeignCall;
import com.addiction.user.users.repository.UserJpaRepository;
import com.addiction.user.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockitoBean
    protected SecurityService securityService;
    @MockitoBean
    protected KakaoApiFeignCall kakaoApiFeignCall;
    @MockitoBean
    protected GoogleApiFeignCall googleApiFeignCall;
    @MockitoBean
    protected OracleStorageService oracleStorageService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserJpaRepository userJpaRepository;
    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;
    @Autowired
    protected PushRepository pushRepository;
    @Autowired
    protected SurveyQuestionRepository surveyQuestionRepository;
    @Autowired
    protected SurveyAnswerRepository surveyAnswerRepository;
    @Autowired
    protected SurveyResultRepository surveyResultRepository;
    @Autowired
    protected SurveyResultDescriptionRepository surveyResultDescriptionRepository;
    @Autowired
    protected UserCigaretteRepository userCigaretteRepository;
    @MockitoBean
    protected com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository userCigaretteHistoryRepository;
    @MockitoBean
    protected org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;
    @Autowired
    protected AlertHistoryRepository alertHistoryRepository;
    @Autowired
    protected AlertSettingRepository alertSettingRepository;
    @Autowired
    protected ChallengeRepository challengeRepository;
    @Autowired
    protected ChallengeJpaRepository cChallengeJpaRepository;
    @Autowired
    protected ChallengeHistoryJpaRepository challengeHistoryJpaRepository;
    @Autowired
    protected ChallengeHistoryRepository challengeHistoryRepository;

    @AfterEach
    public void tearDown() {
        challengeHistoryRepository.deleteAllInBatch();
        challengeRepository.deleteAllInBatch();
        alertHistoryRepository.deleteAllInBatch();
        alertSettingRepository.deleteAllInBatch();
        surveyResultDescriptionRepository.deleteAllInBatch();
        surveyResultRepository.deleteAllInBatch();
        surveyAnswerRepository.deleteAllInBatch();
        surveyQuestionRepository.deleteAllInBatch();
        refreshTokenRepository.deleteAllInBatch();
        pushRepository.deleteAllInBatch();
        userCigaretteRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    protected User createUser(String email, String password, SnsType snsType, SettingStatus settingStatus) {
        return User.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickName("테스트 닉네임")
                .phoneNumber("010-1234-1234")
                .sex(Sex.MALE)
                .role(Role.USER)
                .snsType(snsType)
                .settingStatus(settingStatus)
                .purpose("테스트 목표")
                .build();
    }

    protected LoginUserInfo createLoginUserInfo(Long userId) {
        return LoginUserInfo.builder()
                .userId(userId)
                .build();
    }

	protected SurveyQuestion createSurveyQuestion(String question) {
		return SurveyQuestion.builder()
			.question(question)
			.build();
	}

	protected SurveyAnswer createSurveyAnswer(SurveyQuestion surveyQuestion, String answer, Integer score) {
		return SurveyAnswer.builder()
			.surveyQuestion(surveyQuestion)
			.answer(answer)
			.score(score)
			.build();
	}

	protected SurveyResult createSurveyResult(String title, Integer score) {
		return SurveyResult.builder()
			.title(title)
			.score(score)
			.build();
	}

	protected SurveyResultDescription createSurveyResultDescription(SurveyResult surveyResult, String description) {
		return SurveyResultDescription.builder()
			.surveyResult(surveyResult)
			.description(description)
			.build();
	}

	protected UserCigarette createUserCigarette(User user) {
		return UserCigarette.createEntity(user, "테스트 주소", 1000L);
	}



	protected AlertHistory createAlertHistory(User user, String alertDescription, AlertHistoryStatus alertHistoryStatus) {
		return AlertHistory.builder()
			.user(user)
			.alertDescription(alertDescription)
			.alertHistoryStatus(alertHistoryStatus)
			.build();
	}

	protected AlertHistory createFriendCodeAlertHistory(User user, String alertDescriptionInfo,AlertHistoryStatus alertHistoryStatus) {
		return AlertHistory.builder()
			.user(user)
			.alertDestinationType(AlertDestinationType.FRIEND_CODE)
			.alertDestinationInfo(alertDescriptionInfo)
			.alertHistoryStatus(alertHistoryStatus)
			.build();
	}

    protected Push createPush(User user) {
        return Push.builder()
                .user(user)
                .deviceId("testdeviceId")
                .pushToken("testPushToken")
                .build();
    }

    protected Challenge createChallenge(User user) {
        return Challenge.builder()
                .content("testcontent")
                .title("testtitle")
                .badge("testbadge")
                .user(user)
                .build();
    }

    protected ChallengeHistory createChallengeHistory(User user, Challenge challenge, YnStatus ynStatus) {
        return ChallengeHistory.builder()
                .user(user)
                .challenge(challenge)
                .finishYn(ynStatus)
                .build();
    }

    protected AlertSetting createAlertSetting(User user, AlertType all, AlertType smokingWarning,
                                              AlertType leaderboardRank, AlertType challenge, AlertType report) {
        return AlertSetting.builder()
                .user(user)
                .all(all)
                .smokingWarning(smokingWarning)
                .leaderboardRank(leaderboardRank)
                .challenge(challenge)
                .report(report)
                .build();
    }
}
