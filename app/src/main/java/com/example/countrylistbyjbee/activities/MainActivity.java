package com.example.countrylistbyjbee.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.countrylistbyjbee.modeladapter.CountryList;
import com.example.countrylistbyjbee.modeladapter.CountryListAdapter;
import com.example.countrylistbyjbee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    String url ="https://restcountries.eu/rest/v2/all";
    ProgressDialog dialog;
    EditText etSearch;
    ArrayList<CountryList> countryLists = new ArrayList<CountryList>();
    ArrayList<CountryList> searchCountryLists = new ArrayList<CountryList>();

    private RecyclerView rvCountries;
    private CountryListAdapter countryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = (EditText) findViewById(R.id.et_search);
        rvCountries = (RecyclerView) findViewById(R.id.list_view);
        editTextAction();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCountries.getContext(),
                layoutManager.getOrientation());
        rvCountries.addItemDecoration(dividerItemDecoration);
        rvCountries.setLayoutManager(layoutManager);
        countryListAdapter = new CountryListAdapter(this, countryLists);
        rvCountries.setAdapter(countryListAdapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJsonData(response);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void editTextAction(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(etSearch.getText().length() == 0) {
                    setHideSoftKeyboard(etSearch);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                countryLists.clear();
                for(CountryList str : searchCountryLists){
                    Pattern p = Pattern.compile(etSearch.getText().toString().toLowerCase() + "(.*)");
                    Matcher m = p.matcher(str.getCountryName().toLowerCase());
                    if (m.find()) {
                        countryLists.add(str);
                    }
                    countryListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void parseJsonData(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            countryLists.clear();
            searchCountryLists.clear();

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                CountryList cList = new CountryList();

                cList.setCountryName(object.getString("name"));
                cList.setFlag(object.getString("flag"));
                cList.setCapital(object.getString("capital"));
                cList.setRegion(object.getString("region"));
                cList.setAbbv(object.getString("regionalBlocs"));
                cList.setCallingCodes(object.getString("callingCodes"));
                cList.setPopulation(object.getString("population"));
                cList.setCurrencies(object.getString("currencies"));
                cList.setLnglat(object.getString("latlng"));
                cList.setLanguages(object.getString("languages"));
                cList.setBorders(object.getString("borders"));

                countryLists.add(cList);
                searchCountryLists.add(cList);
                countryListAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setHideSoftKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}