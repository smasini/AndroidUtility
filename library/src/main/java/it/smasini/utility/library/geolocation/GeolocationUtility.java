package it.smasini.utility.library.geolocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public class GeolocationUtility {

    public static void startLocationAddress(String address, final Context context, final CallbackLocationFromAddressFound callbackLocationFromAddressFound){
        AsyncTask<String, Void, Location> task = new AsyncTask<String, Void, Location>(){
            @Override
            protected Location doInBackground(String... strings) {
                Address location = null;
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addressFound;
                try {
                    addressFound = geocoder.getFromLocationName(strings[0],1);
                    location = addressFound.get(0);
                    location.getLatitude();
                    location.getLongitude();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Location loc = new Location("manual");
                loc.setLongitude(location.getLongitude());
                loc.setLatitude(location.getLatitude());
                return loc;
            }
            @Override
            protected void onPostExecute(Location location) {
                callbackLocationFromAddressFound.locationFound(location);
            }
        };
        task.execute(address);
    }

    public interface CallbackLocationFromAddressFound {
        void locationFound(Location location);
    }

    public static double getDegrees(double degrees){
        return (degrees * 0.01745327); // degrees * pi over 180
    }

    /**
     * ritorna la distanza in km tra il punto con lat1, lng1 e il punto con lat2, lng2
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2){
        double lat1rad = getDegrees(lat1);
        double lat2rad = getDegrees(lat2);
        double lng1rad = getDegrees(lng1);
        double lng2rad = getDegrees(lng2);
        // apply the spherical law of cosines to our latitudes and longitudes, and set the result appropriately
        // 6378.1 is the approximate radius of the earth in kilometres
        return Math.acos(Math.sin(lat1rad)*Math.sin(lat2rad) + Math.cos(lat1rad)*Math.cos(lat2rad)*Math.cos(lng2rad-lng1rad)) * 6378.1;
    }

}
