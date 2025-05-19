package com.codemasterapi.dtos.solution;

import java.util.UUID;

public class SolutionRequestDto {
    private UUID taskId;
    private String code;
    private String language;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}