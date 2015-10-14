
-- data for graph
Select  Cast(rows_returned/10 as Unsigned)  as rows_range,
SUM(IF(project_col_count>0,1,0)) as queries_with_project_columns,
AVG(IF(project_col_count>0,time_taken,0)) as project_col_time,
-- AVG(IF(project_col_count>0,rows_returned,0)) as avg_project_col_rows,
SUM(IF(project_star_count=0,1,0)) as queries_with_all_star,
AVG(IF(project_star_count=0,time_taken,0)) as all_star_time,
-- AVG(IF(project_star_count=0,rows_returned,0)) as avg_all_star_rows,
SUM(IF(project_star_count>0,1,0)) as queries_with_table_star,
AVG(IF(project_star_count>0,time_taken,0)) as avg_table_star_time
-- AVG(IF(project_star_count>0,rows_returned,0)) as all_star_count_rows
from analytics
where query_type = 'Select'
group by rows_range
order  by rows_range asc;


-- get the number of rows returned for different types of projected columns
Select  
SUM(IF(project_col_count>0,1,0)) as queries_with_project_columns,
AVG(IF(project_col_count>0,time_taken,0)) as project_col_time,
 AVG(IF(project_col_count>0,rows_returned,0)) as avg_project_col_rows,
SUM(IF(project_star_count=0,1,0)) as queries_with_all_star,
AVG(IF(project_star_count=0,time_taken,0)) as all_star_time,
 AVG(IF(project_star_count=0,rows_returned,0)) as avg_all_star_rows,
SUM(IF(project_star_count>0,1,0)) as queries_with_table_star,
AVG(IF(project_star_count>0,time_taken,0)) as avg_table_star_time,
 AVG(IF(project_star_count>0,rows_returned,0)) as all_star_count_rows
from analytics
where query_type = 'Select';



-- get the number of rows returned ( where rows>1) for different types of projected columns
Select  
SUM(IF(project_col_count>0,1,0)) as queries_with_project_columns,
AVG(IF(project_col_count>0,time_taken,0)) as project_col_time,
 AVG(IF(project_col_count>0,rows_returned,0)) as avg_project_col_rows,
SUM(IF(project_star_count=0,1,0)) as queries_with_all_star,
AVG(IF(project_star_count=0,time_taken,0)) as all_star_time,
 AVG(IF(project_star_count=0,rows_returned,0)) as avg_all_star_rows,
SUM(IF(project_star_count>0,1,0)) as queries_with_table_star,
AVG(IF(project_star_count>0,time_taken,0)) as avg_table_star_time,
 AVG(IF(project_star_count>0,rows_returned,0)) as all_star_count_rows
from analytics
where query_type = 'Select'
and rows_returned >1








