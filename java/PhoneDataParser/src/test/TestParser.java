/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import phonedataparser.Logger;

/**
 *
 * @author sathish
 */
public class TestParser {

    public static void test_getFiles() {
    	//test commit
        String srcDir = "/Users/sathish/Desktop/logcat";
        String logFilePath = "/Users/sathish/Dropbox/UB_Fall_2015/662- DB/code/sql/data/insert_analytics_data.sql";
        Logger logger = new Logger(srcDir, ".gz", logFilePath);
        ArrayList<String> filePaths = logger.getFiles();
        
        filePaths.stream().forEach((filePath) -> {
            System.out.println(filePath);
        });
        
        
    }
    
    public static void test_writeToLog() {
        String srcDir = "/Users/sathish/Desktop/logcat";
        String logFilePath = "/Users/sathish/Dropbox/UB_Fall_2015/662- DB/code/sql/data/insert_analytics_data.sql";
        Logger logger = new Logger(srcDir, ".gz", logFilePath);
        logger.writeToLog();
        
        
    }

}
