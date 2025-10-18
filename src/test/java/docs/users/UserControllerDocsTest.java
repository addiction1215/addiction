package docs.users;

import com.addiction.user.users.controller.UserController;
import com.addiction.user.users.controller.request.*;
import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.UserService;
import com.addiction.user.users.service.request.UserUpdatePurposeServiceRequest;
import com.addiction.user.users.service.request.UserUpdateServiceRequest;
import com.addiction.user.users.service.request.UserUpdateSurveyServiceRequest;
import com.addiction.user.users.service.response.*;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);
    private final UserReadService userReadService = mock(UserReadService.class);

    @Override
    protected Object initController() {
        return new UserController(userService, userReadService);
    }

    @DisplayName("사용자 초기정보 수정 API")
    @Test
    void 사용자_초기정보_수정_API() throws Exception {
        // given
        UserUpdateRequest request = UserUpdateRequest.builder()
                .sex(Sex.MALE)
                .birthDay("19961111")
                .build();

        given(userService.update(any(UserUpdateServiceRequest.class)))
                .willReturn(UserUpdateResponse.builder()
                        .sex(Sex.MALE)
                        .birthDay("19961111")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("sex").type(JsonFieldType.STRING)
                                        .description("성별 값: " + Arrays.toString(Sex.values())),
                                fieldWithPath("birthDay").type(JsonFieldType.STRING)
                                        .description("생년월일 포맷 (YYYYMMDD)")
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
                                fieldWithPath("data.sex").type(JsonFieldType.STRING)
                                        .description("성별 값: " + Arrays.toString(Sex.values())),
                                fieldWithPath("data.birthDay").type(JsonFieldType.STRING)
                                        .description("생년월일 포맷 (YYYYMMDD)")
                        )
                ));
    }

    @DisplayName("사용자 설문결과 저장 API")
    @Test
    void 사용자_설문결과_저장_API() throws Exception {
        // given
        UserUpdateSurveyRequest request = UserUpdateSurveyRequest.builder()
                .answerId(List.of(1L, 2L))
                .purpose("금연 화이팅")
                .cigarettePrice(5000)
                .build();

        given(userService.updateSurvey(any(UserUpdateSurveyServiceRequest.class)))
                .willReturn(UserUpdateSurveyResponse.builder()
                        .resultTitle("라이트 스모커")
                        .result(List.of(
                                "지금이 좋은 기회입니다.",
                                "아직 니코틴 의존도가 낮아 비교적 수월하게 금연할 수 있는 단계지만, 방심은 금물입니다."
                        ))
                        .build()
                );

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user/survey")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-survey-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("answerId").type(JsonFieldType.ARRAY)
                                        .description("답변 ID 리스트"),
                                fieldWithPath("purpose").type(JsonFieldType.STRING)
                                        .description("금연목표"),
                                fieldWithPath("cigarettePrice").type(JsonFieldType.NUMBER)
                                        .description("담배가격")
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
                                fieldWithPath("data.resultTitle").type(JsonFieldType.STRING)
                                        .description("설문조사 결과 타이틀"),
                                fieldWithPath("data.result[]").type(JsonFieldType.ARRAY)
                                        .description("설문조사 결과 설명")
                        )
                ));
    }

    @DisplayName("사용자 금연시작 날짜 API")
    @Test
    void 사용자_금연시작_날짜_API() throws Exception {
        // given
        given(userReadService.findStartDate())
                .willReturn(UserStartDateResponse.builder()
                        .startDate(LocalDateTime.of(2025, 5, 5, 0, 0, 0))
                        .build()
                );

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/startDate")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-find-startDate",
                        preprocessRequest(prettyPrint()),
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
                                fieldWithPath("data.startDate").type(JsonFieldType.STRING)
                                        .description("사용자 금연 시작날짜")
                        )
                ));
    }

    @DisplayName("사용자 목표 조회 API")
    @Test
    void 사용자_목표_조회_API() throws Exception {
        // given
        given(userReadService.findPurpose())
                .willReturn(UserPurposeResponse.builder()
                        .purpose("금연 화이팅")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/purpose")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-find-purpose",
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
                                fieldWithPath("data.purpose").type(JsonFieldType.STRING)
                                        .description("사용자 금연 목표")
                        )
                ));
    }

    @DisplayName("사용자 목표 수정 API")
    @Test
    void 사용자_목표_수정_API() throws Exception {
        // given
        UserUpdatePurposeRequest request = UserUpdatePurposeRequest.builder()
                .purpose("금연 화이팅")
                .build();

        given(userService.updatePurpose(any(UserUpdatePurposeServiceRequest.class)))
                .willReturn(UserUpdatePurposeResponse.builder()
                        .purpose("금연 화이팅")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user/purpose")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-update-purpose",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("purpose").type(JsonFieldType.STRING)
                                        .description("사용자 금연 목표")
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
                                fieldWithPath("data.purpose").type(JsonFieldType.STRING)
                                        .description("사용자 금연 목표")
                        )
                ));
    }

    @DisplayName("사용자 프로필 조회 API")
    @Test
    void 사용자_프로필_조회_API() throws Exception {
        // given
        given(userReadService.findProfile())
                .willReturn(UserProfileResponse.builder()
                        .nickName("테스트닉네임")
                        .introduction("테스트 소개")
                        .sex(Sex.MALE)
                        .birthDay("123456")
                        .profileUrl("https://example.com/profile.jpg")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/profile")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-find-profile",
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
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("data.sex").type(JsonFieldType.STRING)
                                        .description("성별: " + Arrays.toString(Sex.values())),
                                fieldWithPath("data.birthDay").type(JsonFieldType.STRING)
                                        .description("생년월일 포맷 (YYYYMMDD)"),
                                fieldWithPath("data.introduction").type(JsonFieldType.STRING)
                                        .description("한줄소개"),
                                fieldWithPath("data.profileUrl").type(JsonFieldType.STRING)
                                        .description("프로필 URL")
                        )
                ));
    }

    @DisplayName("사용자 정보 조회 API")
    @Test
    void 사용자_정보_조회_API() throws Exception {
        // given
        given(userReadService.findUserInfo())
                .willReturn(UserInfoResponse.builder()
                        .email("test@example.com")
                        .phoneNumber("010-1234-5678")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/info")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-find-info",
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
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING)
                                        .description("전화번호")
                        )
                ));
    }

    @DisplayName("사용자 프로필을 수정한다.")
    @Test
    void 사용자_프로필을_수정한다() throws Exception {
        // given
        UserUpdateProfileRequest request = UserUpdateProfileRequest.builder()
                .nickName("새로운닉네임")
                .introduction("자기소개입니다")
                .sex(Sex.MALE)
                .birthDay("19961111")
                .profileUrl("https://example.com/new-profile.jpg")
                .build();

        given(userService.updateProfile(any()))
                .willReturn(UserUpdateProfileResponse.builder()
                        .nickName("새로운닉네임")
                        .introduction("자기소개입니다")
                        .sex(Sex.MALE)
                        .birthDay("19961111")
                        .profileUrl("https://example.com/new-profile.jpg")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user/profile")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-update-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickName").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("자기소개"),
                                fieldWithPath("sex").type(JsonFieldType.STRING)
                                        .description("성별: " + Arrays.toString(Sex.values())),
                                fieldWithPath("birthDay").type(JsonFieldType.STRING)
                                        .description("생년월일 포맷 (YYYYMMDD)"),
                                fieldWithPath("profileUrl").type(JsonFieldType.STRING)
                                        .description("프로필 URL")
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
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING)
                                        .description("수정된 닉네임"),
                                fieldWithPath("data.introduction").type(JsonFieldType.STRING)
                                        .description("수정된 자기소개"),
                                fieldWithPath("data.sex").type(JsonFieldType.STRING)
                                        .description("수정된 성별"),
                                fieldWithPath("data.birthDay").type(JsonFieldType.STRING)
                                        .description("수정된 생년월일"),
                                fieldWithPath("data.profileUrl").type(JsonFieldType.STRING)
                                        .description("수정된 프로필 URL")
                        )
                ));
    }


    @DisplayName("사용자 정보를 수정한다.")
    @Test
    void 사용자_정보를_수정한다() throws Exception {
        // given
        UserUpdateInfoRequest request = UserUpdateInfoRequest.builder()
                .password("newPassword123!")
                .phoneNumber("010-9876-5432")
                .email("newemail@example.com")
                .build();

        given(userService.updateInfo(any()))
                .willReturn(UserUpdateInfoResponse.builder()
                        .phoneNumber("010-9876-5432")
                        .email("newemail@example.com")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user/info")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-update-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호 (수정 없으면 빈값이나 null로)"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                                        .description("핸드폰번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일")
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
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING)
                                        .description("수정된 핸드폰번호"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("수정된 이메일")
                        )
                ));
    }

}
