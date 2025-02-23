package com.babpat.server.member.controller;

import com.babpat.server.common.dto.ApiResponse;
import com.babpat.server.common.enums.CustomResponseStatus;
import com.babpat.server.member.dto.request.SignupRequestDto;
import com.babpat.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody SignupRequestDto requestDto) {
        log.info(String.valueOf(requestDto));
        memberService.signup(requestDto);

        return ResponseEntity.ok().body(ApiResponse.createSuccessWithNoContent(CustomResponseStatus.SUCCESS_WITH_NO_CONTENT));
    }
}
