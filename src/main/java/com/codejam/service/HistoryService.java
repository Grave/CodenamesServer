package com.codejam.service;

import com.codejam.data.GamePickedCardEvent;
import com.codejam.data.GameVideoEvent;
import com.codejam.model.Card;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {
    private List<GameVideoEvent> events;
    private GameVideoEvent currerntEvent;

    public HistoryService() {
        this.events = new ArrayList<>();
    }

    public void addNewVideoEvent(String videoID, List<String> keyWords, Card.Team team) {
        currerntEvent = new GameVideoEvent();
        currerntEvent.videoID = videoID;
        currerntEvent.keyWords.addAll(keyWords);
        currerntEvent.team = team;
        events.add(currerntEvent);
    }

    public void addPickedCardEvent(String cardText, Card.Team belongsTo) {
        if (currerntEvent == null)
            return;

        GamePickedCardEvent event = new GamePickedCardEvent();
        event.keyWord = cardText;
        event.team = belongsTo;
        currerntEvent.pickedCards.add(event);
    }

    public List<GameVideoEvent> getGameHistory() {
        return events;
    }

    public void reset() {
        events.clear();
        currerntEvent = null;
    }
}
