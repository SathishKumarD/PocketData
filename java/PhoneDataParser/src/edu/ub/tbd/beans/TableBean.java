/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author san
 */
public class TableBean {
    
    private String tbl_name;
    private String tbl_alias;
    private Map<String, ColumnBean> columns = new HashMap<>();

    public TableBean(){
        
    }
    
    public TableBean(String _tbl_name){
        this(_tbl_name, _tbl_name);
    }

    public TableBean(String _tbl_name, String _tbl_alias) {
        this.tbl_name = _tbl_name;
        this.tbl_alias = _tbl_alias;
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
    
    /***************************************************************************
     ***************************************************************************
     *******************        GETTERS & SETTERS            *******************
     ***************************************************************************
     ***************************************************************************
     */
    
    public String getTbl_name() {
        return tbl_name;
    }

    public void setTbl_name(String _tbl_name) {
        this.tbl_name = _tbl_name;
    }

    public String getTbl_alias() {
        return tbl_alias;
    }

    public void setTbl_alias(String _tbl_alias) {
        this.tbl_alias = _tbl_alias;
    }

    public Map<String, ColumnBean> getColumns() {
        return columns;
    }

}
