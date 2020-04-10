package com.example.aouclub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aouclub.sarver.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    private EditText username,password ;
    SharedPreferences pref;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        username = findViewById(R.id.username);
        password = findViewById(R.id.pass);
        pref = PreferenceManager.getDefaultSharedPreferences(SignIn.this);
        editor = PreferenceManager.getDefaultSharedPreferences(SignIn.this).edit();
    }
    public void signIn(View view) {
        login();
    }

    private void login() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.logInURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(!obj.getBoolean("error")){
                        editor.putString("name", obj.getString("name"));
                        editor.putString("sid",obj.getString("sid"));
                        editor.putString("state",obj.getString("state"));
                        editor.putString("email",obj.getString("email"));
                        editor.putString("major",obj.getString("major"));
                        editor.putString("mobile",obj.getString("mobile"));
                        editor.putString("brunch",obj.getString("brunch"));
                        editor.putString("id",obj.getString("id"));

                        editor.putString("image",obj.getString("image"));
                        editor.apply();

                        if (pref.getString("state","").equals("Active")){
                            startActivity(new Intent(SignIn.this,HomeActivity.class));
                            finish();
                        }else{
                            Toast.makeText(SignIn.this, "You are not an active member", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(
                                getApplicationContext(),
                                obj.getString("message"),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } /*catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("username", username.getText().toString().trim());
                parms.put("password", password.getText().toString().trim());
                return parms;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
