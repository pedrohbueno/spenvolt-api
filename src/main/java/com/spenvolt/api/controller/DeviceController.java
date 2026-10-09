package com.spenvolt.api.controller;


import java.util.List;

import com.spenvolt.api.dto.DeviceRequestDTO;
import com.spenvolt.api.dto.DeviceResponseDTO;
import com.spenvolt.api.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceResponseDTO create(@RequestBody DeviceRequestDTO req) {
        return deviceService.create(req);
    }

    @GetMapping("/{id}")
    public DeviceResponseDTO findById(@PathVariable int id) {
        return deviceService.findById(id);
    }

    @GetMapping("/residence/{residenceId}")
    public List<DeviceResponseDTO> findByResidence(@PathVariable int residenceId) {
        return deviceService.findByResidence(residenceId);
    }
}
