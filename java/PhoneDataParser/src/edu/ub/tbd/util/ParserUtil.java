/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author san
 */
public class ParserUtil {
    
    /*
     * We are only accounting for upper case PRAGMA for performance efficiency.
     * Though SQL is case insensitive. Checked the current log and only 36 SQLs are in lower case pragma.
     * For now they will not be parsed and pushed to unparsedsqls.
     * Case-sensitive: PRAGMA ==> 6562115 (parsed) + 2266 (unparsed) | pragma ==> 36 + 0 (unparsed)
     * Case-insensitive: pragma ==> 6562151
     * Conclusion: Hence no weird mixing of cases withing same SQL
     */
    //TODO: <Sankar> fix PRAGMA later if data changes
    public final static Pattern PATTERN_PRAGMA = Pattern.compile("^PRAGMA");
    
    public static boolean isPRAGMA_Query(final String sql){
        return PATTERN_PRAGMA.matcher(sql).find();
    }
    
    public static StringBuilder convertToCSV(String... stringVals){
        if(stringVals == null || stringVals.length == 0){
            return null;
        }
        
        StringBuilder out = new StringBuilder();
        for(int i = 0, size = stringVals.length; i < size; i++){
            out.append("\"").append(stringVals[i]).append("\"");
            if(i < size-1){
                out.append(",");
            }
        }
        
        return out;
    }
    
    public static String applyArgumentsToSQLQuery(String _argument_String, String _sql_query_String){
        int numberOfArgsInSQL = StringUtils.countMatches(_sql_query_String, '?');
        if(numberOfArgsInSQL > 0){
            List<String> _argument_List = jsonArgumentStringToList(_argument_String);
            for(int i = 1; i <= numberOfArgsInSQL; i++){
                String arg_Value = getArgValueForArgIndex(_argument_List, i);
                _sql_query_String = applyOneArgumentToSQLQuery(arg_Value, _sql_query_String);
            }
            return _sql_query_String;
        } else {
            return _sql_query_String;
        }
    }
    
    private static String applyOneArgumentToSQLQuery(String _single_arg_val, String _sql_query_String){
        return _sql_query_String.replaceFirst("\\?", "'" + _single_arg_val +"'");
    }
    
    private static String getArgValueForArgIndex(List<String> _multi_arg_val, int i){
        String out = null;
        try {
            out = _multi_arg_val.get(i-1);
            return out;
        } catch (IndexOutOfBoundsException e) {
            return "fak_" + String.valueOf(i); // Return fake value as value of i with fak prefix
        }
    }
    
    /**
     * 
     * @param str_Arg
     * @return Incase of empty str_Arg it returns empty ArrayList and never returns null
     */
    public static List<String> jsonArgumentStringToList(String str_Arg){
        List<String> out = new ArrayList<>();
        
        if(str_Arg != null && !str_Arg.isEmpty()){
            //The order is important here
            str_Arg = str_Arg.trim();
            str_Arg = str_Arg.replaceFirst("^\\[", "");
            str_Arg = str_Arg.replaceFirst("\\]$", "");
            str_Arg = str_Arg.trim();
            str_Arg = str_Arg.replaceFirst(",$", "");
            
            if(!str_Arg.isEmpty() && !str_Arg.equals("null")){
                out = Arrays.asList(str_Arg.split(","));
            }
        }

        return out;
    }
    
    public static List<Expression> getAndClauses(Expression e) {
        List<Expression> out = new ArrayList<Expression>();
        if (e instanceof AndExpression) {
            AndExpression a = (AndExpression) e;
            out.addAll(getAndClauses(a.getLeftExpression()));
            out.addAll(getAndClauses(a.getRightExpression()));
        } else {
            out.add(e);
        }
        return out;
    }
    
}
