package com.example.layout.imdb.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.layout.imdb.R;
import com.example.layout.imdb.async.GetCastCrew;
import com.example.layout.imdb.async.GetMovieDetails;
import com.example.layout.imdb.db.MovieDataBase;
import com.example.layout.imdb.model.Cast;
import com.example.layout.imdb.model.Constants;
import com.example.layout.imdb.model.MovieInfo;
import com.example.layout.imdb.utils.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class MovieDetails extends Fragment {
/*
A Fragment is a piece of an application's user interface or behavior that can be placed in an Activity.
 */
    public String URL;
    public TextView title;
    public TextView tagline;
    public TextView releaseDate;
    public TextView budget;
    public TextView revenue;
    public TextView status;
    public TextView voteCount;
    public TextView overView;
    public RatingBar ratingBar;
    public ImageView poster;
    public ImageView favorites;
    public ImageView watchlist;
    public String movieID;
    private ImageLoader imageLoader;
    public MovieInfo movie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
/*
Called to have the fragment instantiate its user interface view.
Parameters
inflater	LayoutInflater: The LayoutInflater object that can be used to inflate any views in the fragment,
container	ViewGroup: If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
savedInstanceState	Bundle: If non-null, this fragment is being re-constructed from a previous saved state as given here.
Returns
View	Return the View for the fragment's UI, or null.
->setHasOptionsMenu (boolean hasMenu)
Report that this fragment would like to participate in populating the options menu by receiving a call to onCreateOptionsMenu(Menu, MenuInflater) and related methods.

Parameters
hasMenu	boolean: If true, the fragment has menu items to contribute.
 */
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.movie_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        /*
        Called when the fragment's activity has been created and this fragment's view hierarchy instantiated
        Parameters
savedInstanceState	Bundle: If the fragment is being re-created from a previous saved state, this is the state.
This value may be null.
         */
        super.onActivityCreated(savedInstanceState);
/*
getActivity ()-Return the Activity this fragment is currently associated with.
findViewById (int id)-Finds the first descendant view with the given ID, the view itself if the ID matches getId(),
or null if the ID is invalid (< 0) or there is no matching view in the hierarchy.
 */
        title = (TextView) getActivity().findViewById(R.id.titleTextView2);
        tagline = (TextView) getActivity().findViewById(R.id.smallDescriptionTextView);
        releaseDate = (TextView) getActivity().findViewById(R.id.releaseDateTextView2);
        budget = (TextView) getActivity().findViewById(R.id.budgetTextView);
        revenue = (TextView) getActivity().findViewById(R.id.revenueTextView);
        status = (TextView) getActivity().findViewById(R.id.statusTextView);
        voteCount = (TextView) getActivity().findViewById(R.id.voteCountTextView2);
        ratingBar = (RatingBar) getActivity().findViewById(R.id.ratingBar2);
        poster = (ImageView) getActivity().findViewById(R.id.movieImageView2);
        overView = (TextView) getActivity().findViewById(R.id.descriptionTextView);
        favorites = (ImageView) getActivity().findViewById(R.id.favoritesImageView);
        watchlist = (ImageView) getActivity().findViewById(R.id.watchlistImageView);

        /*
        getArguments ()-Return the arguments supplied to setArguments(Bundle), if any.
        getString (int resId)-Returns a localized string from the application's package's default string table.
Parameters resId	int: Resource id for the string
Returns-String	The string data associated with the resource, stripped of styled text information.This value will never be null.
Log.e-Send an ERROR log message.
Parameters
tag	String: Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
msg	String: The message you would like logged.
         */
        movieID = getArguments().getString("MovieID");
        Log.e("movieID", movieID);
/*
creating the object of the movieInfo and calling it
 */
        movie = new MovieInfo();
        movie = getMovie(movieID);
        movie.setId(movieID);
        title.setText(movie.getTitle());
        tagline.setText(movie.getTagline());
        releaseDate.setText(movie.getDate());
        budget.setText("$" + movie.getBudget());
        revenue.setText("$" + movie.getRevenue());
        status.setText(movie.getStatus());
        overView.setText(movie.getOverview());
        voteCount.setText("(" + movie.getVote_average() + "/10) voted by " + movie.getVote_count() + " users");
        ratingBar.setRating(Float.parseFloat(movie.getVote_average()) / 2);
        if (movie.getPoster().equals("null")) {
            poster.setImageResource(R.drawable.no_image);
        } else {
            //to load the image using picasso and gviving the path of the image
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500"+movie.getPoster()).into(poster);
            Log.d("details image url","http://image.tmdb.org/t/p/w500/"+movie.getPoster());
            /*imageLoader = new ImageLoader(getActivity());
            imageLoader.DisplayImage("http://image.tmdb.org/t/p/w500" + movie.getPoster(), poster);
       */ }

        checkMovie(movieID);//checks the movie id

        /*
        This method is for on click of favorites and
         */
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = favorites.getTag();
                /*
                Returns this view's tag.
                Returns-Object	the Object stored in this view as a tag, or null if not set
setTag (Object tag)-Sets the tag associated with this view. A tag can be used to mark a view in its hierarchy and does not have to
be unique within the hierarchy. Tags can also be used to store data within a view without resorting to another data structure.
         setImageResource (int resId)-Sets a drawable as the content of this ImageView.
                 */
                if (tag == "disable") {
                    favorites.setImageResource(R.drawable.favorite_enable_normal);
                    favorites.setTag("enable");
                    movie.setFavorites(1);
                    /*creating the object of the db and check the db*/
                    MovieDataBase db = new MovieDataBase(getActivity());
                    boolean check = db.checkMovie(movie.getId());
                    if (check)//if the it got checked then update the database other wise add the database
                        db.updateMovieF(movie);
                    else
                        db.addMovie(movie);
                } else {
                    favorites.setImageResource(R.drawable.favorite_disable_normal);
                    favorites.setTag("disable");
                    movie.setFavorites(0);
                    MovieDataBase db = new MovieDataBase(getActivity());
                    boolean check = db.checkMovie(movie.getId());
                    if (check)
                        db.updateMovieF(movie);
                    else
                        db.addMovie(movie);
                }


            }
        });

   /*setOnClickListener-Register a callback to be invoked when this view is clicked. If this view is not clickable, it becomes
   clickable.
Parameters View.OnClickListener: The callback that will run.This value may be null.
This listener for watchlist
    */
        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = watchlist.getTag();
                if (tag == "disable") {
                    watchlist.setImageResource(R.drawable.watchlist_enable_normal);
                    watchlist.setTag("enable");
                    movie.setWatchList(1);
                    MovieDataBase db = new MovieDataBase(getActivity());
                    boolean check = db.checkMovie(movie.getId());
                    if (check)
                        db.updateMovieW(movie);
                    else
                        db.addMovie(movie);

                } else {
                    watchlist.setImageResource(R.drawable.watchlist_disable_normal);
                    watchlist.setTag("disable");
                    movie.setWatchList(0);
                    MovieDataBase db = new MovieDataBase(getActivity());
                    boolean check = db.checkMovie(movie.getId());
                    if (check)
                        db.updateMovieW(movie);
                    else
                        db.addMovie(movie);
                }

            }
        });

