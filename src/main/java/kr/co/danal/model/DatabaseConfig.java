package kr.co.danal.model;

import java.util.Map;

import kr.co.danal.exception.ElementExecuteException;
import kr.co.danal.model.yaml.Config;
import kr.co.danal.model.yaml.exception.YamlException;

public class DatabaseConfig {

	private String driver;
	private String id;
	private String pwd;
	private String url;
	private String database;
	private String table;
	private String configId;

	public DatabaseConfig(String databaseId) {
		try {

			configId = databaseId;
			Map<String, Object> config = Config.DATABASE.getMap(configId);
			
			if (config == null) {
				throw new ElementExecuteException(String.format("%s가 존재하지 않거나 읽어올 수 없습니다.", Config.DATABASE.getPath()));
			}

// TODO 나중에 불필요하다고 생각되면 지워도 된다.
//			if (!config.containsKey(configId)) {
//				String message = String.format("%s에  해당 Database Id(%s)의 정보가 기술되어 있지 않음. config : %s",	YamlConfig.DATABASE_CONFIG.getPath(), configId, config.toString());
//				throw new ElementExecuteException(message);
//			}

			init(config);
		} catch (YamlException e) {
			String errorMsg = String.format("%s의 정보를 읽어오다가 실패했습니다.", Config.DATABASE.getPath());
			throw new ElementExecuteException(errorMsg);
		}

	}

	private void init(Map<String, Object> config) {
		driver = config.getOrDefault("driver", "com.mysql.jdbc.Driver").toString();
		id = config.get("id").toString();
		pwd = config.get("pwd").toString();
		url = config.get("url").toString();
		database = config.get("database").toString();
		table = config.get("table").toString();

		// default set
		driver = driver == null ? "com.mysql.jdbc.Driver" : driver;
	}

	public String getDriver() {
		return driver;
	}

	public String getId() {
		return id;
	}

	public String getPwd() {
		return pwd;
	}

	public String getUrl() {
		return url;
	}

	public String getDatabase() {
		return database;
	}

	public String getTable() {
		return table;
	}

	public String getConfigId() {
		return configId;
	}

}
