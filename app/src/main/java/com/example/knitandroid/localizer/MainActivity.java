package com.example.knitandroid.localizer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LocationListener, SensorListener {
    public String serverAddress = "http://caspinos.ovh:8081/";
    Context appContext;
    Double lat, lon;
    String Address;
    boolean isGPSenabled = false;
    SensorManager sensorManager;
    int TIME = 1000;
    int DISTANCE = 20;
    long lastSensorUpdate=0;
    float last_x,last_y,last_z;
    float x,y,z;
    private static final int SHAKE_THRESHOLD = 6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appContext = this;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        /**
                         * TODO obsługę zmiany pozycji GPSów z lat i lon
                         */

                    }
                });
            }
        }, 0, 1000);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);


        /*
        * SHAKE SHAKE
        * */

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);



    }


    public String getGPSLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        android.location.Location location = null;
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (!isGPSEnabled) {
            askUserToOpenGPS();
        } else if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, DISTANCE, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    return getAddress(lat, lon);
                }

            }
        }
        return null;
    }

    public String getInternetLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        android.location.Location location = null;
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {return  null;}
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, this);
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    return getAddress(lat,lon);
                }
            }
        }
        return null;
    }
    public String getLocation() {


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        android.location.Location location = null;
            String address = new String();
        address=getGPSLocation();
        if(address==null)
            address = getInternetLocation();
        return address;
    }
    public void askUserToOpenGPS() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(appContext);
        /**
         * TODO jesli gps wyłączony to przenieś do ustawień
         */
    }

    @Override
    public void onLocationChanged(Location location) {

  /*
  * TODO Zmiana odpowiednich textVie
  * */
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    public boolean isGPSSet(){
        if(lat!=null&&lon!=null)return true;
        return false;
    }
    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<android.location.Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses!=null&&addresses.size()!=0)
                return addresses.get(0).getAddressLine(0);
            else return Address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastSensorUpdate) > 100) {
                long diffTime = (curTime - lastSensorUpdate);
                lastSensorUpdate = curTime;

                x = values[SensorManager.DATA_X];
                y = values[SensorManager.DATA_Y];
                z = values[SensorManager.DATA_Z];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                  // "action=add_log&lon="+String.valueOf(lon)+"&lat="+String.valueOf(lat)+"&nickname="+getNickname()+"&date="+dateTime
                    /**
                     * TODO wysyłanie requestów
                     */
                getLocation();

                    Toast.makeText(appContext, "Apka działa stosunkowo poprawnie aktualnie przebywasz w "+getAddress(lat,lon), Toast.LENGTH_SHORT).show();
                }
                last_x = x;
                last_y = y;
                last_z = z;

            }
        }
    }

    public String getNickname(){
        /*
        * TODO get text from Edit text
        * */
        return "Kierownik";
    }
    @Override
    public void onAccuracyChanged(int i, int i1) {

    }
}
