package salvador.example.com.redditapp.fragments;



import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import salvador.example.com.redditapp.BuildConfig;
import salvador.example.com.redditapp.DetailActivity;
import salvador.example.com.redditapp.InterFaceAfdapter;
import salvador.example.com.redditapp.R;
import salvador.example.com.redditapp.RedditDb;
import salvador.example.com.redditapp.Utils;
import salvador.example.com.redditapp.adapters.AdapterFeed;
import salvador.example.com.redditapp.model.ChildrenItem;
import salvador.example.com.redditapp.model.Data;
import salvador.example.com.redditapp.model.Home;
import salvador.example.com.redditapp.network.ApiUtils;

public class FragmentFeed extends Fragment  implements  InterFaceAfdapter, LoaderManager.LoaderCallbacks<Home>{
    private static final String BUNDLE_RECYCLER_LAYOUT = "_recycler";

    private static final String TAG = FragmentFeed.class.getSimpleName();
    private View view;
    private static Home home = null;
    private RecyclerView mRecycler;
    private AdapterFeed mAdapter;
    private Loader<Object> loaderManager;
    private static  String type ;
    private ProgressBar progressBar;
    private FrameLayout opacity;
    Loader<Home> homeLoader;
    private AsyncTask mAsync;
    private static final int LOADER_ID = 432;
    Parcelable savedRecy;
    private Parcelable lastFirstVisiblePosition;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feed,container,false);
        opacity = (FrameLayout) view.findViewById(R.id.opacity);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        mRecycler = (RecyclerView) view.findViewById(R.id.recycler_feed);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new AdapterFeed();
        mAdapter.setListener(this);
        mRecycler.setAdapter(mAdapter);

        if (savedInstanceState!= null){
            if (savedInstanceState.containsKey("_recycler")){

            }
        }
        if(getArguments()!= null){
            if(getArguments().containsKey(BUNDLE_RECYCLER_LAYOUT))
        {
             lastFirstVisiblePosition = getArguments().getParcelable(BUNDLE_RECYCLER_LAYOUT);

        }
        }




        fetchData(getArguments());
            return view;
    }



    public void fetchData(Bundle bundle){
        loaderManager = getLoaderManager().getLoader(LOADER_ID);




            type  = (bundle == null ) ? ApiUtils.HOME : bundle.getString("type");
            Log.d(TAG, "fetchData: "+type);
            if (type.equals(ApiUtils.HOME)) {

                if (loaderManager == null){
                    getLoaderManager().initLoader(LOADER_ID,null,this);
                }else{
                    getLoaderManager().restartLoader(LOADER_ID,null,this);
                }
            }else{

                    ApiUtils.mApiService.getHome(type).enqueue(new Callback<Home>() {
                                                                   @Override
                                                                   public void onResponse(Call<Home> call, Response<Home> response) {
                                                                       if (!response.isSuccessful()) {
                                                                           return;
                                                                       }
                                                                       home = response.body();
                                                                       mAdapter.setData(home);
                                                                       opacity.setVisibility(View.GONE);
                                                                       progressBar.setVisibility(View.GONE);
                                                                       mRecycler.getLayoutManager().onRestoreInstanceState(lastFirstVisiblePosition);

                                                                   }

                                                                   @Override
                                                                   public void onFailure(Call<Home> call, Throwable t) {
                                                                       Log.d(TAG, "onFailure: " + t.getMessage());
                                                                   }
                                                               });


                    }
    }



   public Parcelable getRecyclerInstance(){
       lastFirstVisiblePosition  = ((LinearLayoutManager) mRecycler.getLayoutManager()).onSaveInstanceState();
       return lastFirstVisiblePosition;
   }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("_recycler",lastFirstVisiblePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onData(View v) {
        Bundle bundle;
        Intent intent;
        switch (v.getId()) {
            case R.id.share:
                shareTextUrl(v);
                break;

            default:
                ChildrenItem childrenItem = mAdapter.getItem((Integer) v.getTag(R.id.position));
                Log.d(TAG, "onData: "+childrenItem.toString());
                bundle = new Bundle();
                bundle.putString("child",new Gson().toJson(childrenItem).toString());
                intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

        }


    }



    private void shareTextUrl(View v) {
        ChildrenItem childrenItem = mAdapter.getItem((Integer) v.getTag(R.id.position));
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
    @Override
    public void reloadListener(String type){
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fetchData(bundle);
    }

    @NonNull
    @Override
    public Loader<Home> onCreateLoader(int id, @Nullable final Bundle args) {


        return new AsyncTaskLoader<Home>(getContext()) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
            @Nullable
            @Override
            public Home loadInBackground() {
                Home mHome;

                ContentValues values = new ContentValues();
                values.put(Utils.REQUEST,Utils.SELECT_ALL_FAV_DB);
                Uri uri = Utils.buildUri(values);
                Cursor cursor = getContext().getContentResolver().query(uri,null,null,
                        null,
                        RedditDb.RedditFav.COLUMN_ID);
                Log.d(TAG, "isThisFavorite() returned: " + cursor.getCount());
                mHome = new Home();
                Data data = new Data();
                mHome.setData(data);

                ArrayList<ChildrenItem> childrenItems = new ArrayList<>();

                while (cursor.moveToNext()){
                    String json = cursor.getString(cursor.getColumnIndex(RedditDb.RedditFav.COLUMN_JSON));
                    Log.d(TAG, "LOADING SAVEDITMES: "+json);
                    childrenItems.add(new Gson().fromJson(json,ChildrenItem.class));
                }
                data.setChildren(childrenItems);
                mHome.setData(data);
                deliverResult(mHome);
            return mHome;
            }

            @Override
            public void deliverResult(@Nullable Home data) {
                super.deliverResult(data);

            }
        };



    }

    @Override
    public void onLoadFinished(@NonNull Loader<Home> loader, Home data) {
        if (data == null){
            Log.d(TAG, "onLoadFinished: Load Failed");
        }else{
            Log.d(TAG, "Home feed loaded");
            mAdapter.setData(data);
            opacity.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            mRecycler.getLayoutManager().onRestoreInstanceState(lastFirstVisiblePosition);

        }

    }



    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }
}
