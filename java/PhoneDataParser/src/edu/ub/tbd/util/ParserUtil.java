/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.util;

/**
 *
 * @author san
 */
public class ParserUtil {
    
    public static StringBuilder convertToCSV(String... stringVals){
        if(stringVals == null || stringVals.length == 0){
            return null;
        }
        
        StringBuilder out = new StringBuilder();
        for(int i = 0, size = stringVals.length; i < size; i++){
            out.append("\"").append(stringVals[i]).append("\"");
            if(i < size-1){
                out.append(",");
            }
        }
        
        return out;
    }
    
}
