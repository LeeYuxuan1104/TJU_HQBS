package com.collection;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionDemo05 {
	public static void main(String[] args) {
		Collection<String> collection = new ArrayList<>();
		collection.add("Hello");
		collection.add("My");
		collection.add("World");
		System.out.println("isEmpty:" + collection.isEmpty());
	}
}
