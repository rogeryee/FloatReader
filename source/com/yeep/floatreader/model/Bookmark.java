package com.yeep.floatreader.model;

public class Bookmark
{
	private int index;
	private String description;

	// Constructor
	public Bookmark(int index, String description)
	{
		this.index = index;
		this.description = description;
	}

	// Getters and Setters
	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
