var ws;

var player;
var videoID;
var videoPlayerDone = false;
var playerFirstLoad=true;

function setConnected(connected) {
    if(connected === true)
    {
        console.log("Retreiving cards");
        showBoardElements();

        $("#connect").hide();
    }
    else
    {
        $("#main-content").html("");
        $("#connect").show();
    }
}

function connect() {
	var socket = new WebSocket("ws://localhost:8080/greeting");
	ws = Stomp.over(socket);

	ws.connect({}, function(frame) {
		ws.subscribe("/user/queue/errors", function(message) {
			alert("Error " + message.body);
		});

        ws.subscribe("/queue/cardListUpdate", function(message) {
            var cards = JSON.parse(message.body);
            showCards(cards.cardsOnTheTable);
        });

        ws.subscribe("/queue/showVideo", function(message) {
            var video = JSON.parse(message.body);
            videoID = video.id;
            showVideoElements();
        });

        ws.subscribe("/queue/gameEnded", function(message) {
            var gameEndData = JSON.parse(message.body);
            showEndScreen(gameEndData);
        });

        setConnected(true);
	}, function(error) {
        disconnect();
	});
}

connect();

function disconnect() {
	if (ws != null) {
		ws.close();
	}
	setConnected(false);
	console.log("Disconnected");
}

function getCardOwnerClass(team)
{
    var ownerClass = "neutral";

    if (team == "BLUE_TEAM") {
        ownerClass = "blueTeam";
    }else if (team == "RED_TEAM") {
        ownerClass = "redTeam";
    }

    return ownerClass;
}

function showEndScreen(gameEndData)
{
    var generatedHtml = "";
    var banner = "";

    if (gameEndData.endingGameState == "BLUE_TEAM_WON")
    {
        banner = '<div class="blue win-area">BLUE WINS</div>';
    }
    else
    {
        banner = '<div class="red win-area">RED WINS</div>';
    }

    var historyItems = gameEndData.gameHistory;

    for (var i = 0; i < historyItems.length; i++) {
        var item = historyItems[i];
        var keywordsTeam = getCardOwnerClass(item.team);

        var keywordsHtml = "";
        var keywords = item.keyWords;

        for (var j = 0; j < keywords.length; j++) {
            var keyword = keywords[j];

            keywordsHtml += '<h4 class="emptyTeam keyword-mini card">' + keyword + '</h4>';
        }

        var pickedCardsHtml = "";
        var pickedCards = item.pickedCards;

        for (var k = 0; k < pickedCards.length; k++) {
            var card = pickedCards[k];
            var colorBkg = getCardOwnerClass(card.team);

            pickedCardsHtml += '<h4 class="' + colorBkg + ' keyword-mini card">' + card.keyWord + '</h4>';
        }

        var youtubeFrame = '<iframe class="mini-frame" src="https://www.youtube.com/embed/' + item.videoID + '"></iframe>';
        var selectedVideoContainer = '<div class="selected-video-container ' + keywordsTeam + '">' + keywordsHtml + youtubeFrame + '</div>' ;

        generatedHtml += '<div class="history-item">' + selectedVideoContainer + pickedCardsHtml + '</div>';
    }

    generatedHtml = banner + '<div class="history-container">' + generatedHtml + '</div>'

    $("#main-content").html(generatedHtml);
}

function showCards(cards)
{
    var generatedHtml = "";
    var redScore = 0;
    var blueScore = 0;

    for (var i = 0; i < cards.length; i++)
    {
        var card = cards[i];

        var colorTag = "flipped";

        if (card.active == false)
        {
            colorTag = getCardOwnerClass(card.belongsTo);
        }
        else {
            if (card.belongsTo == "BLUE_TEAM") {
                blueScore = blueScore + 1;
            }else if (card.belongsTo == "RED_TEAM") {
                redScore = redScore + 1;
            }
        }

        generatedHtml += '<div class="card"><div class="container ' + colorTag + '"><h4><b>' + card.text + '</b></h4></div></div>';
    }

    var blankTop = '<div class="blank-top" />';
    var redArea = '<div class="red score-area"><div>Red</div><h2>' + redScore + '</h2></div>';
    var blueArea = '<div class="blue score-area"><div>Blue</div><h2>' + blueScore + '</h2></div>';

    generatedHtml = '<div class="grid-container">' + generatedHtml + '</div>';
    generatedHtml = blankTop + redArea + generatedHtml + blueArea;
    generatedHtml = generatedHtml.trim();

    $("#main-content").html(generatedHtml);
}

function showVideoElements()
{
    var youtubeContainer = '<div id="titleConcealer" class="banner"></div><div id="player"></div><script src="https://www.youtube.com/iframe_api"></script>';

    $("#main-content").html(youtubeContainer);

    if (playerFirstLoad) {
        var tag = document.createElement('script');

        tag.src = "https://www.youtube.com/iframe_api";
        var firstScriptTag = document.getElementsByTagName('script')[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

        playerFirstLoad = false;
    } else
    {
        onYouTubeIframeAPIReady();
    }
}

const showBoardElements = async () =>
{
    const response = await fetch('http://localhost:8080/getCardsOnTheTable', {
    method: 'GET'
    });

    const cards = await response.json();
    console.log("Showing cards");
    showCards(cards);
}

function onYouTubeIframeAPIReady() {
    player = new YT.Player('player', {
        height: '576',
        width: '1024',
        videoId: videoID,
        playerVars: { 'autoplay': 1, 'controls': 0, 'start': 10, 'end':25},
        events: {
            'onStateChange': onPlayerStateChange
        }
    });
}

function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.PLAYING && !videoPlayerDone) {
        setTimeout(hideTitleBanner, 3000);
        videoPlayerDone = true;
    }
    if (event.data == YT.PlayerState.ENDED)
    {
        hideElement("player");
        showBoardElements();
    }
}

function hideTitleBanner() {
    hideElement("titleConcealer");
}

function hideElement(elementName)
{
    var concealer = document.getElementById(elementName);
    concealer.style.display = "none";
}
