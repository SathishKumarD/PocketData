/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.util;

import edu.ub.tbd.beans.LogData;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author san
 */
public interface ObjectSerializerUtil {
 public void serialize(String _file, ArrayList<LogData> _lds);
 public ArrayList<LogData> read(String _file);
 public ArrayList<LogData> read(File _file);
         
}
