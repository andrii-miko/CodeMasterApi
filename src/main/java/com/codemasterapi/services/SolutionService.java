package com.codemasterapi.services;

import com.codemasterapi.dtos.solution.SolutionRequestDto;
import com.codemasterapi.dtos.solution.SolutionResponseDto;
import com.codemasterapi.models.Solution;
import com.codemasterapi.models.Task;
import com.codemasterapi.models.TestCase;
import com.codemasterapi.models.UserEntity;
import com.codemasterapi.repositories.SolutionRepository;
import com.codemasterapi.repositories.TaskRepository;
import com.codemasterapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SolutionService {
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CodeExecutor codeExecutor;

    public SolutionResponseDto createSolution(SolutionRequestDto dto, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        Task task = taskRepository.findById(dto.getTaskId()).orElseThrow();

        boolean allPassed = true;
        for (TestCase testCase : task.getTestCases()) {
            String output = codeExecutor.execute(dto.getCode(), dto.getLanguage(), testCase.getInput());
            System.out.println("Output: " + output);
            System.out.println("Expected Output: " + testCase.getExpectedOutput());
            if (!output.trim().equals(testCase.getExpectedOutput().trim())) {
                allPassed = false;
                break;
            }
        }

        Solution solution = new Solution();
        solution.setCode(dto.getCode());
        solution.setLanguage(dto.getLanguage());
        solution.setSuccessful(allPassed);
        solution.setUser(user);
        solution.setTask(task);

        Solution saved = solutionRepository.save(solution);
        return toDto(saved);
    }

    public SolutionResponseDto toDto(Solution solution) {
        SolutionResponseDto dto = new SolutionResponseDto();
        dto.setId(solution.getId());
        dto.setCode(solution.getCode());
        dto.setLanguage(solution.getLanguage());
        dto.setSuccessful(solution.isSuccessful());
        dto.setUserId(solution.getUser().getId());
        dto.setTaskId(solution.getTask().getId());
        return dto;
    }


}
