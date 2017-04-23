package com.flow.booktrade.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class UserConversationActivityJDBCRepository extends BaseJDBCRepository {

	private static final String GET_UNREAD_MESSAGE_COUNT = "sql.conversation.findUnreadMessageCount";
	
	public Map<Long, Integer> getUnreadMessageCount(Set<Long> conversationIds, Long userId) {
		if (conversationIds.size() > 0) {
			String query = readQueryFromProperties(GET_UNREAD_MESSAGE_COUNT);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("convoIds", conversationIds);
			params.put("userId", userId);
			return jdbcTemplate.query(query, params, new ResultSetExtractor<Map<Long, Integer>>() {
				@Override
				public Map<Long, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Map<Long, Integer> map = new HashMap<Long, Integer>();
					while (rs.next()) {
						map.put(rs.getLong("convo_id"), rs.getInt("unread_count"));
					}
					return map;
				}
			});
		} else {
			return new HashMap<Long, Integer>(); // empty result set
		}
	}
}