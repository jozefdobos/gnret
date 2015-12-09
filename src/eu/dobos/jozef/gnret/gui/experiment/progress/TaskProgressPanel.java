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

package eu.dobos.jozef.gnret.gui.experiment.progress;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import eu.dobos.jozef.gnret.gui.AbstractGNRETFrame;
import eu.dobos.jozef.gnret.gui.experiment.stats.TaskCallbackInterface;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;

// borders http://java.sun.com/products/jfc/tsc/articles/borders/index.html

@SuppressWarnings("serial")
public class TaskProgressPanel extends JPanel implements TaskCallbackInterface {
	private JProgressBar progressBar;
	private JLabel correctCountLabel = new JLabel();
	private int correctCount = 0;
	private boolean isWaiting = false;
	String additionalText = new String();

	public TaskProgressPanel(int maxTime, String formattedTime,
			boolean statsOn, int round, boolean isWaiting, int screenCounter) {
		super();
		assert (round >= 0); // non-negative

		this.isWaiting = isWaiting;
		this.setBorder(Settings.INSETS);
		this.setLayout(new BorderLayout());
		this.setBackground(Settings.BACKGROUND_COLOR);

		progressBar = new JProgressBar(0, maxTime);
		
		if (screenCounter == AbstractGNRETFrame.PRACTICE_ROUND_GRID_SCREEN ||
			screenCounter == AbstractGNRETFrame.PAYING_ROUND_ONE_GRID_SCREEN ||
			screenCounter == AbstractGNRETFrame.PAYING_ROUND_TWO_GRID_SCREEN ||
			screenCounter == AbstractGNRETFrame.PAYING_ROUND_THREE_GRID_SCREEN ||
			screenCounter == AbstractGNRETFrame.PAYING_ROUND_FOUR_GRID_SCREEN ||
			screenCounter == AbstractGNRETFrame.PAYING_ROUND_FIVE_GRID_SCREEN ||
			screenCounter == AbstractGNRETFrame.PAYING_ROUND_SIX_GRID_SCREEN) {
			additionalText = " (Verbal Task): ";
		}
		else if (screenCounter == AbstractGNRETFrame.PRACTICE_ROUND_NUMERICAL_SCREEN ||
				screenCounter == AbstractGNRETFrame.PAYING_ROUND_ONE_NUMERICAL_SCREEN ||
				screenCounter == AbstractGNRETFrame.PAYING_ROUND_TWO_NUMERICAL_SCREEN ||
				screenCounter == AbstractGNRETFrame.PAYING_ROUND_THREE_NUMERICAL_SCREEN ||
				screenCounter == AbstractGNRETFrame.PAYING_ROUND_FOUR_NUMERICAL_SCREEN ||
				screenCounter == AbstractGNRETFrame.PAYING_ROUND_FIVE_NUMERICAL_SCREEN ||
				screenCounter == AbstractGNRETFrame.PAYING_ROUND_SIX_NUMERICAL_SCREEN)
		{
			additionalText = " (Numerical Task): ";
		}

		
		if (isWaiting) {
			this.setWaiting();
		} else {
			progressBar.setString(Settings.REMAINING_TIME_TEXT + " "
					+ formattedTime);
		}
		progressBar.setValue(maxTime);
		progressBar.setStringPainted(true);

		Font f = Utils.getIncreasedFont(this.getFont(),
				Settings.FONT_SIZE_INCREASE);
		progressBar.setFont(f);

		this.add(progressBar, BorderLayout.CENTER);
		if (statsOn) {
			correctCountLabel.setText(Settings.CORRECT_COUNT_TEXT + additionalText
					+ correctCount);
			String roundText = Utils.getCurrentRoundDescriptor(round);

			JPanel countsPanel = new JPanel();
			countsPanel.setBackground(Settings.BACKGROUND_COLOR);
			countsPanel.setLayout(new BoxLayout(countsPanel,
					BoxLayout.PAGE_AXIS));

			// roundInfo.setFont(f);
			correctCountLabel.setFont(f);

			// countsPanel.add(roundInfo);
			correctCountLabel.setBorder(Settings.INSETS_ONE_DOWN);
			correctCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			countsPanel.add(correctCountLabel);
			JPanel wrapper = new JPanel();
			wrapper.add(countsPanel);
			countsPanel.setBorder(Utils.getTitledBorder(this, roundText));
			wrapper.setBackground(Settings.BACKGROUND_COLOR);
			this.add(wrapper, BorderLayout.SOUTH);
		}
	}

	public void setTime(int time, String formattedTime) {
		if (!isWaiting) {
			this.progressBar.setValue(time);
			this.progressBar.setString(Settings.REMAINING_TIME_TEXT + " "
					+ formattedTime);
			this.progressBar.repaint();
		}
	}
	
	public void setWaiting() {
		progressBar.setString("Automatic waiting...");
		progressBar.setIndeterminate(true);
		isWaiting = true;
	}
	
	public void setText(String text) {
		this.progressBar.setString(text);
	}

	@Override
	public void incrementCorrectAnswers() {
		correctCount++;
		correctCountLabel.setText(Settings.CORRECT_COUNT_TEXT +  additionalText
				+ correctCount);
	}

	@Override
	public void incrementWrongAnswers() {
		// Does not display wrong answers count
	}

}
