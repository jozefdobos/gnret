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

package eu.dobos.jozef.gnret.gui.experiment.questionnaire;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import eu.dobos.jozef.gnret.filehandling.FileReadingHandler;
import eu.dobos.jozef.gnret.gui.experiment.ExperimentView;
import eu.dobos.jozef.gnret.gui.experiment.instructions.InstructionsPanel;
import eu.dobos.jozef.gnret.gui.experiment.instructions.InstructionsPanel.Text;
import eu.dobos.jozef.gnret.gui.utils.Settings;
import eu.dobos.jozef.gnret.gui.utils.Utils;

public class QuestionnairePanel extends JPanel implements ActionListener {

	private Font f;
	private static final long serialVersionUID = 3832137859129352746L;
	private ExperimentView caller;

	public static final String DEFAULT_CHOICE = "---";
	public static final String OTHER = DEFAULT_CHOICE
			+ " Other, please specify:";
	private static final String[] GENDER_CHOICE = { DEFAULT_CHOICE, "Male",
			"Female" };
	private static final String[] SIMILAR_TASK_CHOICE = { DEFAULT_CHOICE,
			"No, I have never attempted such a task in the past.",
			"Yes, I have briefly attempted such a task in the past.",
			"Yes, I have often attempted such a task in the past." };

	private static final String[] COMPETITIVNESS_CHOICE = {
			DEFAULT_CHOICE,
			"I am not competitive at all. I do not compare my performance to the performance of others.",
			"I am moderately competitive. I often take interest in how my performance compares to the performance of others.",
			"I am strongly competitive. I am always interested in how my performance compares to the performance of others." };

	private JComboBox genderComboBox = new JComboBox(GENDER_CHOICE);
	private JComboBox verbalTaskComboBox = new JComboBox(SIMILAR_TASK_CHOICE);
	private JComboBox numericalTaskComboBox = new JComboBox(SIMILAR_TASK_CHOICE);
	private JComboBox compeititiveComboBox = new JComboBox(
			COMPETITIVNESS_CHOICE);

	private JComboBox ageComboBox;
	private JComboBox fieldOfStudyComboBox;
	private JComboBox countryOfOriginComboBox;
	private JComboBox grewupComboBox;

	// private JTextArea technicalDifficultiesTextArea = new JTextArea();
	private JTextArea otherCommentsTextArea = new JTextArea();

	private JTextField countryOfOriginTextField = new JTextField(20);
	private JTextField grewupTextField = new JTextField(20);
	private JTextField fieldOfStudyTextField = new JTextField(20);

	public static final String GENDER = "Gender";
	public static final String AGE = "Age";
	public static final String COUNTRY_OF_ORIGIN = "Country of birth";
	public static final String FIELD_OF_STUDY = "Field of study";
	public static final String COUNTRY_WHERE_YOU_GREW_UP = "Country where you grew up (if different from your country of origin)";
	public static final String SIMILAR_TASK = "Have you ever attempted a task/game similar to the";
	public static final String VERBAL_TASK = "Verbal task (word spotting) in this experiment";
	public static final String NUMERICAL_TASK = "Numerical task (mental arithmetics/sums) in this experiment";
	public static final String DESCRIBE_YOURSELF = "How would you characterise yourself in terms of competitiveness with others";
	// public static final String TECHNICAL_PROBLEMS =
	// "Did you have any technical difficulties throught the experiment";
	public static final String OTHER_COMMENTS = "Other Comments (e.g., Did you experience any technical difficulties during the experiment?)";

