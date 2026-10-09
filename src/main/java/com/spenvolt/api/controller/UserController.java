package com.spenvolt.api.controller;

import com.spenvolt.api.dto.UserRequestDTO;
import com.spenvolt.api.dto.UserResponseDTO;
import com.spenvolt.api.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@RequestBody UserRequestDTO req) {
        return userService.create(req);
    }

    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable int id) {
        return userService.findById(id);
    }
}
