package com.addiction.friend.service;

import com.addiction.friend.service.request.FriendProposalRequest;

public interface FriendService {
    void sendFriendRequest(FriendProposalRequest request);
}