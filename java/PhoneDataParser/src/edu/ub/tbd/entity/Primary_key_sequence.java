/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.entity;

import java.util.Objects;

/**
 *
 * @author san
 */
public class Primary_key_sequence {
    private String table_name;
    private String primary_key_col_name;
    private int next_id_value;

    
    
    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String _table_name) {
        this.table_name = _table_name;
    }

    public String getPrimary_key_col_name() {
        return primary_key_col_name;
    }

    public void setPrimary_key_col_name(String _primary_key_col_name) {
        this.primary_key_col_name = _primary_key_col_name;
    }

    public int getNext_id_value() {
        return next_id_value;
    }

    public void setNext_id_value(int _next_id_value) {
        this.next_id_value = _next_id_value;
    }

    @Override
    public int hashCode() {
        return this.table_name.hashCode();
    }

    @Override
    public boolean equals(Object _other) {
        if (_other == null || !(_other instanceof Primary_key_sequence)) {
            return false;
        }

        return this.table_name.equals(((Primary_key_sequence) _other).table_name);
    }
    
    
}
