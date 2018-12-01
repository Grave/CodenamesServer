package com.codejam.data;

import java.util.List;

public class GameEndInformation {

    public enum GameState {
        RUNNING,
        BLUE_TEAM_WON,
        RED_TEAM_WON,
        DRAW
    }

    public GameState endingGameState;
    public List<GameVideoEvent> gameHistory;
}
