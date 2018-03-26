package com.example.layout.imdb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.layout.imdb.model.MovieInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pri on 10/21/2017.
 */

public class MovieDataBase extends SQLiteOpenHelper {
    /*
    This is the database of the application where we create the tables where we store the values
    Database extends helper class to manage database creation and version management called SQLiteOpenHelper.
    We can create n number of databases to store the data. There are few important attributes to create the database
     */

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Movie Database";
    private static final String TABLE_MOVIEDETAILS = "Movies";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_RELEASE_DATE = "release_date";
    private static final String COLUMN_POSTER_PATH = "poster_path";
    private static final String COLUMN_VOTE_AVERAGE = "vote_average";
    private static final String COLUMN_VOTE_COUNT = "vote_count";
    private static final String COLUMN_IS_FAVORITE = "favorite";
    private static final String COLUMN_IS_WATCHLIST = "watchlist";
    private static final String DATATYPE_NUMERIC = " INTEGER";
    private static final String DATATYPE_VARCHAR = " TEXT";
    private static final String OPEN_BRACE = "(";
    private static final String COMMA = ",";


    /*
    This is table we created for Movie details and this is distinguish between the columns and type we want in the table
    ON CONFLICT REPLACE for syntax to read more naturally
     */
    private static final String CREATE_TABLE_MOVIEDETAILS = CREATE_TABLE + TABLE_MOVIEDETAILS + OPEN_BRACE +
            COLUMN_ID + DATATYPE_VARCHAR + COMMA +
            COLUMN_TITLE + DATATYPE_VARCHAR + COMMA +
            COLUMN_RELEASE_DATE + DATATYPE_VARCHAR + COMMA +
            COLUMN_POSTER_PATH + DATATYPE_VARCHAR + COMMA +
            COLUMN_VOTE_AVERAGE + DATATYPE_VARCHAR + COMMA +
            COLUMN_VOTE_COUNT + DATATYPE_VARCHAR + COMMA +
            COLUMN_IS_FAVORITE + DATATYPE_NUMERIC + COMMA +
            COLUMN_IS_WATCHLIST + DATATYPE_NUMERIC + COMMA +
            "UNIQUE(" + COLUMN_ID + ") ON CONFLICT REPLACE)";

    public MovieDataBase(Context context) {
        /*Context: to use for locating paths to the the database
This value may be null and the database version we gave */
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
/*
Creating the database and execute the table
 */
        db.execSQL(CREATE_TABLE_MOVIEDETAILS);
    }
/*
This is for to add the details to MovieInfo and put the values in respective columns and get the values for it
 */
    public void addMovie(MovieInfo movieInfo) {

        SQLiteDatabase db = this.getWritableDatabase();
        /*
        getWritableDatabase ()-Create and/or open a database that will be used for reading and writing, and returns
        SQLiteDatabase-a read/write database object valid until close() is called
         ContentValues-This class is used to store a set of values that the ContentResolver can process.
         */
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, movieInfo.getId());
        values.put(COLUMN_TITLE, movieInfo.getTitle());
        values.put(COLUMN_RELEASE_DATE, movieInfo.getDate());
        values.put(COLUMN_POSTER_PATH, movieInfo.getPoster());
        values.put(COLUMN_VOTE_AVERAGE, movieInfo.getVote_average());
        values.put(COLUMN_VOTE_COUNT, movieInfo.getVote_count());
        values.put(COLUMN_IS_FAVORITE, movieInfo.getFavorites());
        values.put(COLUMN_IS_WATCHLIST, movieInfo.getWatchList());
        db.insert(TABLE_MOVIEDETAILS, null, values);
        db.close();
        /*the database gets close */
    }

    public MovieInfo getMovie(String id) {
        /*
        This is for to get the movie details
        getReadableDatabase ()-Create and/or open a database and returns SQLiteDatabase	a database object valid until getWritableDatabase() or close() is called.
         Cursor-interface provides random read-write access to the result set returned by a database query
         database for the movie details  and here it will represent with the id of the rows with column index
         */
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIEDETAILS, new String[]{COLUMN_TITLE, COLUMN_RELEASE_DATE,
                        COLUMN_POSTER_PATH, COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT, COLUMN_IS_FAVORITE, COLUMN_IS_WATCHLIST}, COLUMN_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();/*telling the cursor to move to the first index*/
        MovieInfo movieInfo = new MovieInfo();
        /*creating the object for movie info to get the methods we used in movieInfo class*/
        movieInfo.setId(id);
        /*creating the id and if cursor is not equal to null the we set the details to respective columns
        woth respective indices so that they can be in perfect place
         */
        if (cursor != null) {
            movieInfo.setTitle(cursor.getString(0));
            movieInfo.setDate(cursor.getString(1));
            movieInfo.setPoster(cursor.getString(2));
            movieInfo.setVote_average(cursor.getString(3));
            movieInfo.setVote_count(cursor.getString(4));
            movieInfo.setFavorites(cursor.getInt(5));
            movieInfo.setWatchList(cursor.getInt(6));
        }
        return movieInfo;
        /*after seting them then return the movieInfo

         */
    }
