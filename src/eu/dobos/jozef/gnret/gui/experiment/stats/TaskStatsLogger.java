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

package eu.dobos.jozef.gnret.gui.experiment.stats;

import java.util.LinkedList;

public class TaskStatsLogger implements TaskCallbackInterface,
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1116604770517623486L;

	private int currentTask = -1;
	private LinkedList<Integer> correctAnswers = new LinkedList<Integer>();
	private LinkedList<Integer> wrongAnswers = new LinkedList<Integer>();

	public LinkedList<Integer> getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(LinkedList<Integer> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	public LinkedList<Integer> getWrongAnswers() {
		return wrongAnswers;
	}

	public void setWrongAnswers(LinkedList<Integer> wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

	public void setNextTask() {
		currentTask++;
		wrongAnswers.add(new Integer(0));
		correctAnswers.add(new Integer(0));
	}

	@Override
	public void incrementWrongAnswers() {
		wrongAnswers.set(currentTask, new Integer(
				wrongAnswers.get(currentTask) + 1));

	}

	@Override
	public void incrementCorrectAnswers() {
		correctAnswers.set(currentTask, new Integer(correctAnswers
				.get(currentTask) + 1));

	}

	public int getCurrentWrongAnswers() {
		return wrongAnswers.get(currentTask);
	}

	public int getCurrentCorrectAnswers() {
		return correctAnswers.get(currentTask);
	}

	/***
	 * Returns previous taks' correct answers count or 0, it there was no
	 * previous task.
	 * 
	 * @return
	 */
	public int getPreviousCorrectAnswers() {		
		return getPreviousAnswers(correctAnswers);
	}
	
	/***
	 * Returns previous taks' correct answers count or 0, it there was no
	 * previous task.
	 * 
	 * @return
	 */
	public int getPreviousWrongAnswers() {
		return getPreviousAnswers(wrongAnswers);
	}
	
	/***
	 * Returns previous taks' correct answers count or 0, it there was no
	 * previous task.
	 * 
	 * @return
	 */
	private int getPreviousAnswers(LinkedList<Integer> list) {
		int ret = 0;
		if (currentTask > 0)
			ret = list.get(currentTask - 1);
		return ret;
	}
}
