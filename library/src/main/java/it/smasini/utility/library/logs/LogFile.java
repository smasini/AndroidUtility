package it.smasini.utility.library.logs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Simone on 27/02/17.
 */

public class LogFile {

    private static LogFile instance;

    public static void init(String url){
        instance = new LogFile(url);
    }

    public static LogFile getInstance(){
        if(instance == null)
            throw new RuntimeException("Non è stato chiamato il methode init");
        return instance;
    }

    private FileOutputStream outputStream;
    private OutputStreamWriter myOutWriter;
    private String urlFile = "";

    private LogFile(String url){
        urlFile = url;//FolderUtility.getApplicationFilePath() + "/logfile.txt";
        File file = new File(urlFile);
        if(!file.exists()) {
            try {
                boolean created = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openFile(){
        File file = new File(urlFile);
        try {
            outputStream = new FileOutputStream(file);
            myOutWriter = new OutputStreamWriter(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveFile(){
        try {
            if(myOutWriter!=null)
                myOutWriter.close();
            if(outputStream!=null)
                outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            myOutWriter = null;
            outputStream = null;
        }

    }

    public void writeLine(String text){
        try {
            if(myOutWriter!=null)
                myOutWriter.append(text + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeTimestamp(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        writeLine(format.format(new Date()));
    }

    public void writeSeparator(){
        writeLine("##################################################\n");
    }

    public void writeReturn(){
        writeLine("\n");
    }

}
