/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.constraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 *
 * @author san
 */
public class EitherConstraint {
    
    private final String columnName;
    private final String[] tableNames;
    private static int ID = 0;
    private final int id;
    
    public EitherConstraint(String _colName, String... _tableNames){
        this.id = ID++;
        this.columnName = _colName;
        this.tableNames = _tableNames;
    }
    
    public int getId() {
        return id;
    }

    public String[] getTableNames() {
        return tableNames;
    }

    public String getColumnName() {
        return columnName;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Constraint# "+ id +" : [EITHER] - ");
        sb.append("Col::" + columnName + " IN TABLES {");
        for(String tblName : tableNames){
            sb.append(tblName + "|");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EitherConstraint other = (EitherConstraint) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
    public static void main(String[] args) {
        EitherConstraint c = new EitherConstraint("A", "T1", "T3", "T5");
        System.out.println(c);
    }
    
    public static List<EitherConstraint> createEitherConstraints(Map<String, List<String>> constraints) {
    	if(constraints != null) {
    		List<EitherConstraint> constrainstList = new ArrayList<>();
    		for(Map.Entry<String, List<String>> entry : constraints.entrySet()) {
    			String key = entry.getKey();
    			List<String> value = entry.getValue();
    			EitherConstraint eitherConstraint = new EitherConstraint(key, value.toArray(new String[value.size()]));
    			constrainstList.add(eitherConstraint);
    		}
    		return constrainstList;
    	}
    	return null;
    }
    
}
