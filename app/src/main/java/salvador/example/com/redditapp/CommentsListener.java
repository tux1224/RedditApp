package salvador.example.com.redditapp;

import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;

public interface CommentsListener {

    void onClick(View v);
    boolean onUp(View v);
    ArrayList<Integer> getDisplacement();
    Parcelable getRecyclerInstance();
}
