package com.yeep.floatreader.hotkey;

import java.util.HashMap;
import java.util.Map;

public abstract class HotKeyManager
{
	protected Map<Integer, HotKeyHandler> keys = new HashMap<Integer, HotKeyHandler>();

	// Default Constructor
	public HotKeyManager()
	{
	}

	/**
	 * Register hot key and key handler
	 * 
	 * @param key
	 * @param handler
	 */
	abstract public void registerHotKey(Integer key, HotKeyHandler handler);

	abstract public void unregiesterHotKey(Integer key);

	/**
	 * Destory the hot key
	 */
	abstract public void destroy();
}
