package com.flowersmartmeter.flowersmartwatermeter.MySQLManger;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class Downloader extends AsyncTask<Void,Void,String> {

    Context c;
    String urlAddress;
    Spinner sp;
    ProgressDialog pd;
    String col,colid,meterid;
    LineChart lineChart;
    int i;

    public Downloader(Context c,String urlAddress,Spinner sp,String col,int i) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.sp = sp;
        this.col=col;
        this.i=i;
    }

    public Downloader(Context c, String urlAddress, LineChart lineChart, String col, int i, String txt, String txt2) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.lineChart = lineChart;
        this.col = col;
        this.colid = txt;
        this.meterid=txt2;

        this.i=i;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Downloading..");
        pd.setMessage("Retrieving data...Please wait");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.retrieveData();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        pd.dismiss();
        if (i==0)
        sp.setAdapter(null);

        if (s != null) {
            if(i ==0) {
                Parser p = new Parser(c, s, sp,col,0);
                p.execute();
            }else if (i == 1){
                Parser p = new Parser(c,s, lineChart,col,1);
                p.execute();
            }

        } else {
            Toast.makeText(c, "No data retrieved", Toast.LENGTH_SHORT).show();
        }
    }

    private String retrieveData() {
        HttpURLConnection con=Connector.connect(urlAddress);
        if(con==null)
        {
            return null;
        }

        try {
            if(i==1) {
                OutputStream os = con.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post_data = URLEncoder.encode("colid","UTF-8")+"="+URLEncoder.encode(colid,"UTF-8")+"&"
                        +URLEncoder.encode("meterid","UTF-8")+"="+URLEncoder.encode(meterid,"UTF-8");
                bw.write(post_data);
                bw.flush();
                bw.close();
                InputStream is = con.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder receivedData = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    receivedData.append(line).append("\n");
                }
                is.close();
                br.close();
                return receivedData.toString();
            }else {
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder receivedData = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    receivedData.append(line).append("\n");
                }
                is.close();
                br.close();
                return receivedData.toString();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}