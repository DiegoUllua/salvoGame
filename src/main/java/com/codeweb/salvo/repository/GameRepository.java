package com.codeweb.salvo.repository;

import com.codeweb.salvo.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findById(@Param("gameId") Long gameId);
}
