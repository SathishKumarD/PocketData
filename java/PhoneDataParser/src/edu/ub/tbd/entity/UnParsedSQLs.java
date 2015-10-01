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

    public UnParsedSQLs(String _fileName, int _lineNumber, String _raw_sql) {
        this.fileName = _fileName;
        this.lineNumber = _lineNumber;
        this.raw_sql = _raw_sql;
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
    
    
    
}
