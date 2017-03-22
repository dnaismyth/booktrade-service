package com.flow.booktrade.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flow.booktrade.domain.RBook;

public interface BookRepository extends JpaRepository<RBook, Long> {

}
