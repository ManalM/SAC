package com.example.aouclub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
        TextView name, id, major, brunch, mobile,email;
        CircleImageView image;
        name = root.findViewById(R.id.name);
        id = root.findViewById(R.id.id);
        email = root.findViewById(R.id.email);
        major = root.findViewById(R.id.specialization);
        brunch = root.findViewById(R.id.brunch);
        mobile = root.findViewById(R.id.phone);
        image = root.findViewById(R.id.image);
        //-----------------------change textView---------------------------
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        name.setText(pref.getString("name", ""));
        id.setText(pref.getString("sid", ""));
        email.setText(pref.getString("email",""));
        major.setText(pref.getString("major", ""));
        mobile.setText(pref.getString("mobile", ""));
        brunch.setText(pref.getString("brunch", ""));

        String string = pref.getString("image","");
        URL  url = null;
        try {
            url = new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        try {
            if (url != null) {
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }else{
                Toast.makeText(getContext(), "null url", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(getContext()).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.woman)).load(bitmap).into(image);
        return root;
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
