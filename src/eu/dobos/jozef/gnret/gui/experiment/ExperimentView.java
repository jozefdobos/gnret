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

package eu.dobos.jozef.gnret.gui.experiment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingWorker;

import eu.dobos.jozef.gnret.GenderNeutralRealEffortTask;
import eu.dobos.jozef.gnret.communication.JakartaFtpWrapper;
import eu.dobos.jozef.gnret.gui.AbstractGNRETFrame;
import eu.dobos.jozef.gnret.gui.FtpView;
import eu.dobos.jozef.gnret.gui.admin.table.GnretTableModel;
import eu.dobos.jozef.gnret.gui.experiment.grid.SpellCheckManager;
import eu.dobos.jozef.gnret.gui.experiment.grid.WordSearchPanel;
import eu.dobos.jozef.gnret.gui.experiment.instructions.InstructionsPanel;
import eu.dobos.jozef.gnret.gui.experiment.instructions.InstructionsPanel.Text;
import eu.dobos.jozef.gnret.gui.experiment.instructions.IntroScreen;
import eu.dobos.jozef.gnret.gui.experiment.numerical.NumericalScreenPanel;
import eu.dobos.jozef.gnret.gui.experiment.progress.TaskProgressPanel;
import eu.dobos.jozef.gnret.gui.experiment.questionnaire.QuestionnairePanel;
import eu.dobos.jozef.gnret.gui.experiment.stats.TaskCallbackInterface;
import eu.dobos.jozef.gnret.gui.experiment.stats.TaskStatsLogger;
import eu.dobos.jozef.gnret.gui.experiment.stats.TaskStatsPanel;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;
import eu.dobos.jozef.gnret.gui.utils.WindowClosedFTPListener;
import eu.dobos.jozef.gnret.time.TimeKeeper;

