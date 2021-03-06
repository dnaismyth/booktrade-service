package com.flow.booktrade.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A book object that is availble for trade/sell
 * @author Dayna
 *
 */
public class Book {

	private Long id;
	private String title;
	private String author;
	private String barcode;
	private String description;
	private Condition condition;
	private User owner;
	private BookStatus status;
	private String thumbnailUrl;	// thumbnail url
	private String imageUrl;	// larger image url
	private DataSource dataSource; 
	private List<BookCategory> category = new ArrayList<BookCategory>();
	//@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal price;
	private String uploadedTime;
	
	public Book(){}
	
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public User getOwner(){
		return owner;
	}
	
	public void setOwner(User owner){
		this.owner = owner;
	}
	
	public BookStatus getStatus(){
		return status;
	}
	
	public void setStatus(BookStatus status){
		this.status = status;
	}
	
	public String getThumbnailUrl(){
		return thumbnailUrl;
	}
	
	public void setThumbnailUrl(String thumbnailUrl){
		this.thumbnailUrl = thumbnailUrl;
	}
	
	public String getImageUrl(){
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}
	
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public List<BookCategory> getCategory(){
		return category;
	}
	
	public void setCategory(List<BookCategory> category){
		this.category = category;
	}
	
	public BigDecimal getPrice(){
		return price;
	}
	
	public void setPrice(BigDecimal price){
		this.price = price;
	}
	
	public String getUploadedTime(){
		return uploadedTime;
	}
	
	public void setUploadedTime(String uploadedTime){
		this.uploadedTime = uploadedTime;
	}
	
}
