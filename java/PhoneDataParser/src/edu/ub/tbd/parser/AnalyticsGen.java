/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;

import edu.ub.tbd.beans.LogLineBean;
import edu.ub.tbd.entity.Analytics;
import edu.ub.tbd.entity.Sql_log;
import edu.ub.tbd.exceptions.IncompleteLogicError;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.pragma.Pragma;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperation;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;
import org.json.simple.JSONObject;

/**
 *
 * @author sathish
 */
public class AnalyticsGen {

    private final JSONObject JSON_Obj;
    private final Sql_log sql_log;
    private final Analytics parent_analytics;
    private final ArrayList<Analytics> list_analytics = new ArrayList<>();
    private final LogLineBean logLineBean;
    public static int PRAGMA_COUNT = 0;

    public AnalyticsGen(JSONObject _JSON_Obj, Sql_log _sql_log, LogLineBean _logLineBean) {
        this.JSON_Obj = _JSON_Obj;
        this.sql_log = _sql_log;
        this.logLineBean = _logLineBean;
        this.parent_analytics = initAnalyticsEntity();
        list_analytics.add(parent_analytics);
    }
    
    public ArrayList<Analytics> generate() throws ParseException, Exception{
        String sql_raw = (String) JSON_Obj.get("Results");
        StringReader stream = new StringReader(sql_raw);
        CCJSqlParser parser = new CCJSqlParser(stream);
        
        Statement stmt = null;
        try {
            stmt = parser.Statement();
        } catch (ParseException e) {
            //System.out.println("Unable to Parse SQL : \n" + sql_raw);
            throw e;
        }
        
        if(stmt != null){
            if(stmt instanceof Select){
                Select select = (Select) stmt;
                SelectUnionParser su_parser = new SelectUnionParser();
                select.getSelectBody().accept(su_parser);
                su_parser.populateAnalyticsEntity(parent_analytics);
            } else if (stmt instanceof Pragma){
                Pragma pragma = (Pragma) stmt; //REFER: PRAGMA Grammar => https://www.sqlite.org/pragma.html#pragma_table_info
                parent_analytics.setQuery_type("PRAGMA");
                PRAGMA_COUNT++;
            } else {
                throw new IncompleteLogicError("Handle other statement types");
            }
        } else {
            throw new Exception("Null Statement");
        }
        
        return this.list_analytics;
    }
    
    private Analytics initAnalyticsEntity(){
        Analytics out = new Analytics();

        out.setTicks(Long.parseLong(logLineBean.getTicks()));
        out.setTicks_ms(Double.parseDouble(logLineBean.getTicks_ms()));
        out.setDate_time(Long.parseLong(logLineBean.getTicks()));
        out.setTime_taken((Long) JSON_Obj.get("Time"));
        out.setArguments((String) JSON_Obj.get("Arguments"));
        out.setCounter(((Number) JSON_Obj.get("Counter")).intValue());
        
        Number jsonRowsReturnedValue = (Number) JSON_Obj.get("Rows returned"); // The JSON need not have rows returned for all log lines. Ex: DELETE
        out.setRows_returned((jsonRowsReturnedValue != null) ? 
                jsonRowsReturnedValue.intValue() : 0);
        
        out.setUser_id(sql_log.getUser_id());
        out.setApp_id(sql_log.getApp_id());
        out.setSql_log_id(sql_log.getSql_log_id());
        out.setParent_analytics_id(-1);
        out.setSql_height(0);
        out.setQuery_type((String) JSON_Obj.get("Action"));
            
        return out;
    }
    
    public static String getLogEntryRow(String query)
    {
        
       return null; 
    }
    
    class SelectUnionParser implements SelectVisitor, SelectItemVisitor{

        int numberOfSelect = 0;
        int numberOfUnion = 0;
        int numberOfProjectItems = 0;
        int project_star_count = -1;
        
        public Analytics populateAnalyticsEntity(Analytics _analyticsEntity){
            _analyticsEntity.setProject_col_count(numberOfProjectItems);
            _analyticsEntity.setProject_star_count(project_star_count);
            
            return _analyticsEntity;
        }
        
        @Override
        public void visit(PlainSelect _ps) {
            numberOfSelect++;
            List<SelectItem> selectItems = _ps.getSelectItems();
            for(SelectItem selectItem : selectItems){
                selectItem.accept(this);
            }
        }

        @Override
        public void visit(AllColumns _ac) {
            project_star_count = 0;
        }

        @Override
        public void visit(AllTableColumns _atc) {
            project_star_count = (project_star_count == -1) ? 1 : (project_star_count + 1);
        }

        @Override
        public void visit(SelectExpressionItem _sei) {
            numberOfProjectItems++;
        }

        @Override
        public void visit(SetOperationList _sol) {
            List<SetOperation> setOps = _sol.getOperations();
            for(SetOperation setOp : setOps){
                if(setOp.toString().equalsIgnoreCase("UNION")){
                    numberOfUnion++;
                    throw new IncompleteLogicError("UNION is not completely implmented yet");
                } else {
                    throw new UnsupportedOperationException("Set Op: "+ setOp.toString() + " Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            }
        }

        @Override
        public void visit(WithItem _wi) {
            throw new UnsupportedOperationException("With is Not supported yet.");
        }
        
    }
    
}
