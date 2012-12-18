package com.poplicus.crawler.codegen.gui.components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.poplicus.crawler.codegen.ArgumentDefinition;
import com.poplicus.crawler.codegen.ClassDefinitions;
import com.poplicus.crawler.codegen.Constants;
import com.poplicus.crawler.codegen.MethodDefinition;
import com.poplicus.crawler.codegen.definitions.ActionDefinition;
import com.poplicus.crawler.codegen.definitions.CrawlerDefinition;
import com.poplicus.crawler.codegen.definitions.ElementDefinition;
import com.poplicus.crawler.codegen.definitions.ExpressionDefinition;
import com.poplicus.crawler.codegen.definitions.ExpressionElementsDef;
import com.poplicus.crawler.codegen.definitions.FunctionDefinition;
import com.poplicus.crawler.codegen.definitions.IfElseElementsDef;
import com.poplicus.crawler.codegen.definitions.LHSDefinition;
import com.poplicus.crawler.codegen.definitions.LineOfCodeDefinition;
import com.poplicus.crawler.codegen.definitions.MainIfDefinition;
import com.poplicus.crawler.codegen.definitions.MainIfElementsDef;
import com.poplicus.crawler.codegen.definitions.NavigateDefinition;
import com.poplicus.crawler.codegen.definitions.OperandDefinition;
import com.poplicus.crawler.codegen.definitions.OperatorDefinition;
import com.poplicus.crawler.codegen.definitions.ParameterDefinition;
import com.poplicus.crawler.codegen.definitions.RightHSDefinition;
import com.poplicus.crawler.codegen.definitions.StringDefinition;
import com.poplicus.crawler.codegen.definitions.VariableDefinition;
import com.poplicus.crawler.codegen.definitions.WebPageDefinition;
import com.poplicus.crawler.codegen.factory.CrawlerCache;
import com.poplicus.crawler.codegen.factory.ExpressionCache;
import com.poplicus.crawler.codegen.factory.ObjectFactory;
import com.poplicus.crawler.codegen.gui.components.PopTreeNode.PopTreeNodeType;
import com.poplicus.crawler.codegen.utilities.CodeGenUtil;
import com.poplicus.crawler.codegen.utilities.config.CodeGenConfigCache;

public class PopLoCFrame extends PopJFrame {

	private static final long serialVersionUID = 3613117726087642246L;

	private static CodeGenUtil codeGenUtil = CodeGenUtil.getInstance();
	private static CrawlerCache crawlerCache = CrawlerCache.getInstance();
	private static ExpressionCache expressionCache = ExpressionCache.getInstance();
	private static ObjectFactory objectFactory = ObjectFactory.getInstance();
	private static CodeGenConfigCache configCache = CodeGenConfigCache.getInstance();
	
	private PopButton b_Done = null;
	private PopButton b_Cancel = null;

	private PopComboBox lhsTypeCB = null;
	private PopComboBox rhsTypeCB = null;

	private PopTextField variableNameTF = null;
	private PopComboBox variableTypeCB = null;
	private PopTextField variableValueTF = null;

	private PopTabbedPane rhsTabPane = null;
	private PopTabbedPane actionTabPane = null;

	private PopComboBox actionTypeCB = null;
	private PopComboBox browserTypeCB = null;

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

	private PopTextField operandTF = null;
	private PopButton b_Ellipsis = null;

	private PopButton b_addOperand = null;
	private PopButton b_updateOperand = null;
	private PopButton b_removeOperand = null;
	private PopButton b_resetOperand = null;

	private PopComboBox operatorCB = null;

	private PopButton b_addOperator = null;
	private PopButton b_updateOperator = null;
	private PopButton b_removeOperator = null;
	private PopButton b_resetOperator = null;

	private PopTable symbolsTable = null;

	private int symTableRowNumber = -1;

	private String actionOrExpression = null;
	private ActionDefinition expActionDef = null;
	private String expressionName = null;

	private PopButton b_addOrUpdateExpression = null;
	private PopButton b_resetExpression = null;

	private String previousExpressionName = "";

	public static PopTree rootTree = null;

