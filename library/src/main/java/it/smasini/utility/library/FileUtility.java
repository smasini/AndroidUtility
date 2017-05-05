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

    public static String getMimeTypeFromExtension(String extension){
        switch (extension){
            case "pdf":
                return "application/pdf";
            case "doc":
            case "dot":
            case "word":
                return  "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "dotx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
            case "docm":
                return "application/vnd.ms-word.document.macroEnabled.12";
            case "dotm":
                return "application/vnd.ms-word.template.macroEnabled.12";
            case "xls":
            case "xlt":
            case "xla":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "xltx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
            case "xlsm":
                return "application/vnd.ms-excel.sheet.macroEnabled.12";
            case "xltm":
                return "application/vnd.ms-excel.template.macroEnabled.12";
            case "xlam":
                return "application/vnd.ms-excel.addin.macroEnabled.12";
            case "xlsb":
                return "application/vnd.ms-excel.sheet.binary.macroEnabled.12";
            case "ppt":
            case "pot":
            case "pps":
            case "ppa":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "potx":
                return "application/vnd.openxmlformats-officedocument.presentationml.template";
            case "ppsx":
                return "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
            case "ppam":
                return "application/vnd.ms-powerpoint.addin.macroEnabled.12";
            case "pptm":
                return "application/vnd.ms-powerpoint.presentation.macroEnabled.12";
            case "potm":
                return "application/vnd.ms-powerpoint.template.macroEnabled.12";
            case "ppsm":
                return "application/vnd.ms-powerpoint.slideshow.macroEnabled.12";
            case "mdb":
                return "application/vnd.ms-access";
            case "avi":
                return "video/avi";
            case "bmp":
                return "image/bmp";
            case "gif":
                return "image/gif";
            case "html":
                return "text/html";
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            case "mov":
                return "video/quicktime";
            case "mp3":
                return "audio/mpeg3";
            case "tiff":
            case "tif":
                return "image/tiff";
            case "xml":
                return "text/xml";
            case "png":
                return "image/png";
            case "mpeg":
                return "video/mpeg";
            case "txt":
            case "text":
            case "log":
            default:
                return "text/plain";
        }
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
