/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.constraint;

import java.util.HashMap;
import java.util.List;

import edu.ub.tbd.beans.TableBean;

/**
 *
 * @author san
 */
public class ColumnSetConstraintSolver_Driver implements ConstraintSolver_Driver {

    private static ColumnSetConstraintSolver_Driver instance;
    //There is one ConstraintSolver instance created for each App
    private HashMap<Integer, ConstraintSolver> SOLVERS = new HashMap<>();

    private ColumnSetConstraintSolver_Driver() {

    }

    public static ColumnSetConstraintSolver_Driver getInstance() {
        if (instance == null) {
            instance = new ColumnSetConstraintSolver_Driver();
        }

        return instance;
    }

    //This is the method through which the SOLVERS_COLL should be accessed.
    //Direct access of the SOLVERS_COLL should be avoided.
    private ConstraintSolver getSolver(int _app_id) {
        ConstraintSolver solver = SOLVERS.get(_app_id);
        if (solver == null) {
            solver = new ColumnSetConstraintSolver(_app_id);
            SOLVERS.put(_app_id, solver);
        }

        return solver;
    }

    @Override
    public void solve() {
        for (ConstraintSolver solver : SOLVERS.values()) {
            System.out.println("Solving for App : " + solver.getApp_id());
            solver.solve();
        }
    }

    @Override
    public void addKnowledgeData(int _app_id, List<TableBean> _knowledgeData) {
        getSolver(_app_id).addKnowledgeData(_knowledgeData);
    }

    @Override
    public void addConstraints(int _app_id, List<EitherConstraint> _constraints) {
        getSolver(_app_id).addConstraints(_constraints);
    }

    @Override
    public StringBuilder generateXML() {
        StringBuilder out = new StringBuilder();
        out.append("<schemas>").append("\n");
        
        for(ConstraintSolver solver : SOLVERS.values()){
            if(solver.getApp_id() == -1){
                continue;
            }
            out.append(solver.getXML());
        }
        
        out.append("</schemas>").append("\n");
        
        return out;
    }

}
