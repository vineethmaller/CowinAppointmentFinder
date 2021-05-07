package com.maller.cowin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Center {
	private String center_id;
	private String name;
	private String name_l;
	private String address;
	private String address_l;
	private String state_name;
	private String state_name_l;
	private String district_name;
	private String district_name_l;
	private String block_name;
	private String block_name_l;
	private String pincode;
	private String lat;
	private String longitude;
	private String from;
	private String to;
	private String fee_type;
	private List<Map<String, String>> vaccine_fees; 
	private List<Session> sessions;
	
	public Center() {}
	
	public Center(String center_id, String name, String name_l, String address, String address_l, String state_name,
			String state_name_l, String district_name, String district_name_l, String block_name, String block_name_l,
			String pincode, String lat, String longitude, String from, String to, String fee_type,
			List<Map<String, String>> vaccine_fees, List<Session> sessions) {
		super();
		this.center_id = center_id;
		this.name = name;
		this.name_l = name_l;
		this.address = address;
		this.address_l = address_l;
		this.state_name = state_name;
		this.state_name_l = state_name_l;
		this.district_name = district_name;
		this.district_name_l = district_name_l;
		this.block_name = block_name;
		this.block_name_l = block_name_l;
		this.pincode = pincode;
		this.lat = lat;
		this.longitude = longitude;
		this.from = from;
		this.to = to;
		this.fee_type = fee_type;
		this.vaccine_fees = vaccine_fees;
		this.sessions = sessions;
	}
	
	public String getCenter_id() {
		return center_id;
	}
	public void setCenter_id(String center_id) {
		this.center_id = center_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName_l() {
		return name_l;
	}
	public void setName_l(String name_l) {
		this.name_l = name_l;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress_l() {
		return address_l;
	}
	public void setAddress_l(String address_l) {
		this.address_l = address_l;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public String getState_name_l() {
		return state_name_l;
	}
	public void setState_name_l(String state_name_l) {
		this.state_name_l = state_name_l;
	}
	public String getDistrict_name() {
		return district_name;
	}
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}
	public String getDistrict_name_l() {
		return district_name_l;
	}
	public void setDistrict_name_l(String district_name_l) {
		this.district_name_l = district_name_l;
	}
	public String getBlock_name() {
		return block_name;
	}
	public void setBlock_name(String block_name) {
		this.block_name = block_name;
	}
	public String getBlock_name_l() {
		return block_name_l;
	}
	public void setBlock_name_l(String block_name_l) {
		this.block_name_l = block_name_l;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLong() {
		return longitude;
	}
	public void setLong(String longitude) {
		this.longitude = longitude;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public List<Map<String, String>> getVaccine_fees() {
		return vaccine_fees;
	}
	public void setVaccine_fees(List<Map<String, String>> vaccine_fees) {
		this.vaccine_fees = vaccine_fees;
	}
	public List<Session> getSessions() {
		return sessions;
	}
	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	@Override
	public String toString() {
		return "Center [center_id=" + center_id + ", name=" + name + ", name_l=" + name_l + ", address=" + address
				+ ", address_l=" + address_l + ", state_name=" + state_name + ", state_name_l=" + state_name_l
				+ ", district_name=" + district_name + ", district_name_l=" + district_name_l + ", block_name="
				+ block_name + ", block_name_l=" + block_name_l + ", pincode=" + pincode + ", lat=" + lat
				+ ", longitude=" + longitude + ", from=" + from + ", to=" + to + ", fee_type=" + fee_type
				+ ", vaccine_fees=" + vaccine_fees + ", sessions=" + sessions + "]";
	}
	
	public List<Session> mapToSessions() {
		List<Session> expandedSessions = new ArrayList<>();
		for(Session session : this.sessions) {
			session.setCenter_id(this.center_id);
			session.setName(this.name);
			session.setAddress(this.address);
			session.setState_name(this.state_name);
			session.setDistrict_name(this.district_name);
			session.setBlock_name(this.block_name);
			session.setPincode(this.pincode);
			session.setLat(this.lat);
			session.setLong(this.longitude);
			session.setFrom(this.from);
			session.setTo(this.to);
			session.setFee_type(this.fee_type);
			expandedSessions.add(session);
		}
		return expandedSessions;
	}
}
