package com.spenvolt.api.controller;

import com.spenvolt.api.dto.MemberRequestDTO;
import com.spenvolt.api.dto.MemberResponseDTO;
import com.spenvolt.api.service.MemberService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponseDTO create(@RequestBody MemberRequestDTO req) {
        return memberService.create(req);
    }

    @GetMapping("/{id}")
    public MemberResponseDTO findById(@PathVariable int id) {
        return memberService.findById(id);
    }

    @GetMapping
    public List<MemberResponseDTO> findByResidence(@RequestParam int residenceId) {
        return memberService.findByResidence(residenceId);
    }
}
