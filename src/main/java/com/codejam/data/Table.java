package com.codejam.data;

import com.codejam.model.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table {
    private List<Card> cardsOnTheTable;

    public Table() {
        this.cardsOnTheTable = new ArrayList<>();
    }

    public void initWithRandomData(int numberOfCards){
        List<String> randomWords = new MockDataBase().getRandomWords(numberOfCards);

        int blueCards = numberOfCards / 3;
        int redCards = numberOfCards / 3;
        int neutralCards = numberOfCards - (blueCards + redCards);

        for (int i = 0; i < blueCards; i++) {
            cardsOnTheTable.add(new Card(randomWords.get(i), Card.Team.BLUE_TEAM, true));
        }
        for (int i = 0; i < redCards; i++) {
            cardsOnTheTable.add(new Card(randomWords.get(blueCards + i), Card.Team.RED_TEAM, true));
        }
        for (int i = 0; i < neutralCards; i++) {
            cardsOnTheTable.add(new Card(randomWords.get(blueCards + redCards + i), Card.Team.NEUTRAL, true));
        }

        Collections.shuffle(cardsOnTheTable);
    }

    public List<Card> getCardsOnTheTable(){
        return cardsOnTheTable;
    }
}
