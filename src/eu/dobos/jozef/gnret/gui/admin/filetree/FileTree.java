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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import org.apache.commons.net.ftp.FTPConnectionClosedException;

import eu.dobos.jozef.gnret.communication.CurrentLevelHierarchy;
import eu.dobos.jozef.gnret.communication.JakartaFtpWrapper;
import eu.dobos.jozef.gnret.gui.admin.AdminView;
import eu.dobos.jozef.gnret.gui.admin.actions.GnretActionListener;
import eu.dobos.jozef.gnret.gui.admin.actions.GnretPopupMenu;
import eu.dobos.jozef.gnret.gui.admin.table.GnretTableModel;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;

public class FileTree extends JPanel implements TreeWillExpandListener,
		TreeExpansionListener, TreeSelectionListener {

	private static final long serialVersionUID = -6138462165194958322L;
	private JTree tree;
	private JakartaFtpWrapper ftp;
	private AdminView frame;
	private CurrentLevelHierarchy topLevelHierarchy;
	public static final int MAX_ALLOWED_TREE_EXPANSION_ATTEMPTS = 5;
	public static final String DUMMY_FILE_NAME = " ..";
	private DefaultMutableTreeNode rootNode;
	private GnretPopupMenu popupMenu;

	public FileTree(AdminView frame, JakartaFtpWrapper ftp,
			GnretActionListener actionListener) {
		super();
		this.ftp = ftp;
		this.frame = frame;

		this.popupMenu = new GnretPopupMenu(actionListener);

		tree = new JTree();
		// Enable tool tips.
		ToolTipManager.sharedInstance().registerComponent(tree);
		reloadMainTreeStructure();
		tree.addTreeExpansionListener(this);
		tree.addTreeWillExpandListener(this);
		tree.addTreeSelectionListener(this);

		popupMenu.registerPopupMenu(tree);

		tree.setCellRenderer(new GnretTreeCellRenderer());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(tree);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Explorer"));
		this.add(scrollPane, BorderLayout.CENTER);
	}

	private void changeToPath(TreePath path) throws IOException {
		if (path != null) {
			Object[] pathElements = path.getPath();
			ftp.changeToRootDir();
			if (pathElements.length >= 2) {
				for (int i = 1; i < pathElements.length; i++) {
					ftp.changeWorkingDirectory(pathElements[i].toString());
				}
			}
		}
	}

	private boolean isPathEqual(TreePath currentPath, TreePath prevPath) {
		boolean ret = false;
		if (currentPath != null && prevPath != null) {
			Object[] currentPathComponents = currentPath.getPath();
			Object[] prevPathComponents = prevPath.getPath();
			if (currentPathComponents.length == prevPathComponents.length
					&& currentPathComponents.length > 1) {
				int differences = 0;
				for (int i = 0; i < currentPathComponents.length - 1; i++) { // all
																				// but
																				// last
					if (!currentPathComponents[i].equals(prevPathComponents[i]))
						differences++;
				}
				ret = differences == 0;
			}
		}
		return ret;
	}

	private void createNodes(DefaultMutableTreeNode parentNode,
			CurrentLevelHierarchy currentLevelHierarchy) {
		DefaultMutableTreeNode directory = null;
		DefaultMutableTreeNode file = null;

		// if (parentNode != null && parentNode.getFirstChild() != null &&
		// parentNode.getFirstChild().toString().equals("..")) {
		// parentNode.removeAllChildren(); // clear first
		// }

		for (String subdirs : currentLevelHierarchy.getSubdirNames()) {
			directory = new DefaultMutableTreeNode(subdirs);
			directory.add(new DefaultMutableTreeNode(DUMMY_FILE_NAME)); // dummy
			parentNode.add(directory);
		}

		for (String f : currentLevelHierarchy.getFileNames()) {
			file = new DefaultMutableTreeNode(f);
			parentNode.add(file);
		}
	}

	public void deleteSelected() {
		TreePath[] allSelectedPaths = tree.getSelectionPaths();
		if (allSelectedPaths != null) {

			TreePath prevPath = null;
			for (TreePath selectedPath : allSelectedPaths) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedPath
						.getLastPathComponent();
				if (node.isLeaf()) { // file
					String fileName = node.toString();
					if (!fileName.equals(DUMMY_FILE_NAME)) {
						try {

							if (!isPathEqual(selectedPath, prevPath)) {
								changeToPath(selectedPath);
							}
							prevPath = selectedPath;

							if (ftp.listFileNames().contains(fileName)) {
								ftp.deleteFile(fileName);
							} else {
								frame
										.consoleAppendText("Selected file '"
												+ fileName
												+ "' does not exist in current working directory.");
							}
						} catch (Exception e2) {
							processError(e2);
						}
					} else {
						String dirName = node.toString();
						try {
							changeToPath(selectedPath);
							if (ftp.listSubdirNames().contains(dirName)) {
								ftp.removeDirectory(dirName);
							}
						} catch (Exception e) {
							processError(e);
						}
						break;
					}
				}

			}
		}
		frame.consoleAppendText("Deleting finished, server replied: "
				+ ftp.getReplyString());
	}

	public HashMap<String, String> loadFiles(boolean loadToTable) {
		HashMap<String, String> files = new HashMap<String, String>();
		TreePath[] allSelectedPaths = tree.getSelectionPaths();
		if (allSelectedPaths != null) {
			frame.getDataTable().removeAllData();

			TreePath prevPath = null;

			for (TreePath selectedPath : allSelectedPaths) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedPath
						.getLastPathComponent();
				if (node.isLeaf()) { // file
					String fileName = node.toString();
					if (!fileName.equals(DUMMY_FILE_NAME)) {
						if (Utils.isValidFileType(fileName)) {
							try {

								if (!isPathEqual(selectedPath, prevPath)) {
									changeToPath(selectedPath);
								}
								prevPath = selectedPath;

								if (ftp.listFileNames().contains(fileName)) {

									String fileContent = ftp
											.getFileAsString(fileName);

									files.put(fileName, fileContent);
									if (loadToTable)
										frame.getDataTable().addRow(fileName,
												fileContent);

								} else {
									frame
											.consoleAppendText("Selected file '"
													+ fileName
													+ "' does not exist in current working directory.");
								}
							} catch (IOException e2) {
								processError(e2);
							}
						} else {
							frame
									.consoleAppendText("Invalid file: "
											+ fileName);
						}
					} else { // folder
						break;
					}
				}

			}
		}

		if (!files.isEmpty()) {
			GnretTableModel tableModel = (GnretTableModel) frame.getDataTable()
					.getTable().getModel();
			tableModel.addRankColumns();
		}
		return files;
	}

	private void processError(Exception e2) {
		frame.consoleAppendText("Error: " + e2.getMessage());
		if (e2 instanceof FTPConnectionClosedException) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			frame.reconnect();
		}
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public void refresh() throws ExpandVetoException, IOException {
		ftp.changeToRootDir();
		reloadMainTreeStructure();
		for (int i = 0; i < tree.getRowCount(); i++) {
			TreePath path = tree.getPathForRow(i);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (node.toString().equals(Settings.DEFAULT_FOLDER)) {
				tree.expandRow(i);
				break;
			}
		}
		tree.invalidate();
		tree.repaint();
		frame.consoleAppendText("File structure refreshed");
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void reloadMainTreeStructure() {
		topLevelHierarchy = new CurrentLevelHierarchy();
		try {
			topLevelHierarchy.populate(ftp);
			frame.consoleAppendText(Settings.CURRENT_WORKING_DIRECTORY_TEXT
					+ topLevelHierarchy.getWorkingDirectory());
		} catch (IOException e) {
			processError(e);
		} finally {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		rootNode = null; // reset
		rootNode = new DefaultMutableTreeNode(topLevelHierarchy
				.getWorkingDirectory());
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		createNodes(rootNode, topLevelHierarchy);
		model.setRoot(rootNode);
	}

	public void selectAll() {
		Vector<Integer> selRows = new Vector<Integer>();
		for (int i = 0; i < tree.getRowCount(); i++) {
			TreePath path = tree.getPathForRow(i);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (Utils.isValidFileType(node.toString())) {
				selRows.add(i);
			}
		}
		if (selRows.size() > 0) {
			int[] selection = new int[selRows.size()];
			for (int i = 0; i < selection.length; i++) {
				selection[i] = selRows.get(i);
			}
			tree.setSelectionRows(selection);
		} else {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		tree.invalidate();
		tree.repaint();

	}

	@Override
	public void treeCollapsed(TreeExpansionEvent arg0) {

	}

	@Override
	public void treeExpanded(TreeExpansionEvent arg0) {

	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event)
			throws ExpandVetoException {

	}

	@Override
	public void treeWillExpand(TreeExpansionEvent event)
			throws ExpandVetoException {
		// String dir = event.getPath().getLastPathComponent().toString();
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		TreePath path = event.getPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		if (!node.isLeaf()) {
			try {
				changeToPath(path);
				CurrentLevelHierarchy currentLevelHierarchy = new CurrentLevelHierarchy();
				currentLevelHierarchy.populate(ftp);
				createNodes(node, currentLevelHierarchy);
			} catch (IOException e) {
				processError(e);
			}
		}
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				loadFiles(true);
				return null;
			}

			@Override
			public void done() {
				frame.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		};
		worker.execute();
	}
}
