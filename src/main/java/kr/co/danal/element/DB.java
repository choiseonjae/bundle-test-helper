package kr.co.danal.element;

import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;

import kr.co.danal.exception.ElementExecuteException;
import kr.co.danal.model.DatabaseConfig;

public class DB implements Element {

	private String getRow(Map<String, Object> dbInfoMap) {
		
		DBInformation dbInfo = new DBInformation(dbInfoMap);

		DatabaseConfig config = loadDBConf(dbInfo.getConnection());

		String query = createQuery(config.getTable(), dbInfo.getWhere());
		System.out.println("[DB  QUERY] " + query);
		
		// DB 연결
		try (Statement statement = connectingDataBase(config);
			ResultSet resultSet = statement.executeQuery(query);) {

			if (resultSet.next()) {
				String result = resultSet.getObject(dbInfo.getSelect()).toString();
				System.out.println("[SELECT] " + result);
				return result;
			}
			
			throw new ElementExecuteException("조회 결과 없음. Query : " + query, DB.class);
		} catch (ClassNotFoundException | FileNotFoundException | SQLException e) {
			throw new ElementExecuteException(e, DB.class);
		}
	}

	private String createQuery(String tableName, String where) {
		String query = "SELECT * FROM " + tableName;
		query += " WHERE " + where;
		return query += ";";
	}

	private DatabaseConfig loadDBConf(String connection) {
		return new DatabaseConfig(connection);
	}

	private Statement connectingDataBase(DatabaseConfig config)
			throws SQLException, ClassNotFoundException, FileNotFoundException {
		Class.forName(config.getDriver());
		return DriverManager.getConnection(config.getUrl() + config.getDatabase(), config.getId(), config.getPwd())
				.createStatement();
	}

	@Override
	public String get(String value) {
		return Element.noApplyStringType();
	}

	@Override
	public String get(Map<String, Object> dbInfo) {
		return getRow(dbInfo);
	}
	
	private class DBInformation{
		private String select;
		private String where;
		private String connection;
		
		private DBInformation(Map<String, Object> dbInfo) {
			try {
				this.select = dbInfo.get("select").toString();
				this.where = dbInfo.get("where").toString();
				this.connection = dbInfo.get("connection").toString();
				
				Objects.requireNonNull(select);
				Objects.requireNonNull(where);
				Objects.requireNonNull(connection);
			} catch (NullPointerException e) {
				throw new ElementExecuteException(String.format("DB element의 필수값이 존재하지 않습니다."), e, DB.class);
			}
		}
		
		public String getSelect() {
			return select;
		}
		
		public String getWhere() {
			return where;
		}
		
		public String getConnection() {
			return connection;
		}
	}

}
