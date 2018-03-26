package com.example.layout.imdb.adapter;
//Package objects contain version information about the implementation and specification of a Java package
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.layout.imdb.R;
import com.example.layout.imdb.model.MovieInfo;
import com.example.layout.imdb.utils.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Pri on 10/21/2017.
 */

public class ListAdapter extends ArrayAdapter<MovieInfo> {
    /*ListAdapter:Extended Adapter that is the bridge between a ListView and the data that backs the list.
    ArrayAdapter:You can use this adapter to provide views for an AdapterView, Returns a view for each object in a collection of data objects you provide,
    and can be used with list-based user interface widgets such as ListView or Spinner.
    here arrayadapter holds movieinfo
     */
    private Context context;
    //Interface to global information about an application environment.
    private int resource;
    //Class for accessing an application's resources
    //list view that holds movie info in movie lisr\t
    private List<MovieInfo> movieList;
    public ImageLoader imageLoader;
    //imageloader helps us to load the image

    public ListAdapter(Context context, int resource, List<MovieInfo> movieList) {
        super(context, resource, movieList);
//this refers to the current object
        this.context = context;
        this.resource = resource;
        this.movieList = movieList;
        imageLoader = new ImageLoader(context.getApplicationContext());
        //Return the context of the single, global Application object of the current process.
    }

    @NonNull
    @Override
    public View getView(int position, View row, @NonNull ViewGroup parent) {
/*Get a View that displays the data at the specified position in the data set
Parameters
position	int: The position of the item within the adapter's data set of the item whose view we want.
convertView	View: The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of view types, so that this View is always of the right type (see getViewTypeCount() and getItemViewType(int)).
parent	ViewGroup: The parent that this view will eventually be attached to
Returns
View	A View corresponding to the data at the specified position.

 */
        Holder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            //gets from the context
            row = inflater.inflate(resource, parent, false);
            /*Instantiates a layout XML file into its corresponding View objects.
            Inflate a new view hierarchy from the specified xml resource. Throws InflateException if there is an error.

Parameters
resource	int: ID for an XML layout resource to load (e.g., R.layout.main_page)
root	ViewGroup: Optional view to be the parent of the generated hierarchy.
This value may be null.

Returns
View	The root View of the inflated hierarchy. If root was supplied, this is the root View; otherwise it is the root of the inflated XML file.
findFragmentById(int id)
Finds a fragment that was identified by the given id either when inflated from XML or as the container ID when added in a transaction.
             */
            holder = new Holder();
            holder.titleHolder = (TextView) row.findViewById(R.id.titleTextView);
            holder.dateHolder = (TextView) row.findViewById(R.id.releaseDateTextView);
            holder.ratingHolder = (RatingBar) row.findViewById(R.id.ratingBar);
            holder.imageHolder = (ImageView) row.findViewById(R.id.movieImageView);
            holder.voteHolder = (TextView) row.findViewById(R.id.voteCountTextView);

            row.setTag(holder);
            /**Sets the tag associated with this view.
             * Parameters
             tag	Object: an Object to tag the view with
             */

        } else {

            holder = (Holder) row.getTag();
        //Returns this view's tag.
            //Returns::Object-the Object stored in this view as a tag, or null if not set

        }

        MovieInfo info = movieList.get(position);
//gets the position from movieList
        holder.titleHolder.setText(info.getTitle());
        holder.dateHolder.setText(info.getDate());
        //get the title and date of the movie
        holder.ratingHolder.setRating(Float.parseFloat(info.getVote_average()) / 2);
        //counts the rating of the movie
        if (info.getPoster().equals("null")) {
            //holds the poster until it is null
            holder.imageHolder.setImageResource(R.drawable.no_image);
            //Sets a drawable as the content of this ImageView.
        } else {
            String imageurl = "http://image.tmdb.org/t/p/w500" + info.getPoster();
            //here it holds the image urls that we need to have and get those posters
            Picasso.with(context).load(imageurl).into(holder.imageHolder);
            //Picasso will holds the images in the given code and loads them
          //  imageLoader.DisplayImage("http://image.tmdb.org/t/p/w500" + info.getPoster(), holder.imageHolder);
            Log.d("ImageUrl","http://image.tmdb.org/t/p/w500" + info.getPoster());

        }
        holder.voteHolder.setText("(" + info.getVote_average() + "/10) voted by " + info.getVote_count() + " users");
        //setText() will set the text
        //returns the rows
        return row;
    }

    private static class Holder {
        //Holder class holds the holder declarations
        TextView titleHolder;
        TextView dateHolder;
        RatingBar ratingHolder;
        ImageView imageHolder;
        TextView voteHolder;
    }

    @Override
    public int getCount() {
        //here we get the count of the list and returns the count
        return super.getCount();
    }
}