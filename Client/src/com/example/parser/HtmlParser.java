package com.example.parser;


import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNode.TagAllCondition;

import com.example.client.AboutUsActivity;

/**
 * @author complexityclass Class for html parsing Each method are usefull for
 *         define web page, so here are so many methods
 * 
 */
public class HtmlParser {

	public TagNode rootNode;	

	/**
	 * Constructor
	 * 
	 * @param String
	 *            html : web page URL
	 * @throws Exception
	 *             : web page doesn't exist
	 * */
	public HtmlParser(String html) throws Exception {

		HtmlCleaner cleaner = new HtmlCleaner();
		rootNode = cleaner.clean(html);
	}
	
	public HtmlParser(TagNode rootNode){
		this.rootNode = rootNode;
	}

	public List<TagNode> getObjectByTagAndClass(String tag, String CSSClassName) {

		List<TagNode> linkList = new ArrayList<TagNode>();
		TagNode contentElements[] = rootNode.getElementsByName(tag, true);

		for (int i = 0; contentElements != null && i < contentElements.length; i++) {
			String classType = contentElements[i].getAttributeByName("class");
			if (classType != null && classType.equals(CSSClassName)) {
				linkList.add(contentElements[i]);
			}
		}
		return linkList;

	}

	/**
	 * Get List of tags by class name Use in {@link AboutUsActivity}
	 * 
	 * @param String
	 *            CSSClassName
	 * @return List<TagNode>
	 * */
	public List<TagNode> getContentByClassName(String CSSClassName) {

		List<TagNode> linklist = new ArrayList<TagNode>();
		TagNode contentElements[] = rootNode.getElementsByName("div", true);

		for (int i = 0; contentElements != null && i < contentElements.length; i++) {
			String classType = contentElements[i].getAttributeByName("class");
			if (classType != null && classType.equals(CSSClassName)) {
				linklist.add(contentElements[i]);
			}
		}
		return linklist;
	}

	public List<TagNode> getLinks(String CSSClassName) {

		List<TagNode> linkList = new ArrayList<TagNode>();
		TagNode usageClass[] = rootNode.getElementsByName("a", true);

		for (int i = 0; i < usageClass.length && usageClass != null; i++) {
			String classType = usageClass[i].getAttributeByName("class");
			if (classType != null && classType.equals(CSSClassName)) {
				linkList.add(usageClass[i]);
			}
		}

		return linkList;
	}

	public List<TagNode> getSpanText(String CSSClassName) {
		List<TagNode> spanList = new ArrayList<TagNode>();
		TagNode usageClass[] = rootNode.getElementsByName("span", true);

		for (int i = 0; i < usageClass.length && usageClass != null; i++) {
			String classType = usageClass[i].getAttributeByName("class");
			if (classType != null && classType.equals(CSSClassName)) {
				spanList.add(usageClass[i]);
			}
		}

		return spanList;
	}

	public List<TagNode> getNews(String CSSClassName) {
		List<TagNode> liList = new ArrayList<TagNode>();
		TagNode[] usageClass = rootNode.getElementsByName("p", true);

		for (int i = 0; i < usageClass.length && usageClass != null; i++) {
			String classType = usageClass[i].getAttributeByName("class");
			if (classType != null && classType.equals(CSSClassName)) {
				liList.add(usageClass[i]);
			}
		}

		return liList;
	}

}
