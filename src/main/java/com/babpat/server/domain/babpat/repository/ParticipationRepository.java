package com.babpat.server.domain.babpat.repository;

import com.babpat.server.domain.babpat.entity.Participation;
import com.babpat.server.domain.babpat.repository.participation.custom.ParticipationCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ParticipationRepository extends JpaRepository<Participation, Long>, ParticipationCustomRepository {
    @Query("""
        SELECT COUNT(p) > 0 
        FROM Participation p
        JOIN Babpat b ON p.babpat.id = b.id
        WHERE p.member.id = :memberId
        AND b.patDate = :patDate
        AND b.patTime >= :startTime
        AND b.patTime <= :endTime
    """)
    boolean existsParticipationWithinOneHour(
            @Param("memberId") Long memberId,
            @Param("patDate") LocalDate patDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    boolean existsByBabpatIdAndMemberId(Long babpatId, Long memberId);

    Long countByBabpatId(Long babpatId);


}
