package com.example.aouclub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.IOException;


import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        //-----------------toolbar----
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.main_toolbar);
        TextView toolbarText = root.findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText("Profile");
        ImageButton imageButton = root.findViewById(R.id.button);
        imageButton.setImageResource(R.drawable.menu);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
        ///--------------------initial------------------------------
        TextView name, id, major, brunch, mobile,email,number;
        CircleImageView image;
        name = root.findViewById(R.id.name);
        id = root.findViewById(R.id.id);
        email = root.findViewById(R.id.email);
        major = root.findViewById(R.id.specialization);
        brunch = root.findViewById(R.id.brunch);
        mobile = root.findViewById(R.id.phone);
        number = root.findViewById(R.id.number);
        image = root.findViewById(R.id.image);
        //-----------------------change textView---------------------------
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        name.setText(pref.getString("name", ""));
        id.setText(pref.getString("sid", ""));
        email.setText(pref.getString("email",""));
        major.setText(pref.getString("major", ""));
        mobile.setText(pref.getString("mobile", ""));
        brunch.setText(pref.getString("brunch", ""));
        number.setText(String.valueOf(pref.getInt("numberOfActivity",0)));
        String string =  pref.getString("image","");

        Bitmap bitmap = null;
        try {
            bitmap = convert(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(bitmap);


        return root;
    }
    private Bitmap convert(String strings) throws IOException {

        byte [] encodeByte = Base64.decode(strings,Base64.DEFAULT);
        Bitmap imageURL = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        return imageURL;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Fragment selectedFragment = null;

        switch (menuItem.getItemId()) {

            case R.id.about:
                selectedFragment = new AboutFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
/*            case R.id.contact:
                selectedFragment = new ContactFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;*/
            case R.id.log_out:
                startActivity(new Intent(getContext(), MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu1, popup.getMenu());
        popup.setOnMenuItemClickListener(this);

        popup.show();
    }
}
