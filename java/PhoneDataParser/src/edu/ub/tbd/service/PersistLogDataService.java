/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.service;

import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.constants.AppConstants;
import edu.ub.tbd.util.ObjectSerializerUtil;
import java.util.ArrayList;
import static edu.ub.tbd.constants.AppConstants.OBJ_FILE_BUFFER_SIZE;
import java.io.File;

/**
 *
 * @author san
 */
public class PersistLogDataService {
    private final String OUTPUT_FOLDER = AppConstants.ABS_OBJECTS_FOLDER;
    private final BufferedLogDataWriter writer = new BufferedLogDataWriter();
    private final int bufferSize;
    
    @Deprecated
    //TODO: <Sankar> Make this private after JUnit Test is complete
    public PersistLogDataService(int _bufferSize){
        this.bufferSize = _bufferSize;
    }
    
    public PersistLogDataService(){
        this(50000); //Cache issue of AppConstants final static values
    }
    
    public void write(LogData _obj){
        writer.add(_obj);
        if(bufferSize == writer.totalObjsInBuffer()){
            flush();
        }
    }
    
    public void close(){
        flush();
    }
    
    private void flush(){
        writer.flush(OUTPUT_FOLDER);
    }
    
    class BufferedLogDataWriter{
        private ArrayList<LogData> OBJ_COLLECTION;
        private int totalObjsInBuffer; //Hypothetically OBJ_COLLECTION.size() should work but it isnt. Hence dont change this
        private int totalFilesWritten;

        public BufferedLogDataWriter(){
            OBJ_COLLECTION = new ArrayList<>(bufferSize);
        }

        public void add(LogData _obj){
            OBJ_COLLECTION.add(_obj);
            totalObjsInBuffer++;
        }

        private void flush(String _folderName){
            if(OBJ_COLLECTION != null && !OBJ_COLLECTION.isEmpty()){
                String fileName = _folderName + File.separatorChar + totalFilesWritten++;
                ObjectSerializerUtil.serialize(fileName, OBJ_COLLECTION);
                OBJ_COLLECTION = new ArrayList<>(bufferSize);
                totalObjsInBuffer = 0;
            }
        }
        
        public int totalObjsInBuffer(){
            return totalObjsInBuffer;
        }

    }
}
