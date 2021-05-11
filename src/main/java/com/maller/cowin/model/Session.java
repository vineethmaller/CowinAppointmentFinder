package com.maller.cowin.model;

import java.util.List;

public class Session {
	
	private String session_id;
	
	private String center_id;
	private String name;
	private String address;
	private String state_name;
	private String district_name;
	private String block_name;
	private String pincode;
	
	private String from;
	private String to;
	
	private String lattitude;
	private String longitude;
	
	private String fee_type;
	private String date;
	
	private String available_capacity;
	private String fee;
	private String min_age_limit;
	private String vaccine;
	private List<String> slots;
	
	public Session() {
		
	}
	
	public Session(String session_id, String center_id, String name, String address, String state_name,
			String district_name, String block_name, String pincode, String from, String to, String lattitude, String longitude,
			String fee_type, String date, String available_capacity, String fee, String min_age_limit, String vaccine,
			List<String> slots) {
		this.session_id = session_id;
		this.center_id = center_id;
		this.name = name;
		this.address = address;
		this.state_name = state_name;
		this.district_name = district_name;
		this.block_name = block_name;
		this.pincode = pincode;
		this.from = from;
		this.to = to;
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.fee_type = fee_type;
		this.date = date;
		this.available_capacity = available_capacity;
		this.fee = fee;
		this.min_age_limit = min_age_limit;
		this.vaccine = vaccine;
		this.slots = slots;
	}



	public String getSession_id() {
		return session_id;
	}



	public void setSession_id(String session_id) {
		this.session_id = session_id;
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



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getState_name() {
		return state_name;
	}



	public void setState_name(String state_name) {
		this.state_name = state_name;
	}



	public String getDistrict_name() {
		return district_name;
	}



	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}



	public String getBlock_name() {
		return block_name;
	}



	public void setBlock_name(String block_name) {
		this.block_name = block_name;
	}



	public String getPincode() {
		return pincode;
	}



	public void setPincode(String pincode) {
		this.pincode = pincode;
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



	public String getLat() {
		return lattitude;
	}



	public void setLat(String lattitude) {
		this.lattitude = lattitude;
	}



	public String getLong() {
		return longitude;
	}



	public void setLong(String longitude) {
		this.longitude = longitude;
	}



	public String getFee_type() {
		return fee_type;
	}



	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getAvailable_capacity() {
		return available_capacity;
	}



	public void setAvailable_capacity(String available_capacity) {
		this.available_capacity = available_capacity;
	}



	public String getFee() {
		return fee;
	}



	public void setFee(String fee) {
		this.fee = fee;
	}



	public String getMin_age_limit() {
		return min_age_limit;
	}



	public void setMin_age_limit(String min_age_limit) {
		this.min_age_limit = min_age_limit;
	}



	public String getVaccine() {
		return vaccine;
	}



	public void setVaccine(String vaccine) {
		this.vaccine = vaccine;
	}



	public List<String> getSlots() {
		return slots;
	}



	public void setSlots(List<String> slots) {
		this.slots = slots;
	}



	@Override
	public String toString() {
		return "Session [session_id=" + session_id + ", center_id=" + center_id + ", name=" + name + ", address="
				+ address + ", state_name=" + state_name + ", district_name=" + district_name + ", block_name="
				+ block_name + ", pincode=" + pincode + ", from=" + from + ", to=" + to + ", lattitude=" + lattitude
				+ ", longitude=" + longitude + ", fee_type=" + fee_type + ", date=" + date + ", available_capacity="
				+ available_capacity + ", fee=" + fee + ", min_age_limit=" + min_age_limit + ", vaccine=" + vaccine
				+ ", slots=" + slots + "]";
	}
	
	public String getMinDetails() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nName: ").append(this.name);
		builder.append("\nAddress: ").append(this.address);
		builder.append("\nPIN Code: ").append(this.pincode);
		builder.append("\nAge Group: ").append(this.min_age_limit);
		builder.append("\nVaccine Name: ").append(this.vaccine);
		builder.append("\nAvailable Doses: ").append(this.available_capacity);
		builder.append("\nPIN Code: ").append(this.pincode);
		builder.append("\n");
		return builder.toString();
	}
}
