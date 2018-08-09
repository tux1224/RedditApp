package salvador.example.com.redditapp.widget;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import salvador.example.com.redditapp.R;
import salvador.example.com.redditapp.RedditDb;
import salvador.example.com.redditapp.Utils;
import salvador.example.com.redditapp.model.Home;

public class WidgetRemoteViewService extends RemoteViewsService {
    private Cursor data;
    private String TAG = WidgetRemoteViewService.class.getSimpleName();


    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        return new RemoteViewsFactory(){

            @Override
            public void onCreate() {

            }


            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                ContentValues values = new ContentValues();
                values.put(Utils.REQUEST,Utils.SELECT_ALL_FAV_DB);
                Uri uri = Utils.buildUri(values);
                data     = getContentResolver().query(uri,null,
                        null,
                        null,
                        RedditDb.RedditFav.COLUMN_ID);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {

                if (data != null){
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data != null ? data.getCount():0;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == -1 ||
                        data == null || !data.moveToPosition(position)) {

                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.item_listview);

                String title = data.getString(data.getColumnIndex(RedditDb.RedditFav.COLUMN_TITLE));
                String json = data.getString(data.getColumnIndex(RedditDb.RedditFav.COLUMN_JSON));
                Log.d(TAG, "getViewAt: title");
                views.setTextViewText(R.id.tv_item,title);
                Intent intentDetail = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("child",json);
                intentDetail.putExtras(bundle);
                views.setOnClickFillInIntent(R.id.tv_item_view,intentDetail);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(),R.layout.item_listview);
            }
            @Override
            public int getViewTypeCount() {
                return 1;
            }
            @Override
            public long getItemId(int position) {
                return position;
            }
            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}
