package com.codejam.data;

import com.codejam.model.Card;

import java.util.ArrayList;
import java.util.List;

public class GameVideoEvent {
    public String videoID;
    public Card.Team team;
    public List<String> keyWords;
    public List<GamePickedCardEvent> pickedCards;

    public GameVideoEvent() {
        this.keyWords = new ArrayList<>();
        this.pickedCards = new ArrayList<>();
    }

}
