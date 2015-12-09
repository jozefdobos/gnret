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

package eu.dobos.jozef.gnret.gui.admin;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import org.apache.commons.net.ftp.FTPConnectionClosedException;

import eu.dobos.jozef.gnret.GenderNeutralRealEffortTask;
import eu.dobos.jozef.gnret.communication.JakartaFtpWrapper;
import eu.dobos.jozef.gnret.filehandling.FileWritingHandler;
import eu.dobos.jozef.gnret.gui.AbstractGNRETFrame;
import eu.dobos.jozef.gnret.gui.admin.actions.GnretActionListener;
import eu.dobos.jozef.gnret.gui.admin.actions.GnretMenuBar;
import eu.dobos.jozef.gnret.gui.admin.actions.GnretToolbar;
import eu.dobos.jozef.gnret.gui.admin.filetree.FileTree;
import eu.dobos.jozef.gnret.gui.admin.progress.GnretProgressBar;
import eu.dobos.jozef.gnret.gui.admin.table.DataTable;
import eu.dobos.jozef.gnret.gui.console.Console;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;
import eu.dobos.jozef.gnret.gui.utils.WindowClosedFTPListener;
import eu.dobos.jozef.gnret.time.TimeKeeper;

public class AdminView extends AbstractGNRETFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7950874753108189953L;
	private static final int TOP_HEIGHT = 400;

	private static JSplitPane prepareSplitPane(JSplitPane s) {
		s.setBorder(null);
		s.setOneTouchExpandable(false);
		s.setDividerSize(5);
		return s;
	}

	private Console console = new Console();
	private DataTable dataTable;
	private FileTree fileTree;
	private JakartaFtpWrapper ftp;
	private GnretProgressBar progressPanel = new GnretProgressBar();

	protected int screenCounter = 0;
	private GnretToolbar toolbar;

	public AdminView(JakartaFtpWrapper ftp) {
		super(GenderNeutralRealEffortTask.ACRONYM + " Admin - v"
				+ GenderNeutralRealEffortTask.VERSION);
		setFTP(ftp);
		this.addWindowListener(new WindowClosedFTPListener(ftp));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GnretActionListener listener = new GnretActionListener(this, ftp);
		toolbar = new GnretToolbar(listener);
		this.setJMenuBar(new GnretMenuBar(listener));
		setDataTable(new DataTable());
		JPanel mainPanel = new JPanel();

		JPanel wholeWindowPanel = new JPanel();
		wholeWindowPanel.setLayout(new BorderLayout());
		wholeWindowPanel.add(toolbar, BorderLayout.PAGE_START);
		wholeWindowPanel.add(mainPanel, BorderLayout.CENTER);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(progressPanel, BorderLayout.PAGE_START);

		fileTree = new FileTree(this, ftp, listener);
		fileTree.setPreferredSize(new Dimension(200, TOP_HEIGHT));
		JSplitPane topSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				fileTree, dataTable);
		topSplitPane.setDividerLocation(0.25);
		topSplitPane.setMinimumSize(new Dimension(200, TOP_HEIGHT));

		JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				topSplitPane, console);
		bottomSplitPane.setDividerLocation(0.75);

		topSplitPane = prepareSplitPane(topSplitPane);
		bottomSplitPane = prepareSplitPane(bottomSplitPane);

		mainPanel.add(bottomSplitPane, BorderLayout.CENTER);
		this.getContentPane().add(wholeWindowPanel);
		this.pack();
		// this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(getRootPane());

		refresh();

		this.validate();
		this.setVisible(true);
	}

	public void clearSession() {
		screenCounter = 0;
		setRoundCounter(0);
		progressPanel.setWaitingProgressBar();
		if (timeKeeper != null) {
			timeKeeper.stop();
			timeKeeper = null;
		}
		toolbar.setStartEnabled(true);
		consoleAppendText("Session cleared");
		refresh();
	}

	public synchronized void consoleAppendText(String text) {
		console.append(text);
	}

	public DataTable getDataTable() {
		return dataTable;
	}

	public void printDataTable() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				if (dataTable.getTable() != null) {
					MessageFormat header = new MessageFormat("GNRET");
					Object[] arguments = { new Date(System.currentTimeMillis()) };

					String result = MessageFormat.format("{0,date}, {0,time}",
							arguments);
					MessageFormat footer = new MessageFormat(result);
					boolean showPrintDialog = true;
					boolean interactive = true;
					PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
					attr.add(OrientationRequested.LANDSCAPE);
					try {
						dataTable.getTable().print(JTable.PrintMode.FIT_WIDTH,
								header, footer, showPrintDialog, attr,
								interactive);
					} catch (Exception e) {
						consoleAppendText("Printing failed: " + e.getMessage());
					}
				}
				return null;
			}

			@Override
			public void done() {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		};
		worker.execute();
	}

	public void reconnect() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				consoleAppendText("");
				consoleAppendText("Trying to reconnect...");
				try {
					ftp.tryToReconnectToFTP(false);
					consoleAppendText("Server replied: " + ftp.getReplyString());
				} catch (Exception e) {
					consoleAppendText(e.getMessage());
				}
				return null;
			}

			@Override
			public void done() {
				refresh();
				try {
					ftp.printWorkingDirectory();
					consoleAppendText("Reconnection successful!");
					consoleAppendText("");
				} catch (IOException e) {
					consoleAppendText(e.getMessage());
				} finally {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		};
		worker.execute();

	}

	public void refresh() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				try {
					ftp.changeToRootDir();
					ftp.changeWorkingDirectory(Settings.DEFAULT_FOLDER);
					toolbar.setToggleButtonsSelected(ftp.listSubdirNames());
					ftp.changeToRootDir();
				} catch (Exception e) {
					consoleAppendText(e.getMessage());
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				return null;
			}

			@Override
			public void done() {
				try {
					fileTree.refresh();
				} catch (Exception e) {
					consoleAppendText(e.getMessage());
				} finally {
					dataTable.removeAllData();
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		};
		worker.execute();
	}

	public void returnTime(long time) {
		if (time != 0) // time up
			progressPanel.setTime((int) time,
					TimeKeeper.TIME_FORMAT.format(time));

		if (isProgressCheckScreen(screenCounter)
				&& time % Settings.INTER_CHECK_DELAY == 0) {

			try {
				if (!ftp.isInDefaultDir()) {
					ftp.changeToRootDir();
					ftp.changeWorkingDirectory(Settings.DEFAULT_FOLDER);
				}
				switchScreenIfCanProgress(false, ftp, this, roundCounter,
						screenCounter);
			} catch (FTPConnectionClosedException e2) {
				consoleAppendText(e2.getMessage());
				reconnect();
			} catch (Exception e1) {
				consoleAppendText(e1.getMessage());
			}
		}

		if (previousTime - time >= nextNoopTimeCheck) {
			nextNoopTimeCheck = getNextNoopCheckTime();
			previousTime = time;
			try {
				ftp.sendNoOp();
				consoleAppendText("Sent NO-OPeration command (keep alive message) to the server");
				consoleAppendText("Server replied: " + ftp.getReplyString());
			} catch (IOException e) {
				consoleAppendText(e.getMessage());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (timeKeeper.updateMiliseconds()) {
			consoleAppendText(EXPERIMENT_DESCRIPTION[screenCounter] + " done");
			// time is up, task finished
			timeKeeper.stop();
			if (isProgressCheckScreen(screenCounter)) {
				setTimeKeeper(screenCounter);
			}
			else 
				switchScreen();
		}
		returnTime(timeKeeper.getTime());
	}

	private synchronized void setDataTable(DataTable table) {
		dataTable = table;
	}

	private synchronized void setFTP(JakartaFtpWrapper ftp) {
		this.ftp = ftp;
	}

	private void setTimeKeeper(int screenCounter) {
		timeKeeper = null;

		long timeToSet = EXPERIMENT_TIMING[screenCounter];
		if (screenCounter == PRACTICE_ROUND_GRID_SCREEN) {
			timeToSet += Settings.INTER_CHECK_DELAY / 2;
		}
		timeKeeper = new TimeKeeper(timeToSet, this);
		previousTime = timeToSet;

		progressPanel.setNewProgressBar(screenCounter, (int) timeToSet,
				EXPERIMENT_DESCRIPTION[screenCounter],
				TimeKeeper.TIME_FORMAT.format(timeToSet));
		timeKeeper.start();
	}

	public void start() {
		consoleAppendText("Checking if experiment started");
		switchScreen();
	}

	protected void switchScreen() {
		screenCounter++;

		if (screenCounter == CONNECTION_SCREEN) {
			toolbar.setStartEnabled(true);
		} else if (screenCounter == PRACTICE_ROUND_GRID_SCREEN) {
			toolbar.setStartEnabled(false);
		}

		if (screenCounter == QUESTIONNAIRE_SCREEN) {
			progressPanel.setWaiting(false, -1);
			progressPanel.setText(GnretProgressBar.QUESTIONNAIRE);
			consoleAppendText(GnretProgressBar.QUESTIONNAIRE);
		} else {
			setTimeKeeper(screenCounter);
			if (isProgressCheckScreen(screenCounter)) {
				consoleAppendText("Entered progress check mode == busy wait!!");
				progressPanel.setWaiting(true, roundCounter);
			}
		}
	}

	public void deleteSelected() {
		fileTree.deleteSelected();
	}

	public void selectAll() {
		fileTree.selectAll();
	}

	public void saveAs(final File directoryPath) {

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			final String dateTime = Utils.getFormattedDateTime("yyyy-MM-dd-HH-mm");
			@Override
			public Void doInBackground() {
				String tableContent = dataTable.exportTable(Settings.CSV_DELIMINETER); 
				save(directoryPath.getAbsolutePath(), dateTime + "_Table"
						+ Settings.CSV_FILE_EXTENSION,
						tableContent);
				
				HashMap<String, String> rawFiles = fileTree.loadFiles(true);		
				for (String fileName : rawFiles.keySet()) {					
					String content = rawFiles.get(fileName);
					
					// sanitize
					content = content.replaceAll("\\t", "\"" + Settings.CSV_DELIMINETER + "\""); // remove all tabs
					content = "\"" + content + "\"";
					save(directoryPath.getAbsolutePath(), dateTime + "_" + fileName
							+ Settings.CSV_FILE_EXTENSION,
							content);
				}
				return null;
			}

			@Override
			public void done() {
				setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				
			}
		};
		worker.execute();
	}

	private void save(String path, String fileName, String content) {
		consoleAppendText("Saving to: " + path + File.separator + fileName);
		try {
			FileWritingHandler.saveToCSVFile(path, fileName, content);
		} catch (IOException e) {
		/*	JOptionPane.showMessageDialog(this,
					"Could not save file.\n" + e.getMessage(), "Save error",
					JOptionPane.ERROR_MESSAGE);*/
			consoleAppendText("Saving failed: " + e.getMessage());
		}
	}
	
	public boolean isConnected() {
		return ftp.isConnected();
	}
}
