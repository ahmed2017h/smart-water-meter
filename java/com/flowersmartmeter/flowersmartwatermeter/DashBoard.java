package com.flowersmartmeter.flowersmartwatermeter;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.multidex.MultiDex;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifTextView;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("HandlerLeak")
public class DashBoard extends Fragment {
    View v;
    private ImageView user_pic;
    static Bitmap bitmap;
    private Toolbar toolbar;
    private TextView username,curreading,balance;
    private BarChart chart;
    private GifImageView Cvalve,wait_rd;
    GifTextView readbtn;
    private ImageView valvestatus;
    private ImageView magnetic;
    private ImageView batterystatus;
    private ProgressDialog pDialog;
    private Spinner spcoll;
    private Spinner spmeter;
    private static final String KEY_COLLECTOR = "collectors";
    private static final String KEY_METERS = "meter";
    private static final String KEY_USERID = "user_id";
    private static final String KEY_COLLID ="collector_id";
    private static final String KEY_METERID = "meter_id";
    private static final String KEY_BALANCE ="balance_limit";
    private static final String KEY_STATUS = "result";
    private String meter_info_url = "http://13.235.31.177/backend/getCollectors.php";
    private String balance_url = "http://13.235.31.177/backend/getBalanceData.php";
    private String getyaer_url = "http://13.235.31.177/backend/getYearData.php";
    public static final String HOST = "13.235.31.177";
    public static final int PORT = 889;
    Socket s = new Socket();
    static DataInputStream reader;
    DataOutputStream dOS;
    byte[] input = new byte[1000];
    String[] status;
    String magstatus,bttrystatus,vlvstatus,readcons;
   String[] datesArray;
    String URlIMAGE;
    String user_id;
    boolean stat=true;
    BarDataSet barDataSet;
    BarData thedata;
    XAxis xAxis;
    Handler handler;
    Runnable runnable,runnable1;
    Thread th;
    Boolean isrun,isrun1,chk;
    JsonObjectRequest Meteriforequest,Balancerequest,Yearrequest;
    String hh,hh1,hh2,hh3,hh4,hh5,hh6,st1;
    int count = 0;
    private ArrayList<String> r_meter=new ArrayList<>();
    private ArrayList<String> dates =new ArrayList<>();
    private ArrayList<String>  collectore_list=new ArrayList<>();
    private ArrayList<String> meter_list=new ArrayList<>();
    ArrayAdapter<String> dataAdapter ;
    ArrayAdapter<String> dataAdapter2;
    ArrayList<BarEntry> entries = new ArrayList<BarEntry>(){
    };
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;
    HashSet<String> data_chart = new HashSet<String>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dash_board, container, false);
        // Inflate the layout for this fragment
        initv();
        mContext = getContext();
        if (mContext != null) {
            mPreferences = mContext.getSharedPreferences("lastRead",Context.MODE_PRIVATE);
        }
        mEditor = mPreferences.edit();
        MultiDex.install(getContext());
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RecMsg recMsg = new RecMsg();
        th = new Thread(recMsg);
        th.setDaemon(true);
        th.start();
        //values.add(getActivity().getIntent().getStringExtra("meter_id"));
        isrun = false;
        isrun1 = false;
        URlIMAGE = getActivity().getIntent().getStringExtra("log_url");
        user_id = getActivity().getIntent().getStringExtra("user_id");
       hh = mPreferences.getString("col_index","");
       hh1 = mPreferences.getString("meter_index","");
       hh2 = mPreferences.getString("balance","");
       hh3 = mPreferences.getString("vlvs","");
       hh4 = mPreferences.getString("btry","");
       hh5 = mPreferences.getString("mgn","");
       hh6 = mPreferences.getString("meter_reading","");
        getMeterifo();
        if(!hh.equals("")&&!hh1.equals("")&&!hh2.equals("")) {
            getYeardata(hh, hh1);
            balance.setText(hh2);
            if(hh3.equals("open")){
                Cvalve.setImageResource(R.drawable.on);
                Cvalve.setEnabled(true);
                //valvestatus.setText("Valve");
            }else {
                Cvalve.setEnabled(false);
            }if(hh3.equals("off")){
                Cvalve.setImageResource(R.drawable.off_valve);
                Cvalve.setEnabled(true);
               // valvestatus.setText("Valve");
            }else {
                Cvalve.setEnabled(false);
            }
            if(hh4.equals("good")){
                batterystatus.setImageResource(R.drawable.battery_green);
            }if(hh4.equals("bad")){
                batterystatus.setImageResource(R.drawable.battery_red);
            }if(hh5.equals("yes")){
                magnetic.setImageResource(R.drawable.magnatic_yes);
            }if(hh5.equals("no")){magnetic.setImageResource(R.drawable.magnatic_no);}
            if(!hh6.equals("")){curreading.setText(hh6);}
        }

        //Toast.makeText(getContext(),hh,Toast.LENGTH_LONG).show();

        //Cvalve.setEnabled(false);
        //valvestatus.setEnabled(false);

         Cvalve.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Cvalve.setImageResource(R.drawable.wait_spinner);
                 valvestatus.setVisibility(View.INVISIBLE);
                 Cvalve.setEnabled(false);
                 isrun = true;
                 runnable = new Runnable() {
                     @Override
                     public void run() {
                         if(stat)
                         {
                             String spcollid =spcoll.getSelectedItem().toString().trim();
                             String spmeterid=spmeter.getSelectedItem().toString().trim();
                             conn(spcollid,spmeterid,"OpenTap");
                            // valvestatus.setText("Valve");
                             Cvalve.setImageResource(R.drawable.off_valve);
                             Cvalve.setEnabled(true);
                             valvestatus.setVisibility(View.VISIBLE);

                         }else
                         {
                             String spcollid =spcoll.getSelectedItem().toString().trim();
                             String spmeterid=spmeter.getSelectedItem().toString().trim();
                             conn(spcollid,spmeterid,"CloseTap");
                            // valvestatus.setText("Valve");
                             Cvalve.setImageResource(R.drawable.on);
                             Cvalve.setEnabled(true);
                             valvestatus.setVisibility(View.VISIBLE);
                         }
                     }
                 };
                 handler = new Handler();
                 handler.postDelayed(runnable,2000);
                 handler.sendEmptyMessage(0);
             }
         });
        /* spmeter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 String spcollid = spcoll.getSelectedItem().toString().trim();
                 String spmeterid = spmeter.getSelectedItem().toString().trim();
                 conn(spcollid, spmeterid, "ReadMeater");
                 getBalance(spcollid, spmeterid);
                 getYeardata(spcollid, spmeterid);
             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });*/
        readbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //wait_rd.setImageResource(R.drawable.wait_spinner);
                readbtn.setText(getResources().getText(R.string.wait));
                //readbtn.setText("");
                Cvalve.setEnabled(false);
                readbtn.setEnabled(false);
                valvestatus.setVisibility(View.INVISIBLE);
                Cvalve.setVisibility(View.INVISIBLE);
                //readbtn.setVisibility(View.INVISIBLE);
                //wait_rd.setVisibility(View.VISIBLE);
                isrun1 = true;
                chk=true;
                mEditor.clear();
                mEditor.apply();
                runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        if (count == 0) {
                            count++;
                            chart.clear();
                            entries.clear();
                            r_meter.clear();
                            dates.clear();
                            chart.removeAllViews();
                        } else if (count > 0) {
                            count++;
                            chart.clear();
                            entries.clear();
                            r_meter.clear();
                            dates.clear();
                            chart.removeAllViews();

                        }
                        String spcollid = spcoll.getSelectedItem().toString().trim();
                        String spmeterid = spmeter.getSelectedItem().toString().trim();
                        conn(spcollid, spmeterid, "ReadMeater");
                        getBalance(spcollid, spmeterid);
                        getYeardata(spcollid, spmeterid);
                        Cvalve.setEnabled(true);
                        readbtn.setEnabled(true);
                        //readbtn.setBackground(getResources().getDrawable(R.drawable.custam_button));
                        readbtn.setText(getResources().getText(R.string.read_meter));
                        //wait_rd.setVisibility(View.INVISIBLE);
                        valvestatus.setVisibility(View.VISIBLE);
                        Cvalve.setVisibility(View.VISIBLE);
                        readbtn.setVisibility(View.VISIBLE);
                    }
                };
                handler = new Handler();
                handler.postDelayed(runnable1, 2000);
                handler.sendEmptyMessage(0);
            }
        });
        if(bitmap != null)
        new getImageFromUrl(user_pic).execute(URlIMAGE);
        return v;
    }
    private void initv(){
        Cvalve = v.findViewById(R.id.swBtn);
        valvestatus = v.findViewById(R.id.status_img);
        magnetic = v.findViewById(R.id.magnatic_state);
        batterystatus = v.findViewById(R.id.battery_state);
        readbtn = v.findViewById(R.id.read_meter_btn);
        spcoll = v.findViewById(R.id.coll_id);
        spmeter= v.findViewById(R.id.meter_id);
        user_pic = v.findViewById(R.id.user_pic);
        username = v.findViewById(R.id.username_title);
        curreading = v.findViewById(R.id.Creading);
        balance = v.findViewById(R.id.Vbalance);
        chart = v.findViewById(R.id.barchart);
        //wait_rd = v.findViewById(R.id.wait_img);
    }
    String incom,tinput;
   public void conn(String collid ,String meterid,String command){
                try {
                    incom = "MeaterNum:"+meterid+";CJQNum:"+collid+";"+command+";";
                    byte[] checkconn = new byte[1000];
                    if(!s.isOutputShutdown()) {
                        dOS = new DataOutputStream(s.getOutputStream());
                        //dOS.write("FLOWER10".getBytes());
                        dOS.write(incom.getBytes());
                        reader = new DataInputStream(s.getInputStream());
                        reader.read(input);
                        tinput = new String(input);
                        if(!tinput.equals("")) {
                            int i;
                            i = tinput.indexOf("Magnetic interference:");
                            String ser = tinput.substring(i);
                            status = ser.split(";");
                            if (!command.contains("OpenTap") && !command.contains("CloseTap")) {
                                magstatus = status[0].substring(status[0].indexOf(":") + 1);
                                bttrystatus = status[1].substring(status[1].indexOf(":") + 1);
                                readcons = status[3].substring(status[3].indexOf(":") + 1);
                                vlvstatus = status[2].substring(status[2].indexOf(":") + 1);
                            } else {
                                vlvstatus = status[2].substring(status[2].indexOf(":") + 1);
                            }
                            //line = tinput.substring(21,);
                            //s.close();

                            //}
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(),"The server is not response",Toast.LENGTH_LONG).show();
                        }
                    }else {Toast.makeText(getActivity().getApplicationContext(),"The server is not working",Toast.LENGTH_LONG).show();}
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    // response.setText("UnknownHostException:%s " + e.toString());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    // response.setText("IOException: " + e.toString());
                }
                if(tinput != null) {
                    if (!command.contains("OpenTap") && !command.contains("CloseTap")) {
                        if (magstatus.contains("no")) {
                            magnetic.setImageResource(R.drawable.magnatic_no);
                            mEditor.putString("mgn", "no");
                            mEditor.apply();
                        } else {
                            magnetic.setImageResource(R.drawable.magnatic_yes);
                            mEditor.putString("mgn", "yes");
                            mEditor.apply();
                        }
                        if (bttrystatus.contains("normal")) {
                            batterystatus.setImageResource(R.drawable.battery_green);
                            mEditor.putString("btry", "good");
                            mEditor.apply();
                        } else if (bttrystatus.contains("Undervoltage")) {
                            batterystatus.setImageResource(R.drawable.battery_red);
                            mEditor.putString("btry", "bad");
                            mEditor.apply();
                        } else {
                            batterystatus.setImageResource(R.drawable.battery_green);
                            mEditor.putString("btry", "good");
                            mEditor.apply();
                        }
                        if (vlvstatus.contains("open")) {

                           // valvestatus.setText("Valve");
                            Cvalve.setImageResource(R.drawable.on);
                            // Cvalve.setOnCheckedChangeListener(null);
                            stat = false;
                            st1 = "open";
                            mEditor.putString("vlvs", st1);
                            mEditor.apply();

                        }
                        if (vlvstatus.contains("turn off")) {
                           // valvestatus.setText("Valve");
                            Cvalve.setImageResource(R.drawable.off_valve);
                            stat = true;
                            st1 = "off";
                            mEditor.putString("vlvs", st1);
                            mEditor.apply();
                        }
                        if (!readcons.contains("Performing a valve control action")) {
                            curreading.setText(readcons);
                            mEditor.putString("meter_reading", readcons);
                            mEditor.apply();
                        }
                        //timestamp.setText(line);
                        // Toast.makeText(getContext(), connchk, Toast.LENGTH_LONG).show();
                        Log.i("PDA", "----->" + readcons);
                    } else {
                        if (vlvstatus.contains("open")) {

                            //valvestatus.setText("Valve");
                            Cvalve.setImageResource(R.drawable.on);
                            // Cvalve.setOnCheckedChangeListener(null);
                            stat = false;
                            st1 = "open";
                            mEditor.putString("vlvs", st1);
                            mEditor.apply();

                        }
                        if (vlvstatus.contains("turn off")) {
                           // valvestatus.setText("Valve");
                            Cvalve.setImageResource(R.drawable.off_valve);
                            stat = true;
                            st1 = "off";
                            mEditor.putString("vlvs", st1);
                            mEditor.apply();
                        }
                    }
                }
   }

    class RecMsg implements Runnable{

        @Override
        public void run() {
            try {

                s = new Socket(HOST, PORT, true);
                dOS = new DataOutputStream(s.getOutputStream());
                while (true) {
                    dOS.write(("FLOWER10").getBytes());
                    reader = new DataInputStream(s.getInputStream());
                    s.setReuseAddress(true);
                    reader.read(input);
                    Thread.sleep(300000);
                }


            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static class getImageFromUrl extends AsyncTask<String,Void, Bitmap>
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
    public void getMeterifo(){
       //displayLoader();
        JSONObject jsn = new JSONObject();
        try {
            jsn.put(KEY_USERID,user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Meteriforequest = new JsonObjectRequest(Request.Method.POST, meter_info_url, jsn, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jam, jac;
                    jam = response.getJSONArray(KEY_METERS);

                    for (int i = 0; i < jam.length(); i++) {

                        String meterid = jam.getString(i);
                        meter_list.add(meterid);
                    }
                    jac = response.getJSONArray(KEY_COLLECTOR);
                    for (int i = 0; i < jac.length(); i++) {
                        String[] coll = new String[jac.length()];
                        int j = 0;
                        coll[i] = jac.optString(i);
                        // String cmp = jac.getString(i+1);
                        collectore_list.add(coll[i]);

                        // Log.i("cool", coll[i]);
                    }

                        dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, meter_list);

                    spmeter.setAdapter(dataAdapter);
                    HashSet<String> uniq = new HashSet<>(collectore_list);
                    collectore_list.clear();
                    collectore_list.addAll(uniq);
                    Collections.sort(collectore_list);
                     dataAdapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, collectore_list);
                    spcoll.setAdapter(dataAdapter2);
                    if(hh != "" && hh1 != "" ){
                        int pos1 =  dataAdapter2.getPosition(hh);
                        spcoll.setSelection(pos1);
                        int pos2 = dataAdapter.getPosition(hh1);
                        spmeter.setSelection(pos2);

                    }

                    //pDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(Meteriforequest);

    }
    private void getBalance(String scollid,String smeterid){
        JSONObject jsn = new JSONObject();
        try {
            jsn.put(KEY_COLLID,scollid);
            jsn.put(KEY_METERID,smeterid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Balancerequest = new JsonObjectRequest(Request.Method.POST,balance_url , jsn, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString(KEY_STATUS)=="true"){
                        balance.setText(response.getString(KEY_BALANCE));
                        mEditor.putString("balance",balance.getText().toString());
                        mEditor.apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(Balancerequest);


    }

    private void getYeardata(String scollid, String smeterid){
        JSONObject jsn = new JSONObject();
        try {
            jsn.put(KEY_COLLID,scollid);
            jsn.put(KEY_METERID,smeterid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Yearrequest = new JsonObjectRequest(Request.Method.POST,getyaer_url , jsn, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jam,jad;
                    jam= response.getJSONArray("r_meters");
                    jad = response.getJSONArray("dates");
                    if(response.getString(KEY_STATUS)=="true"){
                        String metercons="";
                        String datecons="";
                        for(int i =0;i<jam.length();i++){

                                 metercons = jam.getString(i);
                                r_meter.add(metercons);


                                datecons = jad.getString(i);
                                dates.add(datecons.substring(5));

                        }
                        String[] jj = r_meter.toArray(new String[0]);
                        int n =0;
                        float m ;
                        float[] num =new float[jj.length];
                        float[] num1 =new float[jj.length];

                        for(int i=0 ;i<jj.length;i++) {

                            num[i]= Float.parseFloat(jj[i]);
                            m= num[i];
                            entries.add(new BarEntry(i, m));

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    barDataSet = new BarDataSet(entries, "Consumption");
                    //barDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    //to enable the cubic density : if 1 then it will be sharp curve
                    //barDataSet.setCubicIntensity(0.3f);
                    //barDataSet.setDrawFilled(false);
                    //barDataSet.setBarBorderWidth(2.5f);
                    //barDataSet.setColor(Color.rgb(10,125,193));
                    barDataSet.setColor(Color.rgb(10,125,193));
                    //set the transparency
                    //lineDataSet.setFillAlpha(20);
                    //lineDataSet.setCircleRadius(2f);
                    barDataSet.setDrawValues(false);
                    Typeface tf = ResourcesCompat.getFont(getContext().getApplicationContext(),R.font.arial);
                    barDataSet.setValueTypeface(tf);
                    //lineDataSet.enableDashedLine();
                    thedata = new BarData(barDataSet);
                    chart.setData(thedata);
                    chart.setTouchEnabled(true);
                    chart.setDragEnabled(true);
                    chart.setScaleEnabled(true);
                    chart.setDrawMarkers(true);
                    Legend legend;
                    legend = chart.getLegend();
                    legend.setForm(Legend.LegendForm.CIRCLE);
                    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    chart.getAxisRight().setDrawLabels(false);
                    datesArray = dates.toArray(new String[dates.size()]);//Set date in x Axis by array j
                YAxis yAxis = chart.getAxisLeft();

                    xAxis = chart.getXAxis();
                    yAxis.setTypeface(tf);
                    chart.getRendererLeftYAxis().getPaintAxisLabels().setTypeface(tf);
                    chart.getRendererLeftYAxis().getPaintAxisLabels().setTextSize(10f);
                    chart.getLegend().setWordWrapEnabled(true);
                    chart.getDescription().setEnabled(false);
                    chart.getDescription().setTextSize(10f);
                    chart.setExtraTopOffset(10f);
                    chart.getXAxis().setGranularity(1f);
                    chart.getXAxis().setLabelCount(14);
                    chart.getXAxis().setGranularityEnabled(true);
                    chart.getAxisLeft().setDrawGridLines(false);
                    chart.getAxisRight().setDrawGridLines(false);
                    chart.getXAxis().setDrawGridLines(false);
                    chart.setExtraBottomOffset(50f);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelRotationAngle(272f);
                    xAxis.setCenterAxisLabels(false);
                    xAxis.setAxisMaximum(entries.size());
                    xAxis.setAxisMaxValue(entries.size());
                    yAxis.setAxisMinimum(0f);
                    chart.getRendererXAxis().getPaintAxisLabels().setTextAlign(Paint.Align.CENTER);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(datesArray));
                    MarkerView mv = new MyMarkerView(getContext(), R.layout.marker_view);
                    chart.setMarkerView(mv);
                    chart.invalidate();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(Yearrequest);

            mEditor.putString("col_index",scollid);
            mEditor.putString("meter_index", smeterid);
            mEditor.commit();

    }
    public class MyMarkerView extends MarkerView {

        private final TextView tvContent;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);

            tvContent = findViewById(R.id.tvContent);
        }

        // runs every time the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {


                tvContent.setText(Utils.formatNumber(e.getY(), 2, true));


            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //if(isrun) {
            // handler.removeCallbacks(runnable);
          // handler.removeCallbacksAndMessages(runnable);
        //}
        if(isrun1){
            //handler.removeCallbacks(runnable1);

            handler.removeCallbacksAndMessages(runnable1);

        }
    }


}
