/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.beans;

import edu.ub.tbd.entity.PKGen;
import java.io.Serializable;
import net.sf.jsqlparser.statement.Statement;

/**
 *
 * @author san
 */
public class LogData implements Serializable{
    public int sql_log_id;
    public long ticks;
    public double ticks_ms;
    public long time_taken;
    public int counter;
    public int rows_returned;
    public int user_id;
    public int app_id = -1; //Default value if no App_Id found for a valid line
    public String action;
    public String sql;
    public Statement stmt;

    public LogData(){
        this.sql_log_id = PKGen.getInstance().getSql_Log_Id();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LogData other = (LogData) obj;
        if (this.user_id != other.user_id) {
            return false;
        }
        if (this.app_id != other.app_id) {
            return false;
        }
        if (this.sql_log_id != other.sql_log_id) {
            return false;
        }
        if (!this.action.equals(other.getAction())) {
            return false;
        }
        if (!this.sql.equals(other.getSql())) {
            return false;
        }
        if (this.ticks != other.ticks) {
            return false;
        }
        if (Double.doubleToLongBits(this.ticks_ms) != Double.doubleToLongBits(other.ticks_ms)) {
            return false;
        }
        if (this.time_taken != other.time_taken) {
            return false;
        }
        if (this.counter != other.counter) {
            return false;
        }
        if (this.rows_returned != other.rows_returned) {
            return false;
        }
        
        if(this.stmt == null || other.getStmt() == null){
            return this.stmt == other.getStmt();
        } else if (!this.stmt.toString().equals(other.getStmt().toString())) {
            return false;
        }
        
        return true;
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

    public String getAction() {
        return action;
    }

    public void setAction(String _action) {
        this.action = _action;
    }
    
    public int getSql_log_id() {
        return sql_log_id;
    }

    public void setSql_log_id(int _sql_log_id) {
        this.sql_log_id = _sql_log_id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String _sql) {
        this.sql = _sql;
    }

    public long getTicks() {
        return ticks;
    }

    public void setTicks(long _ticks) {
        this.ticks = _ticks;
    }

    public double getTicks_ms() {
        return ticks_ms;
    }

    public void setTicks_ms(double _ticks_ms) {
        this.ticks_ms = _ticks_ms;
    }

    public long getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(long _time_taken) {
        this.time_taken = _time_taken;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int _counter) {
        this.counter = _counter;
    }

    public int getRows_returned() {
        return rows_returned;
    }

    public void setRows_returned(int _rows_returned) {
        this.rows_returned = _rows_returned;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement _stmt) {
        this.stmt = _stmt;
    }
}
