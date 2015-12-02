/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.beans;

import java.util.Objects;
import net.sf.jsqlparser.schema.Table;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author san
 */
public class ColumnBean {

    private String col_name;
    private String table_name = null;
    private boolean confirmed = false;

    public ColumnBean(String _col_name) {
        this.col_name = StringUtils.lowerCase(_col_name);
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

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String _table_name) {
        this.table_name = StringUtils.lowerCase(_table_name);
    }

    public void setTable_name(Table _table) {
        if (_table != null) {
            if (_table.getAlias() != null && !_table.getAlias().isEmpty()) {
                this.table_name = _table.getAlias();
            } else {
                this.table_name = _table.getName();
            }
        }
    }

}
