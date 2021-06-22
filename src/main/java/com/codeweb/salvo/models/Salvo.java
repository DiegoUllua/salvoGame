package com.codeweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private int turn;

    @ElementCollection
    @Column(name = "locations")
    private List<String> salvoLocations = new ArrayList<>();


    public Salvo() {
        this.salvoLocations = new ArrayList<>();
        this.turn =0;
    }

    public Salvo(GamePlayer gamePlayer, int turn, List<String> locations) {
        this.gamePlayer = gamePlayer;
        this.turn = turn;
        this.salvoLocations = locations;
    }


    public Map<String,Object> salvoDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", getTurn());
        dto.put("player", getGamePlayer().getId());
        dto.put("locations", getSalvoLocations());
        return dto;
    }

    public long getId() {
        return Id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List <String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List locations) {
        this.salvoLocations = locations;
    }
}
