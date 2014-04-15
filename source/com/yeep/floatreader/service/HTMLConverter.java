package com.yeep.floatreader.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLConverter
{
	// Character Definitions
	// Standard characters
	public static final char SYMBOL_NEW_LINE = '\n';
	public static final char SYMBOL_SPACE = ' ';

	// HTML tags definitions
	public static final String HTML_TAG_HTML = "<html>";
	public static final String HTML_TAG_HTML_END = "</html>";
	public static final String HTML_TAG_BR = "<br>";
	public static final String HTML_SPACE = "&nbsp;";

	private final static String regxpForHtml = "<([^>]*)>"; // filter all tags which begin with '<' and end with '>'

	/**
	 * Create a string represents a HTML page <p> "<html>......</html>
	 */
	public static String toHTML(String string)
	{
		return HTML_TAG_HTML + string + HTML_TAG_HTML_END;
	}

	/**
	 * Convert the specific character to the HTML code
	 */
	public static String toHTML(char character)
	{
		switch (character)
		{
			case SYMBOL_NEW_LINE:
				return HTML_TAG_BR;
			case SYMBOL_SPACE:
				return HTML_SPACE;
			default:
				return String.valueOf(character);
		}
	}

	/**
	 * Returns true if the tag equals <BR>
	 */
	public static boolean isHTMLNewLine(String tag)
	{
		return HTML_TAG_BR.equalsIgnoreCase(tag);
	}

	/**  
	 * Filter all tags which begin with '<' and end with '>'
	 * <p>  
	 *   
	 * @param str  
	 * @return String  
	 */
	public static String filterHtml(String str)
	{
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1)
		{
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString().replace(HTML_SPACE, " ");
	}
}
