package cs1302.omega.mmsnippet;

/**
 * Represents part of response from Musixmatch API (snippet request).
 * Used by Gson to make object from JSON response.
 */
public class Message {
    private Body body;

    /**
     * Getter method for Body of a Message.
     * @return Body body
     */
    public Body getBody() {
        return body;
    }
}
