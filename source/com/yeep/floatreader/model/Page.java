package com.yeep.floatreader.model;

import java.util.LinkedList;

import com.yeep.floatreader.service.HTMLConverter;

/**
 * Page objectt contains a array of lines
 */
public class Page
{
	public final static Page BLANK_PAGE = new Page(null);
	private LinkedList<Line> lines = new LinkedList<Line>();

	// Constructor
	public Page()
	{
	}

	public Page(LinkedList<Line> lines)
	{
		this.lines = lines;
	}

	/**
	 * Add a line to the first index
	 * @param line
	 */
	public void addFirst(Line line)
	{
		this.lines.addFirst(line);
	}

	/**
	 * Add a line to the last index
	 * @param line
	 */
	public void addLast(Line line)
	{
		this.lines.addLast(line);
	}

	/**
	 * Remove the first line
	 * @param line
	 */
	public void removeFirst()
	{
		if (this.lines != null && !this.lines.isEmpty())
			this.lines.removeFirst();
	}

	/**
	 * remove the last line 
	 * @param line
	 */
	public void removeLast()
	{
		if (this.lines != null && !this.lines.isEmpty())
			this.lines.removeLast();
	}

	public int getBeginIndexOfPage()
	{
		if (this.lines == null || this.lines.isEmpty())
			return -1;

		return this.lines.get(0).getBeginIndex();
	}

	/**
	 * Get the count of the line of the page
	 * @return
	 */
	public int getLineCount()
	{
		return this.lines.size();
	}

	/**
	 * Get content of the page
	 * @return
	 */
	public String getPageContent()
	{
		if (this.lines == null || this.lines.isEmpty())
			return "";

		StringBuffer content = new StringBuffer();

		for (Line line : lines)
		{
			content.append(line.getContent());
		}

		return HTMLConverter.toHTML(content.toString());
	}
}
