package com.flow.booktrade.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.flow.booktrade.dto.Book;
import com.flow.booktrade.dto.BookCategory;
import com.flow.booktrade.dto.Condition;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.service.util.TimeUtil;

@Repository
public class BookJDBCRepository extends BaseJDBCRepository {
	
    private final Logger log = LoggerFactory.getLogger(BookJDBCRepository.class);


	private static final String BASE_BOOK_FILTER_QUERY = "sql.books.filterBookSearchBaseQuery";
	private static final String BASE_BOOK_DISTANCE_FILTER_QUERY = "sql.books.filterBookSearchDistanceBaseQuery";
	private static final String BASE_BOOK_DISTANCE_FILTER_QUERY_SELECT = "sql.books.filterBookSearchDistanceBaseQuerySelect";
	private static final String REMOVE_BOOK_REFERENCES = "sql.books.tearDownBookReferences";
	private static final String GET_BOOK_CATEGORIES_BY_BOOK_IDS = "sql.books.findBookCategoriesByBookId";

	private static final String QUERY_MAP_KEY = "query";
	private static final String DISTANCE_VALUE_KEY = "distanceValue";
	private static final String CATEGORY_VALUE_KEY = "categories";
	
	public boolean removeBookAndReferences(Long bookId){
		String query = readQueryFromProperties(REMOVE_BOOK_REFERENCES);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookId);
		int removed = jdbcTemplate.update(query, params);
		if(removed > 0){
			return true;
		}
		return false;
	}
	
	private List<Book> attachBookCategories(List<Book> books){
		LinkedHashMap<Long, Book> bookMap = createBookIdMap(books);
		String query = readQueryFromProperties(GET_BOOK_CATEGORIES_BY_BOOK_IDS);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookIds", bookMap.keySet());
		List<BookCategoryResult> result = jdbcTemplate.query(query,  params, new BookCategoryResultMapper());
		setBookCategories(result, bookMap);
		return new ArrayList<Book>(bookMap.values());
	}
	
	private LinkedHashMap<Long, Book> createBookIdMap(List<Book> books){
		LinkedHashMap<Long, Book> map = new LinkedHashMap<Long, Book>();
		for(Book b : books){
			map.put(b.getId(), b);
		}
		return map;
	}
	
	private void setBookCategories(List<BookCategoryResult> result, Map<Long, Book> bookMap){
		for(BookCategoryResult bcr : result){
			bookMap.get(bcr.getBookId()).getCategory().add(bcr.getCategory());
		}
	}
	
	public List<Book> filterBookSearch(Map<String, String> criteria, User currentUser){
		MapSqlParameterSource params = new MapSqlParameterSource();
		Map<String, Object> buildResult = buildFilterQuery(criteria);
		if(buildResult.containsKey(CATEGORY_VALUE_KEY)){
			params.addValue("categories", buildResult.get(CATEGORY_VALUE_KEY));
		}
		String query = (String)buildResult.get(QUERY_MAP_KEY);
		List<Book> filteredResult = jdbcTemplate.query(query, params, new BookRowMapper());
		return attachBookCategories(filteredResult);
	}
	
	public List<Book> filterBookSearchWithDistance(Map<String, String> criteria, User currentUser){
		MapSqlParameterSource params = new MapSqlParameterSource();
		Map<String, Object> buildResult = buildDistanceFilterQuery(criteria);
		if(buildResult.containsKey(CATEGORY_VALUE_KEY)){
			params.addValue("categories", buildResult.get(CATEGORY_VALUE_KEY));
		}
		
		params.addValue("radius", (Integer)buildResult.get(DISTANCE_VALUE_KEY));
		params.addValue("longitude", currentUser.getLocation().getLongitude());
		params.addValue("latitude", currentUser.getLocation().getLatitude());
		String query = (String)buildResult.get(QUERY_MAP_KEY);
		List<Book> filteredResult = jdbcTemplate.query(query,  params, new BookRowMapper());
		return attachBookCategories(filteredResult);
	}
	
	private Map<String, Object> buildDistanceFilterQuery(Map<String, String> criteria){
		String query = readQueryFromProperties(BASE_BOOK_DISTANCE_FILTER_QUERY);
		Map<String, Object> queryResult = new HashMap<String, Object>();
		if(criteria.containsKey("category")){
			String category = criteria.get("category");
			String [] categories = category.split(",");
			query = query.concat(" AND bc.category IN (:categories)) ").concat(readQueryFromProperties(BASE_BOOK_DISTANCE_FILTER_QUERY_SELECT));
			queryResult.put(CATEGORY_VALUE_KEY, Arrays.asList(categories));
		} else {
			query = query.concat(") ").concat(readQueryFromProperties(BASE_BOOK_DISTANCE_FILTER_QUERY_SELECT));
		}
		boolean firstWhereClause = true;
		if(criteria.containsKey("author") || criteria.containsKey("title")){
			String authorParam = "'%" + criteria.get("author").toLowerCase() + "%'";
			query = query.concat("AND (lower(b.author) LIKE " + authorParam);
			firstWhereClause = false;
		}
		
		if(criteria.containsKey("title")){
			String titleParam = "'%" + criteria.get("title").toLowerCase() + "%'";
			if(!firstWhereClause){
				query = query.concat(" OR ");
			} else {
				firstWhereClause = false;
			}
			query = query.concat("lower(b.title) LIKE "  + titleParam + ")");
		}
		
		query = query.concat(" AND distance < :radius ORDER BY distance ASC");
		queryResult.put(DISTANCE_VALUE_KEY, Integer.decode(criteria.get("distance")));
		queryResult.put(QUERY_MAP_KEY, query);
		return queryResult;
	}
	
	private Map<String, Object> buildFilterQuery(Map<String, String> criteria){
		String query = "";
		String groupBy = " GROUP BY bk.id, u.id";
		Map<String, Object> queryResult = new HashMap<String, Object>();
		boolean firstWhereClause = true;
		if(criteria.containsKey("author")){
			String authorParam = "'%" + criteria.get("author").toLowerCase() + "%'";
			query = query.concat("(lower(bk.author) LIKE " + authorParam);
			firstWhereClause = false;
		}
		
		if(criteria.containsKey("title")){
			String titleParam = "'%" + criteria.get("title").toLowerCase() + "%'";
			if(!firstWhereClause){
				query = query.concat(" OR ");
			} else {
				firstWhereClause = false;
			}
			query = query.concat("lower(bk.title) LIKE "  + titleParam + ")");
		}
		
		if(criteria.containsKey("category")){
			if(!firstWhereClause){
				query = query.concat(" AND ");
			} else {
				firstWhereClause = false;
			}
			String category = criteria.get("category");
			String [] categories = category.split(",");
			query = query.concat("bc.category IN (:categories) ");
			queryResult.put(CATEGORY_VALUE_KEY, Arrays.asList(categories));
		}
		
		String baseQuery = readQueryFromProperties(BASE_BOOK_FILTER_QUERY);
		query = baseQuery.concat(query);

		
		if(criteria.containsKey("sort")){
			String sortDirection = "DESC";
			if("price".equals(criteria.get("sort"))){
				sortDirection = "ASC";
			}
			query = query.concat(groupBy);
			query = query.concat(" ORDER BY ").concat(criteria.get("sort")).concat(" " + sortDirection);
		}
		queryResult.put(QUERY_MAP_KEY, query);
		return queryResult;
	}
	
	
	public class BookRowMapper implements RowMapper<Book> {
		   public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
			   //TODO: Set condition & category
			   Book b = new Book();
			   User owner = new User();
			   owner.setAvatar(rs.getString("avatar"));
			   owner.setName(rs.getString("name"));
			   owner.setId(rs.getLong("userId"));
			   b.setAuthor(rs.getString("author"));
			   b.setTitle(rs.getString("title"));
			   b.setId(rs.getLong("bookId"));
			   b.setBarcode(rs.getString("barcode"));
			   b.setThumbnailUrl(rs.getString("thumbnail_url"));
			   b.setImageUrl(rs.getString("image_url"));
			   b.setOwner(owner);
			   //String uploadedTime = TimeUtil.getZonedDateTimeDifferenceFormatString(TimeUtil.getCurrentTime(), rs.getDate("created_date"));
			   //b.setUploadedTime(uploadedTime);
//			   String category = rs.getString("category");
//			   if(category != null){
//				   BookCategory bc = BookCategory.valueOf(category);
//				   b.setCategory(bc);
//			   }
			   
			   String condition = rs.getString("condition");
			   if(condition != null){
				   Condition bookCondition = Condition.valueOf(condition);
				   b.setCondition(bookCondition);
			   }
			   return b;
		   }
	}
	
	public class BookCategoryResultMapper implements RowMapper<BookCategoryResult> {

		@Override
		public BookCategoryResult mapRow(ResultSet rs, int rowNum) throws SQLException {
			BookCategoryResult bcr = new BookCategoryResult();
			bcr.setBookId(rs.getLong("book_id"));
			BookCategory category = rs.getString("category") != null ? BookCategory.valueOf(rs.getString("category")) : null;
			bcr.setCategory(category);
			return bcr;
		}
		
	}
	
	public static class BookCategoryResult{
		private Long bookId;
		private BookCategory category;
		
		public BookCategoryResult(){}
		
		public Long getBookId(){
			return bookId;
		}
		
		public BookCategory getCategory(){
			return category;
		}
		
		public void setCategory(BookCategory category){
			this.category = category;
		}
		
		public void setBookId(Long bookId){
			this.bookId = bookId;
		}
	}
}
