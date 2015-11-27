/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.util;

import edu.ub.tbd.beans.LogData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author san
 */
public class JavaObjectSerializerUtil implements ObjectSerializerUtil{
    
    public JavaObjectSerializerUtil(){
        
    }
    
    @Override
    public void serialize(String file, ArrayList<LogData> _lds){
        try {
            FileOutputStream fOS = new FileOutputStream(file);
            ObjectOutputStream objOS = new ObjectOutputStream(fOS);
            objOS.writeObject(_lds);
            objOS.close();
            fOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public ArrayList<LogData> read(String file){
        return read(new File(file));
    }
    
    @Override
    public ArrayList<LogData> read(File file){
        ArrayList<LogData> lds = null;
        try {
            FileInputStream fIS = new FileInputStream(file);
            ObjectInputStream objIS = new ObjectInputStream(fIS);
            lds = (ArrayList<LogData>) objIS.readObject();
            objIS.close();
            fIS.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return lds;
    }
}
