package com.parent;



public class Square extends Graphic{

	public Square(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public
	double getArea() {
		// TODO Auto-generated method stub
		return width * height;
	}

	@Override
	public
	double getPerimeter() {
		// TODO Auto-generated method stub
		return (width + height) * 2;
	}

}
