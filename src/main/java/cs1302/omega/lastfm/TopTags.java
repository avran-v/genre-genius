package cs1302.omega.lastfm;

/**
 * Represent part of a result from LastFM API.
 * Class for storing all tags received through request.
 * Used by Gson to make an object from JSON response.
 */
public class TopTags {
    private Tag[] tag;

    /**
     * Getter method for array with all Tag objects.
     * @return Tag[] tag
     */
    public Tag[] getTags() {
        return tag;
    }
}
