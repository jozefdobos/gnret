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

package eu.dobos.jozef.gnret.gui.experiment.numerical;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import eu.dobos.jozef.gnret.gui.experiment.AbstractTask;
import eu.dobos.jozef.gnret.gui.experiment.stats.TaskCallbackInterface;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;

@SuppressWarnings("serial")
public class NumericalScreenPanel extends AbstractTask {
	
	private Font f;
	private JLabel question;
	private NumericalInputPanel numerical;
	private Random generator;
	private static final int MAX_NUMBER = 100; 
		
	private int[] numbers = new int[2];
	
	
	private TaskCallbackInterface [] arrayOfTaskStatsListeners;

	public NumericalScreenPanel(TaskCallbackInterface [] arrayOfTaskStatsListeners, int seedNumber) {
		super();		
		this.arrayOfTaskStatsListeners = arrayOfTaskStatsListeners;
		f = Utils.getIncreasedFont(this.getFont(), Settings.FONT_SIZE_INCREASE);
		this.setFont(f);
		
		generator = new Random(seedNumber);				
		question = new JLabel();
		question.setFont(f);
		
		
		numerical = new NumericalInputPanel(this);
		generateNewQuestion();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(Settings.INSETS);
		
		Dimension minSize = new Dimension(0, 0);
		Dimension prefSize = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		
		this.add(new Box.Filler(minSize, prefSize, maxSize));
		question.setAlignmentX(Component.CENTER_ALIGNMENT);		
		this.add(question);
		
		//numerical.setAlignmentX(Component.TOP_ALIGNMENT);
		//this.add(Box.createRigidArea(new Dimension(50,0)));
		this.add(numerical);
		this.add(new Box.Filler(minSize, prefSize, maxSize));
		this.setBackground(Settings.BACKGROUND_COLOR);
	}
	
	private void generateNewQuestion() {
		String q = "";
		for (int i=0; i < numbers.length; i++) {
			numbers[i] = generator.nextInt(MAX_NUMBER);	
			q = q + numbers[i];			
			if (i != numbers.length -1) 
				q = q + " + ";
		}						
		question.setText(q + " = ?");
	}
	
	public void checkAnswer(float answer) {
		int correctAnswer = 0;
		for (int i=0; i < numbers.length; i++) {
			correctAnswer += numbers[i];
		}
			
		if (correctAnswer == answer) { // correct answer
			generateNewQuestion();
			for (TaskCallbackInterface taskListener : arrayOfTaskStatsListeners)
				taskListener.incrementCorrectAnswers();
		}
		else { // wrong answer
			for (TaskCallbackInterface taskListener : arrayOfTaskStatsListeners)
				taskListener.incrementWrongAnswers();
		}
	}
}
