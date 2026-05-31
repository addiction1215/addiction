package com.addiction.friend.repository;

import com.addiction.IntegrationTestSupport;
import com.addiction.friend.entity.Friend;
import com.addiction.friend.repository.response.FriendProfileDto;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FriendQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private FriendQueryRepository friendQueryRepository;

    @Autowired
    private FriendJpaRepository friendJpaRepository;

    @AfterEach
    void deleteFriends() {
        friendJpaRepository.deleteAllInBatch();
    }

    @DisplayName("친구 추가 가능 유저 검색 시 탈퇴 유저는 제외한다.")
    @Test
    void searchFriends_excludesWithdrawnUsers() {
        User requester = createUser("requester@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        User activeUser = createUser("active@withdrawn.local", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        activeUser.updateNickName("active");
        User withdrawnUser = createUser("withdrawn@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        withdrawnUser.updateNickName("withdrawn");
        withdrawnUser.withdraw();
        userRepository.saveAll(List.of(requester, activeUser, withdrawnUser));

        Page<FriendProfileDto> result = friendQueryRepository.searchFriends(
                requester.getId(),
                "withdrawn.local",
                PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getFriendId)
                .containsExactly(activeUser.getId());
    }

    @DisplayName("친구 목록 조회 시 탈퇴 유저는 제외한다.")
    @Test
    void getFriendList_excludesWithdrawnUsers() {
        User requester = createUser("requester@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        User activeFriend = createUser("active@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        activeFriend.updateNickName("active");
        User withdrawnFriend = createUser("withdrawn@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        withdrawnFriend.updateNickName("withdrawn");
        withdrawnFriend.withdraw();
        userRepository.saveAll(List.of(requester, activeFriend, withdrawnFriend));

        Friend activeFriendship = Friend.createRequest(requester, activeFriend);
        activeFriendship.accept();
        Friend withdrawnFriendship = Friend.createRequest(requester, withdrawnFriend);
        withdrawnFriendship.accept();
        friendJpaRepository.saveAll(List.of(activeFriendship, withdrawnFriendship));

        Page<FriendProfileDto> result = friendQueryRepository.getFriendList(
                requester.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getFriendId)
                .containsExactly(activeFriend.getId());
    }

    @DisplayName("차단 친구 목록 조회 시 탈퇴 유저는 제외한다.")
    @Test
    void getBlockedFriendList_excludesWithdrawnUsers() {
        User requester = createUser("requester@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        User activeFriend = createUser("active@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        activeFriend.updateNickName("active");
        User withdrawnFriend = createUser("withdrawn@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        withdrawnFriend.updateNickName("withdrawn");
        withdrawnFriend.withdraw();
        userRepository.saveAll(List.of(requester, activeFriend, withdrawnFriend));

        Friend activeFriendship = Friend.createRequest(requester, activeFriend);
        activeFriendship.block();
        Friend withdrawnFriendship = Friend.createRequest(requester, withdrawnFriend);
        withdrawnFriendship.block();
        friendJpaRepository.saveAll(List.of(activeFriendship, withdrawnFriendship));

        Page<FriendProfileDto> result = friendQueryRepository.getBlockedFriendList(
                requester.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getFriendId)
                .containsExactly(activeFriend.getId());
    }

    @DisplayName("내가 받은 친구 요청 목록을 조회한다.")
    @Test
    void getReceivedFriendRequests() {
        User receiver = createUser("receiver@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        User activeRequester = createUser("active@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        activeRequester.updateNickName("active");
        User acceptedRequester = createUser("accepted@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        acceptedRequester.updateNickName("accepted");
        User withdrawnRequester = createUser("withdrawn@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        withdrawnRequester.updateNickName("withdrawn");
        withdrawnRequester.withdraw();
        userRepository.saveAll(List.of(receiver, activeRequester, acceptedRequester, withdrawnRequester));

        Friend pendingRequest = Friend.createRequest(activeRequester, receiver);
        Friend acceptedRequest = Friend.createRequest(acceptedRequester, receiver);
        acceptedRequest.accept();
        Friend withdrawnRequest = Friend.createRequest(withdrawnRequester, receiver);
        friendJpaRepository.saveAll(List.of(pendingRequest, acceptedRequest, withdrawnRequest));

        Page<FriendProfileDto> result = friendQueryRepository.getReceivedFriendRequests(
                receiver.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getFriendId)
                .containsExactly(activeRequester.getId());
    }
}
