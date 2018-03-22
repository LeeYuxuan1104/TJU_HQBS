package com.parent;




public class Triangle extends Graphic{
	public double c;
	public Triangle(int x, int y, int width, int height) {
		super(x, y, width, height);
		c = Math.sqrt(width * width + height * height);
	}

	@Override
	public double getArea() {
		// TODO Auto-generated method stub
		return 0.5 * width * height;
	}

	@Override
	public double getPerimeter() {
		// TODO Auto-generated method stub
		 return width + height + c;
	}

}
