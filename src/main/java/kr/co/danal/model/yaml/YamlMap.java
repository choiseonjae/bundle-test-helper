package kr.co.danal.model.yaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kr.co.danal.model.yaml.exception.YamlException;

public class YamlMap {

	private Map<String, Object> yamlMap = new HashMap<>();
	private final String SEPERATOR = ".";

	public YamlMap() {
	}

	public YamlMap(Map<String, Object> yamlMap) {
		this.yamlMap = yamlMap;
	}

	@Override
	public String toString() {
		return yamlMap.toString();
	}

	public boolean isEmpty() {
		return (yamlMap == null || yamlMap.isEmpty());
	}

	public boolean containsKey(String key) {
		try {
			get(key, new Object());
			return true;
		} catch (Throwable e) {
			return false;
		}
	}
	
	public void putAll(Map<String, Object> map) {
		yamlMap.putAll(map);
	}

	/**
	 * YamlMap에 key 값이 존재하지 않을 경우 defaultValue 반환
	 * 
	 * @param yaml에서 찾고자 하는 값의 Key
	 * @param 값이     Null일 경우 반환 할 값
	 * @return
	 * @throws YamlException
	 */
	@SuppressWarnings("unchecked")
	public <T> T getOrDefault(String key, T defaultValue) {
		try {
			Object result = get(key, defaultValue);
			return result == null ? defaultValue : (T) result;
		} catch (YamlException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public int getInt(String key) throws YamlException {
		try {
			return get(key, 1);
		} catch (YamlException e) {
			throw e;
		}
	}

	public String getString(String key) throws YamlException {
		try {
			return get(key, "");
		} catch (YamlException e) {
			throw e;
		}
	}

	public boolean getBoolean(String key) throws YamlException {
		try {
			return get(key, true);
		} catch (YamlException e) {
			throw e;
		}
	}

	public double getDouble(String key) throws YamlException {
		try {
			return get(key, new Double(0));
		} catch (YamlException e) {
			throw e;
		}
	}

	public Map<String, Object> getMap(String key) throws YamlException {
		try {
			return get(key, new HashMap<String, Object>());
		} catch (YamlException e) {
			throw e;
		}

	}

	public List<String> getStringList(String key) throws YamlException {
		try {
			return get(key, new ArrayList<String>());
		} catch (YamlException e) {
			throw e;
		}
	}

	public List<Object> getObjectList(String key) throws YamlException {
		try {
			return get(key, new ArrayList<Object>());
		} catch (YamlException e) {
			throw e;
		}
	}

	public List<Boolean> getBooleanList(String key) throws YamlException {
		try {
			return get(key, new ArrayList<Boolean>());
		} catch (YamlException e) {
			throw e;
		}
	}

	public List<Double> getDoubleList(String key) throws YamlException {
		try {
			return get(key, new ArrayList<Double>());
		} catch (YamlException e) {
			throw e;
		}
	}

	/**
	 * TODO reasonKey 적어야함. YamlMap에서 key로 찾은 뒤 인자로 넘어온 clazz의 Type으로 반환한다.
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 * @throws YamlException
	 * @throws ReturnTypeUnknownException
	 */
	@SuppressWarnings("unchecked")
	private <T> T get(String key, T result) throws YamlException {

		if (key == null || key.isEmpty()) {
			throw new YamlException(YamlException.EMPTY_KEY, "key(" + key + ") is empty.");
		}

		String[] keys = key.split("\\" + SEPERATOR);
		Map<String, Object> subYamlMap = yamlMap;

		for (int i = 0; i < keys.length; i++) {

			String subKey = keys[i];
			if (i == keys.length - 1) {
				
				if (subYamlMap == null) {
					return null;
				}
				
				Object resultObj = subYamlMap.get(subKey);

				if (resultObj == null) {
					return null;
				}

				if (!result.getClass().isInstance(resultObj)) { // type
					String errMsg = "result type of " + key + " is " + resultObj.getClass().getSimpleName()
							+ ". it's not " + result.getClass().getSimpleName();
					throw new YamlException(YamlException.CASTING_FAIL, errMsg);
				}

				return (T) resultObj;
			}

			if (Map.class.isInstance(subYamlMap.get(keys[i]))) { // type 체크
				subYamlMap = Map.class.cast(subYamlMap.get(keys[i]));
			} else {
				String errMsg = "search fail by key. key is wrong.";
				throw new YamlException(YamlException.INVALID_KEY + "_" + key, errMsg);
			}
		}

		return null;
	}

	public int size() {
		return yamlMap.size();
	}

	public void clear() {
		yamlMap.clear();
	}

	public Set<String> keySet() {
		return yamlMap.keySet();
	}

	public Collection<Object> values() {
		return yamlMap.values();
	}

	public Set<Entry<String, Object>> entrySet() {
		return yamlMap.entrySet();
	}

	public String getYamlString() {
		System.out.println(yamlMap);
		return getYamlString("", yamlMap);
	}
	
	public void put(String key, String value) {
//		if(yamlMap == null) {
//			yamlMap = new HashMap<>();
//		}
		yamlMap.put(key, value);
	}

	private String getYamlString(String space, Object obj) {
		StringBuilder yamlStrBuilder = new StringBuilder();
		if (obj instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> childMap = (Map<String, Object>) obj;
			for (Entry<String, Object> childE : childMap.entrySet()) {
				yamlStrBuilder.append("\n" + space + childE.getKey() + " : " + getYamlString(space + " ", childE.getValue()));
			}
		} else if (obj instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> childList = (List<Object>) obj;
			for (Object child : childList) {
				yamlStrBuilder.append("\n" + space + "- " + getYamlString(space + " ", child));
			}
		} else {
			return obj.toString();
		}
		return yamlStrBuilder.toString();
	}
}
