package es.upm.miw.ratingmanager.api.example;

import android.os.AsyncTask;
import android.widget.TextView;

import retrofit2.Response;

/**
 * Created by franlopez on 13/11/2016.
 */

public abstract class APIAsyncTask<U> extends AsyncTask<TextView, Void, Response> {
    private APIExample apiExample;
    private TextView textView;

    public APIAsyncTask(APIExample apiExample) {
        this.apiExample = apiExample;
    }

    public APIExample getApiExample() {
        return apiExample;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    protected abstract void onPreExecute();

    protected abstract Response<U> doInBackground(TextView... TextViews);

    protected abstract void onPostExecute(Response response);
}
