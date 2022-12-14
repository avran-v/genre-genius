package cs1302.omega.mmsearch;

/**
 * Represents one response from Musixmatch API (search request).
 * Used by Gson to make object from JSON response.
 */
public class TrackSearchResponse {
    private Message message;

    /**
     * Getter method for message response contains.
     * @return Messaage message
     */
    public Message getMessage() {
        return message;
    }
}
