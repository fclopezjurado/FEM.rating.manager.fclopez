package es.upm.miw.ratingmanager.api.definition;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by franlopez on 09/11/2016.
 */

public abstract class CallbackWithArgument<T, U> implements Callback<T> {
    private U argument;

    public CallbackWithArgument(U argument) {
        this.argument = argument;
    }

    public U getArgument() {
        return this.argument;
    }

    public abstract void onResponse(Call<T> call, Response<T> response);

    public abstract void onFailure(Call<T> call, Throwable t);
}
