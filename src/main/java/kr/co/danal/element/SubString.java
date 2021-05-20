package kr.co.danal.element;

import java.util.Map;

public class SubString implements Element {

	private static final String START_KEY = "beginIndex";
	private static final String END_KEY = "endIndex";
	
	@Override
	public String get(String encText) {
		return Element.noApplyStringType();
	}

	@Override
	public String get(Map<String, Object> subStringInfo) {
		String text = subStringInfo.get("text").toString();
		if(subStringInfo.containsKey(START_KEY) && !subStringInfo.containsKey(END_KEY)) {
			int beginIndex = Integer.parseInt(subStringInfo.get(START_KEY).toString());
			return text.substring(beginIndex);
		}
		
		if(!subStringInfo.containsKey(START_KEY) && subStringInfo.containsKey(END_KEY)) {
			int endIndex = Integer.parseInt(subStringInfo.get(END_KEY).toString());
			return text.substring(0, endIndex);
		}
			
		int beginIndex = Integer.parseInt(subStringInfo.get(START_KEY).toString());
		int endIndex = Integer.parseInt(subStringInfo.get(END_KEY).toString());
		return text.substring(beginIndex, endIndex);
	}
}
