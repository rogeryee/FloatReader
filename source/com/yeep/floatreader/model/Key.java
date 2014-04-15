package com.yeep.floatreader.model;

public class Key
{
	private int code;
	private int defaultCode;
	private String name;

	// Constructor
	public Key(int defaultCode)
	{
		this.defaultCode = defaultCode;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public int getDefaultCode()
	{
		return defaultCode;
	}

	public void setDefaultCode(int defaultCode)
	{
		this.defaultCode = defaultCode;
	}

	/**
	 * Get the code of the specific key, if the code == 0 then returns the default code of the key
	 * @param key
	 * @param code
	 * @return
	 */
	public int getKeyCode()
	{
		return getCode() == 0 ? getDefaultCode() : getCode();
	}
}
