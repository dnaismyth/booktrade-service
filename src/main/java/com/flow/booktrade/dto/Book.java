package com.flow.booktrade.dto;

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
	private BookCategory category;
	
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
	
	public BookCategory getCategory(){
		return category;
	}
	
	public void setCategory(BookCategory category){
		this.category = category;
	}
	
}
