package kr.co.danal.model;

import java.util.HashMap;
import java.util.Map;

import kr.co.danal.rnd.tuna.util.GeneralStringUtil;

public class Response extends HashMap<String, String> {

	private static final long serialVersionUID = -6225574484284592250L;
	
	public Response() {
		// do nothing...
	}

	public Response(String response, String nvSeparator, String groupSeparator) throws Exception {
		Map<String, String> responseMap = GeneralStringUtil.stringToMap(response, nvSeparator, groupSeparator);
		clear();
//		for (Entry<String, String> e : responseMap.entrySet()) {
//			if (e.getValue() != null) {
//				e.setValue(new String(e.getValue().getBytes("UTF-8"), "UTF-8"));
//			}
//		}
		putAll(responseMap);
	}
	
	public Response(Map<String, String> responseMap) throws Exception {
		clear();
//		for (Entry<String, String> e : responseMap.entrySet()) {
//			if (e.getValue() != null) {
//				e.setValue(new String(e.getValue().getBytes("UTF-8"), "UTF-8"));
//			}
//		}
		putAll(responseMap);
	}
}
