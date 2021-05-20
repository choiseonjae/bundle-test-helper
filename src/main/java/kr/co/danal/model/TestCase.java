package kr.co.danal.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.ValidationException;

import kr.co.danal.util.Factory;

public abstract class TestCase implements OneLineLog {

	protected int index;
	protected Object testCaseInfo;
	protected TestResult result = TestResult.SUCCESS;

	public TestCase(Integer index, Object testCaseInfo) {
		this.index = index;
		this.testCaseInfo = testCaseInfo;
	}

	abstract public void execute() throws Exception;

	@SuppressWarnings("unchecked")
	public static TestCase newTestCase(Integer index, Object testCaseObj)
			throws ValidationException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// TDB, LPYGW ...를 키로 가지고 있는 Map
		Map<String, Object> serverInfo = (Map<String, Object>) Factory.clone(testCaseObj);
		
		requiredOneOfSize(serverInfo);
		
		String serverName = parseServerName(serverInfo);
		Entry<String, Object> testCaseInfo = serverInfo.entrySet().iterator().next();

		String classPath = "kr.co.danal.test." + testCaseInfo.getKey();

		try {
			Class<TestCase> testCaseClazz = (Class<TestCase>) Class.forName(classPath);
			Class<?>[] constructors = new Class[] { Integer.class, Object.class };
			Constructor<TestCase> testCaseConstructor = testCaseClazz.getConstructor(constructors);
			return (TestCase) testCaseConstructor.newInstance(index, testCaseInfo.getValue());
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException(testCaseInfo.getKey() + "은 존재하지 않는 클래스 입니다. test package를 확인해주세요.");
		}
	}

	private static String parseServerName(Map<String, Object> serverInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void requiredOneOfSize(Map<String, Object> testCaseMap) throws ValidationException {
		if (testCaseMap.size() != 1) {
			throw new ValidationException(
					String.format("[TESTCASE] %s :: Test Case의 Key는 하나여야만 합니다.", testCaseMap.toString()));
		}
	}

	public int getIndex() {
		return index;
	}

	public Object getTestCaseInfo() {
		return testCaseInfo;
	};

	public boolean isSuccess() {
		return result == TestResult.SUCCESS ? true : false;
	}

	public TestResult getResult() {
		return result;
	}
}
