package com.codeweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

    // Atributtes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long Id;

    private LocalDateTime startDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Score> scores = new HashSet<>();


    // Constructors

    public Game() {

    }

    public Game(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    // Methods

    public Map<String, Object> gameDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", getId());
        dto.put("created", getDate());
        dto.put("gamePlayers", gamePlayers.stream().map(gamePlayer -> gamePlayer.gamePlayerDTO()).collect(Collectors.toList()));
        dto.put("scores",this.scores.stream().map(score -> score.scoreDTO()).collect(Collectors.toList()));
        return dto;
    }

    // Getters & Setters


    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Long getId() {
        return Id;
    }

    public LocalDateTime getDate() {
        return startDate;
    }

    public void setDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
}
