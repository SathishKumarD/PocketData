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
public class Sql_log extends AbstractEntity{
    private static int curr_PK_ID = 0;
    
    public static int getCurrPKID(){
        return curr_PK_ID;
    }
    
    public static void logIdChangeCallBack(int _newLogId){
        curr_PK_ID = _newLogId;
    }
    
    @Column public int log_id;
    @Column public int user_id;
    @Column public int app_id;
    @Column public String raw_data;

    public Sql_log(int _log_id, int _user_id, int _app_id, String _raw_data) {
        this.log_id = _log_id;
        this.user_id = _user_id;
        this.app_id = _app_id;
        this.raw_data = _raw_data;
    }

    /***************************************************************************
     ***************************************************************************
     *******************        GETTERS & SETTERS            *******************
     ***************************************************************************
     ***************************************************************************
     */
    
    public static int getCurr_PK_ID() {
        return curr_PK_ID;
    }

    public static void setCurr_PK_ID(int _curr_PK_ID) {
        Sql_log.curr_PK_ID = _curr_PK_ID;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int _log_id) {
        this.log_id = _log_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int _user_id) {
        this.user_id = _user_id;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int _app_id) {
        this.app_id = _app_id;
    }

    public String getRaw_data() {
        return raw_data;
    }

    public void setRaw_data(String _raw_data) {
        this.raw_data = _raw_data;
    }

}
