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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author sathish
 */
public class LogAnalyzer {

    String sourceDir;
    String fileExtension;
    String logFilePath;
    HashMap<String, String> appNamesMap;

    public LogAnalyzer(String sourceDir, String fileExtension, String logFilePath) {
        this.sourceDir = sourceDir;
        this.fileExtension = fileExtension;
        this.logFilePath = logFilePath;
        this.appNamesMap = new HashMap<>();
    }

    public void parseLogsAndWriteFile() {
        ArrayList<String> files = getFiles();
        int counter = 0;
        for (String filePath : files) {
            //System.out.println(filePath);
            counter += 1;
            // remove this. For testing purpose I am just parsing two files.
            if (counter < 3) {
                accessGZfile(filePath);
            }
        }
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

    private void accessGZfile(String sourceFile) {

        Writer writer = null;
        int counter = 0;

        try {
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(sourceFile));
            BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
            String logLine = null;
            writer = new BufferedWriter(new FileWriter(this.logFilePath, true));
            System.out.println(sourceFile);
            while ((logLine = br.readLine()) != null) {
            	String appName = null;
                String[] segments = logLine.split("\t");
                // segments[0] - user id
                // segments[5] - process id
                // segments[6] - thread id
                String key = segments[0] + '_' + segments[5] + '_' + segments[6];
                if (logLine.contains("\"Action\":\"APP_NAME\"")) {
                    JSONParser parser = new JSONParser();
                    JSONObject obj = (JSONObject) parser.parse(segments[8]);
                    appName = (String) obj.get("AppName");
                    this.appNamesMap.put(key, appName);
                } else if ((appName = this.appNamesMap.get(key)) != null) {
                    
                    // I have an app name
                    writer.write(appName+"\t");
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
}
