/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;

import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.constants.AppConstants;
import edu.ub.tbd.util.ObjectSerializerUtil;
import java.io.File;
import java.util.ArrayList;
import org.junit.Test;

/**
 *
 * @author san
 */
public class ObjectSerializationTest {
    
    @Test
    public void testSingleSerializedFile() {
        ArrayList<LogData> logs = getActualSerializedData();
        for(int i = 0; i < logs.size(); i++){
            LogData ld = logs.get(i);
            
            if(i > logs.size() - 3){
                System.out.println(ld.getApp_id());
                System.out.println(ld.getTicks());
                System.out.println(ld.getSql());
                System.out.println(ld.getStmt());
            } else {
                /*
                System.out.println(ld.getApp_id());
                System.out.println(ld.getTicks());
                System.out.println(ld.getRaw_sql());
                System.out.println(ld.getStmt());
                        */
            }
            
        }
    }
    
    private ArrayList<LogData> getActualSerializedData(){
        File f = new File("/Users/san/UB/CSE-662/Project/Run/OUTPUT/OBJECTS/1/0");
        return ObjectSerializerUtil.read(f);
    }
    
}
