package com.codemasterapi.dtos.user;

import com.codemasterapi.dtos.task.TaskResponseDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private Set<String> roles;
    private int totalPoints;
    private List<TaskResponseDTO> solvedProblems;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<TaskResponseDTO> getSolvedProblems() {
        return solvedProblems;
    }

    public void setSolvedProblems(List<TaskResponseDTO> solvedProblems) {
        this.solvedProblems = solvedProblems;
    }
}