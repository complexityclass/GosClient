package com.example.adapters;

public class News {

	public int icon;
	public String title;

	public News() {
		super();
	}

	public News(int icon, String title) {
		super();
		this.icon = icon;
		this.title = title;
	}

	@Override
	public String toString() {
		return this.title;
	}
	
}
	

