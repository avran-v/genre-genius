package cs1302.omega.mmsnippet;

import com.google.gson.annotations.SerializedName;

/**
 * Represents part of a response from Musixmatch API from snippet call.
 * Class for language and actual text of a snippet of lyrics from a song.
 * Used by Gson to make an object from JSON response.
 */
public class Snippet {

    @SerializedName("snippet_language")
    private String snippetLanguage;

    @SerializedName("snippet_body")
    private String snippetBody;

    /**
     * Returns the snippet from a particular song.
     * @return String snippetBody
     */
    public String getSnippetBody() {
        return snippetBody;
    }

}
