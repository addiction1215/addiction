package docs.users;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import com.addiction.user.users.controller.LoginController;
import com.addiction.user.users.dto.controller.request.LoginOauthRequest;
import com.addiction.user.users.dto.service.request.OAuthLoginServiceRequest;
import com.addiction.user.users.dto.service.response.OAuthLoginResponse;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.service.LoginService;

import docs.RestDocsSupport;

public class LoginControllerDocsTest extends RestDocsSupport {

	private final LoginService loginService = mock(LoginService.class);

	@Override
	protected Object initController() {
		return new LoginController(loginService);
	}

	@DisplayName("OAuth 로그인 API")
	@Test
	void oauthLogin() throws Exception {
		// given
		LoginOauthRequest request = LoginOauthRequest.builder()
			.token("dagrjrtkfddsdasfheherhrfbgngmusduktregegwfwdwdwdwd")
			.snsType(SnsType.KAKAO)
			.deviceId("testdeviceId")
			.pushKey("tessPushKey")
			.build();

		given(loginService.oauthLogin(any(OAuthLoginServiceRequest.class)))
			.willReturn(OAuthLoginResponse.builder()
				.email("tkdrl8908@naver.com")
				.accessToken(
					"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0a2RybDg5MDhAbmF2ZXIuY29tIiwiZXhwIjoxNjk3NzI1OTYzfQ.StpNeN7Mrcm9n3niSPU8ItRMBZqy__gS8AjRkqlIZ2dWtLaciMQF6EGPY4JaagoFkP-GfhUr8pMYfRewEZ-BYg")
				.refreshToken(
					"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0a2RybDg5MDhAbmF2ZXIuY29tIiwiZXhwIjoxNjk3NzY5MTYzfQ.DJwKVuZxw3zTK8RdnnwS45JM0V_3DJ0kpCDMaf3wnyv5GwLtwwKtVNhfeJmhcGYJZ3gvu534kAZGtAoZb_dgWw")
				.settingStatus(SettingStatus.INCOMPLETE)
				.build()
			);

		// when // then
		mockMvc.perform(
				post("/api/v1/auth/oauth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("user-oauthLogin",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("token").type(JsonFieldType.STRING)
						.description("토큰값"),
					fieldWithPath("snsType").type(JsonFieldType.STRING)
						.description("인증타입 가능한 값: " + Arrays.toString(SnsType.values())),
					fieldWithPath("deviceId").type(JsonFieldType.STRING)
						.description("디바이스ID"),
					fieldWithPath("pushKey").type(JsonFieldType.STRING)
						.description("푸시토큰")
				),
				responseFields(
					fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
						.description("코드"),
					fieldWithPath("httpStatus").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("메세지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터"),
					fieldWithPath("data.email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
						.description("Access-Token"),
					fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
						.description("Refresh-Token"),
					fieldWithPath("data.settingStatus").type(JsonFieldType.STRING)
						.description("설문지 세팅 여부. 가능한 값: " + Arrays.toString(SettingStatus.values()))
				)
			));
	}
}
