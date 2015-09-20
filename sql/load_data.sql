-- Load user data
LOAD DATA LOCAL INFILE '/Users/sathish/Dropbox/UB_Fall_2015/662- DB/code/sql/data/insert_user_data.sql' INTO TABLE user
FIELDS TERMINATED BY '|' 
-- ENCLOSED BY '"' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(unique_id,username);

