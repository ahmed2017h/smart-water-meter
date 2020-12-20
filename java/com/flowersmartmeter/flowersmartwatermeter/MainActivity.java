package com.flowersmartmeter.flowersmartwatermeter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

//import com.flowersmartmeter.flowersmartwatermeter.Helper.LocalHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    private TextView username;
    ImageView user_pic;
    static Bitmap bitmap;
    private SessionHandler session;
    String URlIMAGE;
    Toolbar toolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.dash_board);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flMain,
                            new DashBoard()).commit();
                    return true;
                case R.id.add_meter:
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.add_meter);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flMain,new AddMeter()).commit();
                    return true;
                case R.id.buy_credit_page:
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.buy_credit_page);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flMain,
                            new BuyCredit()).commit();
                    return true;
                case R.id.profile:
                    getSupportActionBar().hide();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flMain,
                            new Profile()).commit();
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       initv();
        session = new SessionHandler(this);
        if(session.getUserDetails()!=null) {
            URlIMAGE = getIntent().getStringExtra("log_url");
            String usr = getIntent().getStringExtra("user_name");
            username.setText(usr);
        }
        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        new getImageFromUrl(user_pic).execute(URlIMAGE);
        getSupportActionBar().setTitle(R.string.dash_board);
        getSupportFragmentManager().beginTransaction().replace(R.id.flMain,
                new DashBoard()).commit();
        //username.setText(user.username);
        //Bitmap bitmap = BitmapFactory.decodeFile("http://108.166.181.6/assets/image/30201911pm195cffabee5bb10Screenshot_20190610-010317_Aman.jpg");

    }

private void initv(){
    user_pic = findViewById(R.id.user_pic);
    username = findViewById(R.id.username_title);
    toolbar = findViewById(R.id.customtoolbar);
}

public static class getImageFromUrl extends AsyncTask<String,Void,Bitmap>
{
    ImageView imgV;
    public getImageFromUrl(ImageView imgV)
    {
        this.imgV = imgV;
    }
    @Override
    protected Bitmap doInBackground(String... url) {
        String imgfromURL = url[0];
        bitmap =null;
        try {
            InputStream srt = new java.net.URL(imgfromURL).openStream();
            bitmap = BitmapFactory.decodeStream(srt);

           // imgV.setImageDrawable(roundedBitmapDrawable);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        //RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        //roundedBitmapDrawable.setCircular(true);
        //imgV.setImageDrawable(roundedBitmapDrawable);
       imgV.setImageBitmap(bitmap);
    }
}
}
