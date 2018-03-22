package com.test;

import com.child.Ant;
import com.child.Pigeon;

public class InterfaceDemo {
	public static void main(String args[]) {
		Ant a = new Ant();
		a.fly();
		System.out.println("Ant's legs are " + a.legnum);
		Pigeon p = new Pigeon();
		p.fly();
		p.egg();
	}
}
