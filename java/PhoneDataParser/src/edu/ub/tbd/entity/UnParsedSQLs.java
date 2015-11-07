/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.entity;

/**
 * Not to be sent in Prod. Just temp until all issue solves
 * @deprecated 
 * @author san
 */
public class UnParsedSQLs extends AbstractEntity{
 
    @Column public String fileName;
    @Column public int lineNumber;
    @Column public String raw_sql;
    @Column public String actual_sql_parsed;

    public UnParsedSQLs(String _fileName, int _lineNumber, String _raw_sql, String _actual_sql_parsed) {
        this.fileName = _fileName;
        this.lineNumber = _lineNumber;
        this.raw_sql = _raw_sql;
        this.actual_sql_parsed = _actual_sql_parsed;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String _fileName) {
        this.fileName = _fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int _lineNumber) {
        this.lineNumber = _lineNumber;
    }

    public String getRaw_sql() {
        return raw_sql;
    }

    public void setRaw_sql(String _raw_sql) {
        this.raw_sql = _raw_sql;
    }

    public String getActual_sql_parsed() {
        return actual_sql_parsed;
    }

    public void setActual_sql_parsed(String _actual_sql_parsed) {
        this.actual_sql_parsed = _actual_sql_parsed;
    }
}
