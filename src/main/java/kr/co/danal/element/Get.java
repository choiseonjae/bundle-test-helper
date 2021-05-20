package kr.co.danal.element;

import java.util.List;
import java.util.Map;

import org.apache.log4j.MDC;

import kr.co.danal.exception.ElementExecuteException;
import kr.co.danal.model.Response;

public class Get implements Element {
	
	@Override
	public String get(String key) {
		// get의 경우 key : get, value : string(가져오고자 하는 값)
		@SuppressWarnings("unchecked")
		List<Response> responses = (List<Response>) MDC.get("responses");
		
		System.out.println(key);
		for (int index = responses.size() - 1; index >= 0; index--) {
			Response response = responses.get(index);
			if (response.containsKey(key)) {
				String result = response.get(key);
				return result;
			}
		}
		
		// TODO Response 보여주기 
		throw new ElementExecuteException(String.format("모든 Response 중 %s는 존재하지 않습니다.", key), Get.class);
	}

	@Override
	public String get(Map<String, Object> map) {
		return Element.noApplyMapType();
	}

}
