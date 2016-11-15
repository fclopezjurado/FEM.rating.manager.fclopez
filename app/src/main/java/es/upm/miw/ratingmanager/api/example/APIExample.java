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

import java.io.IOException;

import es.upm.miw.ratingmanager.R;
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
            TextView textViewToShowAPIResponse = (TextView) findViewById(R.id.APIResponse);
            textViewToShowAPIResponse.setMovementMethod(new ScrollingMovementMethod());

            APIAsyncTask<SessionResponse> myTask = new APIAsyncTask<SessionResponse>(this) {
                @Override
                protected void onPreExecute() {
                }

                @Override
                protected Response<SessionResponse> doInBackground(TextView... TextViews) {
                    Response<SessionResponse> response = null;
                    Call<SessionResponse> call = this.getApiExample().apiManager.
                            getTemporalRequestToken(this.getApiExample().user.getAPIKey());

                    this.setTextView(TextViews[0]);

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

                            this.getTextView().setText(sessionResponse.toString());
                            this.getApiExample().user.setRequestToken(sessionResponse.
                                    getRequestToken());
                        } else {
                            try {
                                JSONObject JSONErrorObject = new JSONObject(response.errorBody()
                                        .string());
                                Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST
                                        + JSONErrorObject.getString("status_message"));
                                Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST + " ("
                                                + JSONErrorObject.getString("status_code") + ") "
                                                + JSONErrorObject.getString("status_message"),
                                        Toast.LENGTH_SHORT).show();
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

            myTask.execute(textViewToShowAPIResponse);
        } else {
            Log.i(FILTER_FOR_LOGS, REQUEST_TOKEN_HAS_ALREADY_BEEN_DEFINED);
            Toast.makeText(getApplicationContext(), REQUEST_TOKEN_HAS_ALREADY_BEEN_DEFINED,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void validateRequestToken() {
        if (this.user.getRequestToken() != null) {
            if (!this.user.isRequestTokenHasBeenValidated()) {
                TextView textViewToShowAPIResponse = (TextView) findViewById(R.id.APIResponse);
                textViewToShowAPIResponse.setMovementMethod(new ScrollingMovementMethod());

                APIAsyncTask<SessionResponse> myTask = new APIAsyncTask<SessionResponse>(this) {
                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected Response<SessionResponse> doInBackground(TextView... TextViews) {
                        Response<SessionResponse> response = null;
                        Call<SessionResponse> call = this.getApiExample().apiManager.
                                validateRequestToken(this.getApiExample().user.getAPIKey(),
                                        this.getApiExample().user.getUserName(),
                                        this.getApiExample().user.getUserPass(),
                                        this.getApiExample().user.getRequestToken());

                        this.setTextView(TextViews[0]);

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

                                this.getTextView().setText(sessionResponse.toString());
                                this.getApiExample().user.setRequestToken(sessionResponse.
                                        getRequestToken());
                                this.getApiExample().user.setRequestTokenHasBeenValidated(true);
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

                myTask.execute(textViewToShowAPIResponse);
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
                    TextView textViewToShowAPIResponse = (TextView) findViewById(R.id.APIResponse);
                    textViewToShowAPIResponse.setMovementMethod(new ScrollingMovementMethod());

                    APIAsyncTask<SessionResponse> myTask = new APIAsyncTask<SessionResponse>(this) {
                        @Override
                        protected void onPreExecute() {
                        }

                        @Override
                        protected Response<SessionResponse> doInBackground(TextView... TextViews) {
                            Response<SessionResponse> response = null;
                            Call<SessionResponse> call = this.getApiExample().apiManager.
                                    getSessionID(this.getApiExample().user.getAPIKey(),
                                            this.getApiExample().user.getRequestToken());

                            this.setTextView(TextViews[0]);

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
                                    this.getTextView().setText(
                                            sessionResponse.toString());

                                    this.getApiExample().user.setSessionID(sessionResponse.
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

                    myTask.execute(textViewToShowAPIResponse);
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
        TextView textViewToShowAPIResponse = (TextView) findViewById(R.id.APIResponse);
        textViewToShowAPIResponse.setMovementMethod(new ScrollingMovementMethod());

        APIAsyncTask<Genres> myTask = new APIAsyncTask<Genres>(this) {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Response<Genres> doInBackground(TextView... TextViews) {
                Response<Genres> response = null;
                Call<Genres> call = this.getApiExample().apiManager.
                        getGenres(this.getApiExample().user.getAPIKey());

                this.setTextView(TextViews[0]);

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
                        Genres genres = (Genres) response.body();
                        this.getApiExample().genres = genres;

                        this.getTextView().setText(genres.toString());
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

        myTask.execute(textViewToShowAPIResponse);
    }

    private void getMovies() {
        if (this.genres.getGenres().size() > 0) {
            TextView textViewToShowAPIResponse = (TextView) findViewById(R.id.APIResponse);
            textViewToShowAPIResponse.setMovementMethod(new ScrollingMovementMethod());

            APIAsyncTask<Movies> myTask = new APIAsyncTask<Movies>(this) {
                @Override
                protected void onPreExecute() {
                }

                @Override
                protected Response<Movies> doInBackground(TextView... TextViews) {
                    Response<Movies> response = null;
                    Call<Movies> call = this.getApiExample().apiManager.
                            getMovies(this.getApiExample().user.getAPIKey(), 1,
                                    START_DATE_TO_FIND_FILMS,
                                    END_DATE_TO_FIND_FILMS,
                                    this.getApiExample().genres.getHorrorGenreID(),
                                    this.getApiExample().genres.genresIDsToString(false));

                    this.setTextView(TextViews[0]);

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
                            Movies movies = (Movies) response.body();
                            this.getApiExample().movies = movies;

                            this.getTextView().setText(movies.toString());

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

            myTask.execute(textViewToShowAPIResponse);
        } else {
            Toast.makeText(getApplicationContext(), FILM_GENRES_HAVE_NOT_BEEN_DEFINED,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void rateMovie() {
        if (this.user.getSessionID() != null) {
            if (this.movies.getMovies().size() > 0) {
                TextView textViewToShowAPIResponse = (TextView) findViewById(R.id.APIResponse);
                textViewToShowAPIResponse.setMovementMethod(new ScrollingMovementMethod());

                APIAsyncTask<RatingResponse> myTask = new APIAsyncTask<RatingResponse>(this) {
                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected Response<RatingResponse> doInBackground(TextView... TextViews) {
                        Response<RatingResponse> response = null;
                        Call<RatingResponse> call = this.getApiExample().apiManager.
                                rateMovie(this.getApiExample().movies.getMovies().get(0).getId(),
                                        this.getApiExample().user.getAPIKey(),
                                        this.getApiExample().user.getSessionID(), 6.5);

                        this.setTextView(TextViews[0]);

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
                                RatingResponse ratingResponse = (RatingResponse) response.body();
                                this.getTextView().setText(ratingResponse.toString());
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

                myTask.execute(textViewToShowAPIResponse);
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
