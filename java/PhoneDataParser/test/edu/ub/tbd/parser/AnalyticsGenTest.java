/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;

import edu.ub.tbd.beans.LogLineBean;
import edu.ub.tbd.entity.Sql_log;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author san
 */
public class AnalyticsGenTest {
    
    public AnalyticsGenTest() {
    }

    @Test
    public void testGenerate() throws Exception {
        
        String sql = "SELECT thread_id FROM (SELECT _id, thread_id FROM pdu WHERE (msg_box=3) UNION SELECT _id, thread_id FROM sms WHERE (type=3))";
        LogLineBean _logLineBean = getDummyLogLineBean(sql);
        Sql_log sqlLogBean = getSql_logBean();
        
        
        
        
    }
    
    
    private LogLineBean getDummyLogLineBean(String _sql){
        return new LogLineBean(
                    new String[]{"0ee9cead2e2a3a58a316dc27571476e8973ff944",
                                 "1427025929324",
                                 "1427025929324.5",
                                 "2015-03-22 12:05:29.324000",
                                 "810","810","I","SQLite-Query-PhoneLab",
                                 "{\"Counter\":168,\"LogFormat\":\"1.0\",\"Time\":1152240,\"Arguments\":\"null\",\"Results\":\""+ _sql +"\",\"Action\":\"SELECT\",\"Rows returned\":0}"});
    }
    
    private Sql_log getSql_logBean(){
        return new Sql_log(1,120,"Dummy Data");
    }
}
