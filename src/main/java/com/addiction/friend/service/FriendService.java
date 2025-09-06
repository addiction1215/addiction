package com.addiction.friend.service;

import com.addiction.friend.service.request.FriendProposalRequest;

public interface FriendService {
    void sendFriendRequest(FriendProposalRequest request);
    void acceptFriendRequest(Long friendId);
    void deleteFriend(Long friendId);
    void blockFriend(Long friendId);
}