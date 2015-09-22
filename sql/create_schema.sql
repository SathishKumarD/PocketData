-- Create Schema
CREATE SCHEMA IF NOT EXISTS pocket_data;

use pocket_data;

-- Create User Table
CREATE TABLE IF NOT EXISTS user(
unique_id 			VARCHAR(40), 		-- guid present in the log file
username		VARCHAR(20),			-- username for easy remembrance
user_id INT NOT NULL AUTO_INCREMENT,
PRIMARY KEY (user_id)
);

-- Create Application table
CREATE TABLE IF NOT EXISTS app(
appname			VARCHAR(20),			-- name of the android application
app_id 			INT NOT NULL AUTO_INCREMENT, 	
PRIMARY KEY	 (app_id)
);

-- Create table UserApps table
CREATE TABLE IF NOT EXISTS user_app(
user_id			INT,
app_id 			INT,						
user_app_id 	INT NOT NULL AUTO_INCREMENT,
PRIMARY KEY	 (user_app_id)
);

-- Create table UserApps table
CREATE TABLE IF NOT EXISTS user_app_process(
user_id			INT,
app_id 			INT,	
process_id 		INT,
thread_id		INT,					
user_app_process_id 	INT NOT NULL AUTO_INCREMENT, 
PRIMARY KEY	 (user_app_process_id)
);



-- Create table sql_log table
CREATE TABLE IF NOT EXISTS sql_log(
ticks 			INT,	
ticks_ms 		DOUBLE,
date_time		DATETIME,	
process_id		INT,
thread_id		INT,	
raw_data		VARCHAR(400),		
log_id 	INT NOT NULL AUTO_INCREMENT, 
PRIMARY KEY	 (log_id)
);

-- Create table sql_log table
CREATE TABLE IF NOT EXISTS analytics(

ticks 			INT,	
ticks_ms 		DOUBLE,
date_time		DATETIME,	
user_id 		INT,
app_name		INT,	
query_type 			VARCHAR(10), -- SELECT, INSERT, UPDATE ETC.,
outerjoin_count		INT,
distinct_count		INT,
limit_count			INT,
orderby_count		INT,
aggregate_count	INT,
groupby_count	INT,
union_count		INT,
join_width		INT,
where_count 	INT,
log_id			INT NOT NULL AUTO_INCREMENT,

PRIMARY KEY	 (log_id)
); 
