package com.example.dhiman.muse.Helpers;


import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.dhiman.muse.Fragments.MiniPlayerFragment;
import com.example.dhiman.muse.Models.ModelSong;
import com.example.dhiman.muse.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

public class MusicPlayerController implements ExoPlayer.EventListener,ExtractorMediaSource.EventListener {
    private static MusicPlayerController muse = null;
    private static Context serviceContext;
    private int myPlaybackState = 0;
    private static final String PLAY_MUSIC_API = "playmusic";
    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private ExtractorsFactory extractorsFactory;
    private TrackSelection.Factory trackSelectionFactory;
    private TrackSelector trackSelector;
    private DefaultBandwidthMeter defaultBandwidthMeter;
    private DataSource.Factory dataSourceFactory;
    private DefaultHttpDataSource myDataSource;
    private boolean isPlaying = false;
    MediaSource mediaSource;
    private listenerFragmentInterface f = null;

    private MusicPlayerController(){
        bandwidthMeter = new DefaultBandwidthMeter();
        extractorsFactory = new DefaultExtractorsFactory();
        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        defaultBandwidthMeter = new DefaultBandwidthMeter();
        myDataSource = new DefaultHttpDataSource(Util.getUserAgent(serviceContext, "mediaPlayerSample"),null);
        player = ExoPlayerFactory.newSimpleInstance(serviceContext, trackSelector);
        player.addListener(this);
    }
    public static MusicPlayerController getPlayerInstance(Context context){//From Service
        serviceContext = context;
        if(muse==null){
            muse =  new MusicPlayerController();
            return muse;
        }
        return muse;
    }
    public static MusicPlayerController getPlayerInstance(){//FROM Fragment Or Activity
        return muse;
    }
    public void setSongToPlay(ModelSong song){
        if(song.musicId == null){
            setPlaybackState(MusicPlayerController.STATE_ERROR);
            //again req to play the song
            return;
        }
        if(isPlaying){
            player.stop();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("DEBUG","Player: Starting to Play : "+song.musicId );
        myDataSource.setRequestProperty("musicid",song.musicId);
        dataSourceFactory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return myDataSource;
            }
        };
        mediaSource = new ExtractorMediaSource(Uri.parse(serviceContext.getResources().getString(R.string.muse_base_url)+PLAY_MUSIC_API), dataSourceFactory, extractorsFactory, null, this);
        player.prepare(mediaSource);
        Log.d("DEBUG","Player: Preparing to Play : "+song.musicId );
        play();
    }
    public void play(){
        if(player!=null){
            player.setPlayWhenReady(true);
        }
    }
    public void pause(){
        if(player!=null){
            player.setPlayWhenReady(false);
        }
    }
    public boolean isPlaying(){
        return isPlaying;
    }
    public void next(ModelSong song){
        if(player!=null){
            setSongToPlay(song);
            play();
        }
    }
    public void previous(ModelSong song){
        if(player!=null){
            setSongToPlay(song);
            play();
        }
    }
    public void stopPlayer(){
        if(player!=null){
            player.stop();
            player.release();
        }
    }
    public long getCurrentPosition(){
        return player.getCurrentPosition();
    }
    public long getBufferedPosition(){
        return player.getBufferedPosition();
    }
    public void seekTo(long position){
        player.seekTo(position);
    }
    private void setPlaybackState(int i){
        myPlaybackState = i;
    }
    public int getPlaybackState(){
        return myPlaybackState;
    }
    public static final int STATE_LOADING =1;
    public static final int STATE_STOP_LOADING = 7;
    public static final int STATE_BUFFERING =2;
    public static final int STATE_PLAYING_ENDED =3;
    public static final int STATE_PLAYING =4;
    public static final int STATE_PAUSED =5;
    public static final int STATE_ERROR = 6;

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        if(isLoading){
            setPlaybackState(MusicPlayerController.STATE_LOADING);
            if(f!=null){
                f.setPlaybackState(MusicPlayerController.STATE_LOADING);
            }

        }
        else{
             if(f!=null){
                f.setPlaybackState(MusicPlayerController.STATE_STOP_LOADING);
            }
        }
        Log.d("DEBUG","Player: OnLoading : "+isLoading);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playbackState == ExoPlayer.STATE_BUFFERING){
            setPlaybackState(MusicPlayerController.STATE_BUFFERING);
            if(f!=null){
                f.setPlaybackState(MusicPlayerController.STATE_BUFFERING);
            }
            isPlaying = false;
        }
        else if(playbackState==ExoPlayer.STATE_READY){
            if(playWhenReady==true){
                isPlaying = true;
                setPlaybackState(MusicPlayerController.STATE_PLAYING);
                if(f!=null){
                    f.setPlaybackState(MusicPlayerController.STATE_PLAYING);
                }
            }
            else{

                isPlaying = false;
                setPlaybackState(MusicPlayerController.STATE_PAUSED);
                if(f!=null){
                    f.setPlaybackState(MusicPlayerController.STATE_PAUSED);
                }
            }
        }
        else if(playbackState == ExoPlayer.STATE_ENDED){
            setPlaybackState(MusicPlayerController.STATE_PLAYING_ENDED);
            if(f!=null){
                f.setPlaybackState(MusicPlayerController.STATE_PLAYING_ENDED);
            }
            if(playWhenReady == true){
                //req_next_song
            }
            else{
                //do something
            }
        }
        else if (playbackState == ExoPlayer.STATE_IDLE){
            if(playWhenReady == true){
                //req_next_song
            }
            else{
                player.release();
            }
        }
        Log.d("DEBUG","Player: playbackState : "+playbackState +" PlayWhenReady: "+playWhenReady);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        setPlaybackState(MusicPlayerController.STATE_ERROR);
        Log.d("DEBUG","Player: OnError : "+error);
    }

    @Override
    public void onPositionDiscontinuity() {
        Log.d("DEBUG","Player: onPositionDiscontinuity : called");
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onLoadError(IOException error) {
        setPlaybackState(MusicPlayerController.STATE_ERROR);
        Log.d("DEBUG","Player: OnLoadError : called");
    }
    public void addListenerFragment(Fragment fd){
        f =(listenerFragmentInterface)fd;
    }
    public interface listenerFragmentInterface{
        void setPlaybackState(int i);
    }
}
