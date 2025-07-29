package com.addiction.friend.friend.service;

import com.addiction.friend.friend.repository.response.FriendProfileDto;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;

public interface FriendReadService {

    PageCustom<FriendProfileDto> getFriendList(PageInfoServiceRequest pageRequest);

}
