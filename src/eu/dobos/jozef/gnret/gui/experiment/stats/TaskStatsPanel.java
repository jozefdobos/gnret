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

package eu.dobos.jozef.gnret.gui.experiment.stats;


import java.awt.Component;
import java.awt.Dimension;

import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;

@SuppressWarnings("serial")
public class TaskStatsPanel extends JPanel {

	private Font f;
	private JPanel p;
	private JPanel wrapper;
	private boolean groupFeedbackOn;
	String[] labels = { "Total Points Score for this Round:", "Rank within group:" };
	
	private int round;
	private boolean setAlready = false;
	
	public TaskStatsPanel(TaskStatsLogger taskStatsLogger, int round,
			boolean groupFeedbackOn) {		
		this.round = round;
		this.groupFeedbackOn = groupFeedbackOn;
		this.setBackground(Settings.BACKGROUND_COLOR);
		
		f = Utils.getIncreasedFont(this.getFont(), Settings.FONT_SIZE_INCREASE);
		
		int correctAnswersForPastRound = taskStatsLogger
				.getCurrentCorrectAnswers()
				+ taskStatsLogger.getPreviousCorrectAnswers();			
		setPage(labels[0], correctAnswersForPastRound+"");
	}
	
	private void setPage(String label, String text) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(Box.createVerticalGlue());
		p = getPanel(label,text);
		wrapper = new JPanel();
		wrapper.setBackground(Settings.BACKGROUND_COLOR);
		wrapper.add(p);
		wrapper.setBorder(Utils.getTitledBorder(this,
				"Results for " + Utils.getCurrentRoundDescriptor(round)));
		wrapper.setMinimumSize(new Dimension(100, 100));
		wrapper.setMaximumSize(new Dimension(900, 200));
		this.add(wrapper);

		wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(Box.createVerticalGlue());
		this.invalidate();
		this.repaint();
	}
	
	private JPanel getPanel(String label, String text) {
		
		JPanel p = new JPanel();		
		JLabel l = new JLabel(label, JLabel.TRAILING);
		l.setFont(f);
		p.add(l);
		JTextField textField = new JTextField(12);
		textField.setText(text);
		textField.setEditable(false);
		textField.setFont(f);
		l.setLabelFor(textField);
		p.add(textField);
		p.setBackground(Settings.BACKGROUND_COLOR);
		
		/*
		int numPairs = 1;
		JPanel p = new JPanel(new SpringLayout());
		for (int i = 0; i < numPairs; i++) {
			JLabel l = new JLabel(label, JLabel.TRAILING);
			l.setFont(f);
			p.add(l);
			JTextField textField = new JTextField(4);
			textField.setText(text);
			textField.setEditable(false);
			textField.setFont(f);
			l.setLabelFor(textField);
			p.add(textField);
		}
		// Lay out the panel.
		SpringUtilities.makeCompactGrid(p, numPairs, 2, // rows, cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad
		*/
		return p;
	}
	
	public void clearScore() {
		if (!setAlready) {
			setAlready = true;
			wrapper.setVisible(false);
			wrapper.invalidate();
			wrapper.repaint();
		}
	}
	
	public void setRank(String rank) {	
		if (groupFeedbackOn) {	
			JLabel l = (JLabel) p.getComponent(0);
			l.setText(labels[1]);
			JTextField t = (JTextField) p.getComponent(1);
			t.setText(rank);
			
			p.invalidate();
			p.repaint();
			wrapper.setVisible(true);
			wrapper.invalidate();
			wrapper.repaint();
			
			setAlready = true;
		}
	}
}
