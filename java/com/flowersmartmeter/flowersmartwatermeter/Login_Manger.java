package com.flowersmartmeter.flowersmartwatermeter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Login_Manger extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_PHONENUMBER = "phoneNumber";
    private static final String KEY_FLAG = "flag";
    private static final String Login_Url="http://13.235.31.177/backend/login.php";
    private static final String OTP_URl = "http://www.oursms.net/api/sendsms.php";
    TextView signup;
    EditText etphone_num;
    Button send;
    private String logo_url;
    private String user_id;
    private String username;
    private String phonenumber;
    private String email;
    private String collector_id;
    private String meter_id;
    private String OTP_Code;
    private SessionHandler session;
    private ProgressDialog pDialog;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__manger);
        setupUIView();
        session = new SessionHandler(Login_Manger.this);
        if (session.getUserDetails()!=null)
        {
            logo_url= session.getUserDetails().getLogo_url();
            username = session.getUserDetails().getUsername();
            phonenumber = session.getUserDetails().getPhonenumber();
            email = session.getUserDetails().getEmail();
            collector_id = session.getUserDetails().getCollector_id();
            meter_id = session.getUserDetails().getMeter_id();
            user_id = session.getUserDetails().getUser_id();
            Intent intent = new Intent(Login_Manger.this,MainActivity.class);
            intent.putExtra("log_url",logo_url);
            intent.putExtra("user_name",username);
            intent.putExtra("phonenumber",phonenumber);
            intent.putExtra("email",email);
            intent.putExtra("coll_id",collector_id);
            intent.putExtra("meter_id",meter_id);
            intent.putExtra("user_id",user_id);
            startActivity(intent);
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //flag = "login";
           phonenumber = etphone_num.getText().toString().trim();
           Login();
           displayLoader();
            }
        });
    }
    public void setupUIView()
    {
        signup = findViewById(R.id.signup_btn);
        etphone_num = findViewById(R.id.login_phone_num);
        send = findViewById(R.id.send_btn);
    }
    private void displayLoader() {
        pDialog = new ProgressDialog(Login_Manger.this);
        pDialog.setMessage("Login.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    private void Login(){
        JSONObject jsn = new JSONObject();
        try {
            jsn.put(KEY_PHONENUMBER,phonenumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Login_Url, jsn, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    JSONObject jsonObject;
                    //signup.setText(response.getString(KEY_STATUS));
                    if(response.getString(KEY_STATUS).equals("true")){

                         jsonObject =response.getJSONObject("userData");

                         logo_url=jsonObject.getString("logo_url");
                         phonenumber = jsonObject.getString("phoneNumber");
                         username = jsonObject.getString("username");
                         email = jsonObject.getString("email");
                         collector_id = jsonObject.getString("collector_id");
                         meter_id = jsonObject.getString("meter_id");
                         user_id = jsonObject.getString("user_id");
                         OTP_Code = getRandomNumberString();
                         sendOTP(phonenumber,OTP_Code);
                       //session.loginUser(logo_url,username,phonenumber,email,collector_id,meter_id,user_id);
                        //signup.setText(email);
                        Intent intent = new Intent(getApplicationContext(),Enter6Digit.class);
                        intent.putExtra("log_url",logo_url);
                        intent.putExtra("user_id",user_id);
                        intent.putExtra("user_name",username);
                        intent.putExtra("phonenumber",phonenumber);
                        intent.putExtra("email",email);
                        intent.putExtra("coll_id",collector_id);
                        intent.putExtra("meter_id",meter_id);
                        intent.putExtra("OTP_Code",OTP_Code);
                        startActivity(intent);
                        finish();
                    }else if(response.getString(KEY_STATUS).equals("false")){
                        AlertDialog.Builder errDialog = new AlertDialog.Builder(Login_Manger.this);
                        errDialog.setTitle("Error Message");
                        errDialog.setMessage("This user is unregistered");
                        errDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                etphone_num.setText("");
                            }
                        });
                        errDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(Login_Manger.this);
        requestQueue.add(request);
    }
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    private void sendOTP(final String phonenum, final String sexdig){
        StringRequest request = new StringRequest(Request.Method.POST, OTP_URl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", "AmanPoints");
                params.put("password", "BestTe@m1");
                params.put("message","The OTP Cod: "+sexdig);
                params.put("numbers",phonenum);
                params.put("sender","AmanPoin-AD");
                params.put("unicode","e");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login_Manger.this);
        requestQueue.add(request);
    }
}
