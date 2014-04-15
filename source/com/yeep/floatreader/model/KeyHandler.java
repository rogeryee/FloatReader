package com.yeep.floatreader.model;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class KeyHandler
{
	// default values
	public static int DEFAULT_KEY_TRAY = 0x1B;
	public static int DEFAULT_KEY_PAGE_UP = 0x21;
	public static int DEFAULT_KEY_PAGE_DOWN = 0x22;

	// Keys
	public static Key KEY_TRAY = new Key(DEFAULT_KEY_TRAY);
	public static Key KEY_PAGE_UP = new Key(DEFAULT_KEY_PAGE_UP);
	public static Key KEY_PAGE_DOWN = new Key(DEFAULT_KEY_PAGE_DOWN);

	private static List<Key> keyList = Arrays.asList(new Key[] { KEY_TRAY, KEY_PAGE_UP, KEY_PAGE_DOWN });
	private static int[] invalidKeys = new int[] { KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_F1, KeyEvent.VK_F2,
			KeyEvent.VK_F3, KeyEvent.VK_F4 };

	/**
	 * Set default value for keys
	 */
	public static void setDefault()
	{
		KEY_TRAY.setCode(DEFAULT_KEY_TRAY);
		KEY_PAGE_UP.setCode(DEFAULT_KEY_PAGE_UP);
		KEY_PAGE_DOWN.setCode(DEFAULT_KEY_PAGE_DOWN);
	}

	public static List<Key> getKeyList()
	{
		return keyList;
	}

	/**
	 * Identify the new code of the specific key is used by other keys or not.
	 * @param key
	 * @param code
	 * @return
	 */
	public static boolean isDuplicatedKey(Key key, int code)
	{
		boolean ret = false;

		for (Key temp : keyList)
		{
			if (temp == key)
				continue;
			if (code == temp.getKeyCode())
				return true;
		}

		return ret;
	}

	/**
	 * Identify the new code of the specific key is valid or not.
	 * @param key
	 * @param code
	 * @return
	 */
	public static boolean isValidKey(int key)
	{
		boolean ret = true;

		for (int invalidKey : invalidKeys)
		{
			if (key == invalidKey)
				return false;
		}

		return ret;
	}
}