package com.example.dhiman.muse.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dhiman.muse.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMusic extends Fragment {


    public MyMusic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_music, container, false);
    }

}
