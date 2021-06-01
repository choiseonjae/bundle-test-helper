package kr.co.bundletesthelper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import kr.co.bundletesthelper.exception.IllegalTestCaseFormatException;
import kr.co.bundletesthelper.model.Response;
import kr.co.bundletesthelper.model.TestCase;
import kr.co.bundletesthelper.model.TestResult;
import kr.co.bundletesthelper.model.yaml.Config;
import kr.co.bundletesthelper.model.yaml.exception.YamlException;
import kr.co.bundletesthelper.util.ElementLauncher;
import kr.co.bundletesthelper.util.Factory;

public abstract class ConnectionProcess extends TestCase {

	private Response expectedResponse = new Response();
	private Response actualResponse = new Response();

	protected String oneLineLog = "";
	
	protected String url = "";

	public ConnectionProcess(Integer index, Object testCaseInfo) {
		super(index, testCaseInfo);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {

		String reqAndResInfo = "";
		try {
			
			Map<String, Object> testcaseMap = (Map<String, Object>) Factory.clone(testCaseInfo);
			// Test Case에서 정보를 각 Map 형태로 추출한다.
			Map<String, String> requestMap = extractMap(testcaseMap, "Request");
			expectedResponse = new Response(extractMap(testcaseMap, "Response"));

			beforeRequest(testcaseMap);
			
			actualResponse = callRequest(requestMap);
			afterRequest(testcaseMap);

			reqAndResInfo = "[Request] " + requestMap + " [Response] " + actualResponse;
			checkResult(expectedResponse, actualResponse); // 실제 값과 예상 값을 비교

			result = TestResult.SUCCESS;
			oneLineLog = String.format("[%-7s] [T.C %d] %s", result, index, reqAndResInfo);
		} catch (NullPointerException exception) {
			String errorName = exception.getClass().getSimpleName();
			String errMsg = exception.getMessage();
			result = TestResult.ERROR;
			oneLineLog = String.format("[%-7s] [T.C %d] %s -> %s %s", result, index, errorName, errMsg, reqAndResInfo);
			throw new IllegalTestCaseFormatException(String.format("Info : %s", testCaseInfo.toString()), exception);
		} catch (AssertionError fail) {
			result = TestResult.FAIL;
			oneLineLog = String.format("[%-7s] [T.C %d] %s %s", result, index, fail.getMessage(), reqAndResInfo);
		} catch (Exception exception) {
			exception.printStackTrace();
			String errorName = exception.getClass().getSimpleName();
			String errMsg = exception.getMessage();
			result = TestResult.ERROR;
			oneLineLog = String.format("[%-7s] [T.C %d] %s -> %s %s", result, index, errorName, errMsg, reqAndResInfo);
			throw exception;
		}
	}

	private Response callRequest(Map<String, String> requestMap) throws Exception {
		try { 
			String url = Config.SERVER.getString(this.getClass().getSimpleName());
			Objects.requireNonNull(url);
			
			actualResponse = new Response(getResponseMap(call(url, requestMap))); // request 요청
		} catch (NullPointerException | YamlException e) {
			actualResponse = new Response(getResponseMap(call(requestMap))); // request 요청
		}
		
		return actualResponse;
	}

	@Override
	public String getOneLineLog() {
		return oneLineLog;
	}

	protected void afterRequest(Map<String, Object> testcaseMap) throws Exception {
		// 현재 아무것도 하지 않지만, 필요해지면 오버라이딩 해서 사용
	}

	protected void beforeRequest(Map<String, Object> testcaseMap) throws Exception {
		// 현재 아무것도 하지 않지만, 필요해지면 오버라이딩 해서 사용
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> extractMap(Map<String, Object> testcaseMap, String key) throws Exception {
		Objects.requireNonNull(testcaseMap.get(key), String.format("%s가 없습니다.", key));
		try {
			// element 적용하기(값 치환 혹은 parameter 제거 등)
			ElementLauncher.applyElement((Map<String, Object>) testcaseMap.get(key));

			Map<String, String> map = (Map<String, String>) testcaseMap.get(key);
			for (Entry<String, String> e : map.entrySet()) {
				if (e.getValue() != null) {
					e.setValue(new String(e.getValue().getBytes("UTF-8"), "UTF-8"));
				}
			}
			return map;
		} catch (ClassCastException e) {
			String errMsg = "테스트 케이스에서 " + key + " 정보를 추출하는데 실패했습니다. yaml에서 테스트 케이스 작성 형식을 다시 확인해 주세요.\n";
			errMsg += "[테스트 케이스] " + testcaseMap.toString() + "\n";
			throw new ClassCastException(errMsg);
		}
	}

	/**
	 * 예상 결과, 실제 결과를 비교하여 다르면 에러 발생한다.
	 * 
	 * @param expectedResponseMap
	 * @param responseMap
	 * @throws Throwable
	 */
	private void checkResult(Response expectedResponseMap, Response responseMap) {
		for (Entry<String, String> expect : expectedResponseMap.entrySet()) {
			String expectValue = expect.getValue();
			String actualValue = responseMap.get(expect.getKey());
			switch (expectValue) {
			case "true":
				assertNotNull(actualValue);
				break;
			case "false":
				assertNull(actualValue);
				break;
			default:
				assertEquals(expectValue, actualValue);
				break;
			}
		}
	}

	public Response getResponse() {
		return actualResponse;
	}

	abstract protected String call(Map<String, String> requestMap) throws Exception;
	abstract protected String call(String url, Map<String, String> requestMap) throws Exception;
	abstract protected Map<String, String> getResponseMap(String response) throws Exception;

}