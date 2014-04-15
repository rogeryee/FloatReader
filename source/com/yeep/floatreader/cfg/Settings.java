package com.yeep.floatreader.cfg;

public class Settings
{
	private String applicationDirectory; // config.dir
	private String recentfilesDirectory; // recentfiles.dir
	private String bookmarkDirectory; // bookmark.dir

	private Boolean windowAlwaysOnTop;// window.alwaysOnTop
	private Boolean windowUndecorated;// window.undecorated
	private Boolean windowShowMenu;// window.showMenu
	private int windowWidth;// window.width
	private int windowHeight;// window.height

	// Setters and Getters
	public String getApplicationDirectory()
	{
		return applicationDirectory;
	}

	public void setApplicationDirectory(String applicationDirectory)
	{
		this.applicationDirectory = applicationDirectory;
	}

	public String getRecentfilesDirectory()
	{
		return recentfilesDirectory;
	}

	public void setRecentfilesDirectory(String recentfilesDirectory)
	{
		this.recentfilesDirectory = recentfilesDirectory;
	}

	public String getBookmarkDirectory()
	{
		return bookmarkDirectory;
	}

	public void setBookmarkDirectory(String bookmarkDirectory)
	{
		this.bookmarkDirectory = bookmarkDirectory;
	}

	public Boolean getWindowAlwaysOnTop()
	{
		return windowAlwaysOnTop;
	}

	public void setWindowAlwaysOnTop(Boolean windowAlwaysOnTop)
	{
		this.windowAlwaysOnTop = windowAlwaysOnTop;
	}

	public Boolean getWindowUndecorated()
	{
		return windowUndecorated;
	}

	public void setWindowUndecorated(Boolean windowUndecorated)
	{
		this.windowUndecorated = windowUndecorated;
	}

	public Boolean getWindowShowMenu()
	{
		return windowShowMenu;
	}

	public void setWindowShowMenu(Boolean windowShowMenu)
	{
		this.windowShowMenu = windowShowMenu;
	}

	public int getWindowWidth()
	{
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth)
	{
		this.windowWidth = windowWidth;
	}

	public int getWindowHeight()
	{
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight)
	{
		this.windowHeight = windowHeight;
	}
}
