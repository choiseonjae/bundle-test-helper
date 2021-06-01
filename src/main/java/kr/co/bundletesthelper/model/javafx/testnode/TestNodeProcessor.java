package kr.co.bundletesthelper.model.javafx.testnode;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationException;

import org.apache.log4j.MDC;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.bundletesthelper.model.Response;
import kr.co.bundletesthelper.model.TestCase;
import kr.co.bundletesthelper.model.TestNodeType;
import kr.co.bundletesthelper.model.TestResult;
import kr.co.bundletesthelper.test.ConnectionProcess;
import kr.co.bundletesthelper.test.Update;
import kr.co.bundletesthelper.util.log.Logger;

public class TestNodeProcessor extends TestNode implements Runnable {

	private TestNodeFile parentNode;
	private String name;
	private List<TestCase> testCases = new ArrayList<>();
	private TestResult result = TestResult.SUCCESS;
	private static final String PROCESS_NAME_UNKNOWN = "Unknown";

	private List<Response> responses = new ArrayList<>();

	private static final Image FILE_IMAGE = new Image(
			TestNodeFolder.class.getClass().getResourceAsStream("/tc_img.png"));

	public TestNodeProcessor(TestNodeFile parentNode, List<Object> testCaseObjs) {
		super();
		this.parentNode = parentNode;
		this.name = getProcessName(testCaseObjs);
		setTestCases(testCaseObjs);
		setValue(this);
		setGraphic(new ImageView(FILE_IMAGE));
	}

	public TestResult getResult() {
		return result;
	}

	@Override
	public void test() {

		try {
			for (TestCase testCase : testCases) {
				testCase.execute();
				if (testCase instanceof ConnectionProcess) {
					responses.add(ConnectionProcess.class.cast(testCase).getResponse());
				} else if (testCase instanceof Update) {
					responses.add(Update.class.cast(testCase).getResponse());
				}
				MDC.put("responses", responses);
			}
			print(); // 진행 상황 출력
		} catch (AssertionError failure) {
			print(); // 진행 상황 출력
		} catch (Exception exception) {
			exception.printStackTrace();
			print(exception); // 진행 상황 출력
//			Logger.error(exception);
		} finally {
			responses.clear();
			MDC.remove("responses");
		}


	}

	@Override
	public TestNodeType getType() {
		return TestNodeType.TEST;
	}

	@Override
	public void print() {
		Logger.debug();
		Logger.info(String.format("[%s -> %s]", parentNode, name));
		for (TestCase tc : testCases) {
			if (tc.isSuccess()) {
				Logger.debug(tc.getOneLineLog());
			} else {
				if (tc.getResult() == TestResult.FAIL) {
					Logger.warn(tc.getOneLineLog());
				} else {
					Logger.error(tc.getOneLineLog());
				}
				break;
			}
		}
	}
	
	public void print(Exception exception) {
		Logger.debug();
		Logger.info(String.format("[%s -> %s]", parentNode, name));
		for (TestCase tc : testCases) {
			if (tc.isSuccess()) {
				Logger.debug(tc.getOneLineLog());
			} else {
				if (tc.getResult() == TestResult.FAIL) {
					Logger.warn(tc.getOneLineLog());
				} else {
					Logger.error(exception, tc.getOneLineLog());
				}
				break;
			}
		}
	}

	@Override
	public String toString() {
		return testCases.isEmpty() ? "[생성 실패] " + name : name;
	}

	private void setTestCases(List<Object> testCaseObjs) {
		try {
			int index = 0;
			for (Object obj : testCaseObjs) {
				testCases.add(TestCase.newTestCase(index++, obj));
			}
		} catch (ValidationException | ClassNotFoundException | NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			Logger.error(e);
			testCases = new ArrayList<>();
		}
	}

	private String getProcessName(List<Object> testCaseObjs) {
		return testCaseObjs.get(0) instanceof String ? testCaseObjs.remove(0).toString() : PROCESS_NAME_UNKNOWN;
	}

	@Override
	public File getFile() {
		return parentNode.getFile();
	}

	@Override
	public void run() {
		test();
	}
}
