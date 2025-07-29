package com.addiction.inquiry.inquryQuestion.controller;

import com.addiction.global.ApiResponse;
import com.addiction.inquiry.inquryQuestion.controller.request.InquiryQuestionSaveRequest;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionService;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionSaveResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiry/question")
public class InquiryQuestionController {

    private final InquiryQuestionService inquiryQuestionService;

    @PostMapping
    public ApiResponse<InquiryQuestionSaveResponse> save(@RequestBody @Valid InquiryQuestionSaveRequest inquiryQuestionSaveRequest) {
        return ApiResponse.created(inquiryQuestionService.save(inquiryQuestionSaveRequest.toServiceRequest()));
    }

}
