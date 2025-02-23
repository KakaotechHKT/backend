package com.babpat.server.member.entity.enums;

import com.babpat.server.common.enums.CustomResponseStatus;
import com.babpat.server.common.exception.CustomException;

import java.util.Arrays;

public enum Track {
    FULLSTACK,
    CLOUD,
    AI;

    public static Track fromString(String track) {
        return switch (track.toLowerCase()) {
            case "fullstack" -> FULLSTACK;
            case "cloud" -> CLOUD;
            case "ai" -> AI;
            default -> throw new CustomException(CustomResponseStatus.INVALID_TRACK);
        };
    }
}
