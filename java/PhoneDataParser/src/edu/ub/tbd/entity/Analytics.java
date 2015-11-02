/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.entity;

import java.sql.Timestamp;

/**
 *
 * @author san
 */
public class Analytics extends AbstractEntity implements Comparable<Analytics>{
    private static int curr_PK_ID = 0;
    
    public static int getCurrPKID(){
        return curr_PK_ID;
    }
    
    @Column public int analytics_id;
    @Column public long ticks;
    @Column public double ticks_ms;
    @Column public Timestamp date_time;
    @Column public long time_taken;
    @Column public String arguments;
    @Column public int counter;
    @Column public int rows_returned;
    @Column public int user_id;
    @Column public int app_id;
    @Column public int sql_log_id;
    @Column public int parent_analytics_id;
    @Column public int sql_height;
    @Column public String query_type;
    @Column public int outerjoin_count;
    @Column public int distinct_count;
    @Column public int limit_count;
    @Column public int orderby_count;
    @Column public int aggregate_count;
    @Column public int groupby_count;
    @Column public int union_count;
    @Column public int join_width;
    @Column public int where_count;
    @Column public int project_col_count;
    @Column public int project_star_count; // -1 -> No * in the Project; 0 -> SELECT * FROM.. (All Columns); 1 -> SELLECT A.*, B.NAME, ...; 2 -> A.*, B.*, C.NAME, ...
    @Column public int noOfRelations;
    @Column public int leftOuterJoin_count;
    @Column public int rightOuterJoint_count;
    @Column public int innerJoin_count;
    @Column public int simpleJoin_count;
    @Column public int crossJoin_count;
    @Column public int naturalJoin_count;
    @Column public int selectItems_count;
    //Aggregate Functions.
    @Column public int maxCount;
    @Column public int minCount;
    @Column public int sumCount;
    @Column public int count;
    @Column public int avgCount;
    @Column public int groupConcatCount;
    @Column public int lengthCount;
    @Column public int substrCount;
    @Column public int castCount;
    @Column public int upperCount;
    @Column public int lowerCount;
    @Column public int coalesceCount;
    @Column public int phoneNoEqualCount;
    @Column public int ifNullCount;
    @Column public int julianDayCount;
    @Column public int dateCount;
    @Column public int strfTimeCount;
    
    @Column public int totalWhereClauses;
    
    

	/**
     * Not upto date with instance variables
     * @deprecated 
     * @param _ticks
     * @param _ticks_ms
     * @param _date_time
     * @param _user_id
     * @param _app_id
     * @param _sql_log_id
     * @param _parent_analytics_id
     * @param _sql_height
     * @param _query_type 
     */
    public Analytics(long _ticks, double _ticks_ms, Timestamp _date_time, 
            int _user_id, int _app_id, int _sql_log_id, int _parent_analytics_id, 
            int _sql_height, String _query_type) 
    {
        this(_user_id, _app_id, _sql_log_id, _parent_analytics_id, _sql_height, _query_type);
        this.ticks = _ticks;
        this.ticks_ms = _ticks_ms;
        this.date_time = _date_time;
    }
    
    /**
     * Not upto date with instance variables
     * @deprecated
     * @param _ticks
     * @param _ticks_ms
     * @param _date_time
     * @param _user_id
     * @param _app_id
     * @param _sql_log_id
     * @param _parent_analytics_id
     * @param _sql_height
     * @param _query_type 
     */
    public Analytics(String _ticks, String _ticks_ms, String _date_time, 
            int _user_id, int _app_id, int _sql_log_id, int _parent_analytics_id, 
            int _sql_height, String _query_type)
    {
        this(_user_id, _app_id, _sql_log_id, _parent_analytics_id, _sql_height, _query_type);
        this.ticks = Long.parseLong(_ticks);
        this.ticks_ms = Double.parseDouble(_ticks_ms);
        //this.date_time = _date_time;
    }

    
    private Analytics(int _user_id, int _app_id, int _sql_log_id, 
            int _parent_analytics_id, int _sql_height, String _query_type) 
    {
        this.analytics_id = ++curr_PK_ID;
        this.user_id = _user_id;
        this.app_id = _app_id;
        this.sql_log_id = _sql_log_id;
        this.parent_analytics_id = _parent_analytics_id;
        this.sql_height = _sql_height;
        this.query_type = _query_type;
    }
    
