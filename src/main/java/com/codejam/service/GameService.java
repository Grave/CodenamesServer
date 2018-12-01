package com.codejam.service;

import com.codejam.model.Card;
import com.codejam.model.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private GameState currentGameState = new GameState();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public boolean canPickCard() {
        return currentGameState.canPickCard;
    }

    public void pickedCardForTeam(Card.Team team) {
        if (currentGameState.currentTeam != team) {
            currentGameState.canPickCard = false;
            messagingTemplate.convertAndSend("/queue/passTurn", team);
        }
    }

    public void teamCanPickCards(Card.Team team) {
        currentGameState.currentTeam = team;
        currentGameState.canPickCard = true;
    }

    public void gameEnded() {
        currentGameState.canPickCard = false;
    }

    public void reset() {
        currentGameState.reset();
    }

    public Card.Team getCurrentTeam() {
        return currentGameState.currentTeam;
    }
}
