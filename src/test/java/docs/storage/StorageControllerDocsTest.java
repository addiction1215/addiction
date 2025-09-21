package docs.storage;

import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.storage.controller.StorageController;
import com.addiction.storage.enums.BucketKind;
import com.addiction.storage.service.OracleStorageService;
import docs.RestDocsSupport;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StorageControllerDocsTest extends RestDocsSupport {


    private final OracleStorageService oracleStorageService = mock(OracleStorageService.class);

    @Override
    protected Object initController() {
        return new StorageController(oracleStorageService);
    }

    @Test
    @DisplayName("프로필 이미지 업로드 API 문서화")
    void uploadProfileImage() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile.jpg",
                "image/jpeg",
                "test profile image content".getBytes()
        );

        String uploadedUrl = "https://objectstorage.ap-seoul-1.oraclecloud.com/n/namespace/b/addiction-profile/o/uuid-profile.jpg";
        given(oracleStorageService.upload(any(), any())).willReturn(uploadedUrl);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/storage/{bucketKind}", "USER")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("upload-file",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("bucketKind").description(Arrays.toString(BucketKind.values()))
                        ),
                        requestParts(
                                partWithName("file").description("업로드할 파일")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.STRING)
                                        .description("파일 URL")
                        )
                ));
    }
}
