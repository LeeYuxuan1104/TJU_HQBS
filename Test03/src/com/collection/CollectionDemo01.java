package com.collection;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionDemo01 {
	public static void main(String[] args) {
		Collection<String> collection = new ArrayList<>();
		collection.add("Hello");
		collection.add("My");
		collection.add("World");
		System.out.println("size:" + collection.size());

		System.out.println("c:" + collection);
	}
}
