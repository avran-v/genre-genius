package cs1302.omega.mmsearch;

import com.google.gson.annotations.SerializedName;

/**
 * Represents header of the JSON response.
 * Used by Gson to make object from JSON response.
 */
public class Header {

    @SerializedName("status_code")
    private int statusCode;

    /**
     * Getter method for status code of the header.
     * @return int status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}
