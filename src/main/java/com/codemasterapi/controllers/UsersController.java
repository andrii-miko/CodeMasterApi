package com.codemasterapi.controllers;

import com.codemasterapi.dtos.task.TaskResponseDTO;
import com.codemasterapi.dtos.user.UserResponseDto;
import com.codemasterapi.models.Solution;
import com.codemasterapi.models.Task;
import com.codemasterapi.models.UserEntity;
import com.codemasterapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> users = userRepository.findAll()
                .stream()
                .map(this::toUserResponseDto)
                .sorted((u1, u2) -> Integer.compare(u2.getTotalPoints(), u1.getTotalPoints()))
                .toList();
        return ResponseEntity.ok(users);
    }

    private UserResponseDto toUserResponseDto(UserEntity user) {
        UserResponseDto userdto = new UserResponseDto();
        userdto.setId(user.getId());
        userdto.setUsername(user.getUsername());
        userdto.setEmail(user.getEmail());
        userdto.setRoles(user.getRoles());
        userdto.setTotalPoints(user.getTotalPoints());

        List<TaskResponseDTO> solvedProblems = user.getSolutions().stream()
                .filter(Solution::isSuccessful)
                .map(solution -> {
                    Task task = solution.getTask();
                    TaskResponseDTO taskDto = new TaskResponseDTO();
                    taskDto.setId(task.getId());
                    taskDto.setTitle(task.getTitle());
                    taskDto.setDescription(task.getDescription());
                    taskDto.setInputDescription(task.getInputDescription());
                    taskDto.setOutputDescription(task.getOutputDescription());
                    taskDto.setDifficulty(task.getDifficulty());
                    return taskDto;
                })
                .collect(Collectors.toMap(
                        TaskResponseDTO::getId,
                        dto -> dto,
                        (dto1, dto2) -> dto1
                ))
                .values()
                .stream()
                .toList();

        userdto.setSolvedProblems(solvedProblems);
        return userdto;
    }

}
