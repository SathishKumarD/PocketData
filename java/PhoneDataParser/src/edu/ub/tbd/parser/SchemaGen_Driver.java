/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;

import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.constants.AppConstants;
import edu.ub.tbd.entity.Analytics;
import edu.ub.tbd.beans.TableBean;
import edu.ub.tbd.service.PersistanceFileService;
import edu.ub.tbd.service.PersistanceService;
import edu.ub.tbd.util.MacFileNameFilter;
import edu.ub.tbd.util.JavaObjectSerializerUtil;
import edu.ub.tbd.util.KryoObjectSerializerUtil;
import edu.ub.tbd.util.ObjectSerializerUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 *
 * @author san
 */
public class SchemaGen_Driver {
    private final PersistanceService ps_Analytics;
    private final ConcurrentLinkedQueue<ArrayList<LogData>> QUEUE= new ConcurrentLinkedQueue<>();
    private final boolean IS_SERIAL_MODE = false;
    
    //<app_id, <tbl_name, TableBean>>
    private final HashMap<Integer, HashMap<String, TableBean>> SCHEMAS = new HashMap<>();
    
    
    private final int READER_THREAD_COUNT = 4; //This is the only configurable parameter
    private volatile int THREAD_EXIT_COUNTER = READER_THREAD_COUNT;
    private final ExecutorService TASK_EXECUTOR = Executors.newFixedThreadPool(READER_THREAD_COUNT + 1); //This one additinal thread is Writer thread
    
    public SchemaGen_Driver() throws Exception {
        this.ps_Analytics = new PersistanceFileService(AppConstants.ABS_DATA_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, Analytics.class);
    }
 
    public void run() throws Exception{
        if(IS_SERIAL_MODE){
            run_sequential();
        } else {
            run_parallel();
        }
    }
    
    private void run_sequential() throws Exception{
//        ObjectSerializerUtil serializer = new JavaObjectSerializerUtil();
        ObjectSerializerUtil serializer = new KryoObjectSerializerUtil();
        
        File obj_folder = new File(AppConstants.ABS_OBJECTS_FOLDER);
        List<File> files = getSortedFilesInAFolder_Integer_Sort(obj_folder);
        for(File f : files){
            ArrayList<LogData> lds = serializer.read(f);
            processAndWriteEachBatch(lds);
        }
    }
    
    private void run_parallel() throws Exception{
        for(int i = 0; i < READER_THREAD_COUNT; i++){
            TASK_EXECUTOR.execute(new Reader(i, READER_THREAD_COUNT));
        }
        
        TASK_EXECUTOR.execute(new Writer());
        
        TASK_EXECUTOR.shutdown();
        
        while(!TASK_EXECUTOR.isTerminated()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    class Reader implements Runnable{

        private final File OBJECTS = new File(AppConstants.ABS_OBJECTS_FOLDER);
        private final int runEach;
        private final int TOTAL_THREAD_COUNT;
//        private final ObjectSerializerUtil serializer = new JavaObjectSerializerUtil();
        private final ObjectSerializerUtil serializer = new KryoObjectSerializerUtil();
        
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
                        synchronized(QUEUE){
                            if(QUEUE.size() > (100 + (runEach * 10))){
                                QUEUE.wait();
                            }
                        }
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
            //System.out.println("Thread - " + runEach + " : Reading file > " + _f.getName());
            ArrayList<LogData> lds = serializer.read(_f);
            QUEUE.add(lds);
        }
    }
    
    class Writer implements Runnable{
        boolean isProcessingComplete = false;
        int counter;
        
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
                    ArrayList<ArrayList<LogData>> ldss = new ArrayList<>();
                    while(!QUEUE.isEmpty()){
                        ldss.add(QUEUE.poll());
                        //System.out.println("Thread - Writer : Polling " + counter++);
                    }
                    try {
                        for(ArrayList<LogData> lds : ldss){
                            processAndWriteEachBatch(lds);
                        }
                        synchronized(QUEUE){
                            if(QUEUE.size() < 25){
                                QUEUE.notifyAll();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            System.out.println("Thread-Writer: completed processing all files. Bye!!!");
        }
    }
    
    private void processAndWriteEachBatch(final ArrayList<LogData> _lds) throws Exception{
        //System.out.println("Thread - Writer : Processing processAndWriteEachBatch()");
        for(LogData ld : _lds){
            parseEachLogData(ld);
        }
    }
    
    private void parseEachLogData(LogData _ld) throws Exception{
        SchemaGen schemaGen = new SchemaGen(_ld);

        HashMap<String, TableBean> extractedSchema = schemaGen.generate();
        updateSCHEMAS(extractedSchema, _ld.getApp_id());
    }
    
    private void updateSCHEMAS(final HashMap<String, TableBean> _extractedSchema, 
            final int _app_id)
    {
        //TODO: <Sankar> write code to update the SCHEMAS
        HashMap<String, TableBean> baseSchemaOfApp = SCHEMAS.get(_app_id);
        if(baseSchemaOfApp == null){
            baseSchemaOfApp = new HashMap<>();
            SCHEMAS.put(_app_id, baseSchemaOfApp);
        }
        
        updateSCHEMAS(baseSchemaOfApp, _extractedSchema);
    }
    
    private void updateSCHEMAS(final HashMap<String, TableBean> _baseSchemaOfApp, 
            final HashMap<String, TableBean> _extractedSchema)
    {
        for(TableBean tbl : _extractedSchema.values()){
            updateSCHEMAS(_baseSchemaOfApp, tbl);
        }
    }
    
    private void updateSCHEMAS(final HashMap<String, TableBean> _baseSchemaOfApp, 
            final TableBean _extractedTbl)
    {
        TableBean baseTbl = _baseSchemaOfApp.get(_extractedTbl.getTbl_name());
        if(baseTbl != null){
            baseTbl.addAllColumns(_extractedTbl.getColumns().values());
        } else {
            _baseSchemaOfApp.put(_extractedTbl.getTbl_name(), _extractedTbl);
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
    
    public void shutDown(){
        //TODO: <Sankar> write if any shutdown Logic here
    }
    
}