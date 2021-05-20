package kr.co.danal.test;

import java.util.Map;

import kr.co.danal.rnd.tuna.util.GeneralStringUtil;
import kr.co.danal.util.HttpManager;

public class TDB extends ConnectionProcess {

	public TDB(Integer index, Object testCaseInfo) {
		super(index, testCaseInfo);
	}

	private static final String URL = "http://192.168.210.76:8080/test_servicedev/";

	@Override
	public Map<String, String> getResponseMap(String response) throws Exception {
		Map<String, String> responseMap = GeneralStringUtil.stringToMap(response, "=", "&");
		return responseMap;
	}
	
	@Override
	protected String call(String url, Map<String, String> requestMap) throws Exception {
		HttpManager http = new HttpManager(url);
		requestMap.put("CLIP", "127.0.0.1");
		return http.post(GeneralStringUtil.mapToString(requestMap, "=", "&"));
	}

	@Override
	protected String call(Map<String, String> requestMap) throws Exception {
		return call(URL, requestMap);
	}

}
