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

package eu.dobos.jozef.gnret.gui.admin.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class GnretMenuBar extends JMenuBar {
	private static final long serialVersionUID = 8913210479511587791L;

	public GnretMenuBar(GnretActionListener listener) {
		super();

		// so that the menu can be on top of Java3D Canvas
		this.setDoubleBuffered(true);

		this.setPreferredSize(new Dimension(200, 20));

		// FILE
		JMenu menu = new JMenu(GnretActionListener.FILE);
		menu.setMnemonic(KeyEvent.VK_F);
		this.add(menu);
		JMenuItem menuItem = null;
		
		// Save As...
		menuItem = getMenuItem(GnretActionListener.SAVE, KeyEvent.VK_S,
				GnretActionListener.SAVE_IMAGE, listener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		

		// Print
		menuItem = getMenuItem(GnretActionListener.PRINT, KeyEvent.VK_P,
				GnretActionListener.PRINT_IMAGE, listener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menu.add(new JSeparator());

		// Exit
		menuItem = getMenuItem(GnretActionListener.EXIT, KeyEvent.VK_F4,
				GnretActionListener.EXIT_IMAGE, listener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				ActionEvent.ALT_MASK));
		menu.add(menuItem);

		// SERVER
		menu = new JMenu(GnretActionListener.SERVER);
		menu.setMnemonic(KeyEvent.VK_E);
		this.add(menu);

		// Refresh
		menuItem = getMenuItem(GnretActionListener.REFRESH, KeyEvent.VK_R,
				GnretActionListener.REFRESH_IMAGE, listener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.ALT_MASK));
		menu.add(menuItem);
		
		// Select All Files
		menuItem = getMenuItem(GnretActionListener.SELECT_ALL, KeyEvent.VK_A,
				GnretActionListener.SELECT_ALL_IMAGE, listener);
		menu.add(menuItem);

		// Reconnect
		menuItem = getMenuItem(GnretActionListener.RECONNECT, KeyEvent.VK_C,
				GnretActionListener.RECONNECT_IMAGE, listener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menu.add(new JSeparator());

		// CLEAR SESSION
		menuItem = getMenuItem(GnretActionListener.CLEAR_SESSION,
				KeyEvent.VK_L, GnretActionListener.REMOVE_IMAGE, listener);
		menu.add(menuItem);
		
		// DELETE SELECTED
		menuItem = GnretMenuBar.getMenuItem(GnretActionListener.DELETE_SELECTED, KeyEvent.VK_D, GnretActionListener.DELETE_IMAGE, listener);
		menu.add(menuItem);
		
		
		// HELP
		menu = new JMenu(GnretActionListener.HELP);
		menu.setMnemonic(KeyEvent.VK_H);
		this.add(menu);

		// About
		menuItem = getMenuItem(GnretActionListener.ABOUT, KeyEvent.VK_A,
				GnretActionListener.ABOUT_IMAGE, listener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));
		menu.add(menuItem);

	}

	public static JMenuItem getMenuItem(String text, int keyEvent,
			String image, GnretActionListener listener) {
		JMenuItem menuItem = new JMenuItem(text, keyEvent);
		menuItem.getAccessibleContext().setAccessibleDescription(text);
		menuItem.setActionCommand(text);
		menuItem.addActionListener(listener);
		menuItem.setIcon(GnretToolbar.getImageIconGeneral(image
				+ GnretActionListener.SMALL_END, GnretActionListener.EXIT));

		return menuItem;
	}

}
