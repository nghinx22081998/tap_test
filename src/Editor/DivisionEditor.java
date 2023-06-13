package Editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Common.StringUtils;
import Common.TextFieldNumber;
import Model.Divisions;

public class DivisionEditor {

	private JFrame mainFrame;
	private JTable table;
	private JTextField textDivisionCode;
	private TextFieldNumber textTotalDivision;
	private JTextField textNameDivision;
	private JButton buttonAdd;
	private JButton buttonClose;
	private JLabel jLabelmessage;

	private static final String[] columnHeader = { "Code", "Name", "Condition Division" };
	
	public void init(ExamineeEditor editor) {
		getDivisionEditor(editor);
		add(editor);
		close();
	}

	private void getDivisionEditor(ExamineeEditor editor) {
		Map<String, Divisions> mapDivision = editor.getMapDivision();

		mainFrame = new JFrame();
		table = new JTable();

		textDivisionCode = new JTextField();
		textTotalDivision = new TextFieldNumber();
		textNameDivision = new JTextField();
		buttonAdd = new JButton("Add");
		buttonClose = new JButton("Close");

		Panel panel1 = new Panel();
		panel1.setLayout(null);
		//Code
		JLabel labelCodeDivistion = new JLabel("Code");
		labelCodeDivistion.setBounds(10, 10, 100, 20);
		textDivisionCode.setBounds(80, 10, 150, 20);
		panel1.add(labelCodeDivistion);
		panel1.add(textDivisionCode);

		//Name
		JLabel labelNameDivistion = new JLabel("Name");
		labelNameDivistion.setBounds(10, 50, 100, 20);
		textNameDivision.setBounds(80, 50, 150, 20);
		panel1.add(labelNameDivistion);
		panel1.add(textNameDivision);

		//
		JLabel labelTotalScoreDivistion = new JLabel("Score Condition");
		labelTotalScoreDivistion.setBounds(10, 90, 150, 20);
		textTotalDivision.setBounds(140, 90, 150, 20);
		panel1.add(labelTotalScoreDivistion);
		panel1.add(textTotalDivision);
		
		//
		jLabelmessage = new JLabel("");
		jLabelmessage.setBounds(10, 120, 300, 20);
		panel1.add(jLabelmessage);

		//Table
		DefaultTableModel defaultTableModel = new DefaultTableModel();
		for (String column : columnHeader) {
			defaultTableModel.addColumn(column);
		}

		table = new JTable(defaultTableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(350, 250));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(300, 10, 500, 400);

		getTableData(mapDivision);

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
				try {
					Divisions division = new Divisions();
					division.setCodeDivision(textDivisionCode.getText());
					division.setName(textNameDivision.getText());
					int scoreDivision = textTotalDivision.getText() != "" ? Integer.parseInt(textTotalDivision.getText()) : 0;
					
					division.setSumConditionDivision(scoreDivision);
					
					String message = checkInput(division);
					if(!StringUtils.isEmty(message)) {
						showMessage(message, false);
						return;
					}
					
					editor.setDivision(division);
					editor.reLoadDivision();

					DefaultTableModel model = (DefaultTableModel) table.getModel();
					Vector row = new Vector();
					row.add(division.getCodeDivision());
					row.add(division.getName());
					row.add(division.getSumConditionDivision());
					model.addRow(row);
					
					showMessage("Successful", true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});
	}

	private void close() {
		buttonClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();

			}
		});
	}

	private void getTableData(Map<String, Divisions> mapDivision) {
		for (Map.Entry<String, Divisions> pair : mapDivision.entrySet()) {
			Divisions division = pair.getValue();

			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Vector row = new Vector();
			row.add(division.getCodeDivision());
			row.add(division.getName());
			row.add(division.getSumConditionDivision());
			model.addRow(row);
		}
	}

	/**
	 * 
	 * @param subject
	 * @return
	 */
	private String checkInput(Divisions   subject) {
		String s = new String();
		
		if(StringUtils.isEmty(subject.getCodeDivision())) {
			s = StringUtils.getMessage("Code", s);
		}
		
		if(StringUtils.isEmty(subject.getName())) {
			s = StringUtils.getMessage("Name", s);
		}
		
		if(subject.getSumConditionDivision() == 0) {
			s = StringUtils.getMessage("Score Condition", s);
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
