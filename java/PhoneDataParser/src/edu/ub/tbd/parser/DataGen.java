/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;

import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.constants.AppConstants;
import edu.ub.tbd.entity.Analytics;
import edu.ub.tbd.entity.Sql_log;
import edu.ub.tbd.service.PersistanceFileService;
import edu.ub.tbd.service.PersistanceService;
import edu.ub.tbd.util.MacFileNameFilter;
import edu.ub.tbd.util.ObjectSerializerUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author san
 */
public class DataGen {
    private final PersistanceService ps_SqlLog;
    private final PersistanceService ps_Analytics;
    private final ConcurrentLinkedQueue<ArrayList<LogData>> QUEUE= new ConcurrentLinkedQueue<>();
    private final int THREAD_COUNTER = 12;
    private volatile int THREAD_EXIT_COUNTER = THREAD_COUNTER - 1;
    private final ExecutorService taskExecutor = Executors.newFixedThreadPool(THREAD_COUNTER);
    
    public DataGen() throws Exception {
        this.ps_SqlLog = new PersistanceFileService(AppConstants.ABS_DATA_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, Sql_log.class);
        
        this.ps_Analytics = new PersistanceFileService(AppConstants.ABS_DATA_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, Analytics.class);
    }
 
    public void run() throws Exception{
        File obj_folder = new File(AppConstants.ABS_OBJECTS_FOLDER);
        List<File> users_Folder = getSortedFilesInAFolder(obj_folder);
        
        for(File user_folder : users_Folder){
            taskExecutor.execute(new Reader(user_folder));
        }
        
        taskExecutor.execute(new Writer());
        
        taskExecutor.shutdown();
        
        while(!taskExecutor.isTerminated()){
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    class Reader implements Runnable{
        private final File userFolder;
        
        public Reader(File _userFolder){
            this.userFolder = _userFolder;
        }
        
        @Override
        public void run() {
            List<File> files = getSortedFilesInAFolder(userFolder);
            for(File f : files){
                try {
                    readEachFile(f);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            THREAD_EXIT_COUNTER--;
            System.out.println("Read all files for user ==> " + userFolder.getName());
        }
        
        private void readEachFile(File _f) throws Exception{
            ArrayList<LogData> lds = ObjectSerializerUtil.read(_f);
            QUEUE.add(lds);
        }
    }
    
    class Writer implements Runnable{
        boolean isProcessingComplete = false;
        
        @Override
        public void run() {
            System.out.println("Thread-Writer: STARTED. Hello!!!");
            
            while(!isProcessingComplete){
                if(QUEUE.isEmpty()){
                    if(THREAD_EXIT_COUNTER < 1){
                        isProcessingComplete = true;
                        break;
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    ArrayList<LogData> lds = QUEUE.poll();
                    try {
                        processAndWriteEachBatch(lds);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            System.out.println("Thread-Writer: completed processing all files. Bye!!!");
        }
        
        private void processAndWriteEachBatch(final ArrayList<LogData> _lds) throws Exception{
            for(LogData ld : _lds){
                parseEachLogData(ld);
            }
        }
        
        private void parseEachLogData(LogData _ld) throws Exception{
            Sql_log sql_log = new Sql_log(_ld);
            ps_SqlLog.write(sql_log);

            AnalyticsGen analyticsGen = new AnalyticsGen(_ld);

            ArrayList<Analytics> list_analytics = analyticsGen.generate();
            for(Analytics analytics : list_analytics){
                ps_Analytics.write(analytics);
            }
        }
        
    }
    
    private List<File> getSortedFilesInAFolder(File _folder){
        File [] arr_files = _folder.listFiles(new MacFileNameFilter());
        List<File> list_files = Arrays.asList(arr_files);
        return list_files;
    }
    
    
    
    public void shutDown() throws Exception{
        ps_SqlLog.close();
        ps_Analytics.close();
    }
}
