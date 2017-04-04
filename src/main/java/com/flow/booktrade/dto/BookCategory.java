package com.flow.booktrade.dto;

public enum BookCategory {
	TEXTBOOK("textbook"),
	FREE("free");
	
	private String type;
	
	BookCategory(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
}
