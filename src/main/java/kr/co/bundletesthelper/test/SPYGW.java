	package kr.co.bundletesthelper.test;

import java.util.Map;

import kr.co.bundletesthelper.rnd.tuna.util.GeneralStringUtil;
import kr.co.bundletesthelper.rnd.tuna.util.JsonUtil;
import kr.co.bundletesthelper.util.HttpManager;

public class SPYGW extends ConnectionProcess {

	public SPYGW(Integer index, Object testCaseInfo) {
		super(index, testCaseInfo);
	}

	private static String URL = "";

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
