package cs1302.omega;
import cs1302.game.DemoGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Control;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import java.net.http.HttpClient;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.StringBuilder;

import cs1302.omega.lastfm.*;
import cs1302.omega.mmsearch.*;
import cs1302.omega.mmsnippet.*;
import cs1302.omega.LyricDisplay;

import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class OmegaApp extends Application {

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    private static final String LASTFM_API = "http://ws.audioscrobbler.com/2.0/";
    private static final String MM_API = "https://api.musixmatch.com/ws/1.1/";
    private Stage stage;
    private Scene scene;
    private VBox root;
    private Text title;

    Text instructions;

    String lfapikey;
    String mmapikey;
    String configPath;

    String[] topTrackNames;
    String[] topTrackArtists;
    int[] mmIds;
    String[] allTags;
    String[] chosenTrackNames;
    String[] chosenTrackArtists;
    String[] snippets;
    Button[] genreButtons;

    Button shuffleButton;

    LyricDisplay[] lyricDisplays;

    boolean visible;

    Label appInfo;

    VBox appInfoBox;

    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {
        configPath = "resources/config.properties";
        this.stage = null;
        this.scene = null;
        this.root = new VBox();
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        Text attribution = new Text("Made with the LastFM and Musixmatch APIs");
        attribution.getStyleClass().add("attribution");
        title = new Text("Genre Genius!");
        title.getStyleClass().add("title");
        instructions = new Text
            ("Choose a genre, can you can guess a song's name \n" +
            " and artist from a snippet of the lyrics?");
        instructions.getStyleClass().add("instructions");
        this.setAPIKeys();
        VBox genreBox = new VBox();
        Text genreTitle = new Text ("Genres:");
        genreBox.setAlignment(Pos.CENTER);
        genreBox.getChildren().add(genreTitle);
        String[] genres = getTopGenres();
        int[] randomNums = getRandomNums(6, 50);
        genreButtons = new Button[3];
        for (int i = 0; i < 3; i++) {
            Button genreButton = new Button(genres[randomNums[i]].toLowerCase());
            genreButton.setOnAction(e -> {
                runNow(() -> getTopTracks(genreButton.getText(),genreButtons));
            });
            genreButton.getStyleClass().add("genre-button");
            genreBox.getChildren().add(genreButton);
            genreButtons[i] = genreButton;
        }

        shuffleButton = new Button("Shuffle genres!");
        shuffleButton.getStyleClass().add("shuffle-button");
        shuffleButton.setOnAction(e -> runNow(() -> Platform.runLater(() -> shuffleGenres())));
        genreBox.getChildren().add(shuffleButton);

        lyricDisplays = new LyricDisplay[3];
        VBox lyricBox = new VBox();
        for (int i = 0; i < 3; i++) {
            LyricDisplay lyricDisplay = new LyricDisplay();
            lyricDisplay.getRevealButton().setOnAction(e -> lyricDisplay.revealInfo());
            lyricBox.getChildren().add(lyricDisplay);
            lyricDisplays[i] = lyricDisplay;
        }
        GridPane mainContent = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(65);
        mainContent.getColumnConstraints().addAll(col1, col2);
        mainContent.add(genreBox, 0, 0);
        mainContent.add(lyricBox, 1, 0);

        mainContent.setId("main-content");
        appInfo = new Label("Once you choose a genre, 3 random tracks will be chosen from it. \n" +
        "Guess info about the song and press 'Reveal Info' to see if you were right! \n" +
        "Try all the genres or challenge yourself by repeating one!");
        appInfoBox = new VBox();
        appInfoBox.getChildren().add(appInfo);
        appInfoBox.getStyleClass().add("app-info-normal");
        appInfoBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(attribution,title,instructions);
        root.getChildren().addAll(mainContent,appInfoBox);
        /*// demonstrate how to load local asset using "file:resources/"
        Image bannerImage = new Image("file:resources/readme-banner.png");
        ImageView banner = new ImageView(bannerImage);
        banner.setPreserveRatio(true);
        banner.setFitWidth(640);

        // some labels to display information
        Label notice = new Label("Modify the starter code to suit your needs.");
        Label instructions
            = new Label("Move left/right with arrow keys; click rectangle to teleport.");

        // demo game provided with the starter code
        DemoGame game = new DemoGame(640, 240);

        // setup scene
        VBox root = new VBox(banner, notice, instructions, game);
        Scene scene = new Scene(root);*/

        // setup stagex
        this.scene = new Scene(this.root);
        scene.getStylesheets().add("OmegaStyle.css");
        stage.setTitle("GenreGenius!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

        // play the game
        // game.play();

    } // start

    public void setAPIKeys() {
        try (FileInputStream configFileStream = new FileInputStream(configPath)) {
            Properties config = new Properties();
            config.load(configFileStream);
            lfapikey = config.getProperty("lastfm.apikey");
            mmapikey = config.getProperty("musixmatch.apikey");
        } catch (IOException ioe) {
            System.err.println(ioe);
            ioe.printStackTrace();
        }
    }
    public void disableButtons() {
        for (int i = 0; i < genreButtons.length; i++) {
            genreButtons[i].setDisable(true);
        }
        shuffleButton.setDisable(true);
    }

    public void enableButtons() {
        for (int i = 0; i < genreButtons.length; i++) {
            genreButtons[i].setDisable(false);
        }
        shuffleButton.setDisable(false);
    }

    public String[] getTopGenres() {
        try {
            String query = String.format("?method=tag.getTopTags&api_key=%s&format=json", lfapikey);
            String uri = LASTFM_API + query;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
            HttpResponse<String> response = HTTP_CLIENT
                .send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Something is wrong.");
            }
            String jsonString = response.body();
            TopTagsResponse tagsResponse =
                GSON.fromJson(jsonString, cs1302.omega.lastfm.TopTagsResponse.class);
            allTags = new String[tagsResponse.getTopTags().getTags().length];
            for (int i = 0; i < allTags.length; i++) {
                Tag tag = tagsResponse.getTopTags().getTags()[i];
                allTags[i] = tag.getName();
            }
            return allTags;
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return new String[0];
    }

    public void shuffleGenres() {
        int[] randomGenres = getRandomNums(6, allTags.length);
        for(int i = 0; i < genreButtons.length; i++){
            genreButtons[i].setText(allTags[randomGenres[i]]);
        }
    }

    public int[] getRandomNums(int total, int max) {
        int[] randomNums = new int[total];
        for (int i = 0; i < total; i++) {
            randomNums[i] = (int)Math.floor(Math.random()*(max+1));
            for (int a = 0; a < total; a++) {
                if (randomNums[a] == randomNums[i]) {
                    if (randomNums[i] + 1 < max) {
                        randomNums[i] += 1;
                    } else {
                        randomNums[i] -= 1;
                    }
                }
            }
        }
        return randomNums;
    }

    public void resetSnippets(){
        for (int i = 0; i < lyricDisplays.length; i++){
            lyricDisplays[i].hideInfo();
            lyricDisplays[i].setLyricSnippet("Lyrics coming soon...");
        }
    }

    public void getTopTracks(String tag, Button[] genreButtons) {
        Platform.runLater(() -> disableButtons());
        Platform.runLater(() -> appInfo.setText("Loading..."));
        Platform.runLater(() -> appInfoBox.getStyleClass().clear());
        Platform.runLater(() -> appInfoBox.getStyleClass().add("app-info-loading"));
        Platform.runLater(() -> resetSnippets());
        try {
            String modTag = URLEncoder.encode(tag, StandardCharsets.UTF_8);
            String query = String.format
                ("?method=tag.gettoptracks&tag=%s&api_key=%s&format=json", modTag, lfapikey);
            String uri = LASTFM_API + query;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
            HttpResponse<String> response = HTTP_CLIENT
                .send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Something is wrong.");
            }
            String jsonString = response.body();
            TopTracksResponse tracksResponse =
                GSON.fromJson(jsonString, cs1302.omega.lastfm.TopTracksResponse.class);
            //int tracksNum = tracksResponse.getTracks().getTrack().length;
            int tracksNum = 50;
            topTrackNames = new String[tracksNum];
            topTrackArtists = new String[tracksNum];
            for (int i = 0; i < tracksNum; i++){
                Track track = tracksResponse.getTracks().getTrack()[i];
                topTrackNames[i] = track.getName();
                topTrackArtists[i] = track.getArtist().getName();
            }
            trackSearch();
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void updateLyricDisplays(int[] chosenTracks) {
        //update this to use the chosen arrays + add snippet
        for(int i = 0; i < lyricDisplays.length; i++){
            lyricDisplays[i].setTrackName(chosenTrackNames[chosenTracks[i]]);
            lyricDisplays[i].setTrackArtist(chosenTrackArtists[chosenTracks[i]]);
            lyricDisplays[i].setLyricSnippet(snippets[i]);
            lyricDisplays[i].enableInfo();
        }
        /*for(int i = 0; i < genreButtons.length; i++){
            genreButtons[i].setDisable(false);
            }*/
    }

    public void trackSearch() {
        try {
            int count = 0;
            mmIds = new int[topTrackNames.length];
            chosenTrackNames = new String[topTrackNames.length];
            chosenTrackArtists = new String[topTrackNames.length];
            boolean getSnippets = true;
            for(int i = 0; i < topTrackNames.length; i++){
                String artist = URLEncoder.encode(topTrackArtists[i], StandardCharsets.UTF_8);
                String track = URLEncoder.encode(topTrackNames[i], StandardCharsets.UTF_8);
                String query = String.format
                    ("track.search?q_artist=%s&q_track=%s&s_track_rating=desc&apikey=%s",
                    artist, track, mmapikey);
                String uri = MM_API + query;
                System.out.println(uri);
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .build();
                HttpResponse<String> response = HTTP_CLIENT
                    .send(request, BodyHandlers.ofString());
                String jsonString = response.body();
                System.out.println(response.statusCode());
                System.out.println(jsonString);
                if (jsonString.contains("\"status_code\":200")) {
                    TrackSearchResponse searchResponse =
                        GSON.fromJson(jsonString, cs1302.omega.mmsearch.TrackSearchResponse.class);
                    if (searchResponse.getMessage().getBody()
                    .getTrackList()[0].getTrack() instanceof MMTrack){
                        MMTrack topResult =
                            searchResponse.getMessage().getBody().getTrackList()[0].getTrack();
                        if(topResult.getHasLyrics() > 0){
                            MMTrack chosen = topResult;
                            mmIds[count] = chosen.getTrackID();
                            chosenTrackNames[count] = chosen.getTrackName();
                            chosenTrackArtists[count] = chosen.getArtistName();
                            count++;
                        }
                    }
                } else {
                    getSnippets = false;
                    Platform.runLater(() -> appInfo.setText("Looks like this program reached its" +
                    " limit for API calls from Musixmatch. \n Try again tomorrow!"));
                    Platform.runLater(() -> appInfoBox.getStyleClass().clear());
                    Platform.runLater(() -> appInfoBox.getStyleClass().add("app-info-error"));
                }
            }
            if(getSnippets){
                getSnippet();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void getSnippet() {
        try {
            int count = 0;
            snippets = new String[mmIds.length];
            if(mmIds.length < 6){
                    Platform.runLater(() -> appInfoBox.getStyleClass().add("app-info-error"));
                    Platform.runLater(() -> appInfo.setText("Looks like this genre doesn't have" +
                    " enough songs, try another!"));
            } else {
                int[] randomTracks = getRandomNums(3, mmIds.length);
                for(int i = 0; i < 3; i++){
                    String trackID = "" + mmIds[randomTracks[i]];
                    String query = String.format
                        ("track.snippet.get?apikey=%s&track_id=%s", mmapikey, trackID);
                    String uri = MM_API + query;
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(uri))
                        .build();
                    HttpResponse<String> response = HTTP_CLIENT
                        .send(request, BodyHandlers.ofString());
                    String jsonString = response.body();
                    System.out.println(jsonString);
                    if(jsonString.contains("\"status_code\":200")) {
                        SnippetResponse snippetResponse =
                            GSON.fromJson(jsonString, cs1302.omega.mmsnippet.SnippetResponse.class);
                        //get snippet and add to array
                        String temp = snippetResponse.getMessage()
                            .getBody().getSnippet().getSnippetBody();
                        if(temp.length() > 2){
                            snippets[i] = temp;
                            count++;
                        }
                    }
                }
                if (count < 3) {
                        Platform.runLater(() -> appInfo.setText("We might not have" +
                        " enough lyrics for this genre? \n Try a different one or test" +
                        " your luck again!"));
                        Platform.runLater(() -> appInfoBox.getStyleClass().clear());
                        Platform.runLater(() -> appInfoBox.getStyleClass().add("app-info-error"));
                } else {
                    Platform.runLater(() -> updateLyricDisplays(randomTracks));
                    Platform.runLater(() -> appInfo.setText("Click again for more" +
                    " or try another genre!"));
                    Platform.runLater(() -> appInfoBox.getStyleClass().clear());
                    Platform.runLater(() -> appInfoBox.getStyleClass().add("app-info-loaded"));
                }
            }
            Platform.runLater(() -> enableButtons());
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public static void runNow(Runnable target){
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    }

} // OmegaApp
