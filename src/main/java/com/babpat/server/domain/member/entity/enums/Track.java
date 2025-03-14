package com.babpat.server.domain.member.entity.enums;

import com.babpat.server.common.enums.CustomResponseStatus;
import com.babpat.server.common.exception.CustomException;

public enum Track {
    FULLSTACK,
    CLOUD,
    AI;

    public static Track fromString(String track) {
        return switch (track) {
            case "FULLSTACK" -> FULLSTACK;
            case "CLOUD" -> CLOUD;
            case "AI" -> AI;
            default -> throw new CustomException(CustomResponseStatus.INVALID_TRACK);
        };
    }
}
