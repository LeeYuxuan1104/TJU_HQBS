package com.model.entity;

public class MEGood {
	private String model;
	private String brand;
	private String information;
	private double price;
	private int count;
	
	public MEGood() {
	
	}

	public MEGood(String model, String brand, String information, double price,
			int count) {
		super();
		this.model = model;
		this.brand = brand;
		this.information = information;
		this.price = price;
		this.count = count;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
