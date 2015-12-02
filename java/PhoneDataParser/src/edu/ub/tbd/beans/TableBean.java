/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author san
 */
public class TableBean {
    
    private String tbl_name;
    private String tbl_alias;
    private String wholeTblName;
    private String schemaName;
    private Map<String, ColumnBean> columns = new HashMap<>();

    private TableBean(){
        
    }
    
    public TableBean(String _tbl_name){
        this(_tbl_name, _tbl_name);
    }

    public TableBean(String _tbl_name, String _tbl_alias) {
        this.tbl_name = StringUtils.lowerCase(_tbl_name);
        this.tbl_alias = StringUtils.lowerCase(_tbl_alias);
    }
    
    public void addColumn(ColumnBean _column){
        ColumnBean existingColumn = columns.get(_column.getCol_name());
        if(existingColumn != null){
            if(_column.isConfirmed()){
                if(!existingColumn.isConfirmed()){
                    existingColumn.setConfirmed(true);
                }
            }
        } else {
            columns.put(_column.getCol_name(), _column);
        }
    }
    
    public void addAllColumns(Collection<ColumnBean> _columns){
        for(ColumnBean col : _columns){
            addColumn(col);
        }
    }
    
    public void setTbl_name(String _tbl_name) {
        this.tbl_name = StringUtils.lowerCase(_tbl_name);
    }
    
    public void setTbl_alias(String _tbl_alias) {
        this.tbl_alias = StringUtils.lowerCase(_tbl_alias);
    }
    
    public void setWholeTblName(String _wholeTblName) {
        this.wholeTblName = StringUtils.lowerCase(_wholeTblName);
    }
    
    public void setSchemaName(String _schemaName) {
        this.schemaName = StringUtils.lowerCase(_schemaName);
    }
    
    /***************************************************************************
     ***************************************************************************
     *************        GENERATED - GETTERS & SETTERS         ****************
     ***************************************************************************
     ***************************************************************************
     */
    
    public String getTbl_name() {
        return tbl_name;
    }

    public String getTbl_alias() {
        return tbl_alias;
    }

    public Map<String, ColumnBean> getColumns() {
        return columns;
    }

    public String getWholeTblName() {
        return wholeTblName;
    }

    public String getSchemaName() {
        return schemaName;
    }

}
