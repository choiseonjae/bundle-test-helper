package kr.co.danal.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import kr.co.danal.model.DatabaseConfig;
import kr.co.danal.rnd.tuna.util.GeneralStringUtil;

/**
 * 사용 예시
 * Database: 
 *  Id: LgLogProtocol
 *  Request: 
 *   TID : 202020202020
 *  Response:
 *   MEMBERID : LG2020202020
 * @author homesking
 *
 */
public class Database extends ConnectionProcess {

	public Database(Integer index, Object testCaseInfo) {
		super(index, testCaseInfo);
	}

	private DatabaseConfig databaseConfig;
	private Map<String, String> selectColumn;
	

	@Override
	protected String call(String url, Map<String, String> requestMap) throws Exception {
		// DB 연결
		try (Statement statement = connectingDatabase(databaseConfig);) {

			String table = databaseConfig.getTable();
			String where = getWhere(requestMap);
			String query = createQuery(table, where);

			ResultSet resultSet = statement.executeQuery(query);

			Function<ResultSet, Integer> getRowCnt = result -> {
				try {
					result.last();
					int rowCount = result.getRow();
					result.beforeFirst();
					return rowCount;
				} catch (SQLException e) {
					return -1;
				}
			};
			
			int rowCount = getRowCnt.apply(resultSet);
			String errMsg = rowCount == -1 ? "SQL 오류" : String.valueOf(rowCount);
			assertTrue("Database 검색 row가 " + errMsg + "임.", rowCount == 1);
			
			Map<String, String> resMap = new HashMap<>();
			resultSet.next();
			for (String column : selectColumn.keySet()) {
				if (resultSet.getObject(column) != null) {
					resMap.put(column, resultSet.getObject(column).toString());
				}
			}
			
			return GeneralStringUtil.mapToString(resMap);
		} catch (ClassNotFoundException | FileNotFoundException | SQLException e) {
			throw new Exception(e.getMessage() + "\n[" + e.getClass().getSimpleName() + "] " + "Database 요청 실패.");
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	protected String call(Map<String, String> requestMap) throws Exception {
		return call(null, requestMap);
	}
	
	@Override
	protected Map<String, String> getResponseMap(String response) throws Exception {
		return GeneralStringUtil.stringToMap(response);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void beforeRequest(Map<String, Object> testcaseMap) throws Exception {
		final String ID = "Id";
		databaseConfig = new DatabaseConfig(testcaseMap.get(ID).toString());
		selectColumn = (Map<String, String>) testcaseMap.get("Response");
		assertNotNull("비교할 select(Response)가 없다. Response를 확인해라.", selectColumn);
	}

	private String getWhere(Map<String, String> whereMap) throws Exception {
		StringBuilder where = new StringBuilder();
		for (Entry<String, String> param : whereMap.entrySet()) {
			where.append(param.getKey());
			where.append("=");
			where.append("'" + param.getValue() + "'");
			where.append(" AND ");
		}
		return where.toString().substring(0, where.lastIndexOf(" AND "));
	}

	private String createQuery(String tableName, String params) {
		String query = "SELECT * FROM " + tableName;
		query += " WHERE " + params;
		return query += ";";
	}

	private Statement connectingDatabase(DatabaseConfig config)
			throws SQLException, ClassNotFoundException, FileNotFoundException {
		Class.forName(config.getDriver());
		return DriverManager.getConnection(config.getUrl() + config.getDatabase(), config.getId(), config.getPwd())
				.createStatement();
	}

}
