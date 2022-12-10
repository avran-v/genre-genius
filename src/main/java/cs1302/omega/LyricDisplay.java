package cs1302.omega;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class LyricDisplay extends VBox{
    VBox lyricDisplay;
    Label lyricSnippet;
    Button revealInfo;
    HBox trackInfo;
    VBox artistInfo;
    Label artistLabel;
    Label artistName;
    VBox trackNameInfo;
    Label trackLabel;
    Label trackName;

    public LyricDisplay() {
        super();
        lyricDisplay = new VBox();
        lyricSnippet = new Label("Lyrics coming soon...");
        revealInfo = new Button("Reveal Info");
        revealInfo.setDisable(true);

        artistLabel = new Label("Artist");
        artistName = new Label("Sample Artist Name");
        artistInfo = new VBox();
        artistInfo.getChildren().addAll(artistLabel, artistName);

        trackLabel = new Label("Track");
        trackName = new Label("Sample Track Name");
        trackNameInfo = new VBox();
        trackNameInfo.getChildren().addAll(trackLabel, trackName);

        hideInfo();

        trackInfo = new HBox();
        trackInfo.getChildren().addAll(trackNameInfo, artistInfo);

        lyricDisplay.getChildren().addAll(lyricSnippet, revealInfo, trackInfo);
        this.getChildren().add(lyricDisplay);
    }

    public void setTrackName(String newName){
        trackName.setText(newName);
    }

    public void setTrackArtist(String newArtist){
        artistName.setText(newArtist);
    }

    public void enableInfo(){
        revealInfo.setDisable(false);
    }

    public Button getRevealButton(){
        return revealInfo;
    }

    public void revealInfo(){
        artistName.setVisible(true);
        trackName.setVisible(true);
    }

    public void hideInfo(){
        artistName.setVisible(false);
        trackName.setVisible(false);
    }
}
