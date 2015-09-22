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
	
	private static String srcDir = "C:\\Users\\Saravanan\\Downloads\\DB - 662\\project\\output";
	private static String outFilePath = "/Users/sathish/Dropbox/UB_Fall_2015/662- DB/code/sql/data/insert_analytics_data.sql";
	
    public static void test_getFiles() {
        Logger logger = new Logger(srcDir, ".gz", outFilePath);
        ArrayList<String> filePaths = logger.getFiles();

        if(filePaths != null) {
        	for(String path : filePaths) {
        		System.out.println(path);
            }
        }
        
    }
    
    public static void testParseLogsAndWriteFile() {
        Logger logger = new Logger(srcDir, ".gz", outFilePath);
        logger.parseLogsAndWriteFile();
    }

}
