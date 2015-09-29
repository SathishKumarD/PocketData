/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.entity;

/**
 *
 * @author san
 */
public class User extends AbstractEntity implements Comparable<User>{
    private static int curr_PK_ID = 0;
    
    public static int getCurrPKID(){
        return curr_PK_ID;
    }
    
    //In the current run it has pool of 14 user fake names
    private final static String [] userFakeNames = {"Ramarajan","MGR", "Sivaji",
        "Rajini", "Kamal", "TR","SRK", "Alli Arjuna","Pandiarajan", "Dhanush", 
        "Ajith", "Vijay", "Vadivelu", "Sathyaraj"};
    
    private static int nextIndex_userFakeNames = 0;
    
    @Column public int user_id; //TODO: <Sankar> FIX all the variables to be private later
                                 //      current code it is necessary for a variable to be public for reflection to persist it
    @Column public String guid;
    @Column public String user_name;

    public User(String _guid) {
        this.guid = _guid;
        this.user_name = userFakeNames[nextIndex_userFakeNames++];
        this.user_id = ++curr_PK_ID;
    }
    
    @Override
    public String toString() {
        return "User{" + "\"user_id\":\"" + user_id + "\", \"guid\":\"" + guid + 
                "\", \"user_name\":\"" + user_name + "\"}";
    }

    //TODO: <Sankar> compare is inconsistent with equals. FIX ME
    public static int compare(User _o1, User _o2) {
        return (_o1.user_id < _o2.user_id) ? -1 : ((_o1.user_id == _o2.user_id) ? 0 : 1);
    }

    @Override
    public int compareTo(User _o) {
        return compare(this, _o);
    }
    
    /***************************************************************************
     ***************************************************************************
     *******************        GETTERS & SETTERS            *******************
     ***************************************************************************
     ***************************************************************************
     */
    
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int _user_id) {
        this.user_id = _user_id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String _guid) {
        this.guid = _guid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String _user_name) {
        this.user_name = _user_name;
    }

}
