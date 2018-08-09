package salvador.example.com.redditapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public interface InterFaceAfdapter {

    void onData(View v);
    void reloadListener(String type);
    Parcelable getRecyclerInstance();


}
