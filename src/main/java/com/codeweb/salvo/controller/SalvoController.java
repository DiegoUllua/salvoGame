package com.codeweb.salvo.controller;

import com.codeweb.salvo.models.*;
import com.codeweb.salvo.repository.*;
import com.codeweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @RequestMapping("/game_view/{id}")
    public ResponseEntity<Map<String,Object>> getGameView(@PathVariable long id, Authentication authentication) {

        Optional<GamePlayer> gpFound = gamePlayerRepository.findById(id);
        Player player  = playerRepository.findByEmail(authentication.getName());

        ResponseEntity<Map<String,Object>> response;

        if(Util.isGuest(authentication)){
            return new  ResponseEntity<>(Util.makeMap("error","Paso algo"), HttpStatus.UNAUTHORIZED);
        }

        if(gpFound.get().getPlayer().getId() != player.getId()){
            return new  ResponseEntity<>(Util.makeMap("error","Paso algo"),HttpStatus.CONFLICT);
        }

        if(gpFound.isPresent()){

            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            Map<String, Object> hits = new LinkedHashMap<>();
            GamePlayer opponent = gpFound.get().getOpponent();

            dto.put("id", gpFound.get().getId());
            dto.put("created",gpFound.get().getGame().getDate());
            dto.put("gameState",gameState(gpFound.get(), opponent));
            dto.put("gamePlayers",gpFound.get().getGame().getGamePlayers().stream().map(gp -> gp.gamePlayerDTO()).collect(Collectors.toList()));
            dto.put("ships",gpFound.get().getShips().stream().map(ship -> ship.shipDTO()).collect(Collectors.toList()));
            dto.put("salvoes",  gpFound.get().getGame().getGamePlayers().stream().flatMap(gp -> gp.getSalvoes().stream().map(sa -> sa.salvoDTO())).collect(Collectors.toList()));
            // Agregar los hits al dto
            hits.put("self", hitsAndSinks(gpFound.get(),gpFound.get().getOpponent()));
            if(opponent != null){
                hits.put("opponent", hitsAndSinks(gpFound.get().getOpponent(),gpFound.get()));
            }/*else{
                hits.put("opponent",new ArrayList<>());
            }*/

            dto.put("hits",hits);


            response = new ResponseEntity<>(dto, HttpStatus.OK);
        }else{
            response = new ResponseEntity<>(Util.makeMap("Problem","id provided not found"),HttpStatus.NOT_FOUND);
        }

        return response;

    }


    public String gameState(GamePlayer self, GamePlayer opponent){

        if(self.getShips().size() == 0){return "PLACESHIPS";}

        if(self.getGame().getGamePlayers().size() == 1){return "WAITINGFOROPP";}

        if(self.getGame().getGamePlayers().size() == 2){

            if(opponent.getShips().size() == 0){
                return "WAIT";
            }

            if( self.getSalvoes().size() == opponent.getSalvoes().size() &&
                    getIfAllSunk(self,opponent) && getIfAllSunk(opponent,self)){

                scoreRepository.save(new Score(self.getPlayer(),self.getGame(),0.5f, LocalDateTime.now()));

                return "TIE";
            }
            if(self.getSalvoes().size() == opponent.getSalvoes().size() &&
                    getIfAllSunk(self,opponent) && !getIfAllSunk(opponent,self)){

                scoreRepository.save(new Score(self.getPlayer(),self.getGame(),0, LocalDateTime.now()));
                return "LOST";

            }
            if(self.getSalvoes().size() == opponent.getSalvoes().size() &&
                    !getIfAllSunk(self,opponent) && getIfAllSunk(opponent,self)){

                scoreRepository.save(new Score(self.getPlayer(),self.getGame(),1, LocalDateTime.now()));
                return "WON";
            }

            if(self.getSalvoes().size() > opponent.getSalvoes().size()){
                return "WAIT";
            }

            if(self.getSalvoes().size() == opponent.getSalvoes().size() && self.getId() > opponent.getId()){
                return "WAIT";
            }
            if(self.getSalvoes().size() <= opponent.getSalvoes().size()){
                return "PLAY";
            }

        }

        return "UNDEFINED";
    }

    private Boolean getIfAllSunk (GamePlayer self, GamePlayer opponent) {

        if(!opponent.getShips().isEmpty() && !self.getSalvoes().isEmpty()){
            return opponent.getSalvoes().stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList()).containsAll(self.getShips().stream()
                    .flatMap(ship -> ship.getLocations().stream()).collect(Collectors.toList()));
        }
        return false;
    }

    public List<Map> hitsAndSinks(GamePlayer self, GamePlayer opponent){

        List<Map> hits = new ArrayList<>();

        int carrierDamage = 0;
        int battleShipDamage = 0;
        int submarineDamage = 0;
        int destroyerDamage = 0;
        int patrolBoatDamage = 0;

        List <String> patrolboatlocations = findShipLocations(self,ShipType.PATROL_BOAT);
        List <String> battleShiplocations = findShipLocations(self,ShipType.BATTLESHIP);
        List <String> submarinelocations = findShipLocations(self,ShipType.SUBMARINE);
        List <String> destroyerlocations = findShipLocations(self,ShipType.DESTROYER);
        List <String> carrierlocations = findShipLocations(self,ShipType.CARRIER);

        // recorro la lista de salvos
        for(Salvo salvo : opponent.getSalvoes()){

            int carrierInTurn = 0;
            int battleShipInTurn = 0;
            int submarineInTurn = 0;
            int destroyerInTurn = 0;
            int patrolBoatInTurn = 0;

            int missedShots = salvo.getSalvoLocations().size();
            Map<String,Object> damagePerTurn = new LinkedHashMap<>();
            Map<String,Object> hitsPerTurn = new LinkedHashMap<>();
            List <String> hitCellList = new ArrayList<>();

            // Recorro la lista de posiciones de cada tiro del salvo
            for (String location : salvo.getSalvoLocations()){

                // compruebo si ese disparo dio en alguna posicion de alguno de mis ships
                if(patrolboatlocations.contains(location)){
                    // si dio, aumento los contadores, agrego la celda a la lista de hits y disminuyo la de tiros errados
                    patrolBoatInTurn ++;
                    patrolBoatDamage ++;
                    hitCellList.add(location);
                    missedShots --;

                }
                if(battleShiplocations.contains(location)){
                    battleShipInTurn ++;
                    battleShipDamage ++;
                    hitCellList.add(location);
                    missedShots --;
                }
                if(submarinelocations.contains(location)){
                    submarineInTurn ++;
                    submarineDamage ++;
                    hitCellList.add(location);
                    missedShots --;
                }
                if(destroyerlocations.contains(location)){
                    destroyerInTurn ++;
                    destroyerDamage ++;
                    hitCellList.add(location);
                    missedShots --;
                }
                if(carrierlocations.contains(location)){
                    carrierInTurn ++;
                    carrierDamage ++;
                    hitCellList.add(location);
                    missedShots --;
                }

                // Agrego al array de hits en el turno
                damagePerTurn.put("patrolboatHits",patrolBoatInTurn);
                damagePerTurn.put("carrierHits",carrierInTurn);
                damagePerTurn.put("destroyerHits",destroyerInTurn);
                damagePerTurn.put("submarineHits",submarineInTurn);
                damagePerTurn.put("battleshipHits",battleShipInTurn);

                //Agrego al array de hits en total
                damagePerTurn.put("patrolboat",patrolBoatDamage);
                damagePerTurn.put("carrier",carrierDamage);
                damagePerTurn.put("destroyer",destroyerDamage);
                damagePerTurn.put("submarine",submarineDamage);
                damagePerTurn.put("battleship",battleShipDamage);

            }



            // Agrego la info al array principal
            hitsPerTurn.put("turn", salvo.getTurn());
            hitsPerTurn.put("missed",missedShots);
            hitsPerTurn.put("damages",damagePerTurn);
            hitsPerTurn.put("hitLocations",hitCellList);

            hits.add(hitsPerTurn);

        }

        return hits;
    }

    public List<String> findShipLocations(GamePlayer gamePlayer, ShipType type) {
        Optional<Ship> response;
        response = gamePlayer.getShips().stream().filter(ship -> ship.getType().equals(type)).findFirst();
        if (response.isEmpty()) {

            return new ArrayList<String>();
        }
        return response.get().getLocations();
    }



    @RequestMapping("/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Object> setSalvoes(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Salvo salvo) {

        if (Util.isGuest(authentication)) {
            return new ResponseEntity<>(Util.makeMap("error", "Paso algo"), HttpStatus.UNAUTHORIZED);
        }

        Optional<GamePlayer> gpFound = gamePlayerRepository.findById(gamePlayerId);
        Player player = playerRepository.findByEmail(authentication.getName());
        GamePlayer opponentGp;

        if (gpFound.get().getPlayer().getId() != player.getId()) {
            return new ResponseEntity<>(Util.makeMap("error", "You dont have access to this game"), HttpStatus.CONFLICT);
        }

        opponentGp = gpFound.get().getOpponent();

        if(opponentGp == null){
            return new  ResponseEntity<>(Util.makeMap("error","You dont have an opponent"),HttpStatus.FORBIDDEN);
        }

        if(salvo.getSalvoLocations().size() > 5){
            // Si tengo mas de 5 elementos en el array no puedo disparar este salvo
            return new  ResponseEntity<>(Util.makeMap("error","You can add max 5 shoots"),HttpStatus.FORBIDDEN);
        }

        if(gpFound.get().getSalvoes().size() > opponentGp.getSalvoes().size()){
            // Si mi cantidad de salvos es mayor que la del oponente no puedo disparar
            return new  ResponseEntity<>(Util.makeMap("error","Wait for your opponent to shoot first"),HttpStatus.FORBIDDEN);
        }else{
            // Si es menor o igual puedo disparar
            salvo.setTurn(gpFound.get().getSalvoes().size()+1);
            salvo.setGamePlayer(gpFound.get());
            salvoRepository.save(salvo);
            gpFound.get().addSalvo(salvo);
            gamePlayerRepository.save(gpFound.get());
            return new ResponseEntity<>(Util.makeMap("ok","salvo created"),HttpStatus.CREATED);
        }



    }




}
