package com.addiction;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.addiction.global.security.SecurityService;
import com.addiction.jwt.dto.LoginUserInfo;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyAnswer.repository.SurveyAnswerRepository;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.surveyQuestion.repository.SurveyQuestionRepository;
import com.addiction.user.push.repository.PushRepository;
import com.addiction.user.refreshToken.repository.RefreshTokenRepository;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Role;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.oauth.feign.google.GoogleApiFeignCall;
import com.addiction.user.users.oauth.feign.kakao.KakaoApiFeignCall;
import com.addiction.user.users.repository.UserRepository;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

	@MockitoBean
	protected SecurityService securityService;
	@MockitoBean
	protected KakaoApiFeignCall kakaoApiFeignCall;
	@MockitoBean
	protected GoogleApiFeignCall googleApiFeignCall;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	protected UserRepository userRepository;
	@Autowired
	protected RefreshTokenRepository refreshTokenRepository;
	@Autowired
	protected PushRepository pushRepository;
	@Autowired
	protected SurveyQuestionRepository surveyQuestionRepository;
	@Autowired
	protected SurveyAnswerRepository surveyAnswerRepository;

	@AfterEach
	public void tearDown() {
		surveyAnswerRepository.deleteAllInBatch();
		surveyQuestionRepository.deleteAllInBatch();
		refreshTokenRepository.deleteAllInBatch();
		pushRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	protected User createUser(String email, String password, SnsType snsType, SettingStatus settingStatus) {
		return User.builder()
			.email(email)
			.password(bCryptPasswordEncoder.encode(password))
			.phoneNumber("010-1234-1234")
			.sex(Sex.MAIL)
			.role(Role.USER)
			.snsType(snsType)
			.settingStatus(settingStatus)
			.build();
	}

	protected LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}

	protected SurveyQuestion createSurveyQuestion(String question) {
		return SurveyQuestion.builder()
			.question(question)
			.build();
	}

	protected SurveyAnswer createSurveyAnswer(SurveyQuestion surveyQuestion, String answer, int score) {
		return SurveyAnswer.builder()
			.surveyQuestion(surveyQuestion)
			.answer(answer)
			.score(score)
			.build();
	}
}
