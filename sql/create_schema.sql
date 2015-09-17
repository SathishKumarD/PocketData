-- Create Schema
CREATE SCHEMA IF NOT EXISTS pocket_data;

use pocket_data;

-- Create User Table
CREATE TABLE IF NOT EXISTS user(
user_id INT NOT NULL AUTO_INCREMENT,
unique_id 			VARCHAR(40), 		-- guid present in the log file
username		VARCHAR(20),			-- username for easy remembrance
PRIMARY KEY (user_id)
);

-- Create Application table
CREATE TABLE IF NOT EXISTS app(
app_id 			INT NOT NULL AUTO_INCREMENT, 			
appname			VARCHAR(20),			-- name of the android application
PRIMARY KEY	 (app_id)
);

-- Create table UserApps table
CREATE TABLE IF NOT EXISTS user_app(
user_app_id 	INT NOT NULL AUTO_INCREMENT, 			
user_id			INT,
app_id 			INT,						
PRIMARY KEY	 (user_app_id)
);

-- Create table UserApps table
CREATE TABLE IF NOT EXISTS user_app_process(
user_app_process_id 	INT NOT NULL AUTO_INCREMENT, 			
user_id			INT,
app_id 			INT,	
process_id 		INT,
thread_id		INT,					
PRIMARY KEY	 (user_app_process_id)
);



-- Create table sql_log table
CREATE TABLE IF NOT EXISTS sql_log(
log_id 	INT NOT NULL AUTO_INCREMENT, 			
unique_id		INT,
ticks 			INT,	
ticks_ms 		DOUBLE,
date_time		DATETIME,	
process_id		INT,
thread_id		INT,	
raw_data		VARCHAR(400),		
PRIMARY KEY	 (log_id)
) 