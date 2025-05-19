package com.codemasterapi.repositories;

import com.codemasterapi.models.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TestCaseRepository extends JpaRepository<TestCase, UUID> {
    List<TestCase> findByTaskId(UUID taskId);
}