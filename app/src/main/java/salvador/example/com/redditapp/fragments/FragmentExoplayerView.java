package salvador.example.com.redditapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import salvador.example.com.redditapp.R;

public class FragmentExoplayerView extends Fragment implements ExoPlayer.EventListener, View.OnClickListener{
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private View view;
    private Uri uriVieo;
    private int mPosition;
    private int mPreviousPosition = 0;
    private static final String TAG = FragmentExoplayerView.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mPreviousPosition = savedInstanceState.getInt("sesion",0);
            Log.d(TAG, "onCreateView: "+mPreviousPosition);
        }
        view = inflater.inflate(R.layout.exoplayer,container, false);

        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);
        uriVieo =  Uri.parse((String) getArguments().get("url_video"));

        return view;
    }


    private void initializePlayer(Uri mediaUri) {


        if (mExoPlayer == null) {

            // Create an instance of the ExoPlayer.
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveVideoTrackSelection.Factory(bandwidthMeter));
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            if(mediaUri.toString().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.novideo);
                mPlayerView.setDefaultArtwork(bitmap);
                mPlayerView.setUseController(false);

            }else {
                // Set the ExoPlayer.EventListener to this activity.
                mExoPlayer.addListener(this);
                mPlayerView.setUseController(false);
                // Prepare the MediaSource.
                mExoPlayer.seekTo(mPreviousPosition);
                String userAgent = Util.getUserAgent(getContext(), getResources().getString(R.string.app_name));
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
                mExoPlayer.prepare(loopingSource);
                mExoPlayer.setPlayWhenReady(true);
            }

        }
    }
    private void releaseExoplayer(){

        if(mExoPlayer != null){
            Log.d(TAG, "releaseExoplayer: != null");
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.stop();
            mPlayerView.setDefaultArtwork(null);
            mExoPlayer.release();
        } mExoPlayer = null;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("position",mPosition);

        if (mExoPlayer!= null){
            Log.d(TAG, "onSaveInstanceState: " + mExoPlayer.getCurrentPosition());
            outState.putInt("sesion", (int) mExoPlayer.getCurrentPosition());
            mPreviousPosition = (int) mExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(TAG, "onActivityCreated: ");
            mPreviousPosition = savedInstanceState.getInt("sesion", 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: "+mPreviousPosition);
        initializePlayer(uriVieo);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        releaseExoplayer();
    }
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
