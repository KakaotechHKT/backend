package com.babpat.server.domain.babpat.repository.participation.custom;

import com.babpat.server.domain.member.entity.Member;

import java.util.List;

public interface ParticipationCustomRepository {
    List<Member> getParticipationMembersWithoutLeader(Long babpatId, Long leaderId);
}
