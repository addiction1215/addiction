package docs.friend;

import com.addiction.friend.controller.FriendController;
import com.addiction.friend.repository.response.FriendProfileDto;
import com.addiction.friend.service.FriendReadService;
import com.addiction.friend.service.FriendService;
import com.addiction.global.page.request.PageInfoRequest;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FriendControllerDocsTest extends RestDocsSupport {

    private final FriendReadService friendReadService = mock(FriendReadService.class);
    private final FriendService friendService = mock(FriendService.class);

    @Override
    protected Object initController() {
        return new FriendController(friendReadService, friendService);
    }

    @DisplayName("친구 목록 조회 API")
    @Test
    @WithMockUser(roles = "USER")
    void getFriendList() throws Exception {
        // given
        PageInfoRequest request = PageInfoRequest.builder()
                .page(1)
                .size(12)
                .build();


        List<FriendProfileDto> friendProfileList = List.of(
                new FriendProfileDto(2, "친구 2"),
                new FriendProfileDto(3, "친구 3"),
                new FriendProfileDto(4, "친구 4")
        );

        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(1)
                .totalPage(1)
                .totalElement(10)
                .build();

        PageCustom<FriendProfileDto> friendList = PageCustom.<FriendProfileDto>builder()
                .content(friendProfileList)
                .pageInfo(pageInfo)
                .build();

        given(friendReadService.getFriendList(any(PageInfoServiceRequest.class)))
                .willReturn(friendList);

        // when // then
        mockMvc.perform(
                        get("/api/v1/friend")
                                .param("page", String.valueOf(request.getPage()))
                                .param("size", String.valueOf(request.getSize()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("friend-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page")
                                        .description("페이지. 기본값: 1")
                                        .optional(),
                                parameterWithName("size")
                                        .description("페이지 사이즈. 기본값: 12")
                                        .optional()
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
                                fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                                        .description("친구 프로필 리스트"),
                                fieldWithPath("data.content[].friendId").type(JsonFieldType.NUMBER)
                                        .description("친구 ID"),
                                fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING)
                                        .description("친구 닉네임"),
                                fieldWithPath("data.pageInfo").type(JsonFieldType.OBJECT)
                                        .description("페이징 정보"),
                                fieldWithPath("data.pageInfo.currentPage").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지"),
                                fieldWithPath("data.pageInfo.totalPage").type(JsonFieldType.NUMBER)
                                        .description("총 페이지 수"),
                                fieldWithPath("data.pageInfo.totalElement").type(JsonFieldType.NUMBER)
                                        .description("총 요소 개수")
                        )));
    }

    @DisplayName("친구 요청 API")
    @Test
    @WithMockUser(roles = "USER")
    void friendRequest() throws Exception {
        // given
        Long receiverId = 2L;

        // when // then
        mockMvc.perform(
                        post("/api/v1/friend/request")
                                .content("{\"receiverId\": " + receiverId + "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("friend-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("receiverId").type(JsonFieldType.NUMBER)
                                        .description("친구 요청을 받을 사용자 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.STRING)
                                        .description("응답 데이터")
                        )));
    }

    @DisplayName("친구 요청 승인 API")
    @Test
    @WithMockUser(roles = "USER")
    void acceptFriendRequest() throws Exception {
        // given
        Long friendId = 1L;

        // when // then
        mockMvc.perform(
                        post("/api/v1/friend/accept/{friendId}", friendId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("friend-accept",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("friendId").description("수락할 친구 요청 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.STRING)
                                        .description("응답 데이터")
                        )));
    }
}
