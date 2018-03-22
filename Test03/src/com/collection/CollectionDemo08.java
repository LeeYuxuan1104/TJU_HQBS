package com.collection;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionDemo08 {
	public static void main(String[] args) {
		// 创建集合1
		Collection<String> c1 = new ArrayList<String>();
		c1.add("abc1");
		c1.add("abc2");
		c1.add("abc3");
		c1.add("abc4");

		// 创建集合2
		Collection<String> c2 = new ArrayList<String>();

		c2.add("abc5");
		c2.add("abc6");
		c2.add("abc7");

		System.out.println("addAll:" + c1.addAll(c2));
		System.out.println("c1:" + c1);
		System.out.println("containsAll:" + c1.containsAll(c2));
		System.out.println("c2:" + c2);

	}
}
