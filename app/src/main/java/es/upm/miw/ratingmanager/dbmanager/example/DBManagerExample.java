package es.upm.miw.ratingmanager.dbmanager.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import es.upm.miw.ratingmanager.R;
import es.upm.miw.ratingmanager.api.example.User;
import es.upm.miw.ratingmanager.api.manager.APIManager;
import es.upm.miw.ratingmanager.api.pojos.Genres;
import es.upm.miw.ratingmanager.api.pojos.Movie;
import es.upm.miw.ratingmanager.api.pojos.Movies;
import es.upm.miw.ratingmanager.api.pojos.SessionResponse;
import es.upm.miw.ratingmanager.dbmanager.contracts.MoviesContract;
import es.upm.miw.ratingmanager.dbmanager.contracts.RatingsContract;
import es.upm.miw.ratingmanager.dbmanager.contracts.UsersContract;
import es.upm.miw.ratingmanager.dbmanager.parcelables.Rating;
import es.upm.miw.ratingmanager.dbmanager.storage.DatabaseStorage;
import retrofit2.Call;
import retrofit2.Response;

public class DBManagerExample extends Activity {
    private static final String FILTER_FOR_LOGS = "MiW16";
    private static final String START_DATE_TO_FIND_FILMS = "2016-10-01";
    private static final String END_DATE_TO_FIND_FILMS = "2016-11-01";

    private static final String UNSUCCESSFUL_REQUEST = "UNSUCCESSFUL REQUEST. ";
    private static final String REQUEST_TOKEN_HAS_NOT_BEEN_DEFINED =
            "REQUEST TOKEN HAS NOT BEEN DEFINED";
    private static final String REQUEST_TOKEN_HAS_ALREADY_BEEN_VALIDATED =
            "REQUEST TOKEN HAS ALREADY BEEN VALIDATED";
    private static final String REQUEST_TOKEN_HAS_NOT_BEEN_VALIDATED =
            "REQUEST TOKEN HAS NOT BEEN VALIDATED";
    private static final String REQUEST_TOKEN_HAS_ALREADY_BEEN_DEFINED =
            "REQUEST TOKEN HAS ALREADY BEEN DEFINED";
    private static final String SESSION_ID_HAS_ALREADY_BEEN_DEFINED =
            "SESSION ID HAS ALREADY BEEN DEFINED";
    private static final String SESSION_ID_HAS_NOT_BEEN_DEFINED =
            "SESSION ID HAS NOT BEEN DEFINED";
    private static final String FILM_GENRES_HAVE_NOT_BEEN_DEFINED =
            "FILM GENRES HAVE NOT BEEN DEFINED";
    private static final String MOVIES_HAS_NOT_BEEN_STORED_IN_DATABASE =
            "MOVIES HAS NOT BEEN STORED IN DATABASE";

