package com.example.aouclub;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ActivityFragment extends Fragment {

    RecyclerView recyclerView;
    Adapter adapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_activity, container, false);
        //-----------------toolbar----
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.main_toolbar);
        TextView toolbarText = v.findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText("Activities");
        //--------------------------------

        recyclerView = v.findViewById(R.id.recyclerview);
       // adapter= new Adapter()
       return v;
    }
}
