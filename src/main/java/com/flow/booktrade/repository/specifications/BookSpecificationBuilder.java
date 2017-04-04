package com.flow.booktrade.repository.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import com.flow.booktrade.domain.RBook;

public class BookSpecificationBuilder {
	private final List<BookSearchCriteria> params;
	 
    public BookSpecificationBuilder() {
        params = new ArrayList<BookSearchCriteria>();
    }
 
    public BookSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new BookSearchCriteria(key, operation, value));
        return this;
    }
 
    public Specification<RBook> build() {
        if (params.size() == 0) {
            return null;
        }
 
        List<Specification<RBook>> specs = new ArrayList<Specification<RBook>>();
        for (BookSearchCriteria param : params) {
            specs.add(new BookSpecifications(param));
        }
 
        Specification<RBook> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}
