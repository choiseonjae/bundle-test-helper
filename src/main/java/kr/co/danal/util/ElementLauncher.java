package kr.co.danal.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kr.co.danal.element.Element;
import kr.co.danal.exception.ElementExecuteException;
import kr.co.danal.exception.IllegalTestCaseFormatException;

public class ElementLauncher {

	/**
	 *
	 * elements 패키기지에 있는 클래스 로직을 수행한다.
	 * 
	 * @param map
	 * @throws Exception
	 */
	public static void applyElement(Map<String, Object> map) {

//		try {

		Iterator<Entry<String, Object>> elements = map.entrySet().iterator();
		while (elements.hasNext()) {
			Entry<String, Object> element = elements.next();
			if (element.getValue() == null) {
				elements.remove();
			} else {
				map.put(element.getKey(), ElementLauncher.execElement(element));
			}
		}

//		} catch (Exception e) {
//			e.
//		}
	}

	/**
	 * element 클래스를 찾아서 실행시킨다.
	 * 
	 * @param entry
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws Throwable
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static String execElement(Entry<String, Object> entry) {

		String key = entry.getKey();
		Object value = entry.getValue();

		/*
		 * key : value 의 value 가 element 이면 한번 더 실행
		 * 
		 */
		if (value instanceof Map) {

			Map<String, Object> valueMap = (Map<String, Object>) value;
			if (valueMap.size() > 1) {
				// value의 Map 들도 element가 존재할 수 있으니 적용
				ElementLauncher.execElements(valueMap);
				try {
					Element element = findElement(key);
					return element.get(valueMap);
				} catch (ClassNotFoundException e) {
					throw new ElementExecuteException(String.format("Element %s가 존재하지 않습니다.", key), e);
				}

			} else {
				// 밑에 존재하는 element를 실행 시킨 뒤 밑으로 내려간다.
				Entry<String, Object> entryChlid = valueMap.entrySet().iterator().next();
				value = execElement(entryChlid);
			}
		}

		if (value instanceof List) {
			value = mergeList((List) value);
		}

		/*
		 * parameter 혹은 Element의 Value
		 * 
		 * ex) TID : "202011101443" 에서 "202011101443" 이거나 AUTHSESSION : {Get : TID} 에서
		 * TID이다.
		 * 
		 */
		if (value instanceof String) {
			return executeStringElement(key, value.toString());
		}

		String errorMsg = String.format("Test Cast(Yaml)의 형식이 틀렸거나 존재하지 않는 Element를 호출했습니다.\n key : %s, value : %s", key, value); 
		throw new IllegalTestCaseFormatException(errorMsg);

	}
	
	private static String executeStringElement(String key, String value) {
		try {
			// 밑에 element가 더 존재한다.
			return findElement(key).get(value);
		} catch (ClassNotFoundException exception) {
			return value; // Element가 아닌 값으로의 Value
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Element findElement(String key) throws ClassNotFoundException {
		String path = "kr.co.danal.element." + key;
		try {
			Class<Element> clazz = (Class<Element>) Class.forName(path);
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ElementExecuteException(String.format("Element %s를 newInstance 하는데 실패했습니다."), e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static String mergeList(List<Object> list) {
		StringBuilder listStr = new StringBuilder();
		for (Object element : list) {
			if (Map.class.isInstance(element)) {
				Map<String, Object> map = (Map<String, Object>) element;
				listStr.append(execElement(map.entrySet().iterator().next()));
			} else if (List.class.isInstance(element)) {
				List<Object> listPup = (List) element;
				listStr.append(mergeList(listPup));
			} else {
				listStr.append(element);
			}
		}
		return listStr.toString();
	}

	private static void execElements(Map<String, Object> elements)  {
		for (Entry<String, Object> element : elements.entrySet()) {
			elements.put(element.getKey(), execElement(element));
		}
	}

}