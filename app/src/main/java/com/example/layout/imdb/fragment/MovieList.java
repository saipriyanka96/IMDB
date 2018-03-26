package com.example.layout.imdb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.layout.imdb.R;
import com.example.layout.imdb.adapter.ListAdapter;
import com.example.layout.imdb.async.GetMovieInfo;
import com.example.layout.imdb.async.GetSingleMovieInfo;
import com.example.layout.imdb.db.MovieDataBase;
import com.example.layout.imdb.mainwork.DetailsActivity;
import com.example.layout.imdb.model.Constants;
import com.example.layout.imdb.model.MovieInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pri on 10/23/2017.
 */

public class MovieList extends ListFragment {
//A fragment that displays a list of items by binding to a data source such as an array or Cursor,
// and exposes event handlers when the user selects an item.
    public String URL;
    private ListView listview;
    private List<MovieInfo> movieList;
    public ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.movie_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listview = getListView();//get the list view
        movieList = new ArrayList<>();//creating the array list

        SetList(Constants.MOVIE_NOW_PLAYING);//set the movie which are playing

        getActivity().setTitle(Constants.NOW_PLAYING);//get them and set title to them
//An Adapter object acts as a bridge between an AdapterView and the underlying data for that view
        adapter = new ListAdapter(getActivity(), R.layout.list_item, movieList);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        /*
        This method will be called when an item in the list is selected. Subclasses should override. Subclasses can call getListView().getItemAtPosition(position) if they need to access the data associated with the selected item.
        Parameters
l	ListView: The ListView where the click happened
v	View: The view that was clicked within the ListView
position	int: The position of the view in the list
id	long: The row id of the item that was clicked

         */
        MovieInfo movieInfo = movieList.get(position);//to get the postion of movie in the movielist
        adapter.imageLoader.clearCache();//to clear the cache of image loader
        showDetails(movieInfo.getId());//show the details with their ids
    }

    void showDetails(String id) {

        Intent intent = new Intent();
        intent.setClass(getActivity(), DetailsActivity.class);
        intent.putExtra("MovieID", id);//Add extended data to the intent
        startActivity(intent);//To start an activity, use the method startActivity(intent)
/*
setClass (Context packageContext,Class<?> cls)-Convenience for calling setComponent(ComponentName) with the name returned by a Class object.
setComponent (ComponentName component)-Explicitly set the component to handle the intent
Parameters
packageContext	Context: A Context of the application package implementing this class.
This value must never be null.
cls	Class: The class name to set, equivalent to setClassName(context, cls.getName()).
This value must never be null.
Returns
Intent	Returns the same Intent object, for chaining multiple calls into a single statement.
This value will never be null.
 */
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*
        Initialize the contents of the Activity's standard options menu. You should place your menu items in to menu
        Parameters
menu	Menu: The options menu in which you place your items.
Returns
boolean	You must return true for the menu to be displayed; if you return false it will not be shown.
         */
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //here we set the list and get the list from them
        switch (id) {

            case R.id.watchlist:

                WatchList();

                return true;

            case R.id.favorites:

                Favorites();

                return true;

            case R.id.refresh:

                String barTitle = getActivity().getTitle().toString();

                switch (barTitle) {
                    case Constants.MOST_POPULAR:
                        SetList(Constants.MOVIE_POPULAR);
                    case Constants.UPCOMING:
                        SetList(Constants.MOVIE_UPCOMING);
                    case Constants.NOW_PLAYING:
                        SetList(Constants.MOVIE_NOW_PLAYING);
                    case Constants.TOP_RATED:
                        SetList(Constants.MOVIE_TOP_RATED);
                    case Constants.LATEST:
                        SetListSingleMovie(Constants.MOVIE_LATEST);
                }

                return true;

            case R.id.most_popular:

                SetList(Constants.MOVIE_POPULAR);
                getActivity().setTitle(Constants.MOST_POPULAR);

                return true;

            case R.id.upcoming_movies:

                SetList(Constants.MOVIE_UPCOMING);
                getActivity().setTitle(Constants.UPCOMING);

                return true;

            case R.id.latest_movies:

                SetListSingleMovie(Constants.MOVIE_LATEST);
                getActivity().setTitle(Constants.LATEST);

                return true;

            case R.id.now_playing:

                SetList(Constants.MOVIE_NOW_PLAYING);
                getActivity().setTitle(Constants.NOW_PLAYING);

                return true;

            case R.id.top_rated:

                SetList(Constants.MOVIE_TOP_RATED);
                getActivity().setTitle(Constants.TOP_RATED);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetList(String context_path) {
//this for set the list for the lovie
        URL = Constants.BASE_URL + Constants.API_VERSION + "/" + context_path + Constants.API_KEY;
        movieList.clear();//once urk is et clear the list and get the new movie list
        new GetMovieInfo(getActivity(), movieList, listview).execute(URL);
    }

    private void SetListSingleMovie(String context_path) {
//this for single movie information
        URL = Constants.BASE_URL + Constants.API_VERSION + "/" + context_path + Constants.API_KEY;
        movieList.clear();
        new GetSingleMovieInfo(getActivity(), movieList, listview).execute(URL);
    }

    private void Favorites() {
        //this is to add list in favorites from the database
        movieList.clear();
        MovieDataBase db = new MovieDataBase(getActivity());
        movieList = db.getFavorites();
        if (movieList.isEmpty()) {
            //if the list is empty
            Toast.makeText(getActivity(), "Favorites list is empty", Toast.LENGTH_SHORT).show();
        } else {
            //otherwise get the list using list adapter
            getActivity().setTitle(Constants.FAVORITES);
            adapter = new ListAdapter(getActivity(), R.layout.list_item, movieList);
            listview.setAdapter(adapter);
        }
    }

    private void WatchList() {
        //it is to see the watchlist
        movieList.clear();
        MovieDataBase db = new MovieDataBase(getActivity());
        movieList = db.getWatchList();
        if (movieList.isEmpty()) {
            Toast.makeText(getActivity(), "Watchlist is empty", Toast.LENGTH_SHORT).show();
        } else {
            getActivity().setTitle(Constants.WATCHLIST);
            adapter = new ListAdapter(getActivity(), R.layout.list_item, movieList);
            listview.setAdapter(adapter);
        }
    }

}