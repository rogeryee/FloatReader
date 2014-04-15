package com.yeep.floatreader.model;

public class RecentFile
{
	private String filename;
	private int index;

	public RecentFile()
	{
	}

	public RecentFile(String filename, int index)
	{
		this.filename = filename;
		this.index = index;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

}
