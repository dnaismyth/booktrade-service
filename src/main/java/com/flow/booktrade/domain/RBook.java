package com.flow.booktrade.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flow.booktrade.dto.BookStatus;
import com.flow.booktrade.dto.Condition;

@Entity
@Table(name="book")
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
	
}
