package com.maller.cowin.model;

public class District {
	private int state_id;
	private int district_id;
	private String district_name;
	private String district_name_1;
	
	public District() {}
	
	public District(int state_id, int district_id, String district_name, String district_name_1) {
		this.state_id = state_id;
		this.district_id = district_id;
		this.district_name = district_name;
		this.district_name_1 = district_name_1;
	}
	
	public int getState_id() {
		return state_id;
	}
	public void setState_id(int state_id) {
		this.state_id = state_id;
	}
	public int getDistrict_id() {
		return district_id;
	}
	public void setDistrict_id(int district_id) {
		this.district_id = district_id;
	}
	public String getDistrict_name() {
		return district_name;
	}
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}
	public String getDistrict_name_1() {
		return district_name_1;
	}
	public void setDistrict_name_1(String district_name_1) {
		this.district_name_1 = district_name_1;
	}

	@Override
	public String toString() {
		return "District [state_id=" + state_id + ", district_id=" + district_id + ", district_name=" + district_name
				+ ", district_name_1=" + district_name_1 + "]";
	}
}
