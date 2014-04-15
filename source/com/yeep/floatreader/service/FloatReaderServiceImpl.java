package com.yeep.floatreader.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yeep.floatreader.Constants;
import com.yeep.floatreader.cfg.Configuration;
import com.yeep.floatreader.model.Bookmark;
import com.yeep.floatreader.model.BookmarkList;
import com.yeep.floatreader.model.RecentFile;

public class FloatReaderServiceImpl implements FloatReaderService
{
	private static final String FILE_PATH_BOOKMARK = Configuration.getSettings().getBookmarkDirectory() + "/"
			+ Constants.FILE_NAME_BOOKMARK;
	private static final String FILE_PATH_RECENTFILES = Configuration.getSettings().getRecentfilesDirectory() + "/"
			+ Constants.FILE_NAME_RECENTFILES;

	private static final String CHARSET_DEFAULT = Constants.CHARSET_UTF8;

	@Override
	public BookmarkList loadBookmarkList(String filename) throws IOException
	{
		BookmarkList bookmarkList = null;
		Map<String, BookmarkList> bookmarkListMap = loadAllBookmarkList();
		if (bookmarkListMap != null)
		{
			bookmarkList = bookmarkListMap.get(filename);
		}
		return bookmarkList;
	}

	@Override
	public void updateBookmarkList(BookmarkList bookmarkList) throws IOException
	{
		Map<String, BookmarkList> bookmarkListMap = loadAllBookmarkList();
		if (bookmarkListMap != null)
		{
			bookmarkListMap.put(bookmarkList.getFilename(), bookmarkList);

			List<String> texts = new ArrayList<String>();
			for (BookmarkList bookmarks : bookmarkListMap.values())
			{
				texts.add(convertBookmarkToCode(bookmarks));
				write(texts, FILE_PATH_BOOKMARK);
			}
		}
	}

	/**
	 * Load all bookmarks
	 * @return
	 * @throws IOException 
	 * @throws IOException 
	 */
	private Map<String, BookmarkList> loadAllBookmarkList() throws IOException
	{
		Map<String, BookmarkList> map = new HashMap<String, BookmarkList>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_PATH_BOOKMARK), Charset
				.forName(CHARSET_DEFAULT)));

		String row;
		while ((row = br.readLine()) != null)
		{
			BookmarkList bookmarkList = convertCodeToBookmark(row);

			if (bookmarkList != null)
			{
				map.put(bookmarkList.getFilename(), bookmarkList);
			}
		}
		return map;
	}

	@Override
	public List<RecentFile> loadRecentFiles() throws IOException
	{
		List<RecentFile> list = new ArrayList<RecentFile>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_PATH_RECENTFILES), Charset
				.forName(CHARSET_DEFAULT)));

		String row;
		while ((row = br.readLine()) != null)
		{
			RecentFile recentfile = convertCodeToRecentFile(row);

			if (recentfile != null)
			{
				list.add(recentfile);
			}
		}
		return list;
	}

	@Override
	public void updateRecentFiles(RecentFile recentfile) throws IOException
	{
		List<RecentFile> recentfiles = loadRecentFiles();
		if (recentfiles != null)
		{
			String recentFilename = recentfile.getFilename();
			RecentFile toRemove = null;
			for (RecentFile temp : recentfiles)
			{
				if (temp.getFilename().equalsIgnoreCase(recentFilename))
					toRemove = temp;
			}
			if (toRemove != null)
				recentfiles.remove(toRemove);
			recentfiles.add(recentfile);

			// update to file
			List<String> texts = new ArrayList<String>();
			for (RecentFile temp : recentfiles)
			{
				texts.add(convertRecentFileToCode(temp));
				write(texts, FILE_PATH_RECENTFILES);
			}
		}
	}

	@Override
	public void deleteRecentFiles() throws IOException
	{
		File file = new File(FILE_PATH_RECENTFILES);
		if (file.exists())
			file.delete();
		file.createNewFile();
	}

	/**
	 * Write the text to the specific file
	 * @param texts
	 * @param path
	 */
	private void write(List<String> texts, String path)
	{
		try
		{
			FileOutputStream writerStream = new FileOutputStream(path);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(writerStream, CHARSET_DEFAULT));

			for (String text : texts)
			{
				bw.write(text);
				bw.newLine();
			}

			bw.flush();
			bw.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Convert the code to bookmark object
	 * Pattern:
	 *  filename|index1$description1|index2$description2
	 * @param code
	 * @return
	 */
	private BookmarkList convertCodeToBookmark(String code)
	{
		if (code == null || "".equals(code.trim()))
			return null;

		BookmarkList bookmarkList = new BookmarkList();

		String[] statements = code.split("\\|");

		if (statements.length < 2)
			return null;

		bookmarkList.setFilename(statements[0]); // filename
		for (int i = 1; i < statements.length; i++)
		{
			String[] bookmark = statements[i].split("\\$");
			if (bookmark.length == 2)
			{
				bookmarkList.getBookmarks().add(new Bookmark(Integer.parseInt(bookmark[0]), bookmark[1]));
			}
		}

		return bookmarkList;
	}

	/**
	 * Convert bookmark object to the code
	 * Pattern:
	 *  filename|index1$description1|index2$description2
	 * @param code
	 * @return
	 */
	private String convertBookmarkToCode(BookmarkList bookmarkList)
	{
		if (bookmarkList == null || bookmarkList.getFilename() == null)
			return null;

		StringBuffer sb = new StringBuffer();
		sb.append(bookmarkList.getFilename().replaceAll("\\|", " ")).append("|");

		for (Bookmark bookmark : bookmarkList.getBookmarks())
		{
			sb.append(bookmark.getIndex()).append("$");
			sb.append(bookmark.getDescription()).append("|");
		}

		return sb.toString();
	}

	/**
	 * Convert the code to the RecentFile object
	 * Pattern:
	 *  filename|index1
	 * @param code
	 * @return
	 */
	private RecentFile convertCodeToRecentFile(String code)
	{
		if (code == null || "".equals(code.trim()))
			return null;

		String[] statements = code.split("\\|");
		if (statements.length == 2)
		{
			return new RecentFile(statements[0], Integer.parseInt(statements[1]));
		}
		return null;
	}

	/**
	 * Convert RecentFile object to the code
	 * Pattern:
	 *  filename|index1
	 * @param code
	 * @return
	 */
	private String convertRecentFileToCode(RecentFile recentfile)
	{
		if (recentfile == null || recentfile.getFilename() == null)
			return null;

		StringBuffer sb = new StringBuffer();
		sb.append(recentfile.getFilename().replaceAll("\\|", " ")).append("|");
		sb.append(recentfile.getIndex());

		return sb.toString();
	}
}
