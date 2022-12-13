package cs1302.omega.mmsearch;

import com.google.gson.annotations.SerializedName;

public class MMTrack {

    @SerializedName("track_id")
    private int trackID;

    @SerializedName("has_lyrics")
    private int hasLyrics;

    @SerializedName("artist_name")
    private String artistName;

    @SerializedName("track_name")
    private String trackName;

    public int getTrackID() {
        return trackID;
    }

    public int getHasLyrics() {
        return hasLyrics;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getTrackName(){
        return trackName;
    }
}
