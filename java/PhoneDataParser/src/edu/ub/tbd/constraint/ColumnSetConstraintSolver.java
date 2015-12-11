/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.constraint;

import edu.ub.tbd.beans.ColumnBean;
import edu.ub.tbd.beans.TableBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author san
 */
class ColumnSetConstraintSolver implements ConstraintSolver{
    //There is one ColumnSetConstraintSolver created for each App as its obvious
    private final int app_id;
    //Contains all the columns that were precisely predicted beloning to a Table during SQL parsing
    private final HashMap<String, TableBean> KNOWLEDGE_DB = new HashMap<>();
    //Has the Collection of All Constraints to be solved for this App
    private final HashMap<Integer, EitherConstraint> CONSTRAINTS_DB = new HashMap<>();
    
    private final HashMap<String, ArrayList<EitherConstraint>> INVERTED_INDEXES = new HashMap<>();
    
    ColumnSetConstraintSolver(int _app_id){
        this.app_id = _app_id;
    }

    @Override
    public int getApp_id() {
        return app_id;
    }
    
    @Override
    public void solve() {
        System.out.println(app_id + " : # of Contraints before solve = " + CONSTRAINTS_DB.size());
        for(TableBean tblBean : KNOWLEDGE_DB.values()){
            String tblName = tblBean.getTbl_name();
            Collection<ColumnBean> colBeans = tblBean.getColumns().values();
            if(colBeans != null){
                for(ColumnBean colBean : colBeans){
                    String colName = colBean.getCol_name();
                    String key = tblName + "." + colName;
                    ArrayList<EitherConstraint> constraintsToRemove = INVERTED_INDEXES.get(key);
                    if(constraintsToRemove != null){
                        for(EitherConstraint contraint : constraintsToRemove){
                            CONSTRAINTS_DB.remove(contraint.getId());
                        }
                    }

                }
            }
        }
        System.out.println(app_id + " : # of Contraints after solve = " + CONSTRAINTS_DB.size());
    }

    @Override
    public void addConstraint(EitherConstraint _constraint) {
        CONSTRAINTS_DB.put(_constraint.getId(), _constraint);
        updateInvertedIndex(_constraint);
    }
    
    private void updateInvertedIndex(EitherConstraint _constraint){
        String colName = _constraint.getColumnName();
        for(String tblName : _constraint.getTableNames()){
            updateInvertedIndexForTable(colName, tblName, _constraint);
        }
    }
    
    private void updateInvertedIndexForTable(final String _colName, final String _tblName, final EitherConstraint _constraint){
        String key = _tblName + "." + _colName;
        ArrayList<EitherConstraint> invertedIndex = INVERTED_INDEXES.get(key);
        if(invertedIndex == null){
            invertedIndex = new ArrayList<>();
            INVERTED_INDEXES.put(key, invertedIndex);
        }
        
        invertedIndex.add(_constraint);
    }

    @Override
    public void addKnowledgeData(List<TableBean> _knowledgeData) {
        if(_knowledgeData != null){
            for(TableBean tblBean : _knowledgeData){
                TableBean KDB_tblBean = KNOWLEDGE_DB.get(tblBean.getTbl_name());
                if(KDB_tblBean == null){
                    KNOWLEDGE_DB.put(tblBean.getTbl_name(), tblBean);
                } else {
                    KDB_tblBean.addAllColumns(tblBean.getColumns().values());
                }
            }
        }
    }

    @Override
    public void addConstraints(List<EitherConstraint> _constraints) {
        if(_constraints != null){
            for(EitherConstraint constraint : _constraints){
                addConstraint(constraint);
            }
        }
    }
}
