/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.service;

import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.constants.AppConstants;
import edu.ub.tbd.util.MacFileNameFilter;
import edu.ub.tbd.util.ObjectSerializerUtil;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author san
 */
public class PersistLogDataServiceTest {
    
    public PersistLogDataServiceTest() {
    }
    
//    @Before
    public void setUp(){
        cleanUpOBJECTS_FOLDER();
    }
    
//    @After
    public void tearDown(){
        cleanUpOBJECTS_FOLDER();
    }
    
    private void cleanUpOBJECTS_FOLDER(){
        File OBJECTS_FOLDER = new File(AppConstants.ABS_OBJECTS_FOLDER);
        try {
            for(File f : OBJECTS_FOLDER.listFiles()){
                FileUtils.forceDelete(f);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testWrite_1LogDataBean_1User_NumOfBeansMultipleOfBufferSize() {
        ArrayList<LogData> logDataToWrite = generateDummyLogDataBeans(1);
        PersistLogDataService ld_Service = new PersistLogDataService(1);
        
        for(LogData ld: logDataToWrite){
            ld_Service.write(ld);
        }
        
        ArrayList<LogData> actualData = getActualSerializedData();
        
        LogData[] expected = new LogData[logDataToWrite.size()];
        logDataToWrite.toArray(expected);
        
        LogData[] actual = new LogData[logDataToWrite.size()];
        actualData.toArray(actual);
        
        assertArrayEquals("Input and output doesnt match", expected, actual);
    }
    
    @Test
    public void testWrite_10LogDataBean_1User_NumOfBeansMultipleOfBufferSize() {
        ArrayList<LogData> logDataToWrite = generateDummyLogDataBeans(10);
        PersistLogDataService ld_Service = new PersistLogDataService(5);
        
        for(LogData ld: logDataToWrite){
            ld_Service.write(ld);
        }
        
        ArrayList<LogData> actualData = getActualSerializedData();
        
        LogData[] expected = new LogData[logDataToWrite.size()];
        logDataToWrite.toArray(expected);
        
        LogData[] actual = new LogData[logDataToWrite.size()];
        actualData.toArray(actual);
        
        assertArrayEquals("Input and output doesnt match", expected, actual);
    }
    
    @Test
    public void testWrite_10LogDataBean_2User_NumOfBeansMultipleOfBufferSize() {
        ArrayList<LogData> logDataToWrite = generateDummyLogDataBeans(10, 0, 1);
        PersistLogDataService ld_Service = new PersistLogDataService(5);
        
        for(LogData ld: logDataToWrite){
            ld_Service.write(ld);
        }
        
        ArrayList<LogData> actualData = getActualSerializedData();
        
        LogData[] expected = new LogData[logDataToWrite.size()];
        logDataToWrite.toArray(expected);
        
        LogData[] actual = new LogData[logDataToWrite.size()];
        actualData.toArray(actual);
        
        assertArrayEquals("Input and output doesnt match", expected, actual);
    }
    
    @Test
    public void testWrite_11LogDataBean_1User_NumOfBeansNotMultipleOfBufferSize() {
        ArrayList<LogData> logDataToWrite = generateDummyLogDataBeans(11);
        PersistLogDataService ld_Service = new PersistLogDataService(3);
        
        for(LogData ld: logDataToWrite){
            ld_Service.write(ld);
        }
        
        ld_Service.close();
        
        ArrayList<LogData> actualData = getActualSerializedData();
        
        LogData[] expected = new LogData[logDataToWrite.size()];
        logDataToWrite.toArray(expected);
        
        LogData[] actual = new LogData[logDataToWrite.size()];
        actualData.toArray(actual);
        
        assertArrayEquals("Input and output doesnt match", expected, actual);
    }
    
    @Test
    public void testWrite_11LogDataBean_2Users_NumOfBeansNotMultipleOfBufferSize() {
        ArrayList<LogData> logDataToWrite = generateDummyLogDataBeans(11, 0, 1);
        PersistLogDataService ld_Service = new PersistLogDataService(3);
        
        for(LogData ld: logDataToWrite){
            ld_Service.write(ld);
        }
        
        ld_Service.close();
        
        ArrayList<LogData> actualData = getActualSerializedData();
        
        LogData[] expected = new LogData[logDataToWrite.size()];
        logDataToWrite.toArray(expected);
        
        LogData[] actual = new LogData[logDataToWrite.size()];
        actualData.toArray(actual);
        
        assertArrayEquals("Input and output doesnt match", expected, actual);
    }
    
    
    @Test
    public void testWrite_100000LogDataBean_4Users_NumOfBeansNotMultipleOfBufferSize() {
        ArrayList<LogData> logDataToWrite = generateDummyLogDataBeans(100000, 0, 1, 2, 3);
        PersistLogDataService ld_Service = new PersistLogDataService(30000);
        
        for(LogData ld: logDataToWrite){
            ld_Service.write(ld);
        }
        
        ld_Service.close();
        
        ArrayList<LogData> actualData = getActualSerializedData();
        
        LogData[] expected = new LogData[logDataToWrite.size()];
        logDataToWrite.toArray(expected);
        
        LogData[] actual = new LogData[logDataToWrite.size()];
        actualData.toArray(actual);
        
        assertArrayEquals("Input and output doesnt match", expected, actual);
    }
    
    @Test
    public void testWrite_1000000LogDataBean_4Users_NumOfBeansNotMultipleOfBufferSize() {
        ArrayList<LogData> logDataToWrite = generateDummyLogDataBeans(1000000, 0, 1, 2, 3);
        PersistLogDataService ld_Service = new PersistLogDataService(300000);
        
        for(LogData ld: logDataToWrite){
            ld_Service.write(ld);
        }
        
        ld_Service.close();
        
        ArrayList<LogData> actualData = getActualSerializedData();
        
        LogData[] expected = new LogData[logDataToWrite.size()];
        logDataToWrite.toArray(expected);
        
        LogData[] actual = new LogData[logDataToWrite.size()];
        actualData.toArray(actual);
        
        assertArrayEquals("Input and output doesnt match", expected, actual);
    }
    
    private ArrayList<LogData> getActualSerializedData(){
        ArrayList<LogData> out = new ArrayList<>();
        
        File OBJECTS_FOLDER = new File(AppConstants.ABS_OBJECTS_FOLDER);
        for(File user_folder : OBJECTS_FOLDER.listFiles(new MacFileNameFilter())){
            for(File f : user_folder.listFiles(new MacFileNameFilter())){
                ArrayList<LogData> dataFile = ObjectSerializerUtil.read(f);
                for(LogData ld : dataFile){
                    out.add(ld);
                }
            }
        }
        
        return out;
    }
    
    private ArrayList<LogData> generateDummyLogDataBeans(final int _count, int... _user_ids){
        ArrayList<LogData> out = new ArrayList<>(_count);
        if(_user_ids == null || _user_ids.length == 0){
            return generateDummyLogDataBeansForSingleUser(_count, 0);
        } else {
            for(int user_id : _user_ids){
                out.addAll(generateDummyLogDataBeansForSingleUser(_count, user_id));
            }
            return out;
        }
    }
    
    private ArrayList<LogData> generateDummyLogDataBeansForSingleUser(final int _count, int user_id){
        ArrayList<LogData> out = new ArrayList<>(_count);
        
        int app_id = 0;
        int sql_log_id = 200;
        String raw_sql;
        long ticks = 141290181019L;
        double ticks_ms = 141290181019.05;
        long time_taken = 10;
        String arguments = "";
        int counter = 0;
        int rows_returned = 0;
        Statement stmt = null;
    
        String sql = "select first_name, last_name from student where _id = ";
        for(int i = 1; i <= _count; i++){
            app_id += i;
            sql_log_id += i;
            raw_sql = sql + i + " ;";
            ticks += i;
            ticks_ms += i;
            counter += i;
            rows_returned += i;
            
            StringReader stream = new StringReader(raw_sql);
            CCJSqlParser parser = new CCJSqlParser(stream);
            try {
                stmt = parser.Statement();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            LogData ld = new LogData();
            ld.setUser_id(user_id);
            ld.setApp_id(app_id);
            ld.setSql_log_id(sql_log_id);
            ld.setSql(raw_sql);
            ld.setTicks(ticks);
            ld.setTicks_ms(ticks_ms);
            ld.setTime_taken(time_taken);
            ld.setCounter(counter);
            ld.setRows_returned(rows_returned);
            ld.setStmt(stmt);
            out.add(ld);
        }
        
        return out;
    }
}
