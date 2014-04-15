package com.yeep.floatreader.model;

import java.util.ArrayList;
import java.util.List;

public class BookmarkList
{
	private String filename;
	private List<Bookmark> bookmarks = new ArrayList<Bookmark>();

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public List<Bookmark> getBookmarks()
	{
		return bookmarks;
	}

	public void setBookmarks(List<Bookmark> bookmarks)
	{
		this.bookmarks = bookmarks;
	}

}
