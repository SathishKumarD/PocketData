/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.entity;

import edu.ub.tbd.util.ParserUtil;

/**
 *
 * @author san
 */
public class Unparsed_log_lines extends AbstractEntity{
    private static int curr_PK_ID = 0;
    
    public static int getCurrPKID(){
        return curr_PK_ID;
    }
    
    @Column public int unparsed_log_lines_id;
    @Column public String file_location;
    @Column public int file_line_number;
    @Column public String raw_data;
    @Column public String exception_trace;
    

    public Unparsed_log_lines(String _file_location, int _file_line_number,
                String _raw_data, String _exception_trace)
    {
        this.unparsed_log_lines_id = ++curr_PK_ID;
        this.file_location = _file_location;
        this.file_line_number = _file_line_number;
        this.raw_data = _raw_data;
        this.exception_trace = _exception_trace;
    }
    
    @Override
    public String toString() {
        return "Unparsed_log_lines{" + 
                    "\"unparsed_log_lines_id\":\""      + unparsed_log_lines_id + 
                    "\", \"file_location\":\""          + file_location + 
                    "\", \"file_line_number\":\""       + file_line_number + 
                    "\", \"raw_data\":\""               + raw_data + 
                    "\", \"exception_trace\":\""        + exception_trace + 
                    "\"}";
    }
    
    /***************************************************************************
     ***************************************************************************
     *******************        GETTERS & SETTERS            *******************
     ***************************************************************************
     ***************************************************************************
     */
    
    public int getUnparsed_log_lines_id() {
        return unparsed_log_lines_id;
    }

    public void setUnparsed_log_lines_id(int _unparsed_log_lines_id) {
        this.unparsed_log_lines_id = _unparsed_log_lines_id;
    }

    public String getFile_location() {
        return file_location;
    }

    public void setFile_location(String _file_location) {
        this.file_location = _file_location;
    }

    public int getFile_line_number() {
        return file_line_number;
    }

    public void setFile_line_number(int _file_line_number) {
        this.file_line_number = _file_line_number;
    }

    public String getRaw_data() {
        return raw_data;
    }

    public void setRaw_data(String _raw_data) {
        this.raw_data = _raw_data;
    }

    public String getException_trace() {
        return exception_trace;
    }

    public void setException_trace(String _exception_trace) {
        this.exception_trace = _exception_trace;
    }
    
}
