/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.beans;

import java.util.Objects;

/**
 *
 * @author san
 */
public class ColumnBean {
 
    private String col_name;
    private boolean confirmed;

    public ColumnBean(String _col_name) {
        this.col_name = _col_name.toLowerCase();
    }

    public String getCol_name() {
        return col_name;
    }

    public void setCol_name(String _col_name) {
        this.col_name = _col_name.toLowerCase();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean _confirmed) {
        this.confirmed = _confirmed;
    }

    @Override
    public int hashCode() {
        return this.col_name.hashCode();
    }

    @Override
    public boolean equals(Object _obj) {
        if (_obj == null) {
            return false;
        }
        if (getClass() != _obj.getClass()) {
            return false;
        }
        final ColumnBean other = (ColumnBean) _obj;
        if (!Objects.equals(this.col_name, other.col_name)) {
            return false;
        }
        return true;
    }
    
    
    
}
