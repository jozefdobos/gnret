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

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.apache.commons.net.ftp.FTPConnectionClosedException;

import eu.dobos.jozef.gnret.communication.JakartaFtpWrapper;
import eu.dobos.jozef.gnret.filehandling.FileWritingHandler;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;
import eu.dobos.jozef.gnret.time.TimeKeeper;

public abstract class AbstractGNRETFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 727146086756011811L;

	protected TimeKeeper timeKeeper;
	protected int roundCounter = 0;
	private Random randomizer = new Random();
	protected long nextNoopTimeCheck = getNextNoopCheckTime();
	protected long previousTime = 0;

	public static final int CONNECTION_SCREEN = 0;
	public static final int PRACTICE_ROUND_GRID_SCREEN = 1;
	public static final int PRACTICE_ROUND_NUMERICAL_SCREEN = 2;
	public static final int PRACTICE_ROUND_RESULTS_SCREEN = 3;
	public static final int PROGRESS_CHECK_PRACTICE_ROUND = 4;

	public static final int PAYING_ROUND_ONE_GRID_SCREEN = 5;
	public static final int PAYING_ROUND_ONE_NUMERICAL_SCREEN = 6;
	public static final int PAYING_ROUND_ONE_RESULTS_SCREEN = 7;
	public static final int PROGRESS_CHECK_PAYING_ROUND_ONE = 8;

	public static final int PAYING_ROUND_TWO_GRID_SCREEN = 9;
	public static final int PAYING_ROUND_TWO_NUMERICAL_SCREEN = 10;
	public static final int PAYING_ROUND_TWO_RESULTS_SCREEN = 11;
	public static final int PROGRESS_CHECK_PAYING_ROUND_TWO = 12;

	public static final int PAYING_ROUND_THREE_GRID_SCREEN = 13;
	public static final int PAYING_ROUND_THREE_NUMERICAL_SCREEN = 14;
	public static final int PAYING_ROUND_THREE_RESULTS_SCREEN = 15;
	public static final int PROGRESS_CHECK_PAYING_ROUND_THREE = 16;

	public static final int PAYING_ROUND_FOUR_GRID_SCREEN = 17;
	public static final int PAYING_ROUND_FOUR_NUMERICAL_SCREEN = 18;
	public static final int PAYING_ROUND_FOUR_RESULTS_SCREEN = 19;
	public static final int PROGRESS_CHECK_PAYING_ROUND_FOUR = 20;

	public static final int PAYING_ROUND_FIVE_GRID_SCREEN = 21;
	public static final int PAYING_ROUND_FIVE_NUMERICAL_SCREEN = 22;
	public static final int PAYING_ROUND_FIVE_RESULTS_SCREEN = 23;
	public static final int PROGRESS_CHECK_PAYING_ROUND_FIVE = 24;

	public static final int PAYING_ROUND_SIX_GRID_SCREEN = 25;
	public static final int PAYING_ROUND_SIX_NUMERICAL_SCREEN = 26;
	public static final int PAYING_ROUND_SIX_RESULTS_SCREEN = 27;
	public static final int PROGRESS_CHECK_PAYING_ROUND_SIX = 28;

	public static final int QUESTIONNAIRE_SCREEN = 29;

	public static final String[] EXPERIMENT_DESCRIPTION = { "Start screen",
			Settings.PRACTICE_ROUND + " GRID",
			Settings.PRACTICE_ROUND + " NUMERICAL",
			Settings.PRACTICE_ROUND + " RESULTS", "",
			Settings.PAYING_ROUND + " 1 GRID",
			Settings.PAYING_ROUND + " 1 NUMERICAL",
			Settings.PAYING_ROUND + " 1 RESULTS", "",
			Settings.PAYING_ROUND + " 2 GRID",
			Settings.PAYING_ROUND + " 2 NUMERICAL",
			Settings.PAYING_ROUND + " 2 RESULTS", "",
			Settings.PAYING_ROUND + " 3 GRID",
			Settings.PAYING_ROUND + " 3 NUMERICAL",
			Settings.PAYING_ROUND + " 3 RESULTS", "",
			Settings.PAYING_ROUND + " 4 GRID",
			Settings.PAYING_ROUND + " 4 NUMERICAL",
			Settings.PAYING_ROUND + " 4 RESULTS", "",
			Settings.PAYING_ROUND + " 5 GRID",
			Settings.PAYING_ROUND + " 5 NUMERICAL",
			Settings.PAYING_ROUND + " 5 RESULTS", "",
			Settings.PAYING_ROUND + " 6 GRID",
			Settings.PAYING_ROUND + " 6 NUMERICAL",
			Settings.PAYING_ROUND + " 6 RESULTS", "" };

	public static final long[] _EXPERIMENT_TIMING = {
		Settings.PROCEEDING_CHECK_DURATION, // start session check
			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS,
			Settings.PROCEEDING_CHECK_DURATION,// practice

			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS, // round 1
			Settings.PROCEEDING_CHECK_DURATION,

			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS, // round 2
			Settings.PROCEEDING_CHECK_DURATION,

			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS, // round 3
			Settings.PROCEEDING_CHECK_DURATION,

			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS, // round 4
			Settings.PROCEEDING_CHECK_DURATION,

			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS, // round 5
			Settings.PROCEEDING_CHECK_DURATION,

			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS,
			TimeKeeper.SIX_SECONDS, // round 6
			Settings.PROCEEDING_CHECK_DURATION,
			Settings.PROCEEDING_CHECK_DURATION // Questionnaire keep alive
	};

	public static final long[] EXPERIMENT_TIMING = {
			Settings.PROCEEDING_CHECK_DURATION, // start session check
			Settings.TASK_DURATION,
			Settings.TASK_DURATION,
			Settings.RESULT_DURATION, // practice
			Settings.PROCEEDING_CHECK_DURATION,

			Settings.TASK_DURATION,
			Settings.TASK_DURATION,
			Settings.RESULT_DURATION, // round 1
			Settings.PROCEEDING_CHECK_DURATION,

			Settings.TASK_DURATION,
			Settings.TASK_DURATION,
			Settings.RESULT_DURATION, // round 2
			Settings.PROCEEDING_CHECK_DURATION,

			Settings.TASK_DURATION,
			Settings.TASK_DURATION,
			Settings.RESULT_DURATION, // round 3
			Settings.PROCEEDING_CHECK_DURATION,

			Settings.TASK_DURATION,
			Settings.TASK_DURATION,
			Settings.RESULT_DURATION, // round 4
			Settings.PROCEEDING_CHECK_DURATION,

			Settings.TASK_DURATION,
			Settings.TASK_DURATION,
			Settings.RESULT_DURATION, // round 5
			Settings.PROCEEDING_CHECK_DURATION,

			Settings.TASK_DURATION, 
			Settings.TASK_DURATION,
			Settings.RESULT_DURATION, // round 6
			Settings.PROCEEDING_CHECK_DURATION,

			Settings.PROCEEDING_CHECK_DURATION // Questionnaire keep alive
	};
	
	public AbstractGNRETFrame(String name) {
		super(name);
	}

	// abstract
	public abstract void consoleAppendText(String text);
	protected abstract void returnTime(long time);
	protected abstract void switchScreen();


	public static boolean isProgressCheckScreen(int screenCounter) {
		return (screenCounter == CONNECTION_SCREEN
				|| screenCounter == PROGRESS_CHECK_PRACTICE_ROUND
				|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_ONE
				|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_TWO
				|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_THREE
				|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_FOUR
				|| screenCounter == PROGRESS_CHECK_PAYING_ROUND_FIVE || screenCounter == PROGRESS_CHECK_PAYING_ROUND_SIX);
	}

	public static boolean isResultsScreen(int screenCounter) {
		return (screenCounter == PRACTICE_ROUND_RESULTS_SCREEN
				|| screenCounter == PAYING_ROUND_ONE_RESULTS_SCREEN
				|| screenCounter == PAYING_ROUND_TWO_RESULTS_SCREEN
				|| screenCounter == PAYING_ROUND_THREE_RESULTS_SCREEN
				|| screenCounter == PAYING_ROUND_FOUR_RESULTS_SCREEN
				|| screenCounter == PAYING_ROUND_FIVE_RESULTS_SCREEN || screenCounter == PAYING_ROUND_SIX_RESULTS_SCREEN);
	}

	public static void saveTextToLocalFile(AbstractGNRETFrame frame,
			String fileName, String text) {
		try {
			FileWritingHandler.appendToFile(fileName, text, true);
		} catch (IOException e) {
			frame.consoleAppendText("E10: " + e.getMessage());
			String titleDescription = "Error while saving to a local file";
			if (0 == Utils.showRetryCancelErrorDialog(frame, e.getMessage(),
					titleDescription)) {
				saveTextToLocalFile(frame, fileName, text);
			}
		}
	}

	/***
	 * 
	 * @param checkStart
	 *            true for start experiment, false for individual rounds
	 * @return
	 */
	public static void switchScreenIfCanProgress(final boolean checkStart,
			final JakartaFtpWrapper ftp, final AbstractGNRETFrame frame,
			final int roundCounter, final int screenCounter) {		
		SwingWorker<boolean[], Void> worker = new SwingWorker<boolean[], Void>() {
			@Override
			protected boolean[] doInBackground() throws Exception {
				boolean[] ret = { false };
				try {
					frame.consoleAppendText("Checking if can proceed... round counter: " + roundCounter);
					if (!ftp.isConnected()) {
						frame.consoleAppendText("Proceed check -- Disconnected, trying to reconnect");

						tryToReconnectAndLog(ftp, true, frame);
					}
					if (checkStart) {
						ret[0] = JakartaFtpWrapper.canStart(ftp);
					} else {
						ret[0] = JakartaFtpWrapper
								.canProceed(ftp, roundCounter);
					}
				} catch (Exception e) {
					frame.consoleAppendText("E7: " + e.getMessage());
					tryToReconnectAndLog(ftp, true, frame);
				}
				return ret;
			}

			@Override
			public void done() {
				try {
					boolean[] canProceed = get();

					if (canProceed != null) {
						if (canProceed[0]) {
							frame.consoleAppendText("Result: canProceed is true");
							if (frame.timeKeeper != null) {
								frame.timeKeeper.stop();
								frame.timeKeeper = null;
							}
							if (!(screenCounter == CONNECTION_SCREEN)) {
								frame.incrementRoundCounter();
							}
							frame.switchScreen();
						} else {
							frame.consoleAppendText("Result: canProceed is false");
						}
					}
				} catch (InterruptedException ignore) {
					frame.consoleAppendText("E8: " + ignore.getMessage());
				} catch (java.util.concurrent.ExecutionException e) {
					String why = null;
					Throwable cause = e.getCause();
					if (cause != null) {
						why = cause.getMessage();
					} else {
						why = e.getMessage();
					}
					frame.consoleAppendText("E9: " + why);
				}
			}
		};
		worker.execute();
	}

	public static boolean tryToReconnectAndLog(JakartaFtpWrapper ftp,
			boolean changeToDefaultDir, AbstractGNRETFrame frame)
			throws UnknownHostException, FTPConnectionClosedException,
			SocketException, IOException {
		boolean ret = false;
		if (ret = ftp.tryToReconnectToFTP(changeToDefaultDir)) {
			frame.consoleAppendText("Reconnected: " + ftp.getReplyString());
		} else
			frame.consoleAppendText("Reconnect replied: "
					+ ftp.getReplyString());
		return ret;
	}

	public synchronized void decrementRoundCounter() {
		roundCounter--;
	}

	/***
	 * Returns random number between 30 and 60 seconds inclusive
	 * 
	 * @return
	 */
	public long getNextNoopCheckTime() {
		return TimeKeeper.getTimeInMilliseconds(0, 30 + randomizer.nextInt(61));
	}

	public int getRoundCounter() {
		return roundCounter;
	}

	// Fast forward testing time
	public synchronized void incrementRoundCounter() {
		roundCounter++;
	}

	public synchronized void setRoundCounter(int round) {
		roundCounter = round;
	}

	public void uploadLocalFileToServer(final AbstractGNRETFrame frame,
			final JakartaFtpWrapper ftp, final String fileName) {
		try {
			if (!ftp.isConnected()) {
				consoleAppendText("File Upload -- Disconnected, trying to reconnect");
				tryToReconnectAndLog(ftp, true, frame);
			}
			consoleAppendText("Trying to upload file: " + fileName
					+ " to the server");
			boolean uploaded = ftp.uploadFile(
					FileWritingHandler.getAbsolutePathToLocalFile(fileName),
					fileName);
			if (uploaded)
				consoleAppendText("Uploaded successfully");
		} catch (FTPConnectionClosedException ftpe) {
			frame.consoleAppendText("E11: " + ftpe.getMessage());
			try {
				ftp.disconnect();
			} catch (Exception e2) {
				frame.consoleAppendText("E12: " + e2.getMessage());
			}
			try {
				tryToReconnectAndLog(ftp, true, frame);
			} catch (Exception e3) {
				frame.consoleAppendText("E13: " + e3.getMessage());
			}
		} catch (Exception e) {
			String titleDescription = "Error while uploading data to the server";
			frame.consoleAppendText(titleDescription);
			frame.consoleAppendText("E14: " + e.getMessage());
			if (0 == Utils.showRetryCancelErrorDialog(frame, e.getMessage(),
					titleDescription)) {
				try {
					tryToReconnectAndLog(ftp, true, frame);
				} catch (Exception e4) {
					frame.consoleAppendText("E16: " + e4.getMessage());
				}
				uploadLocalFileToServer(frame, ftp, fileName);
			}
		}
	}
}
