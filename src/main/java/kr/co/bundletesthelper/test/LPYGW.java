package kr.co.bundletesthelper.test;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import kr.co.bundletesthelper.rnd.tuna.util.GeneralStringUtil;
import kr.co.bundletesthelper.util.HttpManager;

public class LPYGW extends ConnectionProcess {

	public LPYGW(Integer index, Object testCaseInfo) {
		super(index, testCaseInfo);
	}

	private final static String URL = "";

	@Override
	public Map<String, String> getResponseMap(String response) throws Exception {
		Map<String, String> resMap = GeneralStringUtil.stringToMap(response, "=", "&");
		// url decoding
		for (Entry<String, String> resEntry : resMap.entrySet()) {
			if(resEntry.getValue() == null) {
				continue;
			}
			
			resMap.put(resEntry.getKey(), URLDecoder.decode(resEntry.getValue(), "UTF-8"));
		}
		return resMap;
	}

	@Override
	protected String call(Map<String, String> requestMap) throws Exception {
		return call(URL, requestMap);
	}

	@Override
	protected String call(String url, Map<String, String> requestMap) throws Exception {
		for(Entry<String, String> request : requestMap.entrySet()) {
			request.setValue(URLEncoder.encode(request.getValue(), "UTF-8"));
		}
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		
		HttpManager http = new HttpManager(url, headers);
		return http.post(GeneralStringUtil.mapToHTTPString(requestMap));
	}

}
