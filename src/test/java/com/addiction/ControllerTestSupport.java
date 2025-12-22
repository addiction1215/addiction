package com.addiction;

import com.addiction.alertHistory.controller.alertHistory.AlertHistoryController;
import com.addiction.alertHistory.service.alertHistory.AlertHistoryReadService;
import com.addiction.alertHistory.service.alertHistory.AlertHistoryService;
import com.addiction.alertSetting.controller.AlertSettingController;
import com.addiction.alertSetting.service.AlertSettingReadService;
import com.addiction.alertSetting.service.AlertSettingService;
import com.addiction.challenge.controller.ChallengeController;
import com.addiction.challenge.service.ChallengeReadService;
import com.addiction.challenge.service.ChallengeService;
import com.addiction.survey.surveyQuestion.controller.SurveyQuestionController;
import com.addiction.survey.surveyQuestion.service.SurveyQuestionReadService;
import com.addiction.user.userCigarette.controller.UserCigaretteController;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigarette.service.UserCigaretteService;
import com.addiction.user.userCigaretteHistory.controller.UserCigaretteHistoryController;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.users.controller.LoginController;
import com.addiction.user.users.controller.UserController;
import com.addiction.user.users.service.LoginService;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = {
        LoginController.class,
        UserController.class,
        SurveyQuestionController.class,
        UserCigaretteController.class,
        UserCigaretteHistoryController.class,
        AlertHistoryController.class,
        AlertSettingController.class,
        ChallengeController.class,
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected EntityManager entityManager;

    @MockitoBean
    protected LoginService loginService;

    @MockitoBean
    protected UserService userService;

    @MockitoBean
    protected SurveyQuestionReadService surveyQuestionReadService;

    @MockitoBean
    protected UserReadService userReadService;

    @MockitoBean
    protected UserCigaretteService userCigaretteService;

    @MockitoBean
    protected UserCigaretteReadService userCigaretteReadService;

    @MockitoBean
    protected UserCigaretteHistoryService userCigaretteHistoryService;

    @MockitoBean
    protected AlertHistoryReadService alertHistoryReadService;

    @MockitoBean
    protected AlertHistoryService alertHistoryService;

    @MockitoBean
    protected AlertSettingReadService alertSettingReadService;

    @MockitoBean
    protected AlertSettingService alertSettingService;

    @MockitoBean
    protected ChallengeReadService challengeReadService;

    @MockitoBean
    protected ChallengeService challengeService;

}

