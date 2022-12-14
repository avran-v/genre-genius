package cs1302.omega.mmsnippet;

/**
 * Represents full response from Musixmatch API request (snippet request).
 * Used by Gson to make object from JSON response.
 */
public class SnippetResponse {
    private Message message;

    /**
     * Getter method to return Message of a response.
     * @return Message message
     */
    public Message getMessage() {
        return message;
    }
}
