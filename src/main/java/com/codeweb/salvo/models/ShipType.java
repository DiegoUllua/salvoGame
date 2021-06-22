package com.codeweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShipType {
    @JsonProperty("carrier")
    CARRIER(lenght= 5),

    @JsonProperty("battleship")
    BATTLESHIP(lenght=4),

    @JsonProperty("submarine")
    SUBMARINE(lenght=3),

    @JsonProperty("destroyer")
    DESTROYER(lenght=3),

    @JsonProperty("patrolboat")
    PATROL_BOAT(lenght=2);

    private static int lenght;

    private ShipType(int lenght){


    }
}
