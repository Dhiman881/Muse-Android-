package com.example.dhiman.muse.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dhiman.muse.Helpers.MusicPlayerController;
import com.example.dhiman.muse.Models.ModelSong;
import com.example.dhiman.muse.MusicService;
import com.example.dhiman.muse.R;

import java.util.ArrayList;

/**
 * Created by dhiman on 24/3/18.
 */

public class MiniPlayerFragment extends Fragment implements MusicPlayerController.listenerFragmentInterface {
    public MiniPlayerFragment() {
        // Required empty public constructor
    }
    private TextView textViewSongTitle;
    private TextView textViewSongArtist;
    private Context activityContext;
    private MusicPlayerController musicPlayer;
    private ImageView mPlayPauseImage;
    private ProgressBar mLoadingBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_player_mini, container, false);
        textViewSongTitle = view.findViewById(R.id.songTitleMiniFragment);
        textViewSongArtist = view.findViewById(R.id.songArtistMiniFragment);
        mPlayPauseImage = view.findViewById(R.id.play_pause);
        mLoadingBar = view.findViewById(R.id.progress_bar_mini_fragment);
        mPlayPauseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicPlayer==null){
                    //ERROR
                }
                else{
                    if(musicPlayer.isPlaying()){
                        musicPlayer.pause();
                    }
                    else{
                        musicPlayer.play();
                    }
                }
            }
        });
        return view;
    }
    public void playSong(ArrayList<ModelSong> songList,int position,Context context){
        activityContext = context;
        textViewSongArtist.setText(((ModelSong)songList.get(position)).musicArtist);
        textViewSongTitle.setText(((ModelSong)songList.get(position)).musicTitle);
        //activityContext.startService(new Intent(activityContext, MusicService.class));
        musicPlayer = MusicPlayerController.getPlayerInstance();
        Log.d("DEBUG","PlayerCallback MiniPlayerFragment: playlist= "+ songList +"position" + position);
        musicPlayer.setSongToPlay((ModelSong) songList.get(position));
        musicPlayer.addListenerFragment(this);
        mPlayPauseImage.setVisibility(View.GONE);
        mLoadingBar.setVisibility(View.VISIBLE);

    }


    @Override
    public void setPlaybackState( int i) {
        switch(i)
        {
            // case statements
            // values must be of same type of expression
            case MusicPlayerController.STATE_BUFFERING :
                // Statements
                mPlayPauseImage.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                break; // break is optional

            case MusicPlayerController.STATE_PLAYING:
                mPlayPauseImage.setVisibility(View.VISIBLE);
                mPlayPauseImage.setImageResource(R.drawable.ic_pause_black_24dp);
                mLoadingBar.setVisibility(View.GONE);
                // Statements
                break; // break is optional
            case MusicPlayerController.STATE_PLAYING_ENDED:
                mPlayPauseImage.setVisibility(View.VISIBLE);
                mPlayPauseImage.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                mLoadingBar.setVisibility(View.GONE);
                // Statements
                break; // b
            case MusicPlayerController.STATE_PAUSED:
                // Statements
                mPlayPauseImage.setVisibility(View.VISIBLE);
                mPlayPauseImage.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                mLoadingBar.setVisibility(View.GONE);
                break;
            case MusicPlayerController.STATE_LOADING:
                // Statements
                mPlayPauseImage.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                break; // break is optional
            case MusicPlayerController.STATE_STOP_LOADING:
                // Statements
                mLoadingBar.setVisibility(View.GONE);
                mPlayPauseImage.setVisibility(View.VISIBLE);
                if(musicPlayer.isPlaying()){
                    mPlayPauseImage.setImageResource(R.drawable.ic_pause_black_24dp);
                }
                else{
                    mPlayPauseImage.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                }
                break; // break is optional
            // We can have any number of case statements
            // below is default statement,used when none of the cases is true.
            // No break is needed in the default case.
            default :
                // Statements
        }
        Log.d("DEBUG","PlayListener "+ i);
    }
}
