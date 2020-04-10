package com.example.aouclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
    ArrayList<String> activity, startDate, endDate, details, place, brunch, time;
    Bitmap[] imageURL;
    Context context;


    private Adapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(Adapter.OnItemClickListener listener) {
        mListener = listener;
    }

    protected Adapter(Context context, ArrayList<String> a, ArrayList<String> date, ArrayList<String> end, ArrayList<String> t, ArrayList<String> d, ArrayList<String> p, ArrayList<String> b, Bitmap[] url) {

        this.context = context;
        activity = a;
        this.startDate = date;
        endDate = end;
        details = d;
        place = p;
        brunch = b;
        time = t;
        imageURL = url;
    }

    @NonNull
    @Override
    public Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);

        View view = mInflater.inflate(R.layout.activity_item, parent, false);
        return new Adapter.viewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.viewHolder holder, int position) {
        holder.time.setText(time.get(position));
        holder.date.setText(startDate.get(position) + " till " + endDate.get(position));
        holder.brunch.setText(brunch.get(position));
        holder.activityPlace.setText(place.get(position));
        holder.activity.setText(activity.get(position));
        //holder.detail.setText(details.get(position));
      //  Glide.with(context).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.logo)).load(imageURL[position]).into(holder.image);
        holder.image.setImageBitmap(imageURL[position]);
    }

    @Override
    public int getItemCount() {
        return activity.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView activityPlace, brunch, activity, date, time, detail;
        ImageView image;

        public viewHolder(@NonNull View itemView, final Adapter.OnItemClickListener listener) {
            super(itemView);
            activityPlace = itemView.findViewById(R.id.place);
            brunch = itemView.findViewById(R.id.brunch_text);
            activity = itemView.findViewById(R.id.activity);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            //detail = itemView.findViewById(R.id.detail);
            image = itemView.findViewById(R.id.ac_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {

                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}

