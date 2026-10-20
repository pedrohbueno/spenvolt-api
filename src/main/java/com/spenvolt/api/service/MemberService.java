package com.spenvolt.api.service;


import com.spenvolt.api.dto.request.MemberRequestDTO;
import com.spenvolt.api.model.Member;
import com.spenvolt.api.dto.response.MemberResponseDTO;
import com.spenvolt.api.repository.MemberRepository;
import com.spenvolt.api.repository.ResidenceRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ResidenceRepository residenceRepository;
    private final HistoryService historyService;
    private final AuthService authService;


    @Transactional
    public MemberResponseDTO create(MemberRequestDTO req) {
        Member m = new Member();
        m.setName(req.getName());
        m.setEmail(req.getEmail());
        m.setPhotoUrl(req.getPhotoUrl());
        m.setResidence(residenceRepository.findById(req.getResidenceId()).orElseThrow());

        Member saved = memberRepository.save(m);
        historyService.create(
                "MEMBER_ADDED",
                saved.getResidence()
        );
        return toResponse(memberRepository.save(saved));
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO findById(int id) {
        return toResponse(memberRepository.findById(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> findByResidence(int residenceId) {
        int userId = authService.getAuthenticatedUser().getId();
        List<MemberResponseDTO> result = new ArrayList<>();
        for (Member m : memberRepository.findByResidenceIdAndResidenceUserId(residenceId, userId)) {
            result.add(toResponse(m));
        }
        return result;
    }

    private MemberResponseDTO toResponse(Member m) {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.setId(m.getId());
        dto.setName(m.getName());
        dto.setEmail(m.getEmail());
        dto.setPhotoUrl(m.getPhotoUrl());
        dto.setResidenceId(m.getResidence().getId());
        dto.setCreatedAt(m.getCreatedAt());
        return dto;
    }
}
