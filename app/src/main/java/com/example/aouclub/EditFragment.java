package com.example.aouclub;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class EditFragment extends Fragment  implements  PopupMenu.OnMenuItemClickListener{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit, container, false);
        //-----------------toolbar----
        Toolbar toolbar = (Toolbar)root.findViewById(R.id.main_toolbar);
        TextView toolbarText =root.findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText("Profile");
        ImageButton image = root.findViewById(R.id.button);
        image.setImageResource(R.drawable.menu);
        image.setVisibility(View.VISIBLE);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
        editData(root);
        return root;
    }
    private void editData(View v){

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
            case R.id.contact:
                selectedFragment = new ContactFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            case R.id.log_out:
                startActivity(new Intent(getContext(),MainActivity.class));
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
