package es.upm.miw.ratingmanager.api;

/**
 * Created by franlopez on 07/11/2016.
 */

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class TemporalRequestToken {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("expires_at")
    @Expose
    private String expiresAt;
    @SerializedName("request_token")
    @Expose
    private String requestToken;

    /**
     *
     * @return
     * The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The expiresAt
     */
    public String getExpiresAt() {
        return expiresAt;
    }

    /**
     *
     * @param expiresAt
     * The expires_at
     */
    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     *
     * @return
     * The requestToken
     */
    public String getRequestToken() {
        return requestToken;
    }

    /**
     *
     * @param requestToken
     * The request_token
     */
    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

}