package com.example.aouclub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText name, id, major, brunch, mobile, email, pass, confirmPass;
    private int PICK_IMAGE_REQUEST = 1;


    private ImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;
    ImageView image;
    SharedPreferences pref;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.name_up);
        id = findViewById(R.id.id_up);
        email = findViewById(R.id.email_up);
        major = findViewById(R.id.specialization_up);
        brunch = findViewById(R.id.brunch_up);
        mobile = findViewById(R.id.phone_up);
        pass =  findViewById(R.id.password_up);
        confirmPass =findViewById(R.id.confirm_password_up);
        pref = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
        editor = PreferenceManager.getDefaultSharedPreferences(SignUp.this).edit();

    }

    public void signUp(View view) {
        register();
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void register(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.logInURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(!obj.getBoolean("error")){
                        if (isEmailValid(email.getText().toString())){
                            if (pass.getText().toString().equals(confirmPass.getText().toString())){
                                editor.putString("name", name.getText().toString());
                                editor.putString("id",id.getText().toString());
                                editor.putString("email",email.getText().toString());
                                editor.putString("major",major.getText().toString());
                                editor.putString("mobile",mobile.getText().toString());
                                editor.putString("brunch",brunch.getText().toString());
                                //todo:image
                             //   editor.putString("image",obj.getString("image"));
                                editor.apply();
                                startActivity(new Intent(SignUp.this, HomeActivity.class));
                                finish();

                            }else{
                                Toast.makeText(SignUp.this, "password doesn't match", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(SignUp.this, "you have to write an appropriate email ", Toast.LENGTH_SHORT).show();
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
                }

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
                parms.put("name", name.getText().toString().trim());
                parms.put("password", pass.getText().toString().trim());
                parms.put("sid",id.getText().toString().trim().trim());
                parms.put("major",major.getText().toString().trim());
                parms.put("mobile",mobile.getText().toString().trim());
                parms.put("brunch",brunch.getText().toString().trim());
                parms.put("email",email.getText().toString().trim());
                parms.put("image",getStringImage(bitmap));
                //todo:test image and change in php the png
                return parms;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                userImage.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }
*/
}
