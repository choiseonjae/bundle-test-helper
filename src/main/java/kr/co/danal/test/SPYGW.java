	package kr.co.danal.test;

import java.util.Map;

import kr.co.danal.rnd.tuna.util.GeneralStringUtil;
import kr.co.danal.rnd.tuna.util.JsonUtil;
import kr.co.danal.util.HttpManager;

public class SPYGW extends ConnectionProcess {

	public SPYGW(Integer index, Object testCaseInfo) {
		super(index, testCaseInfo);
	}

	private static String URL = "http://192.168.210.76:8080/spay/";

	@Override
	public Map<String, String> getResponseMap(String response) throws Exception {
		Map<String, String> resMap = JsonUtil.toMap(response, String.class);
		return resMap;
	}
	
	@Override
	protected String call(String url, Map<String, String> requestMap) throws Exception {
		HttpManager http = new HttpManager(url);
		return http.post(GeneralStringUtil.mapToHTTPString(requestMap));
	}

	@Override
	protected String call(Map<String, String> requestMap) throws Exception {
		return call(URL, requestMap);
	}

}
