/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.constraint;

/**
 *
 * @author san
 */
interface ConstraintSolver {
    public void solve();
    public int getApp_id();
    public void addConstraint(EitherConstraint _constraint);
    
}
