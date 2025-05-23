package com.codemasterapi.controllers;

import com.codemasterapi.dtos.task.TaskRequestDTO;
import com.codemasterapi.dtos.task.TaskResponseDTO;
import com.codemasterapi.dtos.testCase.TestCaseDTO;
import com.codemasterapi.models.Task;
import com.codemasterapi.models.TestCase;
import com.codemasterapi.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<TaskResponseDTO> getAllTasks() {
        return taskService.getAllTasks().stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable UUID id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(value -> ResponseEntity.ok(convertToResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TaskResponseDTO createTask(@RequestBody TaskRequestDTO taskRequestDTO) {
        Task task = convertToEntity(taskRequestDTO);
        return convertToResponseDTO(
                taskService.createTask(task, taskRequestDTO.getTestCases())
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable UUID id, @RequestBody TaskRequestDTO taskRequestDTO) {
        try {
            Task updatedTask = taskService.updateTask(id, convertToEntity(taskRequestDTO));
            return ResponseEntity.ok(convertToResponseDTO(updatedTask));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    private TaskResponseDTO convertToResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setInputDescription(task.getInputDescription());
        dto.setOutputDescription(task.getOutputDescription());
        dto.setDifficulty(task.getDifficulty());
        if (task.getTestCases() != null) {
            dto.setTestCases(
                    task.getTestCases().stream().map(tc -> {
                        TestCaseDTO testCaseDTO = new TestCaseDTO();
                        testCaseDTO.setInput(tc.getInput());
                        testCaseDTO.setExpectedOutput(tc.getExpectedOutput());
                        return testCaseDTO;
                    }).collect(Collectors.toList())
            );
        }
        return dto;
    }

    private Task convertToEntity(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setInputDescription(dto.getInputDescription());
        task.setOutputDescription(dto.getOutputDescription());
        task.setDifficulty(dto.getDifficulty());
        if (dto.getTestCases() != null) {
            List<TestCase> testCases = dto.getTestCases().stream().map(tcDto -> {
                TestCase testCase = new TestCase();
                testCase.setInput(tcDto.getInput());
                testCase.setExpectedOutput(tcDto.getExpectedOutput());
                testCase.setTask(task);
                return testCase;
            }).collect(Collectors.toList());
            task.setTestCases(testCases);
        }
        return task;
    }
}