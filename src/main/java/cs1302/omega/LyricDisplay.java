package cs1302.omega;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

public class LyricDisplay extends VBox {
    VBox lyricDisplay;
    Label lyricSnippet;
    Button revealInfo;
    VBox trackInfo;
    HBox artistInfo;
    Label artistLabel;
    Label artistName;
    HBox trackNameInfo;
    Label trackLabel;
    Label trackName;

    public LyricDisplay() {
        super();
        lyricDisplay = new VBox();
        lyricSnippet = new Label("Lyrics coming soon...");
        revealInfo = new Button("Reveal Info");
        revealInfo.getStyleClass().add("reveal-info");
        revealInfo.setDisable(true);

        artistLabel = new Label("Artist Name:");
        artistName = new Label("Sample Artist Name");
        artistInfo = new HBox();
        artistInfo.getChildren().addAll(artistLabel, artistName);
        artistInfo.getStyleClass().add("artist-info");

        trackLabel = new Label("Track Name:");
        trackName = new Label("Sample Track Name");
        trackNameInfo = new HBox();
        trackNameInfo.getChildren().addAll(trackLabel, trackName);
        trackNameInfo.getStyleClass().add("track-name-info");

        hideInfo();

        trackInfo = new VBox();
        trackInfo.getChildren().addAll(trackNameInfo, artistInfo);

        lyricDisplay.getChildren().addAll(lyricSnippet, revealInfo, trackInfo);
        lyricDisplay.setAlignment(Pos.TOP_CENTER);
        lyricDisplay.getStyleClass().add("lyric-display");
        this.getChildren().add(lyricDisplay);
    }

    public void setTrackName(String newName) {
        trackName.setText(newName);
    }

    public void setTrackArtist(String newArtist) {
        artistName.setText(newArtist);
    }

    public void setLyricSnippet(String snippet) {
        lyricSnippet.setText(snippet);
    }

    public void enableInfo() {
        revealInfo.setDisable(false);
    }

    public Button getRevealButton() {
        return revealInfo;
    }

    public void revealInfo() {
        artistName.setVisible(true);
        trackName.setVisible(true);
    }

    public void hideInfo() {
        artistName.setVisible(false);
        trackName.setVisible(false);
    }
}
