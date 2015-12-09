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

import javax.swing.table.DefaultTableCellRenderer;

public class IntegerRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 6673008669665007047L;

	public IntegerRenderer() {
		super();
	}

	public void setValue(Object value) {

		Object whatToSet = value.toString();
		setToolTipText(" ");
		if (value instanceof Integer) {
			if (Integer.MIN_VALUE < (Integer) value
					&& (Integer) value < Integer.MAX_VALUE) {
				whatToSet = (Integer) value;
				setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			} else
				whatToSet = "";
		} else {
			setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		}
		setText(whatToSet.toString());

		if (!whatToSet.toString().isEmpty())
			setToolTipText(whatToSet.toString());

	}
}
