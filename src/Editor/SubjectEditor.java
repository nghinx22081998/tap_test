package Editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Common.StringUtils;
import Model.Divisions;
import Model.Subjects;

public class SubjectEditor {

	private JFrame mainFrame;
	private JTable table;
	private JComboBox<String> cbDivisionCode;
	private JTextField textNameSubject;
	private JButton buttonAdd;
	private JButton buttonClose;
	private JLabel jLabelmessage;

	public void init(ExamineeEditor editor) {
		getSubjectEditor(editor);
		add(editor);
		close();
	}

	private void getSubjectEditor(ExamineeEditor editor) {
		String[] columnsNames = { "Column Position", "Name", "Division" };

		mainFrame = new JFrame();
		table = new JTable();

		cbDivisionCode = new JComboBox<>();
		textNameSubject = new JTextField();
		buttonAdd = new JButton("Add");
		buttonClose = new JButton("Close");

		Panel panel1 = new Panel();
		panel1.setLayout(null);
		//
		JLabel labelCodeDivistion = new JLabel("Division");
		labelCodeDivistion.setBounds(10, 10, 100, 20);
		cbDivisionCode.setBounds(80, 10, 150, 20);
		getDivisionCode(editor);
		panel1.add(labelCodeDivistion);
		panel1.add(cbDivisionCode);

		//Name Subject
		JLabel labelNameSubject = new JLabel("Name");
		labelNameSubject.setBounds(10, 50, 150, 20);
		textNameSubject.setBounds(80, 50, 150, 20);
		panel1.add(labelNameSubject);
		panel1.add(textNameSubject);

		//
		jLabelmessage = new JLabel("");
		jLabelmessage.setBounds(10, 100, 300, 20);
		panel1.add(jLabelmessage);
		
		//Table
		DefaultTableModel defaultTableModel = new DefaultTableModel();
		for (String column : columnsNames) {
			defaultTableModel.addColumn(column);
		}

		table = new JTable(defaultTableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(350, 250));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(300, 10, 500, 400);

		getTableData(editor);

		panel1.add(scrollPane);

		//Button
		buttonAdd.setBounds(80, 250, 80, 20);
		panel1.add(buttonAdd);
		buttonClose.setBounds(180, 250, 100, 20);
		panel1.add(buttonClose);

		panel1.setBackground(Color.LIGHT_GRAY);

		mainFrame.add(panel1);

		mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		mainFrame.setSize(900, 600);
		mainFrame.setVisible(true);
		mainFrame.setLocation(600, 200);
		mainFrame.setResizable(false);
	}

	private void add(ExamineeEditor editor) {
		buttonAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String name = textNameSubject.getText();
				Object location = table.getValueAt(table.getRowCount() - 1, 0);
				String division = cbDivisionCode.getSelectedItem().toString();

				//
				Subjects sb = new Subjects();
				sb.setDivisionId(division);
				sb.setName(name);
				sb.setColumnPosition(Integer.parseInt(String.valueOf(location)) + 1);
				
				String message = checkInput(sb);
				if(!StringUtils.isEmty(message)) {
					showMessage(message, false);
					return;
				}
				
				//
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				Vector row = new Vector();
				row.add(Integer.parseInt(String.valueOf(location)) + 1);
				row.add(name);
				row.add(division);
				model.addRow(row);

				
				editor.setSubject(sb);
				editor.addNewColumn(sb);// add new column
				
				showMessage("Successful", true);
				
			
			}
		});
	}
	
	/**
	 * 
	 */
	private void close() {
		buttonClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
	}

	/**
	 * 
	 * @param examineeEditor
	 */
	private void getDivisionCode(ExamineeEditor examineeEditor) {
		cbDivisionCode.removeAllItems();
		Map<String, Divisions> mapDivision = examineeEditor.getMapDivision();
		for (Map.Entry<String, Divisions> division : mapDivision.entrySet()) {
			cbDivisionCode.addItem(division.getKey());
		}
	}
	/**
	 * 
	 * @param editor
	 */
	private void getTableData(ExamineeEditor editor) {
		Map<Integer, Subjects> mapSubject = editor.getMapSubjects();
		
		if(mapSubject.isEmpty()) {
			return;
		}
		
		for (Map.Entry<Integer, Subjects> pair : mapSubject.entrySet()) {
			
			Subjects subjects = pair.getValue();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Vector row = new Vector();
			row.add(subjects.getColumnPosition());
			row.add(subjects.getName());
			row.add(subjects.getDivisionId());
			model.addRow(row);
		}
	}
	
	/**
	 * 
	 * @param subject
	 * @return
	 */
	private String checkInput(Subjects subject) {
		String s = new String();
		if(StringUtils.isEmty(subject.getName())) {
			s = StringUtils.getMessage("Name", s);
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

}
