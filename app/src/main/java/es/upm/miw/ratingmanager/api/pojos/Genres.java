package es.upm.miw.ratingmanager.api.pojos;

/**
 * Created by franlopez on 07/11/2016.
 */

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Genres {
    private static final String HORROR_GENRE = "Horror";

    @SerializedName("genres")
    @Expose
    private List<Genre> genres = new ArrayList<Genre>();

    /**
     * @return The genres
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * @param genres The genres
     */
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        String genresToString = "";

        for (Genre genre : genres)
            genresToString += genre.toString();

        return "Genres{" +
                "genres=" + genresToString +
                '}';
    }

    public String genresIDsToString(boolean includeHorrorGenre) {
        String genresIDsToString = "";

        for (Genre genre : genres) {
            if ((!includeHorrorGenre && !genre.getName().equals(HORROR_GENRE))
                    || (includeHorrorGenre)) {
                if (genresIDsToString.length() == 0)
                    genresIDsToString += genre.getId();
                else
                    genresIDsToString += "," + genre.getId();
            }

        }
        return genresIDsToString;
    }

    public String getHorrorGenreID() {
        for (Genre genre : genres)
            if (genre.getName().equals(HORROR_GENRE))
                return Integer.toString(genre.getId());

        return "";
    }
}
