package com.codeweb.salvo.repository;

import com.codeweb.salvo.models.Game;
import com.codeweb.salvo.models.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;


@RepositoryRestResource
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {

    Optional<GamePlayer> findById(@Param("gamePlayerId") Long gamePlayerId);

}
