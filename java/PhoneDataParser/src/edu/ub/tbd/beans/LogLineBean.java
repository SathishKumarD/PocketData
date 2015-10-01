/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.beans;

/**
 *
 * @author san
 */
public class LogLineBean {
    
    private String user_guid;
    private String ticks;
    private String ticks_ms;
    private String date_time;
    private String process_id;
    private String thread_id;
    private String log_level;
    private String tag;
    private String log_msg;
    
    public LogLineBean(String... _logLineSplitData){
        user_guid   = _logLineSplitData[0];
        ticks       = _logLineSplitData[1];
        ticks_ms    = _logLineSplitData[2];
        date_time   = _logLineSplitData[3];
        process_id  = _logLineSplitData[4];
        thread_id   = _logLineSplitData[5];
        log_level   = _logLineSplitData[6];
        tag         = _logLineSplitData[7];
        log_msg     = _logLineSplitData[8];
    }

    /***************************************************************************
     ***************************************************************************
     *******************        GETTERS & SETTERS            *******************
     ***************************************************************************
     ***************************************************************************
     */
    
    public String getUser_guid() {
        return user_guid;
    }

    public void setUser_guid(String _user_guid) {
        this.user_guid = _user_guid;
    }

    public String getTicks() {
        return ticks;
    }

    public void setTicks(String _ticks) {
        this.ticks = _ticks;
    }

    public String getTicks_ms() {
        return ticks_ms;
    }

    public void setTicks_ms(String _ticks_ms) {
        this.ticks_ms = _ticks_ms;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String _date_time) {
        this.date_time = _date_time;
    }

    public String getProcess_id() {
        return process_id;
    }

    public void setProcess_id(String _process_id) {
        this.process_id = _process_id;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String _thread_id) {
        this.thread_id = _thread_id;
    }

    public String getLog_level() {
        return log_level;
    }

    public void setLog_level(String _log_level) {
        this.log_level = _log_level;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String _tag) {
        this.tag = _tag;
    }

    public String getLog_msg() {
        return log_msg;
    }

    public void setLog_msg(String _log_msg) {
        this.log_msg = _log_msg;
    }
    
    public String get_app_key()
    {
      return  this.getUser_guid() + "_" + this.getProcess_id() + 
                            "_" + this.getThread_id();
    }
    
    
}
