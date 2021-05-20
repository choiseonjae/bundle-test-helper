package kr.co.danal.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import kr.co.danal.model.Response;
import kr.co.danal.model.TestCase;
import kr.co.danal.util.Factory;


public class Update extends TestCase {
	
	private TestCase testCase;
	
	public Update(Integer index, Object testCaseInfo) {
		super(index, testCaseInfo);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		Object testCaseInfoClone = Factory.clone(testCaseInfo);
		if(!Map.class.isInstance(testCaseInfoClone)) {
			throw new Exception("Update 는 Map 형태여야 함. " + testCaseInfoClone.toString());
		}
		
		Map<String, Object> infoMap = (Map<String, Object>) testCaseInfoClone;
		
		if (!infoMap.containsKey("Origin") || !infoMap.containsKey("Change")) {
			throw new Exception("Origin, Change key가 없음.");
		}

		Map<String, Object> origin = (Map<String, Object>) Factory.clone(infoMap.get("Origin"));
		Map<String, Object> change = (Map<String, Object>) infoMap.get("Change");
		
		// clone 된  map key를 때고, value만 사용.-> 그게  Map 형태임(request, response를 가지고 있는...)
		Entry<String, Object> process = ((Map<String, Object>) origin).entrySet().iterator().next();
		Map<String, Object> defaultMap = (Map<String, Object>) process.getValue();

		for (Entry<String, Object> entry : change.entrySet()) {
			if (!defaultMap.containsKey(entry.getKey())) {
				defaultMap.put(entry.getKey(), entry.getValue());
			}
			// Map 일경우
			if (Map.class.isInstance(entry.getValue())) {
				Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
				((Map<String, Object>) defaultMap.get(entry.getKey())).putAll(valueMap);
			} else {
				if (entry.getValue() != null) {
					defaultMap.put(entry.getKey(), entry.getValue());
				} else {
					defaultMap.remove(entry.getKey());
				}
			}
		}
		
		Map<String, Object> processMap = new HashMap<>();
		processMap.put(process.getKey(), process.getValue());
		testCase = TestCase.newTestCase(index, processMap);
		try {
			testCase.execute();
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		} finally {
			// 대행 객체의 결과와 Sync 맞추기
			result = testCase.getResult();
			index = testCase.getIndex();
		}
	}

	@Override
	public String getOneLineLog() {
		return testCase.getOneLineLog();
	}
	
	public Response getResponse() {
		return ConnectionProcess.class.cast(testCase).getResponse();
	}
}
