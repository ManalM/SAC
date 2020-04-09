package com.example.aouclub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
    ArrayList<String> activity , date,details,place,brunch,time,imageURL;
    Context context;
//todo: image
    protected  Adapter(Context context , ArrayList<String> a,ArrayList<String> date,ArrayList<String> t,ArrayList<String>  d,ArrayList<String> p,ArrayList<String>b,ArrayList<String> url){

        this.context = context;
        activity =a ;
        this.date = date;
        details =d;
        place=p;
        brunch =b;
        time=t;
        imageURL = url;
    }
    @NonNull
    @Override
    public Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);

        View view = mInflater.inflate(R.layout.activity_item, parent, false);
        return new Adapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.viewHolder holder, int position) {
        holder.time.setText(time.get(position));
        holder.date.setText(date.get(position));
        holder.brunch.setText(brunch.get(position));
        holder.activityPlace.setText(place.get(position));
        holder.activity.setText(activity.get(position));
        Glide.with(context).load(imageURL).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return activity.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView activityPlace,brunch,activity, date,time;
        ImageView image;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            activityPlace = itemView.findViewById(R.id.place);
            brunch  =  itemView.findViewById(R.id.brunch_text);
            activity =  itemView.findViewById(R.id.activity);
            date =  itemView.findViewById(R.id.date);
            time =  itemView.findViewById(R.id.time);
            image = itemView.findViewById(R.id.ac_img);
        }
    }
}
