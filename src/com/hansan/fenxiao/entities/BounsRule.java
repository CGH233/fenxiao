package com.hansan.fenxiao.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="bouns_rule")
public class BounsRule extends BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private int level;
	private String directReward;
	private String indirectReward;
	private String regionalReward;
	private String directRetail;
	private String indirectRetail;
	private String twoMaReward;
	private String directRetailRe;
	private String indirectRetailRe;
	private String twoMaRetailRe;
	private String identityName;
	
	public String getIdentityName() {
		return identityName;
	}
	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getDirectReward() {
		return directReward;
	}
	public void setDirectReward(String directReward) {
		this.directReward = directReward;
	}
	public String getIndirectReward() {
		return indirectReward;
	}
	public void setIndirectReward(String indirectReward) {
		this.indirectReward = indirectReward;
	}
	public String getRegionalReward() {
		return regionalReward;
	}
	public void setRegionalReward(String regionalReward) {
		this.regionalReward = regionalReward;
	}
	public String getDirectRetail() {
		return directRetail;
	}
	public void setDirectRetail(String directRetail) {
		this.directRetail = directRetail;
	}
	public String getIndirectRetail() {
		return indirectRetail;
	}
	public void setIndirectRetail(String indirectRetail) {
		this.indirectRetail = indirectRetail;
	}
	public String getTwoMaReward() {
		return twoMaReward;
	}
	public void setTwoMaReward(String twoMaReward) {
		this.twoMaReward = twoMaReward;
	}
	public String getDirectRetailRe() {
		return directRetailRe;
	}
	public void setDirectRetailRe(String directRetailRe) {
		this.directRetailRe = directRetailRe;
	}
	public String getIndirectRetailRe() {
		return indirectRetailRe;
	}
	public void setIndirectRetailRe(String indirectRetailRe) {
		this.indirectRetailRe = indirectRetailRe;
	}
	public String getTwoMaRetailRe() {
		return twoMaRetailRe;
	}
	public void setTwoMaRetailRe(String twoMaRetailRe) {
		this.twoMaRetailRe = twoMaRetailRe;
	}

	
}
