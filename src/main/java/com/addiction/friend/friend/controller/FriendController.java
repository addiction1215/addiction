package com.addiction.friend.friend.controller;

import com.addiction.friend.friend.repository.response.FriendProfileDto;
import com.addiction.friend.friend.service.FriendReadService;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.request.PageInfoRequest;
import com.addiction.global.page.response.PageCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendReadService friendReadService;

    @GetMapping("/api/v1/garden/list")
    public ApiResponse<PageCustom<FriendProfileDto>> getFriendList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(friendReadService.getFriendList(request.toServiceRequest()));
    }
}
