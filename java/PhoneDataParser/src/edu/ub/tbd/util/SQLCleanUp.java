/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.util;

/**
 *
 * @author san
 */
public class SQLCleanUp {
    
    public static String cleanUpSQL(String _raw_SQL, String _arguments_HashCoded, 
            String _arguments)
    {
        if(_raw_SQL != null && ! _raw_SQL.isEmpty()){
            
            _raw_SQL = _raw_SQL.trim();
            
            String sql_Junk_Prefix_CleanedUp = cleanUpJunkPrefix(_raw_SQL);
            
            String sql_Arg_Param_CleanedUp = ParserUtil.applyArgumentsToSQLQuery(
                    ((_arguments_HashCoded != null) ? _arguments_HashCoded : _arguments), sql_Junk_Prefix_CleanedUp);
            
            return sql_Arg_Param_CleanedUp;
        } else {
            return _raw_SQL;
        }
        
    }
    
    public static String cleanUpJunkPrefix(String _sql){
        _sql = _sql.replaceFirst("^SQLiteProgram:\\s*", "");
        return _sql;
    }
    
}
