package com.parent;


public class Circle extends Graphic {
	public double r;

	public Circle(int x, int y, int width, int height) {
		super(x, y, width, height);
		r = (double) width / 2.0;
	}

	@Override
	public double getArea() {
		 return Math.PI * r * r;
	}

	@Override
	public double getPerimeter() {
		 return 2 * Math.PI * r;
	}

}
