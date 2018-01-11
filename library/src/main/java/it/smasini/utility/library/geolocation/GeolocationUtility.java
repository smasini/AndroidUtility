package it.smasini.utility.library.geolocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public class GeolocationUtility {

    public static Address getAddresFromLatLng(Context context, double latitude, double longitude){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if(addresses!=null && addresses.size()>0) {
                return addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    public static String getUrlForGetAddressFromLatLng(double latitude, double longitude){
        return String.format("http://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=true", latitude, longitude);
    }

    public static GeolocationInfo parseJSONObjectForAddress(JSONObject object){
        GeolocationInfo gi = null;
        String status = object.optString("status");
        if(status.equalsIgnoreCase("ok")){
            JSONArray results = object.optJSONArray("results");
            if(results!=null) {
                JSONObject zero = results.optJSONObject(0);
                if(zero!=null) {
                    JSONArray address_components = zero.optJSONArray("address_components");
                    if (address_components != null) {
                        for (int i = 0; i < address_components.length(); i++) {
                            JSONObject zero2 = address_components.optJSONObject(i);
                            if(zero2!=null){
                                if(gi==null){
                                    gi = new GeolocationInfo();
                                }
                                String long_name = zero2.optString("long_name");
                                JSONArray mtypes = zero2.optJSONArray("types");
                                String type = mtypes.optString(0);
                                /*
                            if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {

                            }
                            */
                                if((type!=null && !type.isEmpty()) && (long_name!= null && !long_name.isEmpty())){
                                    if(type.equalsIgnoreCase("street_number")){
                                        gi.setAddress1(long_name + " ");
                                    }
                                    else if(type.equalsIgnoreCase("route")){
                                        gi.setAddress1(gi.getAddress1() + long_name);
                                    }
                                    else if(type.equalsIgnoreCase("sublocality")){
                                        gi.setAddress2(long_name);
                                    }
                                    else if(type.equalsIgnoreCase("locality")){
                                        gi.setCity(long_name);
                                    }
                                    else if(type.equalsIgnoreCase("administrative_area_level_2")){
                                        gi.setCountry(long_name);
                                    }
                                    else if(type.equalsIgnoreCase("administrative_area_level_1")){
                                        gi.setState(long_name);
                                    }
                                    else if(type.equalsIgnoreCase("country")){
                                        gi.setCountry(long_name);
                                    }
                                    else if(type.equalsIgnoreCase("postal_code")){
                                        gi.setPostalCode(long_name);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return gi;
    }

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
     * @param lat1 latitude of first point
     * @param lng1 longitude of first point
     * @param lat2 latitude of second point
     * @param lng2 longitude of second point
     * @return distance
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
