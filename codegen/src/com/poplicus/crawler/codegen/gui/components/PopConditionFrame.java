package com.poplicus.crawler.codegen.gui.components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.poplicus.crawler.codegen.Condition;
import com.poplicus.crawler.codegen.definitions.ConditionDefinition;
import com.poplicus.crawler.codegen.definitions.Conditions;
import com.poplicus.crawler.codegen.definitions.CrawlerDefinition;
import com.poplicus.crawler.codegen.definitions.ElseDefinition;
import com.poplicus.crawler.codegen.definitions.ElseIfDefinition;
import com.poplicus.crawler.codegen.definitions.IfDefinition;
import com.poplicus.crawler.codegen.definitions.IfElseElementsDef;
import com.poplicus.crawler.codegen.definitions.MainIfDefinition;
import com.poplicus.crawler.codegen.definitions.MainIfElementsDef;
import com.poplicus.crawler.codegen.definitions.WebPageDefinition;
import com.poplicus.crawler.codegen.factory.ConditionCache;
import com.poplicus.crawler.codegen.factory.CrawlerCache;
import com.poplicus.crawler.codegen.gui.components.PopTreeNode.PopTreeNodeType;
import com.poplicus.crawler.codegen.utilities.CodeGenUtil;
import com.poplicus.crawler.codegen.utilities.config.CodeGenConfigCache;

public class PopConditionFrame extends PopJFrame {

	private static final long serialVersionUID = -3704486839375164398L;

	private static FontManager fontManager = FontManager.getInstance();
	private static CodeGenUtil codeGenUtil = CodeGenUtil.getInstance();
	private static ConditionCache conditionCache = ConditionCache.getInstance();
	private static CrawlerCache crawlerCache = CrawlerCache.getInstance();
	private static CodeGenConfigCache configCache = CodeGenConfigCache.getInstance();

	private PopButton b_Done = null;
	private PopButton b_Cancel = null;

	private PopComboBox cBoxLeft = null;
	private PopComboBox cBoxOperator = null;
	private PopComboBox cBoxRight = null;

	private ButtonGroup rightConditionBGroup = null;
	private PopCheckBox rightExistingCondition = null;
	private PopCheckBox rightNewCondition = null;

	private ButtonGroup leftConditionBGroup = null;
	private PopCheckBox leftExistingCondition = null;
	private PopCheckBox leftNewCondition = null;

	private String con_LO_Selected_CB = null;
	private String con_RO_Selected_CB = null;

	private PopTextField leftOperandTF = null;
	private PopTextField rightOperandTF = null;

	private PopCheckBox superConditionCBX = null;

	private PopTable conditionsTable = null;

	private PopButton bCreate = null;
	private PopButton bUpdate = null;
	private PopButton bDelete = null;
	private PopButton bClear = null;

	private int conTableRowNumber = -1;
	private String conditionName = null;

	public static PopTree rootTree = null;

