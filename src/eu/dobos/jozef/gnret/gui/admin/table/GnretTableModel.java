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

package eu.dobos.jozef.gnret.gui.admin.table;

import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

import eu.dobos.jozef.gnret.gui.admin.rank.RankHandler;
import eu.dobos.jozef.gnret.gui.experiment.questionnaire.QuestionnairePanel;
import eu.dobos.jozef.gnret.gui.utils.Settings;

public class GnretTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -1282655065769210505L;

	public static final int FILE_NAME_COLUMN_INDEX = 0;
	public static final int DATE_COLUMN_INDEX = 1;
	public static final int TIME_COLUMN_INDEX = 2;
	public static final int ID_COLUMN_INDEX = 3;
	public static final int GROUP_COLUMN_INDEX = 4;
	public static final int IP_ADDRESS_COLUMN_INDEX = 5;
	public static final int MAC_ADDRESS_COLUMN_INDEX = 6;
	public static final int HOST_NAME_COLUMN_INDEX = 7;
	public static final int PARTICIPANT_STARTED_COLUMN_INDEX = 8;

	public static final int RANK_PRACTICE_COLUMN_INDEX = 9;
	public static final int RANK_PAYING_ONE_COLUMN_INDEX = 10;
	public static final int RANK_PAYING_TWO_COLUMN_INDEX = 11;
	public static final int RANK_PAYING_THREE_COLUMN_INDEX = 12;
	public static final int RANK_PAYING_FOUR_COLUMN_INDEX = 13;
	public static final int RANK_PAYING_FIVE_COLUMN_INDEX = 14;
	public static final int RANK_PAYING_SIX_COLUMN_INDEX = 15;

	// Practice Round
	public static final int PRACTICE_GRID_CORRECT_COLUMN_INDEX = 16;
	public static final int PRACTICE_GRID_WRONG_COLUMN_INDEX = 17;
	public static final int PRACTICE_NUMERICAL_CORRECT_COLUMN_INDEX = 18;
	public static final int PRACTICE_NUMERICAL_WRONG_COLUMN_INDEX = 19;
	public static final int PRACTICE_SUM_CORRECT_COLUMN_INDEX = 20;
	public static final int PRACTICE_SUM_WRONG_COLUMN_INDEX = 21;

	// Paying Round 1
	public static final int PAYING_ONE_GRID_CORRECT_COLUMN_INDEX = 22;
	public static final int PAYING_ONE_GRID_WRONG_COLUMN_INDEX = 23;
	public static final int PAYING_ONE_NUMERICAL_CORRECT_COLUMN_INDEX = 24;
	public static final int PAYING_ONE_NUMERICAL_WRONG_COLUMN_INDEX = 25;
	public static final int PAYING_ONE_SUM_CORRECT_COLUMN_INDEX = 26;
	public static final int PAYING_ONE_SUM_WRONG_COLUMN_INDEX = 27;

	// Paying Round 2
	public static final int PAYING_TWO_GRID_CORRECT_COLUMN_INDEX = 28;
	public static final int PAYING_TWO_GRID_WRONG_COLUMN_INDEX = 29;
	public static final int PAYING_TWO_NUMERICAL_CORRECT_COLUMN_INDEX = 30;
	public static final int PAYING_TWO_NUMERICAL_WRONG_COLUMN_INDEX = 31;
	public static final int PAYING_TWO_SUM_CORRECT_COLUMN_INDEX = 32;
	public static final int PAYING_TWO_SUM_WRONG_COLUMN_INDEX = 33;

	// Paying Round 3
	public static final int PAYING_THREE_GRID_CORRECT_COLUMN_INDEX = 34;
	public static final int PAYING_THREE_GRID_WRONG_COLUMN_INDEX = 35;
	public static final int PAYING_THREE_NUMERICAL_CORRECT_COLUMN_INDEX = 36;
	public static final int PAYING_THREE_NUMERICAL_WRONG_COLUMN_INDEX = 37;
	public static final int PAYING_THREE_SUM_CORRECT_COLUMN_INDEX = 38;
	public static final int PAYING_THREE_SUM_WRONG_COLUMN_INDEX = 39;

	// Paying Round 4
	public static final int PAYING_FOUR_GRID_CORRECT_COLUMN_INDEX = 40;
	public static final int PAYING_FOUR_GRID_WRONG_COLUMN_INDEX = 41;
	public static final int PAYING_FOUR_NUMERICAL_CORRECT_COLUMN_INDEX = 42;
	public static final int PAYING_FOUR_NUMERICAL_WRONG_COLUMN_INDEX = 43;
	public static final int PAYING_FOUR_SUM_CORRECT_COLUMN_INDEX = 44;
	public static final int PAYING_FOUR_SUM_WRONG_COLUMN_INDEX = 45;

	// Paying Round 5
	public static final int PAYING_FIVE_GRID_CORRECT_COLUMN_INDEX = 46;
	public static final int PAYING_FIVE_GRID_WRONG_COLUMN_INDEX = 47;
	public static final int PAYING_FIVE_NUMERICAL_CORRECT_COLUMN_INDEX = 48;
	public static final int PAYING_FIVE_NUMERICAL_WRONG_COLUMN_INDEX = 49;
	public static final int PAYING_FIVE_SUM_CORRECT_COLUMN_INDEX = 50;
	public static final int PAYING_FIVE_SUM_WRONG_COLUMN_INDEX = 51;

	// Paying Round 6
	public static final int PAYING_SIX_GRID_CORRECT_COLUMN_INDEX = 52;
	public static final int PAYING_SIX_GRID_WRONG_COLUMN_INDEX = 53;
	public static final int PAYING_SIX_NUMERICAL_CORRECT_COLUMN_INDEX = 54;
	public static final int PAYING_SIX_NUMERICAL_WRONG_COLUMN_INDEX = 55;
	public static final int PAYING_SIX_SUM_CORRECT_COLUMN_INDEX = 56;
	public static final int PAYING_SIX_SUM_WRONG_COLUMN_INDEX = 57;

	public static final int GENDER_COLUMN_INDEX = 58;
	public static final int AGE_COLUMN_INDEX = 59;	
	public static final int COUNTRY_OF_ORIGIN_COLUMN_INDEX = 60;
	public static final int COUNTRY_WHERE_YOU_GREW_UP_COLUMN_INDEX = 61;
	public static final int FIELD_OF_STUDY_COLUMN_INDEX = 62;
	public static final int VERBAL_TASK_COLUMN_INDEX = 63;
	public static final int NUMERICAL_TASK_COLUMN_INDEX = 64;
	public static final int DESCRIBE_YOURSELF_COLUMN_INDEX = 65;
	// public static final int TECHNICAL_PROBLEMS_COLUMN_INDEX = 64;
	public static final int OTHER_COMMENTS_COLUMN_INDEX = 66;

	// \u2713 is unicode tick symbol
	public static final String[] COLUMN_HEADER_NAMES = {
			"File name",
			"Date",
			"Time",
			"ID",
			"Group",
			"IP address",
			"MAC address",
			"Host name",
			"Started",

			// Rank
			"R-P",
			"R-1",
			"R-2",
			"R-3",
			"R-4",
			"R-5",
			"R-6",

			// Practice Round
			"P-G \u2713",
			"P-G X",
			"P-N \u2713",
			"P-N X",
			"P-S \u2713",
			"P-S X",

			// Paying Round 1
			"1-G \u2713",
			"1-G X",
			"1-N \u2713",
			"1-N X",
			"1-S \u2713",
			"1-S X",

			// Paying Round 2
			"2-G \u2713",
			"2-G X",
			"2-N \u2713",
			"2-N X",
			"2-S \u2713",
			"2-S X",

			// Paying Round 3
			"3-G \u2713",
			"3-G X",
			"3-N \u2713",
			"3-N X",
			"3-S \u2713",
			"3-S X",

			// Paying Round 4
			"4-G \u2713",
			"4-G X",
			"4-N \u2713",
			"4-N X",
			"4-S \u2713",
			"4-S X",

			// Paying Round 5
			"5-G \u2713", "5-G X",
			"5-N \u2713",
			"5-N X",
			"5-S \u2713",
			"5-S X",

			// Paying Round 6
			"6-G \u2713", "6-G X", "6-N \u2713", "6-N X", "6-S \u2713",
			"6-S X",

			QuestionnairePanel.GENDER,
			QuestionnairePanel.AGE, 
			QuestionnairePanel.COUNTRY_OF_ORIGIN,
			QuestionnairePanel.COUNTRY_WHERE_YOU_GREW_UP,
			QuestionnairePanel.FIELD_OF_STUDY,
			QuestionnairePanel.VERBAL_TASK, QuestionnairePanel.NUMERICAL_TASK,
			QuestionnairePanel.DESCRIBE_YOURSELF,
			// QuestionnairePanel.TECHNICAL_PROBLEMS,
			QuestionnairePanel.OTHER_COMMENTS

	};

	public static final String[] COLUMN_HEADER_TOOLTIPS = {
			"File name",
			"Date",
			"Time",
			"Participant's ID number",
			"Participant's group",
			"IP address",
			"MAC address",
			"Host name",
			"Has participant pressed start?",

			// Ranks
			"Rank for " + Settings.PRACTICE_ROUND,
			"Rank for " + Settings.PAYING_ROUND + " 1",
			"Rank for " + Settings.PAYING_ROUND + " 2",
			"Rank for " + Settings.PAYING_ROUND + " 3",
			"Rank for " + Settings.PAYING_ROUND + " 4",
			"Rank for " + Settings.PAYING_ROUND + " 5",
			"Rank for " + Settings.PAYING_ROUND + " 6",

			// Practice Round
			Settings.PRACTICE_ROUND + " WORD grid correct answers count",
			Settings.PRACTICE_ROUND + " WORD grid wrong answers count",
			Settings.PRACTICE_ROUND + " NUMERICAL correct answers count",
			Settings.PRACTICE_ROUND + " NUMERICAL grid wrong answers count",
			Settings.PRACTICE_ROUND + " correct answers SUM",
			Settings.PRACTICE_ROUND + " wrong answers SUM",

			// Paying Round 1
			Settings.PAYING_ROUND + " 1 WORD grid correct answers count",
			Settings.PAYING_ROUND + " 1 WORD grid wrong answers count",
			Settings.PAYING_ROUND + " 1 NUMERICAL correct answers count",
			Settings.PAYING_ROUND + " 1 NUMERICAL grid wrong answers count",
			Settings.PAYING_ROUND + " 1 correct answers SUM",
			Settings.PAYING_ROUND + " 1 wrong answers SUM",

			// Paying Round 2
			Settings.PAYING_ROUND + " 2 WORD grid correct answers count",
			Settings.PAYING_ROUND + " 2 WORD grid wrong answers count",
			Settings.PAYING_ROUND + " 2 NUMERICAL correct answers count",
			Settings.PAYING_ROUND + " 2 NUMERICAL grid wrong answers count",
			Settings.PAYING_ROUND + " 2 correct answers SUM",
			Settings.PAYING_ROUND + " 2 wrong answers SUM",

			// Paying Round 3
			Settings.PAYING_ROUND + " 3 WORD grid correct answers count",
			Settings.PAYING_ROUND + " 3 WORD grid wrong answers count",
			Settings.PAYING_ROUND + " 3 NUMERICAL correct answers count",
			Settings.PAYING_ROUND + " 3 NUMERICAL grid wrong answers count",
			Settings.PAYING_ROUND + " 3 correct answers SUM",
			Settings.PAYING_ROUND + " 3 wrong answers SUM",

			// Paying Round 4
			Settings.PAYING_ROUND + " 4 WORD grid correct answers count",
			Settings.PAYING_ROUND + " 4 WORD grid wrong answers count",
			Settings.PAYING_ROUND + " 4 NUMERICAL correct answers count",
			Settings.PAYING_ROUND + " 4 NUMERICAL grid wrong answers count",
			Settings.PAYING_ROUND + " 4 correct answers SUM",
			Settings.PAYING_ROUND + " 4 wrong answers SUM",

			// Paying Round 5
			Settings.PAYING_ROUND + " 5 WORD grid correct answers count",
			Settings.PAYING_ROUND + " 5 WORD grid wrong answers count",
			Settings.PAYING_ROUND + " 5 NUMERICAL correct answers count",
			Settings.PAYING_ROUND + " 5 NUMERICAL grid wrong answers count",
			Settings.PAYING_ROUND + " 5 correct answers SUM",
			Settings.PAYING_ROUND + " 5 wrong answers SUM",

			// Paying Round 6
			Settings.PAYING_ROUND + " 6 WORD grid correct answers count",
			Settings.PAYING_ROUND + " 6 WORD grid wrong answers count",
			Settings.PAYING_ROUND + " 6 NUMERICAL correct answers count",
			Settings.PAYING_ROUND + " 6 NUMERICAL grid wrong answers count",
			Settings.PAYING_ROUND + " 6 correct answers SUM",
			Settings.PAYING_ROUND + " 6 wrong answers SUM",

			QuestionnairePanel.GENDER,
			QuestionnairePanel.AGE, 
			QuestionnairePanel.COUNTRY_OF_ORIGIN,
			QuestionnairePanel.COUNTRY_WHERE_YOU_GREW_UP,
			QuestionnairePanel.FIELD_OF_STUDY,
			QuestionnairePanel.VERBAL_TASK, QuestionnairePanel.NUMERICAL_TASK,
			QuestionnairePanel.DESCRIBE_YOURSELF,
			// QuestionnairePanel.TECHNICAL_PROBLEMS,
			QuestionnairePanel.OTHER_COMMENTS
 };

	private LinkedList<Object[]> data = new LinkedList<Object[]>();
	public static final int FILENAME_NOT_LISTED = -1;

	public void addRow(String fileName, String fileContents) {
		String[] individualCellContents = fileContents
				.split(Settings.DELIMINETER);
		Object[] row = null;
		int index = getFileIndex(fileName);
		if (index == FILENAME_NOT_LISTED) {
			row = new Object[COLUMN_HEADER_NAMES.length];
			data.add(row);
		} else {
			row = data.get(index);
		}

		for (int i = 0; i < row.length; i++) {
			if (isNumericalColumn(i))
				row[i] = new Integer(Integer.MIN_VALUE);
			else
				row[i] = new String("");
		}

		row[0] = fileName;
		int counter = 1;
		for (String cellContent : individualCellContents) {
			// shift forwards to leave blank space for RANK columns
			if (counter == RANK_PRACTICE_COLUMN_INDEX)
				counter = PRACTICE_GRID_CORRECT_COLUMN_INDEX;

			row[counter] = getCellObject(cellContent, counter);
			counter++;
			if (counter == COLUMN_HEADER_NAMES.length)
				break;
		}
		fireTableDataChanged();
	}

	public void addRankColumns() {
		int whereToWriteIndex = RANK_PRACTICE_COLUMN_INDEX;
		for (int columnIndex = PRACTICE_SUM_CORRECT_COLUMN_INDEX; columnIndex <= PAYING_SIX_SUM_CORRECT_COLUMN_INDEX; columnIndex += PAYING_ONE_SUM_CORRECT_COLUMN_INDEX
				- PRACTICE_SUM_CORRECT_COLUMN_INDEX) {
			setColumn(whereToWriteIndex, new RankHandler(
					getColumn(columnIndex), columnIndex).getRankColumn());
			whereToWriteIndex++;
		}
		fireTableDataChanged();
	}

	private boolean isNumericalColumn(int index) {
		return (ID_COLUMN_INDEX == index
				|| (PRACTICE_GRID_CORRECT_COLUMN_INDEX <= index && index <= PAYING_SIX_SUM_WRONG_COLUMN_INDEX) || (RANK_PRACTICE_COLUMN_INDEX <= index && index <= RANK_PAYING_SIX_COLUMN_INDEX));
	}

	private Object getCellObject(String cellContent, int columnIndex) {
		Object cellObject = cellContent;
		if (isNumericalColumn(columnIndex)) {
			try {
				cellObject = new Integer(cellContent);
			} catch (NumberFormatException e) {
				cellObject = new Integer(Integer.MIN_VALUE);
			}
		}
		return cellObject;
	}

	/***
	 * Removes all data from the table
	 */
	public void removeAllData() {
		data = null;
		data = new LinkedList<Object[]>();
		fireTableDataChanged();
	}

	/***
	 * Returns -1 if fileName not present
	 * 
	 * @param fileName
	 * @return
	 */
	private int getFileIndex(String fileName) {
		int counter = 0;
		for (Object[] rows : data) {
			if (rows[0].equals(fileName)) {
				return counter;
			}
			counter++;
		}
		return FILENAME_NOT_LISTED;
	}

	public int getColumnCount() {
		return COLUMN_HEADER_NAMES.length;
	}

	public int getRowCount() {
		return data.size();
	}

	/***
	 * Returns an array of all objects from a given column
	 * 
	 * @param columnIndex
	 * @return
	 */
	public Object[] getColumn(int columnIndex) {
		Object[] column = new Object[getRowCount()];
		for (int row = 0; row < getRowCount(); row++) {
			column[row] = getValueAt(row, columnIndex);
		}
		return column;
	}

	public String getColumnName(int col) {
		return COLUMN_HEADER_NAMES[col];
	}

	public Object getValueAt(int row, int col) {
		if (data.size() != 0)
			return data.get(row)[col];
		else
			return null;
	}

	public Class<? extends Object> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		data.get(row)[col] = value;
	}

	public void setColumn(int columnIndex, Object[] newColumn) {
		int row = 0;
		for (Object o : newColumn) {
			setValueAt(o, row, columnIndex);
			row++;
		}
	}

	public String exportTable(String delimineter) {
		String ret = new String();
		for (String cht : COLUMN_HEADER_TOOLTIPS) {
			ret += "\"" + cht + "\"" + delimineter;
		}

		for (int row = 0; row < getRowCount(); row++) {
			ret += "\n";
			for (int col = 0; col < getColumnCount(); col++) {
				Object val = getValueAt(row, col);
				if (val.equals(Integer.MIN_VALUE)
						|| val.equals(Integer.MAX_VALUE)) 
					ret += "";
				else 
					ret += "\"" + val + "\"";
				ret += delimineter;
			}
		}

		return ret;
	}
}
