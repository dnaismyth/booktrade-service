package com.flow.booktrade.service.mapper;

import com.flow.booktrade.domain.RBook;
import com.flow.booktrade.dto.Book;

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
		}
		
		return rb;
	}
}
