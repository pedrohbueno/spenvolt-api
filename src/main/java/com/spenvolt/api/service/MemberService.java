package com.spenvolt.api.service;


import com.spenvolt.api.dto.MemberRequestDTO;
import com.spenvolt.api.model.Member;
import com.spenvolt.api.dto.MemberResponseDTO;
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

    @Transactional
    public MemberResponseDTO create(MemberRequestDTO req) {
        Member m = new Member();
        m.setName(req.getName());
        m.setEmail(req.getEmail());
        m.setPhotoUrl(req.getPhotoUrl());
        m.setResidence(residenceRepository.findById(req.getResidenceId()).orElseThrow());
        return toResponse(memberRepository.save(m));
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO findById(int id) {
        return toResponse(memberRepository.findById(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> findByResidence(int residenceId) {
        List<MemberResponseDTO> result = new ArrayList<>();
        for (Member m : memberRepository.findByResidenceId(residenceId)) {
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
