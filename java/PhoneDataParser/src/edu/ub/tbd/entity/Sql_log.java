/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.entity;

import edu.ub.tbd.beans.LogData;

/**
 *
 * @author san
 */
public class Sql_log extends AbstractEntity{
    
    @Column public int sql_log_id;
    @Column public int user_id;
    @Column public int app_id;
    @Column public String clean_sql;

    public Sql_log(LogData _ld){
        this.sql_log_id = _ld.getSql_log_id();
        this.user_id = _ld.getUser_id();
        this.app_id = _ld.getApp_id();
        this.clean_sql = _ld.getSql();
    }

    /***************************************************************************
     ***************************************************************************
     *******************        GETTERS & SETTERS            *******************
     ***************************************************************************
     ***************************************************************************
     */
    
    public int getSql_log_id() {
        return sql_log_id;
    }

    public void setSql_log_id(int _sql_log_id) {
        this.sql_log_id = _sql_log_id;
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

    public String getClean_sql() {
        return clean_sql;
    }

    public void setClean_sql(String _clean_sql) {
        this.clean_sql = _clean_sql;
    }

}
