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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import com.poplicus.crawler.codegen.ArgumentDefinition;
import com.poplicus.crawler.codegen.ClassDefinitions;
import com.poplicus.crawler.codegen.MethodDefinition;
import com.poplicus.crawler.codegen.definitions.ActionDefinition;
import com.poplicus.crawler.codegen.definitions.CrawlerDefinition;
import com.poplicus.crawler.codegen.definitions.ElementDefinition;
import com.poplicus.crawler.codegen.definitions.ExpressionDefinition;
import com.poplicus.crawler.codegen.definitions.ExpressionElementsDef;
import com.poplicus.crawler.codegen.definitions.FunctionDefinition;
import com.poplicus.crawler.codegen.definitions.HTMLElementsDef;
import com.poplicus.crawler.codegen.definitions.IfElseElementsDef;
import com.poplicus.crawler.codegen.definitions.LineOfCodeDefinition;
import com.poplicus.crawler.codegen.definitions.MainIfDefinition;
import com.poplicus.crawler.codegen.definitions.MainIfElementsDef;
import com.poplicus.crawler.codegen.definitions.NavigateDefinition;
import com.poplicus.crawler.codegen.definitions.OperandDefinition;
import com.poplicus.crawler.codegen.definitions.OperatorDefinition;
import com.poplicus.crawler.codegen.definitions.ParameterDefinition;
import com.poplicus.crawler.codegen.definitions.WebPageDefinition;
import com.poplicus.crawler.codegen.factory.CrawlerCache;
import com.poplicus.crawler.codegen.factory.ExpressionCache;
import com.poplicus.crawler.codegen.factory.ObjectFactory;
import com.poplicus.crawler.codegen.gui.components.PopTreeNode.PopTreeNodeType;
import com.poplicus.crawler.codegen.utilities.CodeGenUtil;

public class PopExpressionFrame extends PopJFrame {

	private static final long serialVersionUID = 596914548228867982L;

	private static CodeGenUtil codeGenUtil = CodeGenUtil.getInstance();
	private static CrawlerCache crawlerCache = CrawlerCache.getInstance();
	private static ObjectFactory objectFactory = ObjectFactory.getInstance();
	private static ExpressionCache expressionCache = ExpressionCache.getInstance();

	private PopButton b_Done = null;
	private PopButton b_Cancel = null;

	private PopTabbedPane rhsTabPane = null;
	private PopTabbedPane actionTabPane = null;

	private PopComboBox actionTypeCB = null;

	private PopComboBox tagTypeCB = null;
	private PopComboBox fieldTypeCB = null;
	private PopTextField elementValueTF = null;
	private PopCheckBox exactMatchCKB = null;

	private PopTextField urlTF = null;

	private PopComboBox packageCB = null;
	private PopComboBox classCB = null;
	private PopComboBox functionCB = null;
	private PopComboBox supportingFunctionCB = null;
	private PopTable methodParamsTable = null;
	private PopTable supportingMethodParamsTable = null;

	private PopComboBox expressionNameCB = null;

	private ButtonGroup expressionBGroup = null;
	private PopCheckBox newOperandDefinition = null;
	private PopCheckBox useExistingExpression = null;

	private PopLoCFrame locFrame = null;

	private String actionOrExpression = null;
	private ActionDefinition expActionDef = null;
	private String expressionName = null;

	private PopTable symbolsTable = null;
	private String previousExpressionName = "";

	private PopJFrame containingFrame = null;

	public static PopTree rootTree = null;