	public PopConditionFrame() {
		this.setLayout(new GridBagLayout());
		this.addWindowListener(new CodeGenWindowAdapter());
		Toolkit.getDefaultToolkit( ).setDynamicLayout(true);
		this.setTitle("Create If-Else Condition");

		javax.swing.UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 11));
		javax.swing.UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.PLAIN, 11));

		this.add(getPrimaryBrowserControlPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.1,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		this.add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.01, 0.1,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	private void populateOperator(JComboBox comboBox) {
		comboBox.addItem("");
		List<String> operators = configCache.getConfigurations().getConditionOperators();
		for(String operator : operators) {
			comboBox.addItem(operator);
		}
//		comboBox.addItem("Contains");
//		comboBox.addItem("DoesNotContain");
//		comboBox.addItem("LogicalAND");
//		comboBox.addItem("LogicalOR");
//		comboBox.addItem("Equals");
//		comboBox.addItem("NotEquals");
//		comboBox.addItem("LessThan");
//		comboBox.addItem("LessThanAndEquals");
//		comboBox.addItem("Boolean");
//		comboBox.addItem("LogicalComplement");
	}

	private void populateConditions(JComboBox comboBox) {
		List<String> conditions = conditionCache.getConditionNames();
		comboBox.removeAllItems();
		comboBox.addItem("");
		for(String con : conditions) {
			comboBox.addItem(con);
		}
	}

	private ConditionDefinition getConditionDefinition(String leftOperand, String operator, String rightOperand,
			  boolean superCondition, String conditionName) {
		ConditionDefinition conDef = null;
		if(conditionName != null) {
			Condition condition = conditionCache.get(conditionName);
			if(leftOperand.contains("pop:crwl:cons:")) {
				condition.setLeftSideValue(conditionCache.get(leftOperand));
			} else {
				condition.setLeftSideValue(leftOperand);
			}
			if(rightOperand.contains("pop:crwl:cons:")) {
				condition.setRightSideValue(conditionCache.get(rightOperand));
			} else {
				condition.setRightSideValue(rightOperand);
			}

			condition.setOperator(codeGenUtil.getConditionOperator(operator));
			condition.setSuperCondition(superCondition);
			conditionCache.put(conditionName, condition);

			conDef = new ConditionDefinition(false);
			if(condition.getLeftSideValue() instanceof Condition) {
				conDef.setLshOperand(((Condition) condition.getLeftSideValue()).getName());
			} else {
				conDef.setLshOperand((String) condition.getLeftSideValue());
			}
			if(condition.getRightSideValue() instanceof Condition) {
				conDef.setRhsOperand(((Condition) condition.getRightSideValue()).getName());
			} else {
				conDef.setRhsOperand((String) condition.getRightSideValue());
			}
			conDef.setOperator(condition.getOperator().toString());
			conDef.setSuperCondition(condition.isSuperCondition());
		} else {
			conDef = new ConditionDefinition();
			conDef.setLshOperand(leftOperand);
			conDef.setRhsOperand(rightOperand);
			conDef.setOperator(operator);
			conDef.setSuperCondition(superCondition);

			Condition condition = new Condition();
			if(leftOperand.contains("pop:crwl:cons:")) {
				condition.setLeftSideValue(conditionCache.get(leftOperand));
			} else {
				condition.setLeftSideValue(leftOperand);
			}
			condition.setOperator(codeGenUtil.getConditionOperator(operator));
			if(rightOperand.contains("pop:crwl:cons:")) {
				condition.setRightSideValue(conditionCache.get(rightOperand));
			} else {
				condition.setRightSideValue(rightOperand);
			}
			condition.setSuperCondition(superCondition);
			condition.setName(conDef.getName());

			conditionCache.put(conDef.getName(), condition);
		}
		return conDef;
	}

	//PBC - Start
	private JPanel getPrimaryBrowserControlPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(getCRUDConditionPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.02,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
		return mainPanel;
	}

	private JPanel getCRUDConditionPanel() {
		JPanel crudConPanel = new JPanel();
		crudConPanel.setLayout(new GridBagLayout());

		//left operand panel - start
		JPanel leftOperandPanel = new JPanel();
		leftOperandPanel.setLayout(new GridBagLayout());

		leftExistingCondition = new PopCheckBox("Use Existing Condition");
		leftExistingCondition.setName("PBC_CBX1");
		leftExistingCondition.addItemListener(new Con_CBX_Left_Listener());

		leftNewCondition = new PopCheckBox("Other String");
		leftNewCondition.setName("PBC_CBX2");
		leftNewCondition.addItemListener(new Con_CBX_Left_Listener());

		leftConditionBGroup = new ButtonGroup();
		leftConditionBGroup.add(leftExistingCondition);
		leftConditionBGroup.add(leftNewCondition);

		cBoxLeft = new PopComboBox();
		cBoxLeft.setEnabled(false);
		leftOperandTF = new PopTextField();
		leftOperandTF.setEnabled(false);

		leftOperandPanel.add(leftExistingCondition, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(cBoxLeft, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftNewCondition, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftOperandTF, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		crudConPanel.add(leftOperandPanel, new GridBagConstraints(0, 0, 2, 1, 1.0, 0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		PopTitledBorder leftTitledBorder = new PopTitledBorder("Left Operand");
		leftTitledBorder.setTitleFont(fontManager.getBorderFontPlain());
		leftOperandPanel.setBorder(leftTitledBorder);
		//left operand panel - end

		//right operand panel - start
		JPanel rightOperandPanel = new JPanel();
		rightOperandPanel.setLayout(new GridBagLayout());

		rightExistingCondition = new PopCheckBox("Use Existing Condition");
		rightExistingCondition.setName("PBC_CBX3");
		rightExistingCondition.addItemListener(new Con_CBX_Right_Listener());

		rightNewCondition = new PopCheckBox("Other String");
		rightNewCondition.setName("PBC_CBX4");
		rightNewCondition.addItemListener(new Con_CBX_Right_Listener());

		rightConditionBGroup = new ButtonGroup();
		rightConditionBGroup.add(rightExistingCondition);
		rightConditionBGroup.add(rightNewCondition);

		cBoxRight = new PopComboBox();
		cBoxRight.setEnabled(false);
		rightOperandTF = new PopTextField();
		rightOperandTF.setEnabled(false);

		rightOperandPanel.add(rightExistingCondition, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(cBoxRight, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightNewCondition, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightOperandTF, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		crudConPanel.add(rightOperandPanel, new GridBagConstraints(0, 1, 2, 1, 1.0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		PopTitledBorder rightTitledBorder = new PopTitledBorder("Right Operand");
		rightTitledBorder.setTitleFont(fontManager.getBorderFontPlain());
		rightOperandPanel.setBorder(rightTitledBorder);
		//right operand panel - end

		//operator - start
		JPanel operatorPanel = new JPanel();
		operatorPanel.setLayout(new GridBagLayout());
		PopLabel labelOperator = new PopLabel("Operator");
		cBoxOperator = new PopComboBox();
		populateOperator(cBoxOperator);

		superConditionCBX = new PopCheckBox("Super Condition");

		operatorPanel.add(labelOperator, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(cBoxOperator, new GridBagConstraints(1, 0, 1, 1, 0.1, 1.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(superConditionCBX, new GridBagConstraints(2, 0, 1, 1, 0.1, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		crudConPanel.add(operatorPanel, new GridBagConstraints(0, 2, 1, 1, 0.5, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));

		crudConPanel.add(getConditionTablePanel(), new GridBagConstraints(0, 3, 2, 2, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));
		//operator - end

		//control buttons - start
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		JPanel invisiblePanel = new JPanel();

		bCreate = new PopButton("Create");
		bUpdate = new PopButton("Update");
		bDelete = new PopButton("Delete");
		bClear = new PopButton("Reset");

		buttonPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bCreate.setName("PBC_B_Create");
		bCreate.addActionListener(new Con_Button_Action_Listener());
		buttonPanel.add(bCreate, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bUpdate.setName("PBC_B_Update");
		bUpdate.addActionListener(new Con_Button_Action_Listener());
		buttonPanel.add(bUpdate, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bDelete.setName("PBC_B_Delete");
		bDelete.addActionListener(new Con_Button_Action_Listener());
		buttonPanel.add(bDelete, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		bClear.setName("PBC_B_Clear");
		bClear.addActionListener(new Con_Button_Action_Listener());
		buttonPanel.add(bClear, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		crudConPanel.add(buttonPanel, new GridBagConstraints(1, 2, 1, 1, 1.0, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 8, 0), 0, 0));

		updateEnableStateOfUpdateAndDeleteButtons();
		//control buttons - end

		PopTitledBorder titledBorder = new PopTitledBorder("Create / Edit / Delete Conditions");
		crudConPanel.setBorder(titledBorder);

		populateConditions(cBoxLeft);
		populateConditions(cBoxRight);

		return crudConPanel;
	}

	private void updateEnableStateOfUpdateAndDeleteButtons() {
		if((conTableRowNumber > -1) && (conditionName != null)) {
			bUpdate.setEnabled(true);
			bDelete.setEnabled(true);
		} else {
			bUpdate.setEnabled(false);
			bDelete.setEnabled(false);
		}
	}


	private JScrollPane getConditionTablePanel() {
		Object[] columnNames = {"Condition Name", "Left Operand", "Operator", "Right Operand", "Super Condition"};
		Object[][] rowData = new Object[0][0];
		conditionsTable = new PopTable(rowData, columnNames);
		conditionsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
		conditionsTable.addMouseListener(new Condition_Table_Mouse_Adapter());

		JScrollPane scrollPane = new JScrollPane(conditionsTable);
		conditionsTable.setFillsViewportHeight(true);

		return scrollPane;
	}

	private void createCondition() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		String operator = null;
		if(con_LO_Selected_CB != null && con_RO_Selected_CB != null
				&& cBoxOperator.getSelectedItem() != null) {
			if(con_LO_Selected_CB.equals("PBC_CBX1")) {
				leftOperand = (String) cBoxLeft.getSelectedItem();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Left Operand.");
					cBoxLeft.requestFocus();
					return;
				}
			} else if(con_LO_Selected_CB.equals("PBC_CBX2")) {
				leftOperand = leftOperandTF.getText();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Left Operand.");
					leftOperandTF.requestFocus();
					return;
				}
			}

			if(con_RO_Selected_CB.equals("PBC_CBX3")) {
				rightOperand = (String) cBoxRight.getSelectedItem();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Right Operand.");
					cBoxRight.requestFocus();
					return;
				}
			} else if(con_RO_Selected_CB.equals("PBC_CBX4")) {
				rightOperand = rightOperandTF.getText();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Right Operand.");
					rightOperandTF.requestFocus();
					return;
				}
			}
			operator = (String) cBoxOperator.getSelectedItem();
			if(!(operator != null && operator.length() > 0)) {
				JOptionPane.showMessageDialog(this, "Select a valid value from \'Operator\' drop down.");
				cBoxOperator.requestFocus();
				return;
			}

			conDef = getConditionDefinition(leftOperand, operator, rightOperand, superConditionCBX.isSelected(), null);
			conditionsTable.addRow(new Object[]{conDef.getName(),
				conDef.getLhsOperand(), conDef.getOperator(), conDef.getRhsOperand(), conDef.isSuperCondition()});
			populateConditions(cBoxLeft);
			populateConditions(cBoxRight);
			clearConditionFields();

			conTableRowNumber = -1;
			conditionName = null;

			con_LO_Selected_CB = null;
			con_RO_Selected_CB = null;
			cBoxOperator.setSelectedIndex(-1);
		} else {
			JOptionPane.showMessageDialog(this, "Left Operand, Right Operand and Operator are mandatory fields.");
		}
	}

	private void clearConditionFields() {
		leftConditionBGroup.remove(leftExistingCondition);
		leftExistingCondition.setSelected(false);
		leftConditionBGroup.add(leftExistingCondition);

		cBoxLeft.setSelectedIndex(-1);

		leftConditionBGroup.remove(leftNewCondition);
		leftNewCondition.setSelected(false);
		leftConditionBGroup.add(leftNewCondition);

		leftOperandTF.setText(null);

		cBoxLeft.setEnabled(false);
		leftOperandTF.setEnabled(false);

		rightConditionBGroup.remove(rightExistingCondition);
		rightExistingCondition.setSelected(false);
		rightConditionBGroup.add(rightExistingCondition);

		cBoxRight.setSelectedIndex(-1);

		rightConditionBGroup.remove(rightNewCondition);
		rightNewCondition.setSelected(false);
		rightConditionBGroup.add(rightNewCondition);

		rightOperandTF.setText(null);

		cBoxRight.setEnabled(false);
		rightOperandTF.setEnabled(false);

		cBoxOperator.setSelectedIndex(-1);
		superConditionCBX.setSelected(false);

		this.repaint();
	}

	private void updateCondition() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		if(con_LO_Selected_CB != null && con_RO_Selected_CB != null) {
			if(con_LO_Selected_CB.equals("PBC_CBX1")) {
				leftOperand = (String) cBoxLeft.getSelectedItem();
			} else if(con_LO_Selected_CB.equals("PBC_CBX2")) {
				leftOperand = leftOperandTF.getText();
			}

			if(con_RO_Selected_CB.equals("PBC_CBX3")) {
				rightOperand = (String) cBoxRight.getSelectedItem();
			} else if(con_RO_Selected_CB.equals("PBC_CBX4")) {
				rightOperand = rightOperandTF.getText();
			}
			conDef = getConditionDefinition(leftOperand, (String) cBoxOperator.getSelectedItem(),
				rightOperand, superConditionCBX.isSelected(), conditionName);

			conditionsTable.getModel().setValueAt(conDef.getLhsOperand(), conTableRowNumber, 1);
			conditionsTable.getModel().setValueAt(conDef.getOperator(), conTableRowNumber, 2);
			conditionsTable.getModel().setValueAt(conDef.getRhsOperand(), conTableRowNumber, 3);
			conditionsTable.getModel().setValueAt(conDef.isSuperCondition(), conTableRowNumber, 4);

			populateConditions(cBoxLeft);
			populateConditions(cBoxRight);
			clearConditionFields();
			bCreate.setEnabled(true);

			conTableRowNumber = -1;
			conditionName = null;

			updateEnableStateOfUpdateAndDeleteButtons();
		}

	}

	private void deleteCondition() {
		conditionCache.remove(conditionName);

		((DefaultTableModel) conditionsTable.getModel()).removeRow(conTableRowNumber);

		populateConditions(cBoxLeft);
		populateConditions(cBoxRight);
		clearConditionFields();
		bCreate.setEnabled(true);

		conTableRowNumber = -1;
		conditionName = null;

		updateEnableStateOfUpdateAndDeleteButtons();
	}

	private void clearCondition() {
		populateConditions(cBoxLeft);
		populateConditions(cBoxRight);
		clearConditionFields();
		bCreate.setEnabled(true);

		conTableRowNumber = -1;
		conditionName = null;

		updateEnableStateOfUpdateAndDeleteButtons();
	}

	private void populateConditionFields() {
		Condition condition = conditionCache.get(conditionName);
		clearConditionFields();
		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getLeftSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition.setSelected(true);
				cBoxLeft.setEnabled(true);
				cBoxLeft.setSelectedItem(((Condition) condition.getLeftSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getLeftSideValue()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition.setSelected(true);
				cBoxLeft.setEnabled(true);
				cBoxLeft.setSelectedItem(condition.getLeftSideValue());
			} else {
				leftNewCondition.setSelected(true);
				leftOperandTF.setEnabled(true);
				leftOperandTF.setText((String) condition.getLeftSideValue());
			}
		}

		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getRightSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition.setSelected(true);
				cBoxRight.setEnabled(true);
				cBoxRight.setSelectedItem(((Condition) condition.getRightSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getRightSideValue()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition.setSelected(true);
				cBoxRight.setEnabled(true);
				cBoxRight.setSelectedItem(condition.getRightSideValue());
			} else {
				rightNewCondition.setSelected(true);
				rightOperandTF.setEnabled(true);
				rightOperandTF.setText((String) condition.getRightSideValue());
			}
		}

		cBoxOperator.setSelectedItem(condition.getOperator().toString());
		superConditionCBX.setSelected(condition.isSuperCondition());
		bCreate.setEnabled(false);
	}
	// PBC - End

	private JPanel getButtonsPanel() {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridBagLayout());

		b_Done = new PopButton("Done");
		b_Done.setName("B_Done");
		b_Done.addActionListener(new ButtonsPanel_Buttons_Action_Listener(this));

		b_Cancel = new PopButton("Cancel");
		b_Cancel.setName("B_Cancel");
		b_Cancel.addActionListener(new ButtonsPanel_Buttons_Action_Listener(this));

		JPanel invisiblePanel = new JPanel();

		buttonsPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		buttonsPanel.add(b_Done, new GridBagConstraints(2, 0, 1, 1, 0.1, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		buttonsPanel.add(b_Cancel, new GridBagConstraints(3, 0, 1, 1, 0.1, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		return buttonsPanel;
	}

	private StringBuffer getSuperCondition() {
		StringBuffer sbf = null;
		String conditionName = null;
		int noOfRows = conditionsTable.getDefaultModel().getRowCount();
		int noOfColumns = conditionsTable.getDefaultModel().getColumnCount();
		if(conditionsTable.getDefaultModel().getColumnName(noOfColumns - 1).equalsIgnoreCase("Super Condition")) {
			for(int index = 0; index < noOfRows; index ++) {
				if(((Boolean) conditionsTable.getDefaultModel().getValueAt(index, (noOfColumns - 1))).booleanValue()) {
					conditionName = (String) conditionsTable.getDefaultModel().getValueAt(index, 0);
				}
			}
		}
		if(conditionName != null) {
			sbf = conditionCache.get(conditionName).toStringBuffer();
		}
		return sbf;
	}

	private boolean validateConditions(PopTable conTable) {
		boolean superCondition = false;
		boolean notASingleSuperCondition = true;
		int noOfRows = conTable.getDefaultModel().getRowCount();
		for(int index = 0; index < noOfRows; index ++) {
			if(((Boolean) conTable.getDefaultModel().getValueAt(index, 4)).booleanValue()) {
				notASingleSuperCondition = false;
				if(!superCondition) {
					superCondition = ((Boolean) conTable.getDefaultModel().getValueAt(index, 4)).booleanValue();
				} else {
					return false;
				}
			}
		}
		if(notASingleSuperCondition) {
			return false;
		} else {
			return true;
		}
	}

	private int getNumberOfSuperConditions(PopTable conTable) {
		int noOfSuperConditions = 0;
		int noOfRows = conTable.getDefaultModel().getRowCount();
		for(int index = 0; index < noOfRows; index ++) {
			if(((Boolean) conTable.getDefaultModel().getValueAt(index, 4)).booleanValue()) {
				++ noOfSuperConditions;
			}
		}
		return noOfSuperConditions;
	}

	private boolean validateConditions() {
		boolean validConditions = true;
		int noOfRows = conditionsTable.getDefaultModel().getRowCount();
		if(noOfRows > 0) {
			if(!validateConditions(conditionsTable)) {
				int noOfSuperConditions = getNumberOfSuperConditions(conditionsTable);
				if(noOfSuperConditions > 1) {
					validConditions = false;
					JOptionPane.showMessageDialog(this, "There should be only one Super Condition.");
				} else if(noOfSuperConditions <= 0) {
					validConditions = false;
					JOptionPane.showMessageDialog(this, "There should be atleast one Super Condition.");
				}
				return validConditions;
			}

		} else {
			validConditions = false;
			JOptionPane.showMessageDialog(this, "There should be atleast one Super Condition.");
			return validConditions;
		}
		return validConditions;
	}


	private void addConditionsToTree(PopConditionFrame conFrame) {
		rootTree.getContainingFrame().setEnabled(false);
		PopTreeNode openingNode = null;
		PopTreeNode closingNode = null;
		PopTreeNode localParentNode = null;
		if(getParentNode() != null) {
			localParentNode = getParentNode();
		} else {
			localParentNode = (PopTreeNode) rootTree.getSelectionPath().getLastPathComponent();
		}
		if(localParentNode instanceof PopTreeNode) {
			String superCondition = getSuperCondition().toString();
			if(superCondition != null && superCondition.length() > 0) {
				openingNode = new PopTreeNode(getDisplayText(superCondition) + " {", true, getNodeType());
				if(getNodeType().equals(PopTreeNodeType.Else_If) || getNodeType().equals(PopTreeNodeType.Else)) {
					if(getPeerIfNode() != null) {
						if(getNodeType().equals(PopTreeNodeType.Else)) {
							getPeerIfNode().setAddedElse(true);
						}
						openingNode.setPeerIfNode(getPeerIfNode());
					}
				} else if(getNodeType().equals(PopTreeNodeType.If)) {
					updateSiblingIfs(localParentNode, true);
				}
				openingNode.setParentNodeID(localParentNode.getNodeID());
				openingNode.setParentNodeType(localParentNode.getNodeType());
				localParentNode.add(openingNode);

				closingNode = new PopTreeNode("}", true, getNodeType());
				closingNode.setClosingTag(true);
				localParentNode.add(closingNode);
				for (int i = 0; i < rootTree.getRowCount(); i++) {
			         rootTree.expandRow(i);
				}
			}
		}

		conFrame.dispose();
		rootTree.getContainingFrame().setEnabled(true);
		rootTree.updateUI();
		rootTree.getContainingFrame().setVisible(true);

		addConditionsToDefinition(openingNode);
	}

	private void updateSiblingIfs(PopTreeNode parentNode, boolean siblingIfStatus) {
		@SuppressWarnings("unchecked")
		Enumeration<PopTreeNode> childs = parentNode.children();
		PopTreeNode tempNode = null;
		while(childs.hasMoreElements()) {
			tempNode = childs.nextElement();
			if(tempNode.getNodeType().equals(PopTreeNodeType.If)
				|| tempNode.getNodeType().equals(PopTreeNodeType.Else_If)
				|| tempNode.getNodeType().equals(PopTreeNodeType.Else)) {
				tempNode.setSiblingIf(siblingIfStatus);
			}
		}
	}

	public void addElseToTree(PopConditionFrame conFrame) {
		rootTree.getContainingFrame().setEnabled(false);
		PopTreeNode openingNode = null;
		PopTreeNode closingNode = null;
		PopTreeNode localParentNode = null;
		if(getParentNode() != null) {
			localParentNode = getParentNode();
		} else {
			localParentNode = (PopTreeNode) rootTree.getSelectionPath().getLastPathComponent();
		}
		if(localParentNode instanceof PopTreeNode) {
			openingNode = new PopTreeNode("else {", true, getNodeType());
			if(getNodeType().equals(PopTreeNodeType.Else)) {
				if(getPeerIfNode() != null) {
					getPeerIfNode().setAddedElse(true);
					openingNode.setPeerIfNode(getPeerIfNode());
				}
			}
			openingNode.setParentNodeID(localParentNode.getNodeID());
			openingNode.setParentNodeType(localParentNode.getNodeType());
			localParentNode.add(openingNode);

			closingNode = new PopTreeNode("}", true, getNodeType());
			closingNode.setClosingTag(true);
			localParentNode.add(closingNode);
			for (int i = 0; i < rootTree.getRowCount(); i++) {
		         rootTree.expandRow(i);
			}
		}

		conFrame.dispose();
		rootTree.getContainingFrame().setEnabled(true);
		rootTree.updateUI();
		rootTree.getContainingFrame().setVisible(true);

		addConditionsToDefinition(openingNode);
	}

	private void addConditionsToDefinition(PopTreeNode openingNode) {
		if(openingNode != null) {
			CrawlerDefinition crawlerDef = crawlerCache.get(getSourceID());
			WebPageDefinition webPageDef = crawlerDef.getWebPage(getUniqueNameOfWebPage());

			if(openingNode.getParentNodeType().equals(PopTreeNodeType.None)) {
				MainIfDefinition mainIfDef = webPageDef.getMainIF();
				if(mainIfDef == null) {
					mainIfDef = new MainIfDefinition();
				}
				mainIfDef.setMainIfElementsDef(getMainIfElseElementsDef(openingNode));
				webPageDef.setMainIF(mainIfDef);
			} else {
				webPageDef.addIfElseElementsDef(getIfElseElementsDef(openingNode));
			}
			crawlerDef.replaceWebPage(webPageDef);
			crawlerCache.put(getSourceID(), crawlerDef);
		}
	}

	private IfElseElementsDef getIfElseElementsDef(PopTreeNode openingNode) {
		IfElseElementsDef ifElse = null;
		if(getNodeType().equals(PopTreeNodeType.If)) {
			ifElse = new IfDefinition();
			((IfDefinition) ifElse).setConditions(getConditionDefinitions(conditionsTable));
			((IfDefinition) ifElse).setNodeID(openingNode.getNodeID());
			((IfDefinition) ifElse).setParentNodeID(openingNode.getParentNodeID());
		} else if(getNodeType().equals(PopTreeNodeType.Else_If)) {
			ifElse = new ElseIfDefinition();
			((ElseIfDefinition) ifElse).setConditions(getConditionDefinitions(conditionsTable));
			((ElseIfDefinition) ifElse).setNodeID(openingNode.getNodeID());
			((ElseIfDefinition) ifElse).setParentNodeID(openingNode.getParentNodeID());
		} else if(getNodeType().equals(PopTreeNodeType.Else)) {
			ifElse = new ElseDefinition();
			((ElseDefinition) ifElse).setConditions(getConditionDefinitions(conditionsTable));
			((ElseDefinition) ifElse).setNodeID(openingNode.getNodeID());
			((ElseDefinition) ifElse).setParentNodeID(openingNode.getParentNodeID());
		}
		return ifElse;
	}

	private MainIfElementsDef getMainIfElseElementsDef(PopTreeNode openingNode) {
		MainIfElementsDef ifElse = null;
		if(getNodeType().equals(PopTreeNodeType.If)) {
			ifElse = new IfDefinition();
			((IfDefinition) ifElse).setConditions(getConditionDefinitions(conditionsTable));
			((IfDefinition) ifElse).setNodeID(openingNode.getNodeID());
			((IfDefinition) ifElse).setParentNodeID(openingNode.getParentNodeID());
		} else if(getNodeType().equals(PopTreeNodeType.Else_If)) {
			ifElse = new ElseIfDefinition();
			((ElseIfDefinition) ifElse).setConditions(getConditionDefinitions(conditionsTable));
			((ElseIfDefinition) ifElse).setNodeID(openingNode.getNodeID());
			((ElseIfDefinition) ifElse).setParentNodeID(openingNode.getParentNodeID());
		} else if(getNodeType().equals(PopTreeNodeType.Else)) {
			ifElse = new ElseDefinition();
			((ElseDefinition) ifElse).setConditions(getConditionDefinitions(conditionsTable));
			((ElseDefinition) ifElse).setNodeID(openingNode.getNodeID());
			((ElseDefinition) ifElse).setParentNodeID(openingNode.getParentNodeID());
		}
		return ifElse;
	}

	private Conditions getConditionDefinitions(PopTable popTable) {
		Conditions conditions = new Conditions();
		String conditionName = null;
		int noOfRows = popTable.getDefaultModel().getRowCount();
		for(int index = 0; index < noOfRows; index ++) {
			conditionName = (String) popTable.getDefaultModel().getValueAt(index, 0);
			conditions.addCondition(conditionCache.getConditionDefinition(conditionName));
		}
		return conditions;
	}

	private String getDisplayText(String conditionText) {
		if(this.getNodeType().equals(PopTreeNodeType.If)) {
			return "if" + conditionText;
		} else if(this.getNodeType().equals(PopTreeNodeType.Else_If)) {
			return "else if" + conditionText;
		} else if(this.getNodeType().equals(PopTreeNodeType.Else)) {
			return "else" + conditionText;
		}
		return null;
	}

	private void cancelConditions(PopConditionFrame conFrame) {
		rootTree.getContainingFrame().setEnabled(true);
		rootTree.getContainingFrame().setVisible(true);
		conFrame.dispose();
	}

	class CodeGenWindowAdapter extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			((PopConditionFrame) e.getSource()).dispose();
		}

	}

	class Con_CBX_Left_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("PBC_CBX1")) {
					cBoxLeft.setEnabled(true);
					leftOperandTF.setEnabled(false);
					con_LO_Selected_CB = "PBC_CBX1";
				} else if(((JCheckBox) e.getSource()).getName().equals("PBC_CBX2")) {
					cBoxLeft.setEnabled(false);
					leftOperandTF.setEnabled(true);
					con_LO_Selected_CB = "PBC_CBX2";
				}
			}
		}

	}

	class Con_CBX_Right_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("PBC_CBX3")) {
					cBoxRight.setEnabled(true);
					rightOperandTF.setEnabled(false);
					con_RO_Selected_CB = "PBC_CBX3";
				} else if(((JCheckBox) e.getSource()).getName().equals("PBC_CBX4")) {
					cBoxRight.setEnabled(false);
					rightOperandTF.setEnabled(true);
					con_RO_Selected_CB = "PBC_CBX4";
				}
			}
		}

	}

	class Con_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("PBC_B_Create")) {
				createCondition();
			} else if(buttonClicked.equals("PBC_B_Update")) {
				updateCondition();
			} else if(buttonClicked.equals("PBC_B_Delete")) {
				deleteCondition();
			} else if(buttonClicked.equals("PBC_B_Clear")) {
				clearCondition();
			}
		}

	}

	class Condition_Table_Mouse_Adapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				JTable conTable = (JTable) e.getSource();
				conTableRowNumber = conTable.getSelectedRow();
				conditionName = (String) conTable.getModel().getValueAt(conTableRowNumber, 0);
				bUpdate.setEnabled(true);
				bDelete.setEnabled(true);
				populateConditionFields();
			}
		}

	}

	class ButtonsPanel_Buttons_Action_Listener implements ActionListener {

		PopConditionFrame conFrame = null;

		public ButtonsPanel_Buttons_Action_Listener(PopConditionFrame conFrame) {
			this.conFrame = conFrame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("B_Done")) {
				if(validateConditions()) {
					addConditionsToTree(conFrame);
				}
			} else if(buttonClicked.equals("B_Cancel")) {
				cancelConditions(conFrame);
			}
		}

	}

}
