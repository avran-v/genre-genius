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

    String lfapikey;
    String mmapikey;
    String configPath;

    String[] topTrackNames;
    String[] topTrackArtists;
    int[] mmIds;
    String[] chosenTrackNames;
    String[] chosenTrackArtists;
    String[] snippets;
    Button[] genreButtons;

    LyricDisplay[] lyricDisplays;

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
        title = new Text("Genre Genius!");
        title.setFont(Font.font("Helvetica", 20));
        title.setFill(Color.AQUA);
        this.setAPIKeys();
        VBox genreBox = new VBox();
        String[] genres = getTopGenres();
        int[] randomNums = getRandomNums(6, 50);
        genreButtons = new Button[6];
        for(int i = 0; i < 6; i++){
            Button genreButton = new Button(genres[randomNums[i]].toLowerCase());
            genreButton.setOnAction(e -> getTopTracks(genreButton.getText(),genreButtons));
            genreBox.getChildren().add(genreButton);
            genreButtons[i] = genreButton;
        }

        lyricDisplays = new LyricDisplay[3];
        VBox lyricBox = new VBox();
        for(int i = 0; i < 3; i++){
            LyricDisplay lyricDisplay = new LyricDisplay();
            lyricDisplay.getRevealButton().setOnAction(e -> lyricDisplay.revealInfo());
            lyricBox.getChildren().add(lyricDisplay);
            lyricDisplays[i] = lyricDisplay;
        }
        HBox mainContent = new HBox();
        mainContent.getChildren().addAll(genreBox, lyricBox);
        root.getChildren().add(title);
        root.getChildren().add(mainContent);
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

        // setup stage
        this.scene = new Scene(this.root);
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
            String[] allTags = new String[tagsResponse.getTopTags().getTags().length];
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

    public int[] getRandomNums(int total, int max) {
        int[] randomNums = new int[total];
        for(int i = 0; i < total; i++){
            randomNums[i] = (int)Math.floor(Math.random()*(max+1));
            for(int a = 0; a < total; a++){
                if(randomNums[a] == randomNums[i]){
                    if(randomNums[i] + 1 < max){
                        randomNums[i] += 1;
                    } else {
                        randomNums[i] -= 1;
                    }
                }
            }
        }
        return randomNums;
    }

    public void getTopTracks(String tag, Button[] genreButtons){
        for(int i = 0; i < genreButtons.length; i++){
            genreButtons[i].setDisable(true);
        }
        for(int i = 0; i < lyricDisplays.length; i++){
            lyricDisplays[i].hideInfo();
        }
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
            int tracksNum = tracksResponse.getTracks().getTrack().length;
            topTrackNames = new String[tracksNum];
            topTrackArtists = new String[tracksNum];
            for (int i = 0; i < tracksNum; i++){
                Track track = tracksResponse.getTracks().getTrack()[i];
                topTrackNames[i] = track.getName();
                topTrackArtists[i] = track.getArtist().getName();
            }
            trackSearch();
            /*for(int i = 0; i < genreButtons.length; i++){
                genreButtons[i].setDisable(false);
                }*/
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
            lyricDisplays[i].setLyricSnippet(snippets[chosenTracks[i]]);
            lyricDisplays[i].enableInfo();
        }
        for(int i = 0; i < genreButtons.length; i++){
            genreButtons[i].setDisable(false);
        }
    }

    public void trackSearch() {
        try {
            int count = 0;
            mmIds = new int[topTrackNames.length];
            chosenTrackNames = new String[topTrackNames.length];
            chosenTrackArtists = new String[topTrackNames.length];
            for(int i = 0; i < topTrackNames.length; i++){
                String artist = URLEncoder.encode(topTrackArtists[i], StandardCharsets.UTF_8);
                String track = URLEncoder.encode(topTrackNames[i], StandardCharsets.UTF_8);
                String query = String.format
                    ("track.search?q_artist=%s&q_track=%s&s_track_rating=desc&apikey=%s",
                    artist, track, mmapikey);
                String uri = MM_API + query;
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .build();
                HttpResponse<String> response = HTTP_CLIENT
                    .send(request, BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    System.out.println("Something is wrong.");
                }
                String jsonString = response.body();
                TrackSearchResponse searchResponse =
                    GSON.fromJson(jsonString, cs1302.omega.mmsearch.TrackSearchResponse.class);
                // make response into track search object
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
            int chosenTracks[] = getRandomNums(3, mmIds.length);
            //updateLyricDisplays(chosenTracks);
            getSnippet();
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void getSnippet() {
        try {
            snippets = new String[mmIds.length];
            int[] randomTracks = getRandomNums(3, mmIds.length);
            for(int i = 0; i < mmIds.length; i++){
                String trackID = "" + mmIds[i];
                String query = String.format
                    ("track.snippet.get?apikey=%s&track_id=%s", mmapikey, trackID);
                String uri = MM_API + query;
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .build();
                HttpResponse<String> response = HTTP_CLIENT
                    .send(request, BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    System.out.println("Something is wrong.");
                }
                String jsonString = response.body();
                SnippetResponse snippetResponse =
                    GSON.fromJson(jsonString, cs1302.omega.mmsnippet.SnippetResponse.class);
                //get snippet and add to array
                snippets[i] = snippetResponse.getMessage().getBody().getSnippet().getSnippetBody();
            }
            updateLyricDisplays(randomTracks);
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

} // OmegaApp
