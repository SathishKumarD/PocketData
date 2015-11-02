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
import edu.ub.tbd.util.ParserUtil;
import edu.ub.tbd.util.SQLCleanUp;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.pragma.Pragma;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.Union;
//import net.sf.jsqlparser.statement.select.SetOperation;
//import net.sf.jsqlparser.statement.select.SetOperationList;
//import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;

import org.json.simple.JSONObject;

import com.sun.javafx.fxml.expression.BinaryExpression;

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
        String argumentsHashCoded = (String) JSON_Obj.get("Arguments(hashCoded)");
        String arguments = (String) JSON_Obj.get("Arguments");
        
        
        
        String sql_CleanedUp = SQLCleanUp.cleanUpSQL(sql_raw, argumentsHashCoded, arguments);
        
        StringReader stream = new StringReader(sql_CleanedUp);
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
            } else if (stmt instanceof Delete) {
                //TODO: <Sankar> Implement this
            } else if (stmt instanceof Update) {
                //TODO: <Sankar> Implement this
            } else if (stmt instanceof Insert) {
                //TODO: <Sankar> Implement this
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
            
            int joinWidth = 0;
            if(_ps.getJoins() != null) {
            	joinWidth = _ps.getJoins().size();
            }
            parent_analytics.setJoin_width(joinWidth);
            
            int noOfTables = 1;
            if(joinWidth > 0) {
            	HashSet<String> set = new HashSet<>();
            	int crossProduct = 0;
            	int innerJoin = 0;
            	int leftOuterJoin = 0;
            	int naturalJoin = 0;
            	int outerJoin = 0;
            	int rightOuterJoin = 0;
            	int simpleJoin = 0;
                
            	for(Join join : _ps.getJoins()) {
            		set.add(_ps.getFromItem().toString());
            		set.add(join.getRightItem().toString());
            		
            		if(join.isLeft()) {
        				leftOuterJoin++;
        			} else if(join.isRight()) {
        				rightOuterJoin++;
        			} else if(join.isOuter() || join.isFull()){
        				outerJoin++;
        			} else if(join.isSimple()) {
            			simpleJoin++;
            		} else if(join.isNatural()) {
            			naturalJoin++;
            		} /*else if(join.isCross()) {
            			crossProduct++;
            		}*/ else {
            			// CASE: R1 JOIN R2 ON (R1.C1 = R2.C2)
            			innerJoin++;
            		} 
            	}
            	noOfTables = set.size();
            	
            	parent_analytics.setOuterjoin_count(outerJoin);
            	parent_analytics.setLeftOuterJoin_count(leftOuterJoin);
            	parent_analytics.setRightOuterJoint_count(rightOuterJoin);
            	parent_analytics.setSimpleJoin_count(simpleJoin);
            	parent_analytics.setNaturalJoin_count(naturalJoin);
            	parent_analytics.setCrossJoin_count(crossProduct);
            	parent_analytics.setInnerJoin_count(innerJoin);
            }
            
        	parent_analytics.setNoOfRelations(noOfTables);
        	
        	int selectItemsCount = _ps.getSelectItems().size();
        	parent_analytics.setSelectItems_count(selectItemsCount);
        	
        	int avgCount = 0;
        	int maxCount = 0;
        	int minCount = 0;
        	int sumCount = 0;
        	int count = 0;
        	int groupConcat = 0;
        	int lengthCount = 0;
        	int substrCount = 0;
        	int castCount = 0;
        	int upperCount = 0;
        	int lowerCount = 0;
        	int coalesceCount = 0;
        	int phoneNoEqualCount = 0;
        	int strfTimeCount = 0;
        	int ifNullCount = 0;
        	int julianDayCount = 0;
        	int dateCount = 0;
        	for(SelectItem item : _ps.getSelectItems()) {
        		if(item instanceof SelectExpressionItem) {
        			SelectExpressionItem eachItem = (SelectExpressionItem) item;
        			String exprString = eachItem.getExpression().toString().toUpperCase();
        			
        			if(exprString.contains("(")) {
        				// The above check is added for the false negatives. Some of the selection is like AVGCOUNT, MAXSUM etc
            			if(exprString.startsWith("AVG")){
            				avgCount++;
            			} else if(exprString.startsWith("MAX")) {
            				maxCount++;
            			} else if(exprString.startsWith("MIN")) {
            				minCount++;
            			} else if(exprString.startsWith("SUM")) {
            				sumCount++;
            			} else if(exprString.startsWith("COUNT")) {
            				count++;
            			} else if(exprString.startsWith("GROUP_CONCAT")) {
            				groupConcat++;
            			} else if(exprString.startsWith("LENGTH")) {
            				lengthCount++;
            			} else if(exprString.startsWith("SUBSTR")) {
            				substrCount++;
            			} else if(exprString.startsWith("CAST")) {
            				castCount++;
            			} else if(exprString.startsWith("UPPER")) {
            				upperCount++;
            			}  else if(exprString.startsWith("LOWER")) {
            				lowerCount++;
            			} else if(exprString.startsWith("COALESCE")) {
            				coalesceCount++;
            			} else if(exprString.startsWith("PHONE_NUMBERS_EQUAL")) {
            				phoneNoEqualCount++;
            			} else if(exprString.startsWith("STRFTIME")) {
            				strfTimeCount++;
            			} else if(exprString.startsWith("IFNULL")) {
            				ifNullCount++;
            			} else if(exprString.startsWith("JULIANDAY")) {
            				julianDayCount++;
            			} else if(exprString.startsWith("DATE")) {
            				dateCount++;
            			}
        			}
        		}
        	}
        	
        	parent_analytics.setAvgCount(avgCount);
        	parent_analytics.setMaxCount(maxCount);
        	parent_analytics.setMinCount(minCount);
        	parent_analytics.setCount(count);
        	parent_analytics.setSumCount(sumCount);
        	parent_analytics.setGroupConcatCount(groupConcat);
        	parent_analytics.setLengthCount(lengthCount);
        	parent_analytics.setSubstrCount(substrCount);
        	parent_analytics.setCastCount(castCount);
        	parent_analytics.setUpperCount(upperCount);
        	parent_analytics.setLowerCount(lowerCount);
        	parent_analytics.setCoalesceCount(coalesceCount);
        	parent_analytics.setPhoneNoEqualCount(phoneNoEqualCount);
        	parent_analytics.setStrfTimeCount(strfTimeCount);
        	parent_analytics.setIfNullCount(ifNullCount);
        	parent_analytics.setJulianDayCount(julianDayCount);
        	parent_analytics.setDateCount(dateCount);
        	
        	int totalWhereClauses = 0;
        	if(_ps.getWhere() != null) {
        		totalWhereClauses =  ParserUtil.getAndClauses(_ps.getWhere()).size();
        	}
        	
        	parent_analytics.setTotalWhereClauses(totalWhereClauses);
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
        public void visit(Union _union) {
            numberOfUnion++;
            //throw new UnsupportedOperationException("Not supported yet."); //TODO - Fix me 
        }
        
        /*
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
        */
    }
    
}
