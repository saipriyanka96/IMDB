package com.example.layout.imdb.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.layout.imdb.R;
import com.example.layout.imdb.adapter.ListAdapter;
import com.example.layout.imdb.connection.ServiceHandler;
import com.example.layout.imdb.model.Constants;
import com.example.layout.imdb.model.MovieInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Pri on 10/21/2017.
 */

public class GetSingleMovieInfo extends AsyncTask<String, Void, Boolean> {

    private Context context;

    private List<MovieInfo> movieList;
    private ListView listview;


    public GetSingleMovieInfo(Context context, List<MovieInfo> movieList, ListView listview) {

        this.context = context;
        this.movieList = movieList;
        this.listview = listview;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.makeServiceCall(urls[0], ServiceHandler.GET);

        if (jsonStr != null) {
            try {
                JSONObject o = new JSONObject(jsonStr);
                MovieInfo movieInfo = new MovieInfo();
                movieInfo.setId(o.getString(Constants.TAG_ID));
                movieInfo.setTitle(o.getString(Constants.TAG_TITLE));
                movieInfo.setDate(o.getString(Constants.TAG_RELEASE_DATE));
                movieInfo.setPoster(o.getString(Constants.TAG_POSTER_PATH));
                movieInfo.setVote_average(o.getString(Constants.TAG_VOTE_AVERAGE));
                movieInfo.setVote_count(o.getString(Constants.TAG_VOTE_COUNT));
                movieList.add(movieInfo);

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) {
            ListAdapter adapter = new ListAdapter(context, R.layout.list_item, movieList);
            //we are using list adapter to get the list of the movielist
            listview.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
    }
}