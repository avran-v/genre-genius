package cs1302.omega.lastfm;

/**
 * Represents part of a result from the LastFM search API.
 * Contains information about the artist of a music track.
 * Used by Gson to create an object from JSON response.
 */
public class Artist {
    private String name;

    /**
     * Getter method to return the name of an artist.
     * @return String name
     */
    public String getName() {
        return name;
    }

}
