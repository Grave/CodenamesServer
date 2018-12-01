package com.codejam.web;

import com.codejam.data.BoardUpdate;
import com.codejam.data.GameEndInformation;
import com.codejam.model.Card;
import com.codejam.service.GameService;
import com.codejam.service.HistoryService;
import com.codejam.service.TableService;
import com.codejam.service.YoutubeSearchService;
import com.codejam.web.data.VideoInfo;
import com.codejam.web.data.WebCard;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class DemoController {

    @Autowired
    YoutubeSearchService youtubeSearchService;

    @Autowired
    TableService tableService;

    @Autowired
    HistoryService historyService;

    @Autowired
    GameService gameService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @RequestMapping("/hello")
    public String sayHello(){

        List<String> keywords = new ArrayList<>();
        keywords.add("surfing");
        keywords.add("cat");

        return youtubeSearchService.firstSearchResult(keywords);
    }

    @RequestMapping("/update")
    public void getCardsOnTheTable(@RequestBody WebCard card) {

        if (!gameService.canPickCard())
            return;

        Pair<Card.Team, Boolean> cardUpdateResult = tableService.updateCardState(card.cardText);

        if (!cardUpdateResult.getValue())
            return;

        BoardUpdate result = new BoardUpdate(tableService.getCardsOnTheTable());
        result.setResult(gameService.getCurrentTeam(), cardUpdateResult);

        messagingTemplate.convertAndSend("/queue/cardListUpdate", result);

        gameService.pickedCardForTeam(cardUpdateResult.getKey());
        historyService.addPickedCardEvent(card.cardText, cardUpdateResult.getKey());

        GameEndInformation.GameState currentGameState = tableService.getCurrentGameStateGameState();
        if (currentGameState != GameEndInformation.GameState.RUNNING) {
            System.out.println("Winner winner chicken dinner!");
            GameEndInformation endInformation = new GameEndInformation();
            endInformation.endingGameState = currentGameState;
            endInformation.gameHistory = historyService.getGameHistory();

            messagingTemplate.convertAndSend("/queue/gameEnded", endInformation);
            gameService.gameEnded();
        }
    }

    @RequestMapping("/getCardsOnTheTable")
    public List<Card> getCardsOnTheTable(){
        return tableService.getCardsOnTheTable();
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchOnYoutube(@RequestBody List<String> keywords){
        return youtubeSearchService.firstSearchResult(keywords);
    }

    @RequestMapping("/playVideo/{team}/{nCards}")
    public void playVideo(@PathVariable("team") String team, @PathVariable("nCards") int nCards){
        Card.Team team_data;
        if(team.equals("blue")) team_data = Card.Team.BLUE_TEAM;
        else team_data = Card.Team.RED_TEAM;

        List<String> keywords = tableService.getNextKeywords(team_data);

        if (keywords.size() <= nCards)
            nCards = keywords.size();
        String videoID = youtubeSearchService.firstSearchResult(keywords.subList(0, nCards));

        VideoInfo videoInfo = new VideoInfo();
        videoInfo.id = videoID;

        gameService.teamCanPickCards(team_data);
        historyService.addNewVideoEvent(videoID, keywords, team_data);

        messagingTemplate.convertAndSend("/queue/showVideo", videoInfo);
    }

    @RequestMapping("/replay")
    public void replay()
    {
        tableService.beginNewGame();

        gameService.reset();
        historyService.reset();

        BoardUpdate newSet = new BoardUpdate(tableService.getCardsOnTheTable());
        messagingTemplate.convertAndSend("/queue/cardListUpdate", newSet);
    }
}