    public Analytics(){
        this.analytics_id = ++curr_PK_ID;
    }
    
    public void setDate_time(long _ticks_ms) {
        setDate_time(new Timestamp(_ticks_ms));
    }

    public static int compare(Analytics _o1, Analytics _o2) {
        return (_o1.analytics_id < _o2.analytics_id) ? -1 : ((_o1.analytics_id == _o2.analytics_id) ? 0 : 1);
    }
    
    @Override
    public int compareTo(Analytics _o) {
        return compare(this, _o);
    }

    @Override
    public int hashCode() {
        return this.analytics_id;
    }

    @Override
    public boolean equals(Object _obj) {
        if(_obj == null || !(_obj instanceof Analytics)){
            return false;
        }
        return this.analytics_id == ((Analytics) _obj).analytics_id;
    }

    /***************************************************************************
     ***************************************************************************
     *******************        GETTERS & SETTERS            *******************
     ***************************************************************************
     ***************************************************************************
     */
    
    public int getAnalytics_id() {
        return analytics_id;
    }

    public void setAnalytics_id(int _analytics_id) {
        this.analytics_id = _analytics_id;
    }

    public long getTicks() {
        return ticks;
    }

    public void setTicks(long _ticks) {
        this.ticks = _ticks;
    }

    public double getTicks_ms() {
        return ticks_ms;
    }

    public void setTicks_ms(double _ticks_ms) {
        this.ticks_ms = _ticks_ms;
    }

    public Timestamp getDate_time() {
        return date_time;
    }

    public void setDate_time(Timestamp _date_time) {
        this.date_time = _date_time;
    }

    public long getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(long _time_taken) {
        this.time_taken = _time_taken;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String _arguments) {
        this.arguments = _arguments;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int _counter) {
        this.counter = _counter;
    }

    public int getRows_returned() {
        return rows_returned;
    }

    public void setRows_returned(int _rows_returned) {
        this.rows_returned = _rows_returned;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int _user_id) {
        this.user_id = _user_id;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int _app_id) {
        this.app_id = _app_id;
    }

    public int getSql_log_id() {
        return sql_log_id;
    }

    public void setSql_log_id(int _sql_log_id) {
        this.sql_log_id = _sql_log_id;
    }

    public int getParent_analytics_id() {
        return parent_analytics_id;
    }

    public void setParent_analytics_id(int _parent_analytics_id) {
        this.parent_analytics_id = _parent_analytics_id;
    }

    public int getSql_height() {
        return sql_height;
    }

    public void setSql_height(int _sql_height) {
        this.sql_height = _sql_height;
    }

    public String getQuery_type() {
        return query_type;
    }

    public void setQuery_type(String _query_type) {
        this.query_type = _query_type;
    }

    public int getOuterjoin_count() {
        return outerjoin_count;
    }

    public void setOuterjoin_count(int _outerjoin_count) {
        this.outerjoin_count = _outerjoin_count;
    }

    public int getDistinct_count() {
        return distinct_count;
    }

    public void setDistinct_count(int _distinct_count) {
        this.distinct_count = _distinct_count;
    }

    public int getLimit_count() {
        return limit_count;
    }

    public void setLimit_count(int _limit_count) {
        this.limit_count = _limit_count;
    }

    public int getOrderby_count() {
        return orderby_count;
    }

    public void setOrderby_count(int _orderby_count) {
        this.orderby_count = _orderby_count;
    }

    public int getAggregate_count() {
        return aggregate_count;
    }

    public void setAggregate_count(int _aggregate_count) {
        this.aggregate_count = _aggregate_count;
    }

    public int getGroupby_count() {
        return groupby_count;
    }

    public void setGroupby_count(int _groupby_count) {
        this.groupby_count = _groupby_count;
    }

    public int getUnion_count() {
        return union_count;
    }

    public void setUnion_count(int _union_count) {
        this.union_count = _union_count;
    }

    public int getJoin_width() {
        return join_width;
    }

    public void setJoin_width(int _join_width) {
        this.join_width = _join_width;
    }

    public int getWhere_count() {
        return where_count;
    }

    public void setWhere_count(int _where_count) {
        this.where_count = _where_count;
    }

    public int getProject_col_count() {
        return project_col_count;
    }

    public void setProject_col_count(int _project_col_count) {
        this.project_col_count = _project_col_count;
    }

