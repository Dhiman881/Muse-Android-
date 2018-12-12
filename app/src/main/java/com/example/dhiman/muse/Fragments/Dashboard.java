package com.example.dhiman.muse.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dhiman.muse.Adapters.RecyclerViewAdapterSongList;
import com.example.dhiman.muse.LoginActivity;
import com.example.dhiman.muse.Models.ModelSong;
import com.example.dhiman.muse.NetworkRequest.NetworkResponseListener;
import com.example.dhiman.muse.NetworkRequest.VollyClass;
import com.example.dhiman.muse.R;
import com.example.dhiman.muse.app.ApplicationVariable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment implements NetworkResponseListener {

    private ArrayList<ModelSong> songList = new ArrayList<ModelSong>();
    public Dashboard() {
        // Required empty public constructor
    }
    private RecyclerView recyclerViewSongList;
    private VollyClass myRequest;
    private static final String DASHBOARD_API = "dashboard";
    private TextView noSongTextView;
    public onPlaySongListener mCallback;
    private Activity myActivityPointer;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            myActivityPointer=(Activity) context;
        }
        try {
            mCallback = (onPlaySongListener) myActivityPointer;
        } catch (ClassCastException e) {
            throw new ClassCastException(myActivityPointer.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerViewSongList = view.findViewById(R.id.recyclerViewSongList);
        recyclerViewSongList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        noSongTextView = view.findViewById(R.id.no_song_textView);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getSongList();
            }
        });
        return view;
    }

    private void getSongList() {
        myRequest = new VollyClass(this,getContext(),getContext().getApplicationContext());
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("authorization", ApplicationVariable.accoundData.token);
        myRequest.makeRequestGet(getContext().getResources().getString(R.string.muse_base_url)+DASHBOARD_API,params);
    }

    @Override
    public void onResponseReceived(String result) {
        try{
            JSONObject jsonResponse = new JSONObject(result);
            if(jsonResponse.optBoolean("success")){
                JSONArray list = jsonResponse.optJSONArray("songList");
                if(list!=null){
                    updateRecycleViewWithSongs(list);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void updateRecycleViewWithSongs(JSONArray list) {
        if(list.length() <1){
            recyclerViewSongList.setVisibility(View.GONE);
            noSongTextView.setVisibility(View.VISIBLE);
        }
        else{
            try {
                for (int i = 0; i < list.length(); i++) {
                    ModelSong song = new ModelSong();
                    song.musicId = list.getJSONObject(i).optString("_id");
                    song.musicTitle = list.getJSONObject(i).optString("title");
                    song.musicArtist = list.getJSONObject(i).optString("artist");
                    song.musicAlbum = list.getJSONObject(i).optString("album");
                    song.musicDuration = list.getJSONObject(i).optString("duration");//its in secconds
                    song.musicDescription = list.getJSONObject(i).optString("description");
                    song.musicYear = list.getJSONObject(i).optString("year");
                    songList.add(song);
                    Log.d("DEBUG","Player: song Added : "+ song.musicId+" " +song.musicTitle+" " + song.musicAlbum);
                }
                RecyclerViewAdapterSongList adapter = new RecyclerViewAdapterSongList(getContext(),songList,this);
                recyclerViewSongList.setAdapter(adapter);
            }catch (Exception e){
                noSongTextView.setText("Some Error Occured");
                recyclerViewSongList.setVisibility(View.GONE);
                noSongTextView.setVisibility(View.VISIBLE);
            }

        }
    }
    public interface onPlaySongListener{
        void playSong(ArrayList<ModelSong> songList,int position);
    }
}
