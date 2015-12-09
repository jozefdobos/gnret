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

package eu.dobos.jozef.gnret.gui.experiment.grid;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.LinkedList;

import eu.dobos.jozef.gnret.filehandling.FileReadingHandler;
import eu.dobos.jozef.gnret.gui.experiment.AbstractTask;
import eu.dobos.jozef.gnret.gui.experiment.stats.TaskCallbackInterface;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;

@SuppressWarnings({ "serial" })
public class WordSearchPanel extends AbstractTask implements
		MouseMotionListener, MouseListener, ComponentListener {

	public static final boolean DEBUG = true;
	public static final int GRID_WIDTH = 15;
	public static final int GRID_HEIGHT = 15;
	public static final char LETTER_SIZE_ESTIMATER = 'W';
	
	/*
	public static final char[] ALPHABET = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z' };
	
	public static final char[] VOWELS = {'A', 'E', 'I', 'O', 'U'};
	public static final char[] CONSONANTS = {'B', 'C', 'D', 'F', 'G',
		'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
		'V', 'W', 'X', 'Y', 'Z' };*/
	
	/*
	public static final char[] TABLE_0 = {	
	'P',	'S',	'A',	'D',	'E',	'R',	'U',	'S',	'A',	'E',	'R',	'T',	'O',	'R',	'E',
	'O',	'R',	'E',	'N',	'N',	'U',	'R',	'D',	'L',	'O',	'F',	'N',	'U',	'O',	'P',
	'V',	'U',	'I',	'P',	'E',	'O',	'C',	'I',	'A',	'S',	'O',	'R',	'P',	'N',	'S',
	'E',	'N',	'A',	'S',	'H',	'Y',	'F',	'A',	'T',	'P',	'A',	'L',	'S',	'E',	'E',
	'R',	'R',	'A',	'T',	'M',	'E',	'Y',	'R',	'R',	'L',	'A',	'I',	'E',	'R',	'U',
	'T',	'E',	'U',	'O',	'S',	'S',	'Q',	'U',	'I',	'E',	'T',	'T',	'T',	'O',	'D',
	'Y',	'A',	'D',	'N',	'O',	'T',	'S',	'S',	'V',	'E',	'E',	'I',	'A',	'U',	'O',
	'L',	'L',	'U',	'N',	'E',	'A',	'A',	'H',	'I',	'N',	'N',	'I',	'L',	'S',	'N',
	'A',	'G',	'E',	'E',	'V',	'T',	'B',	'E',	'A',	'M',	'I',	'Y',	'E',	'T',	'Y',
	'T',	'O',	'I',	'R',	'I',	'E',	'O',	'D',	'L',	'I',	'F',	'T',	'S',	'U',	'M',
	'R',	'E',	'B',	'O',	'S',	'S',	'T',	'A',	'N',	'I',	'S',	'U',	'S',	'P',	'U',
	'U',	'O',	'N',	'U',	'I',	'A',	'A',	'U',	'R',	'A',	'S',	'G',	'N',	'O',	'L',
	'S',	'I',	'G',	'O',	'T',	'L',	'G',	'R',	'O',	'S',	'E',	'T',	'S',	'A',	'P',
	'T',	'A',	'G',	'P',	'I',	'T',	'E',	'H',	'C',	'A',	'S',	'C',	'E',	'O',	'A',
	'R',	'E',	'L',	'E',	'C',	'T',	'R',	'I',	'C',	'I',	'T',	'Y',	'O',	'N',	'L'
	};*/


	private char[][] grid = new char[GRID_HEIGHT][GRID_WIDTH];
	private Point[][] pointGrid = new Point[GRID_HEIGHT][GRID_WIDTH];
	private Point startPoint = new Point(-Integer.MAX_VALUE, -Integer.MAX_VALUE); // in
	// [i][j]
	// grid
	// coordinates
	private Point endPoint = new Point(-Integer.MAX_VALUE, -Integer.MAX_VALUE); // in
	// [i][j]
	// grid
	// coordinates

	private SpellCheckManager spellCheckManager;
	private BasicStroke lineStroke = new BasicStroke(1);
	private LinkedList<Line> selectedLinesList = new LinkedList<Line>();
	private TaskCallbackInterface [] arrayOfTaskStatsListeners;
	
	public WordSearchPanel(TaskCallbackInterface [] arrayOfTaskStatsListeners, SpellCheckManager spellCheckManager, int seedNumber) throws IOException {
		super();
		this.arrayOfTaskStatsListeners = arrayOfTaskStatsListeners;
		this.setBackground(Settings.BACKGROUND_COLOR);
		this.spellCheckManager = spellCheckManager;
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addComponentListener(this);	
		FileReadingHandler frh = new FileReadingHandler();
		grid = frh.readCsvTable(seedNumber, GRID_WIDTH, GRID_HEIGHT);
		
		/*
		Random generator = new Random(seedNumber);
		int letterIndex = 0;
		int counter = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				
				if (seedNumber == 0) {
					grid[j][i] = table[counter]; //TABLE_0[counter];
					counter++;
				}
				else {				
					if (generator.nextBoolean()) {
						letterIndex = generator.nextInt(CONSONANTS.length);
						grid[i][j] = CONSONANTS[letterIndex];
					}
					else {
						letterIndex = generator.nextInt(VOWELS.length);
						grid[i][j] = VOWELS[letterIndex];
					}
				}
			}
		}*/
		reset();
	}

	/***
	 * Returns a count of valid words.
	 * 
	 * @return
	 */
	public int getValidWordsCount() {
		return selectedLinesList.size();
	}

	private void reset() {

		Dimension panelSize = this.getSize();
		int smallerDimension = panelSize.width > panelSize.height ? panelSize.height
				: panelSize.width;

		if (smallerDimension < 0) // safety cap
			smallerDimension = 0;

		this.setFont(Utils.getResizedFont(this.getFont(), smallerDimension / 30));
		lineStroke = new BasicStroke(smallerDimension / 30,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

		// enforces squared grid layout
		int horizontalSpacing = smallerDimension / grid.length;
		int verticalSpacing = smallerDimension / grid[0].length;

		// offset to always center the grid in the panel
		int horizontalOffset = 0;
		int vericalOffset = 0;

		if (panelSize.width > panelSize.height) {
			horizontalOffset = (panelSize.width - grid.length
					* horizontalSpacing) / 2;
		} else {
			vericalOffset = (panelSize.height - grid[0].length
					* verticalSpacing) / 2;
		}

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				pointGrid[i][j] = new Point((i + 1) * horizontalSpacing
						- horizontalSpacing / 2 + horizontalOffset, (j + 1)
						* verticalSpacing - verticalSpacing / 2 + vericalOffset);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		clear(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(this.getFont());

		// Composite originalComposite = g2d.getComposite();
		RenderingHints renderHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(renderHints);

		g2d.setStroke(lineStroke);

		g2d.setComposite(makeComposite(1f));
		g2d.setPaint(Settings.SELECTION_COLOR);
		paintSelectionLine(g2d);

		g2d.setComposite(makeComposite(0.5f));
		g2d.setPaint(Settings.HIGHLIGHT_COLOR);
		paintSelectedLines(g2d);

		g2d.setComposite(makeComposite(1.0f));
		g2d.setPaint(Settings.FOREGROUND_COLOR);
		paintGrid(g2d);

	}

	private void paintSelectionLine(Graphics2D g2d) {
		if (startPoint.x != -Integer.MAX_VALUE
				&& endPoint.x != -Integer.MAX_VALUE
				&& startPoint.y != -Integer.MAX_VALUE
				&& endPoint.y != -Integer.MAX_VALUE) {
			g2d.drawLine(pointGrid[startPoint.x][startPoint.y].x,
					pointGrid[startPoint.x][startPoint.y].y,
					pointGrid[endPoint.x][endPoint.y].x,
					pointGrid[endPoint.x][endPoint.y].y);
		}
	}

	private void paintSelectedLines(Graphics2D g2d) {
		for (Line line : selectedLinesList) {
			if (line.getStart().x != -Integer.MAX_VALUE
					&& line.getStart().y != -Integer.MAX_VALUE
					&& line.getEnd().x != -Integer.MAX_VALUE
					&& line.getEnd().y != -Integer.MAX_VALUE) {

				g2d.drawLine(pointGrid[line.getStart().x][line.getStart().y].x,
						pointGrid[line.getStart().x][line.getStart().y].y,
						pointGrid[line.getEnd().x][line.getEnd().y].x,
						pointGrid[line.getEnd().x][line.getEnd().y].y);
			}
		}
	}

	private void paintGrid(Graphics2D g2d) {
		FontMetrics metrics = g2d.getFontMetrics(this.getFont());
		Rectangle2D defaultLetterSize = metrics.getStringBounds(""
				+ LETTER_SIZE_ESTIMATER, g2d);

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {

				Rectangle2D letterSize = metrics.getStringBounds(""
						+ grid[i][j], g2d);

				// g2d.drawLine(0, (j+1) * verticalSpacing,
				// this.getSize().width, (j+1) * verticalSpacing);
				g2d.drawString(
						"" + grid[i][j],
						(int) (pointGrid[i][j].x - (letterSize.getWidth()) / 2),
						(int) (pointGrid[i][j].y + defaultLetterSize
								.getHeight() / 3));
			}
		}
	}

	/***
	 * Returns i,j coordinates of the closest point
	 * 
	 * @param p
	 * @param isStartPoint
	 * @return
	 */
	private Point getClosestPoint(Point p, boolean isStartPoint) {
		double bestDistance = Double.MAX_VALUE;
		Point returnPoint = new Point();
		for (int i = 0; i < pointGrid.length; i++) {
			for (int j = 0; j < pointGrid[i].length; j++) {
				int deltaX = pointGrid[i][j].x - p.x;
				int deltaY = pointGrid[i][j].y - p.y;
				double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

				boolean isDiagonal = !isStartPoint
						&& Math.abs(pointGrid[startPoint.x][startPoint.y].x
								- pointGrid[i][j].x) == Math
								.abs(pointGrid[startPoint.x][startPoint.y].y
										- pointGrid[i][j].y);
				boolean isVertical = !isStartPoint
						&& (pointGrid[startPoint.x][startPoint.y].x == pointGrid[i][j].x);
				boolean isHorizontal = !isStartPoint
						&& (pointGrid[startPoint.x][startPoint.y].y == pointGrid[i][j].y);

				if (distance < bestDistance
						&& (isStartPoint || isDiagonal || isVertical || isHorizontal)) {
					returnPoint.setLocation(i, j);
					bestDistance = distance;
				}
			}
		}
		return returnPoint;
	}

	protected void clear(Graphics g) {
		super.paintComponent(g);
	}

	public static AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	// MOUSE MOTION LISTENER

	@Override
	public void mouseDragged(MouseEvent e) {
		endPoint = getClosestPoint(new Point(e.getX(), e.getY()), false);

		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	// MOUSE LISTENER

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		startPoint = getClosestPoint(new Point(e.getX(), e.getY()), true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		String word = getWord(startPoint, endPoint);
		//System.out.println(word);

		boolean correctSpelling = spellCheckManager.isSpelledCorrectly(word);

		if (word.length() > 1 && correctSpelling) {
			Line line = new Line(startPoint.x, startPoint.y, endPoint.x,
					endPoint.y);
			if (!isLinePresent(line)) { // correct answer highlighted for the
										// first time
				selectedLinesList.add(line);
				for (TaskCallbackInterface taskListener : arrayOfTaskStatsListeners)
					taskListener.incrementCorrectAnswers();
			}
		} else if (word.length() <= 1 || !correctSpelling) { // wrong attempt
			for (TaskCallbackInterface taskListener : arrayOfTaskStatsListeners)
				taskListener.incrementWrongAnswers();
		}

		// reset
		startPoint.setLocation(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
		endPoint.setLocation(-Integer.MAX_VALUE, -Integer.MAX_VALUE);

		repaint();
	}

	private boolean isLinePresent(Line line) {
		boolean ret = false;
		for (Line l : selectedLinesList) {
			if (l.getStart().x == line.getStart().x
					&& l.getStart().y == line.getStart().y
					&& l.getEnd().x == line.getEnd().x
					&& l.getEnd().y == line.getEnd().y) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/***
	 * Returns a selected word.
	 * 
	 * @param start
	 *            in [i][j] grid coordinates
	 * @param end
	 *            in [i][j] grid coordinates
	 * @return
	 */
	private String getWord(Point start, Point end) {
		String ret = new String();

		if (start.x >= 0 && start.x < GRID_WIDTH && end.x >= 0
				&& end.x < GRID_WIDTH && start.y >= 0 && start.y < GRID_HEIGHT
				&& end.y >= 0 && end.y < GRID_HEIGHT) {

			if (start.x == end.x) { // vertical selection
				for (int i = 0; i < Math.abs(start.y - end.y) + 1; i++) {
					if (start.y <= end.y) { // upwards
						ret += grid[start.x][start.y + i];
					} else { // downwards
						ret += grid[start.x][start.y - i];
					}
				}
			} else if (start.y == end.y) { // horizontal selection
				for (int i = 0; i < Math.abs(start.x - end.x) + 1; i++) {
					if (start.x <= end.x) { // left
						ret += grid[start.x + i][start.y];
					} else { // right
						ret += grid[start.x - i][start.y];
					}
				}
			} else if (start.x != end.x && start.y != end.y) { // diagonal
																// selection
				for (int i = 0; i < Math.min(Math.abs(start.x - end.x) + 1,
						Math.abs(start.y - end.y) + 1); i++) {
					if (start.x <= end.x && start.y <= end.y) { // from bottom
																// right
						// to top left
						ret += grid[start.x + i][start.y + i];
					} else if (start.x <= end.x && start.y > end.y) {
						ret += grid[start.x + i][start.y - i];
					} else if (start.x > end.x && start.y <= end.y) {
						ret += grid[start.x - i][start.y + i];
					} else if (start.x != end.x && start.y != end.y) { // safety
						// measure
						// for
						// single
						// letter
						// words
						ret += grid[start.x - i][start.y - i];
					}
				}
			}
		}
		return ret;
	}

	// COMPONENT LISTENER

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		reset();
		repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}
}
