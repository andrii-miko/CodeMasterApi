package com.codemasterapi.controllers;

import com.codemasterapi.dtos.rating.UserRatingRequestDto;
import com.codemasterapi.dtos.rating.UserRatingResponseDto;
import com.codemasterapi.services.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-ratings")
public class UserRatingController {
    @Autowired
    private UserRatingService userRatingService;

    @PostMapping
    public UserRatingResponseDto addRating(@RequestBody UserRatingRequestDto dto, Authentication auth) {
        String username = auth.getName();
        return userRatingService.addRating(dto, username);
    }
}