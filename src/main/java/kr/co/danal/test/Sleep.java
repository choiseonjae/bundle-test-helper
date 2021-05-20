package kr.co.danal.test;

import kr.co.danal.model.TestCase;

public class Sleep extends TestCase {
	
	private long millis = 0;

	public Sleep(Integer index, Object testCaseInfo) {
		super(index, testCaseInfo);
	}

	@Override
	public String getOneLineLog() {
		return String.format("Sleep %d milli sec ...", millis);
	}

	@Override
	public void execute() throws Exception {
		millis = Integer.parseInt(testCaseInfo.toString());
		Thread.sleep(millis);
	}

}
