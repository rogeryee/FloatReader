package com.yeep.floatreader.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.yeep.basis.swing.widget.table.TableWidget;
import com.yeep.basis.swing.widget.table.TableWidgetModel;
import com.yeep.floatreader.model.RecentFile;
import com.yeep.floatreader.service.FloatReaderService;
import com.yeep.floatreader.service.FloatReaderServiceImpl;

@SuppressWarnings("serial")
public class RecentFileDialog extends JDialog implements ActionListener
{
	private FloatReaderService floatReaderService = new FloatReaderServiceImpl();
	private MainFrame parent;

	private List<RecentFile> recentFiles;
	private JScrollPane scrollPane;
	private TableWidget<RecentFile> recentFileTableWidget;
	private JButton loadBtn;
	private JButton cleanBtn;
	private JButton cancelBtn;

	// Constructor
	public RecentFileDialog(MainFrame parent)
	{
		super(parent, true);
		this.parent = parent;
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
		buttonPanel.add(this.cleanBtn);
		buttonPanel.add(this.cancelBtn);
		add(buttonPanel, BorderLayout.SOUTH);

		pack();
	}

	/**
	 * Create Widgets
	 */
	private void createWidgets()
	{
		this.recentFileTableWidget = new TableWidget<RecentFile>();
		this.scrollPane = new JScrollPane(this.recentFileTableWidget);
		this.recentFileTableWidget.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					try
					{
						RecentFile recentFile = RecentFileDialog.this.recentFileTableWidget.getSelected();
						RecentFileDialog.this.parent.reloadByRecentFile(recentFile);
						closeDialog();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});

		// Load Button
		this.loadBtn = new JButton("Load");
		this.loadBtn.addActionListener(this);

		// Clean Button
		this.cleanBtn = new JButton("Clean");
		this.cleanBtn.addActionListener(this);

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
			this.recentFiles = floatReaderService.loadRecentFiles();
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
			displayErrors(new RuntimeException("Unknown error occurs!"));
		}

		if (this.recentFiles == null)
		{
			this.recentFiles = new ArrayList<RecentFile>();
		}

		TableWidgetModel<RecentFile> listModel = new TableWidgetModel<RecentFile>(this.recentFiles,
				new String[] { "File" }, new String[] { "filename" });
		this.recentFileTableWidget.setListModel(listModel);

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.loadBtn)
		{
			try
			{
				RecentFile recentFile = this.recentFileTableWidget.getSelected();
				this.parent.reloadByRecentFile(recentFile);
				closeDialog();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}

		if (e.getSource() == this.cleanBtn)
		{
			try
			{
				floatReaderService.deleteRecentFiles();
			}
			catch (IOException exception)
			{
				exception.printStackTrace();
				displayErrors(new RuntimeException("Unknown error occurs!"));
			}
			closeDialog();
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
