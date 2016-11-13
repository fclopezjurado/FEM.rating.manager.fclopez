package es.upm.miw.ratingmanager.dbmanager.contracts;

import android.provider.BaseColumns;

/**
 * Created by franlopez on 09/11/2016.
 */

public class RatingsContract {

    private RatingsContract() {
    }

    public static class ratingsTableDefinition implements BaseColumns {
        public final static String TABLE_NAME = "ratings";
        public final static String COLUMN_NAME_ID = _ID;
        public final static String COLUMN_NAME_MOVIE_ID = "movieId";
        public final static String COLUMN_NAME_RATING = "rating";
    }
}
