package com.codeweb.salvo.controller;


import com.codeweb.salvo.models.GamePlayer;
import com.codeweb.salvo.models.Player;
import com.codeweb.salvo.models.Ship;
import com.codeweb.salvo.repository.GamePlayerRepository;
import com.codeweb.salvo.repository.PlayerRepository;
import com.codeweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;


    @RequestMapping("/games/players/{gamePlayerId}/ships")
    public ResponseEntity<Map<String,Object>> putShips(@PathVariable long gamePlayerId, Authentication authentication, @RequestBody Set<Ship> shipSet) {



        if(Util.isGuest(authentication)){
            return new  ResponseEntity<>(Util.makeMap("error","Paso algo"), HttpStatus.UNAUTHORIZED);
        }

        Optional<GamePlayer> gpFound = gamePlayerRepository.findById(gamePlayerId);
        Player player  = playerRepository.findByEmail(authentication.getName());

        if(!gpFound.isPresent()){
            return new ResponseEntity<>(Util.makeMap("Problem","gamePlayerId provided not found"),HttpStatus.NOT_FOUND);
        }

        if(gpFound.get().getPlayer().getId() != player.getId()){
            return new  ResponseEntity<>(Util.makeMap("error","Paso algo"),HttpStatus.CONFLICT);
        }

        if(gpFound.get().getShips().size() > 0){
            return new ResponseEntity<>(Util.makeMap("error","Ships already puts"),HttpStatus.FORBIDDEN);
        }

        if(shipSet.size() < 1){
            return new ResponseEntity<>(Util.makeMap("error","Ship list is empty"),HttpStatus.NOT_MODIFIED);
        }

        gpFound.get().addShips(shipSet);

        gamePlayerRepository.save(gpFound.get());

        return new ResponseEntity<>(Util.makeMap("ok","Ship list added"),HttpStatus.CREATED);

    }
}
