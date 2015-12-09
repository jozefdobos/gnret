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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class DataTable extends JPanel implements AdjustmentListener {

	private static final long serialVersionUID = -4294089249957663168L;
	private GnretTableModel tableModel = new GnretTableModel();
	private JTable table;
	private JScrollPane scrollPane = new JScrollPane();

	public DataTable() {
		super();
		table = new JTable(tableModel) {
			private static final long serialVersionUID = -63036402075229334L;

			public Component prepareRenderer(TableCellRenderer renderer,
					int rowIndex, int vColIndex) {
				Component c = super.prepareRenderer(renderer, rowIndex,
						vColIndex);

				if (!isCellSelected(rowIndex, vColIndex)) {
					c.setForeground(getForeground());
					c.setBackground(getBackground());
					switch (vColIndex) {
					case GnretTableModel.FILE_NAME_COLUMN_INDEX:
					case GnretTableModel.IP_ADDRESS_COLUMN_INDEX:
					case GnretTableModel.MAC_ADDRESS_COLUMN_INDEX:
						c.setForeground(Color.gray);
						break;
					case GnretTableModel.PRACTICE_GRID_CORRECT_COLUMN_INDEX:
					case GnretTableModel.PRACTICE_NUMERICAL_CORRECT_COLUMN_INDEX:
						
					case GnretTableModel.PAYING_ONE_GRID_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_ONE_NUMERICAL_CORRECT_COLUMN_INDEX :
						
					case GnretTableModel.PAYING_TWO_GRID_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_TWO_NUMERICAL_CORRECT_COLUMN_INDEX :
						
					case GnretTableModel.PAYING_THREE_GRID_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_THREE_NUMERICAL_CORRECT_COLUMN_INDEX :
						
					case GnretTableModel.PAYING_FOUR_GRID_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_FOUR_NUMERICAL_CORRECT_COLUMN_INDEX :
						
					case GnretTableModel.PAYING_FIVE_GRID_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_FIVE_NUMERICAL_CORRECT_COLUMN_INDEX :
						
					case GnretTableModel.PAYING_SIX_GRID_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_SIX_NUMERICAL_CORRECT_COLUMN_INDEX :
						// light green
						c.setBackground(new Color(242, 255, 239));
						break;
						
					case GnretTableModel.PARTICIPANT_STARTED_COLUMN_INDEX :
						c.setBackground(new Color(255, 169, 234));
						break;
						
					case GnretTableModel.DATE_COLUMN_INDEX :
					case GnretTableModel.TIME_COLUMN_INDEX :
						c.setBackground(new Color(240, 240, 240));
						break;
					case GnretTableModel.PRACTICE_GRID_WRONG_COLUMN_INDEX:
					case GnretTableModel.PRACTICE_NUMERICAL_WRONG_COLUMN_INDEX:
						
					case GnretTableModel.PAYING_ONE_GRID_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_ONE_NUMERICAL_WRONG_COLUMN_INDEX :
						
					case GnretTableModel.PAYING_TWO_GRID_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_TWO_NUMERICAL_WRONG_COLUMN_INDEX :
						
					case GnretTableModel.PAYING_THREE_GRID_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_THREE_NUMERICAL_WRONG_COLUMN_INDEX :
						
					case GnretTableModel.PAYING_FOUR_GRID_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_FOUR_NUMERICAL_WRONG_COLUMN_INDEX :
						
					case GnretTableModel.PAYING_FIVE_GRID_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_FIVE_NUMERICAL_WRONG_COLUMN_INDEX :
						
					case GnretTableModel.PAYING_SIX_GRID_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_SIX_NUMERICAL_WRONG_COLUMN_INDEX :	
						
						// light red
						c.setBackground(new Color(255, 240, 239));
						break;
					case GnretTableModel.GROUP_COLUMN_INDEX :
						c.setBackground(new Color(160, 213, 255)); // light blue
						break;
					case GnretTableModel.PRACTICE_SUM_CORRECT_COLUMN_INDEX:
					case GnretTableModel.PAYING_ONE_SUM_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_TWO_SUM_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_THREE_SUM_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_FOUR_SUM_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_FIVE_SUM_CORRECT_COLUMN_INDEX :
					case GnretTableModel.PAYING_SIX_SUM_CORRECT_COLUMN_INDEX :
						c.setBackground(Color.green);
						break;
						
					case GnretTableModel.PRACTICE_SUM_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_ONE_SUM_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_TWO_SUM_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_THREE_SUM_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_FOUR_SUM_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_FIVE_SUM_WRONG_COLUMN_INDEX :
					case GnretTableModel.PAYING_SIX_SUM_WRONG_COLUMN_INDEX :
						c.setBackground(new Color(255, 118, 98)); // strong red
						break;
					case GnretTableModel.HOST_NAME_COLUMN_INDEX :
					case GnretTableModel.ID_COLUMN_INDEX:
						c.setBackground(Color.yellow);
						break;
						
					case GnretTableModel.RANK_PRACTICE_COLUMN_INDEX :
					case GnretTableModel.RANK_PAYING_ONE_COLUMN_INDEX :
					case GnretTableModel.RANK_PAYING_TWO_COLUMN_INDEX :
					case GnretTableModel.RANK_PAYING_THREE_COLUMN_INDEX :
					case GnretTableModel.RANK_PAYING_FOUR_COLUMN_INDEX :
					case GnretTableModel.RANK_PAYING_FIVE_COLUMN_INDEX :
					case GnretTableModel.RANK_PAYING_SIX_COLUMN_INDEX :
						c.setBackground(Color.orange);
						break;
					default:
					}
				}
				return c;
			}

			// Implement table header tool tips.
			protected JTableHeader createDefaultTableHeader() {
				return new JTableHeader(columnModel) {
					private static final long serialVersionUID = -5685993138592530432L;

					public String getToolTipText(MouseEvent e) {
						java.awt.Point p = e.getPoint();
						int index = columnModel.getColumnIndexAtX(p.x);
						int realIndex = columnModel.getColumn(index)
								.getModelIndex();
						return GnretTableModel.COLUMN_HEADER_TOOLTIPS[realIndex];
					}
				};
			}
		};
		scrollPane.getHorizontalScrollBar().addAdjustmentListener(this);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(this);
		scrollPane.setViewportView(table);
		table.setFillsViewportHeight(true);
		table.setDefaultRenderer(Integer.class, new IntegerRenderer());
		table.setDefaultRenderer(String.class, new IntegerRenderer());
		table.setDefaultRenderer(Object.class, new IntegerRenderer()); 
		table.getTableHeader().setReorderingAllowed(false);

		ListModel lm = new AbstractListModel() {
			private static final long serialVersionUID = 7363708260513419388L;

			public Object getElementAt(int index) {
				return index + 1; // headers[index];
			}

			@Override
			public int getSize() {
				return Integer.MAX_VALUE;
			}
		};
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JList rowHeader = new JList(lm);
		rowHeader.setFixedCellWidth(50);

		rowHeader.setFixedCellHeight(table.getRowHeight());
		rowHeader.setCellRenderer(new RowHeaderRenderer(table));

		scrollPane.setRowHeaderView(rowHeader);

		setColumnWidths();
		table.setCellSelectionEnabled(true);		
		
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Data"));
		this.add(scrollPane, BorderLayout.CENTER);
		
		new ExcelAdapter(table); // enable copy/paste to clipboard for excel export
	}

	public void repaintRowHeader() {
		scrollPane.getRowHeader().revalidate();
	}

	public void addRow(String fileName, String fileContents) {
		tableModel.addRow(fileName, fileContents);
		table.setAutoCreateRowSorter(true);
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
		repaintRowHeader();
	}

	private void setColumnWidths() {
		TableColumn column = null;
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);
			int width = 15;
			switch (i) {
			case GnretTableModel.GENDER_COLUMN_INDEX :
			case GnretTableModel.AGE_COLUMN_INDEX :
			case GnretTableModel.COUNTRY_OF_ORIGIN_COLUMN_INDEX :
			case GnretTableModel.COUNTRY_WHERE_YOU_GREW_UP_COLUMN_INDEX :
			case GnretTableModel.FIELD_OF_STUDY_COLUMN_INDEX :
			case GnretTableModel.VERBAL_TASK_COLUMN_INDEX :
			case GnretTableModel.NUMERICAL_TASK_COLUMN_INDEX :
			case GnretTableModel.DESCRIBE_YOURSELF_COLUMN_INDEX :
			//case GnretTableModel.TECHNICAL_PROBLEMS_COLUMN_INDEX :
			case GnretTableModel.OTHER_COMMENTS_COLUMN_INDEX :
			case GnretTableModel.HOST_NAME_COLUMN_INDEX :
				width = 100;
				break;
			case GnretTableModel.TIME_COLUMN_INDEX:
			case GnretTableModel.DATE_COLUMN_INDEX:
				width = 60;
				break;
			case GnretTableModel.GROUP_COLUMN_INDEX :
			case GnretTableModel.PARTICIPANT_STARTED_COLUMN_INDEX :
				width = 50;
				break;
			case GnretTableModel.PRACTICE_SUM_CORRECT_COLUMN_INDEX :
			case GnretTableModel.PAYING_ONE_SUM_CORRECT_COLUMN_INDEX :
			case GnretTableModel.PAYING_TWO_SUM_CORRECT_COLUMN_INDEX :
			case GnretTableModel.PAYING_THREE_SUM_CORRECT_COLUMN_INDEX :
			case GnretTableModel.PAYING_FOUR_SUM_CORRECT_COLUMN_INDEX :
			case GnretTableModel.PAYING_FIVE_SUM_CORRECT_COLUMN_INDEX :
			case GnretTableModel.PAYING_SIX_SUM_CORRECT_COLUMN_INDEX :
				width = 40;
				break;
			case GnretTableModel.ID_COLUMN_INDEX :
			case GnretTableModel.RANK_PRACTICE_COLUMN_INDEX :
			case GnretTableModel.RANK_PAYING_ONE_COLUMN_INDEX :
			case GnretTableModel.RANK_PAYING_TWO_COLUMN_INDEX :
			case GnretTableModel.RANK_PAYING_THREE_COLUMN_INDEX :
			case GnretTableModel.RANK_PAYING_FOUR_COLUMN_INDEX :
			case GnretTableModel.RANK_PAYING_FIVE_COLUMN_INDEX :
			case GnretTableModel.RANK_PAYING_SIX_COLUMN_INDEX :
				width = 30;
				break;
			default:
				break;
			}
			column.setPreferredWidth(width);
		}
	}
	
	public synchronized void removeAllData() {
		table.setRowSorter(null);
		tableModel.removeAllData();
	}
	
	public JTable getTable() {
		return table;
	}
	
	public String exportTable(String delimineter) {
		return tableModel.exportTable(delimineter);
	}
}
