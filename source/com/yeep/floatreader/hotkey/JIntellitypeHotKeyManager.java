package com.yeep.floatreader.hotkey;

import java.util.Set;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;

public class JIntellitypeHotKeyManager extends HotKeyManager implements
		HotkeyListener, IntellitypeListener
{
	/**
	 * Constructor
	 */
	public JIntellitypeHotKeyManager()
	{
		if (!JIntellitype.isJIntellitypeSupported())
		{
			System.out.println("JIntellitype not supported");
		}

		JIntellitype.getInstance();
		JIntellitype.getInstance().addHotKeyListener(this);
		JIntellitype.getInstance().addIntellitypeListener(this);
	}

	@Override
	public void registerHotKey(Integer key, HotKeyHandler handler)
	{
		keys.put(key, handler);
		JIntellitype.getInstance().unregisterHotKey(key);
		JIntellitype.getInstance().registerHotKey(key, 0, key);
	}
	
	public void unregiesterHotKey(Integer key)
	{
		if (keys == null || keys.isEmpty())
			return;
		
		this.keys.remove(key);
		JIntellitype.getInstance().unregisterHotKey(key);
	}

	public void destroy()
	{
		if (keys == null || keys.isEmpty())
			return;

		Set<Integer> set = keys.keySet();
		for (Integer key : set)
		{
			JIntellitype.getInstance().unregisterHotKey(key);
		}
	}

	public void onIntellitype(int arg0)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void onHotKey(int key)
	{
		HotKeyHandler handler = keys.get(key);
		if (handler != null)
		{
			System.out.println("On Tray Event");
			handler.handle();
		}

	}
}
