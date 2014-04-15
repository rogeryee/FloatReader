package com.yeep.floatreader.cfg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import com.yeep.basis.util.PropertyUtil;
import com.yeep.floatreader.Constants;
import com.yeep.floatreader.model.Key;
import com.yeep.floatreader.model.KeyHandler;

public class KeyConfiguration
{
	private static Properties KEY_PROPERTIES;
	private static File keyFile;

	// propties definitions
	private final static String PROP_KEY_TRAY = "key.tray";
	private final static String PROP_KEY_PAGE_DOWN = "key.pagedown";
	private final static String PROP_KEY_PAGE_UP = "key.pageup";

	// Constructor

	// methods

	public static void initialize(File file)
	{
		if (file == null)
			throw new IllegalArgumentException("File could not be null");

		keyFile = file;

		KEY_PROPERTIES = new Properties();

		// Initialize properties of application
		InputStream stream = null;
		try
		{
			stream = new FileInputStream(file);
			KEY_PROPERTIES.load(stream);
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

		// initialize key handler object
		initKeyHandler();
	}

	/**
	 * Initialize key handler
	 */
	private static void initKeyHandler()
	{
		KeyHandler.KEY_TRAY.setName(PROP_KEY_TRAY);
		KeyHandler.KEY_TRAY.setCode(PropertyUtil.getInteger(KEY_PROPERTIES, PROP_KEY_TRAY, KeyHandler.DEFAULT_KEY_TRAY));

		KeyHandler.KEY_PAGE_UP.setName(PROP_KEY_PAGE_UP);
		KeyHandler.KEY_PAGE_UP.setCode(PropertyUtil.getInteger(KEY_PROPERTIES, PROP_KEY_PAGE_UP,
				KeyHandler.DEFAULT_KEY_PAGE_UP));

		KeyHandler.KEY_PAGE_DOWN.setName(PROP_KEY_PAGE_DOWN);
		KeyHandler.KEY_PAGE_DOWN.setCode(PropertyUtil.getInteger(KEY_PROPERTIES, PROP_KEY_PAGE_DOWN,
				KeyHandler.DEFAULT_KEY_PAGE_DOWN));
	}

	/**
	 * Update key value to file
	 */
	public static void update()
	{
		if (keyFile == null)
			throw new IllegalStateException("The key file should be initialize before calling update");

		try
		{
			FileOutputStream writerStream = new FileOutputStream(keyFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(writerStream, Constants.CHARSET_UTF8));

			for (Key key : KeyHandler.getKeyList())
			{
				bw.write(key.getName() + "=" + key.getKeyCode());
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

}
