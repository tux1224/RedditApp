package salvador.example.com.redditapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import salvador.example.com.redditapp.model.Comments;

public class Utils {

    public static final String REQUEST                  = "request";
    public static final String INSERT_FAV_DB            = "insert_fav_reddit_db";
    public static final String DELTE_FAV_DB             = "delte_fav_db";
    public static final String SELECT_ALL_FAV_DB        = "select_fav_all";
    public static final String SELECT_ONE_FAV_DB        = "select_one_fav_db";

    public static final String INSERT_REDDIT_DB         = "insert_reddit_db";
    public static final String DELTE_REDDIT_DB          = "delte_reddit_db";
    public static final String SELECT_ALL_REDDIT_DB     = "select_reddit_all";
    public static final String SELECT_ONE_REDDIT_DB     = "select_one_reddit_db";
    private static String TAG = Utils.class.getSimpleName();

    public static Uri buildUri(@NonNull ContentValues values){
        Uri uri= null;
        String uriString = "";
        String request = values.getAsString(REQUEST);

        switch (request) {

            case INSERT_FAV_DB:
                uriString = RedditDb.RedditFav.CONTENT_URI.toString();
                break;
            case DELTE_FAV_DB:
                uriString = RedditDb.RedditFav.CONTENT_URI.buildUpon().appendPath(values.getAsString(DELTE_FAV_DB)).toString();
                break;
            case SELECT_ONE_FAV_DB:
                uriString = RedditDb.RedditFav.CONTENT_URI.buildUpon().appendPath(values.getAsString(SELECT_ONE_FAV_DB)).toString();
                break;
            case SELECT_ALL_FAV_DB:
                uriString = RedditDb.RedditFav.CONTENT_URI.toString();
                break;



             case INSERT_REDDIT_DB:
                uriString = RedditDb.SubReddits.CONTENT_URI.toString();
                break;
            case DELTE_REDDIT_DB:
                uriString = RedditDb.SubReddits.CONTENT_URI.buildUpon().appendPath(values.getAsString(DELTE_REDDIT_DB)).toString();
                break;
            case SELECT_ONE_REDDIT_DB:
                uriString = RedditDb.SubReddits.CONTENT_URI.buildUpon().appendPath(values.getAsString(SELECT_ONE_REDDIT_DB)).toString();
                break;
            case SELECT_ALL_REDDIT_DB:
                uriString = RedditDb.SubReddits.CONTENT_URI.toString();
                break;
        }
        try {
            uri = Uri.parse(uriString);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.v(TAG, "Uri Builded ---> " +uri.toString());

        return uri;
    }

    public static ArrayList<Comments> selectJson(String json){
        ArrayList<Comments> comments = null;
        try {
            JSONArray jsonArray= new JSONArray(json);
            Log.d(TAG, ""+jsonArray.get(1).toString());
            JSONObject jsonObject = new JSONObject(jsonArray.get(1).toString());
            comments = loadJson(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comments;
    }
    public static ArrayList<Comments> loadJson(JSONObject jsonObject){
        JSONObject data = null;
        ArrayList<Comments> arrayList = new ArrayList<>();

        try {
            data = (JSONObject) jsonObject.get("data");
            JSONArray jsonArray = data.getJSONArray("children");
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);

                if (jsonObject.get("kind").equals("t1")) {
                    data = (JSONObject) jsonObject.get("data");

                    arrayList.add(loadComments(data));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    public static Comments loadComments(JSONObject data){
        Comments comments = new Comments();

        try {


            comments.setAuthor(data.get("author").toString());
            comments.setCreated_utc(data.get("created_utc").toString());
            comments.setId(data.get("id").toString());
            comments.setBody(data.get("body").toString());
            comments.setScore(data.get("score").toString());
            if (!data.get("replies").equals("")){
                comments.setMoreComments(loadJson((JSONObject) data.get("replies")));
            }else{
                comments.setMoreComments(new ArrayList<Comments>());
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            Toast.makeText(context, "No network connection", Toast.LENGTH_SHORT).show();
        }
        return cm.getActiveNetworkInfo() != null;
    }
}
