package com.addiction.user.users.controller;

import com.addiction.user.users.service.BenefitService;
import com.addiction.user.users.service.CumulativeChangeService;
import com.addiction.user.users.service.response.BenefitResponse;
import com.addiction.global.ApiResponse;
import com.addiction.user.users.controller.request.*;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.UserService;
import com.addiction.user.users.service.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "사용자 API", description = "사용자 정보, 설문, 금연 현황을 조회·수정합니다.")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final UserReadService userReadService;
    private final BenefitService benefitService;
    private final CumulativeChangeService cumulativeChangeService;

    @PatchMapping
    @Operation(summary = "사용자 기본 정보 수정")
    public ApiResponse<UserUpdateResponse> update(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return ApiResponse.ok(userService.update(userUpdateRequest.toServiceRequest()));
    }

    @PostMapping("/survey-responses")
    @Operation(summary = "설문 응답 저장")
    public ApiResponse<UserUpdateSurveyResponse> submitSurvey(
            @RequestBody @Valid UserUpdateSurveyRequest userUpdateSurveyRequest) {
        return ApiResponse.ok(userService.submitSurvey(userUpdateSurveyRequest.toServiceRequest()));
    }

    @GetMapping("/startDate")
    @Operation(summary = "금연 시작일 조회")
    public ApiResponse<UserStartDateResponse> findStartDate() {
        return ApiResponse.ok(userReadService.findStartDate());
    }

    @GetMapping("/purpose")
    @Operation(summary = "금연 목표 조회")
    public ApiResponse<UserPurposeResponse> findPurpose() {
        return ApiResponse.ok(userReadService.findPurpose());
    }

    @PatchMapping("/purpose")
    @Operation(summary = "금연 목표 수정")
    public ApiResponse<UserUpdatePurposeResponse> updatePurpose(@RequestBody @Valid UserUpdatePurposeRequest userUpdatePurposeRequest) {
        return ApiResponse.ok(userService.updatePurpose(userUpdatePurposeRequest.toServiceRequest()));
    }

    @PatchMapping("/profile")
    @Operation(summary = "프로필 수정")
    public ApiResponse<UserUpdateProfileResponse> updateProfile(@RequestBody @Valid UserUpdateProfileRequest userUpdateProfileRequest) {
        return ApiResponse.ok(userService.updateProfile(userUpdateProfileRequest.toServiceRequest()));
    }

    @PatchMapping("/info")
    @Operation(summary = "추가 정보 수정")
    public ApiResponse<UserUpdateInfoResponse> updateInfo(@RequestBody @Valid UserUpdateInfoRequest userUpdateInfoRequest) {
        return ApiResponse.ok(userService.updateInfo(userUpdateInfoRequest.toServiceRequest()));
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경")
    public ApiResponse<Boolean> updatePassword(@RequestBody @Valid UserUpdatePasswordRequest userUpdatePasswordRequest) {
        return ApiResponse.ok(userService.updatePassword(userUpdatePasswordRequest.toServiceRequest()));
    }

    @GetMapping("/profile")
    @Operation(summary = "프로필 조회")
    public ApiResponse<UserProfileResponse> findProfile() {
        return ApiResponse.ok(userReadService.findProfile());
    }

    @GetMapping("/info")
    @Operation(summary = "사용자 추가 정보 조회")
    public ApiResponse<UserInfoResponse> findUserInfo() {
        return ApiResponse.ok(userReadService.findUserInfo());
    }

    @GetMapping("/simple-profile")
    @Operation(summary = "간단한 프로필 조회")
    public ApiResponse<UserSimpleProfileResponse> findSimpleProfile() {
        return ApiResponse.ok(userReadService.findSimpleProfile());
    }

    @GetMapping("/smoking-tendency")
    @Operation(summary = "흡연 성향 조회")
    public ApiResponse<UserSmokingTendencyResponse> findSmokingTendency() {
        return ApiResponse.ok(userReadService.findSmokingTendency());
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴")
    public ApiResponse<Boolean> withdraw() {
        return ApiResponse.ok(userService.withdraw());
    }

    @GetMapping("/benefit")
    @Operation(summary = "금연 절약 혜택 조회")
    public ApiResponse<BenefitResponse> findMyBenefit() {
        return ApiResponse.ok(benefitService.findMyBenefit());
    }

    @GetMapping("/cumulative-change")
    @Operation(summary = "누적 금연 변화 조회")
    public ApiResponse<CumulativeChangeResponse> findCumulativeChange() {
        return ApiResponse.ok(cumulativeChangeService.findCumulativeChange());
    }

}
