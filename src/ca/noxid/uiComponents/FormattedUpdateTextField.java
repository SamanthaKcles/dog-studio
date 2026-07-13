package ca.noxid.uiComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;


public class FormattedUpdateTextField extends JFormattedTextField
		implements KeyListener {
	private static final long serialVersionUID = 5123472341660584854L;
	boolean updated = false;

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

	public FormattedUpdateTextField(NumberFormat f) {
		super(f);
		this.addKeyListener(this);
		//this.setInputVerifier(verify);
	}

	public static NumberFormat getNumberOnlyFormat(int minDigit, int maxDigit) {
		NumberFormat retVal = NumberFormat.getIntegerInstance();
		retVal.setMaximumIntegerDigits(maxDigit);
		retVal.setMinimumIntegerDigits(minDigit);
		retVal.setGroupingUsed(false);
		return retVal;
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
		if (k == '\n') {
			this.setBackground(getDefaultBg());
			updated = false;
			this.fireActionPerformed();
		} else if (k >= ' ' || k == 8 || k == 22) { //character or backspace or paste
			this.setBackground(getUpdatedBg());
			updated = true;
		}
	}

	@Override
	public void processFocusEvent(FocusEvent eve) {
		super.processFocusEvent(eve);
		if (updated) {
			this.setBackground(getDefaultBg());
			updated = false;
			this.fireActionPerformed();
		}
	}
}
