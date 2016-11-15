package es.upm.miw.ratingmanager.dbmanager.example;

import android.os.AsyncTask;
import android.widget.ListView;

import retrofit2.Response;

/**
 * Created by franlopez on 13/11/2016.
 */

public abstract class DBManagerAsyncTask<U> extends AsyncTask<ListView, Void, Response> {
    private DBManagerExample dbManagerExample;
    private ListView listView;

    public DBManagerAsyncTask(DBManagerExample dbManagerExample) {
        this.dbManagerExample = dbManagerExample;
    }

    public DBManagerExample getDbManagerExample() {
        return dbManagerExample;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    protected abstract void onPreExecute();

    protected abstract Response<U> doInBackground(ListView... ListViews);

    protected abstract void onPostExecute(Response response);
}