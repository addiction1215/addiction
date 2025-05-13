package com.addiction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.addiction.survey.surveyQuestion.controller.SurveyQuestionController;
import com.addiction.survey.surveyQuestion.service.SurveyQuestionReadService;
import com.addiction.user.users.controller.LoginController;
import com.addiction.user.users.controller.UserController;
import com.addiction.user.users.service.LoginService;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;

@ActiveProfiles("test")
@WebMvcTest(controllers = {
	LoginController.class,
	UserController.class,
	SurveyQuestionController.class,
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

}

