/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonedataparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author sathish
 */
public class Logger {

    String sourceDir;
    String fileExtension;
    String logFilePath;
    HashMap<String, String> appname;

    public Logger(String sourceDir, String fileExtension, String logFilePath) {
        this.sourceDir = sourceDir;
        this.fileExtension = fileExtension;
        this.logFilePath = logFilePath;
        this.appname = new HashMap<>();
    }

    public ArrayList<String> getFiles() {
        ArrayList<String> filesList = new ArrayList<>();
        File[] files = new File(sourceDir).listFiles();
        iterateFolders(files, filesList);
        return filesList;
    }

    private void iterateFolders(File[] files, ArrayList<String> filesList) {
        for (File file : files) {
            String name = file.getName();
            if (file.isDirectory()) {
                iterateFolders(file.listFiles(), filesList); // Calls same method again.
            } else if (name.endsWith(fileExtension) && file.getAbsolutePath().contains("SQLite-Query-PhoneLab")) {

                filesList.add(file.getAbsolutePath());
            }
        }
    }

    public void writeToLog() {
        ArrayList<String> files = getFiles();
        int counter = 0;
        for (String filePath : files) {
            //System.out.println(filePath);
            counter += 1;
            if (counter < 3) {
                accessGZfile(filePath);
            }
        }

    }

    private void accessGZfile(String sourceFile) {

        Writer writer = null;
        int counter = 0;

        try {
            String appname = null;
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(sourceFile));
            BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
            String logLine = null;
            writer = new BufferedWriter(new FileWriter(this.logFilePath, true));
            System.out.println(sourceFile);
            while ((logLine = br.readLine()) != null) {
                String[] segments = logLine.split("\t");
                String key = segments[0] + '_' + segments[5] + '_' + segments[6];
                if (logLine.contains("\"Action\":\"APP_NAME\"")) {
                    JSONParser parser = new JSONParser();
                    JSONObject obj = (JSONObject) parser.parse(segments[8]);
                    String appName = (String) obj.get("AppName");
                    this.appname.put(key, appName);
                } else if ((appname = this.appname.get(key)) != null) {
                    
                    // I have an app name
                    writer.write(appname+"\t");
                    writer.write(logLine);
                    writer.write("\n");

                } else {
                    
                    // :( I don't have an appname
                    writer.write("\n");
                }
            }

            writer.close();

        } catch (Exception e) {

        } finally {

        }
    }

    private String getAppNameFromHash(String userID, String processID, String threadID) {
        String key = userID + "_" + processID + "_" + threadID;
        return this.appname.get(key);
    }

    private void storeAppname(String userID, String processID, String threadID, String appname) {
        String key = userID + "_" + processID + "_" + threadID;
        this.appname.put(key, appname);
    }

}