	public PopLoCFrame(String browserType, String uniqueWPName, String sourceID) {
		this.setLayout(new GridBagLayout());
		this.setBrowserTypeOfWebPage(browserType);
		this.setUniqueNameOfWebPage(uniqueWPName);
		this.setSourceID(sourceID);

		Toolkit.getDefaultToolkit( ).setDynamicLayout(true);
		this.addWindowListener(new LoCWindowAdapter(this));
		this.setTitle("Create Line of Code");

		javax.swing.UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 11));
		javax.swing.UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.PLAIN, 11));

		this.add(getTypePanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		this.add(getLHSPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		this.add(getRHSPanel(), new GridBagConstraints(0, 2, 1, 1, 0.01, 0.01,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		this.add(getButtonsPanel(), new GridBagConstraints(0, 3, 1, 1, 0.01, 0.001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		enableLHSDefinition(false);
		enableRHSDefinition(false);
	}

	private JPanel getTypePanel() {
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new GridBagLayout());
		PopTitledBorder tBorder = new PopTitledBorder("LHS and RHS Type");
		typePanel.setBorder(tBorder);

		PopLabel lhsType = new PopLabel("LHS Type*");
		lhsTypeCB = new PopComboBox();
		lhsTypeCB.setName("CB_LHSType");
		populateLHSType();
		lhsTypeCB.addActionListener(new LHS_RHS_Type_Action_Listener());
		typePanel.add(lhsType, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		typePanel.add(lhsTypeCB, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 10), 0, 0));

		PopLabel rhsType = new PopLabel("RHS Type*");
		rhsTypeCB = new PopComboBox();
		rhsTypeCB.setName("CB_RHSType");
		rhsTypeCB.setEnabled(false);
		populateRHSType();
		rhsTypeCB.addActionListener(new LHS_RHS_Type_Action_Listener());
		typePanel.add(rhsType, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		typePanel.add(rhsTypeCB, new GridBagConstraints(3, 0, 3, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

		return typePanel;
	}

	private JPanel getLHSPanel() {
		JPanel lhsPanel = new JPanel();
		lhsPanel.setLayout(new GridBagLayout());
		PopTitledBorder tBorder = new PopTitledBorder("LHS Definition");
		lhsPanel.setBorder(tBorder);

		PopLabel variableType = new PopLabel("Variable Type*");
		variableTypeCB = new PopComboBox();
		variableTypeCB.setName("CB_VariableType");
		populateVariableType();
		lhsPanel.add(variableType, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		lhsPanel.add(variableTypeCB, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 10), 0, 0));

		PopLabel variableName = new PopLabel("Variable Name*");
		variableNameTF = new PopTextField();
		variableNameTF.setName("TF_VariableName");
		lhsPanel.add(variableName, new GridBagConstraints(2, 0, 1, 1, 0.05, 0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		lhsPanel.add(variableNameTF, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 10), 0, 0));


		PopLabel variableValue = new PopLabel("Variable Value*");
		variableValueTF = new PopTextField();
		variableValueTF.setName("TF_VariableValue");
		lhsPanel.add(variableValue, new GridBagConstraints(4, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		lhsPanel.add(variableValueTF, new GridBagConstraints(5, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		return lhsPanel;
	}

	private void populateVariableType() {
		variableTypeCB.addItem("");
		List<String> variables = configCache.getConfigurations().getVariableTypes();
		for(String variable : variables) {
			variableTypeCB.addItem(variable);
		}
//		variableTypeCB.addItem("string");
//		variableTypeCB.addItem("int");
	}

	private void populateLHSType() {
		lhsTypeCB.addItem("");
		lhsTypeCB.addItem("Declare Instance Variable");
		lhsTypeCB.addItem("Declare Instance Variable and Initialize");
		lhsTypeCB.addItem("Declare Method Variable");
		lhsTypeCB.addItem("Declare Method Variable and Initialize");
		lhsTypeCB.addItem("Assign Value To An Existing Variable");
		lhsTypeCB.addItem("No LHS");
	}

	private void populateRHSType() {
		rhsTypeCB.removeAllItems();
		rhsTypeCB.addItem("");
		rhsTypeCB.addItem("Assign Value To An Existing Variable");
		rhsTypeCB.addItem("Perform an Action");
		rhsTypeCB.addItem("Return Value From Action");
		rhsTypeCB.addItem("Expression");
		rhsTypeCB.addItem("NoRHS");
	}

	private void populateRHSTypeForNoLHS() {
		rhsTypeCB.removeAllItems();
		rhsTypeCB.addItem("");
		rhsTypeCB.addItem("Perform an Action");
	}

	private void populateRHSTypeForVariableDeclaration() {
		rhsTypeCB.removeAllItems();
		rhsTypeCB.addItem("");
		rhsTypeCB.addItem("No RHS");
	}

	private void populateRHSTypeForVariableDeclarationAndInitialization() {
		rhsTypeCB.removeAllItems();
		rhsTypeCB.addItem("");
		rhsTypeCB.addItem("Assign Value To An Existing Variable");
		rhsTypeCB.addItem("Return Value From Action");
		rhsTypeCB.addItem("Expression");
		rhsTypeCB.addItem("No RHS");
	}

	private void populateRHSTypeToAssignValueToAnExistingVariable() {
		rhsTypeCB.removeAllItems();
		rhsTypeCB.addItem("");
		rhsTypeCB.addItem("Assign Value To An Existing Variable");
		rhsTypeCB.addItem("Return Value From Action");
		rhsTypeCB.addItem("Expression");
		rhsTypeCB.addItem("No RHS");
	}

	private JPanel getRHSPanel() {
		JPanel rhsPanel = new JPanel();
		rhsPanel.setLayout(new GridBagLayout());
		PopTitledBorder tBorder = new PopTitledBorder("RHS Definition");
		rhsPanel.setBorder(tBorder);

		rhsTabPane = new PopTabbedPane();
		rhsTabPane.add("Action", getActionPanel());
		rhsTabPane.add("Expression", getExpressionPanel());

		rhsPanel.add(rhsTabPane, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		return rhsPanel;
	}

	private JPanel getActionPanel() {
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

		PopLabel browserType = new PopLabel("Browser Type*");
		browserTypeCB = new PopComboBox();
		browserTypeCB.setName("CB_BrowserType");
		populateBrowserType();
		browserTypeCB.setEnabled(false);
		browserTypeCB.addActionListener(new Action_Type_Action_Listener());
		actionPanel.add(browserType, new GridBagConstraints(2, 0, 1, 0, 0.005, 0.001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 0), 0, 0));
		actionPanel.add(browserTypeCB, new GridBagConstraints(3, 0, 1, 0, 0.005, 1.0,
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

	private void populateBrowserType() {
		browserTypeCB.addItem("");
		List<String> browsers = configCache.getConfigurations().getBrowserToExecute();
		for(String browser : browsers) {
			browserTypeCB.addItem(browser);
		}
//		browserTypeCB.addItem("Primary");
//		browserTypeCB.addItem("Secondary");
//		browserTypeCB.addItem("Tertiary");
		if(getBrowserTypeOfWebPage() != null) {
			if(getBrowserTypeOfWebPage().equalsIgnoreCase("Primary")) {
				browserTypeCB.setSelectedIndex(1);
			} else if(getBrowserTypeOfWebPage().equalsIgnoreCase("Secondary")) {
				browserTypeCB.setSelectedIndex(2);
			} else if(getBrowserTypeOfWebPage().equalsIgnoreCase("Tertiary")) {
				browserTypeCB.setSelectedIndex(3);
			}
		}
	}

	private JPanel getElementsPanel() {
		JPanel elementsPanel = new JPanel();
		elementsPanel.setLayout(new GridBagLayout());

		PopLabel tagType = new PopLabel("Tag Type*");
		tagTypeCB = new PopComboBox();
		tagTypeCB.setName("CB_ElementTagType");
		populateElementTagType();
		tagTypeCB.addActionListener(new TagType_ActionListener());

		elementsPanel.add(tagType, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		elementsPanel.add(tagTypeCB, new GridBagConstraints(1, 0, 1, 1, 0.1, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		PopLabel fieldType = new PopLabel("Field Type*");
		fieldTypeCB = new PopComboBox();
		fieldTypeCB.setName("CB_ElementFieldType");
		//populateElementFieldType();

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

	private void populateElementTagType() {
		tagTypeCB.addItem("");
		List<String> elements = configCache.getConfigurations().getSupportedHTMLElements();
		for(String element : elements) {
			tagTypeCB.addItem(element);
		}
//		tagTypeCB.addItem("Image");
//		tagTypeCB.addItem("Anchor");
//		tagTypeCB.addItem("Input");
//		tagTypeCB.addItem("Button");
	}

	private void populateElementFieldType(String element) {
		fieldTypeCB.addItem("");
		List<String> fields = configCache.getConfigurations().getSupportedFieldsOfElement(element);
		for(String field : fields) {
			fieldTypeCB.addItem(field);
		}
//		fieldTypeCB.addItem("InnerHTML");
//		fieldTypeCB.addItem("Value");
//		fieldTypeCB.addItem("Name");
//		fieldTypeCB.addItem("Source");
//		fieldTypeCB.addItem("Type");
//		fieldTypeCB.addItem("HREF");
//		fieldTypeCB.addItem("ID");
//		fieldTypeCB.addItem("ClassName");
//		fieldTypeCB.addItem("CheckedAttribute");
//		fieldTypeCB.addItem("InnerText");
	}

	private JPanel getNavigationPanel() {
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

	private JPanel getFunctionDefinitionPanel() {
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

	private void populatePackage() {
		List<String> packages = ClassDefinitions.getPackages();
		packageCB.removeAllItems();
		packageCB.addItem("");
		for (String packageName : packages) {
			if(! packageName.equalsIgnoreCase("csExWB")) {
				packageCB.addItem(packageName);
			}
		}
	}

	private void populateCSExWBPackage() {
		packageCB.removeAllItems();
		packageCB.addItem("");
		packageCB.addItem("csExWB");
	}

	private void populateClass(List<String> classes) {
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

	private void populateFunction(List<String> functions) {
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

	private void populateSupportingFunction(List<String> functions) {
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

	private void populateFunctionParameters(MethodDefinition mDef) {
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

	private void populateSupportingFunctionParameters(MethodDefinition mDef) {
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
		//actionTypeCB.addItem("LinkClick");
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

	private JPanel getExpressionPanel() {
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
		expressionNamePanel.add(expressionNameCB, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 250, 0));

//		superExpressionCBX = new PopCheckBox("Use Expression");
//		expressionNamePanel.add(superExpressionCBX, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
//				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));

		b_addOrUpdateExpression = new PopButton("Add / Update");
		b_addOrUpdateExpression.setName("B_AddOrUpdateExpression");
		b_addOrUpdateExpression.addActionListener(new Expression_Button_Action_Listener());
		expressionNamePanel.add(b_addOrUpdateExpression, new GridBagConstraints(2, 0, 1, 1, 0.001, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 10), 0, 0));

		b_resetExpression = new PopButton("Reset");
		b_resetExpression.setName("B_ResetExpression");
		b_resetExpression.addActionListener(new Expression_Button_Action_Listener());
		expressionNamePanel.add(b_resetExpression, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));

		expressionPanel.add(expressionNamePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		JPanel operandPanel = new JPanel();
		operandPanel.setLayout(new GridBagLayout());
		PopTitledBorder operandBorder = new PopTitledBorder("Add / Update / Delete Operand");
		operandPanel.setBorder(operandBorder);

		PopLabel operand = new PopLabel("Operand");
		operandTF = new PopTextField();
		b_Ellipsis = new PopButton("...");
		b_Ellipsis.setName("B_Ellipsis");
		b_Ellipsis.addActionListener(new Ellipsis_Name_Action_Listener());

		operandPanel.add(operand, new GridBagConstraints(0, 0, 1, 1, 0, 0.001,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 0), 0, 0));
		operandPanel.add(operandTF, new GridBagConstraints(1, 0, 1, 1, 1.1, 1.0,
			GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 10, 5, 10), 0, 0));
		operandPanel.add(b_Ellipsis, new GridBagConstraints(2, 0, 1, 1, 0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, -5, 5, 5), -30, 0));

		JPanel obPanel = new JPanel();
		obPanel.setLayout(new GridBagLayout());

		JPanel invisiblePanel = new JPanel();
		obPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		b_addOperand = new PopButton("Add");
		b_addOperand.setName("Operand_B_Add");
		b_addOperand.addActionListener(new Operand_Button_Action_Listener());
		obPanel.add(b_addOperand, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		b_updateOperand = new PopButton("Update");
		b_updateOperand.setName("Operand_B_Update");
		b_updateOperand.setEnabled(false);
		b_updateOperand.addActionListener(new Operand_Button_Action_Listener());
		obPanel.add(b_updateOperand, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		b_removeOperand = new PopButton("Delete");
		b_removeOperand.setName("Operand_B_Delete");
		b_removeOperand.setEnabled(false);
		b_removeOperand.addActionListener(new Operand_Button_Action_Listener());
		obPanel.add(b_removeOperand, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		b_resetOperand = new PopButton("Reset");
		b_resetOperand.setName("Operand_B_Reset");
		b_resetOperand.addActionListener(new Operand_Button_Action_Listener());
		obPanel.add(b_resetOperand, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		operandPanel.add(obPanel, new GridBagConstraints(0, 1, 3, 1, 1.0, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 8, 0), 0, 0));

		JPanel operatorPanel = new JPanel();
		operatorPanel.setLayout(new GridBagLayout());
		PopTitledBorder operatorBorder = new PopTitledBorder("Add / Update / Delete Operator");
		operatorPanel.setBorder(operatorBorder);

		PopLabel operator = new PopLabel("Operator");
		operatorCB = new PopComboBox();
		populateOperator();
		//operatorCB.addActionListener(new Operator_Action_Listener());
		operatorPanel.add(operator, new GridBagConstraints(0, 0, 1, 1, 0, 0.001,
			GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 5, 0), 0, 0));
		operatorPanel.add(operatorCB, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.001,
			GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 100, 0));

		JPanel operatorBPanel = new JPanel();
		operatorBPanel.setLayout(new GridBagLayout());

		JPanel invisibleOperatorPanel = new JPanel();
		operatorBPanel.add(invisibleOperatorPanel, new GridBagConstraints(0, 0, 1, 1, 0.22, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		b_addOperator = new PopButton("Add");
		b_addOperator.setName("Operator_B_Add");
		b_addOperator.addActionListener(new Operator_Button_Action_Listener());
		operatorBPanel.add(b_addOperator, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 0, 0));

		b_updateOperator = new PopButton("Update");
		b_updateOperator.setName("Operator_B_Update");
		b_updateOperator.setEnabled(false);
		b_updateOperator.addActionListener(new Operator_Button_Action_Listener());
		operatorBPanel.add(b_updateOperator, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		b_removeOperator = new PopButton("Delete");
		b_removeOperator.setName("Operator_B_Delete");
		b_removeOperator.setEnabled(false);
		b_removeOperator.addActionListener(new Operator_Button_Action_Listener());
		operatorBPanel.add(b_removeOperator, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		b_resetOperator = new PopButton("Reset");
		b_resetOperator.setName("Operator_B_Reset");
		b_resetOperator.addActionListener(new Operator_Button_Action_Listener());
		operatorBPanel.add(b_resetOperator, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 1), 0, 0));

		operatorPanel.add(operatorBPanel, new GridBagConstraints(2, 0, 1, 1, 1.0, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 8, 0), 0, 0));

		expressionPanel.add(operandPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.001,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		expressionPanel.add(operatorPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.001,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		expressionPanel.add(getSymbolsTablePanel(), new GridBagConstraints(0, 3, 2, 2, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));

		return expressionPanel;
	}

	private void populateExpressionNames() {
		List<String> expNames = expressionCache.getExpressionNames();
		expressionNameCB.removeAllItems();
		expressionNameCB.addItem("");
		for(String expName : expNames) {
			expressionNameCB.addItem(expName);
		}
	}

	private void populateOperator() {
		operatorCB.addItem("");
		List<String> operators = configCache.getConfigurations().getExpressionOperators();
		for(String operator : operators) {
			operatorCB.addItem(operator);
		}
//		operatorCB.addItem("Plus");
//		operatorCB.addItem("Minus");
//		operatorCB.addItem("Multiply");
//		operatorCB.addItem("Divide");
//		operatorCB.addItem("Modulus");
	}

	private JScrollPane getSymbolsTablePanel() {
		Object[] columnNames = {"Symbols", "Operand Name"};
		Object[][] rowData = new Object[0][0];
		symbolsTable = new PopTable(rowData, columnNames);
		symbolsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
		symbolsTable.addMouseListener(new Symbol_Table_Mouse_Adapter());

		JScrollPane scrollPane = new JScrollPane(symbolsTable);
		symbolsTable.setFillsViewportHeight(true);

		symbolsTable.removeColumn(symbolsTable.getColumnModel().getColumn(1));

		return scrollPane;
	}

	private PopExpressionFrame getExpressionFrame() {
		PopExpressionFrame expressionFrame = new PopExpressionFrame(getBrowserTypeOfWebPage(),
			getUniqueNameOfWebPage(), getSourceID());
		expressionFrame.setSize(800, 550); //800, 480
		expressionFrame.setLocation(175, 150);
		expressionFrame.setLocFrame(this);
		if(getActionOrExpression() != null) {
			if(getActionOrExpression().equalsIgnoreCase("Action")) {
				expressionFrame.setActionOrExpression("Action");
				expressionFrame.setExpActionDef(getExpActionDef());
			} else if(getActionOrExpression().equalsIgnoreCase("Expression")) {
				expressionFrame.setActionOrExpression("Expression");
				expressionFrame.setExpressionName(getExpressionName());
				expressionFrame.setPreviousExpressionName(getExpressionName());
			}
			expressionFrame.initializeFrame();
		}
		expressionFrame.setContainingFrame(this);
		return expressionFrame;
	}

	private String getActionOrExpression() {
		return actionOrExpression;
	}

	public void setActionOrExpression(String actionOrExpression) {
		this.actionOrExpression = actionOrExpression;
	}

	private ActionDefinition getExpActionDef() {
		return expActionDef;
	}

	public void setExpActionDef(ActionDefinition expActionDef) {
		this.expActionDef = expActionDef;
	}

	private String getExpressionName() {
		return expressionName;
	}

	public void setExpressionName(String expressionName) {
		this.expressionName = expressionName;
	}

//	private String getOperandText() {
//		return operandTF.getText();
//	}

	public void setOperandText(String text) {
		this.operandTF.setText(text);
	}

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

	private void enableLHSDefinition(boolean enableFlag) {
		variableNameTF.setEnabled(enableFlag);
		variableTypeCB.setEnabled(enableFlag);
		variableValueTF.setEnabled(enableFlag);
	}

	private void enableRHSDefinition(boolean enableFlag) {
		rhsTabPane.setEnabledAt(0, enableFlag);
		rhsTabPane.setEnabledAt(1, enableFlag);
		rhsTabPane.setSelectedIndex(0);
		enableActionPanel(enableFlag);
		enableExpressionPanel(enableFlag);
	}

	private void enableActionPanel(boolean enableFlag) {
		actionTypeCB.setEnabled(enableFlag);
		//browserTypeCB.setEnabled(enableFlag);
		actionTabPane.setEnabledAt(0, enableFlag);
		actionTabPane.setEnabledAt(1, enableFlag);
		actionTabPane.setEnabledAt(2, enableFlag);
		actionTabPane.setSelectedIndex(0);
		enableElementsPanel(enableFlag);
		enableNavigationPanel(enableFlag);
		enableFunctionPanel(enableFlag);
	}

	private void enableElementsPanel(boolean enableFlag) {
		tagTypeCB.setEnabled(enableFlag);
		fieldTypeCB.setEnabled(enableFlag);
		elementValueTF.setEnabled(enableFlag);
		exactMatchCKB.setEnabled(enableFlag);
	}

	private void enableNavigationPanel(boolean enableFlag) {
		urlTF.setEnabled(enableFlag);
	}

	private void enableFunctionPanel(boolean enableFlag) {
		packageCB.setEnabled(enableFlag);
		classCB.setEnabled(enableFlag);
		functionCB.setEnabled(enableFlag);
		supportingFunctionCB.setEnabled(enableFlag);
		methodParamsTable.setEnabled(enableFlag);
		supportingMethodParamsTable.setEnabled(enableFlag);
	}

	private void enableExpressionPanel(boolean enableFlag) {

	}

	private void enableRHSDefForAssignValueToAnExistingVariable(boolean enableFlag) {
		rhsTabPane.setEnabledAt(0, enableFlag);
		actionTypeCB.setEnabled(enableFlag);
		actionTabPane.setEnabledAt(2, enableFlag);
		actionTabPane.setSelectedIndex(2);
		enableFunctionPanel(enableFlag);
	}

	private void enableRHSDefForPerformAnAction(boolean enableFlag) {
		rhsTabPane.setEnabledAt(0, enableFlag);
		actionTypeCB.setEnabled(enableFlag);
	}

	private void enableRHSDefForReturnValueFromAction(boolean enableFlag) {
		rhsTabPane.setEnabledAt(0, enableFlag);
		actionTypeCB.setEnabled(enableFlag);
		actionTabPane.setEnabledAt(2, enableFlag);
		actionTabPane.setSelectedIndex(2);
		enableFunctionPanel(enableFlag);
	}

	private void enableRHSExpression(boolean enableFlag) {
		rhsTabPane.setEnabledAt(1, enableFlag);
		rhsTabPane.setSelectedIndex(1);
		enableExpressionPanel(enableFlag);
	}

	private void enableNoRHS(boolean enableFlag) {
		enableRHSDefinition(enableFlag);
	}

	private void populateActionTypeForAssignValueToAnExistingVariable() {
		actionTypeCB.removeAllItems();
		actionTypeCB.addItem("");
		actionTypeCB.addItem("Function Call");
	}

	private void populateActionTypeForPerformAnAction() {
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

	private void populateActionTypeForReturnValueFromAction() {
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

	private void performUIUpdateForRHSTypeChange(String selectedItem) {
		if(selectedItem.length() > 0) {
			enableRHSDefinition(false);
			if(selectedItem.equals("Assign Value To An Existing Variable")) {
				enableRHSDefForAssignValueToAnExistingVariable(true);
				populateActionTypeForAssignValueToAnExistingVariable();
				populatePackage();
				actionTypeCB.setSelectedIndex(1);
			} else if(selectedItem.equals("Perform an Action")) {
				enableRHSDefForPerformAnAction(true);
				populateActionTypeForPerformAnAction();
			} else if(selectedItem.equals("Return Value From Action")) {
				enableRHSDefForReturnValueFromAction(true);
				populateActionTypeForReturnValueFromAction();
				populateCSExWBPackage();
				actionTypeCB.setSelectedIndex(1);
			} else if(selectedItem.equals("Expression")) {
				enableRHSExpression(true);
			} else if(selectedItem.equals("No RHS")) {
				enableNoRHS(false);
				populateActionType();
			}
		} else {
			enableRHSDefinition(false);
			populateActionType();
		}
	}

	private Object getDefinition() {
		ActionDefinition actionDef = null;
		String lhsType = (String) lhsTypeCB.getSelectedItem();
		String rhsType = null;
		boolean actionFlow = false;

		if(lhsType != null && lhsType.length() > 0) {
			if(lhsType.equals("No LHS")) {
				rhsType = (String) rhsTypeCB.getSelectedItem();
				if(rhsType != null && rhsType.length() > 0) {
					if(rhsType.equals("Perform an Action")) {
						actionFlow = true;
						String actionType = (String) actionTypeCB.getSelectedItem();
						if(! (actionType != null && actionType.length() > 0)) {
							JOptionPane.showMessageDialog(this, "Action Type is mandatory.");
							actionTypeCB.requestFocus();
							return null;
						}
						String browserType = (String) browserTypeCB.getSelectedItem();
						if(! (browserType != null && browserType.length() > 0)) {
							JOptionPane.showMessageDialog(this, "Browser Type is mandatory.");
							browserTypeCB.requestFocus();
							return null;
						}
						actionDef = getActionDef(actionType, browserType);
					}
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Line of Code definition is empty.");
			lhsTypeCB.requestFocus();
			return null;
		}
		if(actionFlow) {
			return actionDef;
		} else {
			LineOfCodeDefinition locDef = new LineOfCodeDefinition();
			LHSDefinition lhsDef = getLHSDefinition();
			RightHSDefinition rhsDef = null;
			if(lhsDef != null) {
				rhsDef = getRightHSDefinition();
			}

			if(lhsDef != null) {
				VariableDefinition varDef = lhsDef.getVariableDefinition();
				rhsType = (String) rhsTypeCB.getSelectedItem();
				if(rhsType != null && rhsType.length() > 0) {
					String variableValue = (String) variableValueTF.getText();
					if(variableValue != null && variableValue.trim().length() > 0) {
						varDef.setValue(variableValue);
					} else {
						if(rhsType.equals("No RHS")) {
							if(! (lhsDef.getType().equalsIgnoreCase("DeclareMethodVariable")
								|| lhsDef.getType().equalsIgnoreCase("DeclareInstanceVariable"))) {
								JOptionPane.showMessageDialog(this, "Variable Value is mandatory.");
								variableValueTF.requestFocus();
								return null;
							}
						}
					}
				}
			}

			if(lhsDef == null || rhsDef == null) {
				locDef = null;
			} else {
				locDef.setLhsDefiniton(lhsDef);
				locDef.setRhsDefinition(rhsDef);
			}

			return locDef;
		}
	}

	private String getWebPageType() {
		CrawlerDefinition crawlerDef = crawlerCache.get(getSourceID());
		WebPageDefinition webPageDef = crawlerDef.getWebPage(getUniqueNameOfWebPage());
		return webPageDef.getType();
	}

	private LHSDefinition getLHSDefinition() {
		LHSDefinition lhsDef = null;
		VariableDefinition varDef = null;
		String lhsType = (String) lhsTypeCB.getSelectedItem();
		if(lhsType != null && lhsType.length() > 0) {
			lhsDef = new LHSDefinition();
			lhsDef.setType(codeGenUtil.getValidLHSTypeString(lhsType));
			varDef = new VariableDefinition();
			if(! lhsType.equals("No LHS")) {
				String variableType = (String) variableTypeCB.getSelectedItem();
				if( variableType != null && variableType.length() > 0) {
					varDef.setType(variableType);
				} else {
					JOptionPane.showMessageDialog(this, "Variable Type is mandatory.");
					variableTypeCB.requestFocus();
					return null;
				}
				String variableName = (String) variableNameTF.getText();
				if(variableName != null && variableName.trim().length() > 0) {
					varDef.setName(variableName);
				} else {
					JOptionPane.showMessageDialog(this, "Variable Name is mandatory.");
					variableNameTF.requestFocus();
					return null;
				}
			}
			lhsDef.setVariableDefinition(varDef);
		} else {
			JOptionPane.showMessageDialog(this, "Line of Code definition is empty.");
			lhsTypeCB.requestFocus();
			return null;
		}

		return lhsDef;
	}

	private RightHSDefinition getRightHSDefinition() {
		RightHSDefinition rhsDef = null;
		String rhsType = (String) rhsTypeCB.getSelectedItem();
		if(rhsType != null && rhsType.length() > 0) {
			rhsDef = new RightHSDefinition();
			rhsDef.setType(codeGenUtil.getValidRHSTypeString(rhsType));
			if(! rhsType.equals(Constants.RHS_TYPE_NO_RHS)) {
				String actionType = null;
				String browserType = null;
				if(! rhsType.equals(Constants.RHS_TYPE_EXPRESSION)) {
					actionType = (String) actionTypeCB.getSelectedItem();
					if(! (actionType != null && actionType.length() > 0)) {
						JOptionPane.showMessageDialog(this, "Action Type is mandatory.");
						actionTypeCB.requestFocus();
						return null;
					}

					browserType = (String) browserTypeCB.getSelectedItem();
					if(! (browserType != null && browserType.length() > 0)) {
						JOptionPane.showMessageDialog(this, "Browser Type is mandatory.");
						browserTypeCB.requestFocus();
						return null;
					}
				} else {
					if(! (expressionNameCB.getSelectedItem() != null
						&& ((String) expressionNameCB.getSelectedItem()).length() > 0)) {
						JOptionPane.showMessageDialog(this, "Expression Name is mandatory.");
						browserTypeCB.requestFocus();
						return null;
					}
				}

				if(rhsType.equals(Constants.RHS_TYPE_WITH_SPACE_ASSIGN_VALUE_TO_AN_EXISTING_VARIABLE)) {
					ActionDefinition actionDef = getActionDef_Function(actionType, browserType);
					if(actionDef != null) {
						actionDef.setType(codeGenUtil.getValidActionTypeString(actionType));
						actionDef.setBrowserType(browserType);
					}
					if(actionDef != null) {
						rhsDef.setFunctionDef((FunctionDefinition) actionDef.getHtmlElement());
					} else {
						rhsDef = null;
					}
				} else if(rhsType.equals(Constants.RHS_TYPE_WITH_SPACE_RETURN_VALUE_FROM_ACTION)) {
					ActionDefinition actionDef = getActionDef_Function(actionType, browserType);
					if(actionDef != null) {
						actionDef.setType(codeGenUtil.getValidActionTypeString(actionType));
						actionDef.setBrowserType(browserType);
					}
					if(actionDef != null) {
						rhsDef.setActionDef(actionDef);
					} else {
						rhsDef = null;
					}
				} else if(rhsType.equals(Constants.RHS_TYPE_EXPRESSION)) {
					ExpressionDefinition expressionDef = getExpressionDef();
					if(expressionDef != null) {
						rhsDef.setExpressionDef(expressionDef);
					} else {
						rhsDef = null;
					}
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "RHS Type is mandatory.");
			rhsTypeCB.requestFocus();
			return null;
		}

		return rhsDef;
	}

	private boolean isValidExpression() {
		boolean flag = false;
		if(symbolsTable != null && symbolsTable.getRowCount() > 0) {
			if(symbolsTable.getDefaultModel().getValueAt((symbolsTable.getRowCount() - 1), 1) instanceof OperandDefinition) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	private ActionDefinition getActionDef(String actionType, String browserType) {
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

	private ActionDefinition getActionDef_Element(String actionType, String browserType) {
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

	private ActionDefinition getActionDef_Navigation(String actionType, String browserType) {
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

	private ActionDefinition getActionDef_Function(String actionType, String browserType) {
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

	private boolean validateFunctionParameters(FunctionDefinition funcDef, String functionName) {
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

	private boolean validateSupportingFunctionParameters(FunctionDefinition funcDef, String functionName) {
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

//	public ExpressionDefinition getExpressionDef() {
//		ExpressionDefinition expressionDef = null;
//		if(symbolsTable != null && symbolsTable.getRowCount() > 0) {
//			expressionDef = new ExpressionDefinition();
//			int noOfRows = symbolsTable.getRowCount();
//			for(int index = 0; index < noOfRows; index ++) {
//				expressionDef.addExpressionElement(
//					(ExpressionElementsDef) symbolsTable.getDefaultModel().getValueAt(index, 1));
//			}
//		}
//		expressionCache.put(expressionDef.getName(), expressionDef);
//		return expressionDef;
//	}

	private ExpressionDefinition getExpressionDef() {
		return expressionCache.get((String) expressionNameCB.getSelectedItem());
	}

	private void setEnableOperandTF(boolean flag) {
		this.operandTF.setEnabled(flag);
	}

	private void cancelLineOfCode(PopLoCFrame locFrame) {
		rootTree.getContainingFrame().setEnabled(true);
		rootTree.getContainingFrame().setVisible(true);
		locFrame.dispose();
	}

	private void addLineOfCodeToTree(PopLoCFrame locFrame, LineOfCodeDefinition locDef) throws Exception {
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
				openingNode = new PopTreeNode(lineOfCodeString, false, getNodeType());
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


	private IfElseElementsDef getIfElseElementsDef_LineOfCode(LineOfCodeDefinition locDef, PopTreeNode openingNode) {
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

	private IfElseElementsDef getIfElseElementsDef_Action(ActionDefinition actionDef, PopTreeNode openingNode) {
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

	private MainIfElementsDef getMainIfElseElementsDef_LineOfCode(LineOfCodeDefinition locDef, PopTreeNode openingNode) {
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

	private MainIfElementsDef getMainIfElseElementsDef_Action(ActionDefinition actionDef, PopTreeNode openingNode) {
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

	private void addActionToTree(PopLoCFrame locFrame, ActionDefinition actionDef) throws Exception {
		rootTree.getContainingFrame().setEnabled(false);
		PopTreeNode openingNode = null;
		PopTreeNode localParentNode = null;
		if(getParentNode() != null) {
			localParentNode = getParentNode();
		} else {
			localParentNode = (PopTreeNode) rootTree.getSelectionPath().getLastPathComponent();
		}
		if(localParentNode instanceof PopTreeNode) {
			String actionString = objectFactory.getActionFromActionDef(actionDef, getWebPageName()).toStringBuffer().toString();

			if(actionString != null && actionString.length() > 0) {
				openingNode = new PopTreeNode(actionString, false, getNodeType());
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

	    addActionToDefinition(actionDef, openingNode);
	}

	private void addActionToDefinition(ActionDefinition actionDef, PopTreeNode openingNode) {
		if(openingNode != null) {
			CrawlerDefinition crawlerDef = crawlerCache.get(getSourceID());
			WebPageDefinition webPageDef = crawlerDef.getWebPage(getUniqueNameOfWebPage());

			if(openingNode.getParentNodeType().equals(PopTreeNodeType.None)) {
				MainIfDefinition mainIfDef = webPageDef.getMainIF();
				if(mainIfDef == null) {
					mainIfDef = new MainIfDefinition();
				}
				mainIfDef.setMainIfElementsDef(getMainIfElseElementsDef_Action(actionDef, openingNode));
				webPageDef.setMainIF(mainIfDef);
			} else {
				webPageDef.addIfElseElementsDef(getIfElseElementsDef_Action(actionDef, openingNode));
			}
			crawlerDef.replaceWebPage(webPageDef);
			crawlerCache.put(getSourceID(), crawlerDef);
		}
	}

	private void ellipsisWarning() {
		if(b_addOperand.isEnabled() || b_updateOperand.isEnabled()) {
			if(operandTF.getText() != null && operandTF.getText().length() > 0) {
			    int response = JOptionPane.showConfirmDialog(null,
			    	"This operation may overwrite data present in Operand field. \nDo you want to continue?", "Confirm",
				    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			    if (response == JOptionPane.YES_OPTION) {
					setEnableOperandTF(false);
					this.setEnabled(false);
					getExpressionFrame().setVisible(true);
			    }
			} else {
				setEnableOperandTF(false);
				getExpressionFrame().setVisible(true);
			}
		}
	}

	private void addOperand() {
		if(operandTF.getText() != null && operandTF.getText().length() > 0) {
			if(canAddOperand()) {
				OperandDefinition operandDef = new OperandDefinition();
				if(getActionOrExpression() != null) {
					if(getActionOrExpression().equals("Action")) {
						if(getExpActionDef() != null) {
							operandDef.setActionDef(getExpActionDef());
						}
					} else if(getActionOrExpression().equals("Expression")) {
						if(expressionCache.get(getExpressionName()) != null) {
							operandDef.setExpressionDef(expressionCache.get(getExpressionName()));
						}
					} else {
						StringDefinition stringDef = new StringDefinition("", operandTF.getText(), "");
						operandDef.setStringDef(stringDef);
					}
				} else {
					StringDefinition stringDef = new StringDefinition("", operandTF.getText(), "");
					operandDef.setStringDef(stringDef);
				}
				symbolsTable.addRow(new Object[]{operandTF.getText(), operandDef});
				resetOperandObjects();
				resetOperatorObjects();
			} else {
				JOptionPane.showMessageDialog(this, "Only an Operator can be added after an Operand.");
				b_addOperand.requestFocus();
				return;
			}
		} else {
			JOptionPane.showMessageDialog(this, "Operand definition is empty.");
			b_addOperand.requestFocus();
			return;
		}
	}

	private void updateOperand() {
		if(operandTF.getText() != null && operandTF.getText().length() > 0) {
			symbolsTable.getDefaultModel().setValueAt(operandTF.getText(), symTableRowNumber, 0);

			OperandDefinition operandDef = new OperandDefinition();
			if(getActionOrExpression() != null) {
				if(getActionOrExpression().equals("Action")) {
					if(getExpActionDef() != null) {
						operandDef.setActionDef(getExpActionDef());
					}
				} else if(getActionOrExpression().equals("Expression")) {
					if(expressionCache.get(getExpressionName()) != null) {
						operandDef.setExpressionDef(expressionCache.get(getExpressionName()));
					}
				} else {
					StringDefinition stringDef = new StringDefinition("", operandTF.getText(), "");
					operandDef.setStringDef(stringDef);
				}
			} else {
				StringDefinition stringDef = new StringDefinition("", operandTF.getText(), "");
				operandDef.setStringDef(stringDef);
			}
			symbolsTable.getDefaultModel().setValueAt(operandDef, symTableRowNumber, 1);
			resetOperandObjects();
			resetOperatorObjects();
		} else {
			JOptionPane.showMessageDialog(this, "Operand definition is empty.");
			b_addOperand.requestFocus();
			return;
		}
	}

	private void deleteOperand() {
		symbolsTable.getDefaultModel().removeRow(symTableRowNumber);
		symbolsTable.updateUI();
		resetOperandObjects();
		resetOperatorObjects();
	}

	private void clearOperand() {
		resetOperandObjects();
	}

	private void addOperator() {
		if(operatorCB.getSelectedItem() != null && ((String) operatorCB.getSelectedItem()).length() > 0) {
			if(canAddOperator()) {
				OperatorDefinition operatorDef = new OperatorDefinition();
				operatorDef.setOperator((String) operatorCB.getSelectedItem());
				symbolsTable.addRow(
					new Object[]{codeGenUtil.getOperatorSymbol((String) operatorCB.getSelectedItem()), operatorDef});
				resetOperatorObjects();
				resetOperandObjects();
			} else {
				if(symbolsTable.getRowCount() == 0) {
					JOptionPane.showMessageDialog(this, "Only an Operand can be added as first symbol.");
				} else {
					JOptionPane.showMessageDialog(this, "Only an Operand can be added after an Operator.");
				}
				b_addOperator.requestFocus();
				return;
			}
		} else {
			JOptionPane.showMessageDialog(this, "Select an Operator.");
			operatorCB.requestFocus();
			return;
		}
	}

	private void updateOperator() {
		if(operatorCB.getSelectedItem() != null && ((String) operatorCB.getSelectedItem()).length() > 0) {
			symbolsTable.getDefaultModel().setValueAt(
				codeGenUtil.getOperatorSymbol((String) operatorCB.getSelectedItem()), symTableRowNumber, 0);

			OperatorDefinition operatorDef = new OperatorDefinition();
			operatorDef.setOperator((String) operatorCB.getSelectedItem());
			symbolsTable.getDefaultModel().setValueAt(operatorDef, symTableRowNumber, 1);
			resetOperatorObjects();
			resetOperandObjects();
		} else {
			JOptionPane.showMessageDialog(this, "Select an Operator.");
			operatorCB.requestFocus();
			return;
		}
	}

	private void deleteOperator() {
		symbolsTable.getDefaultModel().removeRow(symTableRowNumber);
		symbolsTable.updateUI();
		resetOperandObjects();
		resetOperatorObjects();
	}

	private void clearOperator() {
		resetOperatorObjects();
	}

	public void resetOperandObjects() {
		operandTF.setText("");
		setExpActionDef(null);
		setExpressionName("");
		setActionOrExpression("");
		setEnableOperandTF(true);
		b_addOperand.setEnabled(true);
		b_updateOperand.setEnabled(false);
		b_removeOperand.setEnabled(false);
	}

	private void resetOperatorObjects() {
		operatorCB.setSelectedIndex(-1);
		b_addOperator.setEnabled(true);
		b_updateOperator.setEnabled(false);
		b_removeOperator.setEnabled(false);
	}

	private boolean canAddOperand() {
		boolean flag = true;
		int rowCount = symbolsTable.getRowCount();
		if(rowCount > 0) {
			if(symbolsTable.getDefaultModel().getValueAt((rowCount - 1), 1) instanceof OperandDefinition) {
				flag = false;
			} else if(symbolsTable.getDefaultModel().getValueAt((rowCount - 1), 1) instanceof OperatorDefinition) {
				flag = true;
			}
		} else {
			flag = true;
		}
		return flag;
	}

	private boolean canAddOperator() {
		boolean flag = true;
		int rowCount = symbolsTable.getRowCount();
		if(rowCount > 0) {
			if(symbolsTable.getDefaultModel().getValueAt((rowCount - 1), 1) instanceof OperandDefinition) {
				flag = true;
			} else if(symbolsTable.getDefaultModel().getValueAt((rowCount - 1), 1) instanceof OperatorDefinition) {
				flag = false;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	private void setEnableOperandButtons(boolean flag) {
		b_addOperand.setEnabled(flag);
		b_updateOperand.setEnabled(flag);
		b_removeOperand.setEnabled(flag);
	}

	private void setEnableOperatorButtons(boolean flag) {
		b_addOperator.setEnabled(flag);
		b_updateOperator.setEnabled(flag);
		b_removeOperator.setEnabled(flag);
	}

	private void displayMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	private void addOrUpdateExpression() {
		ExpressionDefinition expressionDef = null;
		String expressionName = (String) expressionNameCB.getSelectedItem();
		if(expressionName != null) {
			if(isValidExpression()) {
				if(expressionName.length() > 0) {
					expressionDef = expressionCache.get(expressionName);
					expressionDef.resetExpressionElements();
				} else if(expressionName.length() == 0) {
					expressionDef = new ExpressionDefinition();
				}
				updateExpressionDefinition(expressionDef);
				expressionCache.put(expressionDef.getName(), expressionDef);
				populateExpressionNames();
				previousExpressionName = expressionDef.getName();
				expressionNameCB.setSelectedItem(expressionDef.getName());
			} else {
				if(symbolsTable != null && symbolsTable.getRowCount() == 0) {
					JOptionPane.showMessageDialog(this, "Expression defined is not valid.");
				} else {
					JOptionPane.showMessageDialog(this, "Expression defined is not valid.\nAn Expression should end with Operand as symbol.");
				}
				b_addOrUpdateExpression.requestFocus();
				return;
			}
		} else {
			expressionDef = new ExpressionDefinition();
			updateExpressionDefinition(expressionDef);
			expressionCache.put(expressionDef.getName(), expressionDef);
			populateExpressionNames();
			previousExpressionName = expressionDef.getName();
			expressionNameCB.setSelectedItem(expressionDef.getName());
		}
	}

	private void updateExpressionDefinition(ExpressionDefinition expressionDef) {
		if(symbolsTable != null && symbolsTable.getRowCount() > 0) {
			int noOfRows = symbolsTable.getRowCount();
			for(int index = 0; index < noOfRows; index ++) {
				expressionDef.addExpressionElement(
					(ExpressionElementsDef) symbolsTable.getDefaultModel().getValueAt(index, 1));
			}
		}
	}

	private void resetExpression() {
		int response = JOptionPane.showConfirmDialog(null,
	    	"This operation may result in losing unsaved Expression definition.\nDo you want to continue?", "Confirm",
		    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(response == JOptionPane.YES_OPTION) {
			expressionNameCB.setSelectedIndex(-1);
			resetOperandObjects();
			resetOperatorObjects();
			previousExpressionName = "";
			symbolsTable.getDefaultModel().getDataVector().removeAllElements();
			symbolsTable.updateUI();
		}
	}

	private void actOnExpressionNameChange(String expressionName) {
	    int response = -1;
	    if(expressionName != null) {
	    	if(! previousExpressionName.equalsIgnoreCase(expressionName)) {
	    		response = JOptionPane.showConfirmDialog(null,
    		    	"This operation may result in losing unsaved Expression definition.\nDo you want to continue?", "Confirm",
    			    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    	    try {
	    			if(response == JOptionPane.YES_OPTION) {
	    				resetOperandObjects();
	    				resetOperatorObjects();
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
	    			} else {
	    				expressionNameCB.setSelectedItem(previousExpressionName);
	    			}
	    	    } catch(Exception ex) {
	    	    	ex.printStackTrace();
	    	    	displayMessage(ex.getMessage());
	    	    }
	    	}
	    }
	}

	class LoCFrameWindowAdapter extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			((PopLoCFrame) e.getSource()).dispose();
		}

	}

	class ButtonsPanel_Buttons_Action_Listener implements ActionListener {

		PopLoCFrame locFrame = null;

		public ButtonsPanel_Buttons_Action_Listener(PopLoCFrame locFrame) {
			this.locFrame = locFrame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			try {
				if(buttonClicked.equals("B_Done")) {
					Object object = getDefinition();
					if(object != null) {
						if(object instanceof ActionDefinition) {
							ActionDefinition actionDef = (ActionDefinition) object;
							if(actionDef != null) {
								addActionToTree(locFrame, actionDef);
							}
						} else if(object instanceof LineOfCodeDefinition) {
							LineOfCodeDefinition locDef = (LineOfCodeDefinition) object;
							if(locDef != null) {
								addLineOfCodeToTree(locFrame, locDef);
							}
						}
					}
				} else if(buttonClicked.equals("B_Cancel")) {
					cancelLineOfCode(locFrame);
				}
			} catch(Exception ex) {
				ex.printStackTrace();
				displayMessage(ex.getMessage());
			}
		}

	}

	class LHS_RHS_Type_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			PopComboBox sourceCombo = (PopComboBox) e.getSource();
			String selectedItem = (String) sourceCombo.getSelectedItem();
			if(sourceCombo.getName().equals("CB_LHSType")) {
				rhsTabPane.setSelectedIndex(0);
				if(selectedItem.length() > 0) {
					rhsTypeCB.setEnabled(true);
					if(selectedItem.equals("Declare Instance Variable")
						|| selectedItem.equals("Declare Method Variable")) {
						populateRHSTypeForVariableDeclaration();
						enableLHSDefinition(true);
					} else if(selectedItem.equals("Declare Instance Variable and Initialize")
						|| selectedItem.equals("Declare Method Variable and Initialize")) {
						populateRHSTypeForVariableDeclarationAndInitialization();
						enableLHSDefinition(true);
					} else if(selectedItem.equals("Assign Value To An Existing Variable")) {
						populateRHSTypeToAssignValueToAnExistingVariable();
						enableLHSDefinition(true);
					} else if(selectedItem.equals("No LHS")) {
						populateRHSTypeForNoLHS();
						enableLHSDefinition(false);
					}
				} else {
					populateRHSType();
					rhsTypeCB.setEnabled(false);
					enableLHSDefinition(false);
					enableRHSDefinition(false);
				}
			} else if(sourceCombo.getName().equals("CB_RHSType")) {
				if(selectedItem != null) {
					performUIUpdateForRHSTypeChange(selectedItem);
				}
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
						if(rhsTypeCB.getSelectedItem() != null) {
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

	}

	class Expression_Name_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			PopComboBox popCBX = (PopComboBox) e.getSource();
			actOnExpressionNameChange((String) popCBX.getSelectedItem());
		}

	}

	class Ellipsis_Name_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String source = ((PopButton) e.getSource()).getName();
			if(source.equals("B_Ellipsis")) {
				ellipsisWarning();
			}
		}

	}

	class Operand_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("Operand_B_Add")) {
				addOperand();
			} else if(buttonClicked.equals("Operand_B_Update")) {
				updateOperand();
			} else if(buttonClicked.equals("Operand_B_Delete")) {
				deleteOperand();
			} else if(buttonClicked.equals("Operand_B_Reset")) {
				clearOperand();
			}
		}

	}

	class Expression_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg) {
			String buttonName = ((PopButton) arg.getSource()).getName();
			if(buttonName.equals("B_AddOrUpdateExpression")) {
				addOrUpdateExpression();
			} else if(buttonName.equals("B_ResetExpression")) {
				resetExpression();
			}
		}

	}

	class Operator_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("Operator_B_Add")) {
				addOperator();
			} else if(buttonClicked.equals("Operator_B_Update")) {
				updateOperator();
			} else if(buttonClicked.equals("Operator_B_Delete")) {
				deleteOperator();
			} else if(buttonClicked.equals("Operator_B_Reset")) {
				clearOperator();
			}
		}

	}

	class Symbol_Table_Mouse_Adapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				setEnableOperandButtons(false);
				setEnableOperatorButtons(false);
				JTable conTable = (JTable) e.getSource();
				int rowCount = conTable.getRowCount();
				symTableRowNumber = conTable.getSelectedRow();
				if(conTable.getModel().getValueAt(symTableRowNumber, 1) instanceof OperandDefinition) {
					OperandDefinition operandDef = (OperandDefinition) conTable.getModel().getValueAt(symTableRowNumber, 1);
					b_updateOperand.setEnabled(true);
					if((rowCount - 1) == symTableRowNumber) {
						b_removeOperand.setEnabled(true);
					}
					b_addOperand.setEnabled(false);
					if(operandDef.getActionDef() != null) {
						try {
							operandTF.setText(objectFactory.getActionFromActionDef(
								operandDef.getActionDef(), getWebPageName()).toStringBuffer().toString());
							setExpActionDef(operandDef.getActionDef());
							setActionOrExpression("Action");
						} catch(Exception ex) {
							ex.printStackTrace();
							displayMessage(ex.getMessage());
						}
					}
					if(operandDef.getStringDef() != null) {
						operandTF.setEnabled(true);
						operandTF.setText(operandDef.getStringDef().getName());
					} else {
						operandTF.setEnabled(false);
					}
					if(operandDef.getExpressionDef() != null) {
						try {
							operandTF.setText(objectFactory.getExpressionFromExpressionDefinition(
								operandDef.getExpressionDef(), getWebPageName()).toStringBuffer().toString());
						} catch(Exception ex) {
							ex.printStackTrace();
							displayMessage(ex.getMessage());
						}
						setExpressionName(operandDef.getExpressionDef().getName());
						setActionOrExpression("Expression");
					}
					b_addOperator.setEnabled(true);
					operatorCB.setSelectedIndex(-1);
				} else if(conTable.getModel().getValueAt(symTableRowNumber, 1) instanceof OperatorDefinition) {
					b_updateOperator.setEnabled(true);
					if((rowCount - 1) == symTableRowNumber) {
						b_removeOperator.setEnabled(true);
					}
					b_addOperator.setEnabled(false);
					b_addOperand.setEnabled(true);
					operatorCB.setSelectedItem(((OperatorDefinition)
						conTable.getModel().getValueAt(symTableRowNumber, 1)).getOperator());
					operandTF.setEnabled(true);
					operandTF.setText("");
				}
			}
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

	class LoCWindowAdapter extends WindowAdapter {

		PopLoCFrame locFrame = null;

		public LoCWindowAdapter(PopLoCFrame locFrame) {
			this.locFrame = locFrame;
		}

		@Override
		public void windowClosing(WindowEvent e) {
	        cancelLineOfCode(locFrame);
		}

	}
	
	class TagType_ActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String selectedItem = (String) ((PopComboBox) e.getSource()).getSelectedItem();
			if(selectedItem != null && selectedItem.length() > 0) {
				populateElementFieldType(selectedItem);
			} else {
				fieldTypeCB.removeAllItems();
			}
			
		}
		
	}

}
