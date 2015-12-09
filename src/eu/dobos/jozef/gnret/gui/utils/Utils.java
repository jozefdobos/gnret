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

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/***
 * Class essential for all GUI components
 * 
 * @author Jozef Doboš
 * 
 */
public class Utils {

	/***
	 * Returns a new derived font with the size of [the original font +
	 * sizeIncrease].
	 * 
	 * @param f
	 * @param sizeIncrease
	 * @return
	 */
	public static Font getIncreasedFont(Font f, float sizeIncrease) {
		return f.deriveFont(f.getSize2D() + sizeIncrease);
	}

	/***
	 * Returns a new derived font with the specified size.
	 * 
	 * @param f
	 * @param newFontSize
	 * @return
	 */
	public static Font getResizedFont(Font f, float newFontSize) {
		return f.deriveFont(newFontSize);
	}

	public static String getCurrentRoundDescriptor(int round) {
		assert (round >= 0);
		String roundText = "";
		if (round == 0)
			roundText = Settings.PRACTICE_ROUND;
		else
			roundText = Settings.PAYING_ROUND + " " + round;
		return roundText;
	}

	public static TitledBorder getTitledBorder(JComponent caller, String title) {
		Font f = Utils.getIncreasedFont(caller.getFont(),
				Settings.FONT_SIZE_INCREASE);

		Border loweredetched = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		// Border loweredbevel = BorderFactory.createLoweredBevelBorder();

		TitledBorder ret = BorderFactory.createTitledBorder(loweredetched,
				title);
		ret.setTitleJustification(TitledBorder.CENTER);
		ret.setTitleFont(f);
		return ret;
	}

	public static String getCurrentDate() {
		return getFormattedDateTime("yyyy-MM-dd");
	}

	public static String getCurrentTime() {
		return getFormattedDateTime("HH:mm:ss");
	}

	public static String getFormattedDateTime(String format) {
		final String DATE_FORMAT_NOW = format;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public static String getFileName(String hostName, String fileExtension) {
		return hostName + "." + fileExtension;
	}
	
	/***
	 * Returns fileName in format: hostName_0000filesCount.fileExtension 
	 * 
	 * @param hostName
	 * @param filesCount
	 * @param fileExtension
	 * @return
	 */
	public static String getFileName(String hostName, int filesCount, String fileExtension) {
		return String.format(hostName + "_%05d." + fileExtension, filesCount);
	}
	
	public static String getFileName(int participantsNumber,
			String fileExtension) {
		return String.format("%05d." + fileExtension, participantsNumber);
	}

	public static int showRetryCancelErrorDialog(JFrame parentComponent,
			String errorMessage, String titleDescription) {
		if (titleDescription.isEmpty()) {
			titleDescription = "Error";
		}
		String message = "There has been a technical problem, please raise your hand immediatelly.\nError: "
				+ errorMessage;
		int n = JOptionPane.showOptionDialog(parentComponent, message,
				titleDescription, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, UIManager
						.getIcon("OptionPane.errorIcon"),
				Settings.RETRY_CANCEL_OPTIONS, // the titles of buttons
				Settings.RETRY_CANCEL_OPTIONS[0]); // default button title
		return n;
	}
	

	/*
	 * public static ImageIcon getImageIcon(String imageName, String altText) {
	 * // Look for the image. String imgLocation =
	 * "/toolbarButtonGraphics/general/" + imageName + ".gif"; java.net.URL
	 * imageURL = this.getClass().getClassLoader().getResource(imgLocation); if
	 * (imageURL != null) { // image found new ImageIcon(imageURL, altText); }
	 * else { // no image found //button.setText(altText);
	 * System.err.println("Resource not found: " + imgLocation); }
	 * 
	 * return new ImageIcon(imageURL, altText); }
	 */
	
	public static boolean isValidFileType(String fileName) {
		return (fileName.indexOf("." + Settings.CONTROL_GROUP) >= 0
				|| fileName.indexOf("." + Settings.T1_GROUP) >= 0
				|| fileName.indexOf("." + Settings.T2_GROUP) >= 0
				|| fileName.indexOf("." + Settings.T3_GROUP) >= 0
				|| fileName.indexOf("."
						+ Settings.CONTROL_GROUP.toLowerCase()) >= 0
				|| fileName.indexOf("." + Settings.T1_GROUP.toLowerCase()) >= 0
				|| fileName.indexOf("." + Settings.T2_GROUP.toLowerCase()) >= 0
				|| fileName.indexOf("." + Settings.T3_GROUP.toLowerCase()) >= 0);
	}
}
