package es.upm.miw.ratingmanager.api;

/**
 * Created by franlopez on 07/11/2016.
 */

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Rating {

    @SerializedName("value")
    @Expose
    private Integer value;

    /**
     * @return The value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    public void setValue(Integer value) {
        this.value = value;
    }

}