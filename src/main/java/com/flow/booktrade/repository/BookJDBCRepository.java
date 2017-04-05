package com.flow.booktrade.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.flow.booktrade.dto.Book;
import com.flow.booktrade.dto.User;

@Repository
public class BookJDBCRepository extends BaseJDBCRepository {
	
    private final Logger log = LoggerFactory.getLogger(BookJDBCRepository.class);


	private static final String BASE_BOOK_FILTER_QUERY = "sql.books.filterBookSearchBaseQuery";
	private static final String BASE_BOOK_DISTANCE_FILTER_QUERY = "sql.books.filterBookSearchWithDistanceBaseQuery";
	
	private static final String QUERY_MAP_KEY = "query";
	private static final String DISTANCE_MAP_KEY = "distance";
	private static final String DISTANCE_VALUE_KEY = "distanceValue";
	private static final String CATEGORY_VALUE_KEY = "categories";
	
	public List<Book> filterBookSearch(Map<String, String> criteria, User currentUser){
		Map<String, Object> buildResult = buildFilterQuery(criteria);
		String query = (String)buildResult.get(QUERY_MAP_KEY);
		Boolean isDistanceQuery = (Boolean)buildResult.get(DISTANCE_MAP_KEY);
		if(isDistanceQuery){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("radius", (Integer)buildResult.get(DISTANCE_VALUE_KEY));
			params.put("longitude", currentUser.getLocation().getLongitude());
			params.put("latitude", currentUser.getLocation().getLatitude());
			return jdbcTemplate.query(query,  params, new BookRowMapper());
		} else {
			if(buildResult.containsKey(CATEGORY_VALUE_KEY)){
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("categories", buildResult.get(CATEGORY_VALUE_KEY));
				return jdbcTemplate.query(query, params, new BookRowMapper());
			} else {
				return jdbcTemplate.query(query, new BookRowMapper());
			}
		}
	}
	
	private Map<String, Object> buildFilterQuery(Map<String, String> criteria){
		String query = "";
		Map<String, Object> queryResult = new HashMap<String, Object>();
		boolean firstWhereClause = true;
		if(criteria.containsKey("author")){
			String authorParam = "'%" + criteria.get("author").toLowerCase() + "%'";
			query = query.concat("lower(b.author) LIKE " + authorParam);
			firstWhereClause = false;
		}
		
		if(criteria.containsKey("title")){
			String titleParam = "'%" + criteria.get("title").toLowerCase() + "%'";
			if(!firstWhereClause){
				query = query.concat(" OR ");
			} else {
				firstWhereClause = false;
			}
			query = query.concat("lower(b.title) LIKE "  + titleParam);
		}
		
		if(criteria.containsKey("category")){
			if(!firstWhereClause){
				query = query.concat(" AND ");
			} else {
				firstWhereClause = false;
			}
			String category = criteria.get("category");
			String [] categories = category.split(",");
			query = query.concat("b.category IN ( :categories) ");
			queryResult.put(CATEGORY_VALUE_KEY, Arrays.asList(categories));
		}
		
		if(criteria.containsKey("distance")){
			String distanceQuery = readQueryFromProperties(BASE_BOOK_DISTANCE_FILTER_QUERY);
			query = distanceQuery.concat(query + "AND distance < :radius ORDER BY distance ASC");
			queryResult.put(DISTANCE_MAP_KEY, true);
			queryResult.put(DISTANCE_VALUE_KEY, Integer.decode(criteria.get("distance")));
		} else {
			String baseQuery = readQueryFromProperties(BASE_BOOK_FILTER_QUERY);
			query = baseQuery.concat(query);
			queryResult.put(DISTANCE_MAP_KEY, false);
		}
		
		
		if(criteria.containsKey("sort")){
			String sortDirection = "DESC";
			if("price".equals(criteria.get("sort"))){
				sortDirection = "ASC";
			}
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
			   return b;
		   }
	}
}
