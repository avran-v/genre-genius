package cs1302.omega.lastfm;

/**
 * Represents entire result of response from LastFM API.
 * Class to store all top tags and their corresponding information.
 * Used by Gson to make an object from JSON response.
 */
public class TopTagsResponse {
    private TopTags toptags;

    /**
     * Getter method to return entire result.
     * Additional getter methods needed to get complete info.
     * @return TopTags toptags
     */
    public TopTags getTopTags () {
        return toptags;
    }
}
