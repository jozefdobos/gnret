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

package eu.dobos.jozef.gnret.gui.admin.filetree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import eu.dobos.jozef.gnret.gui.admin.actions.GnretActionListener;
import eu.dobos.jozef.gnret.gui.admin.actions.GnretToolbar;
import eu.dobos.jozef.gnret.gui.utils.Utils;

public class GnretTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -1518234355509877391L;
	public static final String VALID_FILE = "Valid file";
	private Icon validFileIcon;
	private Icon standardFileIcon;

	public GnretTreeCellRenderer() {
		validFileIcon = GnretToolbar.getImageIconGeneral(
				GnretActionListener.VALID_FILE_IMAGE
						+ GnretActionListener.SMALL_END, VALID_FILE);

		UIDefaults defaults = UIManager.getDefaults();
		// Icon newFolderIcon = defaults.getIcon( "FileChooser.newFolderIcon" );
		// Icon upFolderIcon = defaults.getIcon( "FileChooser.upFolderIcon" );
		// Icon homeFolderIcon = defaults.getIcon(
		// "FileChooser.homeFolderIcon");
		// Icon folderIcon = defaults.getIcon( "FileView.directoryIcon" );
		standardFileIcon = defaults.getIcon("FileView.fileIcon");
		/*
		 * if (fileIcon != null) { DefaultTreeCellRenderer renderer = new
		 * DefaultTreeCellRenderer(); renderer.setLeafIcon(fileIcon);
		 * tree.setCellRenderer(renderer); }
		 */
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		if (leaf) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			String fileName = node.toString();
			if (Utils.isValidFileType(fileName)) {
				setIcon(validFileIcon);
				setToolTipText(VALID_FILE);
			} else if (fileName.indexOf(FileTree.DUMMY_FILE_NAME) >= 0) {
				setIcon(null);
				setToolTipText(null);
			} else {
				setToolTipText("Invalid file"); // no tool tip
				setIcon(standardFileIcon);
			}
		}

		return this;

	}

}
