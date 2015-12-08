/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd;

import edu.ub.tbd.constants.AppConstants;
import edu.ub.tbd.parser.AnalyticsGen_Driver;
import edu.ub.tbd.parser.LogParser;
import edu.ub.tbd.parser.SchemaGen_Driver;

import java.io.File;
import java.io.IOException;

/**
 * This is the driver class of the Phone Data Parser when invoked using jar or command line
 * 
 * @author sathish
 */
public class Main {

    /**
     * @param args the command line arguments [-v|V] [-help] [-p "mode"]
     */
    public static void main(String[] args) {
        extractCommandLineArgs(args);
        
        if(startUp()){
            if(AppConstants.MODE_OBJECT_GEN){
                System.out.println("Running PhoneDataParser in \"OBJECT_GEN\" mode");
                LogParser logParser = null;
                try {
                    String srcDIR = AppConstants.SRC_DIR; //Leave this. It helps in reducing Class unload cache. Talk to Sankar before u remove this
                    logParser = new LogParser(srcDIR, AppConstants.SRC_LOG_FILE_EXT);
                    logParser.parseLogsAndWriteFile();
                } catch (Throwable e) {
                    System.out.println("Log Parsing and Object generation incomplete");
                    e.printStackTrace();
                } finally {
                    try {
                        shutDown(logParser);
                    } catch (Exception e) {
                        System.out.println("Unable to shutdown LogParser");
                        e.printStackTrace();
                    }
                }
            } else if(AppConstants.MODE_ANALYTICS_GEN) {
                System.out.println("Running PhoneDataParser in \"ANALYTICS_GEN\" mode");
                AppConstants.SRC_DIR = AppConstants.ABS_OBJECTS_FOLDER;
                AnalyticsGen_Driver analyticsGenDriver = null;
                try {
                    analyticsGenDriver = new AnalyticsGen_Driver();
                    analyticsGenDriver.run();
                } catch (Exception ex) {
                    System.out.println("Data generation incomplete");
                    ex.printStackTrace();
                } finally {
                    try {
                        shutDown(analyticsGenDriver);
                    } catch (Exception e) {
                        System.out.println("Unable to shutdown DataGenDriver");
                        e.printStackTrace();
                    }
                }
                
            } else {
                System.out.println("Running PhoneDataParser in \"SCHEMA_GEN\" mode");
                AppConstants.SRC_DIR = AppConstants.ABS_OBJECTS_FOLDER;
                SchemaGen_Driver schemaGenDriver = null;
                
                try {
                    schemaGenDriver = new SchemaGen_Driver();
                    schemaGenDriver.run();
                } catch (Exception ex) {
                    System.out.println("Schema generation incomplete");
                    ex.printStackTrace();
                } finally {
                    try {
                        shutDown(schemaGenDriver);
                    } catch (Exception e) {
                        System.out.println("Unable to shutdown SchemaGenDriver");
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Start up of the application failed");
        }
    }
    
    
    private static void extractCommandLineArgs(String[] _args){
        for(int i = 0; i < _args.length; i++){
            if (_args[i].equals("--src") || _args[i].equals("-s")) {
            	String sourceDir = _args[++i];
                AppConstants.SRC_DIR = sourceDir;
            }
            if (_args[i].equals("--dest") || _args[i].equals("-d")) {
            	String destFolder = _args[++i];
                AppConstants.DEST_FOLDER = destFolder;
                AppConstants.ABS_OBJECTS_FOLDER = AppConstants.DEST_FOLDER + File.separatorChar + AppConstants.OBJECTS_FOLDER;
                AppConstants.ABS_DATA_FOLDER = AppConstants.DEST_FOLDER + File.separatorChar + AppConstants.DATA_FOLDER;
            }
            if (_args[i].equals("--mode") || _args[i].equals("-m")) {
            	String mode = _args[++i];
                if(mode.equalsIgnoreCase("obj_gen") || mode.equalsIgnoreCase("o")){
                    AppConstants.MODE_OBJECT_GEN = true;
                    AppConstants.MODE_ANALYTICS_GEN = false;
                    AppConstants.MODE_SCHEMA_GEN = false;
                } else if(mode.equalsIgnoreCase("analytics_gen") || mode.equalsIgnoreCase("a")) {
                    AppConstants.MODE_OBJECT_GEN = false;
                    AppConstants.MODE_ANALYTICS_GEN = true;
                    AppConstants.MODE_SCHEMA_GEN = false;
                } else {
                    AppConstants.MODE_OBJECT_GEN = false;
                    AppConstants.MODE_ANALYTICS_GEN = false;
                    AppConstants.MODE_SCHEMA_GEN = true;
                }
            }
            if(_args[i].equals("--objects") || _args[i].equals("-o")){
                String object_gen_type = _args[++i];
                if(object_gen_type.equalsIgnoreCase("full") || object_gen_type.equalsIgnoreCase("f")){
                    AppConstants.OBJECTS_GEN_FILTER_FULL = true;
                    AppConstants.OBJECTS_GEN_FILTER_FORSCHEMAGEN = false;
                } else if(object_gen_type.equalsIgnoreCase("schemagen") || object_gen_type.equalsIgnoreCase("s")){
                    AppConstants.OBJECTS_GEN_FILTER_FULL = false;
                    AppConstants.OBJECTS_GEN_FILTER_FORSCHEMAGEN = true;
                } else {
                    AppConstants.OBJECTS_GEN_FILTER_FULL = true;
                    AppConstants.OBJECTS_GEN_FILTER_FORSCHEMAGEN = false;
                }
            }
        }
    }
    
    /**
     * Makes sure the required folder setup for the source and destination file exist. If destination file absent, this creates it
     * 
     * @return true  - if start up is successful <br> 
     *         false - if start up fails. Application should terminate if false
     * 
     * @throws java.lang.Exception 
     */
    public static boolean startUp(){
        
        File destinationFolder = new File(AppConstants.DEST_FOLDER);
        if(!destinationFolder.exists()){
            boolean out = destinationFolder.mkdirs();
            if(!out){
                System.out.println("Unable to create folder : " + AppConstants.DEST_FOLDER);
                return out;
            }
        }
        
        if(AppConstants.MODE_OBJECT_GEN){
            File objectsFolder = new File(AppConstants.DEST_FOLDER + File.separatorChar + AppConstants.OBJECTS_FOLDER);
            if(objectsFolder.exists()){
                System.out.println("OBJECTS folder already exists. Delete it and re-run");
                return false;
            } else {
                boolean out = objectsFolder.mkdir();
                if(!out){
                    System.out.println("Unable to create OBJECTS directory");
                    return out;
                }
            }
            
            try {
                if(AppConstants.OBJECTS_GEN_FILTER_FORSCHEMAGEN){
                    new File(AppConstants.DEST_FOLDER + File.separatorChar + AppConstants.OBJECTS_FOLDER + File.separatorChar + AppConstants.OBJECTS_GEN_FILTER_FORSCHEMAGEN_FILENAME).createNewFile();
                } else if(AppConstants.OBJECTS_GEN_FILTER_FORSCHEMAGEN){
                    new File(AppConstants.DEST_FOLDER + File.separatorChar + AppConstants.OBJECTS_FOLDER + File.separatorChar + AppConstants.OBJECTS_GEN_FILTER_FULL_FILENAME).createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            
        }
        
        if(AppConstants.MODE_ANALYTICS_GEN || AppConstants.MODE_SCHEMA_GEN){
            File dataFolder = new File(AppConstants.DEST_FOLDER + File.separatorChar + AppConstants.DATA_FOLDER);
            if(dataFolder.exists()){
                System.out.println("DATA folder already exists. Delete it and re-run");
                return false;
            } else {
                boolean out = dataFolder.mkdir();
                if(!out){
                    System.out.println("Unable to create DATA directory");
                    return out;
                }
            }
        }
        
        if(AppConstants.MODE_ANALYTICS_GEN){
            if(!new File(AppConstants.DEST_FOLDER + File.separatorChar + AppConstants.OBJECTS_FOLDER + File.separatorChar + AppConstants.OBJECTS_GEN_FILTER_FULL_FILENAME).exists()){
                System.out.println("OBJECTS folder does not contain full/all OBJECTS, failing ANALYTICS Generation");
                return false;
            }
        }
            
        
        return true;
    }
    
    public static void shutDown(LogParser _logParser) throws Exception{
        if(_logParser != null){
            _logParser.shutDown();
        }
    }
    
    public static void shutDown(AnalyticsGen_Driver _dataGen) throws Exception{
        if(_dataGen != null){
            _dataGen.shutDown();
        }
    }
    
    public static void shutDown(SchemaGen_Driver _schemaGenDriver) throws Exception{
        if(_schemaGenDriver != null){
            _schemaGenDriver.shutDown();
        }
    }
    
    /*
     * ##############################################################################
     *   All the below are back-up methods to are TO-BE deleted when decided unfit
     * ##############################################################################
     */
    
    /**
     * @deprecated 
     * @param files list of files
     */
    public static void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles()); // Calls same method again.
            } else {
                System.out.println("File: " + file.getName());
            }
        }
    }
    
}