	public QuestionnairePanel(ExperimentView caller, Text textSelector)
			throws IOException {
		super();
		this.caller = caller;
		this.setBackground(Settings.BACKGROUND_COLOR);
		f = Utils.getIncreasedFont(this.getFont(), 11);

		this.setLayout(new BorderLayout());

		Vector<String> ageChoice = new Vector<String>();
		ageChoice.add(DEFAULT_CHOICE);
		for (int i = 16; i <= 100; i++) {
			ageChoice.add(i + "");
		}
		ageComboBox = new JComboBox(ageChoice);

		FileReadingHandler frh = new FileReadingHandler();
		String[] arrayOfFstValues = { DEFAULT_CHOICE, OTHER };

		Vector<String> countries = frh.readTxtFile("countries_list",
				arrayOfFstValues);
		countryOfOriginComboBox = new JComboBox(countries);
		countryOfOriginComboBox.addActionListener(this);
		countryOfOriginComboBox.setActionCommand(COUNTRY_OF_ORIGIN);
		countryOfOriginTextField.setVisible(false);

		grewupComboBox = new JComboBox(countries);
		grewupComboBox.addActionListener(this);
		grewupComboBox.setActionCommand(COUNTRY_WHERE_YOU_GREW_UP);
		grewupTextField.setVisible(false);

		fieldOfStudyComboBox = new JComboBox(frh.readTxtFile(
				"fields_of_study_list", arrayOfFstValues));

		fieldOfStudyComboBox.addActionListener(this);
		fieldOfStudyComboBox.setActionCommand(FIELD_OF_STUDY);
		fieldOfStudyTextField.setVisible(false);

		add(new InstructionsPanel(textSelector), BorderLayout.NORTH);
		JPanel questionsWrapper = new JPanel();
		questionsWrapper.setBackground(Settings.BACKGROUND_COLOR);
		questionsWrapper.setBorder(Settings.INSETS);
		questionsWrapper.setLayout(new BorderLayout());
		questionsWrapper.add(getQuestions(), BorderLayout.CENTER);
		this.add(questionsWrapper, BorderLayout.CENTER);

		JButton submitButton = new JButton(Settings.SUBMIT);
		submitButton.addActionListener(this);
		submitButton.setFont(f);
		submitButton.setBackground(Settings.CORRECT_COLOR);
		submitButton.setActionCommand(Settings.SUBMIT);

		JPanel submitPanel = new JPanel();
		submitPanel.setBackground(Settings.BACKGROUND_COLOR);
		submitPanel.add(submitButton);
		this.add(submitPanel, BorderLayout.SOUTH);

	}

