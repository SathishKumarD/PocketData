-- $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
-- $$$$$$$$$$$$$$$    NAMING STYLE 		$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
-- $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
--
-- 1) All column/table names are lower cased
-- 2) Primary Key is named <table_name>_id
-- 3) Create tables have PK as the first column
-- 4) Relationship tables are named <table1_name>_<table2_name>
-- 5) FK names exactly match PK name
-- 6) Each word in the Column/Table are seperated by '_'
-- 7) PK if present in a table should be first column in CREATE STATEMENT
-- 
-- $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
-- $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

CREATE SCHEMA IF NOT EXISTS pocket_data;

use pocket_data;

CREATE TABLE IF NOT EXISTS user(
user_id 			INT NOT NULL,
guid	 			VARCHAR(40), 	-- guid present in the log file
user_name			VARCHAR(20),	-- username for easy remembrance
PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS app(
app_id 				INT NOT NULL, 	
app_name			VARCHAR(20),	-- name of the android application
PRIMARY KEY	 (app_id)
);

CREATE TABLE IF NOT EXISTS sql_log(
log_id 				INT NOT NULL, 	-- log_id is the same for sql_log and Analytics
user_id 			INT,			-- FK to user table
app_id				INT,			-- FK to app table
raw_data			TEXT,	
PRIMARY KEY	 (log_id)
);

CREATE TABLE IF NOT EXISTS analytics(
log_id				INT NOT NULL,  	-- log_id is the same for sql_log and Analytics
ticks 				INT,	
ticks_ms 			DOUBLE,
date_time			DATETIME,	
user_id 			INT,			-- FK to user table
app_id				INT,			-- FK to app table
query_type 			VARCHAR(10), 	-- SELECT, INSERT, UPDATE ETC.,
outerjoin_count		INT,
distinct_count		INT,
limit_count			INT,
orderby_count		INT,
aggregate_count		INT,
groupby_count		INT,
union_count			INT,
join_width			INT,
where_count 		INT,
PRIMARY KEY	 (log_id)
);

CREATE TABLE IF NOT EXISTS unparsed_log_lines(
unparsed_log_lines_id 	INT NOT NULL, 
file_location 			VARCHAR(200),	
file_line_number 		INT,
raw_data				TEXT,
exception_trace 		VARCHAR(400),	
PRIMARY KEY	 (unparsed_log_lines_id)
);

CREATE TABLE IF NOT EXISTS primary_key_sequence(
table_name				VARCHAR(70),
primary_key_col_name	VARCHAR(70),
next_id_value			INT,
PRIMARY KEY	 (table_name, primary_key_col_name)
);