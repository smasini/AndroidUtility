package it.smasini.utility.library;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Simone on 06/10/16.
 */
public class AssetUtility {

    public static void copyInputStreamToFile(InputStream in, String path) {
        try {
            OutputStream out = new FileOutputStream(new File(path));
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String loadJSONFromAsset(Activity activity, String filename) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject getJSONObjectFromAsset(Activity activity, String filename){
        try {
            return new JSONObject(loadJSONFromAsset(activity,filename));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
