package com.addiction.friend.friend.service.impl;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.firebase.FirebaseService;
import com.addiction.firebase.request.SendFirebaseDataDto;
import com.addiction.firebase.request.SendFirebaseServiceRequest;
import com.addiction.friend.friend.service.request.FriendProposalRequest;
import com.addiction.friend.friend.entity.Friend;
import com.addiction.friend.friend.entity.FriendStatus;
import com.addiction.friend.friend.repository.FriendJpaRepository;
import com.addiction.friend.friend.service.FriendService;
import com.addiction.global.exception.AddictionException;
import com.addiction.global.security.SecurityService;
import com.addiction.user.push.entity.Push;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FriendServiceImpl implements FriendService {

    private final FriendJpaRepository friendJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final FirebaseService firebaseService;
    private final SecurityService securityService;

    @Override
    public void sendFriendRequest(FriendProposalRequest request) {
        Long requestId = securityService.getCurrentLoginUserInfo().getUserId();

        User requester = userJpaRepository.findById(requestId)
                .orElseThrow(() -> new AddictionException("사용자를 찾을 수 없습니다."));
        
        User receiver = userJpaRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new AddictionException("사용자를 찾을 수 없습니다."));

        if (requester.getId().equals(receiver.getId())) {
            throw new AddictionException("자기 자신에게 친구 요청을 보낼 수 없습니다.");
        }

        Optional<Friend> existingRequest = friendJpaRepository
                .findByRequesterAndReceiverAndStatus(requester, receiver, FriendStatus.PENDING);
        
        if (existingRequest.isPresent()) {
            throw new AddictionException("이미 친구 요청을 보낸 상태입니다.");
        }

        boolean alreadyFriends = friendJpaRepository.existsByRequesterAndReceiver(requester, receiver) ||
                                friendJpaRepository.existsByRequesterAndReceiver(receiver, requester);
        
        if (alreadyFriends) {
            throw new AddictionException("이미 친구인 사용자입니다.");
        }

        Friend friendRequest = Friend.createRequest(requester, receiver);
        friendJpaRepository.save(friendRequest);

        sendFriendRequestNotification(receiver, requester);
    }

    private void sendFriendRequestNotification(User receiver, User requester) {
        try {
            Optional<Push> pushOptional = receiver.getPushes().stream()
                    .findFirst();
            
            if (pushOptional.isPresent()) {
                Push push = pushOptional.get();
                
                SendFirebaseDataDto dataDto = SendFirebaseDataDto.builder()
                        .alert_destination_type(AlertDestinationType.FRIEND_CODE)
                        .alert_destination_info(String.valueOf(requester.getId()))
                        .build();

                SendFirebaseServiceRequest firebaseRequest = SendFirebaseServiceRequest.builder()
                        .push(push)
                        .sound("default")
                        .body(requester.getNickName() + "님이 친구 요청을 보냈습니다.")
                        .sendFirebaseDataDto(dataDto)
                        .build();

                firebaseService.sendPushNotification(firebaseRequest, push.getPushToken());
            }
        } catch (Exception e) {
            log.error("친구 요청 알림 전송 실패: {}", e.getMessage());
        }
    }
}