//it checks whether give table and it's id works properly or not
    public boolean checkMovie(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIEDETAILS, null, COLUMN_ID + "=?", new String[]{id}, null, null, null, null);
        return cursor.getCount() > 0;
        //retuns until the count is greater than 0
    }
/*This method to add the movies into Favorites and update them everytime with there String id*/
    public int updateMovieF(MovieInfo movieInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, movieInfo.getFavorites());
        return db.update(TABLE_MOVIEDETAILS, values, COLUMN_ID + "=?", new String[]{movieInfo.getId()});
    }

 /*This method to add the movies into watchlist and update them everytime with there String id*/

    public int updateMovieW(MovieInfo movieInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_WATCHLIST, movieInfo.getWatchList());
        return db.update(TABLE_MOVIEDETAILS, values, COLUMN_ID + "=?", new String[]{movieInfo.getId()});
    }

    /*this is to get the favorites list from movieinfo
    This will like not database withe same values as moviedetails
     once the it shows the table then it will show the table according the way we created the table and cursor will move
     to the first column and get the values from the movie info*/
    public List<MovieInfo> getFavorites() {
        List<MovieInfo> allMovies = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIEDETAILS,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_RELEASE_DATE, COLUMN_POSTER_PATH,
                        COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT, COLUMN_IS_FAVORITE}
                , COLUMN_IS_FAVORITE + "=?", new String[]{String.valueOf(1)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                MovieInfo movieInfo = new MovieInfo();
                movieInfo.setId(cursor.getString(0));
                movieInfo.setTitle(cursor.getString(1));
                movieInfo.setDate(cursor.getString(2));
                movieInfo.setPoster(cursor.getString(3));
                movieInfo.setVote_average(cursor.getString(4));
                movieInfo.setVote_count(cursor.getString(5));
                movieInfo.setFavorites(cursor.getInt(6));
                if (cursor.getInt(6) == 1) {
                    allMovies.add(movieInfo);
                }
                /*once the cursor index is 6 then we add the  movieinfo*/
            } while (cursor.moveToNext());
            /*then cursor moveToNext and return the list

             */
        }
        return allMovies;
    }
/*this method same as getFavorites

 */
    public List<MovieInfo> getWatchList() {
        List<MovieInfo> allMovies = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIEDETAILS,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_RELEASE_DATE, COLUMN_POSTER_PATH,
                        COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT, COLUMN_IS_WATCHLIST}
                , COLUMN_IS_WATCHLIST + "=?", new String[]{String.valueOf(1)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                MovieInfo movieInfo = new MovieInfo();
                movieInfo.setId(cursor.getString(0));
                movieInfo.setTitle(cursor.getString(1));
                movieInfo.setDate(cursor.getString(2));
                movieInfo.setPoster(cursor.getString(3));
                movieInfo.setVote_average(cursor.getString(4));
                movieInfo.setVote_count(cursor.getString(5));
                movieInfo.setWatchList(cursor.getInt(6));
                if (cursor.getInt(6) == 1) {
                    allMovies.add(movieInfo);
                }
            } while (cursor.moveToNext());
        }
        return allMovies;
    }
/*once we add the list to favorites and we want to delete from the favorites them we use the method

 */
    public void deleteNonFavWatchMovie() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIEDETAILS, COLUMN_IS_FAVORITE + "=? AND " + COLUMN_IS_WATCHLIST + "=?", new String[]{String.valueOf(0), String.valueOf(0)});
        db.close();
        /*after deleting it it closes the database

         */
    }

    /*
    Called when the database needs to be upgraded. The implementation should use this method to drop tables, add tables, or do anything else it needs to upgrade to the new schema version.
    Parameters
db	SQLiteDatabase: The database.
oldVersion	int: The old database version.
newVersion	int: The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIEDETAILS);
        /*drops the table once it get upgraded adncreate a new database

         */
        onCreate(db);
    }
}
