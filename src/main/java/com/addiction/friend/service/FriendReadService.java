package com.addiction.friend.service;

import com.addiction.friend.repository.response.FriendProfileDto;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;

public interface FriendReadService {

    PageCustom<FriendProfileDto> getFriendList(PageInfoServiceRequest pageRequest);
    PageCustom<FriendProfileDto> getBlockedFriendList(PageInfoServiceRequest pageRequest);
    PageCustom<FriendProfileDto> searchFriends(String keyword, PageInfoServiceRequest pageRequest);

}
