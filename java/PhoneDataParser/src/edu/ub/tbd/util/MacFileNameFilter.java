/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author san
 */
public class MacFileNameFilter implements FilenameFilter{

    @Override
    public boolean accept(File _dir, String _name) {
        return _name.charAt(0) != '.';
    }
    
}
