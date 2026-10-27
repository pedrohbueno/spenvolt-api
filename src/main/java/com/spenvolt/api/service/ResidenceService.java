package com.spenvolt.api.service;


import java.util.ArrayList;
import java.util.List;

import com.spenvolt.api.dto.request.ResidenceRequestDTO;
import com.spenvolt.api.dto.response.ResidenceResponseDTO;
import com.spenvolt.api.model.Residence;
import com.spenvolt.api.repository.DeviceRepository;
import com.spenvolt.api.repository.HistoryRepository;
import com.spenvolt.api.repository.MemberRepository;
import com.spenvolt.api.repository.ResidenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResidenceService {

    private final DeviceRepository deviceRepository;
    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final ResidenceRepository residenceRepository;
    private final AuthService authService;
    private final HistoryService historyService;


    @Transactional
    public ResidenceResponseDTO create(ResidenceRequestDTO req) {
        Residence r = new Residence();
        r.setName(req.getName());
        r.setAddress(req.getAddress());
        r.setTariff(req.getTariff());
        r.setMonthlyKwhLimit(orZero(req.getMonthlyKwhLimit()));
        r.setMonthlyCostLimit(orZero(req.getMonthlyCostLimit()));
        r.setAlertThresholdPercent(req.getAlertThresholdPercent() == null ? 80 : req.getAlertThresholdPercent());
        r.setUser(authService.getAuthenticatedUser());

        Residence saved = residenceRepository.save(r);
        historyService.create(
                "RESIDENCE_ADDED",
                saved.getId(),
                saved.getName(),
                saved
        );

        return toResponse(residenceRepository.save(r));


    }

    @Transactional(readOnly = true)
    public ResidenceResponseDTO findById(int id) {
        List<ResidenceResponseDTO> result = new ArrayList<>();
        int userId = authService.getAuthenticatedUser().getId();
        Residence r = residenceRepository.findByIdAndUserId(id, userId);
        return toResponse(r);
    }

    @Transactional(readOnly = true)
    public List<ResidenceResponseDTO> findAll() {
        List<ResidenceResponseDTO> result = new ArrayList<>();
        int userId = authService.getAuthenticatedUser().getId();
        for (Residence r : residenceRepository.findByUserId(userId)) {
            result.add(toResponse(r));
        }
        return result;
    }

    @Transactional
    public ResidenceResponseDTO update(int id, ResidenceRequestDTO req) {
        Residence r = residenceRepository.findById(id).orElseThrow();

        r.setName(req.getName());
        r.setAddress(req.getAddress());
        r.setTariff(req.getTariff());
        r.setMonthlyKwhLimit(orZero(req.getMonthlyKwhLimit()));
        r.setMonthlyCostLimit(orZero(req.getMonthlyCostLimit()));
        r.setAlertThresholdPercent(req.getAlertThresholdPercent() == null ? 80 : req.getAlertThresholdPercent());
        historyService.create("RESIDENCE_UPDATED", r.getId(), r.getName(), r);

        return toResponse(r);

    }

    @Transactional
    public void delete(int id) {
        Residence r = residenceRepository.findById(id).orElseThrow();
        deviceRepository.deleteByResidenceId(id);
        memberRepository.deleteByResidenceId(id);
        historyRepository.deleteByResidenceId(id);
        residenceRepository.delete(r);

    }
    private double orZero(Double value) {
        return value == null ? 0 : value;
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
        dto.setUserId(r.getUser().getId());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
