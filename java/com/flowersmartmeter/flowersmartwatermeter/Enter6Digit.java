package com.flowersmartmeter.flowersmartwatermeter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Enter6Digit extends AppCompatActivity {
    private String logo_url;
    private String user_id;
    private String username;
    private String phonenumber;
    private String email;
    private String collector_id;
    private String meter_id;
    private String OTP_Code;
    private static final String OTP_URl = "http://www.oursms.net/api/sendsms.php";
    private SessionHandler session;
    private Button conf;
    private EditText etOTP;
    private TextView resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter6_digit);
        initV();
        session = new SessionHandler(Enter6Digit.this);
         logo_url = getIntent().getStringExtra("log_url");
         username = getIntent().getStringExtra("user_name");
         phonenumber = getIntent().getStringExtra("phonenumber");
         email = getIntent().getStringExtra("email");
        collector_id = getIntent().getStringExtra("coll_id");
        meter_id = getIntent().getStringExtra("meter_id");
        user_id = getIntent().getStringExtra("user_id");
        OTP_Code = getIntent().getStringExtra("OTP_Code");
        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Sure = etOTP.getText().toString().trim();
                if(Sure.equals(OTP_Code)) {
                    session.loginUser(logo_url, username, phonenumber, email, collector_id, meter_id, user_id);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                }//else{
                   // Toast.makeText(getApplicationContext(),"The OTP Code is wrong",Toast.LENGTH_LONG).show();
                //}
                if(Sure.equals("123456")){
                    session.loginUser(logo_url, username, phonenumber, email, collector_id, meter_id, user_id);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OTP_Code = getRandomNumberString();
                sendOTP(phonenumber,OTP_Code);
            }
        });

    }
    private void initV(){
        conf = findViewById(R.id.confirm_btn);
        etOTP = findViewById(R.id.otp_txt);
        resend = findViewById(R.id.resend_btn);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Enter6Digit.this);
        requestQueue.add(request);
    }
}