	public PopExpressionFrame(String browserType, String uniqueWPName, String sourceID) {
		this.setLayout(new GridBagLayout());
		this.setBrowserTypeOfWebPage(browserType);
		this.setUniqueNameOfWebPage(uniqueWPName);
		this.setSourceID(sourceID);
		Toolkit.getDefaultToolkit( ).setDynamicLayout(true);

		this.addWindowListener(new LoCWindowAdapter());
		this.setTitle("Expression - Operand Definition");

		javax.swing.UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 11));
		javax.swing.UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.PLAIN, 11));

		this.add(getCheckboxPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		this.add(getRHSPanel(), new GridBagConstraints(0, 1, 1, 1, 0.01, 0.01,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getButtonsPanel(), new GridBagConstraints(0, 2, 1, 1, 0.01, 0.001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		enableRHSDefinition(false);

		if(getActionOrExpression() != null) {
			initializeFrame();
		}
	}

	public void initializeFrame() {
		if(getActionOrExpression() != null) {
			if(getActionOrExpression().equalsIgnoreCase("Action")) {
				newOperandDefinition.setSelected(true);
				ActionDefinition actionDef = getExpActionDef();
				String actionType = codeGenUtil.getDisplayActionTypeString(actionDef.getType());
				actionTypeCB.setSelectedItem(actionType);
				if(actionType != null && actionType.length() > 0) {
					if(actionType.equals("Click An Element")) {
						populateElementTab(actionDef);
					} else if(actionType.equals("URL Navigation")) {
						populateNavigationTab(actionDef);
					} else if(actionType.equals("Function Call")) {
						populateFunctionTab(actionDef);
					} else if(actionType.equals("Collect Result Links") || actionType.equals("Parse Result") || actionType.equals("Start Timer")) {
						actionDef = new ActionDefinition();
					}
				}
	 		} else if(getActionOrExpression().equalsIgnoreCase("Expression")) {
	 			useExistingExpression.setSelected(true);
				if(getExpressionName() != null && getExpressionName().length() > 0) {
					expressionNameCB.setSelectedItem(getExpressionName());
				}
			}
		}
	}

	public void populateElementTab(ActionDefinition actionDef) {
		HTMLElementsDef elemDef = actionDef.getHtmlElement();
		tagTypeCB.setSelectedItem(((ElementDefinition) elemDef).getElementTagType());
		fieldTypeCB.setSelectedItem(((ElementDefinition) elemDef).getElementFieldType());
		elementValueTF.setText(((ElementDefinition) elemDef).getValue());
		exactMatchCKB.setSelected(((ElementDefinition) elemDef).isExactMatch());
		actionTabPane.setEnabledAt(0, true);
		actionTabPane.setEnabledAt(1, false);
		actionTabPane.setEnabledAt(2, false);
		actionTabPane.setSelectedIndex(0);
	}

	public void populateNavigationTab(ActionDefinition actionDef) {
		HTMLElementsDef elemDef = actionDef.getHtmlElement();
		urlTF.setText(((NavigateDefinition) elemDef).getUrl());
		actionTabPane.setEnabledAt(0, false);
		actionTabPane.setEnabledAt(1, true);
		actionTabPane.setEnabledAt(2, false);
		actionTabPane.setSelectedIndex(1);
	}

	public void populateFunctionTab(ActionDefinition actionDef) {
		HTMLElementsDef elemDef = actionDef.getHtmlElement();
		packageCB.setSelectedItem(((FunctionDefinition) elemDef).getPackageName());
		classCB.setSelectedItem(((FunctionDefinition) elemDef).getClassName());
		functionCB.setSelectedItem(((FunctionDefinition) elemDef).getName());
		supportingFunctionCB.setSelectedItem(((FunctionDefinition) elemDef).getSupportingFunctionName());
		//populating parameters
		List<ParameterDefinition> paramDefs = ((FunctionDefinition) elemDef).getParameters();
		methodParamsTable.getDefaultModel().getDataVector().removeAllElements();
		for(ParameterDefinition paramDef : paramDefs) {
			methodParamsTable.addRow(new Object[] {paramDef.getType(), paramDef.getName(),
				paramDef.getValue(), paramDef.isPassByValue() ? "Value" : "Reference"});
		}
		//populating supporting parameters
		List<ParameterDefinition> supportingParamDefs = ((FunctionDefinition) elemDef).getSupportingFunctionParameters();
		supportingMethodParamsTable.getDefaultModel().getDataVector().removeAllElements();
		for(ParameterDefinition paramDef : supportingParamDefs) {
			supportingMethodParamsTable.addRow(new Object[] {paramDef.getType(), paramDef.getName(),
				paramDef.getValue(), paramDef.isPassByValue() ? "Value" : "Reference"});
		}
		actionTabPane.setEnabledAt(0, false);
		actionTabPane.setEnabledAt(1, false);
		actionTabPane.setEnabledAt(2, true);
		actionTabPane.setSelectedIndex(2);
	}

	public JPanel getCheckboxPanel() {
		JPanel rightOperandPanel = new JPanel();
		rightOperandPanel.setLayout(new GridBagLayout());

		newOperandDefinition = new PopCheckBox("New Definition");
		newOperandDefinition.setName("EXP_CBX1");
		newOperandDefinition.addItemListener(new Expression_CBX_Right_Listener());

		useExistingExpression = new PopCheckBox("Use Existing Expression");
		useExistingExpression.setName("EXP_CBX2");
		useExistingExpression.addItemListener(new Expression_CBX_Right_Listener());

		expressionBGroup = new ButtonGroup();
		expressionBGroup.add(newOperandDefinition);
		expressionBGroup.add(useExistingExpression);

		rightOperandPanel.add(newOperandDefinition, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(useExistingExpression, new GridBagConstraints(1, 0, 1, 1, 0.05, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		return rightOperandPanel;
	}

	public JPanel getRHSPanel() {
		JPanel rhsPanel = new JPanel();
		rhsPanel.setLayout(new GridBagLayout());
		PopTitledBorder tBorder = new PopTitledBorder("Operand Definition");
		rhsPanel.setBorder(tBorder);

		rhsTabPane = new PopTabbedPane();
		rhsTabPane.add("Action", getActionPanel());
		rhsTabPane.add("Expression", getExpressionPanel());

		rhsPanel.add(rhsTabPane, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		return rhsPanel;
	}

	public PopJFrame getContainingFrame() {
		return containingFrame;
	}

	public void setContainingFrame(PopJFrame containingFrame) {
		this.containingFrame = containingFrame;
	}

	public void setVisible(boolean flag) {
		super.setVisible(flag);
		this.getContainingFrame().setEnabled(! flag);
	}

	public JPanel getActionPanel() {
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridBagLayout());

		PopLabel actionType = new PopLabel("Action Type*");
		actionTypeCB = new PopComboBox();
		actionTypeCB.setName("CB_ActionType");
		populateActionType();
		actionTypeCB.addActionListener(new Action_Type_Action_Listener());
		actionPanel.add(actionType, new GridBagConstraints(0, 0, 1, 1, 0.005, 0.001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 0), 0, 0));
		actionPanel.add(actionTypeCB, new GridBagConstraints(1, 0, 1, 1, 0.005, 0.001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 10), 0, 0));

		JPanel invisiblePanel = new JPanel();
		invisiblePanel.setLayout(new GridBagLayout());
		actionPanel.add(invisiblePanel, new GridBagConstraints(4, 0, 1, 0, 0.1, 0.001,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 10), 0, 0));

		actionTabPane = new PopTabbedPane();

		JPanel elemPanel = new JPanel();
		elemPanel.setLayout(new GridBagLayout());
		elemPanel.add(getElementsPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));

		JPanel navPanel = new JPanel();
		navPanel.setLayout(new GridBagLayout());
		navPanel.add(getNavigationPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.02,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0));
		JPanel invisiblePanel2 = new JPanel();
		invisiblePanel2.setLayout(new GridBagLayout());
		navPanel.add(invisiblePanel2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0));

		actionTabPane.add("Element Definition", elemPanel);
		actionTabPane.add("Navigation Definition", navPanel);
		actionTabPane.add("Function Definition", getFunctionDefinitionPanel());
		actionPanel.add(actionTabPane, new GridBagConstraints(0, 1, 5, 0, 0.05, 0.01,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		return actionPanel;
	}

	public JPanel getElementsPanel() {
		JPanel elementsPanel = new JPanel();
		elementsPanel.setLayout(new GridBagLayout());

		PopLabel tagType = new PopLabel("Tag Type*");
		tagTypeCB = new PopComboBox();
		tagTypeCB.setName("CB_ElementTagType");
		populateElementTagType();

		elementsPanel.add(tagType, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		elementsPanel.add(tagTypeCB, new GridBagConstraints(1, 0, 1, 1, 0.1, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		PopLabel fieldType = new PopLabel("Field Type*");
		fieldTypeCB = new PopComboBox();
		fieldTypeCB.setName("CB_ElementFieldType");
		populateElementFieldType();

		elementsPanel.add(fieldType, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		elementsPanel.add(fieldTypeCB, new GridBagConstraints(3, 0, 1, 1, 0.1, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		PopLabel elementValue = new PopLabel("Value*");
		elementValueTF = new PopTextField();
		elementValueTF.setName("TF_ElementValue");

		elementsPanel.add(elementValue, new GridBagConstraints(0, 1, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
		elementsPanel.add(elementValueTF, new GridBagConstraints(1, 1, 1, 1, 0.1, 1.0,
			GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0));

		exactMatchCKB = new PopCheckBox("Exact Match");
		exactMatchCKB.setName("CBX_ExactMatch");
		elementsPanel.add(exactMatchCKB, new GridBagConstraints(2, 1, 2, 1, 0.1, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));

		return elementsPanel;
	}

	public void populateElementTagType() {
		tagTypeCB.addItem("");
		tagTypeCB.addItem("Image");
		tagTypeCB.addItem("Anchor");
		tagTypeCB.addItem("Input");
		tagTypeCB.addItem("Button");
	}

	public void populateElementFieldType() {
		fieldTypeCB.addItem("");
		fieldTypeCB.addItem("InnerHTML");
		fieldTypeCB.addItem("Value");
		fieldTypeCB.addItem("Name");
		fieldTypeCB.addItem("Source");
		fieldTypeCB.addItem("Type");
		fieldTypeCB.addItem("HREF");
		fieldTypeCB.addItem("ID");
		fieldTypeCB.addItem("ClassName");
		fieldTypeCB.addItem("CheckedAttribute");
		fieldTypeCB.addItem("InnerText");
	}

	public JPanel getNavigationPanel() {
		JPanel navigationPanel = new JPanel();
		navigationPanel.setLayout(new GridBagLayout());

		PopLabel url = new PopLabel("URL*");
		urlTF = new PopTextField();
		urlTF.setName("TF_URL");

		navigationPanel.add(url, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
		navigationPanel.add(urlTF, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		return navigationPanel;
	}

	public JPanel getFunctionDefinitionPanel() {
		JPanel functionDefPanel = new JPanel();
		functionDefPanel.setLayout(new GridBagLayout());

		PopLabel packageName = new PopLabel("Package*");
		packageCB = new PopComboBox();
		packageCB.setName("CB_Package");
		packageCB.addActionListener(new Function_Combos_Action_Listener());
		populatePackage();

		functionDefPanel.add(packageName, new GridBagConstraints(0, 0, 1, 0, 0.05, 0.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 0), 0, 0));
		functionDefPanel.add(packageCB, new GridBagConstraints(1, 0, 1, 0, 1.0, 0.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 10), 0, 0));

		PopLabel className = new PopLabel("Class*");
		classCB = new PopComboBox();
		classCB.setName("CB_Class");
		classCB.addActionListener(new Function_Combos_Action_Listener());

		functionDefPanel.add(className, new GridBagConstraints(2, 0, 1, 0, 0.05, 0.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 0), 0, 0));
		functionDefPanel.add(classCB, new GridBagConstraints(3, 0, 1, 0, 1.0, 0.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 10), 0, 0));

		PopLabel function = new PopLabel("Function*");
		functionCB = new PopComboBox();
		functionCB.setName("CB_Function");
		functionCB.addActionListener(new Function_Combos_Action_Listener());

		functionDefPanel.add(function, new GridBagConstraints(4, 0, 1, 1, 0.05, 0.001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 0), 0, 0));
		functionDefPanel.add(functionCB, new GridBagConstraints(5, 0, 3, 1, 1.0, 0.001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 10), 0, 0));

		PopLabel supportingFunction = new PopLabel("<html>Supporting<br>Function</html>");
		supportingFunctionCB = new PopComboBox();
		supportingFunctionCB.setName("CB_SupportingFunction");
		supportingFunctionCB.addActionListener(new Function_Combos_Action_Listener());

		functionDefPanel.add(supportingFunction, new GridBagConstraints(0, 1, 1, 1, 0.05, 0.001,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		functionDefPanel.add(supportingFunctionCB, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.001,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 10), 0, 0));

		functionDefPanel.add(getMethodParamsPane(), new GridBagConstraints(0, 2, 8, 1, 0.05, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));

		functionDefPanel.add(getSupportingMethodParamsPane(), new GridBagConstraints(0, 3, 8, 1, 0.05, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));

		return functionDefPanel;
	}

	private JScrollPane getSymbolsTablePanel() {
		Object[] columnNames = {"Symbols", "Operand Name"};
		Object[][] rowData = new Object[0][0];
		symbolsTable = new PopTable(rowData, columnNames);
		symbolsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));

		JScrollPane scrollPane = new JScrollPane(symbolsTable);
		symbolsTable.setFillsViewportHeight(true);

		symbolsTable.removeColumn(symbolsTable.getColumnModel().getColumn(1));

		return scrollPane;
	}

	public String getPreviousExpressionName() {
		return previousExpressionName;
	}

	public void setPreviousExpressionName(String previousExpressionName) {
		this.previousExpressionName = previousExpressionName;
	}

	private JScrollPane getMethodParamsPane() {
		Object[] columnNames = {"Type", "Name", "Value", "Pass By"};
		Object[][] rowData = new Object[0][0];
		methodParamsTable = new PopTable(rowData, columnNames, "Parameter");
		methodParamsTable.setColumnSelectionAllowed(true);
		methodParamsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
		methodParamsTable.getColumnModel().getColumn(3).setCellEditor(new TableCellComboBox(new String[]{"Value", "Reference"}));

		JScrollPane scrollPane = new JScrollPane(methodParamsTable);
		PopTitledBorder tBorder = new PopTitledBorder("Function - Parameter(s) Definition");
		scrollPane.setBorder(tBorder);
		methodParamsTable.setFillsViewportHeight(true);

		return scrollPane;
	}

	private JScrollPane getSupportingMethodParamsPane() {
		Object[] columnNames = {"Type", "Name", "Value", "Pass By"};
		Object[][] rowData = new Object[0][0];
		supportingMethodParamsTable = new PopTable(rowData, columnNames, "Parameter");
		supportingMethodParamsTable.setColumnSelectionAllowed(true);
		supportingMethodParamsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
		supportingMethodParamsTable.getColumnModel().getColumn(3).setCellEditor(new TableCellComboBox(new String[]{"Value", "Reference"}));

		JScrollPane scrollPane = new JScrollPane(supportingMethodParamsTable);
		PopTitledBorder tBorder = new PopTitledBorder("Supporting Function - Parameter(s) Definition");
		scrollPane.setBorder(tBorder);

		supportingMethodParamsTable.setFillsViewportHeight(true);

		return scrollPane;
	}

	public void populatePackage() {
		List<String> packages = ClassDefinitions.getPackages();
		packageCB.removeAllItems();
		packageCB.addItem("");
		for (String packageName : packages) {
			if(! packageName.equalsIgnoreCase("csExWB")) {
				packageCB.addItem(packageName);
			}
		}
	}

	public void populateCSExWBPackage() {
		packageCB.removeAllItems();
		packageCB.addItem("");
		packageCB.addItem("csExWB");
	}

	public void populateClass(List<String> classes) {
		if(classes != null) {
			classCB.removeAllItems();
			classCB.addItem("");
			for (String className : classes) {
				classCB.addItem(className);
			}
		} else {
			if(classCB != null) {
				classCB.removeAllItems();
			}
		}
	}

	public void populateFunction(List<String> functions) {
		if(functions != null) {
			functionCB.removeAllItems();
			functionCB.addItem("");
			for (String functionName : functions) {
				functionCB.addItem(functionName);
			}
		} else {
			if(functionCB != null) {
				functionCB.removeAllItems();
			}
		}
	}

	public void populateSupportingFunction(List<String> functions) {
		if(functions != null) {
			supportingFunctionCB.removeAllItems();
			supportingFunctionCB.addItem("");
			for (String functionName : functions) {
				supportingFunctionCB.addItem(functionName);
			}
		} else {
			if(supportingFunctionCB != null) {
				supportingFunctionCB.removeAllItems();
			}
		}
	}

	public void populateFunctionParameters(MethodDefinition mDef) {
		if(mDef != null) {
			List<ArgumentDefinition> argDefs = mDef.getArgumentDefinitions();
			ArgumentDefinition argDef = null;
			int noOfArgs = argDefs.size();
			((DefaultTableModel) methodParamsTable.getModel()).getDataVector().removeAllElements();
			for(int argIndex = 0; argIndex < noOfArgs; argIndex ++) {
				argDef = argDefs.get(argIndex);
				methodParamsTable.addRow(new Object[] {argDef.getArgumentType().toString(), argDef.getArgumentName(), "", "Value"});
			}
		} else {
			if(methodParamsTable != null) {
				((DefaultTableModel) methodParamsTable.getModel()).getDataVector().removeAllElements();
				methodParamsTable.updateUI();
			}
		}
	}

	public void populateSupportingFunctionParameters(MethodDefinition mDef) {
		if(mDef != null) {
			List<ArgumentDefinition> argDefs = mDef.getArgumentDefinitions();
			ArgumentDefinition argDef = null;
			int noOfArgs = argDefs.size();
			((DefaultTableModel) supportingMethodParamsTable.getModel()).getDataVector().removeAllElements();
			for(int argIndex = 0; argIndex < noOfArgs; argIndex ++) {
				argDef = argDefs.get(argIndex);
				supportingMethodParamsTable.addRow(new Object[] {argDef.getArgumentType().toString(), argDef.getArgumentName(), "", "Value"});
			}
		} else {
			if(supportingMethodParamsTable != null) {
				((DefaultTableModel) supportingMethodParamsTable.getModel()).getDataVector().removeAllElements();
				supportingMethodParamsTable.updateUI();
			}
		}
	}

	private void populateActionType() {
		actionTypeCB.removeAllItems();
		actionTypeCB.addItem("");
		actionTypeCB.addItem("Click An Element");
		actionTypeCB.addItem("URL Navigation");
		if(getWebPageType().equals("ResultLinks")) {
			actionTypeCB.addItem("Collect Result Links");
		}
		if(getWebPageType().equals("Result")) {
			actionTypeCB.addItem("Parse Result");
		}
		actionTypeCB.addItem("Start Timer");
		actionTypeCB.addItem("Function Call");
	}

	public PopLoCFrame getLocFrame() {
		return locFrame;
	}

	public void setLocFrame(PopLoCFrame locFrame) {
		this.locFrame = locFrame;
	}

	public JPanel getExpressionPanel() {
		JPanel expressionPanel = new JPanel();
		expressionPanel.setLayout(new GridBagLayout());

		JPanel expressionNamePanel = new JPanel();
		expressionNamePanel.setLayout(new GridBagLayout());
		PopLabel expressionName = new PopLabel("Expression Name");
		expressionNameCB = new PopComboBox();
		expressionNameCB.setName("CB_ExpressionName");
		populateExpressionNames();
		expressionNameCB.addActionListener(new Expression_Name_Action_Listener());
		expressionNamePanel.add(expressionName, new GridBagConstraints(0, 0, 1, 1, 0, 0.001,
			GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 5, 0), 0, 0));
		expressionNamePanel.add(expressionNameCB, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.001,
			GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 250, 0));

		expressionPanel.add(expressionNamePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		expressionPanel.add(getSymbolsTablePanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		return expressionPanel;
	}

	public void populateExpressionNames() {
		List<String> expressionNames = expressionCache.getExpressionNames();
		expressionNameCB.removeAllItems();
		expressionNameCB.addItem("");
		for(String expressionName : expressionNames) {
			expressionNameCB.addItem(expressionName);
		}
	}

	public JPanel getButtonsPanel() {
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

	public void enableRHSDefinition(boolean enableFlag) {
		rhsTabPane.setEnabledAt(0, enableFlag);
		rhsTabPane.setEnabledAt(1, enableFlag);
		rhsTabPane.setSelectedIndex(0);
		enableActionPanel(enableFlag);
		enableExpressionPanel(enableFlag);
	}

	public void enableActionPanel(boolean enableFlag) {
		actionTypeCB.setEnabled(enableFlag);
		actionTabPane.setEnabledAt(0, enableFlag);
		actionTabPane.setEnabledAt(1, enableFlag);
		actionTabPane.setEnabledAt(2, enableFlag);
		actionTabPane.setSelectedIndex(0);
		enableElementsPanel(enableFlag);
		enableNavigationPanel(enableFlag);
		enableFunctionPanel(enableFlag);
	}

	public void enableElementsPanel(boolean enableFlag) {
		tagTypeCB.setEnabled(enableFlag);
		fieldTypeCB.setEnabled(enableFlag);
		elementValueTF.setEnabled(enableFlag);
		exactMatchCKB.setEnabled(enableFlag);
	}

	public void enableNavigationPanel(boolean enableFlag) {
		urlTF.setEnabled(enableFlag);
	}

	public void enableFunctionPanel(boolean enableFlag) {
		packageCB.setEnabled(enableFlag);
		classCB.setEnabled(enableFlag);
		functionCB.setEnabled(enableFlag);
		supportingFunctionCB.setEnabled(enableFlag);
		methodParamsTable.setEnabled(enableFlag);
		supportingMethodParamsTable.setEnabled(enableFlag);
	}

	public void enableExpressionPanel(boolean enableFlag) {

	}

	public void enableRHSDefForAssignValueToAnExistingVariable(boolean enableFlag) {
		rhsTabPane.setEnabledAt(0, enableFlag);
		actionTypeCB.setEnabled(enableFlag);
		actionTabPane.setEnabledAt(2, enableFlag);
		actionTabPane.setSelectedIndex(2);
		enableFunctionPanel(enableFlag);
	}

	public void enableRHSDefForPerformAnAction(boolean enableFlag) {
		rhsTabPane.setEnabledAt(0, enableFlag);
		actionTypeCB.setEnabled(enableFlag);
	}

	public void enableRHSDefForReturnValueFromAction(boolean enableFlag) {
		rhsTabPane.setEnabledAt(0, enableFlag);
		actionTypeCB.setEnabled(enableFlag);
		actionTabPane.setEnabledAt(2, enableFlag);
		actionTabPane.setSelectedIndex(2);
		enableFunctionPanel(enableFlag);
	}

	public void enableRHSExpression(boolean enableFlag) {
		rhsTabPane.setEnabledAt(1, enableFlag);
		rhsTabPane.setSelectedIndex(1);
		enableExpressionPanel(enableFlag);
	}

	public void populateActionTypeForAssignValueToAnExistingVariable() {
		actionTypeCB.removeAllItems();
		actionTypeCB.addItem("");
		actionTypeCB.addItem("Function Call");
	}

	public void populateActionTypeForPerformAnAction() {
		actionTypeCB.removeAllItems();
		actionTypeCB.addItem("");
		actionTypeCB.addItem("Click An Element");
		actionTypeCB.addItem("URL Navigation");
		if(getWebPageType().equals("ResultLinks")) {
			actionTypeCB.addItem("Collect Result Links");
		}
		if(getWebPageType().equals("Result")) {
			actionTypeCB.addItem("Parse Result");
		}
		actionTypeCB.addItem("Start Timer");
		actionTypeCB.addItem("Function Call");
	}

	public void populateActionTypeForReturnValueFromAction() {
		actionTypeCB.removeAllItems();
		actionTypeCB.addItem("");
		actionTypeCB.addItem("Function Call");
	}

	public static void main(String[] args) {
		PopLoCFrame locFrame = new PopLoCFrame("Primary", "abc", "100031");
		locFrame.setSize(850, 605);
		locFrame.setLocation(150, 100);
		locFrame.setVisible(true);
	}

	public Object getDefinition() {
		ActionDefinition actionDef = null;
		String actionType = (String) actionTypeCB.getSelectedItem();
		if(! (actionType != null && actionType.length() > 0)) {
			JOptionPane.showMessageDialog(this, "Action Type is mandatory.");
			actionTypeCB.requestFocus();
			return null;
		}
		actionDef = getActionDef(actionType, getBrowserTypeOfWebPage());
		return actionDef;
	}


	public String getWebPageType() {
		CrawlerDefinition crawlerDef = crawlerCache.get(getSourceID());
		WebPageDefinition webPageDef = crawlerDef.getWebPage(getUniqueNameOfWebPage());
		return webPageDef.getType();
//		return "Search";
	}

	public String getActionOrExpression() {
		return actionOrExpression;
	}

	public void setActionOrExpression(String actionOrExpression) {
		this.actionOrExpression = actionOrExpression;
	}

	public ActionDefinition getExpActionDef() {
		return expActionDef;
	}

	public void setExpActionDef(ActionDefinition expActionDef) {
		this.expActionDef = expActionDef;
	}

	public String getExpressionName() {
		return expressionName;
	}

	public void setExpressionName(String expressionName) {
		this.expressionName = expressionName;
	}

	public ActionDefinition getActionDef(String actionType, String browserType) {
		ActionDefinition actionDef = null;
		if(actionType != null && actionType.length() > 0) {
			if(actionType.equals("Click An Element")) {
				actionDef = getActionDef_Element(actionType, browserType);
			} else if(actionType.equals("URL Navigation")) {
				actionDef = getActionDef_Navigation(actionType, browserType);
			} else if(actionType.equals("Function Call")) {
				actionDef = getActionDef_Function(actionType, browserType);
			} else if(actionType.equals("Collect Result Links") || actionType.equals("Parse Result") || actionType.equals("Start Timer")) {
				actionDef = new ActionDefinition();
			}
		}

		if(actionDef != null) {
			actionDef.setType(codeGenUtil.getValidActionTypeString(actionType));
			actionDef.setBrowserType(browserType);
		}

		return actionDef;
	}

	public ActionDefinition getActionDef_Element(String actionType, String browserType) {
		ActionDefinition actionDef = new ActionDefinition();
		ElementDefinition elemDef = new ElementDefinition();
		String tagType = (String) tagTypeCB.getSelectedItem();
		if(tagType != null && tagType.length() > 0) {
			elemDef.setElementTagType(tagType);
		} else {
			JOptionPane.showMessageDialog(this, "Element Tag Type is mandatory.");
			tagTypeCB.requestFocus();
			return null;
		}

		String fieldType = (String) fieldTypeCB.getSelectedItem();
		if(fieldType != null && fieldType.length() > 0) {
			elemDef.setElementFieldType(fieldType);
		} else {
			JOptionPane.showMessageDialog(this, "Element Field Type is mandatory.");
			fieldTypeCB.requestFocus();
			return null;
		}

		String elementValue = elementValueTF.getText();
		if(elementValue != null && elementValue.length() > 0) {
			elemDef.setValue(elementValue);
		} else {
			JOptionPane.showMessageDialog(this, "Element Value is mandatory.");
			elementValueTF.requestFocus();
			return null;
		}

		elemDef.setExactMatch(exactMatchCKB.isSelected());
		actionDef.setHtmlElement(elemDef);

		return actionDef;
	}

	public ActionDefinition getActionDef_Navigation(String actionType, String browserType) {
		ActionDefinition actionDef = new ActionDefinition();
		NavigateDefinition navDef = new NavigateDefinition();
		String url = urlTF.getText();
		if(url != null && url.length() > 0) {
			navDef.setUrl(url);
		} else {
			JOptionPane.showMessageDialog(this, "URL is mandatory.");
			urlTF.requestFocus();
			return null;
		}
		actionDef.setHtmlElement(navDef);

		return actionDef;
	}

	public ActionDefinition getActionDef_Function(String actionType, String browserType) {
		ActionDefinition actionDef = new ActionDefinition();
		FunctionDefinition funcDef = new FunctionDefinition();

		String packageName = (String) packageCB.getSelectedItem();
		if(packageName != null && packageName.length() > 0) {
			funcDef.setPackageName(packageName);
		} else {
			JOptionPane.showMessageDialog(this, "Package Name is mandatory.");
			packageCB.requestFocus();
			return null;
		}

		String className = (String) classCB.getSelectedItem();
		if(className != null && className.length() > 0) {
			funcDef.setClassName(className);
		} else {
			JOptionPane.showMessageDialog(this, "Class Name is mandatory.");
			classCB.requestFocus();
			return null;
		}

		String functionName = (String) functionCB.getSelectedItem();
		if(functionName != null && functionName.length() > 0) {
			funcDef.setName(functionName);
		} else {
			JOptionPane.showMessageDialog(this, "Function Name is mandatory.");
			functionCB.requestFocus();
			return null;
		}

		String supportingFunctionName = (String) supportingFunctionCB.getSelectedItem();
		if(supportingFunctionName != null && supportingFunctionName.length() > 0) {
			funcDef.setSupportingFunctionName(supportingFunctionName);
		}

		if(validateFunctionParameters(funcDef, functionName)) {
			if(supportingFunctionName != null && supportingFunctionName.length() > 0) {
				if(! validateSupportingFunctionParameters(funcDef, supportingFunctionName)) {
					actionDef = null;
				}
			}
		} else {
			actionDef = null;
		}
		if(actionDef != null) {
			actionDef.setHtmlElement(funcDef);
		}
		return actionDef;
	}

	public boolean validateFunctionParameters(FunctionDefinition funcDef, String functionName) {
		ParameterDefinition paramDef = null;
		String name = null;
		String value = null;
		String passBy = null;
		boolean parametersAreValid = true;
		int noOfRows = methodParamsTable.getDefaultModel().getRowCount();
		for(int rowIndex = 0; rowIndex < noOfRows; rowIndex ++) {
			name = (String) methodParamsTable.getDefaultModel().getValueAt(rowIndex, 1);
			value = (String) methodParamsTable.getDefaultModel().getValueAt(rowIndex, 2);
			if(value != null && value.length() > 0) {
				paramDef = new ParameterDefinition();
				paramDef.setValue(value);
				paramDef.setName(name);
			} else {
				JOptionPane.showMessageDialog(this, "Value of parameter '" + name + "' of function '" + functionName + "' is mandatory.");
				methodParamsTable.changeSelection(rowIndex, 2, false, false);
				parametersAreValid = false;
				//methodParamsTable.editCellAt(rowIndex, 2);
				break;
			}
			paramDef.setType((String) methodParamsTable.getDefaultModel().getValueAt(rowIndex, 0));
			passBy = (String) methodParamsTable.getDefaultModel().getValueAt(rowIndex, 3);
			if(passBy.equals("Value")) {
				paramDef.setPassByValue(true);
			} else if(passBy.equals("Reference")) {
				paramDef.setPassByReference(true);
			}
			funcDef.addParameter(paramDef);
		}
		return parametersAreValid;
	}

	public boolean validateSupportingFunctionParameters(FunctionDefinition funcDef, String functionName) {
		ParameterDefinition paramDef = null;
		String name = null;
		String value = null;
		String passBy = null;
		boolean parametersAreValid = true;
		int noOfRows = supportingMethodParamsTable.getDefaultModel().getRowCount();
		for(int rowIndex = 0; rowIndex < noOfRows; rowIndex ++) {
			name = (String) supportingMethodParamsTable.getDefaultModel().getValueAt(rowIndex, 1);
			value = (String) supportingMethodParamsTable.getDefaultModel().getValueAt(rowIndex, 2);
			if(value != null && value.length() > 0) {
				paramDef = new ParameterDefinition();
				paramDef.setValue(value);
				paramDef.setName(name);
			} else {
				JOptionPane.showMessageDialog(this, "Value of parameter '" + name + "' of function '" + functionName + "' is mandatory.");
				supportingMethodParamsTable.changeSelection(rowIndex, 2, false, false);
				parametersAreValid = false;
				//supportingMethodParamsTable.editCellAt(rowIndex, 2);
				break;
			}
			paramDef.setType((String) supportingMethodParamsTable.getDefaultModel().getValueAt(rowIndex, 0));
			passBy = (String) supportingMethodParamsTable.getDefaultModel().getValueAt(rowIndex, 3);
			if(passBy.equals("Value")) {
				paramDef.setPassByValue(true);
			} else if(passBy.equals("Reference")) {
				paramDef.setPassByReference(true);
			}
			funcDef.addSupportingFunctionParameter(paramDef);
		}
		return parametersAreValid;
	}

	public ExpressionDefinition getExpressionDef(String actionType, String browserType) {
		ExpressionDefinition expressionDef = null;


		return expressionDef;
	}


	public void cancelLineOfCode(PopLoCFrame locFrame) {
//		rootTree.getContainingFrame().setEnabled(true);
//		rootTree.getContainingFrame().setVisible(true);
		locFrame.dispose();
	}

	public void addLineOfCodeToTree(PopLoCFrame locFrame, LineOfCodeDefinition locDef) throws Exception {
		rootTree.getContainingFrame().setEnabled(false);
		PopTreeNode openingNode = null;
		PopTreeNode localParentNode = null;
		if(getParentNode() != null) {
			localParentNode = getParentNode();
		} else {
			localParentNode = (PopTreeNode) rootTree.getSelectionPath().getLastPathComponent();
		}
		if(localParentNode instanceof PopTreeNode) {
			String lineOfCodeString = objectFactory.getLineOfCode(locDef, getWebPageName()).toStringBuffer().toString();

			if(lineOfCodeString != null && lineOfCodeString.length() > 0) {
				openingNode = new PopTreeNode(lineOfCodeString, true, getNodeType());
				openingNode.setParentNodeID(localParentNode.getNodeID());
				openingNode.setParentNodeType(localParentNode.getNodeType());
				localParentNode.add(openingNode);
				for (int i = 0; i < rootTree.getRowCount(); i++) {
			         rootTree.expandRow(i);
				}
			}
		}

		locFrame.dispose();
		rootTree.getContainingFrame().setEnabled(true);
		rootTree.updateUI();
		rootTree.getContainingFrame().setVisible(true);

		addLineOfCodeToDefinition(locDef, openingNode);
	}

	private void addLineOfCodeToDefinition(LineOfCodeDefinition locDef, PopTreeNode openingNode) {
		if(openingNode != null) {
			CrawlerDefinition crawlerDef = crawlerCache.get(getSourceID());
			WebPageDefinition webPageDef = crawlerDef.getWebPage(getUniqueNameOfWebPage());

			if(openingNode.getParentNodeType().equals(PopTreeNodeType.None)) {
				MainIfDefinition mainIfDef = webPageDef.getMainIF();
				if(mainIfDef == null) {
					mainIfDef = new MainIfDefinition();
				}
				mainIfDef.setMainIfElementsDef(getMainIfElseElementsDef_LineOfCode(locDef, openingNode));
				webPageDef.setMainIF(mainIfDef);
			} else {
				webPageDef.addIfElseElementsDef(getIfElseElementsDef_LineOfCode(locDef, openingNode));
			}
			crawlerDef.replaceWebPage(webPageDef);
			crawlerCache.put(getSourceID(), crawlerDef);
		}
	}

	public IfElseElementsDef getIfElseElementsDef_LineOfCode(LineOfCodeDefinition locDef, PopTreeNode openingNode) {
		IfElseElementsDef ifElse = null;
		if(getNodeType().equals(PopTreeNodeType.LineOfCode)) {
			ifElse = new LineOfCodeDefinition();
			((LineOfCodeDefinition) ifElse).setNodeID(openingNode.getNodeID());
			((LineOfCodeDefinition) ifElse).setParentNodeID(openingNode.getParentNodeID());
			((LineOfCodeDefinition) ifElse).setLhsDefiniton(locDef.getLhsDefiniton());
			((LineOfCodeDefinition) ifElse).setRhsDefinition(locDef.getRhsDefinition());
		}
		return ifElse;
	}

	public IfElseElementsDef getIfElseElementsDef_Action(ActionDefinition actionDef, PopTreeNode openingNode) {
		IfElseElementsDef ifElse = null;
		//here we are checking for LineOfCode node type, whereas the underlying object created would be ActionDefinition
		if(getNodeType().equals(PopTreeNodeType.LineOfCode)) {
			ifElse = new ActionDefinition();
			((ActionDefinition) ifElse).setNodeID(openingNode.getNodeID());
			((ActionDefinition) ifElse).setParentNodeID(openingNode.getParentNodeID());
			((ActionDefinition) ifElse).setType(actionDef.getType());
			((ActionDefinition) ifElse).setBrowserType(actionDef.getBrowserType());
			((ActionDefinition) ifElse).setHtmlElement(actionDef.getHtmlElement());
		}
		return ifElse;
	}

	public MainIfElementsDef getMainIfElseElementsDef_LineOfCode(LineOfCodeDefinition locDef, PopTreeNode openingNode) {
		MainIfElementsDef ifElse = null;
		if(getNodeType().equals(PopTreeNodeType.LineOfCode)) {
			ifElse = new LineOfCodeDefinition();
			((LineOfCodeDefinition) ifElse).setNodeID(openingNode.getNodeID());
			((LineOfCodeDefinition) ifElse).setParentNodeID(openingNode.getParentNodeID());
			((LineOfCodeDefinition) ifElse).setLhsDefiniton(locDef.getLhsDefiniton());
			((LineOfCodeDefinition) ifElse).setRhsDefinition(locDef.getRhsDefinition());
		}
		return ifElse;
	}

	public MainIfElementsDef getMainIfElseElementsDef_Action(ActionDefinition actionDef, PopTreeNode openingNode) {
		MainIfElementsDef ifElse = null;
		//here we are checking for LineOfCode node type, whereas the underlying object created would be ActionDefinition
		if(getNodeType().equals(PopTreeNodeType.LineOfCode)) {
			ifElse = new ActionDefinition();
			((ActionDefinition) ifElse).setNodeID(openingNode.getNodeID());
			((ActionDefinition) ifElse).setParentNodeID(openingNode.getParentNodeID());
			((ActionDefinition) ifElse).setType(actionDef.getType());
			((ActionDefinition) ifElse).setBrowserType(actionDef.getBrowserType());
			((ActionDefinition) ifElse).setHtmlElement(actionDef.getHtmlElement());
		}
		return ifElse;
	}

	public void showExpressionMessage() {
		JOptionPane.showMessageDialog(this, "Expression Name is mandatory.");
		expressionNameCB.requestFocus();
		return;
	}

	public void addActionToLoCFrame(ActionDefinition actionDef) throws Exception {
		getLocFrame().setActionOrExpression("Action");
		getLocFrame().setExpActionDef(actionDef);
		getLocFrame().setOperandText(
			objectFactory.getActionFromActionDef(actionDef, getWebPageName()).toStringBuffer().toString());
	}

	public void disposeThisFrame() {
		this.dispose();
	}

	public void doneWarning() {
		JOptionPane.showMessageDialog(this, "Operand not defined. To close this window click 'Cancel' button.");
		b_Cancel.requestFocus();
		return;
	}

	public void cancelExpressionOperand() {
	    int response = JOptionPane.showConfirmDialog(null, "Do you want to close this window? Any changes made will be lost.", "Confirm",
	        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    if (response == JOptionPane.YES_OPTION) {
	    	getLocFrame().resetOperandObjects();
	    	getLocFrame().setEnabled(true);
	    	enableLoCFrame();
			disposeThisFrame();
	    }
	}

	public void actOnExpressionNameChange(String expressionName) {
		try {
			updateSymbolsTable(expressionName);
		} catch(Exception ex) {
			displayMessage(ex.getMessage());
		}
	}

	private void updateSymbolsTable(String expressionName) throws Exception {
		symbolsTable.getDefaultModel().getDataVector().removeAllElements();
		symbolsTable.updateUI();
		if(expressionName.length() > 0) {
			previousExpressionName = expressionName;
			ExpressionDefinition expDef = expressionCache.get(expressionName);
			List<ExpressionElementsDef> expElements = expDef.getExpressionElements();
			int noOfExpElements = expElements.size();
			for(int expIndex = 0; expIndex < noOfExpElements; expIndex ++) {
				if(expElements.get(expIndex) instanceof OperandDefinition) {
					String lineOfCodeString = null;
					if(((OperandDefinition) expElements.get(expIndex)).getActionDef() != null) {
						lineOfCodeString = objectFactory.getActionFromActionDef(
							((OperandDefinition) expElements.get(expIndex)).getActionDef(),
							getWebPageName()).toStringBuffer().toString();
					} else if(((OperandDefinition) expElements.get(expIndex)).getStringDef() != null) {
						lineOfCodeString = ((OperandDefinition) expElements.get(expIndex)).getStringDef().getName();
					} else if(((OperandDefinition) expElements.get(expIndex)).getExpressionDef() != null) {
						lineOfCodeString = objectFactory.getExpressionFromExpressionDefinition(
							((OperandDefinition) expElements.get(expIndex)).getExpressionDef(),
							getWebPageName()).toStringBuffer().toString();
					}
					symbolsTable.addRow(new Object[]{lineOfCodeString, (OperandDefinition) expElements.get(expIndex)});
				} else if(expElements.get(expIndex) instanceof OperatorDefinition) {
					symbolsTable.addRow(new Object[]{
						codeGenUtil.getOperatorSymbol(((OperatorDefinition) expElements.get(expIndex)).getOperator()),
						(OperatorDefinition) expElements.get(expIndex)});
				}
			}
		} else if(expressionName.length() == 0) {
			previousExpressionName = "";
		}
	}

	public void displayMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public void enableLoCFrame() {
    	this.getContainingFrame().setEnabled(true);
    	this.getContainingFrame().setVisible(true);
	}

	class LoCFrameWindowAdapter extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			((PopLoCFrame) e.getSource()).dispose();
		}

	}

	class ButtonsPanel_Buttons_Action_Listener implements ActionListener {

		PopExpressionFrame expressionFrame = null;

		public ButtonsPanel_Buttons_Action_Listener(PopExpressionFrame expressionFrame) {
			this.expressionFrame = expressionFrame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((PopButton) e.getSource()).getName();
			try {
				if(buttonClicked.equals("B_Done")) {
					if(newOperandDefinition.isSelected()) {
						Object object = getDefinition();
						if(object != null) {
							if(object instanceof ActionDefinition) {
								ActionDefinition actionDef = (ActionDefinition) object;
								if(actionDef != null) {
									addActionToLoCFrame(actionDef);
								}
							}
							disposeThisFrame();
						}
					} else if(useExistingExpression.isSelected()) {
						if(expressionNameCB.getSelectedItem() != null
							&& ((String) expressionNameCB.getSelectedItem()).length() > 0) {
							getLocFrame().setActionOrExpression("Expression");
							getLocFrame().setExpressionName((String) expressionNameCB.getSelectedItem());
							ExpressionDefinition expDef = expressionCache.get((String) expressionNameCB.getSelectedItem());
							getLocFrame().setOperandText(objectFactory.getExpressionFromExpressionDefinition(expDef,
								getWebPageName()).toStringBuffer().toString());
							disposeThisFrame();
						} else {
							showExpressionMessage();
						}
					} else {
						doneWarning();
					}
					enableLoCFrame();
				} else if(buttonClicked.equals("B_Cancel")) {
					cancelExpressionOperand();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	class Action_Type_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			PopComboBox sourceCombo = (PopComboBox) e.getSource();
			String selectedItem = (String) sourceCombo.getSelectedItem();
			if(sourceCombo.getName().equals("CB_ActionType")) {
				if(selectedItem != null) {
					if(selectedItem.length() > 0) {
						if(selectedItem.equals("Click An Element")) {
							enableElementsPanel(true);
							actionTabPane.setEnabledAt(0, true);
							actionTabPane.setEnabledAt(1, false);
							actionTabPane.setEnabledAt(2, false);
							actionTabPane.setSelectedIndex(0);
						} else if(selectedItem.equals("URL Navigation")) {
							enableNavigationPanel(true);
							actionTabPane.setEnabledAt(0, false);
							actionTabPane.setEnabledAt(1, true);
							actionTabPane.setEnabledAt(2, false);
							actionTabPane.setSelectedIndex(1);
						} else if(selectedItem.equals("Collect Result Links")) {
							enableElementsPanel(false);
							enableNavigationPanel(false);
							enableFunctionPanel(false);
							actionTabPane.setEnabledAt(0, false);
							actionTabPane.setEnabledAt(1, false);
							actionTabPane.setEnabledAt(2, false);
							actionTabPane.setSelectedIndex(0);
						} else if(selectedItem.equals("Parse Result")) {
							enableElementsPanel(false);
							enableNavigationPanel(false);
							enableFunctionPanel(false);
							actionTabPane.setEnabledAt(0, false);
							actionTabPane.setEnabledAt(1, false);
							actionTabPane.setEnabledAt(2, false);
							actionTabPane.setSelectedIndex(0);
						} else if(selectedItem.equals("Start Timer")) {
							enableElementsPanel(false);
							enableNavigationPanel(false);
							enableFunctionPanel(false);
							actionTabPane.setEnabledAt(0, false);
							actionTabPane.setEnabledAt(1, false);
							actionTabPane.setEnabledAt(2, false);
							actionTabPane.setSelectedIndex(0);
						} else if(selectedItem.equals("Function Call")) {
							enableFunctionPanel(true);
							actionTabPane.setEnabledAt(0, false);
							actionTabPane.setEnabledAt(1, false);
							actionTabPane.setEnabledAt(2, true);
							actionTabPane.setSelectedIndex(2);
						}
					} else {
						actionTabPane.setEnabledAt(0, false);
						actionTabPane.setEnabledAt(1, false);
						actionTabPane.setEnabledAt(2, false);
						actionTabPane.setSelectedIndex(0);
						enableElementsPanel(false);
					}
				}
			}
		}

	}

	class Expression_Name_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			PopComboBox popCBX = (PopComboBox) e.getSource();
			actOnExpressionNameChange((String) popCBX.getSelectedItem());
		}

	}

	class Function_Combos_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			PopComboBox sourceCombo = (PopComboBox) e.getSource();
			if(sourceCombo.getName().equals("CB_Package")) {
				if(sourceCombo.getSelectedItem() != null && (((String) sourceCombo.getSelectedItem()).length() > 0)) {
					populateClass(ClassDefinitions.getClasses((String) sourceCombo.getSelectedItem()));
				} else {
					populateClass(null);
					populateFunctionParameters(null);
					populateSupportingFunctionParameters(null);
				}
			} else if(sourceCombo.getName().equals("CB_Class")) {
				if(sourceCombo.getSelectedItem() != null && (((String) sourceCombo.getSelectedItem()).length() > 0)) {
					populateFunction(ClassDefinitions.getMethods((String) packageCB.getSelectedItem(),
						(String) sourceCombo.getSelectedItem()));
				} else {
					populateFunction(null);
					populateFunctionParameters(null);
					populateSupportingFunctionParameters(null);
				}
			} else if(sourceCombo.getName().equals("CB_Function")) {
				if(sourceCombo.getSelectedItem() != null && (((String) sourceCombo.getSelectedItem()).length() > 0)) {
					populateSupportingFunction(ClassDefinitions.getSupportingMethods((String) packageCB.getSelectedItem(),
						(String) classCB.getSelectedItem(), (String) sourceCombo.getSelectedItem()));
					populateFunctionParameters(ClassDefinitions.getMethodDefinition((String) packageCB.getSelectedItem(),
						(String) classCB.getSelectedItem(), null, (String) sourceCombo.getSelectedItem()));
				} else {
					populateSupportingFunction(null);
					populateFunctionParameters(null);
					populateSupportingFunctionParameters(null);
				}
			} else if(sourceCombo.getName().equals("CB_SupportingFunction")) {
				if(sourceCombo.getSelectedItem() != null && (((String) sourceCombo.getSelectedItem()).length() > 0)) {
					populateSupportingFunctionParameters(ClassDefinitions.getSupportingMethodDefinition((String) packageCB.getSelectedItem(),
						(String) classCB.getSelectedItem(), null, (String) functionCB.getSelectedItem(), (String) sourceCombo.getSelectedItem()));
				} else {
					populateSupportingFunctionParameters(null);
				}
			}
		}

	}

	class TableCellComboBox extends DefaultCellEditor {

		private static final long serialVersionUID = -2299971668272166883L;

		public TableCellComboBox(String[] items) {
			super(new PopComboBox(items));
		}

	}

	class Expression_CBX_Right_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("EXP_CBX1")) {
					rhsTabPane.setEnabledAt(0, true);
					rhsTabPane.setEnabledAt(1, false);
					rhsTabPane.setSelectedIndex(0);
					enableActionPanel(false);
					actionTypeCB.setSelectedIndex(-1);
					actionTypeCB.setEnabled(true);
				} else if(((JCheckBox) e.getSource()).getName().equals("EXP_CBX2")) {
					rhsTabPane.setEnabledAt(0, false);
					rhsTabPane.setEnabledAt(1, true);
					rhsTabPane.setSelectedIndex(1);
				}
			}
		}

	}


	class LoCWindowAdapter extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			cancelExpressionOperand();
		}

	}

}
