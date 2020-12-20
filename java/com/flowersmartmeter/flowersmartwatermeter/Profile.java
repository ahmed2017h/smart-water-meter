package com.flowersmartmeter.flowersmartwatermeter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

//import com.flowersmartmeter.flowersmartwatermeter.Helper.LocalHelper;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;

import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class Profile extends Fragment {
    ImageView user_img;
    View v;
    Context mContext;
    Bitmap bitmap;
    private SessionHandler session;
    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    ImageButton getlang_en,getLang_ar;
    Button logout;
    TextView usernam,phonenumber,email,coll_id,meter_id;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI();
        String URlIMAGE = getActivity().getIntent().getStringExtra("log_url");
        String username = getActivity().getIntent().getStringExtra("user_name");
        String phonenum = getActivity().getIntent().getStringExtra("phonenumber");
        String mail = getActivity().getIntent().getStringExtra("email");
        String collid = getActivity().getIntent().getStringExtra("coll_id");
        String meterId = getActivity().getIntent().getStringExtra("meter_id");
        mContext = getContext();
        session = new SessionHandler(getContext());
        usernam.setText(username);
        phonenumber.setText(phonenum);
        email.setText(mail);
        coll_id.setText(collid);
        meter_id.setText(meterId);
        //img_lay = (CircularRevealCardView) v.findViewById(R.id.img_layout);
        new MainActivity.getImageFromUrl(user_img).execute(URlIMAGE);
        getLang_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAppLocal("ar");
                Intent mainIntent = new Intent(getContext(), Splash_Screen.class);
                startActivity(mainIntent);
                getActivity().finish();
            }
        });
        getlang_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAppLocal("en");
                Intent mainIntent = new Intent(getContext(), Splash_Screen.class);
                startActivity(mainIntent);
                getActivity().finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
                Intent intent = new Intent(getContext(),Login_Manger.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return v;
    }
    private void setAppLocal(String lang){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(lang.toLowerCase()));
            config.setLayoutDirection(new Locale(lang));
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }else {
            config.locale = new Locale(lang.toLowerCase());
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
        resources.updateConfiguration(config,dm);
    }
    private void initUI()
    {
        user_img = v.findViewById(R.id.user_img);
        getLang_ar = v.findViewById(R.id.lang_ar);
        getlang_en = v.findViewById(R.id.lang_eng);
        logout = v.findViewById(R.id.logout);
        usernam =v.findViewById(R.id.pf_username);
        phonenumber = v.findViewById(R.id.pf_phonenum);
        email = v.findViewById(R.id.pf_email);
        coll_id = v.findViewById(R.id.pf_collid);
        meter_id = v.findViewById(R.id.pf_meterid);

    }

}
