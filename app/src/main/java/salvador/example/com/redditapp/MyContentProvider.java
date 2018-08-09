package salvador.example.com.redditapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.dean.jraw.models.Subreddit;

public class MyContentProvider extends ContentProvider{

    private SQLiteHelp sqLiteHelp;

    public static final int FAVORITE = 100;
    public static final int FAVORITE_WITH_ID = 101;

    public static final int SUBREDDITS = 200;
    public static final int SUBREDDITS_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(RedditDb.AUTHORITY, RedditDb.PATH_REDDITS_FAV, FAVORITE);
        uriMatcher.addURI(RedditDb.AUTHORITY, RedditDb.PATH_SUBREDDITS, SUBREDDITS);

        uriMatcher.addURI(RedditDb.AUTHORITY, RedditDb.PATH_REDDITS_FAV+ "/*", FAVORITE_WITH_ID);
        uriMatcher.addURI(RedditDb.AUTHORITY, RedditDb.PATH_SUBREDDITS+ "/*", SUBREDDITS_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {

        Context context = getContext();
        sqLiteHelp = new SQLiteHelp(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = sqLiteHelp.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        String id;

        switch (match) {
            // Query for the tasks directory
            case FAVORITE:
                retCursor =  db.query(RedditDb.RedditFav.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

                case SUBREDDITS:
                retCursor =  db.query(RedditDb.SubReddits.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAVORITE_WITH_ID:
                id = uri.getPathSegments().get(1);
                retCursor = db.query(RedditDb.RedditFav.TABLE_NAME, projection,
                        selection+"=?",new String[]{id},null,null,sortOrder);
                break;

                case SUBREDDITS_WITH_ID:
                id = uri.getPathSegments().get(1);
                retCursor = db.query(RedditDb.SubReddits.TABLE_NAME, projection,
                        selection+"=?",new String[]{id},null,null,sortOrder);
                break;

            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new  UnsupportedOperationException("Not yet implemented");

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = sqLiteHelp.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match){
            case FAVORITE:
                id = db.insert(RedditDb.RedditFav.TABLE_NAME,null,values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(RedditDb.RedditFav.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case SUBREDDITS:
                id = db.insert(RedditDb.SubReddits.TABLE_NAME,null,values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(RedditDb.SubReddits.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);


        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = sqLiteHelp.getWritableDatabase();

        String id;
        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case FAVORITE_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(RedditDb.RedditFav.TABLE_NAME, RedditDb.RedditFav.COLUMN_ID+"=?", new String[]{id});
                break;

                case SUBREDDITS_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(RedditDb.SubReddits.TABLE_NAME, RedditDb.SubReddits.COLUMN_ID+"=?", new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
