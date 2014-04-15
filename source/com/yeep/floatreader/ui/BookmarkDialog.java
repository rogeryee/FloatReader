package com.yeep.floatreader.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.yeep.basis.swing.widget.table.TableWidget;
import com.yeep.basis.swing.widget.table.TableWidgetModel;
import com.yeep.floatreader.model.Bookmark;
import com.yeep.floatreader.model.BookmarkList;
import com.yeep.floatreader.model.Line;
import com.yeep.floatreader.model.PageController;
import com.yeep.floatreader.service.FloatReaderService;
import com.yeep.floatreader.service.FloatReaderServiceImpl;
import com.yeep.floatreader.service.HTMLConverter;

@SuppressWarnings("serial")
public class BookmarkDialog extends JDialog implements ActionListener
{
	private FloatReaderService floatReaderService = new FloatReaderServiceImpl();
	private MainFrame parent;

	private PageController page;
	private BookmarkList bookmarkList;

	private JScrollPane scrollPane;
	private TableWidget<Bookmark> bookmarkTableWidget;
	private JButton loadBtn;
	private JButton saveBtn;
	private JButton deleteBtn;
	private JButton cancelBtn;

	// Constructor
	public BookmarkDialog(MainFrame parent)
	{
		super(parent, true);
		this.parent = parent;
		this.page = parent.pageController;
		loadUI();
		setVisible(true);
	}

	/**
	 * Load Layout
	 */
	private void loadUI()
	{
		// set attributes for dialog
		setLocationRelativeTo(this.parent);
		setPreferredSize(new Dimension(500, 200));

		// Create widgets
		createWidgets();

		// init the layout
		initLayout();

		// Render widget
		renderWidgets();
	}

	/**
	 * initialize Layout
	 */
	private void initLayout()
	{
		// Make Layout
		setLayout(new BorderLayout());

		// set scrollPane to center area
		add(this.scrollPane, BorderLayout.CENTER);

		// set buttons to south area
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.loadBtn);
		buttonPanel.add(this.saveBtn);
		buttonPanel.add(this.deleteBtn);
		buttonPanel.add(this.cancelBtn);
		add(buttonPanel, BorderLayout.SOUTH);

		pack();
	}

	/**
	 * Create Widgets
	 */
	private void createWidgets()
	{
		this.bookmarkTableWidget = new TableWidget<Bookmark>();
		this.scrollPane = new JScrollPane(this.bookmarkTableWidget);
		this.bookmarkTableWidget.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					Bookmark bookmark = BookmarkDialog.this.bookmarkTableWidget.getSelected();
					BookmarkDialog.this.parent.reloadByBookmark(bookmark);
					closeDialog();
				}
			}
		});

		// Load Button
		this.loadBtn = new JButton("Load");
		this.loadBtn.addActionListener(this);

		// Save Button
		this.saveBtn = new JButton("Save");
		this.saveBtn.addActionListener(this);

		// Delete Button
		this.deleteBtn = new JButton("Delete");
		this.deleteBtn.addActionListener(this);

		// Cancel Button
		this.cancelBtn = new JButton("Cancel");
		this.cancelBtn.addActionListener(this);
	}

	/**
	 * Render widgets
	 */
	private void renderWidgets()
	{
		try
		{
			this.bookmarkList = floatReaderService.loadBookmarkList(this.page.getFileName());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			displayErrors(new RuntimeException("Unknown error occurs!"));
		}

		if (this.bookmarkList == null)
		{
			this.bookmarkList = new BookmarkList();
			this.bookmarkList.setFilename(this.page.getFileName());
		}

		TableWidgetModel<Bookmark> listModel = new TableWidgetModel<Bookmark>(this.bookmarkList.getBookmarks(),
				new String[] { "Description" }, new String[] { "description" });
		this.bookmarkTableWidget.setListModel(listModel);

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.loadBtn)
		{
			Bookmark bookmark = this.bookmarkTableWidget.getSelected();
			this.parent.reloadByBookmark(bookmark);
			closeDialog();
		}

		if (e.getSource() == this.saveBtn)
		{
			Line line = this.page.getCurrentLine();
			String description = "";
			if (line != null)
				description = HTMLConverter.filterHtml(line.getContent());

			Bookmark bookmark = new Bookmark(line.getBeginIndex(), description);
			this.bookmarkTableWidget.addRow(bookmark);

			try
			{
				floatReaderService.updateBookmarkList(this.bookmarkList);
			}
			catch (IOException exception)
			{
				exception.printStackTrace();
				displayErrors(new RuntimeException("Unknown error occurs!"));
			}
		}

		if (e.getSource() == this.deleteBtn)
		{
			Bookmark bookmark = this.bookmarkTableWidget.getSelected();
			this.bookmarkTableWidget.removeRow(bookmark);

			try
			{
				floatReaderService.updateBookmarkList(this.bookmarkList);
			}
			catch (IOException exception)
			{
				exception.printStackTrace();
				displayErrors(new RuntimeException("Unknown error occurs!"));
			}
		}

		if (e.getSource() == this.cancelBtn)
		{
			closeDialog();
		}
	}

	/**
	 * Display errors
	 */
	private void displayErrors(Exception exception)
	{
		if (exception != null)
			JOptionPane.showMessageDialog(this, exception.getMessage(), "OK", JOptionPane.ERROR_MESSAGE);
	}

	private void closeDialog()
	{
		setVisible(false);
		dispose();
	}
}
