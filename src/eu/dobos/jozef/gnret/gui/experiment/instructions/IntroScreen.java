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

package eu.dobos.jozef.gnret.gui.experiment.instructions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import eu.dobos.jozef.gnret.gui.experiment.ExperimentView;
import eu.dobos.jozef.gnret.gui.experiment.instructions.InstructionsPanel.Text;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;

public class IntroScreen extends JPanel implements ActionListener {

	private static final long serialVersionUID = 7651038759493758329L;
	private static final String START = "Start";
	private JButton startButton = new JButton(START);
	private Font f;
	private ExperimentView caller;

	public IntroScreen(ExperimentView caller) {
		super();
		this.caller = caller;
		this.setBackground(Settings.BACKGROUND_COLOR);
		f = Utils.getIncreasedFont(this.getFont(), Settings.FONT_SIZE_INCREASE);
		setLayout(new BorderLayout());		
		this.add(new InstructionsPanel(Text.INTRODUCTION), BorderLayout.NORTH);
		this.add(getStartButtonPanel(), BorderLayout.CENTER);
		
	}
	
	private JPanel getStartButtonPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Settings.BACKGROUND_COLOR);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		panel.add(Box.createVerticalGlue());
		panel.add(startButton);
		startButton.addActionListener(this);
		startButton.setFont(f);
		startButton.setBackground(Settings.CORRECT_COLOR);
		startButton.setActionCommand(START);
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(Box.createVerticalGlue());
		
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(START)) {
			caller.uploadStartYes();
		}		
	}
}
