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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import eu.dobos.jozef.gnret.gui.utils.Settings;

public class FileWritingHandler {

	public static final JFileChooser fr = new JFileChooser();
	public static final FileSystemView fw = fr.getFileSystemView();

	public static void createInitialFile(final String fileName,
			final String text) throws IOException {
		// create working directory if not set already
		(new File(getLocalDefaultDirectory())).mkdir();
		appendToFile(fileName, text, false);
	}

	/***
	 * @deprecated
	 * @param directory
	 * @param group
	 * @return
	 * @throws IOException
	 */
	public static int getFilesCountInDirectory(String directory, String group)
			throws IOException {

		// create the dir if not already present
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		int counter = 1;
		File[] files = dir.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isFile()) {
					String s = f.getName();
					if (s.endsWith("." + group.toLowerCase())
							|| s.endsWith("." + group.toUpperCase())) {
						counter++;
					}
				}
			}
		}
		return counter;
	}

	public static void saveToCSVFile(final String absolutePath,
			final String fileName, final String text) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath
				+ File.separator + fileName, false));
		writer.write(text);
		writer.close();
	}

	public static void appendToFile(final String fileName, final String text,
			final boolean append) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				getAbsolutePathToLocalFile(fileName), append));
		writer.write(text + Settings.DELIMINETER);
		writer.close();
	}

	public static String getAbsolutePathToLocalFile(final String fileName)
			throws IOException {
		return getLocalDefaultDirectory() + File.separator + fileName;
	}

	/***
	 * Returns absolute canonical path to gnret directory on the local
	 * filesystem
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getLocalDefaultDirectory() throws IOException {
		return fw.getDefaultDirectory().getCanonicalPath() + File.separator
				+ Settings.DEFAULT_FOLDER;
	}

	/***
	 * @deprecated
	 * @return
	 * @throws IOException
	 */
	public static String getLocalDefaultLogDirectory() throws IOException {
		return fw.getDefaultDirectory().getCanonicalPath() + File.separator
				+ Settings.DEFAULT_FOLDER + File.separator
				+ Settings.LOG_FOLDER;
	}
}