//show the methods of respective classes
        showCast();
        showCrew();
        showTrailers();
        showPosters();
    }

    private MovieInfo getMovie(String id) {
//here constants class will get the base url,api version and api key for get the information
        URL = Constants.BASE_URL + Constants.API_VERSION + "/movie/" + id + Constants.API_KEY;
//get the methods by creating an object to it
        MovieInfo movieInfo = new MovieInfo();
        try {
            //here it will get the moviedetails and execute the url of that movie
            movieInfo = new GetMovieDetails(getActivity()).execute(URL).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return movieInfo;//returns the movieinfo
    }

    private void checkMovie(String id) {
//get the database of the movie and deelete the movies which are not favorites and check the details after deleting
        MovieDataBase db = new MovieDataBase(getActivity());
        db.deleteNonFavWatchMovie();
        Boolean check = db.checkMovie(id);

        if (!check) { //checks if movie does not existing in database
            favorites.setImageResource(R.drawable.favorite_disable_normal);
            favorites.setTag("disable");
            watchlist.setImageResource(R.drawable.watchlist_disable_normal);
            watchlist.setTag("disable");
        } else { //if movie does exist
            MovieInfo movieInfo = db.getMovie(id);
            if (movieInfo.getFavorites() == 0) { //set image based on database value
                favorites.setImageResource(R.drawable.favorite_disable_normal);
                favorites.setTag("disable");
                movie.setFavorites(0);

            } else  {
                favorites.setImageResource(R.drawable.favorite_enable_normal);
                favorites.setTag("enable");
                movie.setFavorites(1);
            }

            if (movieInfo.getWatchList() == 0) { //set image based on database value
                watchlist.setImageResource(R.drawable.watchlist_disable_normal);
                watchlist.setTag("disable");
                movie.setWatchList(0);
            } else {
                watchlist.setImageResource(R.drawable.watchlist_enable_normal);
                watchlist.setTag("enable");
                movie.setWatchList(1);
            }
        }
    }


    private void showCast() {
/*this method is to show the cast.
to set the cast we use the linear layout and from the url we will get the values
 */
        LinearLayout castsSection = (LinearLayout) getActivity().findViewById(R.id.casts_section);

        URL = Constants.BASE_URL + Constants.API_VERSION + "/movie/" + movieID + "/credits" + Constants.API_KEY;

        try {
            //using try catch block we are getting the cast list from castcrew and execute the url
            List<Cast> castList = new GetCastCrew(getActivity(), Constants.TAG_CAST).execute(URL).get();

            if (castList != null && !castList.isEmpty()) {
                castsSection.setVisibility(View.VISIBLE);
                /*
                setVisibility (int visibility)-Set the visibility state of this view.
                 VISIBLE-This view is visible
                 GONE-This view is invisible, and it doesn't take any space for layout purposes
                 */
                setCasts(castList);//if the cast section has values the show the detasils
            } else {
                castsSection.setVisibility(View.GONE);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Add multiple casts in the casts container
    private void setCasts(List<Cast> casts) {

        LinearLayout castsContainer = (LinearLayout) getActivity().findViewById(R.id.casts_container);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*Instantiates a layout XML file into its corresponding View objects.
        getSystemService (String name)-Return the handle to a system-level service by name. The class of the returned object varies by the requested name.
         LAYOUT_INFLATER_SERVICE ("layout_inflater")-A LayoutInflater for inflating layout resources in this context
         */
        imageLoader = new ImageLoader(getActivity());//loads the image

        int size = casts.size();//size of the cast
        for (int i = 0; i < size; i++) {
            Cast cast = casts.get(i);

            if (cast != null) {
//if cast is not equal to null then print
                LinearLayout clickableColumn = (LinearLayout) inflater.inflate(R.layout.column_cast_crew, null);
                ImageView thumbnailImage = (ImageView) clickableColumn.findViewById(R.id.thumbnail_image);
                TextView titleView = (TextView) clickableColumn.findViewById(R.id.title_view);
                TextView subTitleView = (TextView) clickableColumn.findViewById(R.id.subtitle_view);

                if (cast.getProfilePath().equals("null")) {
                    thumbnailImage.setImageResource(R.drawable.no_image);
                } else {
                    Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500" + cast.getProfilePath()).into(thumbnailImage);
                   // imageLoader.DisplayImage("http://image.tmdb.org/t/p/w500" + cast.getProfilePath(), thumbnailImage);
                }

                titleView.setText(cast.getName());
                subTitleView.setText(cast.getCharacter());

                castsContainer.addView(clickableColumn);

                if (i != size - 1) {
                    castsContainer.addView(inflater.inflate(R.layout.horizontal_divider, null));
                }
            }
        }
    }

    private void showCrew() {
//method to show the cast
        LinearLayout castsSection = (LinearLayout) getActivity().findViewById(R.id.crew_section);

        URL = Constants.BASE_URL + Constants.API_VERSION + "/movie/" + movieID + "/credits" + Constants.API_KEY;

        try {
            List<Cast> castList = new GetCastCrew(getActivity(), Constants.TAG_CREW).execute(URL).get();

            if (castList != null && !castList.isEmpty()) {
                castsSection.setVisibility(View.VISIBLE);
                setCrew(castList);
            } else {
                castsSection.setVisibility(View.GONE);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Add multiple casts in the casts container
    private void setCrew(List<Cast> casts) {

        LinearLayout castsContainer = (LinearLayout) getActivity().findViewById(R.id.crew_container);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        imageLoader = new ImageLoader(getActivity());

        int size = casts.size();
        for (int i = 0; i < size; i++) {
            Cast cast = casts.get(i);

            if (cast != null) {

                LinearLayout clickableColumn = (LinearLayout) inflater.inflate(R.layout.column_cast_crew, null);
                ImageView thumbnailImage = (ImageView) clickableColumn.findViewById(R.id.thumbnail_image);
                TextView titleView = (TextView) clickableColumn.findViewById(R.id.title_view);
                TextView subTitleView = (TextView) clickableColumn.findViewById(R.id.subtitle_view);

                if (cast.getProfilePath().equals("null")) {
                    thumbnailImage.setImageResource(R.drawable.no_image);
                } else {
                    Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500" + cast.getProfilePath()).into(thumbnailImage);

                    // imageLoader.DisplayImage("http://image.tmdb.org/t/p/w500" + cast.getProfilePath(), thumbnailImage);
                }

                titleView.setText(cast.getName());
                subTitleView.setText(cast.getJob());

                castsContainer.addView(clickableColumn);

                if (i != size - 1) {
                    castsContainer.addView(inflater.inflate(R.layout.horizontal_divider, null));
                }
            }
        }
    }

    private void showTrailers() {

        LinearLayout castsSection = (LinearLayout) getActivity().findViewById(R.id.trailer_section);

        URL = Constants.BASE_URL + Constants.API_VERSION + "/movie/" + movieID + "/videos" + Constants.API_KEY;

        try {
            List<Cast> castList = new GetCastCrew(getActivity(), Constants.TAG_RESULTS).execute(URL).get();

          if (castList != null && !castList.isEmpty()) {
                castsSection.setVisibility(View.VISIBLE);
                setTrailers(castList);
            } else {
                castsSection.setVisibility(View.GONE);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Add multiple casts in the casts container
    private void setTrailers(List<Cast> casts) {

        LinearLayout castsContainer = (LinearLayout) getActivity().findViewById(R.id.trailer_container);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int size = casts.size();
        for (int i = 0; i < size; i++) {
            Cast cast = casts.get(i);

            if (cast != null) {

                LinearLayout clickableColumn = (LinearLayout) inflater.inflate(R.layout.column_trailers, null);
                TextView titleView = (TextView) clickableColumn.findViewById(R.id.trailer_link);

                titleView.setText(cast.getName());
                final String trailer = "https://www.youtube.com/watch?v=" + cast.getKey();
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer)));
                    }
                });

                castsContainer.addView(clickableColumn);

                if (i != size - 1) {
                    castsContainer.addView(inflater.inflate(R.layout.horizontal_divider, null));
                }
            }
        }
    }

    private void showPosters() {

        LinearLayout castsSection = (LinearLayout) getActivity().findViewById(R.id.posters_section);

        URL = Constants.BASE_URL + Constants.API_VERSION + "/movie/" + movieID + "/images" + Constants.API_KEY;

        try {
            List<Cast> castList = new GetCastCrew(getActivity(), Constants.TAG_POSTERS).execute(URL).get();

            if (castList != null && !castList.isEmpty()) {
                castsSection.setVisibility(View.VISIBLE);
                setPosters(castList);
            } else {
                castsSection.setVisibility(View.GONE);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Add multiple casts in the casts container
    private void setPosters(List<Cast> casts) {

        LinearLayout castsContainer = (LinearLayout) getActivity().findViewById(R.id.posters_container);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        imageLoader = new ImageLoader(getActivity());

        int size = casts.size();
        for (int i = 0; i < size; i++) {
            Cast cast = casts.get(i);

            if (cast != null) {

                LinearLayout clickableColumn = (LinearLayout) inflater.inflate(R.layout.column_cast_crew, null);
                ImageView thumbnailImage = (ImageView) clickableColumn.findViewById(R.id.thumbnail_image);
                TextView titleView = (TextView) clickableColumn.findViewById(R.id.title_view);
                TextView subTitleView = (TextView) clickableColumn.findViewById(R.id.subtitle_view);

                if (cast.getProfilePath().equals("null")) {
                    thumbnailImage.setImageResource(R.drawable.no_image);
                } else {
                    Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500" + cast.getProfilePath()).into(thumbnailImage);

                    // imageLoader.DisplayImage("http://image.tmdb.org/t/p/w500" + cast.getProfilePath(), thumbnailImage);
                }

                titleView.setVisibility(View.GONE);
                subTitleView.setVisibility(View.GONE);

                castsContainer.addView(clickableColumn);

                if (i != size - 1) {
                    castsContainer.addView(inflater.inflate(R.layout.horizontal_divider, null));
                }
            }
        }
    }
}