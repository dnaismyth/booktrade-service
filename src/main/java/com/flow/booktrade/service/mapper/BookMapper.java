package com.flow.booktrade.service.mapper;

import java.math.BigDecimal;

import org.hibernate.Hibernate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;

import com.flow.booktrade.domain.RBook;
import com.flow.booktrade.dto.Book;
import com.flow.booktrade.dto.DataSource;
import com.flow.booktrade.service.util.ImageUtil;
import com.flow.booktrade.service.util.S3Utils;
import com.flow.booktrade.service.util.TimeUtil;

public class BookMapper {
	
	private UserMapper userMapper = new UserMapper();
	
	/**
	 * To Book DTO
	 * @param rb
	 * @return
	 */
	public Book toBook(RBook rb, boolean showCategories){
		Book b = null;
		if(rb != null){
			b = new Book();
			b.setAuthor(rb.getAuthor());
			b.setBarcode(rb.getBarcode());
			b.setCondition(rb.getCondition());
			b.setDescription(rb.getDescription());
			b.setId(rb.getId());
			b.setTitle(rb.getTitle());
			b.setOwner(userMapper.toUser(rb.getOwner()));
			b.setStatus(rb.getStatus());
			if(rb.getDataSource() != null && rb.getDataSource().equals(DataSource.BOOK_TRADER)){
				b.setThumbnailUrl(S3Utils.generateUrl(rb.getThumbnailUrl()));
				b.setImageUrl(S3Utils.generateUrl(rb.getImageUrl()));
			} else {
				b.setThumbnailUrl(rb.getThumbnailUrl());
				b.setImageUrl(rb.getImageUrl());
			}
			
			b.setDataSource(rb.getDataSource());
			if(rb.getPrice() != null){
				BigDecimal scaledResult = rb.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
				b.setPrice(scaledResult);
			}
			if(showCategories){
				Hibernate.initialize(rb.getCategory());
				b.setCategory(rb.getCategory());
			}
			String uploadedTime = TimeUtil.getZonedDateTimeDifferenceFormatString(TimeUtil.getCurrentTime(), rb.getCreatedDate());
			b.setUploadedTime(uploadedTime);
		}

		return b;
	}
	
	/**
	 * To RBook entity
	 * @param b
	 * @return
	 */
	public RBook toRBook(Book b){
		RBook rb = null;
		if(b != null){
			rb = new RBook();
			rb.setAuthor(b.getAuthor());
			rb.setBarcode(b.getBarcode());
			rb.setCondition(b.getCondition());
			rb.setDescription(b.getDescription());
			rb.setId(b.getId());
			rb.setOwner(userMapper.toRUser(b.getOwner()));
			rb.setTitle(b.getTitle());
			rb.setStatus(b.getStatus());
			rb.setThumbnailUrl(b.getThumbnailUrl());
			rb.setImageUrl(b.getImageUrl());
			rb.setDataSource(b.getDataSource());
			rb.setPrice(b.getPrice());
			rb.setCategory(b.getCategory());
		}
		
		return rb;
	}
	
	/**
	 * To book page
	 * @param rb
	 * @return
	 */
	public Page<Book> toBookPage(Page<RBook> rb, boolean showCategories){
		Page<Book> bookPage = rb.map(new Converter<RBook, Book>(){
			@Override
			public Book convert(RBook rb) {
				return toBook(rb, showCategories);
			}
			
		});
		return bookPage;
	}
	
}
