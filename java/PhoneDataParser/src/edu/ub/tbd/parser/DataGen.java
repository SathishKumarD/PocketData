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

/**
 *
 * @author san
 */
public class DataGen {
    private final PersistanceService ps_SqlLog;
    private final PersistanceService ps_Analytics;
    
    public DataGen() throws Exception {
        this.ps_SqlLog = new PersistanceFileService(AppConstants.ABS_DATA_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, Sql_log.class);
        
        this.ps_Analytics = new PersistanceFileService(AppConstants.ABS_DATA_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, Analytics.class);
    }
 
    public void run() throws Exception{
        File obj_folder = new File(AppConstants.ABS_OBJECTS_FOLDER);
        File[] users_Folder = obj_folder.listFiles(new MacFileNameFilter());
        for(File each_user : users_Folder){
            System.out.println("Processing User : " + each_user.getName());
            for(File f : each_user.listFiles(new MacFileNameFilter())){
                parseEachFile(f);
            }
        }
    }
    
    private void parseEachFile(File _f) throws Exception{
        ArrayList<LogData> lds = ObjectSerializerUtil.read(_f);
        for(LogData ld : lds){
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
    
    public void shutDown() throws Exception{
        ps_SqlLog.close();
        ps_Analytics.close();
    }
}
