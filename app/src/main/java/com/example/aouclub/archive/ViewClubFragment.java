package com.example.aouclub.archive;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aouclub.R;


public class ViewClubFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_view_club, container, false);
        //-----------------toolbar----
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.main_toolbar);
        TextView toolbarText = v.findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText("Club Information");

        return v;
    }
}
