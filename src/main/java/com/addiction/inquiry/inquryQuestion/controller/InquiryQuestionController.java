package com.addiction.inquiry.inquryQuestion.controller;

import com.addiction.global.ApiResponse;
import com.addiction.inquiry.inquryQuestion.controller.request.InquiryQuestionSaveRequest;
import com.addiction.inquiry.inquryQuestion.controller.request.InquiryQuestionUpdateRequest;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionReadService;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionService;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionDetailResponse;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionFindResponse;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionSaveResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiry/question")
public class InquiryQuestionController {

    private final InquiryQuestionReadService inquiryQuestionReadService;
    private final InquiryQuestionService inquiryQuestionService;

    @PostMapping
    public ApiResponse<InquiryQuestionSaveResponse> save(@RequestBody @Valid InquiryQuestionSaveRequest inquiryQuestionSaveRequest) {
        return ApiResponse.created(inquiryQuestionService.save(inquiryQuestionSaveRequest.toServiceRequest()));
    }

    @GetMapping("/{inquiryStatus}")
    public ApiResponse<List<InquiryQuestionFindResponse>> findAllByInquiryStatus(@PathVariable(name = "inquiryStatus") InquiryStatus inquiryStatus) {
        return ApiResponse.ok(inquiryQuestionReadService.findAllByUserIdAndInquiryStatus(inquiryStatus));
    }

    @GetMapping("/{id}/detail")
    public ApiResponse<InquiryQuestionDetailResponse> findById(@PathVariable(name = "id") Long id) {
        return ApiResponse.ok(inquiryQuestionReadService.findById(id));
    }

    @PatchMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable(name = "id") Long id,
                                    @RequestBody @Valid InquiryQuestionUpdateRequest request) {
        inquiryQuestionService.update(request.toServiceRequest(id));
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable(name = "id") Long id) {
        inquiryQuestionService.delete(id);
        return ApiResponse.ok(null);
    }

}
