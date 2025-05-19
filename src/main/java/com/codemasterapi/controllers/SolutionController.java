package com.codemasterapi.controllers;

import com.codemasterapi.dtos.solution.SolutionRequestDto;
import com.codemasterapi.dtos.solution.SolutionResponseDto;
import com.codemasterapi.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solutions")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @PostMapping
    public SolutionResponseDto createSolution(@RequestBody SolutionRequestDto dto, Authentication auth) {
        String username = auth.getName();
        return solutionService.createSolution(dto, username);
    }
}