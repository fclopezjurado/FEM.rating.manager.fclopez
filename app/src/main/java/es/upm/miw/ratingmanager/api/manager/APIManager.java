package es.upm.miw.ratingmanager.api.manager;

import es.upm.miw.ratingmanager.api.definition.TMDbAPI;
import es.upm.miw.ratingmanager.api.pojos.Genres;
import es.upm.miw.ratingmanager.api.pojos.Movies;
import es.upm.miw.ratingmanager.api.pojos.RatingResponse;
import es.upm.miw.ratingmanager.api.pojos.SessionResponse;
import retrofit2.Call;
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

    public Call<SessionResponse> getTemporalRequestToken(String APIKey) {
        return this.APIInterface.getTemporalRequestToken(APIVersion, APIKey);
    }

    public Call<SessionResponse> validateRequestToken(String APIKey, String userName,
                                                      String userPass, String requestToken) {
        return this.APIInterface.validateRequestToken(APIVersion, APIKey,
                userName, userPass, requestToken);
    }

    public Call<SessionResponse> getSessionID(String APIKey, String requestToken) {
        return this.APIInterface.getSessionID(APIVersion, APIKey,
                requestToken);
    }

    public Call<Genres> getGenres(String APIKey) {
        return this.APIInterface.getGenres(APIVersion, APIKey);
    }

    public Call<Movies> getMovies(String APIKey, int page, String primaryReleaseDateGTE,
                                  String primaryReleaseDateLTE, String genresToInclude,
                                  String genresToExclude) {
        return this.APIInterface.getMovies(APIVersion, APIKey, page,
                primaryReleaseDateGTE, primaryReleaseDateLTE, genresToInclude, genresToExclude);
    }

    public Call<RatingResponse> rateMovie(int movieID, String APIKey, String sessionID,
                                          double rating) {
        return this.APIInterface.rateMovie(APIVersion, movieID, APIKey,
                sessionID, rating);
    }
}
