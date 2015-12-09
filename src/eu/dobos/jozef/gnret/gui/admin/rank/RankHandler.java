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

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

public class RankHandler {

	private Integer[] rankColumn;
	private Random randomizer;

	public RankHandler(Object[] column, int seedNumber) {
		randomizer = new Random(seedNumber);
		LinkedList<RankEntry> rankEntriesList = new LinkedList<RankEntry>();

		int row = 0;
		for (Object entry : column) {
			rankEntriesList.add(new RankEntry(row, (Integer) entry));
			row++;
		}

		sortUniqueRandomly(rankEntriesList);
		// sortUniqueInOrder(rankEntriesList);

	}

	public void sortUniqueInOrder(LinkedList<RankEntry> rankEntriesList) {
		Collections.sort(rankEntriesList);
		rankColumn = new Integer[rankEntriesList.size()];
		int rank = 0;
		// Integer prevRank = Integer.MAX_VALUE;
		for (RankEntry rankEntry : rankEntriesList) {
			rankColumn[rankEntry.getId()] = Integer.MAX_VALUE;

			if (rankEntry.getCorrectAnswers() != Integer.MIN_VALUE) {
				// if (prevRank != rankEntry.getCorrectAnswers()) {
				rank++;
				// }
				rankColumn[rankEntry.getId()] = new Integer(rank);
				// prevRank = rankEntry.getCorrectAnswers();
			}
		}
	}

	public void sortUniqueRandomly(LinkedList<RankEntry> rankEntriesList) {
		assert (rankEntriesList.size() > 0);
		Collections.sort(rankEntriesList);
		rankColumn = new Integer[rankEntriesList.size()];

		int rank = 1;
		Integer prevCorrectAnswers = Integer.MAX_VALUE;
		Vector<RankEntry> toBeRandomlyAssigned = new Vector<RankEntry>();

		for (int i = 0; i < rankEntriesList.size() + 1; i++) {
			if (rankEntriesList.size() == i) {
				rank = assignRandomly(toBeRandomlyAssigned, rank);
			} else {
				RankEntry rankEntry = rankEntriesList.get(i);
				rankColumn[rankEntry.getId()] = Integer.MAX_VALUE;

				if (rankEntry.getCorrectAnswers() != Integer.MIN_VALUE) {
					if (prevCorrectAnswers != rankEntry.getCorrectAnswers()) {
						// assign randomly for previous pass
						rank = assignRandomly(toBeRandomlyAssigned, rank);
						toBeRandomlyAssigned.clear();
					}
					toBeRandomlyAssigned.add(rankEntry);
					prevCorrectAnswers = rankEntry.getCorrectAnswers();
				}
			}
		}
	}

	private int assignRandomly(Vector<RankEntry> toBeRandomlyAssigned, int rank) {
		while (toBeRandomlyAssigned.size() > 0) {
			int index = randomizer.nextInt(toBeRandomlyAssigned.size());
			RankEntry re = toBeRandomlyAssigned.get(index);
			rankColumn[re.getId()] = new Integer(rank);
			rank++;
			toBeRandomlyAssigned.remove(index);
		}
		return rank;
	}

	public Integer[] getRankColumn() {
		return rankColumn;
	}

	/*
	 * public RankHandler(Object[] idArray, Object[][] arrayOfColumns) {
	 * 
	 * LinkedList<LinkedList<RankEntry>> rankEntriesListOfLists = new
	 * LinkedList<LinkedList<RankEntry>>(); for (int col = 0; col <
	 * arrayOfColumns.length; col++) { Object[] column = arrayOfColumns[col];
	 * LinkedList<RankEntry> rankEntriesList = new LinkedList<RankEntry>(); for
	 * (int row =0; row < column.length; row++) { RankEntry rankEntry = new
	 * RankEntry((Integer)idArray[row], (Integer)column[row]);
	 * rankEntriesList.add(rankEntry); } Collections.sort(rankEntriesList);
	 * rankEntriesListOfLists.add(rankEntriesList); } }
	 */
}
