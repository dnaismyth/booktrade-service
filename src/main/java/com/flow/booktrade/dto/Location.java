package com.flow.booktrade.dto;

/**
 * Location object to represent User's location.
 * @author Dayna
 *
 */
public class Location {
	
	private String country;
	private String city;
	
	public Location(){}
	
	public Location(String country, String city){
		this.city = city;
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
}
