package presentation;

import java.io.*;

public class FileWriter {
    public FileWriter(){

    }
    public void write(String fileName,String toWrite){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println(toWrite);
        writer.close();
    }
}
