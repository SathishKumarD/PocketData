/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.constraint;

import edu.ub.tbd.beans.TableBean;
import java.util.List;

/**
 *
 * @author san
 */
interface ConstraintSolver {
    public void solve();
    public StringBuilder getXML();
    public int getApp_id();
    public void addConstraint(EitherConstraint _constraint);
    public void addKnowledgeData(List<TableBean> _knowledgeData);
    public void addConstraints(List<EitherConstraint> _constraints);
}
