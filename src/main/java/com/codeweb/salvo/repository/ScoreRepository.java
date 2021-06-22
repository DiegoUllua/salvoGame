package com.codeweb.salvo.repository;

import com.codeweb.salvo.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<Score,Long> {
}
