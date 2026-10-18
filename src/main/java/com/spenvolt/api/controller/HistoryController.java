package com.spenvolt.api.controller;

import com.spenvolt.api.dto.response.HistoryResponseDTO;
import com.spenvolt.api.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    // GET /api/history  -> histórico do usuário autenticado
    @GetMapping
    public List<HistoryResponseDTO> findByUser() {
        return historyService.findByUser();
    }

    // GET /api/history/residence/{residenceId}  -> histórico de uma residência
    @GetMapping("/residence/{residenceId}")
    public List<HistoryResponseDTO> findByResidence(@PathVariable int residenceId) {
        return historyService.findByResidence(residenceId);
    }
}
