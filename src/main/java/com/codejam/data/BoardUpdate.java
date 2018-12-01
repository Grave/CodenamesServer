package com.codejam.data;

import com.codejam.model.Card;
import javafx.util.Pair;

import java.util.List;

public class BoardUpdate {
    public List<Card> cardsOnTheTable;
    public String sound;

    public BoardUpdate(List<Card> cards)
    {
        cardsOnTheTable = cards;
        sound = "";
    }

    public void setResult(Card.Team currentTeam, Pair<Card.Team, Boolean> cardUpdateResult) {
        if (currentTeam == cardUpdateResult.getKey()) {
            sound = "WinningSound";
        } else if (cardUpdateResult.getKey() == Card.Team.NEUTRAL) {
            sound = "EndTurnSound";
        } else {
            sound = "LosingSound";
        }
    }
}
