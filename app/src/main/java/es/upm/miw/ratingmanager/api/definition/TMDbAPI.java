package es.upm.miw.ratingmanager.api.definition;

import es.upm.miw.ratingmanager.api.pojos.Genres;
import es.upm.miw.ratingmanager.api.pojos.Movies;
import es.upm.miw.ratingmanager.api.pojos.RatingResponse;
import es.upm.miw.ratingmanager.api.pojos.SessionResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by franlopez on 07/11/2016.
 */

public interface TMDbAPI {

    // https://api.themoviedb.org/3/authentication/token/new?api_key=api_key
    @GET("/{api_version}/authentication/token/new")
    Call<SessionResponse> getTemporalRequestToken(@Path("api_version") int APIVersion, @Query("api_key") String APIKey);

    /*
     * https://api.themoviedb.org/3/authentication/token/validate_with_login?api_key=api_key
     * &username=username&password=password&request_token=request_token
     */
    @GET("/{api_version}/authentication/token/validate_with_login")
    Call<SessionResponse> validateRequestToken(@Path("api_version") int APIVersion,
                                               @Query("api_key") String APIKey,
                                               @Query("username") String userName,
                                               @Query("password") String userPass,
                                               @Query("request_token") String requestToken);

    /*
     * https://api.themoviedb.org/3/authentication/session/new?api_key=api_key
     * &request_token=request_token
     */
    @GET("/{api_version}/authentication/session/new")
    Call<SessionResponse> getSessionID(@Path("api_version") int APIVersion,
                                       @Query("api_key") String APIKey,
                                       @Query("request_token") String requestToken);

    /*
     * https://api.themoviedb.org/3/movie/{movieID}/rating?api_key=api_key&session_id=session_id
     */
    @FormUrlEncoded
    @POST("/{api_version}/movie/{movieID}/rating")
    Call<RatingResponse> rateMovie(@Path("api_version") int APIVersion,
                                   @Path("movieID") int movieID, @Query("api_key") String APIKey,
                                   @Query("session_id") String sessionID,
                                   @Field("value") double rating);

    /*
     * https://api.themoviedb.org/3/genre/movie/list?api_key=api_key
     */
    @GET("/{api_version}/genre/movie/list")
    Call<Genres> getGenres(@Path("api_version") int APIVersion, @Query("api_key") String APIKey);

    /*
     * https://api.themoviedb.org/3/discover/movie?api_key=api_key&page=page
     * &primary_release_date.gte=primary_release_date.gte
     * &primary_release_date.lte=primary_release_date.lte&with_genres=genres&without_genres=genres
     */
    @GET("/{api_version}/discover/movie")
    Call<Movies> getMovies(@Path("api_version") int APIVersion, @Query("api_key") String APIKey,
                           @Query("page") int page,
                           @Query("primary_release_date.gte") String primaryReleaseDateGTE,
                           @Query("primary_release_date.lte") String primaryReleaseDateLTE,
                           @Query("with_genres") String genresToInclude,
                           @Query("without_genres") String genresToExclude);
}
