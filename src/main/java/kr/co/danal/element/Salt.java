package kr.co.danal.element;

import java.util.Map;

public class Salt implements Element {

	@Override
	public String get(String value) {
		return convertValueWithSalt(value);
	}

	@Override
	public String get(Map<String, Object> value) {
		return Element.noApplyMapType();
	}

	private String convertValueWithSalt(String value) {
		StringBuilder valueWithSalt = new StringBuilder();
		for (char valueOfChar : value.toCharArray()) {
			valueWithSalt.append(createRamdomSalt(15));
			valueWithSalt.append(valueOfChar);
		}
		return valueWithSalt.toString();
	}
	
	private String createRamdomSalt(int cnt) {
		StringBuilder valueWithSalt = new StringBuilder();
		for (int index = 0; index < cnt; index++) {
			valueWithSalt.append((int) (Math.random() * 10));
		}
		return valueWithSalt.toString();
	}

}
