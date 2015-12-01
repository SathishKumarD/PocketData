/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.constants;

import java.io.File;

/**
 *
 * @author san
 */
public class AppConstants {
    public static String SRC_DIR = "/Users/san/UB/CSE-662/Project/DataSet/logcat";
    public static String DEST_FOLDER = "/Users/san/UB/CSE-662/Project/Run/OUTPUT";
    
    //public static String SRC_DIR = "/Users/san/UB/CSE-662/Project/DataSet/logcat";
    //public static String DEST_FOLDER = "/home/csgrad/sankarav/OUTPUT";
    
    public static final String OBJECTS_FOLDER = "OBJECTS";
    public static String ABS_OBJECTS_FOLDER = DEST_FOLDER + File.separatorChar + OBJECTS_FOLDER;
    public static final String DATA_FOLDER = "DATA";
    public static String ABS_DATA_FOLDER = DEST_FOLDER + File.separatorChar + DATA_FOLDER;
    
    public static boolean MODE_OBJECT_GEN;
    public static boolean MODE_ANALYTICS_GEN;
    public static boolean MODE_SCHEMA_GEN;
    
    public final static String SRC_LOG_FILE_EXT = ".gz";
    public final static String SRC_LOG_FILE_VALUE_SEPERATOR = "\t";
    
    public final static String OUTPUT_FILE_EXT = ".txt";
    public final static String OUTPUT_FILE_VALUE_SEPERATOR = "\t";
    public final static String OUTPUT_FILE_LINE_SEPERATOR = "\n";
    public final static boolean OUTPUT_FILE_WRITE_HEADER = true;
    
    public final static boolean CUMULATE_ANALYTICS_TO_PARENT = true;
    
    //Serialization constants
    public final static int OBJ_FILE_BUFFER_SIZE = 3000;
}
