package es.upm.miw.ratingmanager.contentprovider.controller;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import es.upm.miw.ratingmanager.api.manager.APIManager;
import es.upm.miw.ratingmanager.api.pojos.Genres;
import es.upm.miw.ratingmanager.api.pojos.Movies;
import es.upm.miw.ratingmanager.api.pojos.RatingResponse;
import es.upm.miw.ratingmanager.api.pojos.SessionResponse;
import es.upm.miw.ratingmanager.contentprovider.provider.ApplicationProvider;
import es.upm.miw.ratingmanager.dbmanager.parcelables.Movie;
import es.upm.miw.ratingmanager.dbmanager.parcelables.User;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by franlopez on 13/11/2016.
 */

public class APIController {
    private static final String UNSUCCESSFUL_REQUEST = "UNSUCCESSFUL REQUEST. ";
    private static final String THERE_ARE_NOT_MOVIES_WITH_RELEASE_DATE =
            "THERE ARE NOT MOVIES WITH RELEASE DATE = ";

    private APIManager apiManager;

    public APIController() {
        this.apiManager = new APIManager();
    }

    public ArrayList<Movie> getMoviesByReleaseDate(User APPUser,
                                                   String releaseDateGreaterThanOrEqual,
                                                   String releaseDateLessThanOrEqual) {
        ArrayList<Movie> movies = new ArrayList<>();
        Call<Genres> requestToGetGenres = this.apiManager.getGenres(APPUser.get_apiKey());

        try {
            Response<Genres> response = requestToGetGenres.execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    Genres genres = response.body();
                    movies = this.requestToGetMoviesByReleaseDate(APPUser,
                            releaseDateGreaterThanOrEqual, releaseDateLessThanOrEqual, genres,
                            movies, 1);

                    if (movies.size() == 0)
                        Log.i(ApplicationProvider.FILTER_FOR_LOGS,
                                THERE_ARE_NOT_MOVIES_WITH_RELEASE_DATE);
                } else {
                    Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                }
            } else
                Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
        } catch (IOException e) {
            Log.i(ApplicationProvider.FILTER_FOR_LOGS, e.getMessage());
        }

        return movies;
    }

    private ArrayList<Movie> requestToGetMoviesByReleaseDate(User APPUser,
                                                             String releaseDateGreaterThanOrEqual,
                                                             String releaseDateLessThanOrEqual,
                                                             Genres genres,
                                                             ArrayList<Movie> movies,
                                                             int currentPage) {
        Call<Movies> requestToGetMovies = this.apiManager.getMovies(
                APPUser.get_apiKey(), currentPage, releaseDateGreaterThanOrEqual,
                releaseDateLessThanOrEqual, genres.getHorrorGenreID(),
                genres.genresIDsToString(false));

        try {
            Response<Movies> response = requestToGetMovies.execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    Movies moviesByResponse = response.body();

                    for (es.upm.miw.ratingmanager.api.pojos.Movie movie :
                            moviesByResponse.getMovies()) {
                        Movie movieToDB = new Movie(movie.getId(), movie.getId(),
                                movie.getPosterPath(), movie.getOverview(), movie.getReleaseDate(),
                                movie.getTitle(), movie.getOriginalTitle(), movie.getVoteCount(),
                                movie.getVoteAverage(), System.currentTimeMillis());
                        movies.add(movieToDB);
                    }

                    if (currentPage < moviesByResponse.getTotalPages())
                        this.requestToGetMoviesByReleaseDate(APPUser,
                                releaseDateGreaterThanOrEqual, releaseDateLessThanOrEqual, genres,
                                movies, currentPage + 1);
                } else {
                    Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                }
            } else {
                Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
            }
        } catch (IOException e) {
            Log.i(ApplicationProvider.FILTER_FOR_LOGS, e.getMessage());
        }

        return movies;
    }

    public void generateUserSession(User APPUser) {
        Call<SessionResponse> requestToGetTemporalRequestToken = this.apiManager.
                getTemporalRequestToken(APPUser.get_apiKey());

        try {
            Response<SessionResponse> response = requestToGetTemporalRequestToken.execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    SessionResponse sessionResponse = response.body();

                    APPUser.set_requestToken(sessionResponse.getRequestToken());
                    this.requestToValidateRequestToken(APPUser);
                } else {
                    Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                }
            } else {
                Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
            }
        } catch (IOException e) {
            Log.i(ApplicationProvider.FILTER_FOR_LOGS, e.getMessage());
        }
    }

    private void requestToValidateRequestToken(User APPUser) {
        Call<SessionResponse> requestToValidateRequestToken = this.apiManager.validateRequestToken(
                APPUser.get_apiKey(), APPUser.get_name(), APPUser.get_password(), APPUser.get_requestToken());

        try {
            Response<SessionResponse> response = requestToValidateRequestToken.execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    this.requestToGenerateSessionID(APPUser);
                } else {
                    Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                }
            } else {
                Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
            }
        } catch (IOException e) {
            Log.i(ApplicationProvider.FILTER_FOR_LOGS, e.getMessage());
        }
    }

    private void requestToGenerateSessionID(User APPUser) {
        Call<SessionResponse> requestToValidateRequestToken = this.apiManager.getSessionID(
                APPUser.get_apiKey(), APPUser.get_requestToken());

        try {
            Response<SessionResponse> response = requestToValidateRequestToken.execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    SessionResponse sessionResponse = response.body();
                    APPUser.set_sessionId(sessionResponse.getSessionID());
                } else {
                    Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                }
            } else {
                Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
            }
        } catch (IOException e) {
            Log.i(ApplicationProvider.FILTER_FOR_LOGS, e.getMessage());
        }
    }

    public void rateMovieByMovieID(User APPUser, int movieID, double rating) {
        Call<RatingResponse> requestToRateMovieByMovieID = this.apiManager.rateMovie(movieID,
                APPUser.get_apiKey(), APPUser.get_sessionId(), rating);

        try {
            Response<RatingResponse> response = requestToRateMovieByMovieID.execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    RatingResponse ratingResponse = response.body();
                } else {
                    Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                }
            } else {
                Log.i(ApplicationProvider.FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
            }
        } catch (IOException e) {
            Log.i(ApplicationProvider.FILTER_FOR_LOGS, e.getMessage());
        }
    }
}
