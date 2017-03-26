package com.flow.booktrade.web.rest.vm;

import java.io.Serializable;

public class SimpleRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String value;
	
	public SimpleRequest(){}
	
	public String getValue(){
		return value;
	}
	
	public void setValue(String value){
		this.value = value;
	}
}
