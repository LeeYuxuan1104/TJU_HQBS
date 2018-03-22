package com.collection;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionDemo04 {
	public static void main(String[] args) {
		Collection<String> collection = new ArrayList<>();
		collection.add("Hello");
		collection.add("My");
		collection.add("World");

		System.out.println("contains:" + collection.contains("Hello"));
		System.out.println("contains:" + collection.contains("android"));
	}
}