@SuppressWarnings("serial")
public class ExperimentView extends AbstractGNRETFrame implements KeyListener,
		ActionListener {

	private static String getScoreTextToAppendToFile(
			TaskStatsLogger taskStatsLogger) {
		int roundCorrectAnswersSum = taskStatsLogger.getCurrentCorrectAnswers()
				+ taskStatsLogger.getPreviousCorrectAnswers();
		int roundWrongAnswersSum = taskStatsLogger.getCurrentWrongAnswers()
				+ taskStatsLogger.getPreviousWrongAnswers();

		String text = taskStatsLogger.getPreviousCorrectAnswers()
				+ Settings.DELIMINETER
				+ taskStatsLogger.getPreviousWrongAnswers()
				+ Settings.DELIMINETER
				+ taskStatsLogger.getCurrentCorrectAnswers()
				+ Settings.DELIMINETER
				+ taskStatsLogger.getCurrentWrongAnswers()
				+ Settings.DELIMINETER + roundCorrectAnswersSum
				+ Settings.DELIMINETER + roundWrongAnswersSum;
		return text;
	}

	private TaskProgressPanel progressPanel;
	private TaskStatsLogger taskStatsLogger = new TaskStatsLogger();
	protected int screenCounter = -1;
	private int escButtonPressesCounter = 0;
	private final JakartaFtpWrapper ftp;
	private final String fileName;
	private final String group;
	private boolean shiftKeyOn = false;
	private boolean sessionStarted = false;
	private boolean calculateRank = false;
	private TaskStatsPanel taskStatsPanel;
	private HashMap<String, Integer> ranks;
	private FtpView ftpView;

	private SpellCheckManager spellCheckManager;

	public ExperimentView(FtpView ftpView, JakartaFtpWrapper ftp,
			String fileName, String group) {
		super(GenderNeutralRealEffortTask.ACRONYM + " Experiment");
		this.ftpView = ftpView;
		this.ftp = ftp;
		this.fileName = fileName;
		this.group = group;
		// this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// Set to ignore the button
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// this.addComponentListener((ComponentListener) this);

		// required for keylistener to log
		this.setFocusable(true);
		this.addKeyListener(this);
		this.addWindowListener(new WindowClosedFTPListener(ftp));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(new Dimension(screenSize.width, screenSize.height));
		this.setExtendedState(Frame.MAXIMIZED_BOTH);

		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		displayIntroScreen();
		showExperimentScreen();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (timeKeeper.updateMiliseconds()) {
			// time is up, task finished
			timeKeeper.stop();

			if (isProgressCheckScreen(screenCounter)) {
				checkProgress();
			} else if (screenCounter == QUESTIONNAIRE_SCREEN) {
				timeKeeper = null;
				timeKeeper = new TimeKeeper(EXPERIMENT_TIMING[screenCounter],
						this);
				previousTime = EXPERIMENT_TIMING[screenCounter];
				timeKeeper.start();
			} else
				switchScreen();
		}
		returnTime(timeKeeper.getTime());
	}

	private void checkProgress() {
		progressPanel.setWaiting();
		timeKeeper = null;
		timeKeeper = new TimeKeeper(EXPERIMENT_TIMING[screenCounter], this);
		previousTime = EXPERIMENT_TIMING[screenCounter];
		timeKeeper.start();
	}

	@Override
	public void consoleAppendText(String text) {
		ftpView.getConsole().append("Experiment Window: " + text);
	}

	private void displayInstructions(Text textSelector) {
		this.getContentPane().removeAll();
		this.setLayout(new BorderLayout());
		checkProgress();
		progressPanel = new TaskProgressPanel(1,
				TimeKeeper.TIME_FORMAT.format(1), false, 0, true, screenCounter);
		this.getContentPane().add(progressPanel, BorderLayout.NORTH);
		InstructionsPanel instructionsPanel = new InstructionsPanel(
				textSelector);
		this.getContentPane().add(instructionsPanel);
	}

	private void displayIntroScreen() {
		this.getContentPane().removeAll();
		this.setLayout(new BorderLayout());
		progressPanel = new TaskProgressPanel(1, "1", false, 0, false,
				screenCounter);
		progressPanel.setText("");
		this.getContentPane().add(progressPanel, BorderLayout.NORTH);
		this.getContentPane().add(new IntroScreen(this), BorderLayout.CENTER);
	}

	private void displayNumericalScereenPanel(long timeOut) {
		String s = roundCounter == 0 ? Settings.PRACTICE_ROUND
				: Settings.PAYING_ROUND + " " + roundCounter;
		consoleAppendText("Loading Numerical Task for " + s);
		taskStatsLogger.setNextTask();
		initialiseTimeKeeper(timeOut, true, roundCounter);
		TaskCallbackInterface[] arrayOfTaskStatsListeners = { taskStatsLogger,
				progressPanel };
		// use round number as a seed!
		this.getContentPane().add(
				new NumericalScreenPanel(arrayOfTaskStatsListeners,
						roundCounter), BorderLayout.CENTER);
	}

	private void displayQuestionnaire(Text textSelector) {
		timeKeeper = new TimeKeeper(EXPERIMENT_TIMING[screenCounter], this);
		this.getContentPane().removeAll();
		this.setLayout(new BorderLayout());
		progressPanel = new TaskProgressPanel(1, "1", false, 0, false,
				screenCounter);
		progressPanel.setText("");
		this.getContentPane().add(progressPanel, BorderLayout.NORTH);
		try {
			this.getContentPane().add(
					new QuestionnairePanel(this, textSelector),
					BorderLayout.CENTER);
		} catch (IOException e) {
			consoleAppendText("Could not display Questionnaire: "
					+ e.getMessage());
		}
		timeKeeper.start();
	}

	private void displayRank(final int round, final String fileName) {
		final ExperimentView frame = this;
		SwingWorker<HashMap<String, Integer>, Void> worker = new SwingWorker<HashMap<String, Integer>, Void>() {
			@Override
			protected HashMap<String, Integer> doInBackground()
					throws Exception {
				Vector<String> files = null;
				HashMap<String, Integer> ranks = new HashMap<String, Integer>();
				try {
					frame.consoleAppendText("Checking rank...");
					GnretTableModel table = new GnretTableModel();
					// if (ftp.isConnected()) {
					files = ftp.listFileNames();
					if (files != null) {
						for (String fileName : files) {
							String fileContent = ftp.getFileAsString(fileName);
							table.addRow(fileName, fileContent);
						}

						table.addRankColumns();

						int column = 0;
						switch (round) {
						case 0:
							column = GnretTableModel.RANK_PRACTICE_COLUMN_INDEX;
							break;
						case 1:
							column = GnretTableModel.RANK_PAYING_ONE_COLUMN_INDEX;
							break;
						case 2:
							column = GnretTableModel.RANK_PAYING_TWO_COLUMN_INDEX;
							break;
						case 3:
							column = GnretTableModel.RANK_PAYING_THREE_COLUMN_INDEX;
							break;
						case 4:
							column = GnretTableModel.RANK_PAYING_FOUR_COLUMN_INDEX;
							break;
						case 5:
							column = GnretTableModel.RANK_PAYING_FIVE_COLUMN_INDEX;
							break;
						case 6:
							column = GnretTableModel.RANK_PAYING_SIX_COLUMN_INDEX;
							break;
						default:
							break;
						}

						Object[] rankColumn = table.getColumn(column);

						for (int i = 0; i < rankColumn.length; i++) {
							ranks.put((String) table.getValueAt(i,
									GnretTableModel.FILE_NAME_COLUMN_INDEX),
									(Integer) rankColumn[i]);
						}
						// } else {
						// consoleAppendText("Not connected to the server");
						// tryToReconnectAndLog(ftp, true, frame);
						// }
					} else {
						consoleAppendText("File list on server returned null");
					}
				} catch (Exception e) {
					consoleAppendText("E5: " + e.getMessage());
					tryToReconnectAndLog(ftp, true, frame);
				}
				return ranks;
			}

			@Override
			public void done() {
				try {
					ranks = get();
					if (!(ranks.containsValue(Integer.MAX_VALUE) || ranks
							.containsValue(Integer.MIN_VALUE))
							&& ranks.containsKey(fileName)
							&& ranks.get(fileName) != null) {
						// rank done
						consoleAppendText("Rank calculation finished: "
								+ ranks.get(fileName) + " out of "
								+ ranks.size());
						calculateRank = false;
					}
				} catch (Exception ignore) {
					consoleAppendText("E6: " + ignore.getMessage());
				}
			}
		};
		worker.execute();
	}

	private void displayStatisticalResults(long timeOut) {
		String s = roundCounter == 0 ? Settings.PRACTICE_ROUND
				: Settings.PAYING_ROUND + " " + roundCounter;
		consoleAppendText("Loading Results for " + s);

		ranks = null;
		initialiseTimeKeeper(timeOut, false, 0);
		taskStatsPanel = new TaskStatsPanel(taskStatsLogger, roundCounter,
				group.equals(Settings.T1_GROUP));
		this.getContentPane().add(taskStatsPanel);
		this.getContentPane().validate();

		// display screen before trying to
		// upload to server
		saveAndUpload(getScoreTextToAppendToFile(taskStatsLogger), false);
	}

	private void displayWordSearchPanel(long timeOut) {
		String s = roundCounter == 0 ? Settings.PRACTICE_ROUND
				: Settings.PAYING_ROUND + " " + roundCounter;
		consoleAppendText("Loading Word Grid Task for " + s);
		taskStatsLogger.setNextTask();
		initialiseTimeKeeper(timeOut, true, roundCounter);
		try {
			TaskCallbackInterface[] arrayOfTaskStatsListeners = {
					taskStatsLogger, progressPanel };

			// use round number as seed!
			this.getContentPane().add(
					new WordSearchPanel(arrayOfTaskStatsListeners,
							spellCheckManager, roundCounter),
					BorderLayout.CENTER);

		} catch (IOException e) {
			consoleAppendText("E2: " + e.getMessage());
			String titleDescription = "Error while loading Word Grid";
			if (0 == Utils.showRetryCancelErrorDialog(this, e.getMessage(),
					titleDescription)) {
				displayWordSearchPanel(timeOut);
			}

		}

	}

	private void initialiseTimeKeeper(long timeOut, boolean statsOn, int round) {
		this.getContentPane().removeAll();
		this.setLayout(new BorderLayout());
		timeKeeper = new TimeKeeper(timeOut, this);
		progressPanel = new TaskProgressPanel((int) timeOut,
				TimeKeeper.TIME_FORMAT.format(timeOut), statsOn, round, false,
				screenCounter);
		this.getContentPane().add(progressPanel, BorderLayout.NORTH);
		timeKeeper.start();
	}

	// KEY LISTENER
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftKeyOn = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftKeyOn = false;
		} else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT && shiftKeyOn
				&& sessionStarted) {

			if (screenCounter == PROGRESS_CHECK_PRACTICE_ROUND
					|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_ONE
					|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_TWO
					|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_THREE
					|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_FOUR
					|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_FIVE
					|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_SIX) {
				incrementRoundCounter();
			}
			switchScreen();
		} else if (arg0.getKeyCode() == KeyEvent.VK_LEFT && shiftKeyOn) {

			if (screenCounter == PAYING_ROUND_ONE_GRID_SCREEN
					|| screenCounter == PAYING_ROUND_TWO_GRID_SCREEN
					|| screenCounter == PAYING_ROUND_THREE_GRID_SCREEN
					|| screenCounter == PAYING_ROUND_FOUR_GRID_SCREEN
					|| screenCounter == PAYING_ROUND_FIVE_GRID_SCREEN
					|| screenCounter == PAYING_ROUND_SIX_GRID_SCREEN
					|| screenCounter == QUESTIONNAIRE_SCREEN) {
				decrementRoundCounter();
			}
			screenCounter -= 2;
			switchScreen();
		} else if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			escButtonPressesCounter++;
			if (escButtonPressesCounter >= 3) {
				System.exit(0);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public void returnTime(long time) {
		if (time != 0 && screenCounter != QUESTIONNAIRE_SCREEN) // time up
			progressPanel.setTime((int) time,
					TimeKeeper.TIME_FORMAT.format(time));

		// check screen progress
		if (isProgressCheckScreen(screenCounter)
				&& time % Settings.INTER_CHECK_DELAY == 0) {
			switchScreenIfCanProgress(screenCounter == CONNECTION_SCREEN, ftp,
					this, roundCounter, screenCounter);
		}

		// clear results after one minute
		if ((isResultsScreen(screenCounter) || isProgressCheckScreen(screenCounter))
				&& (time < TimeKeeper.THREE_MINUTES) && taskStatsPanel != null) {
			if (!calculateRank && ranks != null && ranks.get(fileName) != null && (screenCounter != PRACTICE_ROUND_RESULTS_SCREEN) && (screenCounter != PROGRESS_CHECK_PRACTICE_ROUND)) {
				String rank = ranks.get(fileName) + " out of " + ranks.size();
				taskStatsPanel.setRank(rank);
				consoleAppendText("Rank displayed on the screen");
				ranks = null;
				this.invalidate();
				this.repaint();
			} else {
				taskStatsPanel.clearScore();
			}
		}

		if ((screenCounter != QUESTIONNAIRE_SCREEN) && calculateRank
				&& time % (Settings.INTER_CHECK_DELAY * 4) == 0) {
			displayRank(roundCounter, fileName);
		}

		if (previousTime - time >= nextNoopTimeCheck) {
			nextNoopTimeCheck = getNextNoopCheckTime();
			previousTime = time;
			try {
				ftp.sendNoOp();
				consoleAppendText("Sent NO-OPeration command (keep alive message) to the server");
				consoleAppendText("Server replied: " + ftp.getReplyString());
			} catch (Exception e) {
				consoleAppendText("E3: " + e.getMessage());

				try {
					ftp.disconnect();
					tryToReconnectAndLog(ftp, true, this);
				} catch (IOException e1) {
					consoleAppendText("E4: " + e1.getMessage());
				}

			}
		}
	}

	public void saveAndUpload(final String text, final boolean canExit) {
		final ExperimentView view = this;
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				saveTextToLocalFile(view, fileName, text);
				uploadLocalFileToServer(view, ftp, fileName);
				return null;
			}

			@Override
			public void done() {
				if (canExit) {
					System.exit(0);
				}

				if (isResultsScreen(screenCounter))
					calculateRank = group.equals(Settings.T1_GROUP);

			}
		};
		worker.execute();
	}

	public void setDefaultButton(JButton button) {
		this.getRootPane().setDefaultButton(button);
	}

	public void showExperimentScreen() {
		try {
			spellCheckManager = new SpellCheckManager();
			consoleAppendText("Disconnecting...");
			ftp.disconnect();
			validate();
			this.setVisible(true);
		} catch (IOException e) {
			consoleAppendText("E1: " + e.getMessage());
		}
	}

	public void switchScreen() {
		screenCounter++;
		// calculateRank = false;

		if (timeKeeper != null)
			timeKeeper.stop();

		if (screenCounter < EXPERIMENT_TIMING.length)
			previousTime = EXPERIMENT_TIMING[screenCounter];

		switch (screenCounter) {
		case CONNECTION_SCREEN:
			displayInstructions(Text.CONNECTION);
			// displayQuestionnaire(Text.CONCLUSION);
			break;

		case PROGRESS_CHECK_PRACTICE_ROUND:
		case PROGRESS_CHECK_PAYING_ROUND_ONE:
		case PROGRESS_CHECK_PAYING_ROUND_TWO:
		case PROGRESS_CHECK_PAYING_ROUND_THREE:
		case PROGRESS_CHECK_PAYING_ROUND_FOUR:
		case PROGRESS_CHECK_PAYING_ROUND_FIVE:
		case PROGRESS_CHECK_PAYING_ROUND_SIX:
			checkProgress();
			break;

		case PRACTICE_ROUND_GRID_SCREEN:
		case PAYING_ROUND_ONE_GRID_SCREEN:
		case PAYING_ROUND_TWO_GRID_SCREEN:
		case PAYING_ROUND_THREE_GRID_SCREEN:
		case PAYING_ROUND_FOUR_GRID_SCREEN:
		case PAYING_ROUND_FIVE_GRID_SCREEN:
		case PAYING_ROUND_SIX_GRID_SCREEN:
			displayWordSearchPanel(EXPERIMENT_TIMING[screenCounter]);
			break;

		case PRACTICE_ROUND_NUMERICAL_SCREEN:
		case PAYING_ROUND_ONE_NUMERICAL_SCREEN:
		case PAYING_ROUND_TWO_NUMERICAL_SCREEN:
		case PAYING_ROUND_THREE_NUMERICAL_SCREEN:
		case PAYING_ROUND_FOUR_NUMERICAL_SCREEN:
		case PAYING_ROUND_FIVE_NUMERICAL_SCREEN:
		case PAYING_ROUND_SIX_NUMERICAL_SCREEN:
			displayNumericalScereenPanel(EXPERIMENT_TIMING[screenCounter]);
			break;

		case PRACTICE_ROUND_RESULTS_SCREEN:
		case PAYING_ROUND_ONE_RESULTS_SCREEN:
		case PAYING_ROUND_TWO_RESULTS_SCREEN:
		case PAYING_ROUND_THREE_RESULTS_SCREEN:
		case PAYING_ROUND_FOUR_RESULTS_SCREEN:
		case PAYING_ROUND_FIVE_RESULTS_SCREEN:
		case PAYING_ROUND_SIX_RESULTS_SCREEN:
			displayStatisticalResults(EXPERIMENT_TIMING[screenCounter]);
			break;

		case QUESTIONNAIRE_SCREEN:
			displayQuestionnaire(Text.CONCLUSION);
			break;
		default:
			System.exit(0);
			break;
		}
		this.getContentPane().validate();
	}

	public void uploadStartYes() {
		sessionStarted = true;
		final ExperimentView view = this;

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				saveTextToLocalFile(view, fileName, "Y");
				uploadLocalFileToServer(view, ftp, fileName);
				return null;
			}

			@Override
			public void done() {
				switchScreen();
			}
		};
		try {
			if (tryToReconnectAndLog(ftp, true, this))
				worker.execute();
		} catch (Exception e) {
			consoleAppendText("E15: " + e.getMessage());
		}
	}

}
