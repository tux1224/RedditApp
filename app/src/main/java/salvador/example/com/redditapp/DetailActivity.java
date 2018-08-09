package salvador.example.com.redditapp;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import salvador.example.com.redditapp.fragments.FragmentCommentsLoader;
import salvador.example.com.redditapp.fragments.FragmentExoplayerView;
import salvador.example.com.redditapp.model.ChildrenItem;
import salvador.example.com.redditapp.network.ApiUtils;
import salvador.example.com.redditapp.widget.WidgetProvider;

import static salvador.example.com.redditapp.fragments.FragmentCommentsLoader.DISPLACEMENT;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {



    //region declarations
    private static final String TAG = DetailActivity.class.getSimpleName();
    private Bundle bundle;
    private Toolbar toolbar;
    private ChildrenItem childrenItem;
    private ImageView imageView, imageAdd, imageShare, imageFavorite;
    private TextView textView, feedScore, numComments, title, subredditPrefix;
    private FrameLayout frameLayout;
    private FrameLayout opacity;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private WebView webView;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;
    private String permaLinkBuilded;
    private CommentsListener listener;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private ProgressBar progressBar;
    private String jsonRetr;
    private int mOrientation;
    private ArrayList<Integer> mDisplacement;
    private Tracker mTracker;
    public static final String RECYCLER_INSTANCE = "_recycler";
    private Parcelable mRecyclerInstance;
    //endregion


    public void setListener(CommentsListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("DetailActivity open")
                .build());
        if (savedInstanceState!= null) {
            if (savedInstanceState.containsKey(DISPLACEMENT)) {
                mDisplacement = savedInstanceState.getIntegerArrayList(DISPLACEMENT);
            }
            if (savedInstanceState.containsKey(RECYCLER_INSTANCE)){
                mRecyclerInstance = savedInstanceState.getParcelable(RECYCLER_INSTANCE);
            }
        }
        mOrientation = this.getResources().getConfiguration().orientation;

        //region initialization
        imageView       = (ImageView) findViewById(R.id.image_view_header);
        imageFavorite   = (ImageView) findViewById(R.id.poster_layout).findViewById(R.id.imageFavorite);
        imageAdd        = (ImageView) findViewById(R.id.poster_layout).findViewById(R.id.imageAdd);
        imageShare      = (ImageView) findViewById(R.id.poster_layout).findViewById(R.id.imageShare);
        toolbar         = (Toolbar) findViewById(R.id.toolbarDetail);
        textView        = (TextView) findViewById(R.id.poster_layout).findViewById(R.id.tv_description);
        feedScore       = (TextView) findViewById(R.id.poster_layout).findViewById(R.id.feed_score);
        numComments     = (TextView) findViewById(R.id.poster_layout).findViewById(R.id.num_comments);
        title           = (TextView) findViewById(R.id.poster_layout).findViewById(R.id.title);
        subredditPrefix = (TextView) findViewById(R.id.poster_layout).findViewById(R.id.subreddit_name_prefixed);
        frameLayout     = (FrameLayout) findViewById(R.id.detailFrame);
        opacity     = (FrameLayout) findViewById(R.id.opacity);
        appBarLayout    = (AppBarLayout) findViewById(R.id.appBarLayout);
        progressBar     = (ProgressBar) findViewById(R.id.loading_indicator);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
         imageFavorite.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imageAdd.setOnClickListener(this);
        imageShare.setOnClickListener(this);
        textView.setOnClickListener(this);

        //endregion
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);


        childrenItem = new Gson().fromJson(getIntent().getExtras().get("child").toString(),ChildrenItem.class);
        toolbar.setTitle(childrenItem.getData().getTitle());
        toolbar.setSubtitle(childrenItem.getData().getTitle());

        subredditPrefix .setText(childrenItem.getData().getSubredditNamePrefixed());
        textView        .setText(childrenItem.getData().getTitle());
        feedScore       .setText(String.valueOf(childrenItem.getData().getScore()));
        numComments     .setText(String.valueOf(childrenItem.getData().getNumComments()));

        isThisFavorite(childrenItem.getData().getId().toString(), this);
        isRedditSaved(childrenItem.getData().getSubredditNamePrefixed().toString(),this);




        String id;
        if(!Utils.isNetworkConnected(this)){
            finish();
            return;
        }
        if (childrenItem.getData().getDomain().equals("youtube.com")){
              id = Uri.parse(childrenItem.getData().getUrl()).getQueryParameter("v");
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                appBarLayout.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.GONE);
            }
            loadYoutubeVideo(id,frameLayout);
        }else if(childrenItem.getData().getDomain().equals("youtu.be")){
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                appBarLayout.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.GONE);
            }
            id = Uri.parse(childrenItem.getData().getUrl()).getLastPathSegment();
            loadYoutubeVideo(id,frameLayout);
        }else{
            appBarLayout.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.VISIBLE);

            try {

                String image = childrenItem.getData().getPreview().getImages().get(0).getSource().getUrl();
                if (image.contains(".gif?")){
                    image = childrenItem.getData().getThumbnail();
                }
                if (childrenItem.getData().getPreview().getImages().get(0).getSource().getHeight() > 500) {
                    imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    imageView.getLayoutParams().height = (childrenItem.getData().getPreview().getImages().get(0).getSource().getHeight());

                }

             loadImage(image);
            }catch(NullPointerException e){
                Log.e(TAG, "Exception: ", e);
                imageView.setVisibility(View.GONE);
                appBarLayout.setExpanded(false);
                progressBar.setVisibility(View.GONE);
                opacity.setVisibility(View.GONE);
                ViewCompat.setNestedScrollingEnabled((View) nestedScrollView, false);
            }
        }
        
        String permalink = childrenItem.getData().getPermalink();
        permalink = permalink.substring(1, permalink.length()-1);
        permaLinkBuilded = BuildConfig.SERVER_API_URL + permalink;

        Log.d(TAG, "onCreate: "+BuildConfig.SERVER_API_URL+permalink);

        openFragment(permalink);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void openFragment(String body) {
        Log.d(TAG, "openFragment: "+body);
        Bundle bundle = new Bundle();
        if (mDisplacement!= null){
            bundle.putIntegerArrayList(DISPLACEMENT ,mDisplacement);
        }
        if (mRecyclerInstance != null){
            bundle.putParcelable(RECYCLER_INSTANCE, mRecyclerInstance);
        }
        bundle.putString("_permalink",body);
        Fragment fragment = new FragmentCommentsLoader();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.comments_container,fragment, FragmentCommentsLoader.class.getSimpleName())
                .commit();
    }

    private void loadWebView(String url) {

        Log.d(TAG, "Loading... "+url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setVisibility(View.VISIBLE);

    }

    private void loadYoutubeVideo(final String id, final FrameLayout frameLayout){
        Log.d(TAG, "loadYoutubeVideo: "+id);

        frameLayout.setVisibility(View.VISIBLE);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (!wasRestored) {
                    frameLayout.setVisibility(View.VISIBLE);
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                    youTubePlayer.loadVideo(id);
                    youTubePlayer.play();
                    frameLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    opacity.setVisibility(View.GONE);
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(BuildConfig.YOUTUBE_APP_ID, onInitializedListener);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList(DISPLACEMENT,listener.getDisplacement());
        outState.putParcelable(RECYCLER_INSTANCE, listener.getRecyclerInstance());
        super.onSaveInstanceState(outState);
    }

    private void loadImage(String image){

        Log.i(TAG, "loadImage.... "+image);
        Glide
                .with(this)
                .load(image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d(TAG, "onLoadFailed: ");
                        imageView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        opacity.setVisibility(View.GONE);
                        appBarLayout.setExpanded(false);
                        ViewCompat.setNestedScrollingEnabled((View) nestedScrollView, false);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady: ");
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        opacity.setVisibility(View.GONE);


                        return false;
                    }
                })
                .into(imageView);
        imageView.setVisibility(View.VISIBLE);
    }
    private void loadVideo(String url) {
        Log.d(TAG, "loadVideo: "+url);
        Fragment fragment = new FragmentExoplayerView();
        Bundle bundle = new Bundle();
        Log.d(TAG, "loadView: "+url);
        bundle.putString("url_video",url);
        fragment.setArguments(bundle);
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detailFrame,fragment,FragmentExoplayerView.class.getSimpleName())
                .commit();
        frameLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageFavorite:
                Log.d(TAG, "imageFavorite: ");
                toggleFavorite();
                break;
                case R.id.imageAdd:
                Log.d(TAG, "imageAdd: ");
                toggleReddit();
                break;
            case R.id.imageShare:
                Log.d(TAG, "imageShare: ");
                shareTextUrl();
                break;

            default:
                Log.d(TAG, "default: " + permaLinkBuilded);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(permaLinkBuilded));
                startActivity(i);
        }

    }


    private void toggleFavorite(){

        if(isThisFavorite(String.valueOf(childrenItem.getData().getId()),this))
            deleteFavorite();
        else
            saveFavorite();


        imageFavorite.setImageDrawable(isThisFavorite(String.valueOf(childrenItem.getData().getId()),this)
                ? getResources().getDrawable(R.drawable.ic_favorite)
                : getResources().getDrawable(R.drawable.ic_favorite_border));
        Context context = getApplicationContext();
        Intent dataUpdatedIntent = new Intent(WidgetProvider.action)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }

    private void toggleReddit(){

        if(isRedditSaved(String.valueOf(childrenItem.getData().getSubredditNamePrefixed()),this))
            deleteReddit();
        else
            saveReddit();


        imageAdd.setImageDrawable(isRedditSaved(String.valueOf(childrenItem.getData().getSubredditNamePrefixed()),this)
                ? getResources().getDrawable(R.drawable.ic_remove)
                : getResources().getDrawable(R.drawable.ic_add));
    }

    public boolean isThisFavorite(String id, Context context){
        boolean isFavorite = false;
        ContentValues values = new ContentValues();
        values.put(Utils.REQUEST,Utils.SELECT_ONE_FAV_DB);
        values.put(Utils.SELECT_ONE_FAV_DB,id);
        Uri uri = Utils.buildUri(values);
        Cursor cursor = context.getContentResolver().query(uri,null,
                RedditDb.RedditFav.COLUMN_ID,
                new String[]{id},
                RedditDb.RedditFav.COLUMN_ID);
        Log.d(TAG, "isThisFavorite() returned: " + cursor.getCount());
        isFavorite = (cursor.getCount() > 0) ? true : false;
        cursor.close();
        imageFavorite.setImageDrawable( (isFavorite)
                ? getResources().getDrawable(R.drawable.ic_favorite)
                : getResources().getDrawable(R.drawable.ic_favorite_border));
        return isFavorite;

    }
    public boolean isRedditSaved(String id, Context context){
        boolean isReddit = false;
        ContentValues values = new ContentValues();
        values.put(Utils.REQUEST,Utils.SELECT_ONE_REDDIT_DB);
        values.put(Utils.SELECT_ONE_REDDIT_DB,id);
        Uri uri = Utils.buildUri(values);
        Cursor cursor = context.getContentResolver().query(uri,null,
                RedditDb.RedditFav.COLUMN_ID,
                new String[]{id},
                RedditDb.RedditFav.COLUMN_ID);
        Log.d(TAG, "isRedditSaved() returned: " + cursor.getCount());
        isReddit = (cursor.getCount() > 0) ? true : false;
        imageAdd.setImageDrawable( (isReddit)
                ? getResources().getDrawable(R.drawable.ic_remove)
                : getResources().getDrawable(R.drawable.ic_add));

        return isReddit;

    }

    private void saveFavorite(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(RedditDb.RedditFav.COLUMN_ID,childrenItem.getData().getId());
        contentValues.put(RedditDb.RedditFav.COLUMN_TITLE,childrenItem.getData().getTitle());
        contentValues.put(RedditDb.RedditFav.COLUMN_JSON,new Gson().toJson(childrenItem));
        contentValues.put(Utils.REQUEST, Utils.INSERT_FAV_DB);

        try{
            Uri uri = Utils.buildUri(contentValues);
            contentValues.remove(Utils.REQUEST);

            uri = getContentResolver().insert(uri,contentValues);
            Log.d(TAG, "Insert provider returned: "+uri);



            contentValues.put(Utils.REQUEST,Utils.SELECT_ALL_FAV_DB);
            uri= Utils.buildUri(contentValues);

            Cursor cursor = getContentResolver().query(uri,null,null,null, BaseColumns._ID);
            while(cursor.moveToNext()) {
                Log.d(TAG, "Cursor DB returned: " + cursor.getString(cursor.getColumnIndex(RedditDb.RedditFav.COLUMN_ID)));
            }
            cursor.close();
            if(uri != null) {
                Log.d(TAG, "Uri ---> "+uri.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void saveReddit(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(RedditDb.SubReddits.COLUMN_ID,childrenItem.getData().getSubredditNamePrefixed());
        contentValues.put(RedditDb.SubReddits.COLUMN_TITLE,childrenItem.getData().getTitle());
        contentValues.put(RedditDb.SubReddits.COLUMN_SUBREDDIT_ID,childrenItem.getData().getSubredditNamePrefixed());
        contentValues.put(Utils.REQUEST, Utils.INSERT_REDDIT_DB);

        try{
            Uri uri = Utils.buildUri(contentValues);
            contentValues.remove(Utils.REQUEST);

            uri = getContentResolver().insert(uri,contentValues);
            Log.d(TAG, "Insert provider returned: "+uri);



            contentValues.put(Utils.REQUEST,Utils.SELECT_ALL_REDDIT_DB);
            uri= Utils.buildUri(contentValues);

            Cursor cursor = getContentResolver().query(uri,null,null,null, BaseColumns._ID);
            while(cursor.moveToNext()) {
                Log.d(TAG, "Cursor DB returned: " + cursor.getString(cursor.getColumnIndex(RedditDb.SubReddits.COLUMN_ID)));
            }
            cursor.close();
            if(uri != null) {
                Log.d(TAG, "Uri ---> "+uri.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteFavorite(){
        ContentValues values = new ContentValues();
        values.put(Utils.REQUEST,Utils.DELTE_FAV_DB);
        values.put(Utils.DELTE_FAV_DB, childrenItem.getData().getId());

        Uri uri = Utils.buildUri(values);

        Log.d(TAG, "Uri to delete: "+ uri);
        int returned = getContentResolver().delete(uri,null,null);
        Log.d(TAG, "deleted  returned: " + returned );
        setResult(Activity.RESULT_OK, new Intent().putExtra("update",returned));
    }
    private void deleteReddit(){
        ContentValues values = new ContentValues();
        values.put(Utils.REQUEST,Utils.DELTE_REDDIT_DB);
        values.put(Utils.DELTE_REDDIT_DB, childrenItem.getData().getSubredditNamePrefixed());

        Uri uri = Utils.buildUri(values);

        Log.d(TAG, "Uri to delete: "+ uri);
        int returned = getContentResolver().delete(uri,null,null);
        Log.d(TAG, "deleted  returned: " + returned );
        setResult(Activity.RESULT_OK, new Intent().putExtra("update",returned));
    }

    private void shareTextUrl() {

        String url   =  BuildConfig.SERVER_API_URL + childrenItem.getData().getPermalink();
        String title =  childrenItem.getData().getTitle();
        Intent share =  new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Share reddit!"));
    }
}
