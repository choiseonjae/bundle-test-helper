package kr.co.bundletesthelper.element;

import java.util.Map;

import kr.co.bundletesthelper.exception.IllegalTestCaseFormatException;

public interface Element {
	String get(String value);
	String get(Map<String, Object> value);
	
	public static String noApplyStringType() {
		throw new IllegalTestCaseFormatException("String parameter 메소드는 지원하지 않습니다.");
	}
	
	public static String noApplyMapType() {
		throw new IllegalTestCaseFormatException("Map parameter 메소드는 지원하지 않습니다.");
	}
}
