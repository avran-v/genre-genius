package cs1302.omega.mmsearch;

/**
 * Represents entire message from request to Musixmatch API (search request).
 * Used by Gson to make object from JSON response.
 */
public class Message {

    private Header header;

    private Body body;


    /**
     * Getter method for body of message.
     * @return Body
     */
    public Body getBody() {
        return body;
    }

    /**
     * Getter method for header of message.
     * @return Header
     */
    public Header getHeader() {
        return header;
    }

}
