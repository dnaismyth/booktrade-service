package com.flow.booktrade.repository.specifications;

/**
 * Criteria to filter a book search
 * @author Dayna
 *
 */
public class BookSearchCriteria {
	private String key;	// column 
    private String operation; // operation, eg: less than, equal to
    private Object value;	// value to filter by, eg: where firstName = "Joe"
    
    public BookSearchCriteria(String key, String operation, Object value){
    	this.key = key;
    	this.operation = operation;
    	this.value = value;
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
