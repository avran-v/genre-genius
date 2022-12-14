package cs1302.omega.mmsearch;

import com.google.gson.annotations.SerializedName;

/**
 * Represents response from the Musixmatch API (search request).
 * Class that represents body of the response.
 * Used by Gson to make an object from JSON response.
 */
public class Body {

    @SerializedName("track_list")
    private TrackList[] trackList;

    /**
     * Getter method for all tracks returned from a search.
     * @return TrackList[]
     */
    public TrackList[] getTrackList() {
        return trackList;
    }
}
