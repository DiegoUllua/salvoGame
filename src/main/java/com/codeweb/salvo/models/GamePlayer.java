package com.codeweb.salvo.models;

import com.codeweb.salvo.repository.GameRepository;
import com.codeweb.salvo.repository.PlayerRepository;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {




    // Atributtes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set <Ship>ships;

    @OrderBy
    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set <Salvo>salvoes;

    private LocalDateTime startDate;

    // Constructors

    public GamePlayer() {
        this.startDate = LocalDateTime.now();
        this.ships = new HashSet<>();
        this.salvoes = new LinkedHashSet<>();
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.ships = new HashSet<>();
        this.salvoes = new LinkedHashSet<>();
        this.startDate = LocalDateTime.now();
    }

    // Methods

    public Map<String, Object> gamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", getId());
        dto.put("player",this.player.playerDTO());

        return dto;
    }



    public void addShips(Set<Ship> shipSet){
        shipSet.forEach(ship -> {
            ship.setGamePlayer(this);
            this.ships.add(ship);
        });
    }

    public void addSalvo(Salvo salvo){
        salvoes.add(salvo);
    }

    public  GamePlayer getOpponent(){

        GamePlayer opponentGP  = game.getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getId() != this.getId()).
                findFirst().orElse(new GamePlayer());

        return opponentGP;
    }

    // Getters & Setters

    public Score getScore() {
        return player.getScores(this.game);
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    public Long getId() {
        return Id;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

}
