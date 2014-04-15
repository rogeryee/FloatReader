package com.yeep.floatreader.service;

import java.io.IOException;
import java.util.List;

import com.yeep.floatreader.model.BookmarkList;
import com.yeep.floatreader.model.RecentFile;

public interface FloatReaderService
{
	/**
	 * Load the bookmarks of the given filename
	 * @param filename
	 * @return
	 */
	public BookmarkList loadBookmarkList(String filename) throws IOException;

	/**
	 * Update the bookmarks
	 * @param bookmarkList
	 */
	public void updateBookmarkList(BookmarkList bookmarkList) throws IOException;

	/**
	 * Load recent opened files
	 * @return
	 */
	public List<RecentFile> loadRecentFiles() throws IOException;

	/**
	 * Update the recent files
	 * @param fecentfile
	 */
	public void updateRecentFiles(RecentFile fecentfile) throws IOException;

	/**
	 * Delete all recent records
	 * @throws IOException
	 */
	public void deleteRecentFiles() throws IOException;
}
