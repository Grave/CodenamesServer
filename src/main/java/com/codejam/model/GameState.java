package com.codejam.model;

public class GameState {
    public boolean canPickCard = false;
    public Card.Team currentTeam = Card.Team.RED_TEAM;

    public void reset() {
        this.canPickCard = false;
        this.currentTeam = Card.Team.RED_TEAM;
    }
}
