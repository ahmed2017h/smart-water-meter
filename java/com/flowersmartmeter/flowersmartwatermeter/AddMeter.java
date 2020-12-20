package com.flowersmartmeter.flowersmartwatermeter;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMeter extends Fragment {
    View v;
    private EditText etmeter_id, etcoll_id;
    private Button btnAddmeter;
    private static final String KEY_USERID = "user_id";
    private static final String KEY_COLLECTORID = "collector_id";
    private static final String KEY_METERID = "meter_id";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMPTY = "";
    private String Addmeteruri = "http://13.235.31.177/backend/addMeter.php";
    private String user_id;
    private String collector_id;
    private String meter_id;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.add_meter, container, false);
        initv();
        btnAddmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id = getActivity().getIntent().getStringExtra("user_id");
                collector_id = etcoll_id.getText().toString().trim();
                meter_id = etmeter_id.getText().toString().trim();
                try {
                    if(validateInputs()) {
                        displayLoader();
                        setAddmeter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
        return v;

    }
    private void displayLoader() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Login.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    private void initv() {
        etcoll_id = v.findViewById(R.id.etcoll_id);
        etmeter_id = v.findViewById(R.id.etmeter_id);
        btnAddmeter = v.findViewById(R.id.add_meter_btn);
    }

    private void setAddmeter() throws JSONException {
        JSONObject jos = new JSONObject();
        jos.put(KEY_USERID, user_id);
        jos.put(KEY_COLLECTORID, collector_id);
        jos.put(KEY_METERID, meter_id);
        jos.put("flag", "addMeter");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Addmeteruri, jos, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    if (response.getString(KEY_STATUS).equals("false")) {
                        etmeter_id.setText("");
                        Toast.makeText(getContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    } else if (response.getString(KEY_STATUS).equals("true")) {
                        Toast.makeText(getContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(collector_id)) {
            etcoll_id.setError("Full Name cannot be empty");
            etcoll_id.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(meter_id)) {
            etmeter_id.setError("Phone Number cannot be empty");
            etmeter_id.requestFocus();
            return false;
        }
        return true;
    }

}
