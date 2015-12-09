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
import java.awt.Dimension;

import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import eu.dobos.jozef.gnret.gui.utils.Settings;

@SuppressWarnings("serial")
public class InstructionsPanel extends JPanel {

	public enum Text {
		INTRODUCTION, CONNECTION, CONCLUSION
	}

	public InstructionsPanel(Text textSelector) {
		super();
		this.setBorder(Settings.INSETS);
		this.setBackground(Settings.BACKGROUND_COLOR);
		JEditorPane editorPane = createEditorPane(textSelector);
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setPreferredSize(new Dimension(250, 145));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));
		this.setLayout(new BorderLayout());
		this.add(editorScrollPane, BorderLayout.CENTER);
	}

	private JEditorPane createEditorPane(Text textSelector) {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);

		String file = "default.html";
		switch (textSelector) {
		case INTRODUCTION:
			file = "introduction.html";
			break;
		case CONNECTION :
			file = "connection.html";
			break;
		case CONCLUSION:
			file = "conclusion.html";
			break;
		default:
			break;
		}
		java.net.URL helpURL = this.getClass().getClassLoader().getResource(
				file);
		if (helpURL != null) {
			try {
				editorPane.setPage(helpURL);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + helpURL);
			}
		} else {
			System.err.println("Couldn't find file: " + file);
		}
		return editorPane;
	}
}