    public int getProject_star_count() {
        return project_star_count;
    }

    public void setProject_star_count(int _project_star_count) {
        this.project_star_count = _project_star_count;
    }
    
    public int getNoOfRelations() {
		return noOfRelations;
	}

	public void setNoOfRelations(int noOfRelations) {
		this.noOfRelations = noOfRelations;
	}

	public int getLeftOuterJoin_count() {
		return leftOuterJoin_count;
	}

	public void setLeftOuterJoin_count(int leftOuterJoin_count) {
		this.leftOuterJoin_count = leftOuterJoin_count;
	}

	public int getRightOuterJoint_count() {
		return rightOuterJoint_count;
	}

	public void setRightOuterJoint_count(int rightOuterJoint_count) {
		this.rightOuterJoint_count = rightOuterJoint_count;
	}

	public int getInnerJoin_count() {
		return innerJoin_count;
	}

	public void setInnerJoin_count(int innerJoin_count) {
		this.innerJoin_count = innerJoin_count;
	}

	public int getSimpleJoin_count() {
		return simpleJoin_count;
	}

	public void setSimpleJoin_count(int simpleJoin_count) {
		this.simpleJoin_count = simpleJoin_count;
	}

	public int getCrossJoin_count() {
		return crossJoin_count;
	}

	public void setCrossJoin_count(int crossJoin_count) {
		this.crossJoin_count = crossJoin_count;
	}

	public int getNaturalJoin_count() {
		return naturalJoin_count;
	}

	public void setNaturalJoin_count(int naturalJoin_count) {
		this.naturalJoin_count = naturalJoin_count;
	}
	
    public int getSelectItems_count() {
		return selectItems_count;
	}

	public void setSelectItems_count(int selectItems_count) {
		this.selectItems_count = selectItems_count;
	}
	
	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}

	public int getSumCount() {
		return sumCount;
	}

	public void setSumCount(int sumCount) {
		this.sumCount = sumCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAvgCount() {
		return avgCount;
	}

	public void setAvgCount(int avgCount) {
		this.avgCount = avgCount;
	}

	public int getGroupConcatCount() {
		return groupConcatCount;
	}

	public void setGroupConcatCount(int groupConcatCount) {
		this.groupConcatCount = groupConcatCount;
	}

	public int getLengthCount() {
		return lengthCount;
	}

	public void setLengthCount(int lengthCount) {
		this.lengthCount = lengthCount;
	}

	public int getSubstrCount() {
		return substrCount;
	}

	public void setSubstrCount(int substrCount) {
		this.substrCount = substrCount;
	}

	public int getCastCount() {
		return castCount;
	}

	public void setCastCount(int castCount) {
		this.castCount = castCount;
	}

	public int getUpperCount() {
		return upperCount;
	}

	public void setUpperCount(int upperCount) {
		this.upperCount = upperCount;
	}

	public int getLowerCount() {
		return lowerCount;
	}

	public void setLowerCount(int lowerCount) {
		this.lowerCount = lowerCount;
	}

	public int getCoalesceCount() {
		return coalesceCount;
	}

	public void setCoalesceCount(int coalesceCount) {
		this.coalesceCount = coalesceCount;
	}

	public int getPhoneNoEqualCount() {
		return phoneNoEqualCount;
	}

	public void setPhoneNoEqualCount(int phoneNoEqualCount) {
		this.phoneNoEqualCount = phoneNoEqualCount;
	}

	public int getIfNullCount() {
		return ifNullCount;
	}

	public void setIfNullCount(int ifNullCount) {
		this.ifNullCount = ifNullCount;
	}

	public int getJulianDayCount() {
		return julianDayCount;
	}

	public void setJulianDayCount(int julianDayCount) {
		this.julianDayCount = julianDayCount;
	}

	public int getDateCount() {
		return dateCount;
	}

	public void setDateCount(int dateCount) {
		this.dateCount = dateCount;
	}

	public int getStrfTimeCount() {
		return strfTimeCount;
	}

	public void setStrfTimeCount(int strfTimeCount) {
		this.strfTimeCount = strfTimeCount;
	}

	public int getTotalWhereClauses() {
		return totalWhereClauses;
	}

	public void setTotalWhereClauses(int totalWhereClauses) {
		this.totalWhereClauses = totalWhereClauses;
	}
	
	
}
