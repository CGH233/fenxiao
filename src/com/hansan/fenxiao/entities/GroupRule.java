package com.hansan.fenxiao.entities;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="group_rule")
public class GroupRule extends BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private int level;
	private Double lower_limit;
	private Double upper_limit;
	private String commission;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Double getLower_limit() {
		return lower_limit;
	}
	public void setLower_limit(Double lower_limit) {
		this.lower_limit = lower_limit;
	}
	public Double getUpper_limit() {
		return upper_limit;
	}
	public void setUpper_limit(Double upper_limit) {
		this.upper_limit = upper_limit;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
}
	