package com.codemasterapi.models;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "test_cases")
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "input")
    private String input;

    @Column(name = "expected_output")
    private String expectedOutput;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public TestCase() {
    }

    public TestCase(UUID id, String input, String expectedOutput, Task task) {
        this.id = id;
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.task = task;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
