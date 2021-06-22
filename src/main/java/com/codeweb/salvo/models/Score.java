package com.codeweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    private float score;

    private LocalDateTime finishDate;

    public Score() {
    }

    public Score(Player player, Game game , float score,LocalDateTime finishDate ) {
        this.game = game;
        this.player = player;
        this.finishDate = finishDate;
        this.score = score;
    }

    public Map<String, Object> scoreDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player",getPlayer().getId());
        dto.put("finishDate",getFinishDate());
        dto.put("score",getScore());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }
}
