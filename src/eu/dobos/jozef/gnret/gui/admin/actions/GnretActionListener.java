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

package eu.dobos.jozef.gnret.gui.admin.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import eu.dobos.jozef.gnret.GenderNeutralRealEffortTask;
import eu.dobos.jozef.gnret.communication.JakartaFtpWrapper;
import eu.dobos.jozef.gnret.gui.admin.AdminView;
import eu.dobos.jozef.gnret.gui.utils.Settings;

public class GnretActionListener implements ActionListener {

	private AdminView frame;
	private JakartaFtpWrapper ftp;
	// IMAGE ICONS
	public static final String SMALL_END = "16";
	public static final String BIG_END = "24";
	public static final String NEW_IMAGE = "Add";
	public static final String OPEN_IMAGE = "Open";
	public static final String EXIT_IMAGE = "Stop";
	public static final String ABOUT_IMAGE = "About";
	public static final String REFRESH_IMAGE = "Refresh";
	public static final String REMOVE_IMAGE = "Remove";
	public static final String ZOOM_IN_IMAGE = "ZoomIn";
	public static final String ZOOM_OUT_IMAGE = "ZoomOut";
	public static final String DELETE_IMAGE = "Delete";
	public static final String VALID_FILE_IMAGE = "Properties";
	public static final String START_IMAGE = "Play";
	public static final String PAUSE_IMAGE = "Pause";
	public static final String RECONNECT_IMAGE = "Redo";
	public static final String RANK_IMAGE = "History";
	public static final String PRINT_IMAGE = "Print";
	public static final String SAVE_IMAGE = "Save";
	public static final String ENABLE_ROUND_IMAGE = "Import";
	public static final String DISABLE_ROUND_IMAGE = "Export";
	public static final String SELECT_ALL_IMAGE = "Preferences";

	public static final String FILE = "File";
	public static final String SERVER = "Server";
	public static final String HELP = "Help";
	public static final String ABOUT = "About "
			+ GenderNeutralRealEffortTask.ACRONYM;
	public static final String EXIT = "Exit";
	public static final String REFRESH = "Refresh Files";
	public static final String RECONNECT = "Reconnect";
	public static final String RANK = "Show Rank";
	public static final String PRINT = "Print Table...";
	public static final String CLEAR_SESSION = "Clear Session";
	public static final String REMOVED_DIR = "Removed Dir: ";
	public static final String SAVE = "Save Selected Files Into...";

	public static final String DELETE_SELECTED = "Delete Selected Files";
	public static final String SELECT_ALL = "Select All Files";

	// Create a file chooser
	private final JFileChooser fc = new JFileChooser();

