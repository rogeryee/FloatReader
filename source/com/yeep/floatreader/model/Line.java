package com.yeep.floatreader.model;

/**
 * Line class contains the string represent a line 
 */
public class Line
{
	private int beginIndex = -1;
	private int endIndex = -1;
	private String content; // content of the line

	// Constructor
	public Line(int beginIndex, int endIndex, String content)
	{
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
		this.content = content;
	}

	// Methods

	// Setters and Getters
	public int getBeginIndex()
	{
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex)
	{
		this.beginIndex = beginIndex;
	}

	public int getEndIndex()
	{
		return endIndex;
	}

	public void setEndIndex(int endIndex)
	{
		this.endIndex = endIndex;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
