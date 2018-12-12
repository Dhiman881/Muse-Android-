package com.example.dhiman.muse;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dhiman.muse.Helpers.MusicPlayerController;
import com.example.dhiman.muse.Models.ModelSong;

import java.util.ArrayList;

public class MusicService extends Service {
    private MusicPlayerController musicPlayer;
    private ArrayList<ModelSong> currentPlaylist;
    private int currentPlayingPosition;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("DEBUG","MusicService Started");
        musicPlayer = MusicPlayerController.getPlayerInstance(this);
        if(musicPlayer!=null){
            Log.v("DEBUG","MusicPlayer Instance Created");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.stopPlayer();
        Log.v("DEBUG","MusicService Destroyed");
    }
}
