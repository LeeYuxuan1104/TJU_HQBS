package com.child;

import com.entity.Bird;
import com.inter.Flyanimal;

public class Pigeon extends Bird implements Flyanimal{

	@Override
	public void fly() {
		System.out.println("pigeon can fly"); 
	}

	public void egg(){
		System.out.println("pigeon can lay eggs");  
	}
}
