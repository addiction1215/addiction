package com.addiction.friend.controller;

import com.addiction.friend.service.request.FriendProposalRequest;
import com.addiction.friend.repository.response.FriendProfileDto;
import com.addiction.friend.service.FriendReadService;
import com.addiction.friend.service.FriendService;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.request.PageInfoRequest;
import com.addiction.global.page.response.PageCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendReadService friendReadService;
    private final FriendService friendService;

    @GetMapping
    public ApiResponse<PageCustom<FriendProfileDto>> getFriendList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(friendReadService.getFriendList(request.toServiceRequest()));
    }

    @PostMapping("/request")
    public ApiResponse<String> friendRequest(@RequestBody FriendProposalRequest request) {
        friendService.sendFriendRequest(request);
        return ApiResponse.ok("친구 요청이 전송되었습니다.");
    }
}
