package it.smasini.utility.library;

import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Simone on 10/11/16.
 */

public class FileUtility {

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
     *
     * @param srcPath path file to copy
     * @param dstPath directory of destination
     * @param newFilename filename with extension of new file
     * @return true if the file is been copied
     */
    public static boolean copyFile(String srcPath, String dstPath, String newFilename) {
        File src = new File(srcPath);
        FolderUtility.createDirectoty(dstPath);
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

    public static String getExtension(String name){
        String[] s = name.split("\\.");
        if(s.length > 0) {
            return s[s.length - 1].toLowerCase();
        }
        return "";
    }

    public static boolean existFile(String path){
        File file = new File(path);
        return file.exists();
    }

    public static boolean haveValidExtension(String filename){
        return filename.matches("(.*)\\.[a-zA-z0-9]{3}") || filename.matches("(.*)\\.[a-zA-z0-9]{4}");
    }

    public static String getExtensionFromMime(String mimeType){
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(mimeType);
    }

    public static String appendExtensionIfNeed(String filename, String mimeType){
        if(!haveValidExtension(filename)){
            filename += "." + getExtensionFromMime(mimeType);
        }
        return filename;
    }


    /**
     *
     * @param base64 base64 file
     * @param directoryPath directory where to save the file
     * @param filename file with extension
     * @return absolute filepath
     */
    public static String saveBase64IntoFile(String base64, String directoryPath, String filename){
        byte[] pdfAsBytes = Base64.decode(base64, Base64.DEFAULT);
        File file = new File(directoryPath, filename);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file, true);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


}
