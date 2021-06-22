package com.codeweb.salvo.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class Player {

    // Atributtes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long Id;

    private String email;
    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER ,cascade = CascadeType.ALL)
    Set<GamePlayer> gamesPlayer = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Score> scores = new HashSet<>();


    // Constructors

    public Player() {

    }

    public Player(String email, String password) {

        this.email = email;
        this.password = password;
    }

    // Methods

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamesPlayer.add(gamePlayer);
    }


    public Map<String, Object> playerDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", getId());
        dto.put("email",getEmail());
        return dto;
    }

    // Getters & setters



    public Score getScores(Game game) {
        return scores.stream().filter(score -> score.getGame().getId() == game.getId()).findFirst().orElse(null);
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Set<GamePlayer> getGamesPlayer() {
        return gamesPlayer;
    }

    public void setGamesPlayer(Set<GamePlayer> gamesPlayer) {
        this.gamesPlayer = gamesPlayer;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Long getId() {
        return Id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<GamePlayer> getGamesList() {
        return gamesPlayer;
    }

    public void setGamesList(Set<GamePlayer> gamesList) {
        this.gamesPlayer = gamesList;
    }
}
