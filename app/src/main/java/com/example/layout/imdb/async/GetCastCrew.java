package com.example.layout.imdb.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.layout.imdb.connection.ServiceHandler;
import com.example.layout.imdb.model.Cast;
import com.example.layout.imdb.model.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pri on 10/21/2017.
 */

public class GetCastCrew extends AsyncTask<String, Void, List<Cast>> {
/*AsyncTask enables proper and easy use of the UI thread
It holds String and the list of the crew
 */
    private Context context;
    private String tag;

    public GetCastCrew(Context context, String tag) {
//Context-Interface to global information about an application environment.
        this.context = context;
        this.tag = tag;
    }

    @Override
    protected List<Cast> doInBackground(String... urls) {
        /*
        doInBackground (Params... params)
Override this method to perform a computation on a background thread. The specified parameters are the parameters passed to execute(Params...) by the caller of this task. This method can call publishProgress(Progress...) to publish updates on the UI thread.

This method may take several seconds to complete, so it should only be called from a worker thread.

Parameters
params	Params: The parameters of the task.
Returns
Result	A result, defined by the subclass of this task.
         */
        ServiceHandler sh = new ServiceHandler();
        //we wrote methods and class for ServiceHandler and creating the object of the servicehandler so we can use the class methods
        //creating a string
        // Making a request to url and getting response and makes service handler to get the info
        String jsonStr = sh.makeServiceCall(urls[0], ServiceHandler.GET);
        List<Cast> castList = new ArrayList<>();
        //creating a list of cast
        if (jsonStr != null) {
            //if jsonStr is not equal to null
            try {
                //JSON is a syntax for storing and exchanging data.
                //creating the json object
                JSONObject jsonObj = new JSONObject(jsonStr);
                //JSONObject()Creates a JSONObject with  name/value mappings.
                // // Getting JSON Array node
                //JSONArray(Object array)-Creates a new JSONArray with values from the given primitive array.
                JSONArray castArray = jsonObj.getJSONArray(tag);
                    //for cast
                if(tag.equals(Constants.TAG_CAST)) {
                //tag is equal to the tag which we gave in
                    //// looping through All info
                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Cast cast = new Cast();//new object
                        cast.setCharacter(o.getString(Constants.CHARACTER));//setting the from the castarray
                        cast.setName(o.getString(Constants.NAME));
                        cast.setProfilePath(o.getString(Constants.PROFILE_PATH));
                        castList.add(cast);
                        //add the all values into it 7
                    }
                    //for crew
                } else if (tag.equals(Constants.TAG_CREW)) {
                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Cast cast = new Cast();
                        cast.setJob(o.getString(Constants.JOB));
                        cast.setName(o.getString(Constants.NAME));
                        cast.setProfilePath(o.getString(Constants.PROFILE_PATH));
                        castList.add(cast);
                    }
                } else if (tag.equals(Constants.TAG_RESULTS)) {
                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Cast cast = new Cast();
                        cast.setName(o.getString(Constants.NAME));
                        cast.setKey(o.getString(Constants.KEY));
                        castList.add(cast);
                    }
                } else if (tag.equals(Constants.TAG_POSTERS)) {
                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Cast cast = new Cast();
                        cast.setProfilePath(o.getString(Constants.TAG_FILE_PATH));
                        castList.add(cast);
                    }
                }
                return castList;//returns the castlist
            } catch (JSONException e) {
                //otherwise it shows the exception
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            //otherwise it shows the erroe messagee
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Cast> result) {
        /*
        onPostExecute (Result result)
Runs on the UI thread after doInBackground(Params...). The specified result is the value returned by doInBackground(Params...).

This method won't be invoked if the task was cancelled.

This method must be called from the main thread of your app.

Parameters
result	Result: The result of the operation computed by doInBackground(Params...).

         */
        super.onPostExecute(result);

        if (result == null) {
//if result is equal to null
            Toast.makeText(context, "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            /*
            Make a standard toast that just contains a text view with the text from a resource.

Parameters
context	Context: The context to use. Usually your Application or Activity object.
resId	int: The resource id of the string resource to use. Can be formatted text.
duration	int: How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
Value is LENGTH_SHORT or LENGTH_LONG.
             */
        }
    }
}