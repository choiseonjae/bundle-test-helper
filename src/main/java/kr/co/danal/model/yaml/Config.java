package kr.co.danal.model.yaml;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Objects;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import kr.co.danal.model.yaml.exception.YamlException;
import kr.co.danal.util.AlertUtil;

public enum Config {

	TEST("config/test-config.yaml"), DATABASE("config/database-config.yaml"), SERVER("config/server-config.yaml");

	private YamlMap yamlMap;
	private File yamlFile;
	private String path;

	Config(String yamlName) {
		try {
			this.path = yamlName;
			yamlFile = new File(yamlName);
			yamlMap = getYamlMap();

		} catch (YAMLException e) { // 문법 오류면 해당 파일 열어서 수정 기회를 준다.
			AlertUtil.error(e);
			rewriteConfig();
		} catch (Exception e) { // 그 외 오류는 빈 config 파일을 만들어 준다.
			e.printStackTrace();
			yamlMap = new YamlMap();
		} finally {
			if (yamlMap == null) {
				AlertUtil.error("Config Load Fail !!", "Config 파일을 가져오지 못했습니다.\n 오류가 존재하는 파일 : " + yamlName);
				System.exit(0);
			}
			saveYaml(); // config 저장
		}
	}

	@SuppressWarnings("unchecked")
	private YamlMap getYamlMap() throws FileNotFoundException {
		Objects.requireNonNull(yamlFile, "Yaml File이 Null.");

		if (!yamlFile.exists()) {
			throw new NullPointerException(yamlFile.getName() + "이 존재하지 않습니다.");
		}

		InputStream fis = new FileInputStream(yamlFile);

		YamlMap yamlMap = new YamlMap(new Yaml().loadAs(fis, Map.class));

		return yamlMap;
	}

	private void rewriteConfig() {
		try {
			Desktop.getDesktop().edit(yamlFile);
			yamlMap = getYamlMap();
		} catch (Exception e) {
			AlertUtil.error(e);
			System.exit(0);
		}
	}

	public String getName() {
		return yamlFile.getName();
	}

	public String getString(String key) throws YamlException {
		return yamlMap.getString(key);
	}

	public Map<String, Object> getMap(String key) throws YamlException {
		return yamlMap.getMap(key);
	}

	public <T> T getOrDefault(String key, T defaultValue) {
		return yamlMap.getOrDefault(key, defaultValue);
	}

	public void updateYaml(String key, String value) {
		yamlMap.put(key, value);
		saveYaml();
	}
	
	public void updateYaml(Map<String, Object> map) {
		yamlMap.putAll(map);
		saveYaml();
	}

	public void saveYaml() {

		yamlFile.getParentFile().mkdirs();
		try (FileOutputStream fos = new FileOutputStream(yamlFile);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF8"));) {
			if (yamlMap.isEmpty()) {
				writer.write("");
			} else {
				writer.write(yamlMap.getYamlString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.error("config 파일 저장 실패", e.getMessage());
			System.exit(0);
		}
	}
	
	public String getPath() {
		return path;
	}
}
