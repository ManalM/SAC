package com.example.aouclub;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class ActivityDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_detail, container, false);

        ImageView imageView = v.findViewById(R.id.ac_img1);
        TextView name = v.findViewById(R.id.activity_name);
        TextView detail = v.findViewById(R.id.detail);

        Bundle bundle = getArguments();
        name.setText(bundle.getString("name"));
        detail.setText(bundle.getString("detail"));


        byte[] byteArray = bundle.getByteArray("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Glide.with(getContext()).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.logo)).load(bitmap).into(imageView);
        return v;
    }
}
