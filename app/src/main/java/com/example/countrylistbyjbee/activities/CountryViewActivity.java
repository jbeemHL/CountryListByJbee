package com.example.countrylistbyjbee.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.countrylistbyjbee.R;
import com.example.countrylistbyjbee.modeladapter.CountryList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

public class CountryViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double longitude, latitude;
    String countryName;

    TextView tvCountryName, tvCapital, tvRegion, tvAbbv, tvCallingCodes, tvPopulation,
            tvCurrencies, tvLatitude, tvLongitude, tvLanguages, tvBorders;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countryview);
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DecimalFormat formatter = new DecimalFormat("#,###,###");

        countryName = getIntent().getStringExtra("countryName");
        String capital = getIntent().getStringExtra("capital");
        String region = getIntent().getStringExtra("region");
        String abbv = getIntent().getStringExtra("abbv");
        String callingCodes = getIntent().getStringExtra("callingCodes");
        String population = getIntent().getStringExtra("population");
        String currencies = getIntent().getStringExtra("currencies");
        String latlng = getIntent().getStringExtra("latlng");
        String languages = getIntent().getStringExtra("languages");
        String borders = getIntent().getStringExtra("borders");

        tvCountryName = (TextView) findViewById(R.id.tv_countryname);
//        setTitle(countryName);
        getSupportActionBar().setTitle(countryName);

        tvCapital = (TextView) findViewById(R.id.tv_capital);
        tvRegion = (TextView) findViewById(R.id.tv_region);
        tvAbbv = (TextView) findViewById(R.id.tv_abbv);
        tvCallingCodes = (TextView) findViewById(R.id.tv_callingcodes);
        tvPopulation = (TextView) findViewById(R.id.tv_population);
        tvCurrencies = (TextView) findViewById(R.id.tv_currencies);
        tvLatitude = (TextView) findViewById(R.id.tv_latitude);
        tvLongitude = (TextView) findViewById(R.id.tv_longitude);
        tvLanguages = (TextView) findViewById(R.id.tv_languages);
        tvBorders = (TextView) findViewById(R.id.tv_borders);

        longitude = Double.parseDouble(parseString(latlng, "long"));
        latitude = Double.parseDouble(parseString(latlng, "lat"));

        tvCountryName.setText(countryName);
        tvCapital.setText("Capital: " + capital);
        tvRegion.setText("Region: " + region);
        tvAbbv.setText("Abbreviation: " + parseJsonData(abbv, "acronym"));
        tvCallingCodes.setText("Calling codes: +" + callingCodes.replaceAll("\\[", "")
                .replaceAll("\\]","")
                .replaceAll("\"",""));
        tvPopulation.setText("Population: " + formatter.format(Integer.parseInt(population)));
        tvCurrencies.setText("Currencies: " + parseJsonData(currencies, "code") + " (" +
                parseJsonData(currencies, "symbol") + ")");
        tvLatitude.setText("Latitude: " + latitude);
        tvLongitude.setText("Longitude: " + longitude);
        tvLanguages.setText("Languages: " + parseJsonData(languages, "name"));
        tvBorders.setText("Borders: " + borders);

        longitude = Double.parseDouble(parseString(latlng, "long"));
        latitude = Double.parseDouble(parseString(latlng, "lat"));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        if(longitude != null){
//            mapFragment.getMapAsync(this);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String parseJsonData(String jsonString, String dataSpecific) {
        JSONArray arr = null;
        String data = "";

        try {
            arr = new JSONArray(jsonString);
            JSONObject jObj = arr.getJSONObject(0);
            data = jObj.getString(dataSpecific);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(data.equals("null")){
            data = "";
        }
        return data;
    }

    public String parseString(String toParse, String latlngBoolean){
        toParse = toParse.replaceAll("\\[", "").replaceAll("\\]","");
        String[] separated = toParse.split("\\,");

        if(latlngBoolean.equals("lat")){
            return separated[0];
        } else{
            return separated[1];
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(countryName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 3F));
    }
}