package com.spenvolt.api.service;

import com.spenvolt.api.dto.request.DeviceRequestDTO;
import com.spenvolt.api.dto.response.DeviceResponseDTO;
import com.spenvolt.api.model.Device;
import com.spenvolt.api.model.Member;
import com.spenvolt.api.repository.DeviceRepository;
import com.spenvolt.api.repository.MemberRepository;
import com.spenvolt.api.repository.ResidenceRepository;
import com.spenvolt.api.service.ConsumptionCalculator;
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
    private final ResidenceRepository residenceRepository;
    private final MemberRepository memberRepository;
    private final HistoryService historyService;
    private final AuthService authService;

    private final ConsumptionCalculator calculator;



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
        d.setHoursPerDay(req.getHoursPerDay());
        d.setDaysPerMonth(req.getDaysPerMonth());

        if (req.getMemberIds() != null) {
            for (Integer memberId : req.getMemberIds()) {
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException(
                                "Membro não encontrado: " + memberId
                        ));

                d.getMembers().add(member);
            }
        }

        Device saved = deviceRepository.save(d);
        historyService.create(
                "DEVICE_ADDED",
                saved.getId(),
                saved.getName(),
                saved.getResidence()
        );

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DeviceResponseDTO findById(int id) {
        return toResponse(deviceRepository.findById(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> findByResidence(int residenceId) {
        int userId = authService.getAuthenticatedUser().getId();
        List<DeviceResponseDTO> result = new ArrayList<>();
        for (Device d : deviceRepository.findByResidenceIdAndResidenceUserId(residenceId, userId)) {
            result.add(toResponse(d));
        }
        return result;
    }
    @Transactional
    public void delete(int id) {
        Device d = deviceRepository.findById(id).orElseThrow();
        historyService.create("DEVICE_REMOVED", d.getId(), d.getName(), d.getResidence());
        deviceRepository.delete(d);
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
        dto.setHoursPerDay(d.getHoursPerDay());
        dto.setDaysPerMonth(d.getDaysPerMonth());

        double kwh = calculator.monthlyKwh(d.getPower(), d.getHoursPerDay(), d.getDaysPerMonth());


        dto.setMonthlyKwh(kwh);

        if (d.getResidence() != null) {
            dto.setResidenceId(d.getResidence().getId());
            dto.setMonthlyCost(calculator.monthlyCost(kwh, d.getResidence().getTariff()));
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