	public GnretActionListener(AdminView frame, JakartaFtpWrapper ftp) {
		this.frame = frame;
		this.ftp = ftp;
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		String command = e.getActionCommand();

		if (command.equals(REFRESH)) {
			frame.refresh();
		} else if (command.equals(EXIT)) {
			System.exit(0);
		} else if (command.equals(ABOUT)) {
			showAboutDialog();
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} else if (command.equals(SAVE)) {
			if (!frame.isConnected()) {
				frame.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(frame,
					    "You have to reconnect first!",
					    "Not connected",
					    JOptionPane.ERROR_MESSAGE);
				
			} else {
				int returnVal = fc.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					frame.saveAs(file);
				} else {
					frame.setCursor(Cursor
							.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		} else if (command.equals(Settings.START_EXPERIMENT)) {
			try {
				ftp.changeToRootDir();
				ftp.changeWorkingDirectory(Settings.DEFAULT_FOLDER);
				ftp.makeDirectory(command);
				frame.start();
			} catch (IOException e1) {
				frame.consoleAppendText(e1.getMessage());
			} finally {
				frame.refresh();
			}
		} else if (command.equals(RECONNECT)) {
			frame.reconnect();
		}
		/*
		 * else if (command.equals(RANK)) { frame.showRank(); }
		 */
		else if (command.equals(Settings.PRACTICE_ROUND)
				|| command.indexOf(Settings.PAYING_ROUND) >= 0) {
			if (e.getSource() instanceof JToggleButton) {
				JToggleButton b = (JToggleButton) e.getSource();
				setToggleButtonImage(b, command);

				try {
					JakartaFtpWrapper.setToDefaultDir(ftp);

					if (b.isSelected()) {
						ftp.makeDirectory(command);
						frame.consoleAppendText("Added dir: " + command);
					} else {
						ftp.removeDirectory(command);
						frame.consoleAppendText(REMOVED_DIR + command);
					}
				} catch (IOException e1) {
					frame.consoleAppendText(e1.getMessage());
				} finally {
					frame.refresh();
				}
			}
		} else if (command.equals(DELETE_SELECTED)) {
			int n = JOptionPane.showOptionDialog(frame,
					"Are you sure you want to delete all selected files?\n",
					command, JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, UIManager
							.getIcon("OptionPane.warningIcon"),
					Settings.YES_CANCEL_OPTIONS, // the titles of
					// buttons
					Settings.YES_CANCEL_OPTIONS[1]); // default button
			// title

			if (n == 0) {// YES
				frame.deleteSelected();
				frame.refresh();
			} else {
				frame.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else if (command.equals(CLEAR_SESSION)) {
			int n = JOptionPane
					.showOptionDialog(
							frame,
							"Are you sure you want to clear the session?\n"
									+ "This action will reset the timers and remove approval for:\n"
									+ "- " + Settings.START_EXPERIMENT + "\n"
									+ "- Proceeding to all rounds", command,
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, UIManager
									.getIcon("OptionPane.warningIcon"),
							Settings.YES_CANCEL_OPTIONS, // the titles of
							// buttons
							Settings.YES_CANCEL_OPTIONS[1]); // default button
			// title

			if (n == 0) {// YES
				try {
					ftp.changeToRootDir();
					ftp.changeWorkingDirectory(Settings.DEFAULT_FOLDER);
					ftp.removeDirectory(Settings.START_EXPERIMENT);
					frame.consoleAppendText("Removed dir: "
							+ Settings.START_EXPERIMENT);
					ftp.removeDirectory(Settings.PRACTICE_ROUND);
					frame.consoleAppendText("Removed dir: "
							+ Settings.PRACTICE_ROUND);
					for (int i = 1; i <= Settings.NUMBER_OF_PAYING_ROUNDS; i++) {
						ftp.removeDirectory(Settings.PAYING_ROUND + " " + i);
						frame.consoleAppendText(REMOVED_DIR
								+ Settings.PAYING_ROUND + " " + i);
					}
					frame.clearSession();

				} catch (IOException e1) {
					frame.consoleAppendText(e1.getMessage());
				}
			} else { // NO
				frame.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else if (command.equals(PRINT)) {
			frame.printDataTable();
		} else if (command.equals(SELECT_ALL)) {
			frame.selectAll();
		} else {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

	}

	private void setToggleButtonImage(JToggleButton button, String altText) {
		if (button.isSelected())
			button.setIcon(GnretToolbar.getImageIconGeneral(DISABLE_ROUND_IMAGE
					+ BIG_END, altText));
		else
			button.setIcon(GnretToolbar.getImageIconGeneral(ENABLE_ROUND_IMAGE
					+ BIG_END, altText));
	}

	private void showAboutDialog() {
		String message = "<html><p align=left>" + "Application: "
				+ GenderNeutralRealEffortTask.NAME + "<br>" + "Version: "
				+ GenderNeutralRealEffortTask.VERSION + "<br>"
				+ "Experiment design: " + GenderNeutralRealEffortTask.DESIGNER
				+ "<br>" + "Programming: " + GenderNeutralRealEffortTask.AUTHOR
				+ "<br>" + "Last Modification: "
				+ GenderNeutralRealEffortTask.LAST_MODIFICATION + "<br>";
		JOptionPane.showMessageDialog(frame, message, "About",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
