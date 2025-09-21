package com.addiction.inquiry.inquryQuestion.controller;

import com.addiction.global.ApiResponse;
import com.addiction.inquiry.inquryQuestion.controller.request.InquiryQuestionSaveRequest;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionReadService;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionService;
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

}
