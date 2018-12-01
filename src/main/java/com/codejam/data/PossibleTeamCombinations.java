package com.codejam.data;

import com.codejam.model.Card;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class PossibleTeamCombinations {
    private Card.Team team;
    private List<List<String>> possibleCombinations;
    private int createdPossibleCombinations = 0;

    public PossibleTeamCombinations(Card.Team team) {
        this.team = team;
        possibleCombinations = new ArrayList<>();
    }

    public List<String> getNextKeywords(Table table) {
        if (possibleCombinations.isEmpty()) {
            List<Card> cards = table.getCardsOnTheTable();
            List<String> keywords = cards.stream().filter(c -> c.getBelongsTo().equals(team) && c.isActive()).map(Card::getText).collect(Collectors.toList());
            if (keywords.size() <= 1)
                return keywords;

            for (int i = 0; i < keywords.size(); ++i) {
                for (int j = i + 1; j < keywords.size(); ++j) {
                    List<String> combination = new ArrayList<>();
                    if (createdPossibleCombinations == 0) {
                        combination.add(keywords.get(i));
                        combination.add(keywords.get(j));
                    } else if (createdPossibleCombinations == 1){
                        combination.add(keywords.get(j));
                        combination.add(keywords.get(i));
                    } else {
                        combination.add(keywords.get(i));
                        combination.add(keywords.get(j));
                        Collections.shuffle(combination);
                    }

                    possibleCombinations.add(combination);
                }
            }
            ++createdPossibleCombinations;
            Collections.shuffle(possibleCombinations);
        }

        return possibleCombinations.remove(0);
    }

    public void cardPicked(String text) {
        Iterator<List<String>> iterator = possibleCombinations.iterator();
        while (iterator.hasNext()) {
            List<String> next = iterator.next();
            if (next.contains(text)) {
                iterator.remove();
            }
        }
    }
}
