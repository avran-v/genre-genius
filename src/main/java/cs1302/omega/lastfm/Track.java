package cs1302.omega.lastfm;

/**
 * Represents part of a response from LastFM API.
 * Class to represent information about a single track.
 * Used by Gson to create an object from JSON response.
 */
public class Track {
    private Artist artist;
    private String name;

    /**
     * Getter method for artist of a track.
     * @return Artist artist
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * Getter method for name of a track.
     * @return String name
     */
    public String getName() {
        return name;
    }
}
