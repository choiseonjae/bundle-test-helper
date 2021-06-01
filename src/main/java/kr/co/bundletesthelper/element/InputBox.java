package kr.co.bundletesthelper.element;

import java.util.Map;

import kr.co.bundletesthelper.exception.ElementExecuteException;
import kr.co.bundletesthelper.util.AlertUtil;

public class InputBox implements Element {

	@Override
	public String get(String value) {
		return AlertUtil.input(value);
	}

	@Override
	public String get(Map<String, Object> value) {
		
		try {
			String message = value.get("message").toString();
			String defaultValue = value.get("defaultValue").toString();
		
			return AlertUtil.input(defaultValue, message);
		} catch (NullPointerException e) {
			throw new ElementExecuteException(e, InputBox.class);
		}
	}

}
