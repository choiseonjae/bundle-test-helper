package kr.co.bundletesthelper.model.yaml.exception;

public class YamlException extends Exception {

	private static final long serialVersionUID = -1112363876770275933L;
	private String reason = "";
	private String errMsg = "";
	
	public final static String RESULT_NULL = "RESULT_NULL";
	public final static String CASTING_FAIL = "CASTING_FAIL";
	public final static String EMPTY_KEY = "EMPTY_KEY";
	public final static String INVALID_KEY = "INVALID_KEY";
	public final static String YAML_FILE_READ_FAIL = "YAML_FILE_READ_FAIL";
	public final static String NOT_FOUND_YAML_FILE = "NOT_FOUND_YAML_FILE";
	public final static String NOT_FOUND_YAML_PATH = "NOT_FOUND_YAML_PATH";

	public YamlException(String reason, String errMsg) {
		this.reason = reason;
		this.errMsg = errMsg;
	}
	
	public String getReason() {
		return reason;
	}
	
	public String getMsg() {
		return errMsg;
	}

	public YamlException(String message) {
		super(message);
	}

	public YamlException(Throwable cause) {
		super(cause);
	}

	public YamlException(String message, Throwable cause) {
		super(message, cause);
	}

	public YamlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
