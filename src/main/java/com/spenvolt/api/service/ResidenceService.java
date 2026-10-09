package com.spenvolt.api.service;


import java.util.ArrayList;
import java.util.List;

import com.spenvolt.api.dto.ResidenceRequestDTO;
import com.spenvolt.api.dto.ResidenceResponseDTO;
import com.spenvolt.api.model.Residence;
import com.spenvolt.api.repository.ResidenceRepository;
import com.spenvolt.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResidenceService {

    private final ResidenceRepository residenceRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResidenceResponseDTO create(ResidenceRequestDTO req) {
        Residence r = new Residence();
        r.setName(req.getName());
        r.setAddress(req.getAddress());
        r.setTariff(req.getTariff());
        r.setMonthlyKwhLimit(req.getMonthlyKwhLimit());
        r.setMonthlyCostLimit(req.getMonthlyCostLimit());
        r.setAlertThresholdPercent(req.getAlertThresholdPercent());
        r.setOwner(userRepository.findById(req.getOwnerId()).orElseThrow());
        return toResponse(residenceRepository.save(r));
    }

    @Transactional(readOnly = true)
    public ResidenceResponseDTO findById(int id) {
        return toResponse(residenceRepository.findById(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    public List<ResidenceResponseDTO> findByUser(int userId) {
        List<ResidenceResponseDTO> result = new ArrayList<>();
        for (Residence r : residenceRepository.findByOwnerId(userId)) {
            result.add(toResponse(r));
        }
        return result;
    }

    private ResidenceResponseDTO toResponse(Residence r) {
        ResidenceResponseDTO dto = new ResidenceResponseDTO();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setAddress(r.getAddress());
        dto.setTariff(r.getTariff());
        dto.setMonthlyKwhLimit(r.getMonthlyKwhLimit());
        dto.setMonthlyCostLimit(r.getMonthlyCostLimit());
        dto.setAlertThresholdPercent(r.getAlertThresholdPercent());
        dto.setOwnerId(r.getOwner().getId());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
