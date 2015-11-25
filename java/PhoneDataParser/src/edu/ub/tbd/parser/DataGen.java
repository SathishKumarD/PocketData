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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *
 * @author san
 */
public class DataGen {
    //private final PersistanceService ps_SqlLog;
    private final PersistanceService ps_Analytics;
    private final ConcurrentLinkedQueue<ArrayList<LogData>> QUEUE= new ConcurrentLinkedQueue<>();
    
    private final int READER_THREAD_COUNT = 24; //This is the only configurable parameter
    private volatile int THREAD_EXIT_COUNTER = READER_THREAD_COUNT;
    private final ExecutorService TASK_EXECUTOR = Executors.newFixedThreadPool(READER_THREAD_COUNT + 1); //This one additinal thread is Writer thread
    
    public DataGen() throws Exception {
        /*
        this.ps_SqlLog = new PersistanceFileService(AppConstants.ABS_DATA_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, Sql_log.class);
        */
        
        this.ps_Analytics = new PersistanceFileService(AppConstants.ABS_DATA_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, Analytics.class);
    }
 
    public void run() throws Exception{
        
        for(int i = 0; i < READER_THREAD_COUNT; i++){
            TASK_EXECUTOR.execute(new Reader(i, READER_THREAD_COUNT));
        }
        
        TASK_EXECUTOR.execute(new Writer());
        
        TASK_EXECUTOR.shutdown();
        
        while(!TASK_EXECUTOR.isTerminated()){
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    class Reader implements Runnable{

        private final File OBJECTS = new File(AppConstants.ABS_OBJECTS_FOLDER);
        private final int runEach;
        private final int TOTAL_THREAD_COUNT;
        
        public Reader(int _runEach, int _totalThreadCount){
            this.runEach = _runEach;
            this.TOTAL_THREAD_COUNT = _totalThreadCount;
        }
        
        @Override
        public void run() {
            System.out.println("Thread started for ==> " + runEach);
            List<File> files = getSortedFilesInAFolder_Integer_Sort(OBJECTS);
            for(File f : files){
                if((Integer.parseInt(f.getName()) % TOTAL_THREAD_COUNT) == runEach){
                    //System.out.println("Thread - " + runEach + " : Reading File ==> " + f.getName());
                    try {
                        readEachFile(f);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                
            }
            THREAD_EXIT_COUNTER--;
            System.out.println("Read all files for ==> " + runEach);
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
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    ArrayList<ArrayList<LogData>> ldss = new ArrayList<>();
                    while(!QUEUE.isEmpty()){
                        ldss.add(QUEUE.poll());
                    }
                    try {
                        for(ArrayList<LogData> lds : ldss){
                            processAndWriteEachBatch(lds);
                        }
                        
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
            //Sql_log sql_log = new Sql_log(_ld);
            //ps_SqlLog.write(sql_log);

            AnalyticsGen analyticsGen = new AnalyticsGen(_ld);

            ArrayList<Analytics> list_analytics = analyticsGen.generate();
            for(Analytics analytics : list_analytics){
                ps_Analytics.write(analytics);
            }
        }
        
    }
    
    private static List<File> getSortedFilesInAFolder_Integer_Sort(File _folder){
        File [] arr_files = _folder.listFiles(new MacFileNameFilter());
        List<File> list_files = Arrays.asList(arr_files);
        Collections.sort(list_files, new Comparator<File>() {
                                            public int compare(File f1, File f2) { return new Integer(f1.getName()).compareTo(new Integer(f2.getName())); } 
                                            public boolean equals(Object obj) { return this.equals(obj); }
                                        } );
        return list_files;
    }
    
    public void shutDown() throws Exception{
        //ps_SqlLog.close();
        ps_Analytics.close();
    }
    
    public static void main(String[] args) {
        List<File> files = getSortedFilesInAFolder_Integer_Sort(new File(AppConstants.ABS_OBJECTS_FOLDER));
        for(File f : files){
            System.out.println(f.getName());
        }
        
    }
}
