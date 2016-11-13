package es.upm.miw.ratingmanager.dbmanager.contracts;

import android.provider.BaseColumns;

/**
 * Created by franlopez on 09/11/2016.
 */

public class MoviesContract {

    private MoviesContract() {
    }

    public static class moviesTableDefinition implements BaseColumns {
        public final static String TABLE_NAME = "movies";
        public final static String COLUMN_NAME_ID = _ID;
        public final static String COLUMN_NAME_MOVIE_ID = "movieId";
        public final static String COLUMN_NAME_POSTER_PATH = "posterPath";
        public final static String COLUMN_NAME_OVERVIEW = "overview";
        public final static String COLUMN_NAME_RELEASE_DATE = "releaseDate";
        public final static String COLUMN_NAME_MOVIE_TITLE = "title";
        public final static String COLUMN_NAME_ORIGINAL_TITLE = "originalTitle";
        public final static String COLUMN_NAME_VOTE_COUNT = "voteCount";
        public final static String COLUMN_NAME_VOTE_AVERAGE = "voteAverage";
        public final static String COLUMN_NAME_RECORD_CREATED_AT = "createdAt";
    }
}
