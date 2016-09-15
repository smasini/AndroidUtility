package it.smasini.utility.library.compression;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Simone on 14/09/16.
 */
public class ZipHelper {

    public static void decompress(InputStream inputStream, String path){
        try {
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(inputStream));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;
            String filename;
            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();
                int i = filename.indexOf("/");
                if(i>=0){
                    File create = new File(path + "/" + filename.substring(0,i)+"/");
                    if(!create.exists() || !create.isDirectory()){
                        create.mkdir();
                    }
                }
                FileOutputStream fout = new FileOutputStream(path + filename);
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }
                fout.close();
                zis.closeEntry();
            }
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
