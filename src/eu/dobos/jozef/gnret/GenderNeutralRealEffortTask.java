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

package eu.dobos.jozef.gnret;

import javax.swing.UIManager;

import eu.dobos.jozef.gnret.gui.FtpView;

public class GenderNeutralRealEffortTask {
	// Info
	public static final String NAME = "Gender-Neutral Real-Effort Task";
	public static final String ACRONYM = "GNRET";
	public static final String VERSION = "0.7.4";
	public static final String AUTHOR = "Jozef Doboš";
	public static final String DESIGNER = "Zdenka Kissová";
	public static final String LAST_MODIFICATION = "28/02/2011";

	private static void createAndShowGUI() {
		//new ExperimentView();
		new FtpView();
	}	

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
