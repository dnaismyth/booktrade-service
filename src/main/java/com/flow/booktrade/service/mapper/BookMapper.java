package com.flow.booktrade.service.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.flow.booktrade.domain.RBook;
import com.flow.booktrade.dto.Book;
import com.flow.booktrade.dto.Notification;

public class BookMapper {
	
	private UserMapper userMapper = new UserMapper();
	
	/**
	 * To Book DTO
	 * @param rb
	 * @return
	 */
	public Book toBook(RBook rb){
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
			b.setThumbnailUrl(rb.getThumbnailUrl());
			b.setImageUrl(rb.getImageUrl());
			b.setDataSource(rb.getDataSource());
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
		}
		
		return rb;
	}
	
	/**
	 * To Book Page
	 * @param rb
	 * @param pageable
	 * @return
	 */
	public Page<Book> toBookPage(Page<RBook> rb, Pageable pageable){
		List<Book> books = new ArrayList<Book>();
		Iterator<RBook> iter = rb.iterator();
		while(iter.hasNext()){
			books.add(toBook(iter.next()));
		}
		
		return new PageImpl<Book>(books, pageable, books.size());
	}
}
