package cs1302.omega.mmsearch;

/**
 * Represents part of response from Musixmatch API (search request).
 * Used by Gson to make object from Gson response.
 */
public class TrackList {
    private MMTrack track;

    /**
     * Getter method for track object TrackList contains.
     * @return MMTrack
     */
    public MMTrack getTrack() {
        return track;
    }
}
