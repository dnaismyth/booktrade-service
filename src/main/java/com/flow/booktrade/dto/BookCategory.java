package com.flow.booktrade.dto;

public enum BookCategory {
	TEXTBOOK("textbook"),
	CHILDREN("children"),
	FREE("free");
	
	private String type;
	
	BookCategory(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
}
