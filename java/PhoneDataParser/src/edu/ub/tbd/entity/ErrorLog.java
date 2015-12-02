package edu.ub.tbd.entity;

public class ErrorLog extends AbstractEntity {

	@Column public int app_id;
    @Column public int user_id;
    @Column public String sql;
    
	public ErrorLog(int app_id, int user_id, String sql) {
		this.app_id = app_id;
		this.user_id = user_id;
		this.sql = sql;
	}
	public int getApp_id() {
		return app_id;
	}
	public void setApp_id(int app_id) {
		this.app_id = app_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
    
}
