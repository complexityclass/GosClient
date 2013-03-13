package com.example.adapters;

/**
 * @author complexityclass Handler class for text and image
 * 
 */

public class News {

	public int icon;
	public String title;

	public News() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param int icon : resourse ID {@link R.id.resources}
	 * @param String
	 *            title : text
	 * 
	 */
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
