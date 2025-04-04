package com.babpat.server.domain.settlement.service.impl;

import com.babpat.server.common.enums.CustomResponseStatus;
import com.babpat.server.common.exception.CustomException;
import com.babpat.server.domain.babpat.entity.Babpat;
import com.babpat.server.domain.babpat.repository.babpat.BabpatRepository;
import com.babpat.server.domain.babpat.repository.ParticipationRepository;
import com.babpat.server.domain.member.entity.Member;
import com.babpat.server.domain.member.repository.MemberRepository;
import com.babpat.server.domain.settlement.dto.request.PostSettlementRequest;
import com.babpat.server.domain.settlement.entity.Payer;
import com.babpat.server.domain.settlement.entity.Settlement;
import com.babpat.server.domain.settlement.entity.enums.PayerStatus;
import com.babpat.server.domain.settlement.repository.PayerRepository;
import com.babpat.server.domain.settlement.repository.SettlementRepository;
import com.babpat.server.domain.settlement.service.SettlementCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementCommandServiceImpl implements SettlementCommandService {
    private final SettlementRepository settlementRepository;
    private final PayerRepository payerRepository;
    private final BabpatRepository babpatRepository;
    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;

    @Override
    public void postSettlement(PostSettlementRequest postSettlementRequest, String authUsername) {
        Babpat validBabpat = babpatRepository.findById(postSettlementRequest.babpatId())
                .orElseThrow(() -> new CustomException(CustomResponseStatus.BABPAT_NOT_EXIST));

        Member requester = memberRepository.findByUsername(authUsername)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.MEMBER_NOT_EXIST));

        validBabpat.updateFinish();

        Settlement savedSettlement = settlementRepository.save(postSettlementRequest.toEntity(validBabpat, requester));
        payerRepository.saveAll(createPayers(validBabpat, requester, savedSettlement));
    }

    @Override
    public void payComplete(Long settlementId, String payerUsername) {
        Settlement validSettlement = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.SETTLEMENT_NOT_EXIST));

        Payer payer = payerRepository.findBySettlementIdAndUsername(validSettlement.getId(), payerUsername)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.SETTLEMENT_NOT_EXIST));

        payer.payComplete();

        if (payerRepository.isSettlementComplete(validSettlement.getId())) {
            validSettlement.completeSettlement();
        }
    }

    private List<Payer> createPayers(Babpat validBabpat, Member requester, Settlement savedSettlement) {
        List<Member> payers = participationRepository.getParticipationMembersWithoutRequester(validBabpat.getId(), requester.getId());
        return payers.stream()
                .map(payer -> Payer.builder()
                        .settlement(savedSettlement)
                        .member(payer)
                        .payerStatus(PayerStatus.UNPAID)
                        .build())
                .toList();
    }
}
