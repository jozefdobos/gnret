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

import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import eu.dobos.jozef.gnret.gui.utils.Settings;

public class GnretToolbar extends JToolBar {

	private static final long serialVersionUID = -4784519808970985828L;
	private GnretActionListener listener;
	
	private JButton startButton;

	public GnretToolbar(GnretActionListener listener) {
		super();
		this.listener = listener;
		this.setFloatable(false);
		this.setRollover(true);
		// this.setPreferredSize(new Dimension(100, 24));

		addButtons();
	}

	private void addButtons() {
		// START
		startButton = makeNavigationButton(GnretActionListener.START_IMAGE
				+ GnretActionListener.BIG_END, Settings.START_EXPERIMENT, false);
		this.add(startButton);

		this.addSeparator();		
		JToggleButton toggleButton = makeToggleButton(GnretActionListener.ENABLE_ROUND_IMAGE 
				+ GnretActionListener.BIG_END, 
				Settings.PRACTICE_ROUND, true);
		this.add(toggleButton);
		
		for (int i = 1; i <= Settings.NUMBER_OF_PAYING_ROUNDS; i++) {
			toggleButton = makeToggleButton(GnretActionListener.ENABLE_ROUND_IMAGE 
					+ GnretActionListener.BIG_END, 
					Settings.PAYING_ROUND + " " + i, true);
			this.add(toggleButton);
		}
		/*
		// PAUSE
		pauseButton = new JToggleButton(
				GnretActionListener.PAUSE, getImageIconMedia(
						GnretActionListener.PAUSE_IMAGE
								+ GnretActionListener.BIG_END,
						GnretActionListener.PAUSE));
		pauseButton.setActionCommand(GnretActionListener.PAUSE);
		pauseButton.setToolTipText(GnretActionListener.PAUSE);
		pauseButton.addActionListener(listener);
		pauseButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		pauseButton.setHorizontalTextPosition(AbstractButton.CENTER);
		pauseButton.setEnabled(false);
		this.add(pauseButton);
		listener.registerPauseButton(pauseButton);
		*/
		
		// separator
		this.addSeparator();
		
		// REFRESH
		JButton button = null;
		button = makeNavigationButton(GnretActionListener.REFRESH_IMAGE
				+ GnretActionListener.BIG_END, GnretActionListener.REFRESH,
				true);
		this.add(button);
		
		button = makeNavigationButton(GnretActionListener.SELECT_ALL_IMAGE
				+ GnretActionListener.BIG_END, GnretActionListener.SELECT_ALL,
				true);
		this.add(button);
		
		// RECONNECT
		button = makeNavigationButton(GnretActionListener.RECONNECT_IMAGE
				+ GnretActionListener.BIG_END, GnretActionListener.RECONNECT,
				true);
		this.add(button);
		
		// separator
		this.addSeparator();
		
		// CLEAR SESSIONS
		button = makeNavigationButton(GnretActionListener.REMOVE_IMAGE
				+ GnretActionListener.BIG_END, GnretActionListener.CLEAR_SESSION,
				true);
		this.add(button);
		
		// DELETE SELECTED 
		button = makeNavigationButton(GnretActionListener.DELETE_IMAGE + GnretActionListener.BIG_END, GnretActionListener.DELETE_SELECTED, true);
		this.add(button);
		
		// separator
		this.addSeparator();
		/*
		// RANK
		button = makeNavigationButton(GnretActionListener.RANK_IMAGE
				+ GnretActionListener.BIG_END, GnretActionListener.RANK,
				true);
		this.add(button);*/
		
		// PRINT
		button = makeNavigationButton(GnretActionListener.PRINT_IMAGE
				+ GnretActionListener.BIG_END, GnretActionListener.PRINT,
				true);
		this.add(button);
	}

	private JButton makeNavigationButton(String imageName, String text,
			boolean isGeneral) {
		// Create and initialize the button.
		JButton button = null;
		if (isGeneral)
			button = new JButton(text, getImageIconGeneral(imageName, text));
		else
			button = new JButton(text, getImageIconMedia(imageName, text));
		button.setActionCommand(text);
		button.setToolTipText(text);
		button.addActionListener(listener);
		button.setVerticalTextPosition(AbstractButton.BOTTOM);
		button.setHorizontalTextPosition(AbstractButton.CENTER);
		button.setFocusable(false);
		return button;
	}
	
	private JToggleButton makeToggleButton(String imageName, String text, boolean isGeneral) {
		// Create and initialize the button.
		JToggleButton button = null;
		if (isGeneral)
			button = new JToggleButton(text, getImageIconGeneral(imageName, text));
		else
			button = new JToggleButton(text, getImageIconMedia(imageName, text));
		button.setActionCommand(text);
		button.setToolTipText(text);
		button.addActionListener(listener);
		button.setVerticalTextPosition(AbstractButton.BOTTOM);
		button.setHorizontalTextPosition(AbstractButton.CENTER);
		button.setFocusable(false);
		return button;
	}

	public static ImageIcon getImageIcon(String imageName, String altText) {
		// Look for the image.
		String imgLocation = imageName;
		java.net.URL imageURL = GnretToolbar.class.getResource(imgLocation);
		if (imageURL != null) { // image found
			new ImageIcon(imageURL, altText);
		} else { // no image found
			// button.setText(altText);
			System.err.println("Resource not found: " + imgLocation);
		}
		return new ImageIcon(imageURL, altText);
	}

	public static ImageIcon getImageIconGeneral(String imageName, String altText) {
		return getImageIcon("/toolbarButtonGraphics/general/" + imageName
				+ ".gif", altText);
	}

	public static ImageIcon getImageIconMedia(String imageName, String altText) {
		return getImageIcon("/toolbarButtonGraphics/media/" + imageName
				+ ".gif", altText);
	}
	
	public void setStartEnabled(boolean on) {
		startButton.setEnabled(on);
	}
	
	public synchronized void setToggleButtonsSelected(Vector<String> subdirNames) {
		for (Object o : this.getComponents()) {			
			if (o instanceof JToggleButton) {
				JToggleButton b = (JToggleButton)o;
				b.setSelected(subdirNames.contains(b.getText()));
				
				if (b.isSelected()) 
					b.setIcon(GnretToolbar.getImageIconGeneral(GnretActionListener.DISABLE_ROUND_IMAGE
							+ GnretActionListener.BIG_END, b.getText()));
				else 
					b.setIcon(GnretToolbar.getImageIconGeneral(GnretActionListener.ENABLE_ROUND_IMAGE
							+ GnretActionListener.BIG_END, b.getText()));
			}
		}
	}
}
