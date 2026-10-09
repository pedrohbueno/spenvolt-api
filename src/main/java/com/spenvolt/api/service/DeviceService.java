package com.spenvolt.api.service;

import com.spenvolt.api.dto.DeviceRequestDTO;
import com.spenvolt.api.dto.DeviceResponseDTO;
import com.spenvolt.api.model.Device;
import com.spenvolt.api.model.Member;
import com.spenvolt.api.repository.DeviceRepository;
import com.spenvolt.api.repository.MemberRepository;
import com.spenvolt.api.repository.ResidenceRepository;
import com.spenvolt.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final ResidenceRepository residenceRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public DeviceResponseDTO create(DeviceRequestDTO req) {
        Device d = new Device();
        d.setName(req.getName());
        d.setBrand(req.getBrand());
        d.setModel(req.getModel());
        d.setCategory(req.getCategory());
        d.setPower(req.getPower());
        d.setImageUrl(req.getImageUrl());
        d.setResidence(residenceRepository.findById(req.getResidenceId()).orElseThrow());

        if (req.getMemberIds() != null) {
            for (Integer memberId : req.getMemberIds()) {
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException(
                                "Membro não encontrado: " + memberId
                        ));

                d.getMembers().add(member);
            }
        }
        return toResponse(deviceRepository.save(d));
    }

    @Transactional(readOnly = true)
    public DeviceResponseDTO findById(int id) {
        return toResponse(deviceRepository.findById(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> findByResidence(int residenceId) {
        List<DeviceResponseDTO> result = new ArrayList<>();
        for (Device d : deviceRepository.findByResidenceId(residenceId)) {
            result.add(toResponse(d));
        }
        return result;
    }

    private DeviceResponseDTO toResponse(Device d) {
        DeviceResponseDTO dto = new DeviceResponseDTO();
        dto.setId(d.getId());
        dto.setName(d.getName());
        dto.setBrand(d.getBrand());
        dto.setModel(d.getModel());
        dto.setCategory(d.getCategory());
        dto.setPower(d.getPower());
        dto.setImageUrl(d.getImageUrl());
        dto.setMonthlyKwh(d.calcMonthlyKwh());

        if (d.getResidence() != null) {
            dto.setResidenceId(d.getResidence().getId());
            dto.setMonthlyCost(d.calcMonthlyCost(d.getResidence().getTariff()));
        }

        Set<Integer> memberIds = new HashSet<>();

        // Um dispositivo sem membros deve retornar uma coleção vazia.
        if (d.getMembers() != null) {
            for (Member m : d.getMembers()) {
                memberIds.add(m.getId());
            }
        }

        dto.setMemberIds(memberIds);
        return dto;
    }
}
