/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.entity;

import java.util.Objects;

/**
 *
 * @author san
 */
public class PKGen {
    private static PKGen instance;
    
    public static PKGen getInstance(){
        if(instance == null)
            instance = new PKGen();
        
        return instance;
    }
    
    private PKGen(){
    }
    
    private int[] ids = new int[4];
    
    public int getSql_Log_Id(){
        return ++ids[0];
    }

    @Override
    public String toString() {
        return "Primary_key_sequence{" + "sql_log_id=" + ids[0] + '}';
    }
    
    
    public static void main(String[] args) {
        for(int i = 1; i <= 100; i++){
            System.out.println(PKGen.getInstance().getSql_Log_Id());
        }
        
        System.out.println(PKGen.getInstance());
    }
}
