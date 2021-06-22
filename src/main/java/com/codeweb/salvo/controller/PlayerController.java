package com.codeweb.salvo.controller;

import com.codeweb.salvo.models.Player;
import com.codeweb.salvo.repository.PlayerRepository;
import com.codeweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private PlayerRepository playerRepository;

    @PostMapping("/players")
    public ResponseEntity<Object> registrer(@RequestParam String email, String password){

        if(email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>(Util.makeMap("error","missing data"), HttpStatus.FORBIDDEN);
        }

        if(!Util.validateEmail(email)){
            return new ResponseEntity<>(Util.makeMap("error","email format invalid"),HttpStatus.FORBIDDEN);
        }

        if(playerRepository.findByEmail(email) != null){
            return new ResponseEntity<>(Util.makeMap("error","name already in use"),HttpStatus.FORBIDDEN);
        }

        if(!Util.validatePassword(password)){
            return new ResponseEntity<>(Util.makeMap("error","Your password should have minium eight characters, at least one uppercase letter, one lowercase letter and one numbe"),HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email,passwordEncoder.encode(password)));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/players")
    public List<Object> getAllPlayers() {
        return playerRepository.findAll().stream().map(player -> player.playerDTO()).collect(Collectors.toList());
    }

    @GetMapping("/players/{id}")
    public List<Object> getPlayerById(@PathVariable Long id) {
        return playerRepository.findById(id).stream().map(player -> player.playerDTO()).collect(Collectors.toList());
    }
}
