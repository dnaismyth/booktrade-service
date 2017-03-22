package com.flow.booktrade.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.flow.booktrade.domain.RBook;
import com.flow.booktrade.domain.RUser;
import com.flow.booktrade.exception.ResourceNotFoundException;
import com.flow.booktrade.repository.BookRepository;
import com.flow.booktrade.repository.UserRepository;

public class BaseService {
	
	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private UserRepository userRepo;

	public RBook loadBook(Long id) throws ResourceNotFoundException{
		RBook rb = bookRepo.findOne(id);
		if(rb == null){
			throw new ResourceNotFoundException("Cannot find book with id: " + id);
		}
		return rb;
	}
	
	public RUser loadUserEntity(Long userId) throws ResourceNotFoundException{
		RUser ru = userRepo.getOne(userId);
		if(ru == null){
			throw new ResourceNotFoundException("Cannot find user with id: " + userId);
		}
		return ru;
	}
}
