package com.example.aouclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class SignUp extends AppCompatActivity {

    EditText name, id, major, brunch, mobile, email, pass, confirmPass;
    private int PICK_IMAGE_REQUEST = 1;


    private CircleImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

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
        pass = findViewById(R.id.password_up);
        confirmPass = findViewById(R.id.confirm_password_up);
        imageView = findViewById(R.id.sign_up_image);

        pref = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
        editor = PreferenceManager.getDefaultSharedPreferences(SignUp.this).edit();

        //-----click image to upload----------------
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(SignUp.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);

                    } else {
                        pickImage();
                    }

                } else {
                    pickImage();
                }
            }
        });
    }


    ///------------image functions-----------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST) {
            int value = grantResults[0];
            if (value == PERMISSION_DENIED) {
                Toast.makeText(this, " Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            } else if (value == PERMISSION_GRANTED)
                pickImage();
        }
    }

    private void pickImage() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);


    }

    public String getStringImage(Bitmap bmp) {
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
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //----------------------sign up function--------------------
    public void signUp(View view) {

        if (!email.getText().toString().equals("") && !id.getText().toString().equals("") && !name.getText().toString().equals("")
                && !major.getText().toString().equals("") && !mobile.getText().toString().equals("")
                && !brunch.getText().toString().equals("") && !pass.getText().toString().equals("")
                && !confirmPass.getText().toString().equals("")
        ) {
            if (isEmailValid(email.getText().toString())) {
                if (pass.getText().toString().equals(confirmPass.getText().toString())) {

                    if(bitmap!= null) {
                        register();
                    }else{
                        Toast.makeText(this, "You have to upload an image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUp.this, "password doesn't match", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(SignUp.this, "you have to write an appropriate email ", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(SignUp.this, "Required fields are missing", Toast.LENGTH_SHORT).show();

        }
    }

    //-----check email -------
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void register() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.RegisterURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (!obj.getBoolean("error")) {

                        Toast.makeText(SignUp.this, obj.getString("message"), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(SignUp.this, obj.getString("message"), Toast.LENGTH_LONG).show();

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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("name", name.getText().toString().trim());
                parms.put("password", pass.getText().toString().trim());
                parms.put("sid", id.getText().toString().trim().trim());
                parms.put("major", major.getText().toString().trim());
                parms.put("mobile", mobile.getText().toString().trim());
                parms.put("brunch", brunch.getText().toString().trim());
                parms.put("email", email.getText().toString().trim());
                parms.put("image", getStringImage(bitmap));
                return parms;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

}
