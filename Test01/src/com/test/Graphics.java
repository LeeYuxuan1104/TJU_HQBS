package com.test;

import java.applet.Applet;

import com.parent.Circle;
import com.parent.Square;
import com.parent.Triangle;



public class Graphics extends Applet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	Square square = new Square(5, 15, 25, 25);
	Triangle triangle = new Triangle(5, 50, 8, 4);
	Circle circle = new Circle(5, 90, 25, 25);

	public void paint(java.awt.Graphics g) {
		g.drawRect(square.x, square.y, square.width, square.height);
		g.drawString("Square area:" + square.getArea(), 50, 35);
		g.drawString("Square Perimeter:" + square.getPerimeter(), 50, 55);
		g.drawOval(circle.x, circle.y, circle.width, circle.height);
		g.drawString("circle Area:" + circle.getArea(), 50, 95);
	}
}
