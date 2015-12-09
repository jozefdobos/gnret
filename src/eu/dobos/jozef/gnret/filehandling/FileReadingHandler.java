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

package eu.dobos.jozef.gnret.filehandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.StringTokenizer;
import java.util.Vector;

import eu.dobos.jozef.gnret.gui.utils.Settings;

public class FileReadingHandler {

	/*
	 * public FileReadingHandler() throws IOException { BufferedReader
	 * readbuffer = new BufferedReader(new FileReader( "myFile.txt")); String
	 * strRead; while ((strRead = readbuffer.readLine()) != null) { String
	 * splitarray[] = strRead.split("\t"); String firstentry = splitarray[0];
	 * String secondentry = splitarray[1]; System.out.println(firstentry + " " +
	 * secondentry); } }
	 */

	public static final String TABLE_ = "Table_";

	/***
	 * Reads text file where each entry is separated by a new line
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public Vector<String> readTxtFile(String fileName, String[] fstValues) throws IOException {
		Vector<String> ret = new Vector<String>();
		if (fstValues != null) {
			for (String str : fstValues)
				ret.add(str);
		}
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(fileName + Settings.TXT_FILE_EXTENSION);
		String s = convertStreamToString(is);

		StringTokenizer lines = new StringTokenizer(s, "\n");
		while (lines.hasMoreTokens()) {
			String word = lines.nextToken();
			ret.add(word);
		}
		return ret;
	}

	public char[][] readCsvTable(int roundNumber, int x, int y)
			throws IOException {
		char[][] charArray = new char[x][y];
		roundNumber++; // rounds start from 0, table numbers from 1
		InputStream is = this
				.getClass()
				.getClassLoader()
				.getResourceAsStream(
						TABLE_ + roundNumber + Settings.CSV_FILE_EXTENSION);
		String s = convertStreamToString(is);
		StringTokenizer lines = new StringTokenizer(s, "\n");
		int i = 0;
		while (lines.hasMoreTokens() && i < x) {
			StringTokenizer chars = new StringTokenizer(lines.nextToken(), ",");
			int j = 0;
			while (chars.hasMoreTokens() && j < y) {
				charArray[j][i] = chars.nextToken().charAt(0);
				j++;
			}
			i++;
		}
		return charArray;
	}

	/*
	 * public static String[] convertStreamToStringArray(InputStream is) throws
	 * IOException { String strRead = new String(); if (is != null) {
	 * BufferedReader readbuffer = new BufferedReader( new InputStreamReader(is,
	 * "UTF-8"));
	 * 
	 * while ((strRead = readbuffer.readLine()) != null) { String splitarray[] =
	 * strRead.split(Settings.DELIMINETER); String firstentry = splitarray[0];
	 * String secondentry = splitarray[1]; System.out.println(firstentry + " " +
	 * secondentry); } } return strRead; }
	 */

	public static String convertStreamToString(InputStream is)
			throws IOException {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return new String();
		}
	}
}
