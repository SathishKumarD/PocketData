/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;

/**
 * Log levels used to Log the log entries in the Log file. <br>
 * INFO, ERROR, etc. (Similar to log4j)
 * @author san
 */
public enum LogLevel {
    
    INFO ("I"),
    
    /**
     * ERROR is generally skipped during log parsing in {@link edu.ub.tbd.parser.LogParser LogParser}
     */
    ERROR ("E");
    
    private final String level;
    
    private LogLevel(String _level){
        level = _level;
    }
    
    public boolean equalsName(String _level){
        return (_level == null) ? false : level.equals(_level);
    }
    
    public boolean equals(String _level){
        return (_level != null && _level instanceof String) ? level.equals(_level) : false;
    }
    
    public String toString(){
        return level;
    }
}
