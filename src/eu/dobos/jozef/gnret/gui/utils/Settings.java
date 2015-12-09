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

package eu.dobos.jozef.gnret.gui.utils;

import java.awt.Color;

import javax.swing.border.EmptyBorder;

import eu.dobos.jozef.gnret.time.TimeKeeper;

public class Settings {
	public static final Color BACKGROUND_COLOR = new Color(239, 237, 223);
	public static final Color FOREGROUND_COLOR = Color.BLACK;
	public static final Color WRONG_COLOR = Color.RED;
	public static final Color CORRECT_COLOR = Color.GREEN;
	public static final Color INPUT_BACKGROUND_COLOR = Color.WHITE;
	public static final Color SELECTION_COLOR = new Color(51, 153, 255); // highlight
	public static final Color HIGHLIGHT_COLOR = new Color(255, 246, 0); // selection
	public static final EmptyBorder INSETS = new EmptyBorder(10, 10, 10, 10);
	public static final EmptyBorder INSETS_ONE_DOWN = new EmptyBorder(0, 10, 10, 10);
	public static final float FONT_SIZE_INCREASE = 16.0f;
	
	public static final int COMPONENT_SPACING = 10;
		
	public static final String PRACTICE_ROUND = "Practice Round";
	public static final String PAYING_ROUND = "Paying Round";
	public static final int NUMBER_OF_PAYING_ROUNDS = 6;
	public static final long INTER_CHECK_DELAY = TimeKeeper.FIVE_SECONDS; // 5 seconds
	public static final String START_EXPERIMENT = "Start experiment";
	
	public static final String SUBMIT = "Submit";
	public static final String DELETE = "\u2190";
	
	public static final String DELIMINETER = "\t";
	public static final String CSV_DELIMINETER = ",";
	public static final String CSV_FILE_EXTENSION = ".csv";
	public static final String TXT_FILE_EXTENSION = ".txt";
	
	public static final String ADMIN_GROUP = "Admin";
	public static final String CONTROL_GROUP = "C";
	public static final String T1_GROUP = "T1";
	public static final String T2_GROUP = "T2";
	public static final String T3_GROUP = "T3";

	
	public static final String CORRECT_COUNT_TEXT = "Current points score";
	public static final String REMAINING_TIME_TEXT = "Time remaining:";
	public static final String CURRENT_WORKING_DIRECTORY_TEXT = "Current working directory: ";
	
	
	public static final String DEFAULT_SERVER = "109.74.202.226";
	public static final String DEFAULT_USER_NAME = "zdenka";
	public static final String DEFAULT_PASSWORD = "19sasanka85";
	public static final String DEFAULT_PORT = "21";
	
	public static final String CESS_SERVER = "192.168.12.1";
	public static final String CESS_USER_NAME = "cess.ftp@cess.local";
	public static final String CESS_PASSWORD = "maclaren26!";
	public static final String CESS_PORT = "21";
	
	
	public static final String UNIX_FILE_SEPARATOR = "/";
	public static final String WINDOWS_FILE_SEPARATOR = "\\";
	public static final String DEFAULT_FOLDER = "gnret";
	public static final String LOG_FOLDER = "logs";
	public static final String LOG_FILE = "gnret.log";
	
	public static final boolean DEBUG = false;
	
	public static final Object[] RETRY_CANCEL_OPTIONS = { "Retry", "Cancel" };
	public static final Object[] YES_CANCEL_OPTIONS = { "Yes", "Cancel" };
	
	public static final long TASK_DURATION = TimeKeeper.THREE_MINUTES;
	public static final long RESULT_DURATION = TimeKeeper.FOUR_MINUTES;

	public static final long PROCEEDING_CHECK_DURATION = TimeKeeper.THREE_MINUTES;
}
