/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.entity;

import java.sql.Timestamp;

/**
 *
 * @author san
 */
public class Analytics {
    private static int curr_PK_ID = 0;
    
    public static int getCurrPKID(){
        return curr_PK_ID;
    }
    
    public static void logIdChangeCallBack(int _newLogId){
        curr_PK_ID = _newLogId;
    }
    
    private int log_id;
    private int ticks;
    private double ticks_ms;
    private Timestamp date_time;
    private int user_id;
    private int app_id;
    private String query_type;
    private int outerjoin_count;
    private int distinct_count;
    private int limit_count;
    private int orderby_count;
    private int aggregate_count;
    private int groupby_count;
    private int union_count;
    private int join_width;
    private int where_count;

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int _log_id) {
        this.log_id = _log_id;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int _ticks) {
        this.ticks = _ticks;
    }

    public double getTicks_ms() {
        return ticks_ms;
    }

    public void setTicks_ms(double _ticks_ms) {
        this.ticks_ms = _ticks_ms;
    }

    public Timestamp getDate_time() {
        return date_time;
    }

    public void setDate_time(Timestamp _date_time) {
        this.date_time = _date_time;
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

    public String getQuery_type() {
        return query_type;
    }

    public void setQuery_type(String _query_type) {
        this.query_type = _query_type;
    }

    public int getOuterjoin_count() {
        return outerjoin_count;
    }

    public void setOuterjoin_count(int _outerjoin_count) {
        this.outerjoin_count = _outerjoin_count;
    }

    public int getDistinct_count() {
        return distinct_count;
    }

    public void setDistinct_count(int _distinct_count) {
        this.distinct_count = _distinct_count;
    }

    public int getLimit_count() {
        return limit_count;
    }

    public void setLimit_count(int _limit_count) {
        this.limit_count = _limit_count;
    }

    public int getOrderby_count() {
        return orderby_count;
    }

    public void setOrderby_count(int _orderby_count) {
        this.orderby_count = _orderby_count;
    }

    public int getAggregate_count() {
        return aggregate_count;
    }

    public void setAggregate_count(int _aggregate_count) {
        this.aggregate_count = _aggregate_count;
    }

    public int getGroupby_count() {
        return groupby_count;
    }

    public void setGroupby_count(int _groupby_count) {
        this.groupby_count = _groupby_count;
    }

    public int getUnion_count() {
        return union_count;
    }

    public void setUnion_count(int _union_count) {
        this.union_count = _union_count;
    }

    public int getJoin_width() {
        return join_width;
    }

    public void setJoin_width(int _join_width) {
        this.join_width = _join_width;
    }

    public int getWhere_count() {
        return where_count;
    }

    public void setWhere_count(int _where_count) {
        this.where_count = _where_count;
    }

}
