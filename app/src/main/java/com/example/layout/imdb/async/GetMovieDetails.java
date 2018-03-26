package com.example.layout.imdb.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.layout.imdb.connection.ServiceHandler;
import com.example.layout.imdb.model.Constants;
import com.example.layout.imdb.model.MovieInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pri on 10/21/2017.
 */

public class GetMovieDetails extends AsyncTask<String, Void, MovieInfo> {

    private Context context;

    public GetMovieDetails(Context context) {

        this.context = context;
    }

    @Override
    protected MovieInfo doInBackground(String... urls) {
        ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.makeServiceCall(urls[0], ServiceHandler.GET);

        if (jsonStr != null) {
            try {
                JSONObject o = new JSONObject(jsonStr);

                MovieInfo movieInfo = new MovieInfo();
                movieInfo.setTitle(o.getString(Constants.TAG_TITLE));
                movieInfo.setDate(o.getString(Constants.TAG_RELEASE_DATE));
                movieInfo.setPoster(o.getString(Constants.TAG_POSTER_PATH));
                movieInfo.setVote_average(o.getString(Constants.TAG_VOTE_AVERAGE));
                movieInfo.setVote_count(o.getString(Constants.TAG_VOTE_COUNT));
                movieInfo.setBudget(o.getString(Constants.TAG_BUDGET));
                movieInfo.setRevenue(o.getString(Constants.TAG_REVENUE));
                movieInfo.setTagline(o.getString(Constants.TAG_TAGLINE));
                movieInfo.setStatus(o.getString(Constants.TAG_STATUS));
                movieInfo.setOverview(o.getString(Constants.TAG_OVERVIEW));

                return movieInfo;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return null;
    }

    @Override
    protected void onPostExecute(MovieInfo result) {
        super.onPostExecute(result);

        if (result == null) {

            Toast.makeText(context, "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
    }
}