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

package eu.dobos.jozef.gnret.time;

import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Timer;
import java.util.concurrent.Callable;

public class TimeKeeper implements Callable<Object> {
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"mm:ss");
	public static final long TEN_MINUTES = getTimeInMilliseconds(10, 0); // ms
	public static final long THIRTY_MINUTES = getTimeInMilliseconds(30, 0); // ms
	public static final long SIX_MINUTES = getTimeInMilliseconds(6, 0); // ms
	public static final long THREE_MINUTES = getTimeInMilliseconds(3, 0); // ms
	public static final long FOUR_MINUTES = getTimeInMilliseconds(4, 0); // ms
	public static final long HALF_MINUTE = getTimeInMilliseconds(0, 30); // ms
	public static final long QUATER_MINUTE = getTimeInMilliseconds(0, 15); // ms
	public static final long SIX_SECONDS = getTimeInMilliseconds(0, 6); //ms
	public static final long FIVE_SECONDS = getTimeInMilliseconds(0, 5); //ms
	private static final int SECOND = 1000;
	private static Date date = new Date();
	private long time = 0;
	private Timer timer;
	
	public TimeKeeper(long time, ActionListener caller) {
		timer = new Timer(SECOND, caller);
		this.time = time; 
		timer.setCoalesce(false);
	}

	public Object call() {
		start();
		return null;
	}

	
	/***
	 * Sets time for times in milliseconds
	 * 
	 * @param time
	 */
	public void setTime(long time) {
		this.time = time;
	}

	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

	/***
	 * Returns true if the set time is up.
	 * 
	 * @return
	 */
	public boolean updateMiliseconds() {
		time -= 1000;
		return time <= 0;
	}

	/***
	 * Returns current time as string formatted by Settings.TIME_FORMAT.
	 * 
	 * @return
	 */
	public String getCurrentFormattedTime() {
		return TIME_FORMAT.format(time);
	}

	public static String getCurrentSystemFormattedTime() {
		date.setTime(System.currentTimeMillis());
		return TIME_FORMAT.format(date);
	}

	/***
	 * Returns time represented in milliseconds.
	 * 
	 * @param minutes
	 *            have to be greater or equal zero (non-negative)
	 * @param seconds
	 *            have to be greater or equal zero (non-negative)
	 * @return
	 */
	public static long getTimeInMilliseconds(int minutes, int seconds) {
		assert (minutes >= 0 && seconds >= 0);
		return ((minutes * 60) + seconds) * 1000; // time in milliseconds
	}

	public long getTime() {
		return time;
	}
}
