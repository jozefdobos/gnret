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

package eu.dobos.jozef.gnret.gui.console;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import eu.dobos.jozef.gnret.filehandling.FileWritingHandler;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;

public class Console extends JPanel {

	private static final long serialVersionUID = 580570221590741741L;
	private JTextArea console = new JTextArea();
	public static final int MAX_NUMBER_OF_LINES = 1000;

	public Console() {
		super();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(console);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		console.setRows(5);
		console.setEditable(false);
		console.setWrapStyleWord(true);
		console.setLineWrap(true);
		
		try {
			FileWritingHandler.createInitialFile(Settings.LOG_FILE, "GNRET Log started on " + Utils.getCurrentDate() + " at " + Utils.getCurrentTime() + "\n");
			append("Log located at: " + FileWritingHandler.getAbsolutePathToLocalFile(Settings.LOG_FILE));
		} catch (IOException e) {
			console.append("Could not create local log file: " + e.getMessage());
		}
		

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Console"));
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public synchronized void append(String text) {
		if (text != null && !text.isEmpty()) {
			if (console.getText().split(
							System.getProperty("line.separator")).length < MAX_NUMBER_OF_LINES) {
				String content = (Utils.getCurrentTime() + " > " + text + "\n");
				console.append(content);
				try {
					FileWritingHandler.appendToFile(Settings.LOG_FILE, content, true);
				} catch (IOException e) {
					// do nothing
				}
			} else {
				console.setText(text);
			}
			console.setCaretPosition(console.getText().length());
		}
	}

	public void setText(String text) {
		clear();
		console.append(text);
	}

	public void clear() {
		console.setText("");
	}

}
