/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.entity;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author san
 */
public class App extends AbstractEntity implements Comparable<App>{
    private static int curr_PK_ID = 0;
    
    public static int getCurrPKID(){
        return curr_PK_ID;
    }
    
    @Column public int app_id;
    @Column public String app_name;
    public Set<String> uniqueSQLHash = new HashSet<>();

    public App(String _app_name) {
        this.app_id = curr_PK_ID++;
        this.app_name = _app_name;
    }

    @Override
    public int hashCode() {
        return this.app_name.hashCode();
    }
    
    /**
     * 
     * @param _newHash
     * @return  true if new hash is not already present
     *          false if new hash is already present
     */
    public boolean addSQLHash(String _newHash){
        return uniqueSQLHash.add(_newHash);
    }
    
    public int getUniqueSQLHashCount(){
        return uniqueSQLHash.size();
    }

    @Override
    public boolean equals(Object _other) {
        if (_other == null || _other instanceof App) {
            return false;
        }
        return this.app_name.equals(((App) _other).app_name);
    }

    //TODO: <Sankar> compare is inconsistent with equals. FIX ME
    public static int compare(App _o1, App _o2) {
        return (_o1.app_id < _o2.app_id) ? -1 : ((_o1.app_id == _o2.app_id) ? 0 : 1);
    }
    
    @Override
    public int compareTo(App _o) {
        return compare(this, _o);
    }
    
    /***************************************************************************
     ***************************************************************************
     *******************        GETTERS & SETTERS            *******************
     ***************************************************************************
     ***************************************************************************
     */
    
    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int _app_id) {
        this.app_id = _app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String _app_name) {
        this.app_name = _app_name;
    }
    
}
