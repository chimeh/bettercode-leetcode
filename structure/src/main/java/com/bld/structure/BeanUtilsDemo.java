package com.bld.structure;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.bld.misc.MyDate;

public class BeanUtilsDemo {

	/** Simple demos of what BeanUtils can do
	 * @param args
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		Calendar c = Calendar.getInstance();
		Map<String, String> m = BeanUtils.describe(c);
		Map<String, Object> mo = new java.util.HashMap<String, Object>(m);
		printMap(mo);
				
		System.out.println("----------------------------------");
		
		Map<String,Integer> dateMap = new HashMap<String,Integer>();
		dateMap.put("day", Integer.valueOf(30));
		dateMap.put("month", 1);
		dateMap.put("year", 2006);
		
		
		MyDate myDate = new MyDate();

		BeanUtils.populate(myDate, dateMap);
		
		// BeanUtils.copyProperties()
		
		System.out.println("----------------------------------");
		printMap(PropertyUtils.describe(myDate));

		System.out.println(myDate);
	}

	/**
	 * @param m
	 */
	private static void printMap(Map<String, Object> m) {
		Set<String> keys = m.keySet();
		for (String v : keys) {
			System.out.println(v + "-->" + m.get(v).toString() + "(" + m.get(v).getClass().getName() + ")");
		}
	}

}
