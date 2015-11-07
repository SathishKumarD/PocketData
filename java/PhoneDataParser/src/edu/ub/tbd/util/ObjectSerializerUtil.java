/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author san
 */
public class ObjectSerializerUtil {
    
    
    public static <O> void serialize(String file, O obj){
        try {
            FileOutputStream fOS = new FileOutputStream(file);
            ObjectOutputStream objOS = new ObjectOutputStream(fOS);
            objOS.writeObject(obj);
            objOS.close();
            fOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static <O> O read(String file){
        return read(new File(file));
    }
    
    public static <O> O read(File file){
        O obj = null;
        try {
            FileInputStream fIS = new FileInputStream(file);
            ObjectInputStream objIS = new ObjectInputStream(fIS);
            obj = (O) objIS.readObject();
            objIS.close();
            fIS.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
