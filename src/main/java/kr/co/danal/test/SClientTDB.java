package kr.co.danal.test;

import java.util.Map;

import kr.co.danal.rnd.TeleditClient;
import kr.co.danal.rnd.tuna.util.GeneralStringUtil;

public class SClientTDB extends ConnectionProcess {

	private static TeleditClient teledit = new TeleditClient(System.getProperty("user.dir") + "\\config\\");

	public SClientTDB(Integer index, Object testCaseInfo) {
		super(index, testCaseInfo);
	}

	@Override
	public Map<String, String> getResponseMap(String response) throws Exception {
		return GeneralStringUtil.stringToMap(response, "=", "\n");
	}
	
	@Override
	protected String call(String url, Map<String, String> requestMap) throws Exception {
		return teledit.SClient(GeneralStringUtil.mapToString(requestMap, "=", ";"));
	}

	@Override
	protected String call(Map<String, String> requestMap) throws Exception {
		return call(null, requestMap);
	}

}
