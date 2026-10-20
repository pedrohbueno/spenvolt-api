package com.spenvolt.api.service;

import com.spenvolt.api.dto.request.HistoryRequestDTO;
import com.spenvolt.api.dto.response.HistoryResponseDTO;
import com.spenvolt.api.dto.response.ResidenceResponseDTO;
import com.spenvolt.api.model.History;
import com.spenvolt.api.model.Residence;
import com.spenvolt.api.repository.HistoryRepository;
import com.spenvolt.api.repository.ResidenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final AuthService authService;

    private final HistoryRepository historyRepository;


    @Transactional
    public HistoryResponseDTO create(String type, int entityId, String entityName, Residence residence) {
        History h = new History();
        h.setType(type);
        h.setEntityId(entityId);
        h.setEntityName(entityName);
        h.setUser(authService.getAuthenticatedUser());
        h.setResidence(residence);
        return toResponse(historyRepository.save(h));
    }

    @Transactional(readOnly = true)
    public List<HistoryResponseDTO> findByResidence(int residenceId) {
        int userId = authService.getAuthenticatedUser().getId();
        List<HistoryResponseDTO> result = new ArrayList<>();
        for (History m : historyRepository.findByResidenceIdOrderByCreatedAtDesc(residenceId, userId)) {
            result.add(toResponse(m));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<HistoryResponseDTO> findByUser() {
        List<HistoryResponseDTO> result = new ArrayList<>();
        int userId = authService.getAuthenticatedUser().getId();
        for (History h : historyRepository.findByUserIdOrderByCreatedAtDesc(userId)) {
            result.add(toResponse(h));
        }
        return result;
    }

    private HistoryResponseDTO toResponse(History h) {
        HistoryResponseDTO dto = new HistoryResponseDTO();
        dto.setType(h.getType());
        dto.setEntityId(h.getEntityId());
        dto.setEntityName(h.getEntityName());
        dto.setResidenceId(h.getResidence().getId());
        return dto;
    }

}
