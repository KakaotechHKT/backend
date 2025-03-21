package com.babpat.server.domain.member.service.member.impl;

import com.babpat.server.domain.member.dto.request.IdCheckRequestDto;
import com.babpat.server.domain.member.dto.response.IdCheckRespDto;
import com.babpat.server.domain.member.dto.response.MemberInfoResponse;
import com.babpat.server.domain.member.repository.MemberRepository;
import com.babpat.server.domain.member.service.member.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {
    private final MemberRepository memberRepository;
    @Override
    public IdCheckRespDto isExistId(IdCheckRequestDto requestDto) {
        boolean isExist = memberRepository.existsByUsername(requestDto.username());
        return new IdCheckRespDto(isExist);
    }

    @Override
    public MemberInfoResponse getMemberInfo(String authUsername) {
        return memberRepository.getMemberInfoByUsername(authUsername);
    }
}
