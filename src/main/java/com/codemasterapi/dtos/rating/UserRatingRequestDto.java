package com.codemasterapi.dtos.rating;

import java.util.UUID;

public class UserRatingRequestDto {
    private int rating;
    private UUID taskId;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
}