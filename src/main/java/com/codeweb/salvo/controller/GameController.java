package com.codeweb.salvo.controller;

import com.codeweb.salvo.models.Game;
import com.codeweb.salvo.models.GamePlayer;
import com.codeweb.salvo.models.Player;
import com.codeweb.salvo.repository.GamePlayerRepository;
import com.codeweb.salvo.repository.GameRepository;
import com.codeweb.salvo.repository.PlayerRepository;
import com.codeweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @GetMapping("/games")
    public Map<String,Object> getAllGames(Authentication authentication) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        if(!Util.isGuest(authentication)){
            Player player = playerRepository.findByEmail(authentication.getName());
            dto.put("player",player.playerDTO());
        }else{
            dto.put("player","Guest");
        }

        dto.put("games",gameRepository.findAll().stream().map(game -> game.gameDTO()).collect(Collectors.toList()));

        return dto;
    }

    @PostMapping("/games")
    public ResponseEntity<Object> addGame(Authentication authentication){

        // Check autentication
        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error","missing data"),HttpStatus.UNAUTHORIZED);
        }

        Game gameAux = gameRepository.save(new Game(LocalDateTime.now()));

        Player playerAux = playerRepository.findByEmail(authentication.getName());

        GamePlayer gpAux = gamePlayerRepository.save(new GamePlayer(gameAux,playerAux));

        return new ResponseEntity<>(Util.makeMap("gpid",gpAux.getId()),HttpStatus.CREATED);
    }

    @PostMapping("/game/{gameId}/players")
    public ResponseEntity<Object> joinGame(@PathVariable Long gameId, Authentication authentication){

        Optional<Game> game = gameRepository.findById(gameId);

        // Check autentication
        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error","missing data"),HttpStatus.FORBIDDEN);
        }

        if(gameId == null || !game.isPresent()){
            return new ResponseEntity<>(Util.makeMap("error","gameId not found"),HttpStatus.NOT_FOUND);
        }else if(game.get().getGamePlayers().size() > 1){
            return new ResponseEntity<>(Util.makeMap("error","game is full"),HttpStatus.FORBIDDEN);
        }else{
            Player playerAux = playerRepository.findByEmail(authentication.getName());
            GamePlayer gamePlayerAux = gamePlayerRepository.save(new GamePlayer(game.get(),playerAux));

            return new ResponseEntity<>(Util.makeMap("gpid",gamePlayerAux.getId()),HttpStatus.CREATED);
        }

    }





}
