package salvador.example.com.redditapp.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import salvador.example.com.redditapp.InterFaceAfdapter;
import salvador.example.com.redditapp.R;
import salvador.example.com.redditapp.fragments.FragmentFeed;
import salvador.example.com.redditapp.model.ChildrenItem;
import salvador.example.com.redditapp.model.Home;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.ViewHolder>{

    private static final String TAG = AdapterFeed.class.getSimpleName();

    private InterFaceAfdapter listener;
    private Home home;
    private Context context;

    public void setListener(InterFaceAfdapter listener){
        this.listener = listener;
    }

    public void setData(Home data){
        if (data == null) {
            return;
        }
        home = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(viewType, parent, false);
         return new ViewHolder(view);
    }

    public ChildrenItem getItem(int position){

        return home.getData().getChildren().get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {

        final ChildrenItem childrenItem = getItem(position);

        String image = "";


        holder.view.setTag(R.id.position,position);
        holder.share.setTag(R.id.position,position);
        holder.title.setText(childrenItem.getData().getTitle());
        holder.subredditName.setText(childrenItem.getData().getSubredditNamePrefixed());
        holder.score.setText(String.valueOf(childrenItem.getData().getScore()));
        holder.comments.setText(String.valueOf(childrenItem.getData().getNumComments()));
        holder.view.setVisibility(View.VISIBLE);
        holder.imageView.setVisibility(View.GONE);
        holder.imageView.setImageDrawable(null);

        holder.imageView.setImageResource(android.R.color.transparent);


        Log.d(TAG, "bindData: "+childrenItem.getData().getPermalink());
        Log.w(TAG, "bindData: "+childrenItem.getData().toString());

        try {
            image = (childrenItem.getData().getThumbnail());
            loadImage(childrenItem, image, holder.imageView);

        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: ",e );
            image = "none";
            loadImage(childrenItem, image, holder.imageView);

        }

    }
    private void loadImage(final ChildrenItem childrenItem, final String image, final ImageView imageView){

         Log.d(TAG, "loadImage: "+image);

        /*if (childrenItem.getData().getPreview().getImages().get(0).getSource().getHeight() > 400){
             imageView.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
             imageView.getLayoutParams().height =(childrenItem.getData().getPreview().getImages().get(0).getSource().getHeight());
         }*/

        Glide.with(((Activity)context))
                .load(image)
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d(TAG, "onLoadFailed: ");
                imageView.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d(TAG, "onResourceReady: ");
                imageView.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(imageView);

        imageView.setVisibility(View.VISIBLE);


    }

    private void loadWebView(ChildrenItem childrenItem, String url, final WebView webView){

        if (childrenItem.getData().getPreview().getImages().get(0).getSource().getHeight() > 400) {
            webView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            webView.getLayoutParams().height = childrenItem.getData().getPreview().getImages().get(0).getSource().getHeight();
        }
       webView.setWebViewClient(new WebViewClient(){


           @Override
           public void onPageFinished(WebView view, String url) {
               super.onPageFinished(view, url);
               webView.setVisibility(View.VISIBLE);

           }
       });
        webView.getLayoutParams().height = 800;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);


    }

    void vibrate(){
        Vibrator v = (Vibrator)  ((FragmentFeed) listener).getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(0, VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            v.vibrate(50);
        }
    }

    @Override
    public int getItemViewType(final int position) {
        String type = getItem(position).getData().getPostHint() == null ? "" : getItem(position).getData().getPostHint();
        int mType = R.layout.feed_item;

        switch (type){

            case "image":
                Log.d(TAG, "getItemViewType: image");
               return mType = R.layout.feed_item;

            default:
                Log.d(TAG, "getItemViewType: default");
                return mType = R.layout.feed_item;

        }
    }

    @Override
    public int getItemCount() {

        return (home != null) ? home.getData().getChildren().size() : 0 ;
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        private WebView webView;
        private TextView title
                , subredditName
                ,score
                ,comments;
        private ImageView imageView
                ,share
                ,imageComments;

        public  ViewHolder(final View itemView) {
            super(itemView);
            title           = (TextView) itemView.findViewById(R.id.title);
            subredditName   = (TextView) itemView.findViewById(R.id.subreddit_name_prefixed);
            score           = (TextView) itemView.findViewById(R.id.feed_score);
            comments        = (TextView) itemView.findViewById(R.id.num_comments);
            imageView       = (ImageView) itemView.findViewById(R.id.image_view);
            share           = (ImageView) itemView.findViewById(R.id.share);
            share.setOnClickListener(this);

            view = itemView;
            view.setOnClickListener(this);

        }

        @Nullable
        @Override
        public void onClick(View v) {
                 listener.onData(v);
        }

    }
}
