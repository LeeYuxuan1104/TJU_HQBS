package com.parent;

public abstract class Graphic {
	public int x, y;		 // 画图的坐标
    public int width, height;// 图形的宽和高

    public Graphic(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    abstract double getArea();

    abstract double getPerimeter();
}
