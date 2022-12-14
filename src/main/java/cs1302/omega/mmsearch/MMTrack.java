package cs1302.omega.mmsearch;

import com.google.gson.annotations.SerializedName;

/**
 * Represents one track returned from Musixmatch API (search request).
 * Used by Gson to make object from JSON response.
 */
public class MMTrack {

    @SerializedName("track_id")
    private int trackID;

    @SerializedName("has_lyrics")
    private int hasLyrics;

    @SerializedName("artist_name")
    private String artistName;

    @SerializedName("track_name")
    private String trackName;

    /**
     * Getter method for Musixmatch ID of a track.
     * @return int trackID
     */
    public int getTrackID() {
        return trackID;
    }

    /**
     * Getter method for int representing lyrics of a track.
     * 0 means track has no lyrics, 1 means track has lyrics in Musixmatch database
     * @return int hasLyrics
     */
    public int getHasLyrics() {
        return hasLyrics;
    }

    /**
     * Getter method for name of artist of a track.
     * @return String artistName
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Getter method for name of a track.
     * @return String trackName
     */
    public String getTrackName() {
        return trackName;
    }
}
