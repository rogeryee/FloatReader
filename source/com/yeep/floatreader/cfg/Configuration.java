package com.yeep.floatreader.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.yeep.basis.util.PropertyUtil;
import com.yeep.floatreader.Constants;

public class Configuration
{
	private static Configuration instance = new Configuration();
	private static Properties APPLICATION_PROPERTIES;
	private static Settings SETTINGS;

	// Constructor
	private Configuration()
	{
		initialize();
	}

	// methods
	public static Configuration getInstance()
	{
		return instance;
	}

	public void initialize()
	{
		APPLICATION_PROPERTIES = new Properties();
		// Initialize properties of application
		InputStream stream = null;
		try
		{
			stream = new FileInputStream(new File(System.getProperty(Constants.CONFIGURATION_PROPERTIES_NAME)));
			APPLICATION_PROPERTIES.load(stream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (stream != null)
					stream.close();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}

		// build settings
		buildSettings();
	}

	private void buildSettings()
	{
		// Initialize properties of UI
		SETTINGS = new Settings();

		// window width
		SETTINGS.setWindowWidth(PropertyUtil.getInteger(APPLICATION_PROPERTIES, "window.width"));

		// window height
		SETTINGS.setWindowHeight(PropertyUtil.getInteger(APPLICATION_PROPERTIES, "window.height"));

		// Always on top
		SETTINGS.setWindowAlwaysOnTop((PropertyUtil.getBoolean(APPLICATION_PROPERTIES, "window.alwaysOnTop")));

		// Undecorated Window
		SETTINGS.setWindowUndecorated(PropertyUtil.getBoolean(APPLICATION_PROPERTIES, "window.undecorated"));

		// Show Menu
		SETTINGS.setWindowShowMenu(PropertyUtil.getBoolean(APPLICATION_PROPERTIES, "window.showMenu"));

		// Check Directories
		buildTempDirectoryAndFile();
	}

	/**
	 * Check used directories of application
	 */
	private void buildTempDirectoryAndFile()
	{
		// Get Temp directory
		String tempDirPath = System.getProperty(Constants.APPLICATION_TEMP_DIR).replace('\\', '/');

		// Check config.dir folder : %user.home%/.floatreader
		String applicationDirectory = tempDirPath + PropertyUtil.getString(APPLICATION_PROPERTIES, "config.dir");
		File floatreaderConfig = new File(applicationDirectory);
		if (!floatreaderConfig.exists())
			floatreaderConfig.mkdirs();
		SETTINGS.setApplicationDirectory(applicationDirectory);

		// Check recentfile.dir folder : %user.home%/.floatreader
		String recentfilesDirectory = tempDirPath + PropertyUtil.getString(APPLICATION_PROPERTIES, "recentfiles.dir");
		File recentfilesDirectoryPath = new File(recentfilesDirectory);
		if (!recentfilesDirectoryPath.exists())
			recentfilesDirectoryPath.mkdirs();
		SETTINGS.setRecentfilesDirectory(recentfilesDirectory);

		// Check recentfile file : %user.home%/.floatreader
		File recentFilePath = new File(recentfilesDirectory + "/" + Constants.FILE_NAME_RECENTFILES);
		if (!recentFilePath.exists())
		{
			try
			{
				recentFilePath.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// Check config.dir folder : %user.home%/.floatreader
		String bookmarkDirectory = tempDirPath + PropertyUtil.getString(APPLICATION_PROPERTIES, "bookmark.dir");
		File bookmarkDirectoryPath = new File(bookmarkDirectory);
		if (!bookmarkDirectoryPath.exists())
			bookmarkDirectoryPath.mkdirs();
		SETTINGS.setBookmarkDirectory(bookmarkDirectory);

		// Check bookmark file : %user.home%/.floatreader
		File bookmarkPath = new File(bookmarkDirectory + "/" + Constants.FILE_NAME_BOOKMARK);
		if (!bookmarkPath.exists())
		{
			try
			{
				bookmarkPath.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// Check key configuration file : %user.home%/.floatreader
		File keyConfigPath = new File(applicationDirectory + "/" + Constants.FILE_NAME_KEY_CONFIG);
		if (!keyConfigPath.exists())
		{
			try
			{
				keyConfigPath.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// initialize key configuration
		KeyConfiguration.initialize(keyConfigPath);
	}

	public static Settings getSettings()
	{
		return SETTINGS;
	}
}
