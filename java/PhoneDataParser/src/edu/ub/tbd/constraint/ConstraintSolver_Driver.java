/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.constraint;

import java.util.List;

import edu.ub.tbd.beans.TableBean;

/**
 *
 * @author san
 */
public interface ConstraintSolver_Driver {
    public void solve();
    public void addKnowledgeData(int app_id, List<TableBean> knowledgeData);
    public void addConstraints(int app_id, List<EitherConstraint> constraints);
}
