/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import edu.ub.tbd.constants.AppConstants;
import java.util.ArrayList;
import edu.ub.tbd.parser.LogParser;

/**
 * This class is not user anymore.
 * @deprecated Main calls LogParser directly
 * @author sathish
 */
public class TestParser {
	
    /*
    public static void test_getFiles() {
        LogParser logger = new LogParser(AppConstants.SRC_DIR, 
                                                AppConstants.SRC_LOG_FILE_EXT, 
                                                AppConstants.DEST_FILE);
        
        ArrayList<String> filePaths = logger.getFiles();

        if(filePaths != null) {
            for(String path : filePaths) {
                System.out.println(path);
            }
        }
    }
    */
    
    
    public static void testParseLogsAndWriteFile() throws Exception{
        LogParser logger = new LogParser(AppConstants.SRC_DIR, 
                                                AppConstants.SRC_LOG_FILE_EXT, 
                                                AppConstants.DEST_FILE);
        
        logger.parseLogsAndWriteFile();
    }

}
