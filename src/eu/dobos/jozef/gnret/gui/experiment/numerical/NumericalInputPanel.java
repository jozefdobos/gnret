/**
*  Copyright (C) 2011 Jozef Dobos
*
*  This program is free software: you can redistribute it and/or modify
*  it under the terms of the GNU Affero General Public License as
*  published by the Free Software Foundation, either version 3 of the
*  License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU Affero General Public License for more details.
*
*  You should have received a copy of the GNU Affero General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package eu.dobos.jozef.gnret.gui.experiment.numerical;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;

@SuppressWarnings("serial")
public class NumericalInputPanel extends JPanel implements ActionListener {

	private Font f;
	private JButton submitButton;
	private JTextField textField = new JTextField();
	private boolean dotSet = false;
	private static final String[] DIGITS = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9" };
	private static final String DOT = ".";
	private NumericalScreenPanel caller;

	public NumericalInputPanel(NumericalScreenPanel caller) {
		super();
		this.caller = caller;
		f = Utils.getIncreasedFont(this.getFont(), Settings.FONT_SIZE_INCREASE);
		this.setBackground(Settings.BACKGROUND_COLOR);
		this.setPreferredSize(new Dimension(400, 300));
		this.setMaximumSize(new Dimension(400, 300));

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 1, 1, 1);
		c.weightx = 0.5; // spread in X direction

		int row = 0;
		
		// hidden row - hack
		
		c.gridy = row;
		c.weighty = 0;

		c.gridx = 0;
		this.add(getHiddenButton(Settings.SUBMIT), c);
		c.gridx = 1;
		this.add(getHiddenButton(Settings.SUBMIT), c);
		c.gridx = 2;
		this.add(getHiddenButton(Settings.SUBMIT), c);
		
		
		
		c.weighty = 0.5; // spread in the Y direction
		row++;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 2;
		textField.setEditable(false);
		textField.setBorder(BorderFactory.createLoweredBevelBorder() );
		textField.setHorizontalAlignment(JTextField.RIGHT);
		textField.setFont(f);
		textField.setBackground(Settings.INPUT_BACKGROUND_COLOR);
		textField.setText(DIGITS[0]);
		this.add(textField, c);
		clearTextField();

		c.gridwidth = 1;
		c.gridx = 2;
		JButton clearButton = getButton(Settings.DELETE);
		clearButton.setBackground(Settings.WRONG_COLOR);
		this.add(clearButton, c);

		// Next row
		row++;
		c.gridy = row;

		c.gridx = 0;
		this.add(getButton(DIGITS[7]), c);
		c.gridx = 1;
		this.add(getButton(DIGITS[8]), c);
		c.gridx = 2;
		this.add(getButton(DIGITS[9]), c);

		// Next row
		row++;
		c.gridy = row;

		c.gridx = 0;
		this.add(getButton(DIGITS[4]), c);
		c.gridx = 1;
		this.add(getButton(DIGITS[5]), c);
		c.gridx = 2;
		this.add(getButton(DIGITS[6]), c);

		// Next row
		row++;
		c.gridy = row;

		c.gridx = 0;
		this.add(getButton(DIGITS[1]), c);
		c.gridx = 1;
		this.add(getButton(DIGITS[2]), c);
		c.gridx = 2;
		this.add(getButton(DIGITS[3]), c);

		// Next row
		row++;
		c.gridy = row;
		c.gridx = 0;
		this.add(getButton(DIGITS[0]), c);

		c.gridx = 1;
		//this.add(getButton(DOT), c);
		
		submitButton = getButton(Settings.SUBMIT);
		submitButton.setBackground(Settings.CORRECT_COLOR);
		// submitButton.setContentAreaFilled(false);
		// submitButton.setOpaque(true);
		submitButton.requestFocusInWindow();
	//	c.gridx = 2;
		c.gridwidth = 2;
		this.add(submitButton, c);

		// set all columns to the same width as c.weightx doesn't work
		// hack
		row++;

		c.gridy = row;
		c.weighty = 0;

		c.gridx = 0;
		this.add(getHiddenButton(Settings.SUBMIT), c);
		c.gridx = 1;
		this.add(getHiddenButton(Settings.SUBMIT), c);
		c.gridx = 2;
		this.add(getHiddenButton(Settings.SUBMIT), c);
	}

	public JButton getDefaultButton() {
		return submitButton;
	}

	private JButton getButton(String label) {
		JButton b = new JButton(label);
		b.setFocusable(false);
		b.addActionListener(this);
		b.setActionCommand(label);
		b.setFont(f);
		return b;
	}

	private JButton getHiddenButton(String label) {
		JButton hidden = new JButton(label);
		hidden.setFont(f);
		hidden.setFocusable(false);
		hidden.setOpaque(true);
		hidden.setContentAreaFilled(false);
		hidden.setEnabled(false);
		hidden.setForeground(this.getBackground());
		hidden.setBorderPainted(false);		
		return hidden;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals(Settings.DELETE)) {
			clearTextField();
		} else if (action.equals(DOT)) {
			if (!dotSet) {
				textField.setText(textField.getText() + DOT);
				dotSet = true;
			}
		} else if (action.equals(Settings.SUBMIT)) {
			if (textField.getText() != null) {
				float number = Float.parseFloat(textField.getText());
				caller.checkAnswer(number);
				textField.setText(DIGITS[0]);
			}
		} else { // DIGITS
			if (textField.getText().equals(DIGITS[0])) {
				textField.setText(action);
			} else {
				textField.setText(textField.getText() + action);
			}
		}

	}

	private void clearTextField() {
		String currentText = textField.getText();
		if (currentText.length() == 1)
			textField.setText(DIGITS[0]);
		else if (!currentText.isEmpty()) // delete one by one
			textField.setText(currentText.substring(0, currentText.length() -1));
		dotSet = false;
	}

}
