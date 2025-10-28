package docs.alertSetting;

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

import com.addiction.alertSetting.controller.AlertSettingController;
import com.addiction.alertSetting.controller.request.AlertSettingUpdateRequest;
import com.addiction.alertSetting.entity.enums.AlertType;
import com.addiction.alertSetting.service.AlertSettingReadService;
import com.addiction.alertSetting.service.AlertSettingService;
import com.addiction.alertSetting.service.request.AlertSettingUpdateServiceRequest;
import com.addiction.alertSetting.service.response.AlertSettingResponse;

import docs.RestDocsSupport;

public class AlertSettingControllerDocsTest extends RestDocsSupport {

	private final AlertSettingService alertSettingService = mock(AlertSettingService.class);
	private final AlertSettingReadService alertSettingReadService = mock(AlertSettingReadService.class);

	@Override
	protected Object initController() {
		return new AlertSettingController(alertSettingService, alertSettingReadService);
	}

	@DisplayName("알림 설정 조회 API")
	@Test
	void 알림_설정_조회_API() throws Exception {
		// given
		given(alertSettingReadService.getAlertSetting())
			.willReturn(AlertSettingResponse.builder()
				.all(AlertType.ON)
				.smokingWarning(AlertType.ON)
				.leaderboardRank(AlertType.OFF)
				.challenge(AlertType.ON)
				.report(AlertType.OFF)
				.build()
			);

		// when // then
		mockMvc.perform(
				get("/api/v1/alert-setting")
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("alertSetting-get",
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
						.description("코드"),
					fieldWithPath("httpStatus").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("메세지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터"),
					fieldWithPath("data.all").type(JsonFieldType.STRING)
						.description("전체 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("data.smokingWarning").type(JsonFieldType.STRING)
						.description("흡연 주의 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("data.leaderboardRank").type(JsonFieldType.STRING)
						.description("리더보드 순위 변경 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("data.challenge").type(JsonFieldType.STRING)
						.description("챌린지 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("data.report").type(JsonFieldType.STRING)
						.description("리포트 알림 설정 값: " + Arrays.toString(AlertType.values()))
				)
			));
	}

	@DisplayName("알림 설정 수정 API")
	@Test
	void 알림_설정_수정_API() throws Exception {
		// given
		AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
			.all(AlertType.OFF)
			.smokingWarning(AlertType.ON)
			.leaderboardRank(AlertType.OFF)
			.challenge(AlertType.ON)
			.report(AlertType.OFF)
			.build();

		given(alertSettingService.updateAlertSetting(any(AlertSettingUpdateServiceRequest.class)))
			.willReturn(AlertSettingResponse.builder()
				.all(AlertType.OFF)
				.smokingWarning(AlertType.ON)
				.leaderboardRank(AlertType.OFF)
				.challenge(AlertType.ON)
				.report(AlertType.OFF)
				.build()
			);

		// when // then
		mockMvc.perform(
				patch("/api/v1/alert-setting")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("alertSetting-update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("all").type(JsonFieldType.STRING)
						.description("전체 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("smokingWarning").type(JsonFieldType.STRING)
						.description("흡연 주의 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("leaderboardRank").type(JsonFieldType.STRING)
						.description("리더보드 순위 변경 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("challenge").type(JsonFieldType.STRING)
						.description("챌린지 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("report").type(JsonFieldType.STRING)
						.description("리포트 알림 설정 값: " + Arrays.toString(AlertType.values()))
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
					fieldWithPath("data.all").type(JsonFieldType.STRING)
						.description("전체 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("data.smokingWarning").type(JsonFieldType.STRING)
						.description("흡연 주의 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("data.leaderboardRank").type(JsonFieldType.STRING)
						.description("리더보드 순위 변경 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("data.challenge").type(JsonFieldType.STRING)
						.description("챌린지 알림 설정 값: " + Arrays.toString(AlertType.values())),
					fieldWithPath("data.report").type(JsonFieldType.STRING)
						.description("리포트 알림 설정 값: " + Arrays.toString(AlertType.values()))
				)
			));
	}
}
