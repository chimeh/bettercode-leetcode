// BEGIN main
package com.bld.structure;


public class ArrayIteratorDemo {
	
	private final static String[] names = {
		"rose", "petunia", "tulip"
	};
	
	public static void main(String[] args) {
		ArrayIterator<String> arrayIterator = new ArrayIterator<String>(names);

		// Java 5, 6 way
		for (String s : arrayIterator) {
			System.out.println(s);
		}
		
		// Java 8 way
//		arrayIterator.forEach(s->System.out.println(s));
	}
}
// END main
