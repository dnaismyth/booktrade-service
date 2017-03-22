package com.flow.booktrade.domain;

import javax.persistence.Embeddable;

@Embeddable
public class RLocation {
	
	private String city;
	private String country;
	
	public RLocation(){}
	
	public RLocation(String city, String country){
		this.city = city;
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
