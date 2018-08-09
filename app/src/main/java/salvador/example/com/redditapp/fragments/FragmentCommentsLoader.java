package salvador.example.com.redditapp.fragments;

import android.content.AsyncTaskLoader;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import salvador.example.com.redditapp.CommentsListener;
import salvador.example.com.redditapp.DetailActivity;
import salvador.example.com.redditapp.R;
import salvador.example.com.redditapp.Utils;
import salvador.example.com.redditapp.adapters.AdapterComments;
import salvador.example.com.redditapp.model.Comments;
import salvador.example.com.redditapp.network.ApiUtils;

public class FragmentCommentsLoader  extends Fragment implements CommentsListener {

    private RecyclerView recyclerView;
    private AdapterComments mAdapter;
    private String TAG = FragmentCommentsLoader.class.getSimpleName();
    private ArrayList<Comments> mComments = null;
    private ArrayList<Integer> mDisplacement = null;
    private ArrayList<Comments> throwComments;
    private ArrayList<Comments> aux ;
    public static final String DISPLACEMENT = "displacement";
    private String RECYCLER_INSTANCE = "_recycler";
    private Parcelable mRecyclerInstance;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_comments);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AdapterComments();
        mAdapter.setListener(this);
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                if (mDisplacement.size() >= 1){
                    onUp(null);
                }else{
                    mAdapter.notifyDataSetChanged();
                }

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ((DetailActivity)getActivity()).setListener(this);


        if (getArguments().containsKey(DISPLACEMENT)){
            mDisplacement = getArguments().getIntegerArrayList(DISPLACEMENT);
        }
        if (getArguments().containsKey(RECYCLER_INSTANCE)){
            mRecyclerInstance = getArguments().getParcelable(RECYCLER_INSTANCE);
        }
        if (getArguments().containsKey("_permalink")) {
            String permalink = getArguments().getString("_permalink");


            if (savedInstanceState!= null){

                return view;
            }
                ApiUtils.getAPIService().getPost(permalink).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {


                        if (!response.isSuccessful()) {

                            return;
                        }
                        new AsyncTask<String, Void, ArrayList<Comments>>() {
                            @Override
                            protected ArrayList<Comments> doInBackground(String... strings) {


                                return Utils.selectJson(strings[0]);
                            }

                            @Override
                            protected void onPostExecute(ArrayList<Comments> comments) {
                                super.onPostExecute(comments);
                                        throwComments = mComments;
                                if (mDisplacement != null && mDisplacement.size() > 0) {
                                    reload();
                                } else {

                                    mDisplacement = new ArrayList<>();
                                    mComments = comments;
                                    mAdapter.setData(mComments);
                                    recyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerInstance);
                                }
                            }
                        }.execute(response.body().toString());
;


                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                        Log.e(TAG, "onFailure: ", t);
                    }
                });
            }

        return view;
    }

    public void printComments(ArrayList<Comments> lComments){
        for (Comments comments : lComments) {
            Log.d(TAG, "PrintingComment ---> : " + comments.getScore());
            if (comments.getMoreComments().size() > 0) {
                Log.d(TAG, "HasMoreComments ");
                printComments(comments.getMoreComments());
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (mDisplacement.size() == 0){
            mDisplacement.add(0);
            throwComments = mComments;
        }
        mDisplacement.add((Integer) v.getTag(R.id.position));

        throwComments = throwComments.get((Integer) v.getTag(R.id.position)).getMoreComments();
        mAdapter.setData(throwComments);
        Log.d(TAG, "onClick: "+v.getTag(R.id.position));

        for (int i = 0; i < mDisplacement.size(); i++) {

            Log.d(TAG, "Tracker "+mDisplacement.toString());
        }
    }

    private void reload(){

        if (mDisplacement.size() > 1) {

            aux = mComments;
            for (int i = 1; i <  mDisplacement.size(); i++) {

                throwComments = aux.get(mDisplacement.get(i)).getMoreComments();
                aux = throwComments;

            }
            mAdapter.setData(throwComments);

        }if (mDisplacement.size() == 1){
            mAdapter.setData(mComments);

        }
            Log.d(TAG, "Tracker "+mDisplacement.toString());

    }
    @Override
    public boolean onUp(View v) {

        boolean flag = true;
        if (mDisplacement.size() > 1) {
            flag = false;
            aux = mComments;
            mDisplacement.remove(mDisplacement.size()-1);

            for (int i = 1; i <  mDisplacement.size(); i++) {

                    throwComments = aux.get(mDisplacement.get(i)).getMoreComments();
                    aux = throwComments;

           }
            mAdapter.setData(throwComments);

        }if (mDisplacement.size() == 1){
            mAdapter.setData(mComments);
            mDisplacement.remove(mDisplacement.size()-1);

            flag = false;
        }
            Log.d(TAG, "Tracker "+mDisplacement.toString());

        return flag;
    }

    @Override
    public ArrayList<Integer> getDisplacement() {
        return mDisplacement;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(RECYCLER_INSTANCE,getRecyclerInstance());
        super.onSaveInstanceState(outState);
    }

    @Override
    public Parcelable getRecyclerInstance() {
        return  recyclerView.getLayoutManager().onSaveInstanceState();
    }


}
