package salvador.example.com.redditapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelp extends SQLiteOpenHelper {


    // The name of the database
    private static final String DATABASE_NAME = "redditDb.db";


    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    // Constructor
    SQLiteHelp(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE_REDDITS_FAV = "CREATE TABLE "  + RedditDb.RedditFav.TABLE_NAME + " (" +
                RedditDb.RedditFav._ID                + " INTEGER PRIMARY KEY, " +
                RedditDb.RedditFav.COLUMN_TITLE + " TEXT NOT NULL, " +
                RedditDb.RedditFav.COLUMN_JSON + " TEXT NOT NULL, " +
                RedditDb.RedditFav.COLUMN_ID    + " TEXT NOT NULL UNIQUE);";


        final String CREATE_TABLE_SUBREDDITS = "CREATE TABLE "  + RedditDb.SubReddits.TABLE_NAME + " (" +
                RedditDb.SubReddits._ID                + " INTEGER PRIMARY KEY, " +
                RedditDb.SubReddits.COLUMN_TITLE + " TEXT NOT NULL, " +
                RedditDb.SubReddits.COLUMN_ID    + " TEXT NOT NULL UNIQUE,"+
                RedditDb.SubReddits.COLUMN_SUBREDDIT_ID    + " TEXT NOT NULL UNIQUE);";

        db.execSQL(CREATE_TABLE_SUBREDDITS);
        db.execSQL(CREATE_TABLE_REDDITS_FAV);

    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RedditDb.RedditFav.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RedditDb.SubReddits.TABLE_NAME);
        onCreate(db);
    }
}
