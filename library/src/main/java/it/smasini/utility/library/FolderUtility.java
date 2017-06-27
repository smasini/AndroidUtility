package it.smasini.utility.library;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Created by Simone Masini on 30/06/2016
 */
public class FolderUtility {


    public static void askPermissions(Activity activity){
        askPermissions(activity, null);
    }

    public static void askPermissions(Activity activity, PermissionHelper.PermissionListener permissionListener){
        PermissionHelper ph = new PermissionHelper(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, permissionListener);
        ph.askPermissions();
    }

    public static boolean havePermissions(Activity activity){
        return havePermissions(activity, null);
    }

    public static boolean havePermissions(Activity activity, PermissionHelper.PermissionListener permissionListener){
        PermissionHelper ph = new PermissionHelper(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, permissionListener);
        return ph.havePermissions();
    }

    /**
     * require   uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
     * in manifest permission
     * @param name the name of the app
     * @return path of the application data on external memory
     */
    public static String getApplicationFilePath(String name){
        File sdDir = Environment.getExternalStorageDirectory();
        String path = sdDir.getAbsolutePath();
        Log.d("Application folder name", name);
        path += "/" + name;
        boolean directoryCreated = createDirectoty(path);
        if(directoryCreated){
            try {
                File nomedia = new File(path, ".nomedia");
                nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * Create a directory
     * @param path of directory to create
     * @return true if directory is created now, false if already exist or there is an error
     */
    public static boolean createDirectoty(String path){
        File file = new File(path);
        if(!file.exists()){
            return file.mkdir();
        }
        return false;
    }

    /**
     * delete directory with content
     * @param directoryPath to delete
     * @return true if the directory is deleted
     */
    public static boolean deleteDirectory(String directoryPath){
        return deleteDirectory(directoryPath, true);
    }

    /**
     * delete content of a directory and choice to include the directory
     * @param directoryPath to delete
     * @param includeRoot for include the root directory
     * @return true if the directory is deleted
     */
    public static boolean deleteDirectory(String directoryPath, boolean includeRoot){
        File dir = new File(directoryPath);
        if (dir.exists() && dir.isDirectory()) {
            String[] children = dir.list();
            if(children!=null) {
                for (String child : children) {
                    File file = new File(dir, child);
                    if (file.isDirectory()) {
                        deleteDirectory(file.getAbsolutePath());
                    } else {
                        FileUtility.deleteFile(file.getAbsolutePath());
                    }
                }
            }
            if(includeRoot)
                return dir.delete();
            else
                return true;
        }
        return false;
    }

    /**
     * delete all data from the application data directory
     * @param name the name of the app
     * @return true if data is deleted
     */
    public static boolean deleteAllApplicationData(String name){
        return deleteDirectory(getApplicationFilePath(name));
    }


    /**
     * @param size size
     * return memory free on the External memory
     * @return the memory free
     */
    public static double freeMemory(Size size)
    {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        long byteSize = externalStorageDir.getFreeSpace();
        return convertSize(byteSize, size);
    }

    /**
     *
     * @param sizeByte size in byte
     * @param size type to convert
     * @return the memory converted
     */
    public static double convertSize(long sizeByte, Size size){
        switch (size){
            case BYTE:
                return sizeByte;
            case KB:
                return sizeByte/1024.0;
            case MB:
                return ((sizeByte/1024.0)/1024.0);
            case GB:
                return (((sizeByte/1024.0)/1024.0)/1024.0);
            case TB:
                return ((((sizeByte/1024.0)/1024.0)/1024.0)/1024.0);
        }
        return 0;
    }

    public enum Size{
        BYTE,
        KB,
        MB,
        GB,
        TB
    }

    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }
}
