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

import com.addiction.user.users.entity.enums.Sex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import com.addiction.user.users.controller.LoginController;
import com.addiction.user.users.controller.request.LoginOauthRequest;
import com.addiction.user.users.controller.request.LoginRequest;
import com.addiction.user.users.controller.request.UserSaveRequest;
import com.addiction.user.users.service.UserService;
import com.addiction.user.users.service.request.LoginServiceRequest;
import com.addiction.user.users.service.request.OAuthLoginServiceRequest;
import com.addiction.user.users.service.request.UserSaveServiceRequest;
import com.addiction.user.users.service.response.LoginResponse;
import com.addiction.user.users.service.response.OAuthLoginResponse;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.service.LoginService;
import com.addiction.user.users.service.response.UserSaveResponse;

import docs.RestDocsSupport;

public class LoginControllerDocsTest extends RestDocsSupport {

	private final LoginService loginService = mock(LoginService.class);
	private final UserService userService = mock(UserService.class);

	@Override
	protected Object initController() {
		return new LoginController(loginService, userService);
	}
	@DisplayName("일반 로그인 API")
	@Test
	void normalLogin() throws Exception {
		// given
		LoginRequest request = LoginRequest.builder()
			.email("tkdrl8908@naver.com")
			.password("1234")
			.deviceId("testdeviceId")
			.pushKey("tessPushKey")
			.build();

		given(loginService.normalLogin(any(LoginServiceRequest.class)))
			.willReturn(LoginResponse.builder()
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
				post("/api/v1/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("user-login",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("deviceId").type(JsonFieldType.STRING)
						.description("디바이스ID"),
					fieldWithPath("pushKey").type(JsonFieldType.STRING)
						.description("푸시키")
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
						.description("미션 및 캐릭터 초기세팅여부. 가능한 값: " + Arrays.toString(SettingStatus.values()))
				)
			));
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

	@DisplayName("사용자 저장 API")
	@Test
	void 사용자_저장_API() throws Exception {
		// given
		UserSaveRequest request = UserSaveRequest.builder()
                .email("test@test.com")
                .password("1234")
                .birthDay("123411111")
                .nickName("testUser")
                .sex(Sex.FEMAIL)
                .build();

		given(userService.save(any(UserSaveServiceRequest.class)))
			.willReturn(UserSaveResponse.builder()
                .email("test@test.com")
                .birthDay("123411111")
                .nickName("testUser")
                .sex(Sex.FEMAIL)
				.build()
			);

		// when // then
		mockMvc.perform(
				post("/api/v1/auth/join")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andDo(document("user-save",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("birthDay").type(JsonFieldType.STRING)
						.description("생년월일 (YYYYMMDD)"),
					fieldWithPath("nickName").type(JsonFieldType.STRING)
						.description("닉네임"),
                    fieldWithPath("sex").type(JsonFieldType.STRING)
                            .description("성별 가능한값: " + Arrays.toString(Sex.values()))
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
					fieldWithPath("data.birthDay").type(JsonFieldType.STRING)
						.description("생년월일"),
					fieldWithPath("data.nickName").type(JsonFieldType.STRING)
						.description("닉네임"),
                    fieldWithPath("data.sex").type(JsonFieldType.STRING)
                            .description("성별")
				)
			));
	}
}
