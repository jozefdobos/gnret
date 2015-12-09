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

package eu.dobos.jozef.gnret.gui.admin.rank;


public class RankEntry implements Comparable<RankEntry> {
	private int row;
	private int correctAnswers;
	
	public RankEntry(int row, int correctAnswers) {
		this.row = row;
		this.correctAnswers = correctAnswers;
	}

	public int getId() {
		return row;
	}

	public void setId(int id) {
		this.row = id;
	}

	public int getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(int correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	@Override
	public int compareTo(RankEntry o) {
		int ret;
		if (this.correctAnswers == ((RankEntry) o).getCorrectAnswers())
            ret = 0;
        else if (this.correctAnswers < ((RankEntry) o).getCorrectAnswers())
            ret = 1;
        else
            ret = -1;
		return ret;
	}

}