	private JScrollPane getQuestions() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.white);
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);

		int question = 0;
		int x = 10;
		int y = 10;
		int offset = (int) (x * 2.5);

		panel.setPreferredSize(new Dimension(900, 1100));

		// Gender
		question++;
		JLabel label = new JLabel(question + ") *" + GENDER + ":");
		label.setFont(f);
		panel.add(label);
		layout.putConstraint(SpringLayout.WEST, label, x, SpringLayout.WEST,
				panel);
		layout.putConstraint(SpringLayout.NORTH, label, y, SpringLayout.NORTH,
				panel);
		addComponentToLayout(layout, panel, label, genderComboBox, x + offset,
				y);

		// AGE
		question++;
		label = new JLabel(question + ") *" + AGE + ":");
		addComponentToLayout(layout, panel, genderComboBox, label, x, y
				+ offset);
		addComponentToLayout(layout, panel, label, ageComboBox, x + offset, y);

		// Country of origin
		question++;
		label = new JLabel(question + ") *" + COUNTRY_OF_ORIGIN + ":");
		addComponentToLayout(layout, panel, ageComboBox, label, x, y + offset);
		addComponentToLayout(layout, panel, label, countryOfOriginComboBox, x
				+ offset, y);
		addOtherFieldToLayout(layout, panel, countryOfOriginComboBox,
				countryOfOriginTextField, offset, 0);

		// Grew up country
		question++;
		label = new JLabel(question + ") " + COUNTRY_WHERE_YOU_GREW_UP + ":");
		addComponentToLayout(layout, panel, countryOfOriginComboBox, label, x,
				y + offset);
		addComponentToLayout(layout, panel, label, grewupComboBox, x + offset,
				y);
		addOtherFieldToLayout(layout, panel, grewupComboBox, grewupTextField,
				offset, 0);

		// Field of study
		question++;
		label = new JLabel(question + ") *" + FIELD_OF_STUDY + ":");
		addComponentToLayout(layout, panel, grewupComboBox, label, x, y
				+ offset);
		addComponentToLayout(layout, panel, label, fieldOfStudyComboBox, x
				+ offset, y);
		addOtherFieldToLayout(layout, panel, fieldOfStudyComboBox,
				fieldOfStudyTextField, offset, 0);

		// Similar task
		question++;
		JLabel label2 = new JLabel(question + ") " + SIMILAR_TASK + ":");
		addComponentToLayout(layout, panel, fieldOfStudyComboBox, label2, x, y
				+ offset);
		// i) Verbal
		label = new JLabel("i) *" + VERBAL_TASK + "?");
		addComponentToLayout(layout, panel, label2, label, x + offset, y
				+ offset / 4);
		addComponentToLayout(layout, panel, label, verbalTaskComboBox, x
				+ offset * 2, y);
		// ii) Numerical
		label = new JLabel("ii) *" + NUMERICAL_TASK + "?");
		addComponentToLayout(layout, panel, verbalTaskComboBox, label, x
				+ offset, y + offset / 4);
		addComponentToLayout(layout, panel, label, numericalTaskComboBox, x
				+ offset * 2, y);

		// Characterise yourself
		question++;
		label = new JLabel(question + ") *" + DESCRIBE_YOURSELF + "?");
		addComponentToLayout(layout, panel, numericalTaskComboBox, label, x, y
				+ offset);
		addComponentToLayout(layout, panel, label, compeititiveComboBox, x
				+ offset, y);

		// Technical difficulties
		/*
		 * question++; label = new JLabel( question + ") " + TECHNICAL_PROBLEMS
		 * + "?"); addComponentToLayout(layout, panel, compeititiveComboBox,
		 * label, x, y + offset); JScrollPane technicalDifficultiesScrollPane =
		 * addTextAreaToLayout( layout, panel, label,
		 * technicalDifficultiesTextArea, x + offset, y);
		 */

		// Other comments
		question++;
		label = new JLabel(question + ") " + OTHER_COMMENTS + ":");
		addComponentToLayout(layout, panel, compeititiveComboBox, label, x, y
				+ offset);
		addTextAreaToLayout(layout, panel, label, otherCommentsTextArea, x
				+ offset, y);

		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		// scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		return scrollPane;
	}

	private void addComponentToLayout(SpringLayout layout, JComponent panel,
			JComponent previousComponent, JComponent currentComponent, int x,
			int y) {
		currentComponent.setFont(f);
		panel.add(currentComponent);
		layout.putConstraint(SpringLayout.WEST, currentComponent, x,
				SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, currentComponent, y,
				SpringLayout.SOUTH, previousComponent);
	}

	private void addOtherFieldToLayout(SpringLayout layout, JComponent panel,
			JComponent previousComponent, JComponent currentComponent, int x,
			int y) {
		currentComponent.setFont(f);
		panel.add(currentComponent);
		layout.putConstraint(SpringLayout.NORTH, currentComponent, y,
				SpringLayout.NORTH, previousComponent);
		layout.putConstraint(SpringLayout.WEST, currentComponent, x,
				SpringLayout.EAST, previousComponent);
	}

	private JScrollPane addTextAreaToLayout(SpringLayout layout,
			JComponent panel, JComponent previousComponent,
			JTextArea currentComponent, int x, int y) {
		currentComponent.setFont(f);
		JScrollPane scrollPane = new JScrollPane(currentComponent);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray));
		scrollPane.setPreferredSize(new Dimension(700, 100));
		panel.add(scrollPane);
		layout.putConstraint(SpringLayout.WEST, scrollPane, x,
				SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, scrollPane, y,
				SpringLayout.SOUTH, previousComponent);
		return scrollPane;
	}

	/***
	 * Removes double quotes, \n and \t.
	 * 
	 * @param s
	 * @return
	 */
	private String sterilizeString(String s) {
		s = s.replaceAll("\"", "'");
		// replace all double quotes with single quotes
		s = s.replaceAll("\\n", "");
		// remove all new line characters
		s = s.replaceAll("\\t", ""); // remove all tabs
		if (s.isEmpty())
			s = " ";
		return s;
	}

	private boolean isComboBoxProperlySelected(JComboBox box) {
		boolean ret = !((String) box.getSelectedItem()).equals(DEFAULT_CHOICE);
		return ret;
	}

	private boolean isComboBoxProperlySelectedOrOther(JComboBox box,
			JTextField textField) {
		boolean ret = ((String) box.getSelectedItem()).equals(DEFAULT_CHOICE)
				|| (((String) box.getSelectedItem()).equals(OTHER) && textField.getText().isEmpty());
		return !ret;
	}
	
	private String getSelectedString(JComboBox box, JTextField field) {
		String ret = new String();
		if (((String) box.getSelectedItem()).equals(OTHER))
			ret = OTHER + " " + field.getText();
		else
			ret = ((String) box.getSelectedItem());
		return ret;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		if (command.equals(Settings.SUBMIT)) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			if (isComboBoxProperlySelected(genderComboBox)
					&& isComboBoxProperlySelected(ageComboBox)
					&& isComboBoxProperlySelectedOrOther(
							countryOfOriginComboBox, countryOfOriginTextField)
					&& isComboBoxProperlySelectedOrOther(fieldOfStudyComboBox,
							fieldOfStudyTextField)

					&& isComboBoxProperlySelected(verbalTaskComboBox)
					&& isComboBoxProperlySelected(numericalTaskComboBox)
					&& isComboBoxProperlySelected(compeititiveComboBox)) {

				String data = new String();
				
				// gender
				data += genderComboBox.getSelectedItem() + Settings.DELIMINETER;
				
				// age
				data += ageComboBox.getSelectedItem() + Settings.DELIMINETER;
				
				// country of birth
				data += sterilizeString(getSelectedString(countryOfOriginComboBox, countryOfOriginTextField))
						+ Settings.DELIMINETER;
				
				// grew up 
				data += sterilizeString(getSelectedString(grewupComboBox, grewupTextField))
						+ Settings.DELIMINETER;
				
				// field of study
				data += sterilizeString(getSelectedString(fieldOfStudyComboBox, fieldOfStudyTextField))
				+ Settings.DELIMINETER;
				
				// verbal
				data += verbalTaskComboBox.getSelectedItem()
						+ Settings.DELIMINETER;
				
				// numerical
				data += numericalTaskComboBox.getSelectedItem()
						+ Settings.DELIMINETER;
				// yourself
				data += compeititiveComboBox.getSelectedItem()
						+ Settings.DELIMINETER;
				// data +=
				// sterilizeString(technicalDifficultiesTextArea.getText())
				// + Settings.DELIMINETER;
				
				// other comments
				data += sterilizeString(otherCommentsTextArea.getText());
				caller.saveAndUpload(data, true);
			} else {
				JOptionPane
						.showMessageDialog(
								this,
								"All fields marked with an asterisk (*) have to be filled in.",
								"Missing data", JOptionPane.WARNING_MESSAGE);
				this.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		else if (command.equals(COUNTRY_OF_ORIGIN)) {
			JComboBox cb = (JComboBox) arg0.getSource();
			String name = (String) cb.getSelectedItem();

			countryOfOriginTextField.setVisible(name.equals(OTHER));
			if (!name.equals(OTHER))
				countryOfOriginTextField.setText("");

		} else if (command.equals(COUNTRY_WHERE_YOU_GREW_UP)) {
			JComboBox cb = (JComboBox) arg0.getSource();
			String name = (String) cb.getSelectedItem();

			grewupTextField.setVisible(name.equals(OTHER));
			if (!name.equals(OTHER))
				grewupTextField.setText("");
		} else if (command.equals(FIELD_OF_STUDY)) {
			JComboBox cb = (JComboBox) arg0.getSource();
			String name = (String) cb.getSelectedItem();

			fieldOfStudyTextField.setVisible(name.equals(OTHER));
			if (!name.equals(OTHER))
				fieldOfStudyTextField.setText("");
		}
	}
}
