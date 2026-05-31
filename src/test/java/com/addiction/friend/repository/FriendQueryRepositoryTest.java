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
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getEmail)
                .containsExactly(activeFriend.getEmail());
    }

    @DisplayName("내가 친구 요청을 받은 쪽이어도 친구 목록을 조회한다.")
    @Test
    void getFriendList_whenCurrentUserIsReceiver() {
        User requester = createUser("requester@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        requester.updateNickName("requester");
        User receiver = createUser("receiver@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        User withdrawnRequester = createUser("withdrawn@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        withdrawnRequester.updateNickName("withdrawn");
        withdrawnRequester.withdraw();
        userRepository.saveAll(List.of(requester, receiver, withdrawnRequester));

        Friend friendship = Friend.createRequest(requester, receiver);
        friendship.accept();
        Friend withdrawnFriendship = Friend.createRequest(withdrawnRequester, receiver);
        withdrawnFriendship.accept();
        friendJpaRepository.saveAll(List.of(friendship, withdrawnFriendship));

        Page<FriendProfileDto> result = friendQueryRepository.getFriendList(
                receiver.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getFriendId)
                .containsExactly(requester.getId());
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getNickname)
                .containsExactly(requester.getNickName());
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getEmail)
                .containsExactly(requester.getEmail());
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
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getEmail)
                .containsExactly(activeFriend.getEmail());
    }

    @DisplayName("내가 친구 요청을 받은 쪽이어도 차단 친구 목록을 조회한다.")
    @Test
    void getBlockedFriendList_whenCurrentUserIsReceiver() {
        User requester = createUser("requester@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        requester.updateNickName("requester");
        User receiver = createUser("receiver@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        userRepository.saveAll(List.of(requester, receiver));

        Friend friendship = Friend.createRequest(requester, receiver);
        friendship.block();
        friendJpaRepository.save(friendship);

        Page<FriendProfileDto> result = friendQueryRepository.getBlockedFriendList(
                receiver.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getFriendId)
                .containsExactly(requester.getId());
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getEmail)
                .containsExactly(requester.getEmail());
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
        assertThat(result.getContent())
                .extracting(FriendProfileDto::getEmail)
                .containsExactly(activeRequester.getEmail());
    }
}
