package it.smasini.utility.library.geolocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public class Geolocation implements LocationListener {

    public static double latitude;
    public static double longitude;

    private static final String TAG = "GEOLOC";
    private static final float DISTANCE_BETWEEN_LOCATION = 0;
    private static final long TIME_BETWEEN_LOCATION = 1000 * 30;//time in millisec between update position

    private LocationManager locMan;
    private static Geolocation geolocation;
    private Context context;

    private Geolocation(Context context) {
        locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void askPermission(Activity activity){
        if(!checkPermission()){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
    }

    public static Geolocation getGeolocalizzazioneInstance(Context context) {
        if (geolocation == null) {
            geolocation = new Geolocation(context);
            geolocation.getLastLocation();
        }
        return geolocation;
    }

    public void getLastLocation() {
        if(!checkPermission())
            return;
        Location lastKnownLocationGps = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastKnowLocationNetwork = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location lastKnowLocationPassive = locMan.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if(lastKnownLocationGps!=null)
            changeLocation(lastKnownLocationGps);
        else if(lastKnowLocationNetwork!=null)
            changeLocation(lastKnowLocationNetwork);
        else if(lastKnowLocationPassive!=null)
            changeLocation(lastKnowLocationPassive);
    }

    public void startLocation(){
        if(!checkPermission())
            return;
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,TIME_BETWEEN_LOCATION,DISTANCE_BETWEEN_LOCATION,this);
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,TIME_BETWEEN_LOCATION,DISTANCE_BETWEEN_LOCATION,this);
        locMan.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, TIME_BETWEEN_LOCATION, DISTANCE_BETWEEN_LOCATION, this);
    }

    public void stopLocation(){
        if(!checkPermission())
            return;
        locMan.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled");
    }

    @Override
    public void onLocationChanged(Location loc) {
        Log.d(TAG, "onLocationChanged");
        if (loc == null)
            return;
        changeLocation(loc);
        stopLocation();
    }

    public void changeLocation(Location loc){
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
    }
}
