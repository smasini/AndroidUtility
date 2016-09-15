package it.smasini.utility.library.compression;

import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Simone on 14/09/16.
 */
public class GZipHelper {

    public static byte[] compress(String string) {
        try{
            ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
            GZIPOutputStream gos = new GZIPOutputStream(os);
            gos.write(string.getBytes());
            gos.close();
            byte[] compressed = os.toByteArray();
            os.close();
            return compressed;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decompress(byte[] compressed) {
        final int BUFFER_SIZE = 32;
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(compressed);
            GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);

            StringBuilder string = new StringBuilder();
            byte[] data = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = gis.read(data)) != -1) {
                string.append(new String(data, 0, bytesRead));
            }
            gis.close();
            is.close();
            return string.toString();
        }catch (IOException e) {
                e.printStackTrace();
        }
        return "";
    }

    public static String decompress(InputStream inputStream){
        final int BUFFER_SIZE = 32;
        try {
            GZIPInputStream stream = new GZIPInputStream(inputStream);
            StringBuilder string = new StringBuilder();
            byte[] data = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = stream.read(data)) != -1) {
                string.append(new String(data, 0, bytesRead));
            }
            stream.close();
            return string.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void decompressInFile(InputStream stream, String path){
        try {
            stream = new GZIPInputStream(stream);
            InputSource is = new InputSource(stream);
            InputStream input = new BufferedInputStream(is.getByteStream());
            OutputStream output = new FileOutputStream(path);
            byte data[] = new byte[2097152];
            long total = 0;
            int count;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (BufferOverflowException | IOException e) {
            e.printStackTrace();
        }
    }




}
