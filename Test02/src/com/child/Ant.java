package com.child;

import com.entity.Insect;
import com.inter.Flyanimal;

public class Ant extends Insect implements Flyanimal {

	@Override
	public void fly() {
		System.out.println("Ant can Fly");
	}
}
