package com.flowersmartmeter.flowersmartwatermeter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;
import static com.paypal.android.sdk.payments.PayPalService.EXTRA_PAYPAL_CONFIGURATION;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyCredit extends Fragment {

public static final int PAYPAL_REQUEST_CODE =7171;
private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
        .clientId(com.flowersmartmeter.flowersmartwatermeter.Config.Config.PAYPAL_CLINTE_ID);
    private Button btnPayNow,btnclc;
    private EditText etbalance;
    private TextView current_balance,curreading,edtAmount;
    private String amount ="";
    private Spinner spcoll,spmeter;
    private ProgressDialog pDialog;
    private View v;
    private static final String KEY_COLLECTOR = "collectors";
    private static final String KEY_METERS = "meter";
    private static final String KEY_USERID = "user_id";
    private static final String KEY_COLLID ="collector_id";
    private static final String KEY_METERID = "meter_id";
    private static final String KEY_BALANCE ="balance_limit";
    private static final String KEY_STATUS = "result";
    private static final String KEY_TRANSACTIONID = "transactionId";
    private static final String KEY_AMOUNT = "paymentAmount";
    private static final String KEY_PAYRID = "payerId";
    private static final String KEY_CID = "collectorId";
    private static final String KEY_MID = "meterId";
    private static final String KEY_PAYMRTHOD = "paymentMethod";
    private static final String KEY_PHONENUMBER = "phoneNumber";
    private static final String KEY_CREDIT ="credit";
    private String meter_info_url = "http://13.235.31.177/backend/getCollectors.php";
    private String balance_url = "http://13.235.31.177/backend/getBalanceData.php";
    private String addtrans_url = "http://13.235.31.177/backend/addTransaction.php";
    public static final String HOST = "13.235.31.177";
    public static final int PORT = 889;
    Socket s = new Socket();
    static DataInputStream reader;
    DataOutputStream dOS;
    byte[] input = new byte[1000];
    String[] status;
    String readcons;
    int counter;
    private ArrayList<String> collectore_list=new ArrayList<>();
    private ArrayList<String> meter_list=new ArrayList<>();
    private FragmentActivity myContext;

    public BuyCredit() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(),PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v =   inflater.inflate(R.layout.fragment_buy_credit,container,false);
        // Inflate the layout for this fragment
        initView();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RecMsg recMsg = new RecMsg();
        Thread th = new Thread(recMsg);
        th.setDaemon(true);
        th.start();
        counter = 0;
        getMeterifo();
            Intent intent = new Intent(getActivity(), PayPalService.class);
            intent.putExtra(EXTRA_PAYPAL_CONFIGURATION, config);
            getActivity().startService(intent);
            edtAmount.setText("0");
            btnclc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String bala = etbalance.getText().toString();
                    if(!bala.equals("")) {
                        int bal = Integer.parseInt(etbalance.getText().toString());
                        int result = bal / 10;
                        edtAmount.setText(String.valueOf(result));
                    }else{
                        etbalance.requestFocus();
                    }
                }
            });
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtAmount.getText().toString()!=""&& Integer.parseInt(edtAmount.getText().toString())!=0) {
                    processPayment();
                }
            }
        });
        curreading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spcollid =spcoll.getSelectedItem().toString().trim();
                String spmeterid=spmeter.getSelectedItem().toString().trim();
                counter++;
                if(!spcollid.equals("")&&!spmeterid.equals("")) {
                    getBalance(spcollid, spmeterid);
                    if(counter>1) {
                        conn(spcollid, spmeterid, "ReadMeater");
                    }
                }else {
                    if (spcollid.equals("")) {
                        AlertDialog.Builder errDialog = new AlertDialog.Builder(getContext());
                        errDialog.setTitle("Error Message");
                        errDialog.setMessage("Select your Collector Id");
                        errDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        errDialog.show();
                    }
                    if (spmeterid.equals("")) {
                        AlertDialog.Builder errDialog = new AlertDialog.Builder(getContext());
                        errDialog.setTitle("Error Message");
                        errDialog.setMessage("Select your Meter Id");
                        errDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        errDialog.show();
                    }
                }
            }
        });
        spmeter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spcollid =spcoll.getSelectedItem().toString().trim();
                String spmeterid=spmeter.getSelectedItem().toString().trim();
                counter++;
                if(!spcollid.equals("")&&!spmeterid.equals("")) {
                    getBalance(spcollid, spmeterid);
                    if(counter>1) {
                        conn(spcollid, spmeterid, "ReadMeater");
                    }
                }else{
                    if(spcollid.equals("")){
                        AlertDialog.Builder errDialog = new AlertDialog.Builder(getContext());
                        errDialog.setTitle("Error Message");
                        errDialog.setMessage("Select your Collector Id");
                        errDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        errDialog.show();
                    }
                    if(spmeterid.equals("")){
                        AlertDialog.Builder errDialog = new AlertDialog.Builder(getContext());
                        errDialog.setTitle("Error Message");
                        errDialog.setMessage("Select your Meter Id");
                        errDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        errDialog.show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return v;
    }
    private void initView(){
        btnPayNow = v.findViewById(R.id.btnPaynow);
        btnclc = v.findViewById(R.id.btncalc);
        edtAmount = v.findViewById(R.id.edtAmount);
        etbalance = v.findViewById(R.id.newbalance);
        spcoll = v.findViewById(R.id.coll_id);
        spmeter = v.findViewById(R.id.meter_id);
       current_balance = v.findViewById(R.id.current_balance);
        curreading = v.findViewById(R.id.current_reading);
    }
    private void processPayment() {
        amount = edtAmount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD","Flower Water Smart Meter",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null)
                {
                    try{
                        String paymentDetails = confirmation.toJSONObject().toString(6);
                       // getActivity().startActivity(new Intent(getActivity(),PaymentDetails.class)
                        //.putExtra("PaymentDetails",paymentDetails)
                          //      .putExtra("PaymentAmount",amount)
                        //);
                        JSONObject jo;
                        JSONObject jsn = new JSONObject(paymentDetails);
                        jo = jsn.getJSONObject("response");
                        String trID = jo.getString("id");
                        transction(trID);
                        String spcollid =spcoll.getSelectedItem().toString().trim();
                        String spmeterid=spmeter.getSelectedItem().toString().trim();
                        getBalance(spcollid,spmeterid);
                        etbalance.setText("0");
                        edtAmount.setText("0");
                    } catch (JSONException e) {
                            e.printStackTrace();
                    }
                }else if(resultCode == Activity.RESULT_CANCELED)
                    Toast.makeText(getContext(),"Cancel",Toast.LENGTH_SHORT).show();
            }else if(requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
                Toast.makeText(getContext(),"Invalid",Toast.LENGTH_SHORT).show();

        }
    }
    public void transction(String trnsid){

        String userid = getActivity().getIntent().getStringExtra(KEY_USERID);
        String phone = getActivity().getIntent().getStringExtra("phonenumber");
        String spcollid =spcoll.getSelectedItem().toString().trim();
        String spmeterid=spmeter.getSelectedItem().toString().trim();
        int a =Integer.parseInt(current_balance.getText().toString());
        int b = Integer.parseInt(etbalance.getText().toString());
        String credit = String.valueOf(a+b);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("flag","transaction");
            jsonObject.put(KEY_TRANSACTIONID,trnsid);
            jsonObject.put(KEY_AMOUNT,amount);
            jsonObject.put(KEY_CREDIT,credit);
            jsonObject.put(KEY_PAYRID,userid);
            jsonObject.put(KEY_CID,spcollid);
            jsonObject.put(KEY_MID,spmeterid);
            jsonObject.put(KEY_PAYMRTHOD,"PayPal");
            jsonObject.put(KEY_PHONENUMBER,phone);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, addtrans_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
    public void getMeterifo(){
        String user_id = getActivity().getIntent().getStringExtra("user_id");
        JSONObject jsn = new JSONObject();
        try {
            jsn.put(KEY_USERID,user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, meter_info_url, jsn, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jam,jac;
                    jam = response.getJSONArray(KEY_METERS);

                    for(int i =0;i<jam.length();i++){

                        String meterid = jam.getString(i);
                        meter_list.add(meterid);
                    }
                    jac= response.getJSONArray(KEY_COLLECTOR);
                    for (int i=0;i<jac.length();i++){
                        String[] coll = new String[jac.length()];
                        int j = 0;
                        coll[i] = jac.optString(i);
                        // String cmp = jac.getString(i+1);
                        collectore_list.add(coll[i]);

                        // Log.i("cool", coll[i]);
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, meter_list);
                    spmeter.setAdapter(dataAdapter);
                    HashSet<String> uniq = new HashSet<>(collectore_list);
                    collectore_list.clear();
                    collectore_list.addAll(uniq);
                    Collections.sort(collectore_list);
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,collectore_list);
                    spcoll.setAdapter(dataAdapter2);
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
        requestQueue.add(request);
    }
    private void getBalance(String scollid,String smeterid){
        JSONObject jsn = new JSONObject();
        try {
            jsn.put(KEY_COLLID,scollid);
            jsn.put(KEY_METERID,smeterid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,balance_url , jsn, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString(KEY_STATUS)=="true"){
                        current_balance.setText(response.getString(KEY_BALANCE));
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
        requestQueue.add(request);
    }
    String tinput;
    public void conn(String collid ,String meterid,String command) {

        try {
            String incom = "MeaterNum:" + meterid + ";CJQNum:" + collid + ";" + command + ";";
            byte[] checkconn = new byte[1000];
            if (!s.isOutputShutdown()) {
                dOS = new DataOutputStream(s.getOutputStream());
                // dOS.write("FLOWER10".getBytes());
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

                        readcons = status[3].substring(status[3].indexOf(":") + 1);
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"The server is not response",Toast.LENGTH_LONG).show();
                }
                //readcons=tinput;
                //line = tinput.substring(21,);
                //s.close();

                //}
            }else {Toast.makeText(getActivity().getApplicationContext(),"The server is not working",Toast.LENGTH_LONG).show();}

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

            // response.setText("UnknownHostException:%s " + e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

            // response.setText("IOException: " + e.toString());
        }
        curreading.setText(readcons);

        //timestamp.setText(line);
        // Toast.makeText(getContext(), connchk, Toast.LENGTH_LONG).show();
        Log.i("PDA", "----->" + readcons);

    }
    class RecMsg implements Runnable{

        @Override
        public void run() {
            try {
                if(s.isConnected()){
                    try {
                        dOS.flush();
                        dOS.close();
                        reader.close();
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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

}
