package salvador.example.com.redditapp;

import android.Manifest;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.provider.BaseColumns;

public class RedditDb {



    //The authority, which is how your code  knows  which Content Provider to access
    public static final String AUTHORITY = "salvador.example.com.redditapp";
    //The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    // Define the possible paths for accessing data in this movie
    // This is the path for the "tasks" directory
    public static final String PATH_REDDITS_FAV = "reddits_fav";

    public static final String PATH_SUBREDDITS = "subreddits";

    public static final  class RedditFav implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REDDITS_FAV).build();

        // Movie table and column names
        public static final String TABLE_NAME = PATH_REDDITS_FAV;

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ID = "column_id";
        public static final String COLUMN_JSON = "column_json";
    }

    public static final  class SubReddits implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUBREDDITS).build();

        // Movie table and column names
        public static final String TABLE_NAME = PATH_SUBREDDITS;

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ID = "column_id";
        public static final String COLUMN_SUBREDDIT_ID ="subreddit_id";


    }

}
