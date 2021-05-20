package kr.co.danal.element;

import java.util.Map;

import kr.co.danal.rnd.tuna.util.JsonUtil;

public class GetFromJsonString implements Element{

	@Override
	public String get(String value) {
		return Element.noApplyStringType();
	}

	@Override
	public String get(Map<String, Object> jsonInfo) {
		String json = jsonInfo.get("json").toString();
		String key = jsonInfo.get("key").toString();
		Map<String, String> jsonMap = JsonUtil.toMap(json, String.class);
		return jsonMap.get(key);
	}

}
