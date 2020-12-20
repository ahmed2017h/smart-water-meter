package com.flowersmartmeter.flowersmartwatermeter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class SignUpActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_LOGOURL = "logo_url";
    private static final String KEY_USERID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PHONENUMBER = "phoneNumber";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_COLLECTORID = "collector_id";
    private static final String KEY_METERID = "meter_id";
    private static final String KEY_EMPTY = "";
    private static final int RESULTE_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 52;

    private String logo_url;
    private String user_id;
    private String username;
    private String phonenumber;
    private String email;
    private String collector_id;
    private String meter_id;
    private ProgressDialog pDialog;
    private String register_url = "http://13.235.31.177/backend/signup.php";
    private String UploadToServeruri = "http://13.235.31.177/backend/ImageUpload.php";
    private String Addmeteruri = "http://13.235.31.177/backend/addMeter.php";
    private SessionHandler session;
    private Context mContext;
    JSONObject jsonObject;
    Bitmap bitmap;
    EditText etfullname,etphonenum,etemailaddress,etcoll_id,etmeter_id;
    Button submit;
    TextView signin;
    ImageView usrimg;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULTE_LOAD_IMAGE && resultCode==RESULT_OK && data!=null)
        {
            Uri SelectImage = data.getData();
            usrimg.setImageURI(SelectImage);
            try{
                InputStream inputStream = getContentResolver().openInputStream(SelectImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
                usrimg.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else if (requestCode == CAMERA_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri SelectImage = data.getData();
            usrimg.setImageURI(SelectImage);
            try{
                InputStream inputStream = getContentResolver().openInputStream(SelectImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
                usrimg.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SetupUIVeiw();
        mContext = getApplicationContext();
        session = new SessionHandler(getApplicationContext());
       // phonenum.setText(getRandomNumberString());
        //etcoll_id.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(8);
        etcoll_id.setFilters(filterArray);
        etcoll_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString();
                if(!s.equals(s.toUpperCase()))
                {
                    s=s.toUpperCase();
                    etcoll_id.setText(s);
                }
                etcoll_id.setSelection(etcoll_id.getText().length());

            }
        });
        usrimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               // startActivityForResult(intent,RESULTE_LOAD_IMAGE);
                showPictureDialog();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Retrieve the data entered in the edit texts

                    //logo_url = UploadImage(bitmap);
                logo_url = String.valueOf(Calendar.getInstance().getTimeInMillis());
                username = etfullname.getText().toString().trim();
                phonenumber = etphonenum.getText().toString().trim();
                user_id = username+phonenumber;
                email = etemailaddress.getText().toString().trim();
                collector_id = etcoll_id.getText().toString().trim();
                meter_id = etmeter_id.getText().toString().trim();

                //if (validateInputs()) {


                    UploadImage(bitmap);

                //}
            }
        });
    }

    private void SetupUIVeiw()
    {
        etfullname = findViewById(R.id.full_name);
        etphonenum = findViewById(R.id.phone_num);
        etemailaddress = findViewById(R.id.email);
        etcoll_id = findViewById(R.id.coll_id);
        etmeter_id = findViewById(R.id.meter_id);
        submit = findViewById(R.id.submit);
        signin = findViewById(R.id.signin_bk);
        usrimg = findViewById(R.id.usr_img);
    }
    private void displayLoader() {
        pDialog = new ProgressDialog(SignUpActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void loadDashboard() {
        Intent i = new Intent(this, Login_Manger.class);
        startActivity(i);
        finish();

    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                 Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                 startActivityForResult(intent,RESULTE_LOAD_IMAGE);
                                break;
                            case 1:
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    private void UploadImage(final Bitmap bitmap){
        displayLoader();
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name",logo_url);
            jsonObject.put("image",imageToString(bitmap));
            jsonObject.put(KEY_PHONENUMBER,phonenumber);
            jsonObject.put(KEY_METERID,meter_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UploadToServeruri,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    logo_url= response.getString(KEY_MESSAGE);
                if(response.getInt("success")== 1){
                   registerUser();


                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        requestQueue.add(jsonObjectRequest);

    }
    private void registerUser() throws JSONException {
        displayLoader();
        JSONObject jo = new  JSONObject();
        jo.put(KEY_LOGOURL,logo_url);
        jo.put(KEY_USERNAME,username);
        jo.put(KEY_PHONENUMBER,phonenumber);
        jo.put(KEY_USERID,user_id);
        jo.put(KEY_EMAIL,email);
        jo.put(KEY_COLLECTORID,collector_id);
        jo.put(KEY_METERID,meter_id);
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, register_url, jo, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                      pDialog.dismiss();
                        try {

                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                loadDashboard();
                                setAddmeter();
                               Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }else if(response.getInt(KEY_STATUS) == 1){
                                //Display error message if username is already existsing
                                etphonenum.setError("Phone Number already taken!");
                                etphonenum.requestFocus();

                            }else if(response.getInt(KEY_STATUS)==2){
                                etmeter_id.setError("Meter ID is already exists!");
                                etmeter_id.requestFocus();

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                       //pDialog.dismiss();
                        Log.i("err2",error.getMessage());
                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
       // MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
        requestQueue.add(jsArrayRequest);
    }
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(username)) {
            etfullname.setError("Full Name cannot be empty");
            etfullname.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(phonenumber)) {
            etphonenum.setError("Phone Number cannot be empty");
            etphonenum.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(email)) {
            etemailaddress.setError("Email address cannot be empty");
            etemailaddress.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(collector_id)) {
            etcoll_id.setError("Collector ID cannot be empty");
            etcoll_id.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(meter_id)) {
            etmeter_id.setError("Password and Confirm Password does not match");
            etmeter_id.requestFocus();
            return false;
        }

        return true;
    }
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }
    private void setAddmeter() throws JSONException {
        JSONObject jos = new JSONObject();
        jos.put(KEY_USERID,user_id);
        jos.put(KEY_COLLECTORID,collector_id);
        jos.put(KEY_METERID,meter_id);
        jos.put("flag","addMeter");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Addmeteruri, jos, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        requestQueue.add(request);
    }
}
