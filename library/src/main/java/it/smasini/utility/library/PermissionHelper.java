package it.smasini.utility.library;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.Random;

/**
 * Created by Simone on 10/11/16.
 */

public class PermissionHelper {

    private final int MY_REQUEST_CODE;

    private String[] permissions;
    private Activity activity;
    private PermissionListener permissionListener;

    public PermissionHelper(Activity activity, String[] permissions, PermissionListener permissionListener){
        this.permissions = permissions;
        this.activity = activity;
        this.permissionListener = permissionListener;
        Random random = new Random();
        this.MY_REQUEST_CODE = random.nextInt(500);
    }

    public PermissionHelper(Activity activity, String[] permissions){
        this(activity, permissions, null);
    }

    public void askPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, permissions, MY_REQUEST_CODE);
        }
    }

    public void checkIfNeedAskPermission(){
        if(!havePermissions()){
            askPermissions();
        }else{
            if(permissionListener!=null){
                permissionListener.onPermissionGranted();
            }
        }
    }

    public boolean havePermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            boolean haveAll = true;
            for(String permission : permissions){
                haveAll &= ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            }
            return haveAll;
        }
        return true;
    }

    public void onPermissionAccepted(int requestCode, String permissions[], int[] grantResults){
        if(requestCode == MY_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, yay! Do the task you need to do.
            if(permissionListener!=null){
                permissionListener.onPermissionGranted();
            }
        }
    }

    public void setPermissionListener(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public interface PermissionListener{
        void onPermissionGranted();
    }
}
