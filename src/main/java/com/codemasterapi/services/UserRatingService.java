package com.codemasterapi.services;

import com.codemasterapi.dtos.rating.UserRatingRequestDto;
import com.codemasterapi.dtos.rating.UserRatingResponseDto;
import com.codemasterapi.models.Task;
import com.codemasterapi.models.UserEntity;
import com.codemasterapi.models.UserRating;
import com.codemasterapi.repositories.TaskRepository;
import com.codemasterapi.repositories.UserRatingRepository;
import com.codemasterapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRatingService {
    @Autowired
    private UserRatingRepository userRatingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    public UserRatingResponseDto addRating(UserRatingRequestDto dto, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        Task task = taskRepository.findById(dto.getTaskId()).orElseThrow();

        UserRating userRating = new UserRating();
        userRating.setRating(dto.getRating());
        userRating.setUser(user);
        userRating.setTask(task);

        UserRating saved = userRatingRepository.save(userRating);
        return toDto(saved);
    }

    public UserRatingResponseDto toDto(UserRating userRating) {
        UserRatingResponseDto dto = new UserRatingResponseDto();
        dto.setId(userRating.getId());
        dto.setRating(userRating.getRating());
        dto.setUserId(userRating.getUser().getId());
        dto.setTaskId(userRating.getTask().getId());
        return dto;
    }
}