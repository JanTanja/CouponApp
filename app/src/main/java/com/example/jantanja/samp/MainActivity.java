package com.example.jantanja.samp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
//    implements
//        OnMyLocationButtonClickListener,
//        OnMapReadyCallback,
//        ActivityCompat.OnRequestPermissionsResultCallback
{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    TextView couponStore, address, city, state, zip, phoneNumber, lat, lon;
    String url = "http://api.8coupons.com/v1/getdeals?key=48479a233144a31b452da5ce1060993fe0c0b024ecc88bff4d9aa97a4d8337fd08cfc55230663c3017c90904ca6cb063&zip=92064&mileradius=20&limit=500&userid=18381";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        final Button button = (Button) findViewById(R.id.maps);

        couponStore = (TextView) findViewById(R.id.couponStore);
        address = (TextView) findViewById(R.id.address);
        lat = (TextView) findViewById(R.id.latitude);
        lon = (TextView) findViewById(R.id.longitude);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        zip = (TextView) findViewById(R.id.phoneNumber);
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);

        DownloadTask task = new DownloadTask();
        task.execute(url);
    }


    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls)
        {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1)
                {
                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            try
            {
                JSONArray json = new JSONArray(result);
//                String couponInfo = json.getString("result");
//                Log.i("Results - ", String.valueOf(json));

//                JSONArray placeObject = new JSONArray(couponInfo);

                String StoreNameText, storeAddress, storeCity, storeState = "";
                int storeLatitude, storeLongitude, storeZip, storePhoneNumber = 0;

                for (int i = 0; i < json.length(); i++) {
                    JSONObject couponPart = json.getJSONObject(i);
                    Log.i("Store Name - ", couponPart.getString("name"));

                    StoreNameText = couponPart.getString("name");
                    storeAddress = couponPart.getString("address");
                    storeCity = couponPart.getString("city");
                    storeState = couponPart.getString("state");

                    storeLatitude = couponPart.getInt("lat");
                    storeLongitude = couponPart.getInt("lon");
                    storeZip = couponPart.getInt("ZIP");
                    storePhoneNumber = couponPart.getInt("phone");


                    couponStore.setText("Store name is " + StoreNameText);
                    address.setText("Address is " +storeAddress);
                    city.setText("City is " + storeCity);
                    state.setText("State is " + storeState);

                    zip.setText("ZIP is" + Integer.toString(storeZip));
                    phoneNumber.setText("Phone number is "+ Integer.toString(storePhoneNumber));
                    lat.setText("Latitude is " + Integer.toString(storeLatitude));
                    lon.setText("Longitude is " + Integer.toString(storeLongitude));
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            Log.i("Result", result);

        }
//        @Override
//        protected JSONArray getJSONFromResult(String url)
//        {
//            InputStream is = null;
//            String result = "";
//            JSONArray jArray = null;
//
//            return jArray;
//        }
    }
    public void viewMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}