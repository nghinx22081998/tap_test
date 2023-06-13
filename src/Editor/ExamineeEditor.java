package Editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Common.Constants;
import Common.StringUtils;
import Model.Divisions;
import Model.Examinee;
import Model.Subjects;
import ServiceImp.SubjectImp;

public class ExamineeEditor {
	private JFrame mainFrame;

	private JTextField textNameExaminee;
	private JComboBox<String> cbDivision;
	private JTextField textScore;
	private JButton buttonAdd;
	private JButton buttonDivision;
	private JButton buttonSubject;
	private JTable table;
	private JLabel jLabelmessage;

	private Map<Integer, Subjects> mapSubject = new HashMap<>();
	private Map<String, Divisions> mapDivision = new HashMap<>();
	private static final String[] TYPE_DIVISTION = { "I", "S" };
	Map<String, Integer> mapTitleSumSubject = new HashMap<>();
	private boolean flag = false;

	public ExamineeEditor() {

	}

	public void init() {
		//add data example division
		addDataSampleForDivision(mapDivision);
		//add data example subject
		addDataSampleForSubject(mapSubject);
		getSubjectEditor();
		actionAdd();
		openDivision(this);
		openSubject(this);
	}

	private void getSubjectEditor() {
		this.mainFrame = new JFrame();
		mainFrame.setLayout(null);

		textNameExaminee = new JTextField();
		cbDivision = new JComboBox<>(TYPE_DIVISTION);
		textScore = new JTextField();

		getInputPanel();
		getTablePanel();

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1200, 700);
		mainFrame.setVisible(true);
		mainFrame.setLocation(500, 100);
		mainFrame.setResizable(false);
	}
	

	private void getTablePanel() {

		JPanel panel2 = new JPanel();

		DefaultTableModel defaultTableModel = new DefaultTableModel();

		defaultTableModel.addColumn("Examinee");
		defaultTableModel.addColumn("Division");
		
		for (int indexColumn = 1; indexColumn <= mapSubject.size(); indexColumn++) {
			Subjects subjects = new Subjects();
			subjects = mapSubject.get(new Integer(indexColumn));
			defaultTableModel.addColumn(subjects.getName());

		}



		table = new JTable(defaultTableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(750, 630));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		panel2.add(scrollPane);
		panel2.setBounds(380, 10, 840, 650);

		mainFrame.add(panel2);
		mainFrame.revalidate();
		mainFrame.repaint();

	}

	private void getInputPanel() {

		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setBounds(10, 10, 350, 650);
		//Name Examninee
		JLabel jLabelName = new JLabel("Examinee");
		jLabelName.setBounds(10, 10, 100, 20);
		textNameExaminee.setBounds(80, 10, 150, 20);
		panel1.add(jLabelName);
		panel1.add(textNameExaminee);
		//Type Division
		JLabel jLabelDivision = new JLabel("Division");
		jLabelDivision.setBounds(10, 50, 50, 20);
		cbDivision.setBounds(60, 50, 150, 20);
		panel1.add(jLabelDivision);
		panel1.add(cbDivision);
		//Score
		JLabel jLabelScore = new JLabel("Scores");
		jLabelScore.setBounds(10, 90, 50, 20);
		textScore.setBounds(60, 90, 150, 20);
		
		panel1.add(jLabelScore);
		panel1.add(textScore);
		//Message
		jLabelmessage = new JLabel("");
		jLabelmessage.setBounds(10, 140, 300, 20);
		panel1.add(jLabelmessage);

		buttonAdd = new JButton("Add");
		buttonAdd.setBounds(10, 250, 80, 20);
		panel1.add(buttonAdd);

		buttonDivision = new JButton("Division Edit");
		buttonDivision.setBounds(100, 250, 110, 20);
		panel1.add(buttonDivision);

		buttonSubject = new JButton("Subject Edit");
		buttonSubject.setBounds(220, 250, 110, 20);
		

		panel1.add(buttonSubject);
		panel1.setBackground(Color.LIGHT_GRAY);

		mainFrame.add(panel1);
	}

	/**
	 * Action add
	 */
	private void actionAdd() {
		buttonAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				processAdd();
			}
		});
	}
	
	private void processAdd() {
		
		try {
			String txtScore = textScore.getText();
			
			Examinee examinee = new Examinee();
			examinee.setName(textNameExaminee.getText());
			examinee.setDivisionId(cbDivision.getSelectedItem().toString());
			//check input
			String message = checkInput(examinee, txtScore);
			if(!StringUtils.isEmty(message)) {
				showMessage(message, false);
				return;
			}

			List<Subjects> lstSubject = new ArrayList<>();
			String[] scores = txtScore.split(Constants.REGEX);
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Divisions division = mapDivision.get(examinee.getDivisionId());
			Vector row = new Vector();
			
			row.add(examinee.getName());
			row.add(division.getName());
			
			for (int i = 1; i <= mapSubject.size(); i++) {
				Subjects subject = mapSubject.get(i);
				subject.setScore(Integer.parseInt(scores[i - 1]));
				row.add(scores[i - 1]);
				
				lstSubject.add(subject);
				//count subject
				Integer column = mapTitleSumSubject.get(subject.getDivisionId());
				if (column != null) {
					mapTitleSumSubject.put(subject.getDivisionId(), column + 1);
				} else {
					mapTitleSumSubject.put(subject.getDivisionId(), 1 );
				}
			}
			
			if(!flag) {
				addColumnResult();
				flag = true;
			}
			examinee.setLstSubject(lstSubject);
			
			
			SubjectImp subjectImp = new SubjectImp();
			subjectImp.checkConditionSubject(examinee, mapDivision);
			
			
			for (Map.Entry<String, Divisions> entry : mapDivision.entrySet()) {
				Divisions item = entry.getValue();
				Integer sumDivision = examinee.getMapSumEachDivision().get(item.getCodeDivision());
				row.add(sumDivision);
			}
			
			row.add(examinee.getSummAll());
			row.add(examinee.getStatus());
			
			model.addRow(row);
			
			showMessage("Successful", true);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}

	/**
	 * Action open Division Editor
	 */
	private void openDivision(ExamineeEditor editor) {
		buttonDivision.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DivisionEditor divisionEditor = new DivisionEditor();
				divisionEditor.init(editor);

			}
		});
	}

	/**
	 * open Subject Editor
	 */
	private void openSubject(ExamineeEditor editor) {
		buttonSubject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SubjectEditor subjectEditor = new SubjectEditor();
				subjectEditor.init(editor);

			}
		});
	}
	/**
	 * 
	 * @param mapDivision
	 */
	private void addDataSampleForDivision(Map<String, Divisions> mapDivision) {
		Divisions division1 = new Divisions();
		division1.setCodeDivision("I");
		division1.setName("Humanities");
		division1.setSumConditionDivision(160);

		Divisions division2 = new Divisions();
		division2.setCodeDivision("S");
		division2.setName("Science");
		division2.setSumConditionDivision(160);

		mapDivision.put(division1.getCodeDivision(), division1);
		mapDivision.put(division2.getCodeDivision(), division2);
	}
	/**
	 * 
	 * @param mapSubjects
	 */
	private void addDataSampleForSubject(Map<Integer, Subjects> mapSubjects) {
		Subjects sb1 = new Subjects();
		sb1.setName("Math");
		sb1.setColumnPosition(1);
		sb1.setDivisionId("I");

		Subjects sb2 = new Subjects();
		sb2.setName("Sciene");
		sb2.setColumnPosition(2);
		sb2.setDivisionId("S");

		mapSubjects.put(sb1.getColumnPosition(), sb1);
		mapSubjects.put(sb2.getColumnPosition(), sb2);
	}
	/**
	 * 
	 */
	private void addColumnResult() {
		DefaultTableModel defaultTableModel = (DefaultTableModel) table.getModel();
		//add column summary
		for (Map.Entry<String, Divisions> index : mapDivision.entrySet()) {
			Divisions division = index.getValue();
			defaultTableModel
						.addColumn(division.getName() + " " + mapTitleSumSubject.get(division.getCodeDivision()) + " Subject");
		}
		defaultTableModel.addColumn("All Subject");
		defaultTableModel.addColumn("Result");
	}
	/**
	 * 
	 * @param examinee
	 * @param scores
	 * @return
	 */
	private String checkInput(Examinee examinee, String scores) {
		String s = new String();
		if(StringUtils.isEmty(examinee.getName())) {
			s = StringUtils.getMessage("Examinee", s);
		}
		
		if(StringUtils.isEmty(scores)) {
			s =  StringUtils.getMessage("Scores", s);
		}
		return s;
	}
	
	/**
	 * 
	 * @param message
	 * @param isSuccess
	 */
	private void showMessage(String message, boolean isSuccess) {
		if(!isSuccess) {
			jLabelmessage.setText("Please enter: " + message);
			jLabelmessage.setForeground(Color.RED);
		}else {
			jLabelmessage.setText(message);
			jLabelmessage.setForeground(Color.BLUE);
		}
	}
	
	/**
	 * function add new column
	 * @param subject
	 */
	public void addNewColumn(Subjects subject) {
		DefaultTableModel defaultTableModel = (DefaultTableModel) table.getModel();
		defaultTableModel.addColumn(subject.getName());
	}

	public Map<String, Divisions> getMapDivision() {
		return mapDivision;
	}

	public Map<Integer, Subjects> getMapSubjects() {
		return mapSubject;
	}

	public void setSubject(Subjects subject) {
		mapSubject.put(subject.getColumnPosition(), subject);
	}

	public void setDivision(Divisions division) {
		mapDivision.put(division.getCodeDivision(), division);
	}

	public void reLoadDivision() {
		cbDivision.removeAllItems();
		for (Map.Entry<String, Divisions> division : mapDivision.entrySet()) {
			cbDivision.addItem(division.getKey());
		}
	}
	
}
