package ca.noxid.uiComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class UpdateTextField extends JTextField implements KeyListener, ActionListener {
	private static final long serialVersionUID = 5123472341660584854L;
	boolean updated = false;
	private boolean focusSwitchAction = true;

	private static Color getDefaultBg() {
		Color c = UIManager.getColor("TextField.background");
		return c != null ? c : new Color(69, 73, 74);
	}

	private static Color getUpdatedBg() {
		Color accent = UIManager.getColor("TextField.selectionBackground");
		if (accent == null) accent = new Color(75, 110, 140);
		Color field = UIManager.getColor("TextField.background");
		if (field == null) field = new Color(69, 73, 74);
		return new Color(
			(accent.getRed()   + field.getRed()   * 2) / 3,
			(accent.getGreen() + field.getGreen() * 2) / 3,
			(accent.getBlue()  + field.getBlue()  * 2) / 3
		);
	}

	public UpdateTextField() {
		super();
		this.addKeyListener(this);
		this.addActionListener(this);
	}

	public UpdateTextField(String s) {
		super(s);
		this.addKeyListener(this);
		this.addActionListener(this);
	}

	public UpdateTextField(int i) {
		super(i);
		this.addKeyListener(this);
		this.addActionListener(this);
	}

	public void setFireActionOnFocusSwitch(boolean tf) {
		focusSwitchAction = tf;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		//ignore
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		//ignore
	}

	@Override
	public void keyTyped(KeyEvent eve) {
		char k = eve.getKeyChar();
		if (k >= ' ' || k == 8 || k == 22) { //character or backspace or paste
			this.setBackground(getUpdatedBg());
			updated = true;
		}
	}

	@Override
	public void actionPerformed(ActionEvent eve) {
		refresh();
	}

	@Override
	public void processFocusEvent(FocusEvent eve) {
		super.processFocusEvent(eve);
		if (updated && focusSwitchAction) {
			this.fireActionPerformed();
		}
	}

	/**
	 * This sets updated to false and clears the bg
	 * as an override maybe to work around sequencing
	 */
	public void refresh() {
		this.setBackground(getDefaultBg());
		updated = false;
	}

	public boolean isCommited() {
		//if the bg color isn't the special color it has been committed
		return !updated;
	}
}
