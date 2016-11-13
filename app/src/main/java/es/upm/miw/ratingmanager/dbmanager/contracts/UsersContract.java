package es.upm.miw.ratingmanager.dbmanager.contracts;

import android.provider.BaseColumns;

/**
 * Created by franlopez on 09/11/2016.
 */

public class UsersContract {

    private UsersContract() {
    }

    public static class usersTableDefinition implements BaseColumns {
        public final static String TABLE_NAME = "users";
        public final static String COLUMN_NAME_ID = _ID;
        public final static String COLUMN_NAME_USER_NAME = "name";
        public final static String COLUMN_NAME_USER_PASSWORD = "password";
        public final static String COLUMN_NAME_USER_API_KEY = "apiKey";
        public final static String COLUMN_NAME_REQUEST_TOKEN = "requestToken";
        public final static String COLUMN_NAME_SESSION_ID = "sessionId";
    }
}
