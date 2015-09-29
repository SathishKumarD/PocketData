/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.entity;

/**
 *
 * @author san
 */
public class LogIdGenerator {
    private static int logId = 0;
    
    private LogIdGenerator(){
        
    }
    
    public static int getNextId(){
        return notifyLogIdChangeListeners(++logId);
    }
    
    public static int currId(){
        return logId;
    }
    
    private static int notifyLogIdChangeListeners(int _newLogId){
        Sql_log.logIdChangeCallBack(_newLogId);
        Analytics.logIdChangeCallBack(_newLogId);
        return _newLogId;
    }
}
