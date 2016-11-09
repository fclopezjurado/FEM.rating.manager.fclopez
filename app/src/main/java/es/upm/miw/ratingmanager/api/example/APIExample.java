package es.upm.miw.ratingmanager.api.example;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import es.upm.miw.ratingmanager.R;
import es.upm.miw.ratingmanager.api.definition.CallbackWithArgument;
import es.upm.miw.ratingmanager.api.manager.APIManager;
import es.upm.miw.ratingmanager.api.pojos.Genres;
import es.upm.miw.ratingmanager.api.pojos.Movies;
import es.upm.miw.ratingmanager.api.pojos.RatingResponse;
import es.upm.miw.ratingmanager.api.pojos.SessionResponse;
import retrofit2.Call;
import retrofit2.Response;

public class APIExample extends Activity {
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
    private static final String THERE_ARE_NOT_MOVIES_TO_RATE =
            "THERE ARE NOT MOVIES TO RATE";

    private User user;
    private APIManager apiManager;
    private Genres genres;
    private Movies movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiexample);

        this.user = new User();
        this.apiManager = new APIManager();
        this.genres = new Genres();
        this.movies = new Movies();

        Button buttonToGetTemporalRequestToken = (Button) findViewById(
                R.id.buttonToGetTemporalRequestToken);
        Button buttonToValidateTemporalRequestToken = (Button) findViewById(
                R.id.buttonToValidateTemporalRequestToken);
        Button buttonToGetSessionID = (Button) findViewById(
                R.id.buttonToGetSessionID);
        Button buttonToGetGenres = (Button) findViewById(
                R.id.buttonToGetGenres);
        Button buttonToGetHorrorMovies = (Button) findViewById(
                R.id.buttonToGetMovies);
        Button buttonToRateMovie = (Button) findViewById(
                R.id.buttonToRateMovie);

        buttonToGetTemporalRequestToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTemporalRequestToken();
            }
        });

        buttonToValidateTemporalRequestToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateRequestToken();
            }
        });

        buttonToGetSessionID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSessionID();
            }
        });

        buttonToGetGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGenres();
            }
        });

        buttonToGetHorrorMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMovies();
            }
        });

        buttonToRateMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateMovie();
            }
        });
    }

    private void getTemporalRequestToken() {
        if (this.user.getRequestToken() == null) {
            this.apiManager.getTemporalRequestToken(this.user.getAPIKey(),
                    new CallbackWithArgument<SessionResponse, APIExample>(this) {

                        @Override
                        public void onResponse(Call<SessionResponse> call,
                                               Response<SessionResponse> response) {
                            if (response.body() != null) {
                                SessionResponse sessionResponse = response.body();
                                TextView textViewToShowAPIResponse = (TextView) findViewById(
                                        R.id.APIResponse);
                                textViewToShowAPIResponse.setMovementMethod(
                                        new ScrollingMovementMethod());

                                textViewToShowAPIResponse.setText(sessionResponse.toString());
                                this.getArgument().user.setRequestToken(sessionResponse.
                                        getRequestToken());
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
                        }

                        @Override
                        public void onFailure(Call<SessionResponse> call, Throwable t) {
                            Log.i(FILTER_FOR_LOGS, t.getMessage());
                            Toast.makeText(getApplicationContext(), t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), REQUEST_TOKEN_HAS_ALREADY_BEEN_DEFINED,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void validateRequestToken() {
        if (this.user.getRequestToken() != null) {
            if (!this.user.isRequestTokenHasBeenValidated()) {
                this.apiManager.validateRequestToken(this.user.getAPIKey(), this.user.getUserName(),
                        this.user.getUserPass(), this.user.getRequestToken(),
                        new CallbackWithArgument<SessionResponse, APIExample>(this) {

                            @Override
                            public void onResponse(Call<SessionResponse> call,
                                                   Response<SessionResponse> response) {
                                if (response.body() != null) {
                                    SessionResponse sessionResponse = response.body();
                                    TextView textViewToShowAPIResponse = (TextView) findViewById(
                                            R.id.APIResponse);

                                    textViewToShowAPIResponse.setText(sessionResponse.toString());
                                    textViewToShowAPIResponse.setMovementMethod(
                                            new ScrollingMovementMethod());

                                    this.getArgument().user.setRequestToken(sessionResponse.
                                            getRequestToken());
                                    this.getArgument().user.setRequestTokenHasBeenValidated(true);
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
                            }

                            @Override
                            public void onFailure(Call<SessionResponse> call, Throwable t) {
                                Log.i(FILTER_FOR_LOGS, t.getMessage());
                                Toast.makeText(getApplicationContext(), t.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
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
                    this.apiManager.getSessionID(this.user.getAPIKey(), this.user.getRequestToken(),
                            new CallbackWithArgument<SessionResponse, APIExample>(this) {

                                @Override
                                public void onResponse(Call<SessionResponse> call,
                                                       Response<SessionResponse> response) {
                                    if (response.body() != null) {
                                        SessionResponse sessionResponse = response.body();
                                        TextView textViewToShowAPIResponse = (TextView)
                                                findViewById(R.id.APIResponse);

                                        textViewToShowAPIResponse.setText(
                                                sessionResponse.toString());
                                        textViewToShowAPIResponse.setMovementMethod(
                                                new ScrollingMovementMethod());

                                        this.getArgument().user.setSessionID(sessionResponse.
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
                                }

                                @Override
                                public void onFailure(Call<SessionResponse> call, Throwable t) {
                                    Log.i(FILTER_FOR_LOGS, t.getMessage());
                                    Toast.makeText(getApplicationContext(), t.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
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

    private void getGenres() {
        this.apiManager.getGenres(this.user.getAPIKey(), new CallbackWithArgument<Genres,
                APIExample>(this) {

            @Override
            public void onResponse(Call<Genres> call,
                                   Response<Genres> response) {
                if (response.body() != null) {
                    Genres genres = response.body();
                    TextView textViewToShowAPIResponse = (TextView) findViewById(
                            R.id.APIResponse);
                    this.getArgument().genres = genres;

                    textViewToShowAPIResponse.setText(genres.toString());
                    textViewToShowAPIResponse.setMovementMethod(
                            new ScrollingMovementMethod());
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
            }

            @Override
            public void onFailure(Call<Genres> call, Throwable t) {
                Log.i(FILTER_FOR_LOGS, t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMovies() {
        if (this.genres.getGenres().size() > 0) {
            this.apiManager.getMovies(this.user.getAPIKey(), 1, START_DATE_TO_FIND_FILMS,
                    END_DATE_TO_FIND_FILMS, this.genres.getHorrorGenreID(),
                    this.genres.genresIDsToString(false),
                    new CallbackWithArgument<Movies, APIExample>(this) {

                        @Override
                        public void onResponse(Call<Movies> call,
                                               Response<Movies> response) {
                            if (response.body() != null) {
                                Movies movies = response.body();
                                TextView textViewToShowAPIResponse = (TextView) findViewById(
                                        R.id.APIResponse);
                                textViewToShowAPIResponse.setMovementMethod(
                                        new ScrollingMovementMethod());

                                textViewToShowAPIResponse.setText(movies.toString());
                                this.getArgument().movies = movies;
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
                        }

                        @Override
                        public void onFailure(Call<Movies> call, Throwable t) {
                            Log.i(FILTER_FOR_LOGS, t.getMessage());
                            Toast.makeText(getApplicationContext(), t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), FILM_GENRES_HAVE_NOT_BEEN_DEFINED,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void rateMovie() {
        if (this.user.getSessionID() != null) {
            if (this.movies.getMovies().size() > 0) {
                this.apiManager.rateMovie(this.movies.getMovies().get(0).getId(),
                        this.user.getAPIKey(), this.user.getSessionID(), 6.5,
                        new CallbackWithArgument<RatingResponse, APIExample>(this) {

                            @Override
                            public void onResponse(Call<RatingResponse> call,
                                                   Response<RatingResponse> response) {
                                if (response.body() != null) {
                                    RatingResponse ratingResponse = response.body();
                                    TextView textViewToShowAPIResponse = (TextView) findViewById(
                                            R.id.APIResponse);
                                    textViewToShowAPIResponse.setMovementMethod(
                                            new ScrollingMovementMethod());

                                    textViewToShowAPIResponse.setText(ratingResponse.toString());
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
                            }

                            @Override
                            public void onFailure(Call<RatingResponse> call, Throwable t) {
                                Log.i(FILTER_FOR_LOGS, t.getMessage());
                                Toast.makeText(getApplicationContext(), t.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), THERE_ARE_NOT_MOVIES_TO_RATE,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), SESSION_ID_HAS_NOT_BEEN_DEFINED,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
