package cs1302.omega.lastfm;

/**
 * Represnts part of a result from the LastFM search API.
 * Class for an individual tag (genre).
 * Used by Gson to make an object from JSON response.
 */
public class Tag {
    private String name;

    /**
     * Getter method to return name of tag.
     * @return String name
     */
    public String getName() {
        return name;
    }
}
