package kr.co.bundletesthelper.util;

import java.util.Map;
import java.util.Map.Entry;


public class MapToYamlStr {

	public static String getStringValueMap(Map<String, String> map) {
		StringBuilder yamlStrBuilder = new StringBuilder();
		for(Entry<String, String> e : map.entrySet()) {
			yamlStrBuilder.append(e.getKey() + ": " + e.getValue());
			yamlStrBuilder.append("\n");
		}
		return yamlStrBuilder.toString();
	}

	public static String get(Map<String, Object> map) {
		return null;
	}

}
