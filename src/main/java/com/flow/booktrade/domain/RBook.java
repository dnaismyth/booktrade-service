package com.flow.booktrade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flow.booktrade.dto.BookCategory;
import com.flow.booktrade.dto.BookStatus;
import com.flow.booktrade.dto.Condition;
import com.flow.booktrade.dto.DataSource;

@Entity
@Table(name="book", indexes={
		@Index(name="book_title_idx", columnList="title"),
		@Index(name="book_author_idx", columnList="author"),
		@Index(name="book_owner_idx", columnList="owner_id"),
		@Index(name="book_price_idx", columnList="price"),
		@Index(name="book_created_date_idx", columnList="created_date")
})
public class RBook extends AbstractAuditingEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7219680185199196352L;

	@Id
    @GeneratedValue
	private Long id;
	
	@Column(name="author")
	private String author;
	
	@Column(name="title")
	private String title;
	
	@Column(name="barcode")
	private String barcode;
	
    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private BookStatus status;
    
    @Column(name="description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name="condition")
    private Condition condition;
    
    @Column(name="price")
    private BigDecimal price;
    
    /**
     * Source where book data is coming from, eg: GoodReads
     */
    @Enumerated(EnumType.STRING)
    @Column(name="data_source")
    private DataSource dataSource;
    
    @ElementCollection(targetClass = BookCategory.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="book_category", joinColumns = {@JoinColumn(name="book_id")})
    @Column(name="category")
    private List<BookCategory> category = new ArrayList<BookCategory>();
	
	@ManyToOne
	private RUser owner;
	
	@Column(name="thumbnail_url")
	private String thumbnailUrl;
	
	@Column(name="image_url")
	private String imageUrl;
	
	public RBook(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public RUser getOwner(){
		return owner;
	}
	
	public void setOwner(RUser owner){
		this.owner = owner;
	}
	
	public String getBarcode(){
		return barcode;
	}
	
	public void setBarcode(String barcode){
		this.barcode = barcode;
	}
	
	public BookStatus getStatus(){
		return status;
	}
	
	public void setStatus(BookStatus status){
		this.status = status;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public Condition getCondition(){
		return condition;
	}
	
	public void setCondition(Condition condition){
		this.condition = condition;
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
	
}
