package com.codemasterapi.repositories;

import com.codemasterapi.models.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SolutionRepository extends JpaRepository<Solution, UUID> {
}
