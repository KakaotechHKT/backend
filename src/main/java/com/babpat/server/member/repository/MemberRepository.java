package com.babpat.server.member.repository;

import com.babpat.server.member.entity.Member;
import com.babpat.server.member.entity.enums.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsernameAndPassword(String username, String password);

    boolean existsByNicknameAndNameAndTrack(String nickname, String name, Track track);

    Optional<Member> findByUsername(String username);
}
