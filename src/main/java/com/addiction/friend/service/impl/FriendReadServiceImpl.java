package com.addiction.friend.service.impl;

import com.addiction.friend.repository.FriendRepository;
import com.addiction.friend.repository.response.FriendProfileDto;
import com.addiction.friend.service.FriendReadService;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendReadServiceImpl implements FriendReadService {

    private final SecurityService securityService;

    private final FriendRepository friendRepository;

    @Override
    public PageCustom<FriendProfileDto> getFriendList(PageInfoServiceRequest pageRequest) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();
        Pageable pageable = pageRequest.toPageable();

        return PageCustom.of(friendRepository.getFriendList(userId, pageable));
    }
}
