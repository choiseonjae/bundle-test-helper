package kr.co.bundletesthelper.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Factory {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object clone(Object obj) {
		if(obj == null) return null;
		if (Map.class.isInstance(obj)) {
			Map<Object, Object> map = new HashMap<>();
			for(Entry<Object, Object> e : ((Map<Object, Object>)obj).entrySet()) {
				map.put(e.getKey(), clone(e.getValue()));
			}
			return map;
		} else if (List.class.isInstance(obj)) {
			List<Object> newObj = new ArrayList<>();
			for(Object o : ((List) obj)) {
				newObj.add(clone(o));
			}
			return newObj;
		}
		return new String(obj.toString());
	}
	
	public static <T> T clone(Object obj, Class<T> clazz) throws Exception {
		return clazz.cast(clone(obj));
	}
	

}
