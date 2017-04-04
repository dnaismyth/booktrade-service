package com.flow.booktrade.repository.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.flow.booktrade.domain.RBook;

public class BookSpecifications implements Specification<RBook> {
	
	private BookSearchCriteria criteria;
	
	public BookSpecifications(BookSearchCriteria criteria){
		this.criteria = criteria;
	}
	
    @Override
    public Predicate toPredicate(Root<RBook> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    	
        if (criteria.getOperation().equalsIgnoreCase("Order")) {
        	if(root.get(criteria.getKey()).getJavaType() == String.class){
        		query.orderBy(builder.desc(root.get((String)criteria.getValue())));
        }
  
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        } 
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        } 
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                  root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        	
        }
        return null;
    }

}
