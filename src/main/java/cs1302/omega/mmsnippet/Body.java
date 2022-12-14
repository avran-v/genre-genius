package cs1302.omega.mmsnippet;

/**
 * Represents part of response from Musixmatch API (snippet request).
 * Body of message from response given by API.
 * Used by Gson to make object from JSON response.
 */
public class Body {
    private Snippet snippet;

    /**
     * Getter method for Snippet from Body object.
     * @return Snippet snippet
     */
    public Snippet getSnippet() {
        return snippet;
    }
}
