package cs1302.omega.lastfm;

/**
 * Represents a response from the LastFM API.
 * Class to represent all top tracks.
 * Used by Gson to make an object from JSON response.
 */
public class TopTracksResponse {
    private Tracks tracks;

    /**
     * Getter method for top tracks from response.
     * @return Tracks tracks
     */
    public Tracks getTracks() {
        return tracks;
    }
}
