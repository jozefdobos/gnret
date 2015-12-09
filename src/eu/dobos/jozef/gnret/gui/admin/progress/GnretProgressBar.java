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

package eu.dobos.jozef.gnret.gui.admin.progress;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;

public class GnretProgressBar extends JPanel {
	private static final long serialVersionUID = -8496489851003555928L;

	public static final String WAITING = "Waiting for the experiment to start";
	public static final String QUESTIONNAIRE = "Questionnaire";
	private boolean isWaiting = false;
	private JProgressBar progressBar;
	private String text = new String();

	public GnretProgressBar() {
		super();
		this.setBorder(Settings.INSETS);
		this.setLayout(new BorderLayout());
		progressBar = new JProgressBar(0, 1);
		setWaitingProgressBar();
		progressBar.setStringPainted(true);
		Font f = Utils.getIncreasedFont(this.getFont(),
				Settings.FONT_SIZE_INCREASE);
		progressBar.setFont(f);
		this.add(progressBar, BorderLayout.CENTER);
	}

	public void setTime(int time, String formattedTime) {
		if (!isWaiting) {
			this.progressBar.setValue(time);
			this.progressBar.setString(text + ": " + formattedTime);
			this.progressBar.repaint();
		}
	}

	public void setWaitingProgressBar() {
		progressBar.setString(WAITING);
		progressBar.setMaximum(1);
		progressBar.setValue(1);
		this.progressBar.repaint();
	}

	public void setWaiting(boolean on, int round) {
		String t = "";
		if (round != -1) {
			if (round == 0) {
				t = Settings.PRACTICE_ROUND;
			} else {
				t = Settings.PAYING_ROUND + " " + round;
			}
			progressBar.setString("Waiting for the '" + t
					+ "' button to be pressed!");
		}
		progressBar.setIndeterminate(on);
		isWaiting = on;
	}

	public void setText(String text) {
		progressBar.setString(text);
		
		if (text.equals(QUESTIONNAIRE)) {
			progressBar.setMaximum(1);
			progressBar.setValue(1);
		}
	}

	public void setNewProgressBar(int screenCounter, int time, String text,
			String formattedTime) {
		this.text = text;
		progressBar.setMaximum(time);
		progressBar.setIndeterminate(false);
		isWaiting = false;
		setTime(time, formattedTime);
	}

}
