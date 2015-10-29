
SET @app_name = 'Hangouts'; -- give the application name here
SET @time_gap_min = 3; -- Give the peak time interval 

DROP TEMPORARY TABLE IF EXISTS peak_min_table;
CREATE TEMPORARY TABLE IF NOT EXISTS peak_min_table AS (
-- works only on mysql. Other db will throw error saying accessing non agrregate function column that is not in group by
-- it's a hack to get only first value ( max) from group by 
select * from (
Select user_id, app.app_name, ticks div (@time_gap_min*60*1000) as peak_min,
count(*) as total_queries
from analytics a
join app 
where a.app_id = app.app_id
and app_name in (@app_name)
group by user_id,a.app_id,peak_min
order by user_id, app_name,total_queries desc) t
group by user_id, app_name);


Select a.user_id, app.app_name, (ticks div 1000 - @time_gap_min*60* (ticks div (@time_gap_min*60*1000))) as sec,
SUM(IF(query_type = 'SELECT',1,0)) as select_total,
SUM(IF(query_type = 'INSERT',1,0)) as insert_total,
SUM(IF(query_type = 'PRAGMA',1,0)) as pragma_total,
SUM(IF(query_type = 'DELETE',1,0)) as delete_total,
SUM(IF(query_type = 'UPDATE',1,0)) as update_total,
count(*) as total_queries
from analytics a
join app 
join peak_min_table p
where a.app_id = app.app_id
and p.user_id = a.user_id
and app.app_name in (@app_name)
and ticks div (@time_gap_min*60*1000) = p.peak_min
group by user_id, sec
order by user_id, sec;