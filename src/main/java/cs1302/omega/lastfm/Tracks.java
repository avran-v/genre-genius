package cs1302.omega.lastfm;

/**
 * Represents part of a response from the LastFM API.
 * Class to represent top tracks of a specific tag (multiple tracks, 1 tag).
 * Used by Gson to make an object from JSON response.
 */
public class Tracks {
    private Track[] track;


    /**
     * Getter method for all tracks of a tag.
     * @return Track[] track
     */
    public Track[] getTrack() {
        return track;
    }
}
