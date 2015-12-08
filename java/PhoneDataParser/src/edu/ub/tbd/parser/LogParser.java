/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;

import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.beans.LogLineBean;
import edu.ub.tbd.constants.AppConstants;
import edu.ub.tbd.entity.App;
import edu.ub.tbd.entity.UnParsedSQLs;
import edu.ub.tbd.entity.Unparsed_log_lines;
import edu.ub.tbd.entity.User;
import edu.ub.tbd.service.PersistLogDataService;
import edu.ub.tbd.service.PersistanceFileService;
import edu.ub.tbd.service.PersistanceService;
import edu.ub.tbd.util.MacFileNameFilter;
import edu.ub.tbd.util.SQLCleanUp;
import edu.ub.tbd.util.StringUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author sathish
 */
public class LogParser {

    private final String sourceDir;
    private final String fileExtension;
    private final HashMap<String, String> appNameLookUpMap = new HashMap<>();
    
    private JSONParser parser = new JSONParser();
    
    //Cached data to be persisted during app shutdown
    private final HashMap<String, User> usersMap = new HashMap<>();
    private HashMap<String, App> appsMap = new HashMap<>();
    
    
    private final Pattern regx_userGUIDInFilePath; //Use find on this regx and get guid from group 1
    
    //All Persistance Services below. They must be closed during shutdown => in shutDown()
    private final PersistanceService ps_UnparsedLogLines;
    private final PersistLogDataService ps_LogDataService;
    
    /**
     * @deprecated 
     */
    private final PersistanceService ps_UnParsedSQLs;

    public LogParser(String _sourceDir, String _fileExtension) throws Exception{
        this.sourceDir = _sourceDir;
        this.fileExtension = _fileExtension;
        
        String regex_p1 = sourceDir.replaceAll("\\\\", "\\\\\\\\");
        String regexEscFileSep = File.separator.replaceAll("\\\\", "\\\\\\\\");
        String final_Regex = regex_p1 + regexEscFileSep + "(\\p{Alnum}+)" + regexEscFileSep;
        regx_userGUIDInFilePath = Pattern.compile(final_Regex);
        
        this.ps_UnparsedLogLines = new PersistanceFileService(AppConstants.DEST_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, Unparsed_log_lines.class);
        
        this.ps_LogDataService = new PersistLogDataService();
        
        
        this.ps_UnParsedSQLs = new PersistanceFileService(AppConstants.DEST_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, UnParsedSQLs.class);
        
        appsMap.put("", new App("")); //Any SQL for which no app is found will be put into this
    }

    public void parseLogsAndWriteFile() throws Exception{
        ArrayList<String> logFilesToProcess = getLogFilesToProcessFromBaseGZ();
        int counter = 0;
        for (String filePath : logFilesToProcess) {
            System.out.println(filePath);
            counter += 1;
            //TODO: <Satish> remove this. For testing purpose I am just parsing two files.

            /*
            if(counter > 10)
                break;
            */
            
            parseSingleLogFile(filePath);
   
        }
    }
    
    /**
     * Reads the base GZip file and returns only the file paths of the Log GZ files in it
     * @return the log.GZ file locations to be processed
     */
    private ArrayList<String> getLogFilesToProcessFromBaseGZ() {
        ArrayList<String> filesList = new ArrayList<>();
        File[] files = new File(sourceDir).listFiles();
        iterateFolders(files, filesList);
        return filesList;
    }

    /**
     * Helper method used in {@link LogParser#getLogFilesToProcessFromBaseGZ() getLogFilesToProcessFromBaseGZ()}
     * @param files
     * @param filesList Output Object
     */
    private void iterateFolders(File[] files, ArrayList<String> filesList) {
        for (File file : files) {
            String name = file.getName();
            if (file.isDirectory()) {
                iterateFolders(file.listFiles(new MacFileNameFilter()), filesList); // Calls same method again.
            } else if (name.endsWith(fileExtension) && file.getAbsolutePath().contains("SQLite-Query-PhoneLab")) {
                filesList.add(file.getAbsolutePath());
            }
        }
    }

    private void parseSingleLogFile(String _sourceFile) throws Exception{
        parseSingleLogFile(new File(_sourceFile));
    }
    
