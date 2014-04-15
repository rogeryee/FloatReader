package com.yeep.floatreader.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import com.yeep.floatreader.Constants;
import com.yeep.floatreader.model.RecentFile;
import com.yeep.floatreader.service.FloatReaderService;
import com.yeep.floatreader.service.FloatReaderServiceImpl;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements ActionListener
{
	private FloatReaderService floatReaderService = new FloatReaderServiceImpl();
	private MainFrame parent;

	// Menus
	private JMenu fileMenu;
	private JMenuItem openFileMI;
	private JMenuItem shortcutMI;
	private JMenuItem openRecentFilesMI;
	private JMenuItem exitMI;
	private JMenu viewMenu;
	private JMenuItem bookmarkMI;
	private JMenu formatMenu;
	private JMenu encodeMI;
	private JMenuItem encodeGBKMI;
	private JMenuItem encodeUTF8MI;
	private JMenuItem fontMI;

	// Constructor
	public MenuBar(MainFrame parent)
	{
		super();
		this.parent = parent;
		loadUI();
	}

	// methods
	public void loadUI()
	{
		// initialize FileMenus
		initFileMenu();

		// initialize EditMenus
		initViewMenu();

		// initialize FormatMenu
		initFormatMenu();
	}

	/**
	 * initialize FileMenus
	 */
	private void initFileMenu()
	{
		// File menus
		this.fileMenu = new JMenu("File");

		this.openFileMI = createMenuItem("Open File...");
		this.fileMenu.add(this.openFileMI);

		this.shortcutMI = createMenuItem("Shortcut Setting");
		this.fileMenu.add(this.shortcutMI);

		add(this.fileMenu);

		initRecentFileMenu();
	}

	/**
	 * initialize ViewMenus
	 */
	private void initViewMenu()
	{
		// ViewMenus
		this.viewMenu = new JMenu("View");

		this.bookmarkMI = createMenuItem("Bookmark");
		this.viewMenu.add(this.bookmarkMI);

		this.add(this.viewMenu);
	}

	/**
	 * initialize FormatMenu
	 */
	private void initFormatMenu()
	{
		// FontMenu
		this.formatMenu = new JMenu("Format");

		this.fontMI = createMenuItem("Font...");
		this.formatMenu.add(this.fontMI);

		this.encodeMI = new JMenu("Encode");
		this.formatMenu.add(this.encodeMI);

		ButtonGroup encodeButtonGroup = new ButtonGroup();
		this.encodeUTF8MI = new JRadioButtonMenuItem("UTF-8");
		this.encodeUTF8MI.addActionListener(this);
		encodeButtonGroup.add(this.encodeUTF8MI);
		this.encodeMI.add(this.encodeUTF8MI);

		this.encodeGBKMI = new JRadioButtonMenuItem("GBK");
		this.encodeGBKMI.addActionListener(this);
		encodeButtonGroup.add(this.encodeGBKMI);
		this.encodeGBKMI.setSelected(true);
		parent.setCharset(Constants.CHARSET_GBK);
		this.encodeMI.add(this.encodeGBKMI);

		this.add(this.formatMenu);
	}

	/**
	 * Initial recent file menus
	 */
	private void initRecentFileMenu()
	{
		try
		{
			List<RecentFile> recentFiles = floatReaderService.loadRecentFiles();
			if (recentFiles != null && !recentFiles.isEmpty())
			{
				int index = 2;
				this.fileMenu.insertSeparator(index++);

				ListIterator<RecentFile> iterator = recentFiles.listIterator(recentFiles.size());
				while (iterator.hasPrevious())
				{
					final RecentFile recentFile = iterator.previous();
					JMenuItem menuItem = new JMenuItem(recentFile.getFilename());
					menuItem.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							try
							{
								MenuBar.this.parent.reloadByRecentFile(recentFile);
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}
						}
					});
					this.fileMenu.add(menuItem);
					this.fileMenu.insert(menuItem, index++);
				}
			}
			initExitMenu();
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}

	private void initExitMenu()
	{
		this.fileMenu.add(new JPopupMenu.Separator());
		this.exitMI = createMenuItem("Exit");
		this.fileMenu.add(this.exitMI);
	}

	/**
	 * Reload recent file menu
	 */
	protected void reloadRecentFileMenu()
	{
		if (this.fileMenu == null)
			return;

		while (this.fileMenu.getPopupMenu().getComponentCount() > 1)
		{
			this.fileMenu.remove(this.fileMenu.getPopupMenu().getComponentCount() - 1);
		}
		initRecentFileMenu();
	}

	/**
	 * Create menuItem for specific menuName and actionCommand
	 * 
	 * @param menuName
	 * @param actionCommand
	 * @return
	 */
	private JMenuItem createMenuItem(String menuName)
	{
		JMenuItem menuItem = new JMenuItem(menuName);
		menuItem.addActionListener(this);

		return menuItem;
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event == null)
			return;

		if (this.openFileMI == event.getSource())
		{
			this.parent.openFile();
		}
		else if (this.shortcutMI == event.getSource())
		{
			this.parent.openShortcutSettingDialog();
		}
		else if (this.exitMI == event.getSource())
		{
			parent.exit();
		}
		else if (this.bookmarkMI == event.getSource())
		{
			this.parent.openBookmarkDialog();
		}
		else if (this.openRecentFilesMI == event.getSource())
		{
			this.parent.openRecentFilesDialog();
		}
		else if (this.encodeGBKMI == event.getSource())
		{
			this.parent.applyCharset(Constants.CHARSET_GBK);
		}
		else if (this.encodeUTF8MI == event.getSource())
		{
			this.parent.applyCharset(Constants.CHARSET_UTF8);
		}
		else if (this.fontMI == event.getSource())
		{
			this.parent.openChooseFontDialog();
		}
	}
}
