/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonedataparser;
import java.io.File;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.select.Select;

import net.sf.jsqlparser.statement.select.SelectBody;
import java.io.StringReader;
import test.TestParser;
/**
 *
 * @author sathish
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /*System.out.println("hello");
        String query = "select employee_name from employee where employee_id =23";
        CCJSqlParser parser = new CCJSqlParser(new StringReader(query));
        try
        {
	SelectBody select = ((Select) parser.Statement()).getSelectBody();
        System.out.println(select.toString());
        }
        
        catch(ParseException p)
        {
            System.out.println(p);
        }*/
        
        TestParser.test_writeToLog();
    }
    
    public static void showFiles(File[] files) {
    for (File file : files) {
        if (file.isDirectory()) {
            System.out.println("Directory: " + file.getName());
            showFiles(file.listFiles()); // Calls same method again.
        } else {
            System.out.println("File: " + file.getName());
        }
    }
}

    
    
}