    private void parseSingleLogFile(File _sourceFile) throws Exception{
        int user_id = extractUser(_sourceFile.getAbsolutePath());
        
        BufferedReader br = null;
        String logLine = null;
        int lineNumber = 0;
        String fileName = _sourceFile.getName();
        
        try {
            
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(_sourceFile));
            br = new BufferedReader(new InputStreamReader(gzip));
            
            //System.out.println(sourceFile);
            while ((logLine = br.readLine()) != null) {
                lineNumber++; String raw_sql = "";
                try {
                    LogLineBean logLineBean = new LogLineBean(logLine.split(AppConstants.SRC_LOG_FILE_VALUE_SEPERATOR));

                    //TODO: <Sankar> Check how WARN and other levels function
                    if(LogLevel.INFO.equals(logLineBean.getLog_level())){ //Non-INFO log level has no JSON to process

                        JSONObject JSON_Obj = (JSONObject) parser.parse(logLineBean.getLog_msg());

                        switch ((String) JSON_Obj.get("Action")) {
                            case "APP_NAME":
                                {
                                    String appName = (String) JSON_Obj.get("AppName");
                                    String key = logLineBean.get_app_key();
                                    this.appNameLookUpMap.put(key, appName);
                                    if(!appsMap.containsKey(appName)){
                                        appsMap.put(appName, new App(appName));
                                    }
                                }
                                break;
                            case "SCHEMA":
                                //TODO: <Professor> What are Actions : ["SCHEMA", "TABLES"]?
                                //TODO: <Sankar> Actions : ["SCHEMA", "TABLES"] is not into Analytics table yet
                                logUnparsableLine(_sourceFile.getAbsolutePath(), lineNumber, logLine, "ACTION = SCHEMA");
                                break;
                            case "TABLES":
                                logUnparsableLine(_sourceFile.getAbsolutePath(), lineNumber, logLine, "ACTION = TABLES");
                                break;
                            default: //TODO : <Sankar> Implement all the pausible ACTION and throw the Assertion for default and throw new AssertionError();
                                //This is a valid SQL log entry carrying line which needs to be parsed to create LogData Object
                                
                                raw_sql = (String) JSON_Obj.get("Results");
                                String raw_sql_hash = StringUtil.getSHAHash(raw_sql);
                                
                                String appName = ""; appName = lookUpAppName(logLineBean); appName = (appName != null) ? appName : "";
                                App app = appsMap.get(appName);
                                if(AppConstants.MODE_OBJECT_GEN && AppConstants.OBJECTS_GEN_FILTER_FORSCHEMAGEN){
                                    if(app == null || !app.addSQLHash(raw_sql_hash)){
                                        continue; //Skip all the below process and read the next line. Similar SQL was already parsed for this app
                                    }
                                }
                                String arguments = (JSON_Obj.get("Arguments(hashCoded)") != null) 
                                                        ? (String) JSON_Obj.get("Arguments(hashCoded)") 
                                                        : (String) JSON_Obj.get("Arguments");
                                String cleanUp_sql = SQLCleanUp.cleanUpSQL(raw_sql, arguments);
                                StringReader stream = new StringReader(cleanUp_sql);
                                CCJSqlParser jsqlParser = new CCJSqlParser(stream);

                                Statement stmt = null;
                                try {
                                    stmt = jsqlParser.Statement();
                                } catch (net.sf.jsqlparser.parser.ParseException e) {
                                    logUnparsedSQLs(_sourceFile.getAbsolutePath(), lineNumber, raw_sql, cleanUp_sql); //TODO: <Sankar> Remove this catch and leave the below catch Throwable after most of the issues are fixed
                                    throw e;
                                }
                                
                                //At this point the Log line is completely valid.
                                //Create the LogData Object and persist to file system.
                                
                                LogData ld = createLogData(user_id, logLineBean, JSON_Obj, cleanUp_sql, stmt);
                                ps_LogDataService.write(ld);
                        }
                        
                    } else {
                        logUnparsableLine(_sourceFile.getAbsolutePath(), lineNumber, logLineBean.getLog_msg(), 
                                "LogLevel = " + logLineBean.getLog_level());
                    }
                    
                } catch (ParseException e) {
                    logUnparsableLine(_sourceFile.getAbsolutePath(), lineNumber, logLine, "JSON Parse exception");
                } catch (ArrayIndexOutOfBoundsException e) {
                    logUnparsableLine(_sourceFile.getAbsolutePath(), lineNumber, logLine, "ArrayIndexOutOfBoundsException : Invalid number of columns in the Line");
                } catch (net.sf.jsqlparser.parser.ParseException e) {
                    //Do Nothing here. As this exception is caught above and handled and re-thrown
                } catch (Throwable e) {
                    //System.out.println("WEIRD_EXCEPTION | SQL: " + raw_sql);
                    //e.printStackTrace();
                    logUnparsedSQLs(_sourceFile.getAbsolutePath(), lineNumber, raw_sql, null); //TODO: <Sankar> Remove this catch and leave the below catch Throwable after most of the issues are fixed
                } finally {
                    parser.reset();
                }
            }
        } catch (Throwable e) {
            System.out.println("Unknown Exception/Error | LogLine : " + logLine);
            throw e;
        }
        finally {
            if(br != null){
                br.close();
            }
        }
    }
    
    private LogData createLogData(int _user_id, LogLineBean _logLineBean, JSONObject _JSON_Obj, String _clean_sql, Statement _stmt){
        LogData out = new LogData();
        
        out.setTicks(Long.parseLong(_logLineBean.getTicks()));
        out.setTicks_ms(Double.parseDouble(_logLineBean.getTicks_ms()));
        out.setTime_taken((Long) _JSON_Obj.get("Time"));
        out.setCounter(((Number) _JSON_Obj.get("Counter")).intValue());
        
        Number jsonRowsReturnedValue = (Number) _JSON_Obj.get("Rows returned"); // The JSON need not have rows returned for all log lines. Ex: DELETE
        out.setRows_returned((jsonRowsReturnedValue != null) ? 
                jsonRowsReturnedValue.intValue() : 0);
        
        out.setUser_id(_user_id);
        
        String appName = "";
        if ((appName = lookUpAppName(_logLineBean)) != null) {
            // I have an app name
            out.setApp_id(appsMap.get(appName).getApp_id());
        }
        
        out.setAction((String) _JSON_Obj.get("Action"));
        out.setSql(_clean_sql);
        out.setStmt(_stmt);
        
        return out;
    }
    
    private String lookUpAppName(LogLineBean _logLineBean){
        String key = _logLineBean.get_app_key();
        return this.appNameLookUpMap.get(key);
    }
    
    /**
     * Extracts the user info from the filePath provided and adds 
     *    to {@link LogParser#usersMap usersMap}<br>
     * User info is present at {@link LogParser#sourceDir sourceDir}+File.separator+"logcat"+File.separator+<b>[user_guid]</b>+File.separator+...<p>
     * Example: <br>
     * _filePath = /.../logcat/<b>0ee9cead2e2a3a58a316dc27571476e8973ff944</b>/tag/SQLite-Query-PhoneLab/2015/03/22.out.gz <br>
     * 
     * @param _filePath absolute path of the log file being processed
     */
    private int extractUser(String _filePath){
        int out = -1;
        Matcher m = regx_userGUIDInFilePath.matcher(_filePath);
        if(m.find()){
            String user_guid = m.group(1);
            if(!usersMap.containsKey(user_guid)){
                usersMap.put(user_guid, new User(user_guid));
            }
            out = usersMap.get(user_guid).getUser_id();
        }
        
        return out;
    }
    
    private void logUnparsableLine(String _file_location, int _file_line_number, 
            String _raw_data,  String _exception_trace) throws Exception
    {
        
        Unparsed_log_lines unparsedLogLine = new Unparsed_log_lines(
                _file_location, _file_line_number, _raw_data, _exception_trace);
        
        ps_UnparsedLogLines.write(unparsedLogLine);
    }
    
    private void logUnparsableLine(String _file_location, int _file_line_number, 
            String _raw_data) throws Exception
    {
        logUnparsableLine(_file_location, _file_line_number, _raw_data, " ");
    }
    
    private void logUnparsedSQLs(String _file_location, int _file_line_number, 
            String _raw_sql, String _actual_sql_parsed) throws Exception
    {
        ps_UnParsedSQLs.write(new UnParsedSQLs(_file_location, _file_line_number, _raw_sql, _actual_sql_parsed));
    }
    
    /**
     * This method will persist the cached data : <br> 
     * usersMap, unparsed_log_lines_list, App, User_app_process, 
     */
    public void persistCacheData() throws Exception{
        System.out.println("Persisting usersMap....");
        persistUserData();

        System.out.println("Persisting appsMap....");
        persistAppData();

    }
    
    private void persistUserData() throws Exception{
        
        ArrayList<User> sortedUsers = new ArrayList<>(usersMap.size());
        sortedUsers.addAll(usersMap.values());
        Collections.sort(sortedUsers);  //Sorting is good here to insert in the ordered manner. Not Mandatory
        
        PersistanceService ps = new PersistanceFileService(AppConstants.DEST_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, User.class);
        
        for(User user : sortedUsers){
            ps.write(user);
        }
        ps.close();
    }
    
    private void persistAppData() throws Exception{
        
        ArrayList<App> sortedApps = new ArrayList<>(appsMap.size());
        sortedApps.addAll(appsMap.values());
        Collections.sort(sortedApps);   //Sorting is good here to insert in the ordered manner. Not Mandatory
        
        PersistanceService ps = new PersistanceFileService(AppConstants.DEST_FOLDER, 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, App.class);
        
        for(App app : sortedApps){
            ps.write(app);
        }
        ps.close();
    }
    
    private void printUniqueSQLCount(){
        StringBuilder out = new StringBuilder();
        for(App app : appsMap.values()){
            out.append(app.getApp_id()).append("\t|\t").append(app.getApp_name()).append("\t|\t\t\t\t\t\t\t\t").append(app.getUniqueSQLHashCount()).append("\n");
        }
        
        System.out.println(out);
    }
    
    public void shutDown() throws Exception{
        persistCacheData();
        ps_UnparsedLogLines.close();
        ps_LogDataService.close();
        ps_UnParsedSQLs.close();
        //printUniqueSQLCount();
    }
    
    /*
     * ##############################################################################
     *   All the below are back-up methods to are TO-BE deleted when decided unfit
     * ##############################################################################
     */

}
