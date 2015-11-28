package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// Contains helper methods that don't really belong anywhere else
// These are mostly copied and pasted from internet examples/stack overflow

public class Util {

    public static String[] readLines(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> lines = new ArrayList<String>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
            return lines.toArray(new String[lines.size()]);

        } catch (IOException e) {
            System.out.println("Error reading " + filename);
            System.out.println("Working Directory = " +
                    System.getProperty("user.dir"));
            System.out.println("ABANDON ALL HOPE (or make sure the file is in the correct path and retry)");
            System.exit(1);
        }
        return null;
    }

    public static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

}
