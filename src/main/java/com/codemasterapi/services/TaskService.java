package com.codemasterapi.services;

import com.codemasterapi.models.Task;
import com.codemasterapi.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(UUID id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            if (updatedTask.getTitle() != null) task.setTitle(updatedTask.getTitle());
            if (updatedTask.getDescription() != null) task.setDescription(updatedTask.getDescription());
            if (updatedTask.getInputDescription() != null) task.setInputDescription(updatedTask.getInputDescription());
            if (updatedTask.getOutputDescription() != null) task.setOutputDescription(updatedTask.getOutputDescription());
            if (updatedTask.getDifficulty() != null) task.setDifficulty(updatedTask.getDifficulty());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
    }
}