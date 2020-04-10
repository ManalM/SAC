package com.example.aouclub;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aouclub.sarver.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ActivityFragment extends Fragment {

   static RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity, container, false);
        //-----------------toolbar----
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.main_toolbar);
        TextView toolbarText = v.findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText("Activities");
        //--------------------------------

        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        GetData getData = new GetData(getContext(), Config.GetActivityURL);
        getData.execute();
        return v;
    }

    private class GetData extends AsyncTask<Void, Void, Void> {
        ArrayList<String> activity, startDate,endDate, details, place, brunch, time,imageString;
        Bitmap[] bitmaps;
        String jsonUrl;
        Context context;
        Adapter adapter;

        private GetData(Context c, String url) {
            context = c;
            jsonUrl = url;

            //---------
            activity = new ArrayList<>();
            details = new ArrayList<>();
            place = new ArrayList<>();
            startDate = new ArrayList<>();
            endDate= new ArrayList<>();
            brunch = new ArrayList<>();
            time = new ArrayList<>();
            imageString= new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            InputStream inputStream = null;
            String line = null;
            String result =null;
            try {
                URL url = new URL(jsonUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("username", pref.getString("id", ""));
                String query = builder.build().getEncodedQuery();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                bufferedWriter.write(query);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                if (stringBuilder != null){
                    while((line =bufferedReader.readLine() )!= null){
                        stringBuilder.append(line + "\n");

                    }
                }else{
                    result =null;
                }
               result = stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject =null;

                for (int i=0 ;i<jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    details.add(jsonObject.getString("activity_details"));
                    activity.add(jsonObject.getString("activity_title"));
                    time.add(jsonObject.getString("activity_time"));
                    startDate.add(jsonObject.getString("activity_start"));
                    endDate.add(jsonObject.getString("activity_end"));
                    place.add(jsonObject.getString("activity_place"));
                    brunch.add(jsonObject.getString("activity_brunch"));
                    imageString.add(jsonObject.getString("activity_img"));
                    bitmaps = convert(imageString);

                }
            } catch (JSONException  | IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            adapter = new Adapter(context,activity,startDate,endDate,time,details,place,brunch, bitmaps);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Bundle bundle =new Bundle();
                    bundle.putString("detail",details.get(position));
                    bundle.putString("name",activity.get(position));
                    //bundle.putString("bitmap",imageString.get(position));

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmaps[position].compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    bundle.putByteArray("image",byteArray);
                    ActivityDetailFragment activityDetailFragment =new ActivityDetailFragment();
                    activityDetailFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            activityDetailFragment).commit();
                }
            });
        }
    }

    private  Bitmap[] convert(ArrayList<String> strings) throws IOException {
        Bitmap[] imageURL = new Bitmap[strings.size()];
        URL url;
        for (int i =0 ;i<strings.size();i++) {

             url = new URL(strings.get(i));

            imageURL[i] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        return imageURL;
        }
}
