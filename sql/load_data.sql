-- Load user data

SET storage_engine=MYISAM;
ALTER TABLE analytics ENGINE = MyISAM;

 LOAD DATA LOCAL INFILE '/Users/sathish/Desktop/output/analytics.txt' INTO TABLE analytics
FIELDS TERMINATED BY '\t' 
-- ENCLOSED BY '"' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
