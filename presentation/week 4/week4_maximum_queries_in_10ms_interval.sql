select * from 
(Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 1
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t1
union 

select * from (
Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 2
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t2

union 

select * from 
(Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 3
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t3
union 

select * from (
Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 4
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t4

union

select * from (
Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 5
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t5

union

select * from 
(Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 6
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t6
union 

select * from (
Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 7
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t7

union

select * from 
(Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 8
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t8

union 

select * from (
Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 9
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t9

union 

select * from (
Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 10
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t10

union 

select * from (
Select count(*) as no_of_queries,user_id, avg(time_taken) as avg_response_time ,ticks div 10 as ten_ms_interval
from analytics
where user_id = 11
group by user_id, ten_ms_interval
order by no_of_queries desc
limit 1) t11

order by no_of_queries desc