    private User user;
    private APIManager apiManager;
    private Genres genres;
    private Movies movies;
    private DatabaseStorage databaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbmanagerexample);

        this.user = new User();
        this.apiManager = new APIManager();
        this.genres = new Genres();
        this.movies = new Movies();
        this.databaseStorage = new DatabaseStorage(getApplicationContext());

        this.initializeApplication();

        Button buttonToGetMoviesByReleaseDate = (Button) findViewById(
                R.id.buttonToGetMoviesByReleaseDate);
        Button buttonToGetRatingByMovieID = (Button) findViewById(
                R.id.buttonToGetRatingByMovieID);
        Button buttonToGetAppUser = (Button) findViewById(
                R.id.buttonToGetAppUser);
        Button buttonToDeleteMoviesByReleaseDate = (Button) findViewById(
                R.id.buttonToDeleteMoviesByReleaseDate);

        buttonToGetMoviesByReleaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMoviesByReleaseDate();
            }
        });

        buttonToGetRatingByMovieID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRatingByMovieID();
            }
        });

        buttonToGetAppUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAppUser();
            }
        });

        buttonToDeleteMoviesByReleaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMoviesByReleaseDate();
            }
        });
    }

    private void initializeApplication() {
        this.generateSessionID();
        this.generateMovies();
    }

    private void generateSessionID() {
        if (this.user.getRequestToken() == null) {
            DBManagerAsyncTask<SessionResponse> myTask = new DBManagerAsyncTask<SessionResponse>(
                    this) {
                @Override
                protected void onPreExecute() {
                }

                @Override
                protected Response<SessionResponse> doInBackground(ListView... ListViews) {
                    Response<SessionResponse> response = null;
                    Call<SessionResponse> call = this.getDbManagerExample().apiManager.
                            getTemporalRequestToken(this.getDbManagerExample().user.getAPIKey());

                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        Log.i(FILTER_FOR_LOGS, e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    return response;
                }

                @Override
                protected void onPostExecute(Response response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            SessionResponse sessionResponse = (SessionResponse) response.body();
                            this.getDbManagerExample().user.setRequestToken(sessionResponse.
                                    getRequestToken());
                            this.getDbManagerExample().validateRequestToken();
                        } else {
                            try {
                                JSONObject JSONErrorObject = new JSONObject(response.errorBody()
                                        .string());
                                Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST
                                        + JSONErrorObject.getString("status_message"));
                                Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST
                                        + " (" + JSONErrorObject.getString("status_code")
                                        + ") " + JSONErrorObject.getString(
                                        "status_message"), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST + e.getMessage());
                                Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST
                                        + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                        Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST,
                                Toast.LENGTH_LONG).show();
                    }
                }
            };

            myTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), REQUEST_TOKEN_HAS_ALREADY_BEEN_DEFINED,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void validateRequestToken() {
        if (this.user.getRequestToken() != null) {
            if (!this.user.isRequestTokenHasBeenValidated()) {
                DBManagerAsyncTask<SessionResponse> myTask = new DBManagerAsyncTask<
                        SessionResponse>(this) {
                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected Response<SessionResponse> doInBackground(ListView... ListViews) {
                        Response<SessionResponse> response = null;
                        Call<SessionResponse> call = this.getDbManagerExample().apiManager.
                                validateRequestToken(this.getDbManagerExample().user.getAPIKey(),
                                        this.getDbManagerExample().user.getUserName(),
                                        this.getDbManagerExample().user.getUserPass(),
                                        this.getDbManagerExample().user.getRequestToken());

                        try {
                            response = call.execute();
                        } catch (IOException e) {
                            Log.i(FILTER_FOR_LOGS, e.getMessage());
                            Toast.makeText(getApplicationContext(), e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        return response;
                    }

                    @Override
                    protected void onPostExecute(Response response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                SessionResponse sessionResponse = (SessionResponse) response.body();

                                this.getDbManagerExample().user.setRequestToken(sessionResponse.
                                        getRequestToken());
                                this.getDbManagerExample().user.setRequestTokenHasBeenValidated(
                                        true);
                                this.getDbManagerExample().getSessionID();
                            } else {
                                try {
                                    JSONObject JSONErrorObject = new JSONObject(
                                            response.errorBody().string());
                                    Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST
                                            + JSONErrorObject.getString("status_message"));
                                    Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST
                                            + " (" + JSONErrorObject.getString("status_code")
                                            + ") " + JSONErrorObject.getString(
                                            "status_message"), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST
                                            + e.getMessage());
                                    Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST
                                            + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                            Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                };

                myTask.execute();
            } else {
                Toast.makeText(getApplicationContext(), REQUEST_TOKEN_HAS_ALREADY_BEEN_VALIDATED,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), REQUEST_TOKEN_HAS_NOT_BEEN_DEFINED,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionID() {
        if (this.user.getRequestToken() != null) {
            if (this.user.isRequestTokenHasBeenValidated()) {
                if ((this.user.getSessionID() == null)) {
                    DBManagerAsyncTask<SessionResponse> myTask = new DBManagerAsyncTask<
                            SessionResponse>(this) {
                        @Override
                        protected void onPreExecute() {
                        }

                        @Override
                        protected Response<SessionResponse> doInBackground(ListView... ListViews) {
                            Response<SessionResponse> response = null;
                            Call<SessionResponse> call = this.getDbManagerExample().apiManager.
                                    getSessionID(this.getDbManagerExample().user.getAPIKey(),
                                            this.getDbManagerExample().user.getRequestToken());

                            try {
                                response = call.execute();
                            } catch (IOException e) {
                                Log.i(FILTER_FOR_LOGS, e.getMessage());
                                Toast.makeText(getApplicationContext(), e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            return response;
                        }

                        @Override
                        protected void onPostExecute(Response response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    SessionResponse sessionResponse = (SessionResponse)
                                            response.body();

                                    this.getDbManagerExample().user.setSessionID(sessionResponse.
                                            getSessionID());
                                } else {
                                    try {
                                        JSONObject JSONErrorObject = new JSONObject(
                                                response.errorBody().string());
                                        Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST
                                                + JSONErrorObject.getString("status_message"));
                                        Toast.makeText(getApplicationContext(),
                                                UNSUCCESSFUL_REQUEST + " ("
                                                        + JSONErrorObject.getString(
                                                        "status_code")
                                                        + ") " + JSONErrorObject.getString(
                                                        "status_message"),
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST
                                                + e.getMessage());
                                        Toast.makeText(getApplicationContext(),
                                                UNSUCCESSFUL_REQUEST + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                                Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    myTask.execute();
                } else {
                    Toast.makeText(getApplicationContext(), SESSION_ID_HAS_ALREADY_BEEN_DEFINED,
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), REQUEST_TOKEN_HAS_NOT_BEEN_VALIDATED,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), REQUEST_TOKEN_HAS_NOT_BEEN_DEFINED,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void generateMovies() {
        DBManagerAsyncTask<Genres> myTask = new DBManagerAsyncTask<Genres>(this) {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Response<Genres> doInBackground(ListView... ListViews) {
                Response<Genres> response = null;
                Call<Genres> call = this.getDbManagerExample().apiManager.
                        getGenres(this.getDbManagerExample().user.getAPIKey());

                try {
                    response = call.execute();
                } catch (IOException e) {
                    Log.i(FILTER_FOR_LOGS, e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

                return response;
            }

            @Override
            protected void onPostExecute(Response response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        this.getDbManagerExample().genres = (Genres) response.body();
                        this.getDbManagerExample().getMovies();
                    } else {
                        try {
                            JSONObject JSONErrorObject = new JSONObject(response.errorBody()
                                    .string());
                            Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST
                                    + JSONErrorObject.getString("status_message"));
                            Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST
                                    + " (" + JSONErrorObject.getString("status_code")
                                    + ") " + JSONErrorObject.getString(
                                    "status_message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST + e.getMessage());
                            Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST
                                    + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                    Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST,
                            Toast.LENGTH_LONG).show();
                }
            }
        };

        myTask.execute();
    }

    private void getMovies() {
        if (this.genres.getGenres().size() > 0) {
            DBManagerAsyncTask<Movies> myTask = new DBManagerAsyncTask<Movies>(this) {
                @Override
                protected void onPreExecute() {
                }

                @Override
                protected Response<Movies> doInBackground(ListView... ListViews) {
                    Response<Movies> response = null;
                    Call<Movies> call = this.getDbManagerExample().apiManager.
                            getMovies(this.getDbManagerExample().user.getAPIKey(), 1,
                                    START_DATE_TO_FIND_FILMS,
                                    END_DATE_TO_FIND_FILMS,
                                    this.getDbManagerExample().genres.getHorrorGenreID(),
                                    this.getDbManagerExample().genres.genresIDsToString(false));

                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        Log.i(FILTER_FOR_LOGS, e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    return response;
                }

                @Override
                protected void onPostExecute(Response response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            this.getDbManagerExample().movies = (Movies) response.body();
                        } else {
                            try {
                                JSONObject JSONErrorObject = new JSONObject(response.errorBody()
                                        .string());
                                Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST
                                        + JSONErrorObject.getString("status_message"));
                                Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST
                                        + " (" + JSONErrorObject.getString("status_code")
                                        + ") " + JSONErrorObject.getString(
                                        "status_message"), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST + e.getMessage());
                                Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST
                                        + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                        Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST,
                                Toast.LENGTH_LONG).show();
                    }
                }
            };

            myTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), FILM_GENRES_HAVE_NOT_BEEN_DEFINED,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getMoviesByReleaseDate() {
        if (this.movies.getMovies().size() > 0) {
            if (this.databaseStorage.count(MoviesContract.moviesTableDefinition.TABLE_NAME) == 0)
                this.insertMoviesInDB();

            ListView moviesList = (ListView) findViewById(R.id.list);
            ArrayList<String> movies = new ArrayList<>();

            for (es.upm.miw.ratingmanager.dbmanager.parcelables.Movie movie :
                    this.databaseStorage.getMoviesByReleaseDate(START_DATE_TO_FIND_FILMS,
                            END_DATE_TO_FIND_FILMS))
                movies.add(movie.toString());

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    movies);

            moviesList.setAdapter(arrayAdapter);
        } else {
            Toast.makeText(getApplicationContext(), MOVIES_HAS_NOT_BEEN_STORED_IN_DATABASE,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void insertMoviesInDB() {
        for (Movie movie : this.movies.getMovies())
            this.databaseStorage.insertMovie(movie.getId(), movie.getPosterPath(),
                    movie.getOverview(), movie.getReleaseDate(), movie.getTitle(),
                    movie.getOriginalTitle(), movie.getVoteCount(), movie.getVoteAverage(),
                    System.currentTimeMillis());
    }

    private void getRatingByMovieID() {
        if (this.databaseStorage.count(MoviesContract.moviesTableDefinition.TABLE_NAME) > 0) {
            ArrayList<es.upm.miw.ratingmanager.dbmanager.parcelables.Movie> movies = this
                    .databaseStorage.getMoviesByReleaseDate(START_DATE_TO_FIND_FILMS,
                            END_DATE_TO_FIND_FILMS);

            if (this.databaseStorage.count(RatingsContract.ratingsTableDefinition.TABLE_NAME) == 0)
                this.insertRatingInDB(movies.get(0));

            ListView ratingsList = (ListView) findViewById(R.id.list);
            ArrayList<String> ratings = new ArrayList<>();
            Rating rating = this.databaseStorage.getRatingByMovieID(movies.get(0).get_movieId());

            ratings.add(rating.toString());

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    ratings);

            ratingsList.setAdapter(arrayAdapter);
        } else {
            Toast.makeText(getApplicationContext(), MOVIES_HAS_NOT_BEEN_STORED_IN_DATABASE,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void insertRatingInDB(es.upm.miw.ratingmanager.dbmanager.parcelables.Movie movie) {
        this.databaseStorage.insertRating(movie.get_movieId(), 6.78);
    }

    private void getAppUser() {
        if (this.user.getSessionID() != null) {
            if (this.databaseStorage.count(UsersContract.usersTableDefinition.TABLE_NAME) == 0)
                this.insertAppUserInDB();

            ListView usersList = (ListView) findViewById(R.id.list);
            ArrayList<String> users = new ArrayList<>();
            es.upm.miw.ratingmanager.dbmanager.parcelables.User user = this.databaseStorage
                    .getAppUser();

            users.add(user.toString());

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    users);

            usersList.setAdapter(arrayAdapter);
        } else {
            Toast.makeText(getApplicationContext(), SESSION_ID_HAS_NOT_BEEN_DEFINED,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void insertAppUserInDB() {
        this.databaseStorage.insertUser(this.user.getUserName(), this.user.getUserPass(),
                this.user.getAPIKey(), this.user.getRequestToken(), this.user.getSessionID());
    }

    private void deleteMoviesByReleaseDate() {
        this.databaseStorage.deleteMoviesByReleaseDate(START_DATE_TO_FIND_FILMS,
                END_DATE_TO_FIND_FILMS);

        ListView moviesList = (ListView) findViewById(R.id.list);
        ArrayList<String> movies = new ArrayList<>();

        for (es.upm.miw.ratingmanager.dbmanager.parcelables.Movie movie :
                this.databaseStorage.getMoviesByReleaseDate(START_DATE_TO_FIND_FILMS,
                        END_DATE_TO_FIND_FILMS))
            movies.add(movie.toString());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                movies);

        moviesList.setAdapter(arrayAdapter);
    }
}
