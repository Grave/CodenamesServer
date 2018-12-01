package com.codejam.service;

import com.codejam.data.PossibleTeamCombinations;
import com.codejam.data.Table;
import com.codejam.data.GameEndInformation;
import com.codejam.model.Card;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class TableService {

    private static final int TABLE_SIZE = 20;

    private Table table;
    private PossibleTeamCombinations teamBlue;
    private PossibleTeamCombinations teamRed;

    public TableService() {
        beginNewGame();
    }

    public List<String> getNextKeywords(Card.Team team) {
        if (team == Card.Team.BLUE_TEAM) {
            return teamBlue.getNextKeywords(table);
        } else {
            return teamRed.getNextKeywords(table);
        }
    }

    public List<Card> getCardsOnTheTable() {
        if (table == null){
            beginNewGame();
        }

        return table.getCardsOnTheTable();
    }

    public Pair<Card.Team, Boolean> updateCardState(String keyword) {
        for (Card card : table.getCardsOnTheTable()) {
            if(!card.hasText(keyword))
                continue;

            Boolean wasActive = card.isActive();
            card.setActive(false);
            if (wasActive) {
                teamRed.cardPicked(keyword);
                teamBlue.cardPicked(keyword);
            }

            return new Pair<Card.Team, Boolean>(card.getBelongsTo(), wasActive);
        }

        throw new RuntimeException("No Card with Key " + keyword + " found!");
    }

    public GameEndInformation.GameState getCurrentGameStateGameState() {

        boolean blueTeamWon = true;
        boolean redTeamWon = true;

        for (Card card : table.getCardsOnTheTable()) {
            if (!card.isActive())
                continue;

            if (card.getBelongsTo() == Card.Team.BLUE_TEAM) {
                blueTeamWon = false;
            } else if (card.getBelongsTo() == Card.Team.RED_TEAM) {
                redTeamWon = false;
            }
        }

        if (redTeamWon && blueTeamWon) {
            return GameEndInformation.GameState.DRAW;
        } else if (redTeamWon) {
            return GameEndInformation.GameState.RED_TEAM_WON;
        } else if (blueTeamWon) {
            return GameEndInformation.GameState.BLUE_TEAM_WON;
        } else {
            return GameEndInformation.GameState.RUNNING;
        }
    }

    public void beginNewGame()
    {
        table = new Table();
        table.initWithRandomData(TABLE_SIZE);

        teamBlue = new PossibleTeamCombinations(Card.Team.BLUE_TEAM);
        teamRed = new PossibleTeamCombinations(Card.Team.RED_TEAM);
    }
}
