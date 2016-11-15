package es.upm.miw.ratingmanager.contentprovider.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import es.upm.miw.ratingmanager.contentprovider.controller.APIController;
import es.upm.miw.ratingmanager.contentprovider.utils.Utils;
import es.upm.miw.ratingmanager.dbmanager.contracts.UsersContract;
import es.upm.miw.ratingmanager.dbmanager.parcelables.Movie;
import es.upm.miw.ratingmanager.dbmanager.parcelables.User;
import es.upm.miw.ratingmanager.dbmanager.storage.DatabaseStorage;

/**
 * Created by franlopez on 13/11/2016.
 */

public class ApplicationProvider extends ContentProvider {
    public static final String FILTER_FOR_LOGS = "MiW16";
    private static final String AUTHORITY = ApplicationProvider.class.getPackage().getName();
    private static final String APP_USER_HAS_NOT_BEEN_CREATED_OR_RECOVERED =
            "APP USER HAS NOT BEEN CREATED OR RECOVERED = ";

    private static final String MOVIES_ENTITY = "movies";
    private static final String RATINGS_ENTITY = "ratings";

    private static final int ID_OF_URI_FOR_MOVIES = 1;
    private static final int ID_OF_URI_FOR_RATINGS = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MOVIES_ENTITY + "/*", ID_OF_URI_FOR_MOVIES);
        uriMatcher.addURI(AUTHORITY, RATINGS_ENTITY + "/#", ID_OF_URI_FOR_RATINGS);
    }

    private APIController apiController;
    private DatabaseStorage databaseStorage;
    private Utils utils;

    @Override
    public boolean onCreate() {
        this.apiController = new APIController();
        this.databaseStorage = new DatabaseStorage(getContext());
        this.utils = new Utils();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor = null;

        switch (uriMatcher.match(uri)) {
            case ID_OF_URI_FOR_MOVIES:
                String releaseDateGreaterThanOrEqual = uri.getLastPathSegment();
                cursor = this.getMoviesByReleaseDate(releaseDateGreaterThanOrEqual);
                break;
            case ID_OF_URI_FOR_RATINGS:
                int movieID = Integer.parseInt(uri.getLastPathSegment());
                cursor = this.getMovieRatingByMovieID(movieID);
                break;
        }

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long newRatingID = -1;

        switch (uriMatcher.match(uri)) {
            case ID_OF_URI_FOR_MOVIES:
                break;
            case ID_OF_URI_FOR_RATINGS:
                int movieID = Integer.parseInt(uri.getLastPathSegment());

                if (contentValues.containsKey("rating")) {
                    newRatingID = this.databaseStorage.insertRating(movieID,
                            (Double) contentValues.get("rating"));

                    this.apiController.rateMovieByMovieID(this.databaseStorage.getAppUser(),
                            movieID, (Double) contentValues.get("rating"));
                }

                break;
        }

        return ContentUris.withAppendedId(uri, newRatingID);
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ID_OF_URI_FOR_MOVIES:
                return "vnd.android.cursor.dir/vnd.miw." + MOVIES_ENTITY;
            case ID_OF_URI_FOR_RATINGS:
                return "vnd.android.cursor.item/vnd.miw." + RATINGS_ENTITY;
            default:
                return null;
        }
    }

    private Cursor getMoviesByReleaseDate(String releaseDateGreaterThanOrEqual) {
        Cursor cursor = null;

        if (releaseDateGreaterThanOrEqual.length() > 0) {
            String releaseDateLessThanOrEqual = this.utils.addMonthToADate(
                    releaseDateGreaterThanOrEqual);

            User APPUser;
            ArrayList<Movie> movies = this.databaseStorage.getMoviesByReleaseDate(
                    releaseDateGreaterThanOrEqual, releaseDateLessThanOrEqual);

            if (this.databaseStorage.count(UsersContract.usersTableDefinition.TABLE_NAME) == 0)
                APPUser = this.insertAPPUser();
            else
                APPUser = this.databaseStorage.getAppUser();

            if (APPUser.get_sessionId().length() > 0) {
                if (movies.size() == 0) {
                    ArrayList<Movie> moviesFromAPI = this.getMoviesFromAPI(APPUser,
                            releaseDateGreaterThanOrEqual, releaseDateLessThanOrEqual);
                    this.insertMovies(moviesFromAPI);
                } else {
                    Long lastUpdateByAPI = movies.get(0).get_createdAt();
                    Date releaseDate = this.utils.stringToDate(releaseDateGreaterThanOrEqual);
                    Date lastAPIUpdate = this.utils.timeStampToDate(lastUpdateByAPI);

                    if (releaseDate.before(lastAPIUpdate))
                        this.updateMovies(APPUser, releaseDateGreaterThanOrEqual,
                                releaseDateLessThanOrEqual);
                }

                cursor = this.databaseStorage.getMoviesByReleaseDateInCursor(
                        releaseDateGreaterThanOrEqual, releaseDateLessThanOrEqual);
            } else
                Log.i(ApplicationProvider.FILTER_FOR_LOGS,
                        APP_USER_HAS_NOT_BEEN_CREATED_OR_RECOVERED);
        }

        return cursor;
    }

    private void updateMovies(User APPUser, String releaseDateGreaterThanOrEqual,
                              String releaseDateLessThanOrEqual) {
        ArrayList<Movie> moviesFromAPI = this.getMoviesFromAPI(APPUser,
                releaseDateGreaterThanOrEqual, releaseDateLessThanOrEqual);

        this.databaseStorage.deleteMoviesByReleaseDate(releaseDateGreaterThanOrEqual,
                releaseDateLessThanOrEqual);
        this.insertMovies(moviesFromAPI);
    }

    private ArrayList<Movie> getMoviesFromAPI(User APPUser, String releaseDateGreaterThanOrEqual,
                                              String releaseDateLessThanOrEqual) {
        return this.apiController.getMoviesByReleaseDate(APPUser,
                releaseDateGreaterThanOrEqual, releaseDateLessThanOrEqual);

    }

    private void insertMovies(ArrayList<Movie> movies) {
        for (Movie movie : movies) {
            this.databaseStorage.insertMovie(movie.get_movieId(), movie.get_posterPath(),
                    movie.get_overview(), movie.get_releaseDate(), movie.get_title(),
                    movie.get_originalTitle(), movie.get_voteCount(),
                    movie.get_voteAverage(), movie.get_createdAt());
        }
    }

    private User insertAPPUser() {
        User APPUser = new User();
        this.apiController.generateUserSession(APPUser);

        if (APPUser.get_sessionId().length() > 0)
            this.databaseStorage.insertUser(APPUser.get_name(), APPUser.get_password(),
                    APPUser.get_apiKey(), APPUser.get_requestToken(), APPUser.get_sessionId());

        return APPUser;
    }

    private Cursor getMovieRatingByMovieID(int movieID) {
        Cursor cursor = null;

        if (movieID > 0)
            cursor = this.databaseStorage.getRatingByMovieIDInCursor(movieID);

        return cursor;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
