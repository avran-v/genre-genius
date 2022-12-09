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

import cs1302.omega.lastfm.*;

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
    private Stage stage;
    private Scene scene;
    private VBox root;
    private Text title;

    String lfapikey;
    String mmapikey;
    String configPath;

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
        for(int i = 0; i < 3; i++){
            genreBox.getChildren().add(new Button(genres[i]));
        }
        root.getChildren().add(title);
        root.getChildren().add(genreBox);
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

} // OmegaApp
