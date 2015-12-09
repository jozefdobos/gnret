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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

public class GnretPopupMenu extends MouseAdapter {

	private JPopupMenu popup;
	public GnretPopupMenu(GnretActionListener actionListener) {
	
		//...where the GUI is constructed:
    //Create the popup menu.
    popup = new JPopupMenu();
    
    // Refresh
	JMenuItem menuItem = GnretMenuBar.getMenuItem(GnretActionListener.REFRESH, KeyEvent.VK_R, GnretActionListener.REFRESH_IMAGE, actionListener);
	menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
			ActionEvent.ALT_MASK));
    popup.add(menuItem);
    
	// SELECT ALL
	menuItem = GnretMenuBar.getMenuItem(GnretActionListener.SELECT_ALL, KeyEvent.VK_A,
			GnretActionListener.SELECT_ALL_IMAGE, actionListener);
	popup.add(menuItem);
    
	// Reconnect
	menuItem = GnretMenuBar.getMenuItem(GnretActionListener.RECONNECT, KeyEvent.VK_E,
			GnretActionListener.RECONNECT_IMAGE, actionListener);
	menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
			ActionEvent.CTRL_MASK));
	popup.add(menuItem);
    
	
	// SEPARATOR
    popup.addSeparator();
    
    // CLEAR SESSION
	menuItem = GnretMenuBar.getMenuItem(GnretActionListener.CLEAR_SESSION,
			KeyEvent.VK_L, GnretActionListener.REMOVE_IMAGE, actionListener);
	popup.add(menuItem);
    
    // DELETE SELECTED
    menuItem = GnretMenuBar.getMenuItem(GnretActionListener.DELETE_SELECTED, KeyEvent.VK_D, GnretActionListener.DELETE_IMAGE, actionListener);    
    popup.add(menuItem);   
    
    
	}
	
	public void registerPopupMenu(JComponent component) {
		component.addMouseListener(this);
	}

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }

}
