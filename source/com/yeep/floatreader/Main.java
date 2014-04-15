package com.yeep.floatreader;

import javax.swing.SwingUtilities;

import com.yeep.floatreader.cfg.Configuration;
import com.yeep.floatreader.ui.MainFrame;

/**
 * Main Class
 * 
 * @author Yiro
 */
public class Main
{
	/**
	 * Main method
	 */
	public static void main(String[] args)
	{
		try
		{
			// UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					Configuration.getInstance().initialize();
					loadUI();
				}
			});
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Load UI
	 */
	private static void loadUI()
	{
		new MainFrame();
	}
}
