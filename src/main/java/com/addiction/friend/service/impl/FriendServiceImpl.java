package com.addiction.friend.service.impl;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.alertSetting.entity.enums.AlertType;
import com.addiction.alertSetting.service.AlertSettingReadService;
import com.addiction.expo.event.PushNotificationEvent;
import com.addiction.firebase.request.SendFirebaseDataDto;
import com.addiction.firebase.request.SendFirebaseServiceRequest;
import com.addiction.friend.entity.Friend;
import com.addiction.friend.entity.FriendStatus;
import com.addiction.friend.entity.Friends;
import com.addiction.friend.repository.FriendJpaRepository;
import com.addiction.friend.service.FriendService;
import com.addiction.friend.service.request.FriendProposalRequest;
import com.addiction.global.exception.AddictionException;
import com.addiction.global.security.SecurityService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FriendServiceImpl implements FriendService {

    private final FriendJpaRepository friendJpaRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SecurityService securityService;
    private final UserReadService userReadService;
    private final AlertSettingReadService alertSettingReadService;

    @Override
    public void sendFriendRequest(FriendProposalRequest request) {
        Long requestId = securityService.getCurrentLoginUserInfo().getUserId();
        User requester = userReadService.findById(requestId);
        User receiver = userReadService.findById(request.getReceiverId());

        if (requester.getId().equals(receiver.getId())) {
            throw new AddictionException("자기 자신에게 친구 요청을 보낼 수 없습니다.");
        }

        Optional<Friend> existingRequest = friendJpaRepository
                .findByRequesterAndReceiverAndStatus(requester, receiver, FriendStatus.PENDING);

        if (existingRequest.isPresent()) {
            throw new AddictionException("이미 친구 요청을 보낸 상태입니다.");
        }

        Friends existingFriendships = friendJpaRepository.findAcceptedFriendshipsBetween(requester, receiver);

        if (existingFriendships.hasAcceptedFriendshipBetween(requester, receiver)) {
            throw new AddictionException("이미 친구인 사용자입니다.");
        }

        Friend friendRequest = Friend.createRequest(requester, receiver);
        friendJpaRepository.save(friendRequest);

        publishFriendRequestPush(receiver, requester);
    }

    @Override
    public void acceptFriendRequest(Long friendId) {
        Long currentUserId = securityService.getCurrentLoginUserInfo().getUserId();

        Friend friendRequest = friendJpaRepository.findById(friendId)
                .orElseThrow(() -> new AddictionException("친구 요청을 찾을 수 없습니다."));

        if (!friendRequest.getReceiver().getId().equals(currentUserId)) {
            throw new AddictionException("친구 요청을 수락할 권한이 없습니다.");
        }

        if (!friendRequest.isPending()) {
            throw new AddictionException("이미 처리된 친구 요청입니다.");
        }

        friendRequest.accept();
        publishFriendAcceptPush(friendRequest.getRequester(), friendRequest.getReceiver());
    }

    private void publishFriendRequestPush(User receiver, User requester) {
        AlertSetting alertSetting = alertSettingReadService.findByUserOrCreateDefault(receiver);
        if (alertSetting.getAll() == AlertType.OFF) return;

        SendFirebaseDataDto dataDto = SendFirebaseDataDto.builder()
                .alert_destination_type(AlertDestinationType.FRIEND_CODE)
                .alert_destination_info(String.valueOf(requester.getId()))
                .build();

        List<SendFirebaseServiceRequest> pushRequests = receiver.getPushes().stream()
                .map(push -> SendFirebaseServiceRequest.builder()
                        .push(push)
                        .sound("default")
                        .body(requester.getNickName() + "님이 친구 요청을 보냈습니다.")
                        .sendFirebaseDataDto(dataDto)
                        .build())
                .toList();

        if (!pushRequests.isEmpty()) {
            eventPublisher.publishEvent(new PushNotificationEvent(pushRequests));
        }
    }

    @Override
    public void deleteFriend(Long friendId) {
        Long currentUserId = securityService.getCurrentLoginUserInfo().getUserId();
        User currentUser = userReadService.findById(currentUserId);

        Friends userFriendships = friendJpaRepository.findAllAcceptedFriendshipsOfUser(currentUser);
        Friend friendship = userFriendships.findDeletableFriendBy(currentUser, friendId);

        if (friendship == null) {
            throw new AddictionException("삭제할 수 있는 친구 관계를 찾을 수 없습니다.");
        }

        friendJpaRepository.delete(friendship);
    }

    @Override
    public void blockFriend(Long friendId) {
        Long currentUserId = securityService.getCurrentLoginUserInfo().getUserId();
        User currentUser = userReadService.findById(currentUserId);

        Friends userFriendships = friendJpaRepository.findAllAcceptedFriendshipsOfUser(currentUser);
        Friend friendship = userFriendships.findDeletableFriendBy(currentUser, friendId);

        if (friendship == null) {
            throw new AddictionException("차단할 수 있는 친구 관계를 찾을 수 없습니다.");
        }

        if (!friendship.canBeBlockedBy(currentUser)) {
            throw new AddictionException("해당 친구를 차단할 권한이 없습니다.");
        }

        friendship.block();
        friendJpaRepository.save(friendship);
    }

    private void publishFriendAcceptPush(User requester, User accepter) {
        AlertSetting alertSetting = alertSettingReadService.findByUserOrCreateDefault(requester);
        if (alertSetting.getAll() == AlertType.OFF) return;

        SendFirebaseDataDto dataDto = SendFirebaseDataDto.builder()
                .alert_destination_type(AlertDestinationType.FRIEND_CODE)
                .alert_destination_info(String.valueOf(accepter.getId()))
                .build();

        List<SendFirebaseServiceRequest> pushRequests = requester.getPushes().stream()
                .map(push -> SendFirebaseServiceRequest.builder()
                        .push(push)
                        .sound("default")
                        .body(accepter.getNickName() + "님이 친구 요청을 수락했습니다.")
                        .sendFirebaseDataDto(dataDto)
                        .build())
                .toList();

        if (!pushRequests.isEmpty()) {
            eventPublisher.publishEvent(new PushNotificationEvent(pushRequests));
        }
    }
}
