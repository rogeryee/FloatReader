package com.yeep.floatreader.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.yeep.basis.swing.widget.font.FontWidget;

@SuppressWarnings("serial")
public class ChooseFontDialog extends JDialog implements ActionListener
{
	private MainFrame parent;

	private FontWidget fontWidget;
	private JButton okBtn;
	private JButton cancelBtn;

	// Constructor
	public ChooseFontDialog(MainFrame parent)
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
		setPreferredSize(new Dimension(425, 330));

		// Create widgets
		createWidgets();

		// init the layout
		initLayout();
	}

	/**
	 * initialize Layout
	 */
	private void initLayout()
	{
		// Make Layout
		setLayout(new BorderLayout());

		// set scrollPane to center area
		add(this.fontWidget, BorderLayout.CENTER);

		// set buttons to south area
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.okBtn);
		buttonPanel.add(this.cancelBtn);
		add(buttonPanel, BorderLayout.SOUTH);

		pack();
	}

	/**
	 * Create Widgets
	 */
	private void createWidgets()
	{
		this.fontWidget = new FontWidget(parent.getFontOfReader());

		// Load Button
		this.okBtn = new JButton("OK");
		this.okBtn.addActionListener(this);

		// Cancel Button
		this.cancelBtn = new JButton("Cancel");
		this.cancelBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.okBtn)
		{
			parent.changeReaderFont(this.fontWidget.getSelectedFont());
			closeDialog();
		}

		if (e.getSource() == this.cancelBtn)
		{
			closeDialog();
		}
	}

	private void closeDialog()
	{
		setVisible(false);
		dispose();
	}
}
