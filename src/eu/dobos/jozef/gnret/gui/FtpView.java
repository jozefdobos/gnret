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

package eu.dobos.jozef.gnret.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;

import org.apache.commons.net.ftp.FTPConnectionClosedException;

import eu.dobos.jozef.gnret.GenderNeutralRealEffortTask;
import eu.dobos.jozef.gnret.communication.JakartaFtpWrapper;
import eu.dobos.jozef.gnret.filehandling.FileWritingHandler;
import eu.dobos.jozef.gnret.gui.admin.AdminView;
import eu.dobos.jozef.gnret.gui.console.Console;
import eu.dobos.jozef.gnret.gui.experiment.ExperimentView;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.SpringUtilities;
import eu.dobos.jozef.gnret.gui.utils.Utils;

public class FtpView extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6777083128960084832L;

	private static final int SERVER = 0;
	private static final int PORT = 1;
	private static final int USER = 2;
	private static final int PASSWORD = 3;
	private static final int PASSIVE_MODE = 4;

	private static String getLogInfo(int id, String group)
			throws UnknownHostException, SocketException {
		String log = "";
		log += Utils.getCurrentDate() + Settings.DELIMINETER; // Date
		log += Utils.getCurrentTime() + Settings.DELIMINETER; // Time
		log += id + Settings.DELIMINETER; // Participant
		log += group + Settings.DELIMINETER; // Group
		log += JakartaFtpWrapper.getClientIPAddress() + Settings.DELIMINETER;
		log += JakartaFtpWrapper.getClientMacAddress() + Settings.DELIMINETER;
		log += JakartaFtpWrapper.getClientHostName();
		return log;
	}

	/***
	 * Returns empty string if file upload unsuccessful.
	 * 
	 * @param ftp
	 * @return
	 * @throws FTPConnectionClosedException
	 * @throws IOException
	 */
	private static String processFtpConnectionForExperimentView(
			JakartaFtpWrapper ftp, String group, boolean isCessPressed)
			throws FTPConnectionClosedException, IOException {
		Vector<String> fileNames = ftp.listFileNames();
		int participant = 0;
		if (fileNames.isEmpty()) {
			participant = 1;
		} else {
			// participant = fileNames.size() + 1;
			String lastFile = fileNames.get(fileNames.size() - 1); // the last
																	// file on
																	// the
																	// server
			participant = Integer.parseInt(lastFile.substring(0, 5)) + 1; // new
																			// unique
																			// ID
		}
		String fileName = Utils.getFileName(participant, group);
		FileWritingHandler.createInitialFile(fileName,
				getLogInfo(participant, group));
		console.append("Created local file " + fileName);

		if (ftp.uploadFile(
				FileWritingHandler.getAbsolutePathToLocalFile(fileName),
				fileName))
			console.append("Uploaded file " + fileName + " to the server");
		else {
			console.append("Upload of file " + fileName
					+ " to the server failed");
			fileName = new String();
		}
		return fileName;
	}

	private JPanel contentPane = new JPanel();
	private String selectedRadioButton;
	public static final String DEFAULT_BUTTON = "Default";
	public static final String CESS_BUTTON = "   CESS   ";
	public static final String DOBOS_BUTTON = "Doboš.eu";
	public static final String CONNECT_BUTTON = "Connect";

	private boolean isCessPressed = false;
	private static Console console = new Console();
	private JButton connectButton;
	private String[] labels = { "FTP server:", "Port No.:", "User name:",
			"Password:", "" };

	private JComponent[] fields = new JComponent[labels.length];

	private int fieldLength = 15;

	public FtpView() {
		super(GenderNeutralRealEffortTask.ACRONYM + " FTP Login - v"
				+ GenderNeutralRealEffortTask.VERSION);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(contentPane);

		contentPane.setLayout(new BorderLayout());

		// Create and populate the panel.
		JPanel inputPanel = getInputPanel();
		inputPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		// radio buttons
		JPanel radioButtonsPanel = getRadioButtonsPanel();

		JPanel radioWrapper = new JPanel();
		radioWrapper.setLayout(new BorderLayout());
		radioWrapper.add(radioButtonsPanel, BorderLayout.CENTER);
		radioWrapper.setAlignmentY(Component.TOP_ALIGNMENT);
		JPanel doublePanel = new JPanel();
		doublePanel.setLayout(new BoxLayout(doublePanel, BoxLayout.LINE_AXIS));
		doublePanel.add(radioWrapper);
		doublePanel.add(inputPanel);

		JPanel northContentPaneWrapper = new JPanel();
		northContentPaneWrapper.setLayout(new BoxLayout(
				northContentPaneWrapper, BoxLayout.PAGE_AXIS));
		northContentPaneWrapper.add(doublePanel);

		contentPane.add(northContentPaneWrapper, BorderLayout.NORTH);
		contentPane.add(console, BorderLayout.CENTER);

		JPanel buttonsPanel = getButtonsPanel();

		JPanel buttonsPanelWrapper = new JPanel();
		buttonsPanelWrapper.setLayout(new BorderLayout());
		buttonsPanelWrapper.add(buttonsPanel, BorderLayout.EAST);
		contentPane.add(buttonsPanelWrapper, BorderLayout.SOUTH);

		// Display the window.
		this.setLocation(30, 30);
		this.setMinimumSize(new Dimension(300, 400));
		this.pack();
		this.setVisible(true);

		// Get the jvm heap size
		console.append("JVM heap size: " + Runtime.getRuntime().totalMemory()
				/ 1024.0 / 1024.0 + "MB");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		isCessPressed = false;
		if (command.equals(DOBOS_BUTTON)) {
			((JTextField) fields[SERVER]).setText(Settings.DEFAULT_SERVER);
			((JTextField) fields[USER]).setText(Settings.DEFAULT_USER_NAME);
			((JFormattedTextField) fields[PORT]).setText("");
			((JPasswordField) fields[PASSWORD])
					.setText(Settings.DEFAULT_PASSWORD);
			((JCheckBox) fields[PASSIVE_MODE]).setSelected(true);
		} else if (command.equals(CESS_BUTTON)) {
			isCessPressed = true;
			((JTextField) fields[SERVER]).setText(Settings.CESS_SERVER);
			((JTextField) fields[USER]).setText(Settings.CESS_USER_NAME);
			((JFormattedTextField) fields[PORT]).setText(Settings.CESS_PORT);
			((JPasswordField) fields[PASSWORD]).setText(Settings.CESS_PASSWORD);
			((JCheckBox) fields[PASSIVE_MODE]).setSelected(false);
		} else if (command.equals(CONNECT_BUTTON)) {

			final FtpView ftpView = this;
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				// snapshot of global variables for thread safety
				private final String group = new String(
						selectedRadioButton.toLowerCase());
				private final boolean isAdminGroup = selectedRadioButton
						.equals(Settings.ADMIN_GROUP);

				private final String serverName = new String(
						((JTextField) fields[SERVER]).getText());
				private final String userName = new String(
						((JTextField) fields[USER]).getText());
				private final String portString = new String(
						((JTextField) fields[PORT]).getText());
				private Integer port;
				private final String password = new String(
						((JPasswordField) fields[PASSWORD]).getPassword());
				private JakartaFtpWrapper ftp;
				private String fileName;
				private final boolean pasvOn = ((JCheckBox) fields[PASSIVE_MODE])
						.isSelected();

				@Override
				public Void doInBackground() {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					try {
						if (!portString.isEmpty())
							port = Integer.parseInt(portString);
					} catch (NumberFormatException e) {
						console.append(e.getMessage());
					}

					try {
						ftp = new JakartaFtpWrapper(serverName, port, userName,
								new String(password), pasvOn);
						console.append("Connecting '" + selectedRadioButton
								+ "'...");
						
						
						if (isAdminGroup) {
							ftp.setDataTimeout(JakartaFtpWrapper.FTP_DATA_TIMEOUT);
						}
						
						
						if (ftp.connectAndLogin()) {
							console.append("Connected to " + serverName
									+ " at port " + ftp.getRemotePort());
							/*
							 * if (port == null) ((JFormattedTextField)
							 * fields[PORT]) .setText(ftp.getRemotePort() + "");
							 */
							console.append("Welcome message: "
									+ ftp.getReplyString());

							double connectionTimeOut = ftp.getConnectTimeout() / 60000;
							console.append("Connection timeout set to: "
									+ connectionTimeOut + " min.");

							console.append("Passive host: "
									+ ftp.getPassiveHost());
							console.append("Passive port: "
									+ ftp.getPassivePort());

							JakartaFtpWrapper
									.setEstimatedSeparator(checkDefaultDirectoryOnServer(
											ftp, !isAdminGroup));

							if (!isAdminGroup) {
								fileName = processFtpConnectionForExperimentView(
										ftp, group, isCessPressed);
							}
						} else {
							console.append("Unable to connect to " + serverName);
						}

					} catch (Exception e) {
						console.append(e.getMessage());
					}
					return null;
				}

				@Override
				public void done() {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if (ftp.isConnected()) {
								if (!isAdminGroup) {
									if (fileName != null && !fileName.isEmpty()) {
										new ExperimentView(ftpView, ftp,
												fileName, selectedRadioButton);
									} else
										console.append("Empty local filename hence not starting the experiment window!");
								} else {			
									new AdminView(ftp);
								}
							}
						}
					});
				}
			};
			worker.execute();
		} else if (command.equals(Settings.ADMIN_GROUP)
				|| command.equals(Settings.CONTROL_GROUP)
				|| command.equals(Settings.T1_GROUP)
				|| command.equals(Settings.T2_GROUP)
				|| command.equals(Settings.T3_GROUP)) {
			selectedRadioButton = command;
		}
	}

	/***
	 * Checks whether the default directory resides on the server. If not, it
	 * attempts to create it and change working directory into it.
	 * 
	 * @param ftp
	 * @throws FTPConnectionClosedException
	 * @throws IOException
	 */
	private String checkDefaultDirectoryOnServer(JakartaFtpWrapper ftp,
			boolean changeToDefaultServerDirectory)
			throws FTPConnectionClosedException, IOException {
		String dir = Settings.DEFAULT_FOLDER;
		// check if default directory already present at the server side
		boolean directoryExists = ftp.listSubdirNames().contains(dir);
		if (Settings.DEBUG) {
			console.append("Directory check: " + ftp.printWorkingDirectory()
					+ Settings.UNIX_FILE_SEPARATOR + dir);
			console.append("Directory " + (directoryExists ? "ok" : "missing"));
		}
		if (!directoryExists) {
			console.append("Making dir: " + ftp.makeDirectory(dir));
		}
		String separator = Settings.UNIX_FILE_SEPARATOR;
		ftp.changeWorkingDirectory(dir);
		String workingDir = ftp.printWorkingDirectory();
		String[] folders = workingDir.split(Settings.UNIX_FILE_SEPARATOR);
		if (!(folders.length > 1 && folders[folders.length - 1]
				.equals(Settings.DEFAULT_FOLDER))) {
			separator = Settings.WINDOWS_FILE_SEPARATOR;
		}
		if (changeToDefaultServerDirectory) {
			console.append("Current Directory: " + ftp.printWorkingDirectory());
		} else {
			ftp.changeToParentDirectory();
		}
		return separator;
	}

	private JPanel getButtonsPanel() {
		JPanel buttonsPanel = new JPanel();
		JButton cessButton = new JButton(CESS_BUTTON);
		cessButton.addActionListener(this);
		cessButton.setActionCommand(CESS_BUTTON);

		JButton defaultButton = new JButton(DOBOS_BUTTON);
		defaultButton.addActionListener(this);
		defaultButton.setActionCommand(DOBOS_BUTTON);

		connectButton = new JButton(CONNECT_BUTTON);
		connectButton.addActionListener(this);
		connectButton.setActionCommand(CONNECT_BUTTON);

		buttonsPanel.add(connectButton);
		buttonsPanel.add(cessButton);
		buttonsPanel.add(defaultButton);
		buttonsPanel
				.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

		return buttonsPanel;
	}

	public Console getConsole() {
		return console;
	}

	private JPanel getInputPanel() {
		JPanel inputPanel = new JPanel(new SpringLayout());
		for (int i = 0; i < labels.length; i++) {
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			inputPanel.add(l);
			JComponent c = null;
			if (i == PASSWORD) {
				c = new JPasswordField(fieldLength);
			} else if (i == PORT) {
				c = new JFormattedTextField();
			} else if (i == PASSIVE_MODE) {
				c = new JCheckBox("Local passive mode");
			} else {
				c = new JTextField(fieldLength);
			}
			l.setLabelFor(c);
			inputPanel.add(c);
			fields[i] = c;

		}

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(inputPanel, labels.length, 2, // rows,//
																		// cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		// Set up the content pane.
		inputPanel.setOpaque(true); // content panes must be opaque
		inputPanel.setBorder(BorderFactory.createTitledBorder("Login details"));

		inputPanel.setAlignmentX(Component.TOP_ALIGNMENT);
		return inputPanel;
	}

	private JRadioButton getRadioButton(String text, ButtonGroup group) {
		JRadioButton rb = new JRadioButton(text);
		rb.setActionCommand(text);
		group.add(rb);
		rb.addActionListener(this);
		return rb;
	}

	private JPanel getRadioButtonsPanel() {
		JPanel radioButtonsPanel = new JPanel();
		radioButtonsPanel.setAlignmentX(Component.TOP_ALIGNMENT);

		radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel,
				BoxLayout.PAGE_AXIS));
		radioButtonsPanel.setBorder(BorderFactory.createTitledBorder("Group"));

		ButtonGroup group = new ButtonGroup();

		JRadioButton a = getRadioButton(Settings.ADMIN_GROUP, group);
		a.setSelected(true);
		selectedRadioButton = Settings.ADMIN_GROUP;
		radioButtonsPanel.add(a);

		JRadioButton c = getRadioButton(Settings.CONTROL_GROUP, group);
		radioButtonsPanel.add(c);

		JRadioButton t1 = getRadioButton(Settings.T1_GROUP, group);
		radioButtonsPanel.add(t1);

		JRadioButton t2 = getRadioButton(Settings.T2_GROUP, group);
		radioButtonsPanel.add(t2);

		JRadioButton t3 = getRadioButton(Settings.T3_GROUP, group);
		radioButtonsPanel.add(t3);

		return radioButtonsPanel;
	}
}
