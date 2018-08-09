package salvador.example.com.redditapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import salvador.example.com.redditapp.fragments.FragmentFeed;
import salvador.example.com.redditapp.network.ApiUtils;
import salvador.example.com.redditapp.widget.WidgetProvider;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG =  MainActivity.class.getSimpleName();

    private NavigationView navigationView;
    private int SECONDMENU_ID = 0121021;
    private InterFaceAfdapter listener;
    private MStatement mStatement;
    private Tracker mTracker;
    private String BUNDLE_RECYCLER_LAYOUT = "_recycler";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mStatement = new MStatement();
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



            Fragment fragment = new FragmentFeed();
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("filter")){
                Log.d(TAG, "SAVEINSTANCE");
                String filter = savedInstanceState.getString("filter");
                String statement = savedInstanceState.getString("statement");
                Log.d(TAG, statement+filter+"");
                mStatement.statement = statement;
                mStatement.filter = filter;
                Bundle bundle = new Bundle();
                bundle.putString("type", statement+filter);
                if (savedInstanceState.containsKey(BUNDLE_RECYCLER_LAYOUT)) {
                    bundle.putParcelable(BUNDLE_RECYCLER_LAYOUT, savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT));
                }
                fragment.setArguments(bundle);
            }

        }
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("MainActivity")
                .build());

        if(!Utils.isNetworkConnected(this)){
            return;
        }
            listener = (InterFaceAfdapter) fragment;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_container,
                            fragment,
                            FragmentFeed.class.getSimpleName())
                    .commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putString("filter",mStatement.filter);
        outState.putString("statement",mStatement.statement);
        if (listener != null) {
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, listener.getRecyclerInstance());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.main, menu);
        addSubredditsToMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {

            case R.id.action_controvertial:
                mStatement.setFilter(ApiUtils.CONTROVERSIAL);

                break;
            case R.id.action_hot:
                mStatement.setFilter(ApiUtils.HOT);

                break;

            case R.id.action_new:
                mStatement.setFilter(ApiUtils.NEW);

                break;
            case R.id.action_rising:
                mStatement.setFilter(ApiUtils.RISING);

                break;

            case R.id.action_top:
                mStatement.setFilter(ApiUtils.TOP);

                break;


        }

        return super.onOptionsItemSelected(item);
    }


    private void addSubredditsToMenu(){
        Menu menu = navigationView.getMenu();

        Cursor cursor = bringSubReddits();
        Log.d(TAG, "addSubredditsToMenu: ");

        for (int i = 0; i < menu.size(); i++) {
        }
        menu.removeGroup(R.id.subreddit_menu);
        menu.addSubMenu(R.id.subreddit_menu, SECONDMENU_ID,Menu.NONE,getResources().getString(R.string.subreddits));

         while (cursor.moveToNext()){


                menu.add(R.id.subreddit_menu, cursor.getInt(cursor.getColumnIndex(RedditDb.SubReddits._ID))
                        , Menu.NONE,
                        cursor.getString(cursor.getColumnIndex(RedditDb.SubReddits.COLUMN_SUBREDDIT_ID)))
                        .setIcon(getResources().getDrawable(R.drawable.redit_icon))
                .setCheckable(true);
         }
         cursor.close();
    }

    
    public Cursor bringSubReddits(){
        boolean isReddit = false;
        ContentValues values = new ContentValues();
        values.put(Utils.REQUEST,Utils.SELECT_ALL_REDDIT_DB);
        Uri uri = Utils.buildUri(values);
        Cursor cursor = getContentResolver().query(uri,null,
                null,
                null,
                BaseColumns._ID);
        Log.d(TAG, "isRedditSaved() returned: " + cursor.getCount());
        isReddit = (cursor.getCount() > 0) ? true : false;

        Log.d(TAG, "isRedditSaved: BOL "+isReddit);
        return cursor;
    }

    private void reloadHome(String statement){
        if (listener!= null) {
            Log.d(TAG, "reloadHome:-----> "+statement);

            listener.reloadListener(statement);
        }else {
            if(!Utils.isNetworkConnected(this)){
                return;
            }
            Fragment fragment = new FragmentFeed();
            listener = (InterFaceAfdapter) fragment;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_container,
                            fragment,
                            FragmentFeed.class.getSimpleName())
                    .commit();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        switch (item.getItemId()){
            case R.id.nav_home:
                mStatement.setStatement(ApiUtils.HOME);
                break;
            case R.id.nav_popular:
                mStatement.setStatement(ApiUtils.POPULAR);
                break;

            case R.id.nav_all:
                mStatement.setStatement(ApiUtils.ALL);
                break;
            case R.id.nav_origin_content:
                mStatement.setStatement(ApiUtils.ORIGINAL);
                break;
            case R.id.nav_videos:
                mStatement.setStatement(ApiUtils.VIDEOS);
                break;
                default:
                    mStatement.setStatement(item.getTitle().toString());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class MStatement  {

           String filter = "";
           String statement = ApiUtils.HOME;




        public void setFilter(String filter) {
            this.filter = filter;

            if (this.statement.equals(ApiUtils.HOME)){
                this.filter = "";
            }
            if (statement.equals(ApiUtils.ORIGINAL) && this.filter.equals(ApiUtils.CONTROVERSIAL)){

            }
            reloadHome(statement + this.filter);

        }


        public void setStatement(String statement) {
            this.statement = statement;
            if (statement.equals(ApiUtils.HOME)){
                this.filter = "";
            }
            reloadHome(statement + this.filter);

        }
    }
}
