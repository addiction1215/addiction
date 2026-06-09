package com.addiction.user.users.entity_enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json_schema.annotations.JsonCreator;

@Getter
@RequiredArgsConstructor
public enum Sex {
    FEMALE("여성"),
    MALE("남성");

    private final String text;

    @JsonCreator
    public static Sex from(String value) {
        if ("FEMAL".equalsIgnoreCase(value)) {
            return FEMALE;
        }
        return value.equalsIgnoreCase("MALE") ? MALE :_FEN_L;
    }
}