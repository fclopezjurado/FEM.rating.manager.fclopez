package es.upm.miw.ratingmanager.api.manager;

import es.upm.miw.ratingmanager.api.definition.TMDbAPI;
import es.upm.miw.ratingmanager.api.pojos.Genres;
import es.upm.miw.ratingmanager.api.pojos.Movies;
import es.upm.miw.ratingmanager.api.pojos.RatingResponse;
import es.upm.miw.ratingmanager.api.pojos.SessionResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by franlopez on 07/11/2016.
 */

public class APIManager {
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static int APIVersion = 3;

    private TMDbAPI APIInterface;

    public APIManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIManager.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.APIInterface = retrofit.create(TMDbAPI.class);
    }

    public void getTemporalRequestToken(String APIKey, Callback<SessionResponse> callback) {
        Call<SessionResponse> call = this.APIInterface.getTemporalRequestToken(APIVersion, APIKey);
        call.enqueue(callback);
    }

    public void validateRequestToken(String APIKey, String userName, String userPass,
                                     String requestToken, Callback<SessionResponse> callback) {
        Call<SessionResponse> call = this.APIInterface.validateRequestToken(APIVersion, APIKey,
                userName, userPass, requestToken);
        call.enqueue(callback);
    }

    public void getSessionID(String APIKey, String requestToken,
                             Callback<SessionResponse> callback) {
        Call<SessionResponse> call = this.APIInterface.getSessionID(APIVersion, APIKey,
                requestToken);
        call.enqueue(callback);
    }

    public void getGenres(String APIKey, Callback<Genres> callback) {
        Call<Genres> call = this.APIInterface.getGenres(APIVersion, APIKey);
        call.enqueue(callback);
    }

    public void getMovies(String APIKey, int page, String primaryReleaseDateGTE,
                          String primaryReleaseDateLTE, String genresToInclude,
                          String genresToExclude, Callback<Movies> callback) {
        Call<Movies> call = this.APIInterface.getMovies(APIVersion, APIKey, page,
                primaryReleaseDateGTE, primaryReleaseDateLTE, genresToInclude, genresToExclude);
        call.enqueue(callback);
    }

    public void rateMovie(int movieID, String APIKey, String sessionID, double rating,
                          Callback<RatingResponse> callback) {
        Call<RatingResponse> call = this.APIInterface.rateMovie(APIVersion, movieID, APIKey,
                sessionID, rating);
        call.enqueue(callback);
    }
}
