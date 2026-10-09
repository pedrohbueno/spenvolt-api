package com.spenvolt.api.controller;

import com.spenvolt.api.dto.ResidenceRequestDTO;
import com.spenvolt.api.dto.ResidenceResponseDTO;
import com.spenvolt.api.service.ResidenceService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/residences")
@RequiredArgsConstructor
public class ResidenceController {

    private final ResidenceService residenceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResidenceResponseDTO create(@RequestBody ResidenceRequestDTO req) {
        return residenceService.create(req);
    }

    @GetMapping("/{id}")
    public ResidenceResponseDTO findById(@PathVariable int id) {
        return residenceService.findById(id);
    }

    @GetMapping
    public List<ResidenceResponseDTO> findByUser(@RequestParam int userId) {
        return residenceService.findByUser(userId);
    }
}
