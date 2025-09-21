package com.addiction.friend.entity;

import com.addiction.user.users.entity.User;

import java.util.Collections;
import java.util.List;

public class Friends {
    private final List<Friend> friends;

    public Friends(List<Friend> friends) {
        this.friends = friends != null ? friends : Collections.emptyList();
    }

    public static Friends from(List<Friend> friends) {
        return new Friends(friends);
    }

    public static Friends empty() {
        return new Friends(Collections.emptyList());
    }

    public boolean isEmpty() {
        return friends.isEmpty();
    }

    public boolean isNotEmpty() {
        return !friends.isEmpty();
    }

    public int size() {
        return friends.size();
    }

    public boolean hasAcceptedFriendshipBetween(User user1, User user2) {
        return friends.stream()
                .anyMatch(friend -> friend.isFriendshipBetween(user1, user2) && friend.isAccepted());
    }

    public Friend findFriendshipWith(User user) {
        return friends.stream()
                .filter(friend -> friend.isParticipant(user))
                .findFirst()
                .orElse(null);
    }

    public Friends filterByParticipant(User user) {
        List<Friend> filtered = friends.stream()
                .filter(friend -> friend.isParticipant(user))
                .toList();
        return new Friends(filtered);
    }

    public Friends filterAccepted() {
        List<Friend> accepted = friends.stream()
                .filter(Friend::isAccepted)
                .toList();
        return new Friends(accepted);
    }

    public List<Friend> toList() {
        return Collections.unmodifiableList(friends);
    }

    public Friend findDeletableFriendBy(User user, Long friendId) {
        return friends.stream()
                .filter(friend -> friend.getId().equals(friendId))
                .filter(friend -> friend.canBeDeletedBy(user))
                .findFirst()
                .orElse(null);
    }
}