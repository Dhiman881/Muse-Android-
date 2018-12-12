package com.example.dhiman.muse.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dhiman.muse.Fragments.Dashboard;
import com.example.dhiman.muse.Models.ModelSong;
import com.example.dhiman.muse.R;

import java.util.ArrayList;

public class RecyclerViewAdapterSongList extends RecyclerView.Adapter {

    private Context activityContext;
    private ArrayList<ModelSong> songList;
    private Dashboard dashboard = null;
    public RecyclerViewAdapterSongList(Context activityContext, ArrayList<ModelSong> songList,Dashboard d){
        this.activityContext = activityContext;
        this.songList = songList;
        dashboard = d;
    }
    public static class ViewHolderForSong extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView text_view_song_name;
        TextView text_view_song_artist;
        View v;
        public ViewHolderForSong(View v) {
            super(v);
            this.v = v;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_list_item, parent, false);
        ViewHolderForSong holder = new ViewHolderForSong(itemView);
        holder.text_view_song_name = itemView.findViewById(R.id.media_list_song_name);
        holder.text_view_song_artist = itemView.findViewById(R.id.media_list_song_artist);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolderForSong){
            final ViewHolderForSong  viewHolderForSong = (ViewHolderForSong) holder;
            viewHolderForSong.text_view_song_name.setText(((ModelSong)songList.get(position)).musicTitle);
            viewHolderForSong.text_view_song_artist.setText(((ModelSong)songList.get(position)).musicArtist);
            viewHolderForSong.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dashboard!=null)
                        dashboard.mCallback.playSong(songList,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
}
