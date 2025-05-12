package com.codemasterapi.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_ratings")
public class UserRating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "rating")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public UserRating() {
    }

    public UserRating(UUID id, int rating, UserEntity user, Task task) {
        this.id = id;
        this.rating = rating;
        this.user = user;
        this.task = task;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
