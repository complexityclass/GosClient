package com.example.parser;

import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNode.TagAllCondition;

public class HtmlParser {

	TagNode rootNode;

	public HtmlParser(String html) throws Exception {

		HtmlCleaner cleaner = new HtmlCleaner();
		rootNode = cleaner.clean(html);
	}

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
	
	
	public void getLinks(){
		
	}

}
