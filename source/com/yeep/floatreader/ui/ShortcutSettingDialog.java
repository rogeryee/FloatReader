package com.yeep.floatreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.yeep.basis.swing.layout.XYConstraints;
import com.yeep.basis.swing.layout.XYLayout;
import com.yeep.floatreader.cfg.KeyConfiguration;
import com.yeep.floatreader.model.Key;
import com.yeep.floatreader.model.KeyHandler;

@SuppressWarnings("serial")
public class ShortcutSettingDialog extends JDialog implements ActionListener
{
	private MainFrame parent;
	private JTextField pageDownText;
	private JTextField pageUpText;
	private JTextField trayText;
	private JButton okBtn;

	// Constructor
	public ShortcutSettingDialog(MainFrame parent)
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
		setPreferredSize(new Dimension(250, 180));

		// Create widgets
		createWidgets();

		// init the layout
		initLayout();

		// render widgets
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
		JPanel gridPanel = new JPanel(new XYLayout());

		gridPanel.add(new JLabel("Page Up : "), new XYConstraints(10, 10, 100, 20));
		gridPanel.add(this.pageUpText, new XYConstraints(120, 10, 100, 20));

		gridPanel.add(new JLabel("Page Down : "),
				new XYConstraints(10, 40, 100, 20));
		gridPanel.add(this.pageDownText, new XYConstraints(120, 40, 100, 20));

		gridPanel.add(new JLabel("Tray : "), new XYConstraints(10, 70, 100, 20));
		gridPanel.add(this.trayText, new XYConstraints(120, 70, 100, 20));
		add(gridPanel, BorderLayout.CENTER);

		// set buttons to south area
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.okBtn);
		add(buttonPanel, BorderLayout.SOUTH);

		pack();
	}

	/**
	 * Create Widgets
	 */
	private void createWidgets()
	{
		// shortcuts textfield
		this.pageUpText = createShortCutTextField();
		this.pageDownText = createShortCutTextField();
		this.trayText = createShortCutTextField();

		// Load Button
		this.okBtn = new JButton("OK");
		this.okBtn.addActionListener(this);
	}

	/**
	 * Render all widgets
	 */
	private void renderWidgets()
	{
		renderKeyText(this.pageUpText, KeyHandler.KEY_PAGE_UP);
		renderKeyText(this.pageDownText, KeyHandler.KEY_PAGE_DOWN);
		renderKeyText(this.trayText, KeyHandler.KEY_TRAY);
	}

	/**
	 * Render the text field for the specific text and key
	 * 
	 * @param text
	 * @param key
	 */
	private void renderKeyText(JTextField text, Key key)
	{
		if (text == null || key == null)
			return;
		text.setText("KEY " + KeyEvent.getKeyText(key.getKeyCode()));
	}

	/**
	 * Create text field for one text field
	 * 
	 * @return
	 */
	private JTextField createShortCutTextField()
	{
		final JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(100, 20));
		textField.setBorder(BorderFactory.createLineBorder(Color.black));
		textField.setOpaque(true);
		textField.setBackground(null);
		textField.setEditable(false);
		textField.addFocusListener(new TextFocusAdapter(textField)
		{
			/**
			 * {@inheritDoc}
			 */
			public void focusGained(FocusEvent e)
			{
				textField.setBackground(Color.GREEN);
			}

			/**
			 * {@inheritDoc}
			 */
			public void focusLost(FocusEvent e)
			{
				textField.setBackground(null);
			}
		});

		textField.addKeyListener(new KeyAdapter()
		{
			/**
			 * {@inheritDoc}
			 */
			public void keyReleased(KeyEvent e)
			{
				int keyCode = e.getKeyCode();

				if (!KeyHandler.isValidKey(keyCode))
				{
					displayErrors(new RuntimeException(
							"The Key cannot be used, please use another key"));
					return;
				}

				if (pageUpText.hasFocus())
				{
					if (KeyHandler.isDuplicatedKey(KeyHandler.KEY_PAGE_UP, keyCode))
					{
						displayErrors(new RuntimeException(
								"The Key was already used, please use another key"));
						return;
					}

					KeyHandler.KEY_PAGE_UP.setCode(keyCode);
					renderKeyText(pageUpText, KeyHandler.KEY_PAGE_UP);
				}
				else if (pageDownText.hasFocus())
				{
					if (KeyHandler.isDuplicatedKey(KeyHandler.KEY_PAGE_DOWN, keyCode))
					{
						displayErrors(new RuntimeException(
								"The Key was already used, please use another key"));
						return;
					}

					KeyHandler.KEY_PAGE_DOWN.setCode(keyCode);
					renderKeyText(pageDownText, KeyHandler.KEY_PAGE_DOWN);
				}
				else if (trayText.hasFocus())
				{
					System.out.println("keyReleased 5");
					if (KeyHandler.isDuplicatedKey(KeyHandler.KEY_TRAY, keyCode))
					{
						displayErrors(new RuntimeException(
								"The Key was already used, please use another key"));
						return;
					}

					KeyHandler.KEY_TRAY.setCode(keyCode);
					renderKeyText(trayText, KeyHandler.KEY_TRAY);
				}
			}
		});

		return textField;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.okBtn)
		{
			KeyConfiguration.update();
			this.parent.registerTrayHandler(KeyHandler.KEY_TRAY.getDefaultCode());
			closeDialog();
		}
	}

	/**
	 * Display errors
	 */
	private void displayErrors(Exception exception)
	{
		if (exception != null)
			JOptionPane.showMessageDialog(this, exception.getMessage(), "OK",
					JOptionPane.ERROR_MESSAGE);
	}

	private void closeDialog()
	{
		setVisible(false);
		dispose();
	}

	class TextFocusAdapter extends FocusAdapter
	{
		private JComponent component;

		public TextFocusAdapter(JComponent component)
		{
			this.component = component;
		}

		public JComponent getComponent()
		{
			return component;
		}
	}
}
