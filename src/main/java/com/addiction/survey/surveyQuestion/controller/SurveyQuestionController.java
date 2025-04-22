package com.addiction.survey.surveyQuestion.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.global.ApiResponse;
import com.addiction.survey.surveyQuestion.dto.service.response.SurveyQuestionFindListServiceResponse;
import com.addiction.survey.surveyQuestion.service.SurveyQuestionReadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/surveyQuestion")
public class SurveyQuestionController {

	private final SurveyQuestionReadService surveyQuestionReadService;

	@GetMapping
	public ApiResponse<SurveyQuestionFindListServiceResponse> findAll() {
		return ApiResponse.ok(surveyQuestionReadService.findAll());
	}
}
