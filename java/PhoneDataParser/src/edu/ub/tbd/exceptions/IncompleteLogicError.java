/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.exceptions;

/**
 *
 * @author san
 */
public class IncompleteLogicError extends Error{
    
    public IncompleteLogicError(String _errMsg){
        super(_errMsg);
    }
}
