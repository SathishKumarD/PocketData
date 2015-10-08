/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.util;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author san
 */
public class ParserUtilTest {
    
    public ParserUtilTest() {
    }

    @Test
    public void testConvertToCSV() {
        
    }
    
    /*$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
     *$$$$$$$                                                      $$$$$$$$$$$$$
     *$$$$$$$      Tests jsonArgumentStringToList(String str_Arg)  $$$$$$$$$$$$$
     *$$$$$$$                                                      $$$$$$$$$$$$$
     *$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
     */
    
    @Test
    public void testJsonArgumentStringToList_1(){
        String input = "null";
        
        List<String> actual = ParserUtil.jsonArgumentStringToList(input);
        
        assertNotNull(actual);
        assertTrue("Should have ArrayList size 0", actual.isEmpty());
    }

    @Test
    public void testJsonArgumentStringToList_2(){
        String input = "2,2111834650,";
        
        List<String> expected = new ArrayList();
        expected.add("2"); expected.add("2111834650");
        
        List<String> actual = ParserUtil.jsonArgumentStringToList(input);
        
        assertNotNull(actual);
        assertTrue("Should have ArrayList size 2", actual.size() == 2);
        assertArrayEquals("Unexpected values.", expected.toArray(), actual.toArray());
    }
    
    @Test
    public void testJsonArgumentStringToList_3(){
        String input = "[https:\\/\\/www.gstatic.com\\/android\\/keyboard\\/dictionarypack\\/Bayo\\/metadata.json]";
        
        List<String> expected = new ArrayList();
        expected.add("https:\\/\\/www.gstatic.com\\/android\\/keyboard\\/dictionarypack\\/Bayo\\/metadata.json");
        
        List<String> actual = ParserUtil.jsonArgumentStringToList(input);
        
        assertNotNull(actual);
        assertTrue("Should have ArrayList size 1", actual.size() == 1);
        assertArrayEquals("Unexpected values.", expected.toArray(), actual.toArray());
    }
    
    @Test
    public void testJsonArgumentStringToList_4(){
        String input = "[]";
        
        List<String> expected = new ArrayList();
        
        List<String> actual = ParserUtil.jsonArgumentStringToList(input);
        
        assertNotNull(actual);
        assertTrue("Should have ArrayList size 0", actual.isEmpty());
    }
    
    
    @Test
    public void testApplyArgumentsToSQLQuery_1() {
        //assertEquals("Invalid equals", this, this);
        String input = "{\"Counter\":191,\"LogFormat\":\"1.0\",\"Time\":1089583,\"Arguments\":\"[]\",\"Results\":\"SELECT _id FROM view_raw_contacts WHERE (1) AND (data_set='plus' AND account_name NOT IN (?) AND account_type='com.google')\",\"Action\":\"SELECT\",\"Rows returned\":0}";
        String expected = "SELECT _id FROM view_raw_contacts WHERE (1) AND (data_set='plus' AND account_name NOT IN ('fak_1') AND account_type='com.google')";
        String actual = null;
        
        JSONParser parser = new JSONParser();
        try {
            JSONObject JSON_Obj = (JSONObject) parser.parse(input);
            String str_arguments = (String) JSON_Obj.get("Arguments");
            
            actual = ParserUtil.applyArgumentsToSQLQuery(str_arguments, (String) JSON_Obj.get("Results"));
            
        } catch (ParseException e) {
            throw new AssertionError("Invalid Input[JSON] | " + input);
        }
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyArgumentsToSQLQuery_2() {
        //assertEquals("Invalid equals", this, this);
        String input = "{\"Counter\":191,\"LogFormat\":\"1.0\",\"Time\":1089583,\"Arguments\":\"[]\",\"Results\":\"SELECT _id FROM view_raw_contacts WHERE (1) AND (data_set='plus' AND account_name NOT IN (?) AND account_type='com.google')\",\"Action\":\"SELECT\",\"Rows returned\":0}";
        String expected = "SELECT _id FROM view_raw_contacts WHERE (1) AND (data_set='plus' AND account_name NOT IN ('fak_1') AND account_type='com.google')";
        String actual = null;
        
        JSONParser parser = new JSONParser();
        try {
            JSONObject JSON_Obj = (JSONObject) parser.parse(input);
            String str_arguments = (String) JSON_Obj.get("Arguments");
            
            actual = ParserUtil.applyArgumentsToSQLQuery(str_arguments, (String) JSON_Obj.get("Results"));
            
        } catch (ParseException e) {
            throw new AssertionError("Invalid Input[JSON] | " + input);
        }
        
        assertEquals(expected, actual);
    }   
    
    @Test
    public void testApplyArgumentsToSQLQuery_3_HashCoded() {
        //assertEquals("Invalid equals", this, this);
        String input = "{\"Counter\":27,\"LogFormat\":\"1.0\",\"Time\":129375,\"Results\":\"INSERT INTO carriers(bearer,authtype,carrier_enabled,protocol,mmsproxy,roaming_protocol,numeric,mcc,type,mmsc,password,mvno_match_data,mvno_type,name,server,mnc,apn,user,mmsport) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)\",\"Action\":\"INSERT\",\"Arguments(hashCoded)\":\"0,-1,1231,2343,-1630285132,2343,47743056,49679,-697368471,155249537,117478,0,0,1755004508,42,1537,1954370557,117478,1784,\"}";
        String expected = "INSERT INTO carriers(bearer,authtype,carrier_enabled,protocol,mmsproxy,roaming_protocol,numeric,mcc,type,mmsc,password,mvno_match_data,mvno_type,name,server,mnc,apn,user,mmsport) VALUES ('0','-1','1231','2343','-1630285132','2343','47743056','49679','-697368471','155249537','117478','0','0','1755004508','42','1537','1954370557','117478','1784')";
        String actual = null;
        
        JSONParser parser = new JSONParser();
        try {
            JSONObject JSON_Obj = (JSONObject) parser.parse(input);
            String str_arguments = (String) JSON_Obj.get("Arguments(hashCoded)");
            
            actual = ParserUtil.applyArgumentsToSQLQuery(str_arguments, (String) JSON_Obj.get("Results"));
            
        } catch (ParseException e) {
            throw new AssertionError("Invalid Input[JSON] | " + input);
        }
        
        assertEquals(expected, actual);
    }  
    

    @Test
    public void testApplyArgumentsToSQLQuery_String_String() {
    }
    
}
