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


    public static void askPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
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

    public static boolean existFile(String path){
        File file = new File(path);
        return file.exists();
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

    public static String getExtension(String name){
        String[] s = name.split("\\.");
        if(s.length > 0) {
            return s[s.length - 1].toLowerCase();
        }
        return "";
    }

    /**
     *
     * @param srcPath path file to copy
     * @param dstPath directory of destination
     * @param newFilename filename with extension of new file
     * @return true if the file is been copied
     */
    public static boolean copyFile(String srcPath, String dstPath, String newFilename) {
        File src = new File(srcPath);
        createDirectoty(dstPath);
        File dst = new File(dstPath + "/" + newFilename);
        if(src.getAbsolutePath().equals(dst.getAbsolutePath())){
            return true;
        }else{
            try {
                InputStream is = new FileInputStream(src);
                OutputStream os = new FileOutputStream(dst);
                byte[] buff = new byte[1024];
                int len;
                while ((len = is.read(buff)) > 0) {
                    os.write(buff, 0, len);
                }
                is.close();
                os.close();
            }catch (IOException e){
                Log.e("Error copyFile", e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * delete directory with content
     * @param directoryPath to delete
     * @return true if the directory is deleted
     */
    private static boolean deleteDirectory(String directoryPath){
        File dir = new File(directoryPath);
        if (dir.exists() && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                File file = new File(dir, child);
                if(file.isDirectory()){
                    deleteDirectory(file.getAbsolutePath());
                }else{
                    deleteFile(file.getAbsolutePath());
                }
            }
            return dir.delete();
        }
        return false;
    }

    /**
     *
     * @param filepath to delete
     * @return true if is deleted
     */
    public static boolean deleteFile(String filepath){
        File file = new File(filepath);
        if(file.exists() && !file.isDirectory()){
            return file.delete();
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
     * @param size in byte
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
