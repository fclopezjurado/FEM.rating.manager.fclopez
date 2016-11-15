package es.upm.miw.ratingmanager.api.example;

/**
 * Created by franlopez on 09/11/2016.
 */

public class User {
    private static final String API_KEY = "1eaa79705818c190d2059a6b0fc02146";
    private static final String USER_NAME = "fclopez";
    private static final String USER_PASS = "wfJKfi7EyM";

    private String APIKey;
    private String userName;
    private String userPass;
    private String requestToken;
    private boolean requestTokenHasBeenValidated;
    private String sessionID;

    public User() {
        this.setAPIKey(API_KEY);
        this.setUserName(USER_NAME);
        this.setUserPass(USER_PASS);
        this.setRequestToken(null);
        this.setRequestTokenHasBeenValidated(false);
        this.setSessionID(null);
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public boolean isRequestTokenHasBeenValidated() {
        return requestTokenHasBeenValidated;
    }

    public void setRequestTokenHasBeenValidated(boolean requestTokenHasBeenValidated) {
        this.requestTokenHasBeenValidated = requestTokenHasBeenValidated;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }
}
