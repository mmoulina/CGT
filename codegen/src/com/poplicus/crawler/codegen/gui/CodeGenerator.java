package com.poplicus.crawler.codegen.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.poplicus.crawler.codegen.ClassDefinitions;
import com.poplicus.crawler.codegen.Condition;
import com.poplicus.crawler.codegen.Constants;
import com.poplicus.crawler.codegen.FileGenerationException;
import com.poplicus.crawler.codegen.Datum.DatumIdentifier;
import com.poplicus.crawler.codegen.definitions.ActionDefinition;
import com.poplicus.crawler.codegen.definitions.BrowserDefinition;
import com.poplicus.crawler.codegen.definitions.ConditionDefinition;
import com.poplicus.crawler.codegen.definitions.Conditions;
import com.poplicus.crawler.codegen.definitions.CrawlerDefinition;
import com.poplicus.crawler.codegen.definitions.DataGroupDefinition;
import com.poplicus.crawler.codegen.definitions.DataSetDefinition;
import com.poplicus.crawler.codegen.definitions.DatumDefinition;
import com.poplicus.crawler.codegen.definitions.ElseDefinition;
import com.poplicus.crawler.codegen.definitions.ElseIfDefinition;
import com.poplicus.crawler.codegen.definitions.IfDefinition;
import com.poplicus.crawler.codegen.definitions.IfElseElementsDef;
import com.poplicus.crawler.codegen.definitions.LineOfCodeDefinition;
import com.poplicus.crawler.codegen.definitions.MainIfDefinition;
import com.poplicus.crawler.codegen.definitions.MainIfElementsDef;
import com.poplicus.crawler.codegen.definitions.WebPageDefinition;
import com.poplicus.crawler.codegen.definitions.BrowserDefinition.WhichBrowser;
import com.poplicus.crawler.codegen.definitions.WebPages;
import com.poplicus.crawler.codegen.factory.ConditionCache;
import com.poplicus.crawler.codegen.factory.CrawlerCache;
import com.poplicus.crawler.codegen.factory.CrawlerCodeGenFactory;
import com.poplicus.crawler.codegen.factory.CrawlerDefXMLReader;
import com.poplicus.crawler.codegen.factory.DatumCache;
import com.poplicus.crawler.codegen.factory.ObjectFactory;
import com.poplicus.crawler.codegen.gui.components.FontManager;
import com.poplicus.crawler.codegen.gui.components.PopButton;
import com.poplicus.crawler.codegen.gui.components.PopCheckBox;
import com.poplicus.crawler.codegen.gui.components.PopComboBox;
import com.poplicus.crawler.codegen.gui.components.PopLabel;
import com.poplicus.crawler.codegen.gui.components.PopTabbedPane;
import com.poplicus.crawler.codegen.gui.components.PopTable;
import com.poplicus.crawler.codegen.gui.components.PopTextField;
import com.poplicus.crawler.codegen.gui.components.PopTitledBorder;
import com.poplicus.crawler.codegen.gui.components.PopTree;
import com.poplicus.crawler.codegen.gui.components.PopTreeNode;
import com.poplicus.crawler.codegen.gui.components.PopTreeNode.PopTreeNodeType;
import com.poplicus.crawler.codegen.utilities.CodeGenConfig;
import com.poplicus.crawler.codegen.utilities.CodeGenUtil;
import com.poplicus.crawler.codegen.utilities.config.CodeGenConfigCache;
import com.poplicus.crawler.codegen.utilities.config.CodeGenDefConfig;
import com.poplicus.crawler.codegen.utilities.config.ColumnDefConfig;
import com.poplicus.crawler.codegen.utilities.config.TableDefConfig;


public class CodeGenerator extends JFrame {

	private static final long serialVersionUID = -2745292040757133324L;

	private static FontManager fontManager = FontManager.getInstance();
	private static CodeGenUtil codeGenUtil = CodeGenUtil.getInstance();
	private static ConditionCache conditionCache = ConditionCache.getInstance();
	private static CrawlerCache crawlerCache = CrawlerCache.getInstance();
	private static CrawlerCodeGenFactory codeGenFactory = CrawlerCodeGenFactory.getInstance();
	private static DatumCache datumCache = DatumCache.getInstance();
	private static ObjectFactory objectFactory = ObjectFactory.getInstance();
	private static CodeGenConfigCache configCache = CodeGenConfigCache.getInstance();

	private PopTabbedPane mainTabPane = null;
	private PopTabbedPane webPageConditionTabs = null;
	private PopTabbedPane resultSetTabs = null;

	private PopTable [ ] dataTables = null; 

	private PopTextField dGroupStartTagTF = null;
	private PopTextField dGroupEndTagTF = null;

	private int selectedTabOfMainTabPane = -1;

	private String uniqueNameOfWebPage = null;
	private String browserTypeOfWebPage = null;

	private JPanel codePanel = null;
	private JScrollPane popTreeScrollPane = null;
	private PopTree tree = null;

	private PopTextField crawlerNameTF = null;
	private PopTextField clientNameTF = null;
	private PopTextField siteNameTF = null;
	private PopTextField sourceIDTF = null;
	private PopTextField sourceNameTF = null;
	private PopCheckBox handlePopupCKB = null;

	private boolean sameWebPage = false;

	private PopButton b_Next = null;
	private PopButton b_Cancel = null;

	private PopButton b_Previous_Config_Step_2 = null;
	private PopButton b_Next_Config_Step_2 = null;
	private PopButton b_Cancel_Config_Step_2 = null;

	private PopButton b_Previous_Config_Step_3 = null;
	private PopButton b_Next_Config_Step_3 = null;
	private PopButton b_Cancel_Config_Step_3 = null;

	private PopButton bReset = null;
	private PopButton bLoad = null;
	private PopButton bSave = null;
	private PopButton bGenerateCrawler = null;

	private PopComboBox cBoxLeft_PBC = null;
	private PopComboBox cBoxOperator_PBC = null;
	private PopComboBox cBoxRight_PBC = null;

	private ButtonGroup rightConditionBGroup_PBC = null;
	private PopCheckBox rightExistingCondition_PBC = null;
	private PopCheckBox rightNewCondition_PBC = null;

	private ButtonGroup leftConditionBGroup_PBC = null;
	private PopCheckBox leftExistingCondition_PBC = null;
	private PopCheckBox leftNewCondition_PBC = null;

	private String con_LO_Selected_CB_PBC = null;
	private String con_RO_Selected_CB_PBC = null;

	private PopTextField leftOperandTF_PBC = null;
	private PopTextField rightOperandTF_PBC = null;

	private PopCheckBox superConditionCBX_PBC = null;

	private PopTable conditionsTable_PBC = null;

	private PopButton bCreate_PBC = null;
	private PopButton bUpdate_PBC = null;
	private PopButton bDelete_PBC = null;
	private PopButton bClear_PBC = null;

	private int conTableRowNumber_PBC = -1;
	private String conditionName_PBC = null;

	private PopCheckBox invokePBC_CKB = null;
	private PopCheckBox securedWebPageCKB_PBC = null;
	private PopCheckBox downloadImagesCKB_PBC = null;
	private PopCheckBox downloadScriptsCKB_PBC = null;

	private PopComboBox cBoxLeft_SBC = null;
	private PopComboBox cBoxOperator_SBC = null;
	private PopComboBox cBoxRight_SBC = null;

	private ButtonGroup rightConditionBGroup_SBC = null;
	private PopCheckBox rightExistingCondition_SBC = null;
	private PopCheckBox rightNewCondition_SBC = null;

	private ButtonGroup leftConditionBGroup_SBC = null;
	private PopCheckBox leftExistingCondition_SBC = null;
	private PopCheckBox leftNewCondition_SBC = null;

	private String con_LO_Selected_CB_SBC = null;
	private String con_RO_Selected_CB_SBC = null;

	private PopTextField leftOperandTF_SBC = null;
	private PopTextField rightOperandTF_SBC = null;

	private PopCheckBox superConditionCBX_SBC = null;

	private PopTable conditionsTable_SBC = null;

	private PopButton bCreate_SBC = null;
	private PopButton bUpdate_SBC = null;
	private PopButton bDelete_SBC = null;
	private PopButton bClear_SBC = null;

	private int conTableRowNumber_SBC = -1;
	private String conditionName_SBC = null;

	private PopCheckBox invokeSBC_CKB = null;
	private PopCheckBox securedWebPageCKB_SBC = null;
	private PopCheckBox downloadImagesCKB_SBC = null;
	private PopCheckBox downloadScriptsCKB_SBC = null;

	private PopComboBox cBoxLeft_TBC = null;
	private PopComboBox cBoxOperator_TBC = null;
	private PopComboBox cBoxRight_TBC = null;

	private ButtonGroup rightConditionBGroup_TBC = null;
	private PopCheckBox rightExistingCondition_TBC = null;
	private PopCheckBox rightNewCondition_TBC = null;

	private ButtonGroup leftConditionBGroup_TBC = null;
	private PopCheckBox leftExistingCondition_TBC = null;
	private PopCheckBox leftNewCondition_TBC = null;

	private String con_LO_Selected_CB_TBC = null;
	private String con_RO_Selected_CB_TBC = null;

	private PopTextField leftOperandTF_TBC = null;
	private PopTextField rightOperandTF_TBC = null;

	private PopCheckBox superConditionCBX_TBC = null;

	private PopTable conditionsTable_TBC = null;

	private PopButton bCreate_TBC = null;
	private PopButton bUpdate_TBC = null;
	private PopButton bDelete_TBC = null;
	private PopButton bClear_TBC = null;

	private int conTableRowNumber_TBC = -1;
	private String conditionName_TBC = null;

	private PopCheckBox invokeTBC_CKB = null;
	private PopCheckBox securedWebPageCKB_TBC = null;
	private PopCheckBox downloadImagesCKB_TBC = null;
	private PopCheckBox downloadScriptsCKB_TBC = null;

	private PopComboBox cBoxLeft_WPQC = null;
	private PopComboBox cBoxOperator_WPQC = null;
	private PopComboBox cBoxRight_WPQC = null;

	private ButtonGroup rightConditionBGroup_WPQC = null;
	private PopCheckBox rightExistingCondition_WPQC = null;
	private PopCheckBox rightNewCondition_WPQC = null;

	private ButtonGroup leftConditionBGroup_WPQC = null;
	private PopCheckBox leftExistingCondition_WPQC = null;
	private PopCheckBox leftNewCondition_WPQC = null;

	private String con_LO_Selected_CB_WPQC = null;
	private String con_RO_Selected_CB_WPQC = null;

	private PopTextField leftOperandTF_WPQC = null;
	private PopTextField rightOperandTF_WPQC = null;

	private PopCheckBox superConditionCBX_WPQC = null;

	private PopTable conditionsTable_WPQC = null;

	private PopButton bCreate_WPQC = null;
	private PopButton bUpdate_WPQC = null;
	private PopButton bDelete_WPQC = null;
	private PopButton bClear_WPQC = null;

	private int conTableRowNumber_WPQC = -1;
	private String conditionName_WPQC = null;

	private PopTable webPagesTable = null;
	private PopButton bEditWebpage = null;
	private int editWebPageRowNumber = -1;
	private boolean editWebFlow = false;

	private PopTextField wpNameTF = null;
	private PopComboBox wpTypeCMB = null;
	private PopTextField wpNavOrderTF = null;
	private PopComboBox wpBrowserCMB = null;
	private PopTextField wpUrlTF = null;
	private PopCheckBox wpExecuteOnceCBX = null;

	private PopComboBox cBoxLeft_CBQC = null;
	private PopComboBox cBoxOperator_CBQC = null;
	private PopComboBox cBoxRight_CBQC = null;

	private ButtonGroup rightConditionBGroup_CBQC = null;
	private PopCheckBox rightExistingCondition_CBQC = null;
	private PopCheckBox rightNewCondition_CBQC = null;

	private ButtonGroup leftConditionBGroup_CBQC = null;
	private PopCheckBox leftExistingCondition_CBQC = null;
	private PopCheckBox leftNewCondition_CBQC = null;

	private String con_LO_Selected_CB_CBQC = null;
	private String con_RO_Selected_CB_CBQC = null;

	private PopTextField leftOperandTF_CBQC = null;
	private PopTextField rightOperandTF_CBQC = null;

	private PopCheckBox superConditionCBX_CBQC = null;

	private PopTable conditionsTable_CBQC = null;

	private PopButton bCreate_CBQC = null;
	private PopButton bUpdate_CBQC = null;
	private PopButton bDelete_CBQC = null;
	private PopButton bClear_CBQC = null;

	private int conTableRowNumber_CBQC = -1;
	private String conditionName_CBQC = null;

	private Object[] resultSetColumnNames = {"Unique ID", "Column Name", "Data Type",
											 "Variable Name", "HTML Source", "Start Tag",
											 "End Tag", "Entire String", "After Tag"};
	private Object[][] resultSetRowData = new Object[0][0];

	private JPanel buttonsPanell = null;

	private JFileChooser fileChooser = null;

	public CodeGenerator() {
		loadConfig();
		this.setLayout(new GridBagLayout());
		this.setScreenSize();
		this.setExtendedState(MAXIMIZED_BOTH);
		this.addWindowListener(new CodeGenWindowAdapter());
		this.setTitle("Poplicus - Crawler Code Generator - V1.1");

		javax.swing.UIManager.put("OptionPane.messageFont", fontManager.getOptionPaneMessageFontPlain());
		javax.swing.UIManager.put("OptionPane.buttonFont", fontManager.getOptionPaneButtonFontPlain());
		javax.swing.UIManager.put("Button.font", fontManager.getOptionPaneButtonFontPlain());
		javax.swing.UIManager.put("Label.font", fontManager.getOptionPaneButtonFontPlain());
		javax.swing.UIManager.put("ComboBox.font", fontManager.getOptionPaneButtonFontPlain());
		javax.swing.UIManager.put("PopupMenu.font", fontManager.getOptionPaneButtonFontPlain());
		javax.swing.UIManager.put("MenuItem.font", fontManager.getOptionPaneButtonFontPlain());
		javax.swing.UIManager.put("Menu.font", fontManager.getOptionPaneButtonFontPlain());
		javax.swing.UIManager.put("RadioButtonMenuItem.font", fontManager.getOptionPaneButtonFontPlain());
		javax.swing.UIManager.put("List.font", fontManager.getOptionPaneButtonFontPlain());

		Toolkit.getDefaultToolkit( ).setDynamicLayout(true);

		this.add(getInfoPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getMainTabbedPane(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	private void loadConfig() {
		try {
			int read = 0;
			byte[] bytes = new byte[1024];
			if(! validateConfigFileExistence()) {
				File configFile = new File("../CodeGen/config/codegentool_config.xml");
				if(configFile.exists()) {
					FileInputStream fiStream = new FileInputStream(configFile);
					FileOutputStream foStream = new FileOutputStream(new File("C:\\Poplicus\\CodeGen\\Config\\codegentool_config.xml"));
					while((read = fiStream.read(bytes)) != -1) {
						foStream.write(bytes, 0, read);
					}
					fiStream.close();
					foStream.flush();
					foStream.close();
				} else {
					JOptionPane.showMessageDialog(this, "CodeGen configuration file is missing and the tool is not able to create that file.\n" +
						"Going to use hardcoded values for entities referred in configuration file.");
				}
			}
			CodeGenConfig ccConfig = new CodeGenConfig();
			CodeGenDefConfig config = ccConfig.getCodeGenConfiguration("C:\\Poplicus\\CodeGen\\Config\\codegentool_config.xml");
			configCache.setConfigurations(config);
			ClassDefinitions.populateClassDefinitions(configCache.getConfigurations().getFunctionDefinitions());
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getStackTrace());
		}
	}
	
	private boolean validateConfigFileExistence() throws FileGenerationException {
		File configFile = null;	
		InputStream iStream = null; 
		FileOutputStream foStream = null;
		int read = 0;
		byte[] bytes = new byte[1024];
		boolean fileExists = true;
		try {
			if(! validateConfigFile()) {
				configFile = new File("C:\\Poplicus\\CodeGen\\Config\\codegentool_config.xml");
				iStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/codegentool_config.xml");
				if(iStream != null) {
					foStream = new FileOutputStream(configFile);
					while ((read = iStream.read(bytes)) != -1) {
						foStream.write(bytes, 0, read);
					}
					iStream.close();
					foStream.flush();
					foStream.close();
				} else {
					fileExists = false;
				}
			}
		} catch(FileGenerationException exFileGen) {
			fileExists = false;
			throw exFileGen;
		} catch(Exception ex) {
			fileExists = false;
			throw new FileGenerationException(ex.getMessage(), ex);
		}	
		return fileExists;
	}
	
	public boolean validateConfigFile() throws FileGenerationException {
		boolean valid = true;
		File file = null;
		try {
			file = new File("C:\\Poplicus\\CodeGen\\Config");
			if(file.exists()) {
				file = new File("C:\\Poplicus\\CodeGen\\Config\\codegentool_config.xml");
				if(file.exists()) {
					valid = true;
				} else {
					valid = false;
				}
			} else {
				try {
					file.mkdirs();
				} catch(Exception ex) {
					valid = false;
					throw new FileGenerationException("Could not create folder structure C:\\Poplicus\\CodeGen\\Config.");
				}
				valid = false;
			}
		} catch(Exception ex) {
			valid = false;
			throw new FileGenerationException("Exception occcurred while writing source file to C:\\Poplicus\\CodeGen\\Config.");
		}
		return valid;
	}

	private void setScreenSize() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((new Double(dimension.getWidth())).intValue(), (new Double(dimension.getHeight() - 30)).intValue());
		Rectangle rectangle = new Rectangle(0, 0, (new Double(dimension.getWidth())).intValue(), (new Double(dimension.getHeight() - 30)).intValue());
		this.setMaximizedBounds(rectangle);
	}

	private void populateOperator(JComboBox comboBox) {
		comboBox.addItem("");
		List<String> operators = configCache.getConfigurations().getConditionOperators();
		for(String operator : operators) {
			comboBox.addItem(operator);
		}
	}

	private void populateConditions() {
		if(cBoxLeft_PBC != null) {
			populateConditions(cBoxLeft_PBC);
		}
		if(cBoxRight_PBC != null) {
			populateConditions(cBoxRight_PBC);
		}
		if(cBoxLeft_SBC != null) {
			populateConditions(cBoxLeft_SBC);
		}
		if(cBoxRight_SBC != null) {
			populateConditions(cBoxRight_SBC);
		}
		if(cBoxLeft_TBC != null) {
			populateConditions(cBoxLeft_TBC);
		}
		if(cBoxRight_TBC != null) {
			populateConditions(cBoxRight_TBC);
		}
		if(cBoxLeft_WPQC != null) {
			populateConditions(cBoxLeft_WPQC);
		}
		if(cBoxRight_WPQC != null) {
			populateConditions(cBoxRight_WPQC);
		}
		if(cBoxLeft_CBQC != null) {
			populateConditions(cBoxLeft_CBQC);
		}
		if(cBoxRight_CBQC != null) {
			populateConditions(cBoxRight_CBQC);
		}
	}

	private void populateConditions(JComboBox comboBox) {
		List<String> conditions = conditionCache.getConditionNames();
		comboBox.removeAllItems();
		comboBox.addItem("");
		for(String con : conditions) {
			comboBox.addItem(con);
		}
	}

	private JPanel getMainButtonPanel() {
		JPanel mbPanel = new JPanel();
		mbPanel.setLayout(new GridBagLayout());

		JPanel invisiblePanel = new JPanel();

		bReset = new PopButton("Reset");
		bLoad = new PopButton("Load Definition");
		bSave = new PopButton("Save");
		//bSave.setEnabled(false); //should be enabled once XML saving is kicked off

		//bGenerateCrawler = new PopButton("Save & Generate");
		bGenerateCrawler = new PopButton("Generate Code"); // should be reverted back to Save & Generate once XML saving is kicked off

		mbPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bReset.setName("B_Reset");
		bReset.addActionListener(new Main_Button_Action_Listener());
		mbPanel.add(bReset, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bLoad.setName("B_Load");
		bLoad.addActionListener(new Main_Button_Action_Listener());
		mbPanel.add(bLoad, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bSave.setName("B_Save");
		bSave.addActionListener(new Main_Button_Action_Listener());
		mbPanel.add(bSave, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bGenerateCrawler.setName("B_Save_And_Generate");
		bGenerateCrawler.addActionListener(new Main_Button_Action_Listener());
		mbPanel.add(bGenerateCrawler, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		return mbPanel;
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
			} else if(condition.getLeftSideValue() instanceof String) {
				conDef.setLshOperand((String) condition.getLeftSideValue());
			}
			if(condition.getRightSideValue() instanceof Condition) {
				conDef.setRhsOperand(((Condition) condition.getRightSideValue()).getName());
			} else if(condition.getRightSideValue() instanceof String) {
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

	private void saveAndGenerateCrawler() {
		try {
			if(validateCodeGen()) {
				String sourceID = sourceIDTF.getText();
				CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
				if(crawlerDef != null) {
					if(crawlerDef.getWebPages() != null && crawlerDef.getWebPages().size() > 0) {
						String savedFileName = codeGenFactory.saveCrawlerDefinition(crawlerDef);
						String generatedClassFileName = codeGenFactory.generateCrawlerCode(crawlerDef);
						JOptionPane.showMessageDialog(this, "Crawler code generated successfully. Refer to file \n" + generatedClassFileName + ".\n" +
							"Saved definition of this crawler. For definition refer file \n" + savedFileName + ".");
						bGenerateCrawler.requestFocus();
					} else {
						JOptionPane.showMessageDialog(this, "Atleast one Web Flow should be defined to generate Crawler code.");
						bGenerateCrawler.requestFocus();
					}
				} else {
					JOptionPane.showMessageDialog(this, "Atleast one Web Flow should be defined to generate Crawler code.");
					bGenerateCrawler.requestFocus();
				}
			} else {
				return;
			}
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
			bGenerateCrawler.requestFocus();
		}
	}

	private void resetCrawlerConfig() {
		String sourceID = sourceIDTF.getText();
		crawlerCache.remove(sourceID);
		sourceIDTF.setEditable(true);
		selectedTabOfMainTabPane = 0;
		clearCrawlerConfigurations();
		clearCondition_PBC();
		clearCondition_SBC();
		clearCondition_TBC();
		clearConditionTables();
		clearOtherConfigurations();
		clearWebFlowsSummary();
		resetConfigWebFlow();
		mainTabPane.setEnabledAt(0, true);
		mainTabPane.setEnabledAt(1, true);
		mainTabPane.setEnabledAt(2, true);
		mainTabPane.setEnabledAt(3, true);
		mainTabPane.setEnabledAt(4, false);
		mainTabPane.setEnabledAt(5, false);
		mainTabPane.setEnabledAt(6, false);
		mainTabPane.setSelectedIndex(0);
		webPagesTable.clearSelection();
	}

	private void clearCrawlerConfigurations() {
		crawlerNameTF.setText("");
		clientNameTF.setText("");
		siteNameTF.setText("");
		sourceIDTF.setText("");
		sourceNameTF.setText("");
		handlePopupCKB.setSelected(false);
	}

	private void clearConditionTables() {
		((DefaultTableModel) conditionsTable_PBC.getModel()).getDataVector().removeAllElements();
		conditionsTable_PBC.updateUI();

		((DefaultTableModel) conditionsTable_SBC.getModel()).getDataVector().removeAllElements();
		conditionsTable_SBC.updateUI();

		((DefaultTableModel) conditionsTable_TBC.getModel()).getDataVector().removeAllElements();
		conditionsTable_TBC.updateUI();
	}

	private void clearOtherConfigurations() {
		invokePBC_CKB.setSelected(false);
		securedWebPageCKB_PBC.setSelected(false);
		downloadImagesCKB_PBC.setSelected(false);
		downloadScriptsCKB_PBC.setSelected(false);

		invokeSBC_CKB.setSelected(false);
		securedWebPageCKB_SBC.setSelected(false);
		downloadImagesCKB_SBC.setSelected(false);
		downloadScriptsCKB_SBC.setSelected(false);

		invokeTBC_CKB.setSelected(false);
		securedWebPageCKB_TBC.setSelected(false);
		downloadImagesCKB_TBC.setSelected(false);
		downloadScriptsCKB_TBC.setSelected(false);
	}

	private void clearWebFlowsSummary() {
		((DefaultTableModel) webPagesTable.getModel()).getDataVector().removeAllElements();
		webPagesTable.updateUI();
	}

	private void saveCrawler() throws Exception {
		if(validateCodeGen()) {
			String sourceID = sourceIDTF.getText();
			CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
			if(crawlerDef != null) {
				if(crawlerDef.getWebPages() != null && crawlerDef.getWebPages().size() > 0) {
					String generatedClassFileName = codeGenFactory.saveCrawlerDefinition(crawlerDef);
					JOptionPane.showMessageDialog(this, "Crawler definition saved successfully. Refer to file \n" + generatedClassFileName + ".");
					bGenerateCrawler.requestFocus();
				} else {
					JOptionPane.showMessageDialog(this, "Atleast one Web Flow should be defined to save Crawler definition.");
					bGenerateCrawler.requestFocus();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Atleast one Web Flow should be defined to save Crawler definition.");
				bGenerateCrawler.requestFocus();
			}
		} else {
			return;
		}
	}

	private boolean validateCodeGen() {
		CrawlerDefinition crawlerDef = null;
		String sourceID = sourceIDTF.getText();
		if((sourceID != null) && (sourceID.trim().length() > 0)) {
			crawlerDef = crawlerCache.get(sourceID);
			if(crawlerDef == null) {
				crawlerDef = new CrawlerDefinition();
				codeGenUtil.populateDefaultConstructors(crawlerDef);
				crawlerDef.setSourceID(sourceID);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Source ID is a mandatory field.");
			sourceIDTF.requestFocus();
			return false;
		}

		if((crawlerNameTF.getText() != null) && (crawlerNameTF.getText().trim().length() > 0)) {
			crawlerDef.setName(crawlerNameTF.getText());
		} else {
			JOptionPane.showMessageDialog(this, "Crawler Name is a mandatory field.");
			crawlerNameTF.requestFocus();
			return false;
		}

		if((clientNameTF.getText() != null) && (clientNameTF .getText().trim().length() > 0)) {
			crawlerDef.setClientName(clientNameTF .getText());
		} else {
			JOptionPane.showMessageDialog(this, "Client Name is a mandatory field.");
			clientNameTF.requestFocus();
			return false;
		}

		if((siteNameTF.getText() != null) && (siteNameTF.getText().trim().length() > 0)) {
			crawlerDef.setSiteName(siteNameTF.getText());
		} else {
			JOptionPane.showMessageDialog(this, "Site Name is a mandatory field.");
			siteNameTF.requestFocus();
			return false;
		}

		if((sourceNameTF.getText() != null) && (sourceNameTF.getText().trim().length() > 0)) {
			crawlerDef.setSourceName(sourceNameTF.getText());
		} else {
			JOptionPane.showMessageDialog(this, "Source Name is a mandatory field.");
			sourceNameTF.requestFocus();
			return false;
		}
		crawlerDef.setHandlePopup(handlePopupCKB.isSelected());

		crawlerCache.put(sourceID, crawlerDef);

		updatePrimaryBrowserInfo();
		updateSecondaryBrowserInfo();
		updateTertiaryBrowserInfo();

		sourceIDTF.setEditable(false);

		return true;
	}

	private void loadCrawlerDefinition() throws Exception {
		String sourceID = sourceIDTF.getText();
		if((sourceID != null) && (sourceID.trim().length() > 0)) {
			JOptionPane.showMessageDialog(this, "Reset existing Crawler Definition to load another Crawler Definition.");
		} else {
			File definitionFolder = new File("C:\\Poplicus\\CodeGen\\Definition");
			if(definitionFolder.isDirectory()) {
				fileChooser = new JFileChooser(definitionFolder);
			} else {
				fileChooser = new JFileChooser();
			}
			int value = fileChooser.showOpenDialog(this);
			if(value == JFileChooser.APPROVE_OPTION) {
				CrawlerDefinition crawlerDef =
					(new CrawlerDefXMLReader()).getCrawlerDefinition(fileChooser.getSelectedFile().getCanonicalPath());
				crawlerCache.put(crawlerDef.getSourceID(), crawlerDef);
				populateCrawlerDefinition(crawlerDef);
			}
		}
	}

	private void populateCrawlerDefinition(CrawlerDefinition crawlerDef) {
		populateMainDefinition(crawlerDef);
		populatePrimaryBrowserDefinition(crawlerDef);
		populateSecondaryBrowserDefinition(crawlerDef);
		populateTertiaryBrowserDefinition(crawlerDef);
		populateWebPagesDefinition(crawlerDef);
	}

	private void populateMainDefinition(CrawlerDefinition crawlerDef) {
		crawlerNameTF.setText(crawlerDef.getName());
		clientNameTF.setText(crawlerDef.getClientName());
		siteNameTF.setText(crawlerDef.getSiteName());
		sourceIDTF.setText(crawlerDef.getSourceID());
		sourceIDTF.setEditable(false);
		sourceNameTF.setText(crawlerDef.getSourceName());
		handlePopupCKB.setSelected(crawlerDef.isHandlePopup());
	}

	private void populatePrimaryBrowserDefinition(CrawlerDefinition crawlerDef) {
		Conditions primaryConditions = crawlerDef.getPrimaryBrowserDefinition().getConditions();
		for(ConditionDefinition conDef : primaryConditions) {
			conditionsTable_PBC.addRow(new Object[]{conDef.getName(),
				conDef.getLhsOperand(), conDef.getOperator(), conDef.getRhsOperand(), conDef.isSuperCondition()});
		}
		invokePBC_CKB.setSelected(crawlerDef.isInvokeBasePBC());
		securedWebPageCKB_PBC.setSelected(crawlerDef.getPrimaryBrowserDefinition().isSecuredWebPage());
		downloadImagesCKB_PBC.setSelected(crawlerDef.getPrimaryBrowserDefinition().isDownloadImages());
		downloadScriptsCKB_PBC.setSelected(crawlerDef.getPrimaryBrowserDefinition().isDownloadJavaScripts());
	}

	private void populateSecondaryBrowserDefinition(CrawlerDefinition crawlerDef) {
		Conditions secondaryConditions = crawlerDef.getSecondaryBrowserDefinition().getConditions();
		for(ConditionDefinition conDef : secondaryConditions) {
			conditionsTable_SBC.addRow(new Object[]{conDef.getName(),
				conDef.getLhsOperand(), conDef.getOperator(), conDef.getRhsOperand(), conDef.isSuperCondition()});
		}
		invokeSBC_CKB.setSelected(crawlerDef.isInvokeBasePBC());
		securedWebPageCKB_SBC.setSelected(crawlerDef.getSecondaryBrowserDefinition().isSecuredWebPage());
		downloadImagesCKB_SBC.setSelected(crawlerDef.getSecondaryBrowserDefinition().isDownloadImages());
		downloadScriptsCKB_SBC.setSelected(crawlerDef.getSecondaryBrowserDefinition().isDownloadJavaScripts());
	}

	private void populateTertiaryBrowserDefinition(CrawlerDefinition crawlerDef) {
		Conditions tertiaryConditions = crawlerDef.getTertiaryBrowserDefinition().getConditions();
		for(ConditionDefinition conDef : tertiaryConditions) {
			conditionsTable_TBC.addRow(new Object[]{conDef.getName(),
				conDef.getLhsOperand(), conDef.getOperator(), conDef.getRhsOperand(), conDef.isSuperCondition()});
		}
		invokeTBC_CKB.setSelected(crawlerDef.isInvokeBasePBC());
		securedWebPageCKB_TBC.setSelected(crawlerDef.getTertiaryBrowserDefinition().isSecuredWebPage());
		downloadImagesCKB_TBC.setSelected(crawlerDef.getTertiaryBrowserDefinition().isDownloadImages());
		downloadScriptsCKB_TBC.setSelected(crawlerDef.getTertiaryBrowserDefinition().isDownloadJavaScripts());
	}

	private void populateWebPagesDefinition(CrawlerDefinition crawlerDef) {
		WebPages webPages = crawlerDef.getWebPages();
		for(WebPageDefinition webPageDef : webPages) {
			webPagesTable.addRow(new Object[] {webPageDef.getName(), webPageDef.getType(), webPageDef.getBrowser(),
				webPageDef.getNavigationOrder(), webPageDef.isExecuteOnce()});
		}
	}

	private JPanel getInfoPanel() {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		PopTitledBorder tBorder = new PopTitledBorder("Crawler Configuration");
		infoPanel.setBorder(tBorder);

		PopLabel crawlerName = new PopLabel("Crawler Name*");
		crawlerNameTF = new PopTextField();
		crawlerNameTF.setName("TF_CrawlerName");
		infoPanel.add(crawlerName, new GridBagConstraints(0, 0, 1, 1, 0.05, 0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		infoPanel.add(crawlerNameTF, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.5,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 10), 0, 5));

		PopLabel clientName = new PopLabel("Client Name*");
		clientNameTF = new PopTextField();
		clientNameTF.setName("TF_ClientName");
		infoPanel.add(clientName, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		infoPanel.add(clientNameTF, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.5,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 10), 0, 5));

		PopLabel siteName = new PopLabel("Site Name*");
		siteNameTF = new PopTextField();
		siteNameTF.setName("TF_SiteName");
		infoPanel.add(siteName, new GridBagConstraints(4, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		infoPanel.add(siteNameTF, new GridBagConstraints(5, 0, 1, 1, 1.0, 0.5,
			GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 5));

		PopLabel sourceID = new PopLabel("Source ID*");
		sourceIDTF = new PopTextField();
		sourceIDTF.setName("TF_SourceID");
		infoPanel.add(sourceID, new GridBagConstraints(0, 1, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		infoPanel.add(sourceIDTF, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 10), 0, 5));

		PopLabel sourceName = new PopLabel("Source Name*");
		sourceNameTF = new PopTextField();
		sourceNameTF.setName("TF_SourceName");
		infoPanel.add(sourceName, new GridBagConstraints(2, 1, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		infoPanel.add(sourceNameTF, new GridBagConstraints(3, 1, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 10), 0, 5));

		// start of crawler configuration checkbox panel
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new GridBagLayout());

		handlePopupCKB = new PopCheckBox("Handle Popup");
		handlePopupCKB.setName("CBX_HandlePopup");
		checkboxPanel.add(handlePopupCKB, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		infoPanel.add(checkboxPanel, new GridBagConstraints(4, 1, 2, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, -5, 5, 10), 0, 0));
		// end of crawler configuration checkbox panel

		infoPanel.add(getMainButtonPanel(), new GridBagConstraints(0, 2, 6, 1, 1.5, 0.5,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		return infoPanel;
	}

	private JTabbedPane getMainTabbedPane() {
		mainTabPane = new PopTabbedPane();
		mainTabPane.add("Primary Browser", getPrimaryBrowserControlPanel());
		mainTabPane.add("Secondary Browser", getSecondaryBrowserControlPanel());
		mainTabPane.add("Tertiary Browser", getTertiaryBrowserControlPanel());
		mainTabPane.add("Web Flows Summary", getWebPagesPanel());
		mainTabPane.add("Config Web Flow - Step 1", getWebPagePanel());
		mainTabPane.add("Config Web Flow - Step 2", getWebPageConfigStepTwoPanel());
		mainTabPane.add("Config Web Flow - Step 3", getWebPageConfigStepThreePanel());

		mainTabPane.setEnabledAt(4, false);
		mainTabPane.setEnabledAt(5, false);
		mainTabPane.setEnabledAt(6, false);

		mainTabPane.addChangeListener(new MainTab_ChangeListener());

		selectedTabOfMainTabPane = 0;

		return mainTabPane;
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


	//PBC - Start
	private JPanel getPrimaryBrowserControlPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		mainPanel.add(getCRUDConditionPanel_PBC(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.02,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));

		mainPanel.add(getCheckboxPanel_PBC(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 8, 0), 0, 0));

		return mainPanel;
	}

	private JPanel getCRUDConditionPanel_PBC() {
		JPanel crudConPanel = new JPanel();
		crudConPanel.setLayout(new GridBagLayout());

		//left operand panel - start
		JPanel leftOperandPanel = new JPanel();
		leftOperandPanel.setLayout(new GridBagLayout());

		leftExistingCondition_PBC = new PopCheckBox("Use Existing Condition");
		leftExistingCondition_PBC.setName("PBC_CBX1");
		leftExistingCondition_PBC.addItemListener(new PBC_Con_CBX_Left_Listener());

		leftNewCondition_PBC = new PopCheckBox("Other String");
		leftNewCondition_PBC.setName("PBC_CBX2");
		leftNewCondition_PBC.addItemListener(new PBC_Con_CBX_Left_Listener());

		leftConditionBGroup_PBC = new ButtonGroup();
		leftConditionBGroup_PBC.add(leftExistingCondition_PBC);
		leftConditionBGroup_PBC.add(leftNewCondition_PBC);

		cBoxLeft_PBC = new PopComboBox();
		cBoxLeft_PBC.setEnabled(false);
		leftOperandTF_PBC = new PopTextField();
		leftOperandTF_PBC.setEnabled(false);

		leftOperandPanel.add(leftExistingCondition_PBC, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(cBoxLeft_PBC, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftNewCondition_PBC, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftOperandTF_PBC, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
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

		rightExistingCondition_PBC = new PopCheckBox("Use Existing Condition");
		rightExistingCondition_PBC.setName("PBC_CBX3");
		rightExistingCondition_PBC.addItemListener(new PBC_Con_CBX_Right_Listener());

		rightNewCondition_PBC = new PopCheckBox("Other String");
		rightNewCondition_PBC.setName("PBC_CBX4");
		rightNewCondition_PBC.addItemListener(new PBC_Con_CBX_Right_Listener());

		rightConditionBGroup_PBC = new ButtonGroup();
		rightConditionBGroup_PBC.add(rightExistingCondition_PBC);
		rightConditionBGroup_PBC.add(rightNewCondition_PBC);

		cBoxRight_PBC = new PopComboBox();
		cBoxRight_PBC.setEnabled(false);
		rightOperandTF_PBC = new PopTextField();
		rightOperandTF_PBC.setEnabled(false);

		rightOperandPanel.add(rightExistingCondition_PBC, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(cBoxRight_PBC, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightNewCondition_PBC, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightOperandTF_PBC, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
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
		cBoxOperator_PBC = new PopComboBox();
		populateOperator(cBoxOperator_PBC);

		superConditionCBX_PBC = new PopCheckBox("Super Condition");

		operatorPanel.add(labelOperator, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(cBoxOperator_PBC, new GridBagConstraints(1, 0, 1, 1, 0.1, 1.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(superConditionCBX_PBC, new GridBagConstraints(2, 0, 1, 1, 0.1, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		crudConPanel.add(operatorPanel, new GridBagConstraints(0, 2, 1, 1, 0.5, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));

		crudConPanel.add(getConditionTablePanel_PBC(), new GridBagConstraints(0, 3, 2, 2, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));
		//operator - end

		//control buttons - start
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());


		JPanel invisiblePanel = new JPanel();

		bCreate_PBC = new PopButton("Create");
		bUpdate_PBC = new PopButton("Update");
		bDelete_PBC = new PopButton("Delete");
		bClear_PBC = new PopButton("Reset");

		buttonPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bCreate_PBC.setName("PBC_B_Create");
		bCreate_PBC.addActionListener(new PBC_Con_Button_Action_Listener());
		buttonPanel.add(bCreate_PBC, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bUpdate_PBC.setName("PBC_B_Update");
		bUpdate_PBC.addActionListener(new PBC_Con_Button_Action_Listener());
		buttonPanel.add(bUpdate_PBC, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bDelete_PBC.setName("PBC_B_Delete");
		bDelete_PBC.addActionListener(new PBC_Con_Button_Action_Listener());
		buttonPanel.add(bDelete_PBC, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		bClear_PBC.setName("PBC_B_Clear");
		bClear_PBC.addActionListener(new PBC_Con_Button_Action_Listener());
		buttonPanel.add(bClear_PBC, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		crudConPanel.add(buttonPanel, new GridBagConstraints(1, 2, 1, 1, 1.0, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 8, 0), 0, 0));

		updateEnableStateOfUpdateAndDeleteButtons_PBC();
		//control buttons - end

		PopTitledBorder titledBorder = new PopTitledBorder("Create / Edit / Delete Conditions");
		crudConPanel.setBorder(titledBorder);

		populateConditions();

		return crudConPanel;
	}

	private void updateEnableStateOfUpdateAndDeleteButtons_PBC() {
		if((conTableRowNumber_PBC > -1) && (conditionName_PBC != null)) {
			bUpdate_PBC.setEnabled(true);
			bDelete_PBC.setEnabled(true);
		} else {
			bUpdate_PBC.setEnabled(false);
			bDelete_PBC.setEnabled(false);
		}
	}

	private JScrollPane getConditionTablePanel_PBC() {
		Object[] columnNames = {"Condition Name", "Left Operand", "Operator", "Right Operand", "Super Condition"};
		Object[][] rowData = new Object[0][0];
		conditionsTable_PBC = new PopTable(rowData, columnNames);
		conditionsTable_PBC.setPreferredScrollableViewportSize(new Dimension(200, 200));
		conditionsTable_PBC.addMouseListener(new PBC_Condition_Table_Mouse_Adapter());

		JScrollPane scrollPane = new JScrollPane(conditionsTable_PBC);
		conditionsTable_PBC.setFillsViewportHeight(true);

		return scrollPane;
	}

	private JPanel getCheckboxPanel_PBC() {
		JPanel pbcPanel = new JPanel();
		pbcPanel.setLayout(new GridBagLayout());

		invokePBC_CKB = new PopCheckBox("Invoke PBC");
		pbcPanel.add(invokePBC_CKB, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		securedWebPageCKB_PBC = new PopCheckBox("Secured WebPage");
		pbcPanel.add(securedWebPageCKB_PBC, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		downloadImagesCKB_PBC = new PopCheckBox("Download Images");
		pbcPanel.add(downloadImagesCKB_PBC, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		downloadScriptsCKB_PBC = new PopCheckBox("Download Scripts");
		pbcPanel.add(downloadScriptsCKB_PBC, new GridBagConstraints(6, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		PopTitledBorder titledBorder = new PopTitledBorder("Other Configurations");
		pbcPanel.setBorder(titledBorder);

		return pbcPanel;
	}

	private void createCondition_PBC() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		String operator = null;
		if((con_LO_Selected_CB_PBC != null && con_RO_Selected_CB_PBC != null
				&& cBoxOperator_PBC.getSelectedIndex() != -1)) {
			if(con_LO_Selected_CB_PBC.equals("PBC_CBX1")) {
				leftOperand = (String) cBoxLeft_PBC.getSelectedItem();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Left Operand.");
					cBoxLeft_PBC.requestFocus();
					return;
				}
			} else if(con_LO_Selected_CB_PBC.equals("PBC_CBX2")) {
				leftOperand = leftOperandTF_PBC.getText();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Left Operand.");
					leftOperandTF_PBC.requestFocus();
					return;
				}
			}

			if(con_RO_Selected_CB_PBC.equals("PBC_CBX3")) {
				rightOperand = (String) cBoxRight_PBC.getSelectedItem();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Right Operand.");
					cBoxRight_PBC.requestFocus();
					return;
				}
			} else if(con_RO_Selected_CB_PBC.equals("PBC_CBX4")) {
				rightOperand = rightOperandTF_PBC.getText();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Right Operand.");
					rightOperandTF_PBC.requestFocus();
					return;
				}
			}
			operator = (String) cBoxOperator_PBC.getSelectedItem();
			if(!(operator != null && operator.length() > 0)) {
				JOptionPane.showMessageDialog(this, "Select a valid value from \'Operator\' drop down.");
				cBoxOperator_PBC.requestFocus();
				return;
			}

			conDef = getConditionDefinition(leftOperand, operator, rightOperand, superConditionCBX_PBC.isSelected(), null);
			conditionsTable_PBC.addRow(new Object[]{conDef.getName(),
				conDef.getLhsOperand(), conDef.getOperator(), conDef.getRhsOperand(), conDef.isSuperCondition()});

			populateConditions();
			clearConditionFields_PBC();

			conTableRowNumber_PBC = -1;
			conditionName_PBC = null;

			con_LO_Selected_CB_PBC = null;
			con_RO_Selected_CB_PBC = null;
			cBoxOperator_PBC.setSelectedIndex(-1);
		} else {
			JOptionPane.showMessageDialog(this, "Left Operand, Right Operand and Operator are mandatory fields.");
		}
	}


	private void clearConditionFields_PBC() {
		leftConditionBGroup_PBC.remove(leftExistingCondition_PBC);
		leftExistingCondition_PBC.setSelected(false);
		leftConditionBGroup_PBC.add(leftExistingCondition_PBC);

		cBoxLeft_PBC.setSelectedIndex(-1);

		leftConditionBGroup_PBC.remove(leftNewCondition_PBC);
		leftNewCondition_PBC.setSelected(false);
		leftConditionBGroup_PBC.add(leftNewCondition_PBC);

		leftOperandTF_PBC.setText(null);

		cBoxLeft_PBC.setEnabled(false);
		leftOperandTF_PBC.setEnabled(false);

		rightConditionBGroup_PBC.remove(rightExistingCondition_PBC);
		rightExistingCondition_PBC.setSelected(false);
		rightConditionBGroup_PBC.add(rightExistingCondition_PBC);

		cBoxRight_PBC.setSelectedIndex(-1);

		rightConditionBGroup_PBC.remove(rightNewCondition_PBC);
		rightNewCondition_PBC.setSelected(false);
		rightConditionBGroup_PBC.add(rightNewCondition_PBC);

		rightOperandTF_PBC.setText(null);

		cBoxRight_PBC.setEnabled(false);
		rightOperandTF_PBC.setEnabled(false);

		cBoxOperator_PBC.setSelectedIndex(-1);
		superConditionCBX_PBC.setSelected(false);

		this.repaint();
	}

	private void updateCondition_PBC() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		if(con_LO_Selected_CB_PBC != null && con_RO_Selected_CB_PBC != null) {
			if(con_LO_Selected_CB_PBC.equals("PBC_CBX1")) {
				leftOperand = (String) cBoxLeft_PBC.getSelectedItem();
			} else if(con_LO_Selected_CB_PBC.equals("PBC_CBX2")) {
				leftOperand = leftOperandTF_PBC.getText();
			}

			if(con_RO_Selected_CB_PBC.equals("PBC_CBX3")) {
				rightOperand = (String) cBoxRight_PBC.getSelectedItem();
			} else if(con_RO_Selected_CB_PBC.equals("PBC_CBX4")) {
				rightOperand = rightOperandTF_PBC.getText();
			}
			conDef = getConditionDefinition(leftOperand, (String) cBoxOperator_PBC.getSelectedItem(),
				rightOperand, superConditionCBX_PBC.isSelected(), conditionName_PBC);

			conditionsTable_PBC.getModel().setValueAt(conDef.getLhsOperand(), conTableRowNumber_PBC, 1);
			conditionsTable_PBC.getModel().setValueAt(conDef.getOperator(), conTableRowNumber_PBC, 2);
			conditionsTable_PBC.getModel().setValueAt(conDef.getRhsOperand(), conTableRowNumber_PBC, 3);
			conditionsTable_PBC.getModel().setValueAt(conDef.isSuperCondition(), conTableRowNumber_PBC, 4);

			populateConditions();
			clearConditionFields_PBC();
			bCreate_PBC.setEnabled(true);

			conTableRowNumber_PBC = -1;
			conditionName_PBC = null;

			updateEnableStateOfUpdateAndDeleteButtons_PBC();
		}

	}

	private void deleteCondition_PBC() {
		conditionCache.remove(conditionName_PBC);

		((DefaultTableModel) conditionsTable_PBC.getModel()).removeRow(conTableRowNumber_PBC);

		populateConditions();
		clearConditionFields_PBC();
		bCreate_PBC.setEnabled(true);

		conTableRowNumber_PBC = -1;
		conditionName_PBC = null;

		updateEnableStateOfUpdateAndDeleteButtons_PBC();
	}

	private void clearCondition_PBC() {
		populateConditions();
		clearConditionFields_PBC();
		bCreate_PBC.setEnabled(true);

		conTableRowNumber_PBC = -1;
		conditionName_PBC = null;

		updateEnableStateOfUpdateAndDeleteButtons_PBC();
	}

	private void populateConditionFields_PBC() {
		Condition condition = conditionCache.get(conditionName_PBC);
		clearConditionFields_PBC();
		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getLeftSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition_PBC.setSelected(true);
				cBoxLeft_PBC.setEnabled(true);
				cBoxLeft_PBC.setSelectedItem(((Condition) condition.getLeftSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getLeftSideValue()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition_PBC.setSelected(true);
				cBoxLeft_PBC.setEnabled(true);
				cBoxLeft_PBC.setSelectedItem(condition.getLeftSideValue());
			} else {
				leftNewCondition_PBC.setSelected(true);
				leftOperandTF_PBC.setEnabled(true);
				leftOperandTF_PBC.setText((String) condition.getLeftSideValue());
			}
		}

		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getRightSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition_PBC.setSelected(true);
				cBoxRight_PBC.setEnabled(true);
				cBoxRight_PBC.setSelectedItem(((Condition) condition.getRightSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getRightSideValue()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition_PBC.setSelected(true);
				cBoxRight_PBC.setEnabled(true);
				cBoxRight_PBC.setSelectedItem(condition.getRightSideValue());
			} else {
				rightNewCondition_PBC.setSelected(true);
				rightOperandTF_PBC.setEnabled(true);
				rightOperandTF_PBC.setText((String) condition.getRightSideValue());
			}
		}

		cBoxOperator_PBC.setSelectedItem(condition.getOperator().toString());
		superConditionCBX_PBC.setSelected(condition.isSuperCondition());
		bCreate_PBC.setEnabled(false);
	}
	// PBC - End

	//SBC - Start
	private JPanel getSecondaryBrowserControlPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		mainPanel.add(getCRUDConditionPanel_SBC(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.02,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));

		mainPanel.add(getCheckboxPanel_SBC(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 8, 0), 0, 0));

		return mainPanel;
	}

	private JPanel getCRUDConditionPanel_SBC() {
		JPanel crudConPanel = new JPanel();
		crudConPanel.setLayout(new GridBagLayout());

		//left operand panel - start
		JPanel leftOperandPanel = new JPanel();
		leftOperandPanel.setLayout(new GridBagLayout());

		leftExistingCondition_SBC = new PopCheckBox("Use Existing Condition");
		leftExistingCondition_SBC.setName("SBC_CBX1");
		leftExistingCondition_SBC.addItemListener(new SBC_Con_CBX_Left_Listener());

		leftNewCondition_SBC = new PopCheckBox("Other String");
		leftNewCondition_SBC.setName("SBC_CBX2");
		leftNewCondition_SBC.addItemListener(new SBC_Con_CBX_Left_Listener());

		leftConditionBGroup_SBC = new ButtonGroup();
		leftConditionBGroup_SBC.add(leftExistingCondition_SBC);
		leftConditionBGroup_SBC.add(leftNewCondition_SBC);

		cBoxLeft_SBC = new PopComboBox();
		cBoxLeft_SBC.setEnabled(false);
		leftOperandTF_SBC = new PopTextField();
		leftOperandTF_SBC.setEnabled(false);

		leftOperandPanel.add(leftExistingCondition_SBC, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(cBoxLeft_SBC, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftNewCondition_SBC, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftOperandTF_SBC, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
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

		rightExistingCondition_SBC = new PopCheckBox("Use Existing Condition");
		rightExistingCondition_SBC.setName("SBC_CBX3");
		rightExistingCondition_SBC.addItemListener(new SBC_Con_CBX_Right_Listener());

		rightNewCondition_SBC = new PopCheckBox("Other String");
		rightNewCondition_SBC.setName("SBC_CBX4");
		rightNewCondition_SBC.addItemListener(new SBC_Con_CBX_Right_Listener());

		rightConditionBGroup_SBC = new ButtonGroup();
		rightConditionBGroup_SBC.add(rightExistingCondition_SBC);
		rightConditionBGroup_SBC.add(rightNewCondition_SBC);

		cBoxRight_SBC = new PopComboBox();
		cBoxRight_SBC.setEnabled(false);
		rightOperandTF_SBC = new PopTextField();
		rightOperandTF_SBC.setEnabled(false);

		rightOperandPanel.add(rightExistingCondition_SBC, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(cBoxRight_SBC, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightNewCondition_SBC, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightOperandTF_SBC, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
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
		cBoxOperator_SBC = new PopComboBox();
		populateOperator(cBoxOperator_SBC);

		superConditionCBX_SBC = new PopCheckBox("Super Condition");

		operatorPanel.add(labelOperator, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(cBoxOperator_SBC, new GridBagConstraints(1, 0, 1, 1, 0.1, 1.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(superConditionCBX_SBC, new GridBagConstraints(2, 0, 1, 1, 0.1, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		crudConPanel.add(operatorPanel, new GridBagConstraints(0, 2, 1, 1, 0.5, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));

		crudConPanel.add(getConditionTablePanel_SBC(), new GridBagConstraints(0, 3, 2, 2, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));
		//operator - end

		//control buttons - start
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		JPanel invisiblePanel = new JPanel();

		bCreate_SBC = new PopButton("Create");
		bUpdate_SBC = new PopButton("Update");
		bDelete_SBC = new PopButton("Delete");
		bClear_SBC = new PopButton("Reset");

		buttonPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bCreate_SBC.setName("SBC_B_Create");
		bCreate_SBC.addActionListener(new SBC_Con_Button_Action_Listener());
		buttonPanel.add(bCreate_SBC, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bUpdate_SBC.setName("SBC_B_Update");
		bUpdate_SBC.addActionListener(new SBC_Con_Button_Action_Listener());
		buttonPanel.add(bUpdate_SBC, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bDelete_SBC.setName("SBC_B_Delete");
		bDelete_SBC.addActionListener(new SBC_Con_Button_Action_Listener());
		buttonPanel.add(bDelete_SBC, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		bClear_SBC.setName("SBC_B_Clear");
		bClear_SBC.addActionListener(new SBC_Con_Button_Action_Listener());
		buttonPanel.add(bClear_SBC, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		crudConPanel.add(buttonPanel, new GridBagConstraints(1, 2, 1, 1, 1.0, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 8, 0), 0, 0));

		updateEnableStateOfUpdateAndDeleteButtons_SBC();
		//control buttons - end

		PopTitledBorder titledBorder = new PopTitledBorder("Create / Edit / Delete Conditions");
		crudConPanel.setBorder(titledBorder);

		populateConditions();

		return crudConPanel;
	}

	private void updateEnableStateOfUpdateAndDeleteButtons_SBC() {
		if((conTableRowNumber_SBC > -1) && (conditionName_SBC != null)) {
			bUpdate_SBC.setEnabled(true);
			bDelete_SBC.setEnabled(true);
		} else {
			bUpdate_SBC.setEnabled(false);
			bDelete_SBC.setEnabled(false);
		}
	}


	private JScrollPane getConditionTablePanel_SBC() {
		Object[] columnNames = {"Condition Name", "Left Operand", "Operator", "Right Operand", "Super Condition"};
		Object[][] rowData = new Object[0][0];
		conditionsTable_SBC = new PopTable(rowData, columnNames);
		conditionsTable_SBC.setPreferredScrollableViewportSize(new Dimension(200, 200));
		conditionsTable_SBC.addMouseListener(new SBC_Condition_Table_Mouse_Adapter());

		JScrollPane scrollPane = new JScrollPane(conditionsTable_SBC);
		conditionsTable_SBC.setFillsViewportHeight(true);

		return scrollPane;
	}

	private JPanel getCheckboxPanel_SBC() {
		JPanel sbcPanel = new JPanel();
		sbcPanel.setLayout(new GridBagLayout());

		invokeSBC_CKB = new PopCheckBox("Invoke SBC");
		sbcPanel.add(invokeSBC_CKB, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		securedWebPageCKB_SBC = new PopCheckBox("Secured WebPage");
		sbcPanel.add(securedWebPageCKB_SBC, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		downloadImagesCKB_SBC = new PopCheckBox("Download Images");
		sbcPanel.add(downloadImagesCKB_SBC, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		downloadScriptsCKB_SBC = new PopCheckBox("Download Scripts");
		sbcPanel.add(downloadScriptsCKB_SBC, new GridBagConstraints(6, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		PopTitledBorder titledBorder = new PopTitledBorder("Other Configurations");
		sbcPanel.setBorder(titledBorder);

		return sbcPanel;
	}

	private void createCondition_SBC() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		String operator = null;
		if(con_LO_Selected_CB_SBC != null && con_RO_Selected_CB_SBC != null
				&& cBoxOperator_SBC.getSelectedIndex() != -1) {
			if(con_LO_Selected_CB_SBC.equals("SBC_CBX1")) {
				leftOperand = (String) cBoxLeft_SBC.getSelectedItem();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Left Operand.");
					cBoxLeft_SBC.requestFocus();
					return;
				}
			} else if(con_LO_Selected_CB_SBC.equals("SBC_CBX2")) {
				leftOperand = leftOperandTF_SBC.getText();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Left Operand.");
					leftOperandTF_SBC.requestFocus();
					return;
				}
			}

			if(con_RO_Selected_CB_SBC.equals("SBC_CBX3")) {
				rightOperand = (String) cBoxRight_SBC.getSelectedItem();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Right Operand.");
					cBoxRight_SBC.requestFocus();
					return;
				}
			} else if(con_RO_Selected_CB_SBC.equals("SBC_CBX4")) {
				rightOperand = rightOperandTF_SBC.getText();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Right Operand.");
					rightOperandTF_SBC.requestFocus();
					return;
				}
			}
			operator = (String) cBoxOperator_SBC.getSelectedItem();
			if(!(operator != null && operator.length() > 0)) {
				JOptionPane.showMessageDialog(this, "Select a valid value from \'Operator\' drop down.");
				cBoxOperator_SBC.requestFocus();
				return;
			}

			conDef = getConditionDefinition(leftOperand, operator, rightOperand, superConditionCBX_SBC.isSelected(), null);
			conditionsTable_SBC.addRow(new Object[]{conDef.getName(),
				conDef.getLhsOperand(), conDef.getOperator(), conDef.getRhsOperand(), conDef.isSuperCondition()});

			populateConditions();
			clearConditionFields_SBC();

			conTableRowNumber_SBC = -1;
			conditionName_SBC = null;

			con_LO_Selected_CB_SBC = null;
			con_RO_Selected_CB_SBC = null;
			cBoxOperator_SBC.setSelectedIndex(-1);
		} else {
			JOptionPane.showMessageDialog(this, "Left Operand, Right Operand and Operator are mandatory fields.");
		}
	}

	private void clearConditionFields_SBC() {
		leftConditionBGroup_SBC.remove(leftExistingCondition_SBC);
		leftExistingCondition_SBC.setSelected(false);
		leftConditionBGroup_SBC.add(leftExistingCondition_SBC);

		cBoxLeft_SBC.setSelectedIndex(-1);

		leftConditionBGroup_SBC.remove(leftNewCondition_SBC);
		leftNewCondition_SBC.setSelected(false);
		leftConditionBGroup_SBC.add(leftNewCondition_SBC);

		leftOperandTF_SBC.setText(null);

		cBoxLeft_SBC.setEnabled(false);
		leftOperandTF_SBC.setEnabled(false);

		rightConditionBGroup_SBC.remove(rightExistingCondition_SBC);
		rightExistingCondition_SBC.setSelected(false);
		rightConditionBGroup_SBC.add(rightExistingCondition_SBC);

		cBoxRight_SBC.setSelectedIndex(-1);

		rightConditionBGroup_SBC.remove(rightNewCondition_SBC);
		rightNewCondition_SBC.setSelected(false);
		rightConditionBGroup_SBC.add(rightNewCondition_SBC);

		rightOperandTF_SBC.setText(null);

		cBoxRight_SBC.setEnabled(false);
		rightOperandTF_SBC.setEnabled(false);

		cBoxOperator_SBC.setSelectedIndex(-1);
		superConditionCBX_SBC.setSelected(false);

		this.repaint();
	}

	private void updateCondition_SBC() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		if(con_LO_Selected_CB_SBC != null && con_RO_Selected_CB_SBC != null) {
			if(con_LO_Selected_CB_SBC.equals("SBC_CBX1")) {
				leftOperand = (String) cBoxLeft_SBC.getSelectedItem();
			} else if(con_LO_Selected_CB_SBC.equals("SBC_CBX2")) {
				leftOperand = leftOperandTF_SBC.getText();
			}

			if(con_RO_Selected_CB_SBC.equals("SBC_CBX3")) {
				rightOperand = (String) cBoxRight_SBC.getSelectedItem();
			} else if(con_RO_Selected_CB_SBC.equals("SBC_CBX4")) {
				rightOperand = rightOperandTF_SBC.getText();
			}
			conDef = getConditionDefinition(leftOperand, (String) cBoxOperator_SBC.getSelectedItem(),
				rightOperand, superConditionCBX_SBC.isSelected(), conditionName_SBC);

			conditionsTable_SBC.getModel().setValueAt(conDef.getLhsOperand(), conTableRowNumber_SBC, 1);
			conditionsTable_SBC.getModel().setValueAt(conDef.getOperator(), conTableRowNumber_SBC, 2);
			conditionsTable_SBC.getModel().setValueAt(conDef.getRhsOperand(), conTableRowNumber_SBC, 3);
			conditionsTable_SBC.getModel().setValueAt(conDef.isSuperCondition(), conTableRowNumber_SBC, 4);

			populateConditions();
			clearConditionFields_SBC();
			bCreate_SBC.setEnabled(true);

			conTableRowNumber_SBC = -1;
			conditionName_SBC = null;

			updateEnableStateOfUpdateAndDeleteButtons_SBC();
		}

	}

	private void deleteCondition_SBC() {
		conditionCache.remove(conditionName_SBC);

		((DefaultTableModel) conditionsTable_SBC.getModel()).removeRow(conTableRowNumber_SBC);

		populateConditions();
		clearConditionFields_SBC();
		bCreate_SBC.setEnabled(true);

		conTableRowNumber_SBC = -1;
		conditionName_SBC = null;

		updateEnableStateOfUpdateAndDeleteButtons_SBC();
	}

	private void clearCondition_SBC() {
		populateConditions();
		clearConditionFields_SBC();
		bCreate_SBC.setEnabled(true);

		conTableRowNumber_SBC = -1;
		conditionName_SBC = null;

		updateEnableStateOfUpdateAndDeleteButtons_SBC();
	}

	private void populateConditionFields_SBC() {
		Condition condition = conditionCache.get(conditionName_SBC);
		clearConditionFields_SBC();
		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getLeftSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition_SBC.setSelected(true);
				cBoxLeft_SBC.setEnabled(true);
				cBoxLeft_SBC.setSelectedItem(((Condition) condition.getLeftSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getLeftSideValue()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition_SBC.setSelected(true);
				cBoxLeft_SBC.setEnabled(true);
				cBoxLeft_SBC.setSelectedItem(condition.getLeftSideValue());
			} else {
				leftNewCondition_SBC.setSelected(true);
				leftOperandTF_SBC.setEnabled(true);
				leftOperandTF_SBC.setText((String) condition.getLeftSideValue());
			}
		}

		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getRightSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition_SBC.setSelected(true);
				cBoxRight_SBC.setEnabled(true);
				cBoxRight_SBC.setSelectedItem(((Condition) condition.getRightSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getRightSideValue()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition_SBC.setSelected(true);
				cBoxRight_SBC.setEnabled(true);
				cBoxRight_SBC.setSelectedItem(condition.getRightSideValue());
			} else {
				rightNewCondition_SBC.setSelected(true);
				rightOperandTF_SBC.setEnabled(true);
				rightOperandTF_SBC.setText((String) condition.getRightSideValue());
			}
		}

		cBoxOperator_SBC.setSelectedItem(condition.getOperator().toString());
		superConditionCBX_SBC.setSelected(condition.isSuperCondition());
		bCreate_SBC.setEnabled(false);
	}
	//SBC - End

	//TBC - Start
	private JPanel getTertiaryBrowserControlPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		mainPanel.add(getCRUDConditionPanel_TBC(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.02,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));

		mainPanel.add(getCheckboxPanel_TBC(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 8, 0), 0, 0));

		return mainPanel;
	}

	private JPanel getCRUDConditionPanel_TBC() {
		JPanel crudConPanel = new JPanel();
		crudConPanel.setLayout(new GridBagLayout());

		//left operand panel - start
		JPanel leftOperandPanel = new JPanel();
		leftOperandPanel.setLayout(new GridBagLayout());

		leftExistingCondition_TBC = new PopCheckBox("Use Existing Condition");
		leftExistingCondition_TBC.setName("TBC_CBX1");
		leftExistingCondition_TBC.addItemListener(new TBC_Con_CBX_Left_Listener());

		leftNewCondition_TBC = new PopCheckBox("Other String");
		leftNewCondition_TBC.setName("TBC_CBX2");
		leftNewCondition_TBC.addItemListener(new TBC_Con_CBX_Left_Listener());

		leftConditionBGroup_TBC = new ButtonGroup();
		leftConditionBGroup_TBC.add(leftExistingCondition_TBC);
		leftConditionBGroup_TBC.add(leftNewCondition_TBC);

		cBoxLeft_TBC = new PopComboBox();
		cBoxLeft_TBC.setEnabled(false);
		leftOperandTF_TBC = new PopTextField();
		leftOperandTF_TBC.setEnabled(false);

		leftOperandPanel.add(leftExistingCondition_TBC, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(cBoxLeft_TBC, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftNewCondition_TBC, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftOperandTF_TBC, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
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

		rightExistingCondition_TBC = new PopCheckBox("Use Existing Condition");
		rightExistingCondition_TBC.setName("TBC_CBX3");
		rightExistingCondition_TBC.addItemListener(new TBC_Con_CBX_Right_Listener());

		rightNewCondition_TBC = new PopCheckBox("Other String");
		rightNewCondition_TBC.setName("TBC_CBX4");
		rightNewCondition_TBC.addItemListener(new TBC_Con_CBX_Right_Listener());

		rightConditionBGroup_TBC = new ButtonGroup();
		rightConditionBGroup_TBC.add(rightExistingCondition_TBC);
		rightConditionBGroup_TBC.add(rightNewCondition_TBC);

		cBoxRight_TBC = new PopComboBox();
		cBoxRight_TBC.setEnabled(false);
		rightOperandTF_TBC = new PopTextField();
		rightOperandTF_TBC.setEnabled(false);

		rightOperandPanel.add(rightExistingCondition_TBC, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(cBoxRight_TBC, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightNewCondition_TBC, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightOperandTF_TBC, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
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
		cBoxOperator_TBC = new PopComboBox();
		populateOperator(cBoxOperator_TBC);

		superConditionCBX_TBC = new PopCheckBox("Super Condition");

		operatorPanel.add(labelOperator, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(cBoxOperator_TBC, new GridBagConstraints(1, 0, 1, 1, 0.1, 1.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(superConditionCBX_TBC, new GridBagConstraints(2, 0, 1, 1, 0.1, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		crudConPanel.add(operatorPanel, new GridBagConstraints(0, 2, 1, 1, 0.5, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));

		crudConPanel.add(getConditionTablePanel_TBC(), new GridBagConstraints(0, 3, 2, 2, 1.0, 1.0,
		GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));
		//operator - end

		//control buttons - start
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		JPanel invisiblePanel = new JPanel();

		bCreate_TBC = new PopButton("Create");
		bUpdate_TBC = new PopButton("Update");
		bDelete_TBC = new PopButton("Delete");
		bClear_TBC = new PopButton("Reset");

		buttonPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bCreate_TBC.setName("TBC_B_Create");
		bCreate_TBC.addActionListener(new TBC_Con_Button_Action_Listener());
		buttonPanel.add(bCreate_TBC, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bUpdate_TBC.setName("TBC_B_Update");
		bUpdate_TBC.addActionListener(new TBC_Con_Button_Action_Listener());
		buttonPanel.add(bUpdate_TBC, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bDelete_TBC.setName("TBC_B_Delete");
		bDelete_TBC.addActionListener(new TBC_Con_Button_Action_Listener());
		buttonPanel.add(bDelete_TBC, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		bClear_TBC.setName("TBC_B_Clear");
		bClear_TBC.addActionListener(new TBC_Con_Button_Action_Listener());
		buttonPanel.add(bClear_TBC, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		crudConPanel.add(buttonPanel, new GridBagConstraints(1, 2, 1, 1, 1.0, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 8, 0), 0, 0));

		updateEnableStateOfUpdateAndDeleteButtons_TBC();
		//control buttons - end

		PopTitledBorder titledBorder = new PopTitledBorder("Create / Edit / Delete Conditions");
		crudConPanel.setBorder(titledBorder);

		populateConditions();

		return crudConPanel;
	}

	private void updateEnableStateOfUpdateAndDeleteButtons_TBC() {
		if((conTableRowNumber_TBC > -1) && (conditionName_TBC != null)) {
			bUpdate_TBC.setEnabled(true);
			bDelete_TBC.setEnabled(true);
		} else {
			bUpdate_TBC.setEnabled(false);
			bDelete_TBC.setEnabled(false);
		}
	}


	private JScrollPane getConditionTablePanel_TBC() {
		Object[] columnNames = {"Condition Name", "Left Operand", "Operator", "Right Operand", "Super Condition"};
		Object[][] rowData = new Object[0][0];
		conditionsTable_TBC = new PopTable(rowData, columnNames);
		conditionsTable_TBC.setPreferredScrollableViewportSize(new Dimension(200, 200));
		conditionsTable_TBC.addMouseListener(new TBC_Condition_Table_Mouse_Adapter());

		JScrollPane scrollPane = new JScrollPane(conditionsTable_TBC);
		conditionsTable_TBC.setFillsViewportHeight(true);

		return scrollPane;
	}

	private JPanel getCheckboxPanel_TBC() {
		JPanel tbcPanel = new JPanel();
		tbcPanel.setLayout(new GridBagLayout());

		invokeTBC_CKB = new PopCheckBox("Invoke TBC");
		tbcPanel.add(invokeTBC_CKB, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		securedWebPageCKB_TBC = new PopCheckBox("Secured WebPage");
		tbcPanel.add(securedWebPageCKB_TBC, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		downloadImagesCKB_TBC = new PopCheckBox("Download Images");
		tbcPanel.add(downloadImagesCKB_TBC, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		downloadScriptsCKB_TBC = new PopCheckBox("Download Scripts");
		tbcPanel.add(downloadScriptsCKB_TBC, new GridBagConstraints(6, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		PopTitledBorder titledBorder = new PopTitledBorder("Other Configurations");
		tbcPanel.setBorder(titledBorder);

		return tbcPanel;
	}

	private void createCondition_TBC() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		String operator = null;
		if(con_LO_Selected_CB_TBC != null && con_RO_Selected_CB_TBC != null
				&& cBoxOperator_TBC.getSelectedIndex() != -1) {
			if(con_LO_Selected_CB_TBC.equals("TBC_CBX1")) {
				leftOperand = (String) cBoxLeft_TBC.getSelectedItem();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Left Operand.");
					cBoxLeft_TBC.requestFocus();
					return;
				}
			} else if(con_LO_Selected_CB_TBC.equals("TBC_CBX2")) {
				leftOperand = leftOperandTF_TBC.getText();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Left Operand.");
					leftOperandTF_TBC.requestFocus();
					return;
				}
			}

			if(con_RO_Selected_CB_TBC.equals("TBC_CBX3")) {
				rightOperand = (String) cBoxRight_TBC.getSelectedItem();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Right Operand.");
					cBoxRight_TBC.requestFocus();
					return;
				}
			} else if(con_RO_Selected_CB_TBC.equals("TBC_CBX4")) {
				rightOperand = rightOperandTF_TBC.getText();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Right Operand.");
					rightOperandTF_TBC.requestFocus();
					return;
				}
			}
			operator = (String) cBoxOperator_TBC.getSelectedItem();
			if(!(operator != null && operator.length() > 0)) {
				JOptionPane.showMessageDialog(this, "Select a valid value from \'Operator\' drop down.");
				cBoxOperator_TBC.requestFocus();
				return;
			}

			conDef = getConditionDefinition(leftOperand, operator, rightOperand, superConditionCBX_TBC.isSelected(), null);
			conditionsTable_TBC.addRow(new Object[]{conDef.getName(),
				conDef.getLhsOperand(), conDef.getOperator(), conDef.getRhsOperand(), conDef.isSuperCondition()});
			populateConditions();
			clearConditionFields_TBC();

			conTableRowNumber_TBC = -1;
			conditionName_TBC = null;

			con_LO_Selected_CB_TBC = null;
			con_RO_Selected_CB_TBC = null;
			cBoxOperator_TBC.setSelectedIndex(-1);
		} else {
			JOptionPane.showMessageDialog(this, "Left Operand, Right Operand and Operator are mandatory fields.");
		}
	}

	private void clearConditionFields_TBC() {
		leftConditionBGroup_TBC.remove(leftExistingCondition_TBC);
		leftExistingCondition_TBC.setSelected(false);
		leftConditionBGroup_TBC.add(leftExistingCondition_TBC);

		cBoxLeft_TBC.setSelectedIndex(-1);

		leftConditionBGroup_TBC.remove(leftNewCondition_TBC);
		leftNewCondition_TBC.setSelected(false);
		leftConditionBGroup_TBC.add(leftNewCondition_TBC);

		leftOperandTF_TBC.setText(null);

		cBoxLeft_TBC.setEnabled(false);
		leftOperandTF_TBC.setEnabled(false);

		rightConditionBGroup_TBC.remove(rightExistingCondition_TBC);
		rightExistingCondition_TBC.setSelected(false);
		rightConditionBGroup_TBC.add(rightExistingCondition_TBC);

		cBoxRight_TBC.setSelectedIndex(-1);

		rightConditionBGroup_TBC.remove(rightNewCondition_TBC);
		rightNewCondition_TBC.setSelected(false);
		rightConditionBGroup_TBC.add(rightNewCondition_TBC);

		rightOperandTF_TBC.setText(null);

		cBoxRight_TBC.setEnabled(false);
		rightOperandTF_TBC.setEnabled(false);

		cBoxOperator_TBC.setSelectedIndex(-1);
		superConditionCBX_TBC.setSelected(false);

		this.repaint();
	}

	private void updateCondition_TBC() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		if(con_LO_Selected_CB_TBC != null && con_RO_Selected_CB_TBC != null) {
			if(con_LO_Selected_CB_TBC.equals("TBC_CBX1")) {
				leftOperand = (String) cBoxLeft_TBC.getSelectedItem();
			} else if(con_LO_Selected_CB_TBC.equals("TBC_CBX2")) {
				leftOperand = leftOperandTF_TBC.getText();
			}

			if(con_RO_Selected_CB_TBC.equals("TBC_CBX3")) {
				rightOperand = (String) cBoxRight_TBC.getSelectedItem();
			} else if(con_RO_Selected_CB_TBC.equals("TBC_CBX4")) {
				rightOperand = rightOperandTF_TBC.getText();
			}
			conDef = getConditionDefinition(leftOperand, (String) cBoxOperator_TBC.getSelectedItem(),
				rightOperand, superConditionCBX_TBC.isSelected(), conditionName_TBC);

			conditionsTable_TBC.getModel().setValueAt(conDef.getLhsOperand(), conTableRowNumber_TBC, 1);
			conditionsTable_TBC.getModel().setValueAt(conDef.getOperator(), conTableRowNumber_TBC, 2);
			conditionsTable_TBC.getModel().setValueAt(conDef.getRhsOperand(), conTableRowNumber_TBC, 3);
			conditionsTable_TBC.getModel().setValueAt(conDef.isSuperCondition(), conTableRowNumber_TBC, 4);

			populateConditions();
			clearConditionFields_TBC();
			bCreate_TBC.setEnabled(true);

			conTableRowNumber_TBC = -1;
			conditionName_TBC = null;

			updateEnableStateOfUpdateAndDeleteButtons_TBC();
		}

	}

	private void deleteCondition_TBC() {
		conditionCache.remove(conditionName_TBC);
		((DefaultTableModel) conditionsTable_TBC.getModel()).removeRow(conTableRowNumber_TBC);
		populateConditions();
		clearConditionFields_TBC();
		bCreate_TBC.setEnabled(true);
		conTableRowNumber_TBC = -1;
		conditionName_TBC = null;
		updateEnableStateOfUpdateAndDeleteButtons_TBC();
	}

	private void clearCondition_TBC() {
		populateConditions();
		clearConditionFields_TBC();
		bCreate_TBC.setEnabled(true);
		conTableRowNumber_TBC = -1;
		conditionName_TBC = null;
		updateEnableStateOfUpdateAndDeleteButtons_TBC();
	}

	private void populateConditionFields_TBC() {
		Condition condition = conditionCache.get(conditionName_TBC);
		clearConditionFields_TBC();
		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getLeftSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition_TBC.setSelected(true);
				cBoxLeft_TBC.setEnabled(true);
				cBoxLeft_TBC.setSelectedItem(((Condition) condition.getLeftSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getLeftSideValue()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition_TBC.setSelected(true);
				cBoxLeft_TBC.setEnabled(true);
				cBoxLeft_TBC.setSelectedItem(condition.getLeftSideValue());
			} else {
				leftNewCondition_TBC.setSelected(true);
				leftOperandTF_TBC.setEnabled(true);
				leftOperandTF_TBC.setText((String) condition.getLeftSideValue());
			}
		}

		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getRightSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition_TBC.setSelected(true);
				cBoxRight_TBC.setEnabled(true);
				cBoxRight_TBC.setSelectedItem(((Condition) condition.getRightSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getRightSideValue()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition_TBC.setSelected(true);
				cBoxRight_TBC.setEnabled(true);
				cBoxRight_TBC.setSelectedItem(condition.getRightSideValue());
			} else {
				rightNewCondition_TBC.setSelected(true);
				rightOperandTF_TBC.setEnabled(true);
				rightOperandTF_TBC.setText((String) condition.getRightSideValue());
			}
		}

		cBoxOperator_TBC.setSelectedItem(condition.getOperator().toString());
		superConditionCBX_TBC.setSelected(condition.isSuperCondition());
		bCreate_TBC.setEnabled(false);
	}
	//TBC - End

	//WebPages - Start
	private JPanel getWebPagesPanel() {
		JPanel webPagesPanel = new JPanel();
		webPagesPanel.setLayout(new GridBagLayout());
		webPagesPanel.setBorder(new PopTitledBorder("Create / Edit / Delete Web Flows"));

		Object[] columnNames = {"Name", "Type", "Browser To Execute", "Navigation Order", "Execute Once"};
		Object[][] rowData = new Object[0][0];
		webPagesTable = new PopTable(rowData, columnNames);
		webPagesTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
		//webPagesTable.removeColumn(webPagesTable.getColumnModel().getColumn(Constants.WEBPAGE_SUMMARY_TABLE_UNIQUE_NAME));
		//webPagesTable.addMouseListener(new PBC_Condition_Table_Mouse_Adapter());

		JScrollPane scrollPane = new JScrollPane(webPagesTable);
		webPagesTable.setFillsViewportHeight(true);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		JPanel invisiblePanel = new JPanel();

		PopButton bCreateWebPage = new PopButton("Create");
		bCreateWebPage.setName("B_Create_WebPage");
		bCreateWebPage.addActionListener( new WebPages_Button_Action_Listener());

		bEditWebpage = new PopButton("Update");
		bEditWebpage.setName("B_Update_WebPage");
		bEditWebpage.addActionListener( new WebPages_Button_Action_Listener());

		PopButton bDeleteWebPage = new PopButton("Delete");
		bDeleteWebPage.setName("B_Delete_WebPage");
		bDeleteWebPage.addActionListener( new WebPages_Button_Action_Listener());

		buttonPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		buttonPanel.add(bCreateWebPage, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		buttonPanel.add(bEditWebpage, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		buttonPanel.add(bDeleteWebPage, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		webPagesPanel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		webPagesPanel.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));

		return webPagesPanel;
	}

	private void createWebPage() {
		mainTabPane.setEnabledAt(0, false);
		mainTabPane.setEnabledAt(1, false);
		mainTabPane.setEnabledAt(2, false);
		mainTabPane.setEnabledAt(3, false);
		mainTabPane.setEnabledAt(4, true);
		mainTabPane.setSelectedIndex(4);
		webPageConditionTabs.setSelectedIndex(0);
	}

	private void updateWebPage() {
		int rowCount = webPagesTable.getRowCount();
		if(rowCount == 0) {
			JOptionPane.showMessageDialog(this, "No Web Flow(s) defined yet.");
			bEditWebpage.requestFocus();
			return;
		}
		editWebPageRowNumber = webPagesTable.getSelectedRow();
		if(editWebPageRowNumber >= 0) {
			updateWebPageStepOne((String) webPagesTable.getValueAt(editWebPageRowNumber, Constants.WEBPAGE_SUMMARY_TABLE_NAME));
		} else {
			JOptionPane.showMessageDialog(this, "Select a Web Flow to Update.");
			bEditWebpage.requestFocus();
			return;
		}
	}

	private void updateWebPageStepOne(String webPageName) {
		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
		WebPageDefinition webPageDef = crawlerDef.getWebPages().getWebPageByName(webPageName);
		uniqueNameOfWebPage = webPageDef.getUniqueName();
		wpNameTF.setText(webPageDef.getName());
		wpTypeCMB.setSelectedItem(webPageDef.getType());
		wpNavOrderTF.setText(webPageDef.getNavigationOrder());
		wpBrowserCMB.setSelectedItem(webPageDef.getBrowser());
		wpUrlTF.setText(webPageDef.getUrl());
		wpExecuteOnceCBX.setSelected(webPageDef.isExecuteOnce());
		populateConditions(webPageDef.getConditions(), conditionsTable_WPQC);
		populateConditions(webPageDef.getMainIF().getConditions(), conditionsTable_CBQC);
		mainTabPane.setEnabledAt(0, false);
		mainTabPane.setEnabledAt(1, false);
		mainTabPane.setEnabledAt(2, false);
		mainTabPane.setEnabledAt(3, false);
		mainTabPane.setEnabledAt(4, true);
		mainTabPane.setEnabledAt(5, false);
		mainTabPane.setEnabledAt(6, false);
		mainTabPane.setSelectedIndex(4);
		webPageConditionTabs.setSelectedIndex(0);
		editWebFlow = true;
		sameWebPage = true;
	}

	private void populateConditions(Conditions conditions, PopTable table) {
		String lhsOperand = null;
		String rhsOperand = null;
		Condition tempCondition = null;
		for(ConditionDefinition condition : conditions) {
			tempCondition = conditionCache.get(condition.getName());
			if(tempCondition.getLeftSideValue() instanceof Condition) {
				lhsOperand = ((Condition) tempCondition.getLeftSideValue()).getName();
			} else {
				lhsOperand = (String) tempCondition.getLeftSideValue();
			}
			if(tempCondition.getRightSideValue() instanceof Condition) {
				rhsOperand = ((Condition) tempCondition.getRightSideValue()).getName();
			} else {
				rhsOperand = (String) tempCondition.getRightSideValue();
			}
			table.addRow(new Object[]{condition.getName(),
				lhsOperand, condition.getOperator(), rhsOperand, condition.isSuperCondition()});
		}
		table.clearSelection();
	}

	private void deleteWebPage() {
	    int response = JOptionPane.showConfirmDialog(null, "Do you want to delete the selected Web Flow?", "Confirm",
	            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    if (response == JOptionPane.YES_OPTION) {
			int selectedRow = webPagesTable.getSelectedRow();
			if(selectedRow > -1) {
				String webPageName = (String) webPagesTable.getValueAt(selectedRow, 0);
				String sourceID = sourceIDTF.getText();
				CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
				crawlerDef.removeWebPageByName(webPageName);
				crawlerCache.put(sourceID, crawlerDef);
				webPagesTable.getDefaultModel().removeRow(selectedRow);
				webPagesTable.updateUI();
			}
	    }
	}

	private void clearWebPage() {

	}
	//WebPages - End

	//WebPage - Start
	private JPanel getWebPagePanel() {
		JPanel webPagePanel = new JPanel();
		webPagePanel.setLayout(new GridBagLayout());

		JPanel configPanel = new JPanel();
		configPanel.setLayout(new GridBagLayout());
		configPanel.setBorder(new PopTitledBorder("Web Flow - Configuration"));

		PopLabel wpNameLBL = new PopLabel("Name*");
		wpNameTF = new PopTextField();

		PopLabel wpTypeLBL = new PopLabel("Type*");
		wpTypeCMB = new PopComboBox();
		populateWebPageType(wpTypeCMB);

		PopLabel wpNavOrderLBL = new PopLabel("Navigation Order*");
		wpNavOrderTF = new PopTextField();
		wpNavOrderTF.addKeyListener(new WebPage_Key_Adapter());

		PopLabel wpBrowserLBL = new PopLabel("Browser To Execute*");
		wpBrowserCMB = new PopComboBox();
		populateBrowserType(wpBrowserCMB);

		wpExecuteOnceCBX = new PopCheckBox("Execute Once");

		PopLabel wpUrlLBL = new PopLabel("URL*");
		wpUrlTF = new PopTextField();

		configPanel.add(wpNameLBL, new GridBagConstraints(0, 0, 1, 1, 0.05, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		configPanel.add(wpNameTF, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.5,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 5, 15), 5, 0));
		configPanel.add(wpTypeLBL, new GridBagConstraints(2, 0, 1, 1, 0.05, 0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		configPanel.add(wpTypeCMB, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 15), 0, 0));
		configPanel.add(wpNavOrderLBL, new GridBagConstraints(4, 0, 1, 1, 0.01, 0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		configPanel.add(wpNavOrderTF, new GridBagConstraints(5, 0, 1, 1, 1.0, 0.5,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 5, 15), 5, 0));
		configPanel.add(wpBrowserLBL, new GridBagConstraints(6, 0, 1, 1, 0.01, 0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		configPanel.add(wpBrowserCMB, new GridBagConstraints(7, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
		configPanel.add(wpUrlLBL, new GridBagConstraints(0, 1, 1, 1, 0.05, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		configPanel.add(wpUrlTF, new GridBagConstraints(1, 1, 3, 1, 1.0, 0.5,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 5, 15), 5, 0));
		configPanel.add(wpExecuteOnceCBX, new GridBagConstraints(4, 1, 1, 1, 0.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, -5, 5, 0), 0, 0));

		webPagePanel.add(configPanel, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.01,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		webPageConditionTabs = new PopTabbedPane();
		webPageConditionTabs.addTab("Web Flow - Qualifying Conditions", getWPQualifyingConditionsPanel());
		webPageConditionTabs.addTab("Code Block - Qualifying Conditions", getCBQualifyingConditionsPanel());

		webPagePanel.add(webPageConditionTabs, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.02,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridBagLayout());

		b_Next = new PopButton("Next");
		b_Next.setName("B_WebPage_Next");
		b_Next.addActionListener(new WebPageConfig_1_Button_Action_Listener());

		b_Cancel = new PopButton("Cancel");
		b_Cancel.setName("B_WebPage_Cancel");
		b_Cancel.addActionListener(new WebPageConfig_1_Button_Action_Listener());

		JPanel invisiblePanel = new JPanel();

		buttonsPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		buttonsPanel.add(b_Next, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		buttonsPanel.add(b_Cancel, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		webPagePanel.add(buttonsPanel, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));

		return webPagePanel;
	}

	private void populateBrowserType(PopComboBox cBox) {
		cBox.addItem("");
		List<String> browsers = configCache.getConfigurations().getBrowserToExecute();
		for(String browser : browsers) {
			cBox.addItem(browser);
		}
	}

	private void populateWebPageType(PopComboBox cBox) {
		cBox.addItem("");
		List<String> webpageTypes = configCache.getConfigurations().getWebflowTypes();
		for(String webpageType : webpageTypes) {
			cBox.addItem(webpageType);
		}
	}

	private void wpConfigStep_1() throws Exception {
		if(updateWebPageStepOne()) {
			mainTabPane.setEnabledAt(4, false);
			mainTabPane.setEnabledAt(5, true);
			mainTabPane.setEnabledAt(6, false);
			mainTabPane.setSelectedIndex(5);
			if(editWebFlow) {
				resetConfigWebFlowStep_Two();
				mainTabPane.setEnabledAt(5, true);
				mainTabPane.setSelectedIndex(5);
				populateTree();
				updateTree();
			}
		}
	}

	private void updateTree() {
		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
		WebPages webPages = crawlerDef.getWebPages();
		WebPageDefinition webPageDef = webPages.getWebPage(uniqueNameOfWebPage);
		//setting unique name WebPageDefinition to be edited
		uniqueNameOfWebPage = webPageDef.getUniqueName();
		tree.setUniqueNameOfWebPage(uniqueNameOfWebPage);
		tree.setWebPageName(webPageDef.getName());
		tree.setSourceID(sourceID);
		tree.setBrowserTypeOfWebPage(browserTypeOfWebPage);
	}

	private void populateTree() throws Exception {
		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
		WebPageDefinition webPageDef = crawlerDef.getWebPage(uniqueNameOfWebPage);
		List<MainIfElementsDef> mainIfElements = webPageDef.getMainIF().getMainIfElementsDef();
		PopTreeNode ifNode = null;
		PopTreeNode elseIfNode = null;
		PopTreeNode elseNode = null;
		PopTreeNode locNode = null;
		PopTreeNode actionNode = null;

		for(MainIfElementsDef element : mainIfElements) {
			if(element instanceof IfDefinition) {
				ifNode = new PopTreeNode("if" +
					codeGenUtil.getCondition(((IfDefinition) element).getConditions()).toStringBuffer() + " {",
					true, PopTreeNodeType.If);
				ifNode.setNodeID(((IfDefinition) element).getNodeID());
				List<IfElseElementsDef> ifChilds = ((IfDefinition) element).getIfElseElements();
				if(ifChilds.size() > 0) {
					addChildPopTreeNodes(ifNode, ifChilds);
				}
				((PopTreeNode) tree.getModel().getRoot()).add(ifNode);
				PopTreeNode ifClosingNode = new PopTreeNode("}", true, PopTreeNodeType.If);
				ifClosingNode.setClosingTag(true);
				((PopTreeNode) tree.getModel().getRoot()).add(ifClosingNode);
			} else if(element instanceof ElseIfDefinition) {
				elseIfNode = new PopTreeNode("else if" +
					codeGenUtil.getCondition(((ElseIfDefinition) element).getConditions()).toStringBuffer() + " {",
					true, PopTreeNodeType.Else_If);
				elseIfNode.setNodeID(((ElseIfDefinition) element).getNodeID());
				List<IfElseElementsDef> ifChilds = ((ElseIfDefinition) element).getIfElseElements();
				if(ifChilds.size() > 0) {
					addChildPopTreeNodes(elseIfNode, ifChilds);
				}
				elseIfNode.setPeerIfNode(ifNode);
				((PopTreeNode) tree.getModel().getRoot()).add(elseIfNode);
				PopTreeNode elseIfClosingNode = new PopTreeNode("}", true, PopTreeNodeType.Else_If);
				elseIfClosingNode.setClosingTag(true);
				((PopTreeNode) tree.getModel().getRoot()).add(elseIfClosingNode);
				//ifNode.setSiblingIf(true);
			} else if(element instanceof ElseDefinition) {
				elseNode = new PopTreeNode("else {", true, PopTreeNodeType.Else);
				elseNode.setNodeID(((ElseDefinition) element).getNodeID());
				List<IfElseElementsDef> ifChilds = ((ElseDefinition) element).getIfElseElements();
				if(ifChilds.size() > 0) {
					addChildPopTreeNodes(elseNode, ifChilds);
				}
				elseNode.setPeerIfNode(ifNode);
				elseNode.getPeerIfNode().setAddedElse(true);
				((PopTreeNode) tree.getModel().getRoot()).add(elseNode);
				PopTreeNode elseClosingNode = new PopTreeNode("}", true, PopTreeNodeType.Else);
				elseClosingNode.setClosingTag(true);
				((PopTreeNode) tree.getModel().getRoot()).add(elseClosingNode);
				//elseIfNode.setSiblingIf(true);
			} else if(element instanceof LineOfCodeDefinition) {
				String lineOfCodeString = objectFactory.getLineOfCode((LineOfCodeDefinition) element,
						webPageDef.getName()).toStringBuffer().toString();
				locNode = new PopTreeNode(lineOfCodeString, true, PopTreeNodeType.LineOfCode);
				locNode.setNodeID(((LineOfCodeDefinition) element).getNodeID());
				((PopTreeNode) tree.getModel().getRoot()).add(locNode);
			} else if(element instanceof ActionDefinition) {
				String actionString = objectFactory.getActionFromActionDef((ActionDefinition) element,
					webPageDef.getName()).toStringBuffer().toString();
				actionNode = new PopTreeNode(actionString, true, PopTreeNodeType.Action);
				actionNode.setNodeID(((ActionDefinition) element).getNodeID());
				((PopTreeNode) tree.getModel().getRoot()).add(actionNode);
			}
		}
		for (int rowIndex = 0; rowIndex < tree.getRowCount(); rowIndex ++) {
			tree.expandRow(rowIndex);
		}
		tree.updateUI();
	}

	private void addChildPopTreeNodes(PopTreeNode parentNode, List<IfElseElementsDef> ifElseElements) throws Exception {
		PopTreeNode ifNode = null;
		PopTreeNode elseIfNode = null;
		PopTreeNode elseNode = null;
		PopTreeNode locNode = null;
		PopTreeNode actionNode = null;
		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
		WebPageDefinition webPageDef = crawlerDef.getWebPages().getWebPage(uniqueNameOfWebPage);

		for(IfElseElementsDef element : ifElseElements) {
			if(element instanceof IfDefinition) {
				ifNode = new PopTreeNode("if" +
					codeGenUtil.getCondition(((IfDefinition) element).getConditions()).toStringBuffer() + " {",
					true, PopTreeNodeType.If);
				ifNode.setNodeID(((IfDefinition) element).getNodeID());
				updateSiblingIfs(parentNode, true);
				ifNode.setParentNodeID(parentNode.getNodeID());
				ifNode.setParentNodeType(parentNode.getNodeType());
				parentNode.add(ifNode);
				List<IfElseElementsDef> ifChilds = ((IfDefinition) element).getIfElseElements();
				if(ifChilds.size() > 0) {
					addChildPopTreeNodes(ifNode, ifChilds);
				}
				PopTreeNode ifClosingNode = new PopTreeNode("}", true, PopTreeNodeType.If);
				ifClosingNode.setClosingTag(true);
				parentNode.add(ifClosingNode);
			} else if(element instanceof ElseIfDefinition) {
				elseIfNode = new PopTreeNode("else if" +
					codeGenUtil.getCondition(((ElseIfDefinition) element).getConditions()).toStringBuffer() + " {",
					true, PopTreeNodeType.Else_If);
				elseIfNode.setNodeID(((ElseIfDefinition) element).getNodeID());
				elseIfNode.setParentNodeID(parentNode.getNodeID());
				elseIfNode.setParentNodeType(parentNode.getNodeType());
				parentNode.add(elseIfNode);
				List<IfElseElementsDef> ifChilds = ((ElseIfDefinition) element).getIfElseElements();
				if(ifChilds.size() > 0) {
					addChildPopTreeNodes(elseIfNode, ifChilds);
				}
				elseIfNode.setPeerIfNode(ifNode);
				PopTreeNode elseIfClosingNode = new PopTreeNode("}", true, PopTreeNodeType.Else_If);
				elseIfClosingNode.setClosingTag(true);
				parentNode.add(elseIfClosingNode);
				//ifNode.setSiblingIf(true);
			} else if(element instanceof ElseDefinition) {
				elseNode = new PopTreeNode("else {", true, PopTreeNodeType.Else);
				elseNode.setNodeID(((ElseDefinition) element).getNodeID());
				elseNode.setParentNodeID(parentNode.getNodeID());
				elseNode.setParentNodeType(parentNode.getNodeType());
				parentNode.add(elseNode);
				List<IfElseElementsDef> ifChilds = ((ElseDefinition) element).getIfElseElements();
				if(ifChilds.size() > 0) {
					addChildPopTreeNodes(elseNode, ifChilds);
				}
				elseNode.setPeerIfNode(ifNode);
				elseNode.getPeerIfNode().setAddedElse(true);
				PopTreeNode elseClosingNode = new PopTreeNode("}", true, PopTreeNodeType.Else);
				elseClosingNode.setClosingTag(true);
				parentNode.add(elseClosingNode);
				if(elseIfNode != null) {
					elseIfNode.setSiblingIf(true);
				}
			} else if(element instanceof LineOfCodeDefinition) {
				String lineOfCodeString = objectFactory.getLineOfCode((LineOfCodeDefinition) element,
					webPageDef.getName()).toStringBuffer().toString();
				locNode = new PopTreeNode(lineOfCodeString, true, PopTreeNodeType.LineOfCode);
				locNode.setNodeID(((LineOfCodeDefinition) element).getNodeID());
				locNode.setParentNodeID(parentNode.getNodeID());
				locNode.setParentNodeType(parentNode.getNodeType());
				parentNode.add(locNode);
			} else if(element instanceof ActionDefinition) {
				String actionString = objectFactory.getActionFromActionDef((ActionDefinition) element,
					webPageDef.getName()).toStringBuffer().toString();
				actionNode = new PopTreeNode(actionString, true, PopTreeNodeType.Action);
				actionNode.setNodeID(((ActionDefinition) element).getNodeID());
				actionNode.setParentNodeID(parentNode.getNodeID());
				actionNode.setParentNodeType(parentNode.getNodeType());
				parentNode.add(actionNode);
			}
		}
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

	private boolean cancelConfigWP() {
	    int response = -1;
	    if(editWebFlow) {
	    	response = JOptionPane.showConfirmDialog(null, "Do you want to undo changes made to this Web Flow?", "Confirm",
		        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    	if(response == JOptionPane.YES_OPTION) {
	    		returnToWebFlowSummary();
	    		editWebFlow = false;
	    	}
	    } else {
	    	response = JOptionPane.showConfirmDialog(null, "Do you want to delete this Web Flow?", "Confirm",
	            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (response == JOptionPane.YES_OPTION) {
		    	if(uniqueNameOfWebPage != null) {
					String sourceID = sourceIDTF.getText();
					CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
					crawlerDef.removeWebPage(uniqueNameOfWebPage);
					crawlerCache.put(sourceID, crawlerDef);
					sameWebPage = false;
					uniqueNameOfWebPage = null;
		    	}
		    	returnToWebFlowSummary();
		    }
	    }
	    return false;
	}

	private boolean returnToWebFlowSummary() {
		mainTabPane.setEnabledAt(0, true);
		mainTabPane.setEnabledAt(1, true);
		mainTabPane.setEnabledAt(2, true);
		mainTabPane.setEnabledAt(3, true);
		mainTabPane.setEnabledAt(4, false);
		mainTabPane.setEnabledAt(5, false);
		mainTabPane.setEnabledAt(6, false);
		mainTabPane.setSelectedIndex(3);
		sameWebPage = false;
		resetConfigWebFlow();
		return true;
	}
	//WebPage - End

	//WebPage - Qualifying Conditions - Start
	private JPanel getWPQualifyingConditionsPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(getCRUDConditionPanel_WPQualCon(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.02,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
		return mainPanel;
	}

	private JPanel getCRUDConditionPanel_WPQualCon() {
		JPanel crudConPanel = new JPanel();
		crudConPanel.setLayout(new GridBagLayout());

		//left operand panel - start
		JPanel leftOperandPanel = new JPanel();
		leftOperandPanel.setLayout(new GridBagLayout());

		leftExistingCondition_WPQC = new PopCheckBox("Use Existing Condition");
		leftExistingCondition_WPQC.setName("WPQC_CBX1");
		leftExistingCondition_WPQC.addItemListener(new WPQC_Con_CBX_Left_Listener());

		leftNewCondition_WPQC = new PopCheckBox("Other String");
		leftNewCondition_WPQC.setName("WPQC_CBX2");
		leftNewCondition_WPQC.addItemListener(new WPQC_Con_CBX_Left_Listener());

		leftConditionBGroup_WPQC = new ButtonGroup();
		leftConditionBGroup_WPQC.add(leftExistingCondition_WPQC);
		leftConditionBGroup_WPQC.add(leftNewCondition_WPQC);

		cBoxLeft_WPQC = new PopComboBox();
		cBoxLeft_WPQC.setEnabled(false);
		leftOperandTF_WPQC = new PopTextField();
		leftOperandTF_WPQC.setEnabled(false);

		leftOperandPanel.add(leftExistingCondition_WPQC, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(cBoxLeft_WPQC, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftNewCondition_WPQC, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftOperandTF_WPQC, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
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

		rightExistingCondition_WPQC = new PopCheckBox("Use Existing Condition");
		rightExistingCondition_WPQC.setName("WPQC_CBX3");
		rightExistingCondition_WPQC.addItemListener(new WPQC_Con_CBX_Right_Listener());

		rightNewCondition_WPQC = new PopCheckBox("Other String");
		rightNewCondition_WPQC.setName("WPQC_CBX4");
		rightNewCondition_WPQC.addItemListener(new WPQC_Con_CBX_Right_Listener());

		rightConditionBGroup_WPQC = new ButtonGroup();
		rightConditionBGroup_WPQC.add(rightExistingCondition_WPQC);
		rightConditionBGroup_WPQC.add(rightNewCondition_WPQC);

		cBoxRight_WPQC = new PopComboBox();
		cBoxRight_WPQC.setEnabled(false);
		rightOperandTF_WPQC = new PopTextField();
		rightOperandTF_WPQC.setEnabled(false);

		rightOperandPanel.add(rightExistingCondition_WPQC, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(cBoxRight_WPQC, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightNewCondition_WPQC, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightOperandTF_WPQC, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
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
		cBoxOperator_WPQC = new PopComboBox();
		populateOperator(cBoxOperator_WPQC);

		superConditionCBX_WPQC = new PopCheckBox("Super Condition");

		operatorPanel.add(labelOperator, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(cBoxOperator_WPQC, new GridBagConstraints(1, 0, 1, 1, 0.1, 1.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(superConditionCBX_WPQC, new GridBagConstraints(2, 0, 1, 1, 0.1, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		crudConPanel.add(operatorPanel, new GridBagConstraints(0, 2, 1, 1, 0.5, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));

		crudConPanel.add(getConditionTablePanel_WPQC(), new GridBagConstraints(0, 3, 2, 2, 1.0, 1.0,
		GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));
		//operator - end

		//control buttons - start
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		JPanel invisiblePanel = new JPanel();

		bCreate_WPQC = new PopButton("Create");
		bUpdate_WPQC = new PopButton("Update");
		bDelete_WPQC = new PopButton("Delete");
		bClear_WPQC = new PopButton("Reset");

		buttonPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bCreate_WPQC.setName("WPQC_B_Create");
		bCreate_WPQC.addActionListener(new WPQC_Con_Button_Action_Listener());
		buttonPanel.add(bCreate_WPQC, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bUpdate_WPQC.setName("WPQC_B_Update");
		bUpdate_WPQC.addActionListener(new WPQC_Con_Button_Action_Listener());
		buttonPanel.add(bUpdate_WPQC, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bDelete_WPQC.setName("WPQC_B_Delete");
		bDelete_WPQC.addActionListener(new WPQC_Con_Button_Action_Listener());
		buttonPanel.add(bDelete_WPQC, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		bClear_WPQC.setName("WPQC_B_Clear");
		bClear_WPQC.addActionListener(new WPQC_Con_Button_Action_Listener());
		buttonPanel.add(bClear_WPQC, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		crudConPanel.add(buttonPanel, new GridBagConstraints(1, 2, 1, 1, 1.0, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 8, 0), 0, 0));

		updateEnableStateOfUpdateAndDeleteButtons_WPQC();
		//control buttons - end

		PopTitledBorder titledBorder = new PopTitledBorder("Create / Edit / Delete Conditions");
		crudConPanel.setBorder(titledBorder);

		populateConditions();

		return crudConPanel;
	}

	private void updateEnableStateOfUpdateAndDeleteButtons_WPQC() {
		if((conTableRowNumber_WPQC > -1) && (conditionName_WPQC != null)) {
			bUpdate_WPQC.setEnabled(true);
			bDelete_WPQC.setEnabled(true);
		} else {
			bUpdate_WPQC.setEnabled(false);
			bDelete_WPQC.setEnabled(false);
		}
	}

	private JScrollPane getConditionTablePanel_WPQC() {
		Object[] columnNames = {"Condition Name", "Left Operand", "Operator", "Right Operand", "Super Condition"};
		Object[][] rowData = new Object[0][0];
		conditionsTable_WPQC = new PopTable(rowData, columnNames);
		conditionsTable_WPQC.setPreferredScrollableViewportSize(new Dimension(200, 200));
		conditionsTable_WPQC.addMouseListener(new WPQC_Condition_Table_Mouse_Adapter());

		JScrollPane scrollPane = new JScrollPane(conditionsTable_WPQC);
		conditionsTable_WPQC.setFillsViewportHeight(true);

		return scrollPane;
	}

	private void createCondition_WPQC() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		String operator = null;
		if(con_LO_Selected_CB_WPQC != null && con_RO_Selected_CB_WPQC != null
				&& cBoxOperator_WPQC.getSelectedIndex() != -1) {
			if(con_LO_Selected_CB_WPQC.equals("WPQC_CBX1")) {
				leftOperand = (String) cBoxLeft_WPQC.getSelectedItem();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Left Operand.");
					cBoxLeft_WPQC.requestFocus();
					return;
				}
			} else if(con_LO_Selected_CB_WPQC.equals("WPQC_CBX2")) {
				leftOperand = leftOperandTF_WPQC.getText();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Left Operand.");
					leftOperandTF_WPQC.requestFocus();
					return;
				}
			}

			if(con_RO_Selected_CB_WPQC.equals("WPQC_CBX3")) {
				rightOperand = (String) cBoxRight_WPQC.getSelectedItem();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Right Operand.");
					cBoxRight_WPQC.requestFocus();
					return;
				}
			} else if(con_RO_Selected_CB_WPQC.equals("WPQC_CBX4")) {
				rightOperand = rightOperandTF_WPQC.getText();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Right Operand.");
					rightOperandTF_WPQC.requestFocus();
					return;
				}
			}
			operator = (String) cBoxOperator_WPQC.getSelectedItem();
			if(!(operator != null && operator.length() > 0)) {
				JOptionPane.showMessageDialog(this, "Select a valid value from \'Operator\' drop down.");
				cBoxOperator_WPQC.requestFocus();
				return;
			}

			conDef = getConditionDefinition(leftOperand, operator, rightOperand, superConditionCBX_WPQC.isSelected(), null);
			conditionsTable_WPQC.addRow(new Object[]{conDef.getName(),
				conDef.getLhsOperand(), conDef.getOperator(), conDef.getRhsOperand(), conDef.isSuperCondition()});

			populateConditions();
			clearConditionFields_WPQC();

			conTableRowNumber_WPQC = -1;
			conditionName_WPQC = null;

			con_LO_Selected_CB_WPQC = null;
			con_RO_Selected_CB_WPQC = null;
			cBoxOperator_WPQC.setSelectedIndex(-1);
		} else {
			JOptionPane.showMessageDialog(this, "Left Operand, Right Operand and Operator are mandatory fields.");
		}
	}

	private void clearConditionFields_WPQC() {
		leftConditionBGroup_WPQC.remove(leftExistingCondition_WPQC);
		leftExistingCondition_WPQC.setSelected(false);
		leftConditionBGroup_WPQC.add(leftExistingCondition_WPQC);

		cBoxLeft_WPQC.setSelectedIndex(-1);

		leftConditionBGroup_WPQC.remove(leftNewCondition_WPQC);
		leftNewCondition_WPQC.setSelected(false);
		leftConditionBGroup_WPQC.add(leftNewCondition_WPQC);

		leftOperandTF_WPQC.setText(null);

		cBoxLeft_WPQC.setEnabled(false);
		leftOperandTF_WPQC.setEnabled(false);

		rightConditionBGroup_WPQC.remove(rightExistingCondition_WPQC);
		rightExistingCondition_WPQC.setSelected(false);
		rightConditionBGroup_WPQC.add(rightExistingCondition_WPQC);

		cBoxRight_WPQC.setSelectedIndex(-1);

		rightConditionBGroup_WPQC.remove(rightNewCondition_WPQC);
		rightNewCondition_WPQC.setSelected(false);
		rightConditionBGroup_WPQC.add(rightNewCondition_WPQC);

		rightOperandTF_WPQC.setText(null);

		cBoxRight_WPQC.setEnabled(false);
		rightOperandTF_WPQC.setEnabled(false);

		cBoxOperator_WPQC.setSelectedIndex(-1);
		superConditionCBX_WPQC.setSelected(false);

		this.repaint();
	}

	private void updateCondition_WPQC() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		if(con_LO_Selected_CB_WPQC != null && con_RO_Selected_CB_WPQC != null) {
			if(con_LO_Selected_CB_WPQC.equals("WPQC_CBX1")) {
				leftOperand = (String) cBoxLeft_WPQC.getSelectedItem();
			} else if(con_LO_Selected_CB_WPQC.equals("WPQC_CBX2")) {
				leftOperand = leftOperandTF_WPQC.getText();
			}

			if(con_RO_Selected_CB_WPQC.equals("WPQC_CBX3")) {
				rightOperand = (String) cBoxRight_WPQC.getSelectedItem();
			} else if(con_RO_Selected_CB_WPQC.equals("WPQC_CBX4")) {
				rightOperand = rightOperandTF_WPQC.getText();
			}
			conDef = getConditionDefinition(leftOperand, (String) cBoxOperator_WPQC.getSelectedItem(),
				rightOperand, superConditionCBX_WPQC.isSelected(), conditionName_WPQC);

			conditionsTable_WPQC.getModel().setValueAt(conDef.getLhsOperand(), conTableRowNumber_WPQC, 1);
			conditionsTable_WPQC.getModel().setValueAt(conDef.getOperator(), conTableRowNumber_WPQC, 2);
			conditionsTable_WPQC.getModel().setValueAt(conDef.getRhsOperand(), conTableRowNumber_WPQC, 3);
			conditionsTable_WPQC.getModel().setValueAt(conDef.isSuperCondition(), conTableRowNumber_WPQC, 4);

			populateConditions();
			clearConditionFields_WPQC();
			bCreate_WPQC.setEnabled(true);

			conTableRowNumber_WPQC = -1;
			conditionName_WPQC = null;

			updateEnableStateOfUpdateAndDeleteButtons_WPQC();
		}
	}

	private void deleteCondition_WPQC() {
		conditionCache.remove(conditionName_WPQC);
		((DefaultTableModel) conditionsTable_WPQC.getModel()).removeRow(conTableRowNumber_WPQC);

		populateConditions();
		clearConditionFields_WPQC();
		bCreate_WPQC.setEnabled(true);

		conTableRowNumber_WPQC = -1;
		conditionName_WPQC = null;

		updateEnableStateOfUpdateAndDeleteButtons_WPQC();
	}

	private void clearCondition_WPQC() {
		populateConditions();
		clearConditionFields_WPQC();
		bCreate_WPQC.setEnabled(true);

		conTableRowNumber_WPQC = -1;
		conditionName_WPQC = null;

		updateEnableStateOfUpdateAndDeleteButtons_WPQC();
	}

	private void populateConditionFields_WPQC() {
		Condition condition = conditionCache.get(conditionName_WPQC);
		clearConditionFields_WPQC();
		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getLeftSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition_WPQC.setSelected(true);
				cBoxLeft_WPQC.setEnabled(true);
				cBoxLeft_WPQC.setSelectedItem(((Condition) condition.getLeftSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getLeftSideValue()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition_WPQC.setSelected(true);
				cBoxLeft_WPQC.setEnabled(true);
				cBoxLeft_WPQC.setSelectedItem(condition.getLeftSideValue());
			} else {
				leftNewCondition_WPQC.setSelected(true);
				leftOperandTF_WPQC.setEnabled(true);
				leftOperandTF_WPQC.setText((String) condition.getLeftSideValue());
			}
		}

		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getRightSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition_WPQC.setSelected(true);
				cBoxRight_WPQC.setEnabled(true);
				cBoxRight_WPQC.setSelectedItem(((Condition) condition.getRightSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getRightSideValue()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition_WPQC.setSelected(true);
				cBoxRight_WPQC.setEnabled(true);
				cBoxRight_WPQC.setSelectedItem(condition.getRightSideValue());
			} else {
				rightNewCondition_WPQC.setSelected(true);
				rightOperandTF_WPQC.setEnabled(true);
				rightOperandTF_WPQC.setText((String) condition.getRightSideValue());
			}
		}

		cBoxOperator_WPQC.setSelectedItem(condition.getOperator().toString());
		superConditionCBX_WPQC.setSelected(condition.isSuperCondition());
		bCreate_WPQC.setEnabled(false);
	}
	//WebPage - Qualifying Conditions - End

	//CodeBlock - Qualifying Conditions - Start
	private JPanel getCBQualifyingConditionsPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(getCRUDConditionPanel_CBQualCon(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.02,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
		return mainPanel;
	}

	private JPanel getCRUDConditionPanel_CBQualCon() {
		JPanel crudConPanel = new JPanel();
		crudConPanel.setLayout(new GridBagLayout());

		//left operand panel - start
		JPanel leftOperandPanel = new JPanel();
		leftOperandPanel.setLayout(new GridBagLayout());

		leftExistingCondition_CBQC = new PopCheckBox("Use Existing Condition");
		leftExistingCondition_CBQC.setName("CBQC_CBX1");
		leftExistingCondition_CBQC.addItemListener(new CBQC_Con_CBX_Left_Listener());

		leftNewCondition_CBQC = new PopCheckBox("Other String");
		leftNewCondition_CBQC.setName("CBQC_CBX2");
		leftNewCondition_CBQC.addItemListener(new CBQC_Con_CBX_Left_Listener());

		leftConditionBGroup_CBQC = new ButtonGroup();
		leftConditionBGroup_CBQC.add(leftExistingCondition_CBQC);
		leftConditionBGroup_CBQC.add(leftNewCondition_CBQC);

		cBoxLeft_CBQC = new PopComboBox();
		cBoxLeft_CBQC.setEnabled(false);
		leftOperandTF_CBQC = new PopTextField();
		leftOperandTF_CBQC.setEnabled(false);

		leftOperandPanel.add(leftExistingCondition_CBQC, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(cBoxLeft_CBQC, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftNewCondition_CBQC, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		leftOperandPanel.add(leftOperandTF_CBQC, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
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

		rightExistingCondition_CBQC = new PopCheckBox("Use Existing Condition");
		rightExistingCondition_CBQC.setName("CBQC_CBX3");
		rightExistingCondition_CBQC.addItemListener(new CBQC_Con_CBX_Right_Listener());

		rightNewCondition_CBQC = new PopCheckBox("Other String");
		rightNewCondition_CBQC.setName("CBQC_CBX4");
		rightNewCondition_CBQC.addItemListener(new CBQC_Con_CBX_Right_Listener());

		rightConditionBGroup_CBQC = new ButtonGroup();
		rightConditionBGroup_CBQC.add(rightExistingCondition_CBQC);
		rightConditionBGroup_CBQC.add(rightNewCondition_CBQC);

		cBoxRight_CBQC = new PopComboBox();
		cBoxRight_CBQC.setEnabled(false);
		rightOperandTF_CBQC = new PopTextField();
		rightOperandTF_CBQC.setEnabled(false);

		rightOperandPanel.add(rightExistingCondition_CBQC, new GridBagConstraints(0, 0, 1, 1, 0.05, 1.0,
			GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(cBoxRight_CBQC, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightNewCondition_CBQC, new GridBagConstraints(2, 0, 1, 1, 0.05, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		rightOperandPanel.add(rightOperandTF_CBQC, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
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
		cBoxOperator_CBQC = new PopComboBox();
		populateOperator(cBoxOperator_CBQC);

		superConditionCBX_CBQC = new PopCheckBox("Super Condition");

		operatorPanel.add(labelOperator, new GridBagConstraints(0, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(cBoxOperator_CBQC, new GridBagConstraints(1, 0, 1, 1, 0.1, 1.0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		operatorPanel.add(superConditionCBX_CBQC, new GridBagConstraints(2, 0, 1, 1, 0.1, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		crudConPanel.add(operatorPanel, new GridBagConstraints(0, 2, 1, 1, 0.5, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));

		crudConPanel.add(getConditionTablePanel_CBQC(), new GridBagConstraints(0, 3, 2, 2, 1.0, 1.0,
		GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));
		//operator - end

		//control buttons - start
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		JPanel invisiblePanel = new JPanel();

		bCreate_CBQC = new PopButton("Create");
		bUpdate_CBQC = new PopButton("Update");
		bDelete_CBQC = new PopButton("Delete");
		bClear_CBQC = new PopButton("Reset");

		buttonPanel.add(invisiblePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bCreate_CBQC.setName("CBQC_B_Create");
		bCreate_CBQC.addActionListener(new CBQC_Con_Button_Action_Listener());
		buttonPanel.add(bCreate_CBQC, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bUpdate_CBQC.setName("CBQC_B_Update");
		bUpdate_CBQC.addActionListener(new CBQC_Con_Button_Action_Listener());
		buttonPanel.add(bUpdate_CBQC, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		bDelete_CBQC.setName("CBQC_B_Delete");
		bDelete_CBQC.addActionListener(new CBQC_Con_Button_Action_Listener());
		buttonPanel.add(bDelete_CBQC, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		bClear_CBQC.setName("CBQC_B_Clear");
		bClear_CBQC.addActionListener(new CBQC_Con_Button_Action_Listener());
		buttonPanel.add(bClear_CBQC, new GridBagConstraints(4, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		crudConPanel.add(buttonPanel, new GridBagConstraints(1, 2, 1, 1, 1.0, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 8, 0), 0, 0));

		updateEnableStateOfUpdateAndDeleteButtons_CBQC();
		//control buttons - end

		PopTitledBorder titledBorder = new PopTitledBorder("Create / Edit / Delete Conditions");
		crudConPanel.setBorder(titledBorder);

		populateConditions();

		return crudConPanel;
	}

	private void updateEnableStateOfUpdateAndDeleteButtons_CBQC() {
		if((conTableRowNumber_CBQC > -1) && (conditionName_CBQC != null)) {
			bUpdate_CBQC.setEnabled(true);
			bDelete_CBQC.setEnabled(true);
		} else {
			bUpdate_CBQC.setEnabled(false);
			bDelete_CBQC.setEnabled(false);
		}
	}

	private JScrollPane getConditionTablePanel_CBQC() {
		Object[] columnNames = {"Condition Name", "Left Operand", "Operator", "Right Operand", "Super Condition"};
		Object[][] rowData = new Object[0][0];
		conditionsTable_CBQC = new PopTable(rowData, columnNames);
		conditionsTable_CBQC.setPreferredScrollableViewportSize(new Dimension(200, 200));
		conditionsTable_CBQC.addMouseListener(new CBQC_Condition_Table_Mouse_Adapter());

		JScrollPane scrollPane = new JScrollPane(conditionsTable_CBQC);
		conditionsTable_CBQC.setFillsViewportHeight(true);

		return scrollPane;
	}

	private void createCondition_CBQC() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		String operator = null;
		if(con_LO_Selected_CB_CBQC != null && con_RO_Selected_CB_CBQC != null
				&& cBoxOperator_CBQC.getSelectedIndex() != -1) {
			if(con_LO_Selected_CB_CBQC.equals("CBQC_CBX1")) {
				leftOperand = (String) cBoxLeft_CBQC.getSelectedItem();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Left Operand.");
					cBoxLeft_CBQC.requestFocus();
					return;
				}
			} else if(con_LO_Selected_CB_CBQC.equals("CBQC_CBX2")) {
				leftOperand = leftOperandTF_CBQC.getText();
				if(!(leftOperand != null && leftOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Left Operand.");
					leftOperandTF_CBQC.requestFocus();
					return;
				}
			}

			if(con_RO_Selected_CB_CBQC.equals("CBQC_CBX3")) {
				rightOperand = (String) cBoxRight_CBQC.getSelectedItem();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Select a valid value from \'Use Existing Condition\' drop down in Right Operand.");
					cBoxRight_CBQC.requestFocus();
					return;
				}
			} else if(con_RO_Selected_CB_CBQC.equals("CBQC_CBX4")) {
				rightOperand = rightOperandTF_CBQC.getText();
				if(!(rightOperand != null && rightOperand.length() > 0)) {
					JOptionPane.showMessageDialog(this, "Enter a valid value in \'Other String\' text field of Right Operand.");
					rightOperandTF_CBQC.requestFocus();
					return;
				}
			}
			operator = (String) cBoxOperator_CBQC.getSelectedItem();
			if(!(operator != null && operator.length() > 0)) {
				JOptionPane.showMessageDialog(this, "Select a valid value from \'Operator\' drop down.");
				cBoxOperator_CBQC.requestFocus();
				return;
			}

			conDef = getConditionDefinition(leftOperand, operator, rightOperand, superConditionCBX_CBQC.isSelected(), null);
			conditionsTable_CBQC.addRow(new Object[]{conDef.getName(),
				conDef.getLhsOperand(), conDef.getOperator(), conDef.getRhsOperand(), conDef.isSuperCondition()});

			populateConditions();
			clearConditionFields_CBQC();

			conTableRowNumber_CBQC = -1;
			conditionName_CBQC = null;

			con_LO_Selected_CB_CBQC = null;
			con_RO_Selected_CB_CBQC = null;
			cBoxOperator_CBQC.setSelectedIndex(-1);
		} else {
			JOptionPane.showMessageDialog(this, "Left Operand, Right Operand and Operator are mandatory fields.");
		}
	}

	private void clearConditionFields_CBQC() {
		leftConditionBGroup_CBQC.remove(leftExistingCondition_CBQC);
		leftExistingCondition_CBQC.setSelected(false);
		leftConditionBGroup_CBQC.add(leftExistingCondition_CBQC);

		cBoxLeft_CBQC.setSelectedIndex(-1);

		leftConditionBGroup_CBQC.remove(leftNewCondition_CBQC);
		leftNewCondition_CBQC.setSelected(false);
		leftConditionBGroup_CBQC.add(leftNewCondition_CBQC);

		leftOperandTF_CBQC.setText(null);

		cBoxLeft_CBQC.setEnabled(false);
		leftOperandTF_CBQC.setEnabled(false);

		rightConditionBGroup_CBQC.remove(rightExistingCondition_CBQC);
		rightExistingCondition_CBQC.setSelected(false);
		rightConditionBGroup_CBQC.add(rightExistingCondition_CBQC);

		cBoxRight_CBQC.setSelectedIndex(-1);

		rightConditionBGroup_CBQC.remove(rightNewCondition_CBQC);
		rightNewCondition_CBQC.setSelected(false);
		rightConditionBGroup_CBQC.add(rightNewCondition_CBQC);

		rightOperandTF_CBQC.setText(null);

		cBoxRight_CBQC.setEnabled(false);
		rightOperandTF_CBQC.setEnabled(false);

		cBoxOperator_CBQC.setSelectedIndex(-1);
		superConditionCBX_CBQC.setSelected(false);

		this.repaint();
	}

	private void updateCondition_CBQC() {
		ConditionDefinition conDef = null;
		String leftOperand = null;
		String rightOperand = null;
		if(con_LO_Selected_CB_CBQC != null && con_RO_Selected_CB_CBQC != null) {
			if(con_LO_Selected_CB_CBQC.equals("CBQC_CBX1")) {
				leftOperand = (String) cBoxLeft_CBQC.getSelectedItem();
			} else if(con_LO_Selected_CB_CBQC.equals("CBQC_CBX2")) {
				leftOperand = leftOperandTF_CBQC.getText();
			}

			if(con_RO_Selected_CB_CBQC.equals("CBQC_CBX3")) {
				rightOperand = (String) cBoxRight_CBQC.getSelectedItem();
			} else if(con_RO_Selected_CB_CBQC.equals("CBQC_CBX4")) {
				rightOperand = rightOperandTF_CBQC.getText();
			}
			conDef = getConditionDefinition(leftOperand, (String) cBoxOperator_CBQC.getSelectedItem(),
				rightOperand, superConditionCBX_CBQC.isSelected(), conditionName_CBQC);

			conditionsTable_CBQC.getModel().setValueAt(conDef.getLhsOperand(), conTableRowNumber_CBQC, 1);
			conditionsTable_CBQC.getModel().setValueAt(conDef.getOperator(), conTableRowNumber_CBQC, 2);
			conditionsTable_CBQC.getModel().setValueAt(conDef.getRhsOperand(), conTableRowNumber_CBQC, 3);
			conditionsTable_CBQC.getModel().setValueAt(conDef.isSuperCondition(), conTableRowNumber_CBQC, 4);

			populateConditions();
			clearConditionFields_CBQC();
			bCreate_CBQC.setEnabled(true);

			conTableRowNumber_CBQC = -1;
			conditionName_CBQC = null;

			updateEnableStateOfUpdateAndDeleteButtons_CBQC();
		}
	}

	private void deleteCondition_CBQC() {
		conditionCache.remove(conditionName_CBQC);

		((DefaultTableModel) conditionsTable_CBQC.getModel()).removeRow(conTableRowNumber_CBQC);

		populateConditions();
		clearConditionFields_CBQC();
		bCreate_CBQC.setEnabled(true);

		conTableRowNumber_CBQC = -1;
		conditionName_CBQC = null;

		updateEnableStateOfUpdateAndDeleteButtons_CBQC();
	}

	private void clearCondition_CBQC() {
		populateConditions();
		clearConditionFields_CBQC();
		bCreate_CBQC.setEnabled(true);

		conTableRowNumber_CBQC = -1;
		conditionName_CBQC = null;

		updateEnableStateOfUpdateAndDeleteButtons_CBQC();
	}

	private void populateConditionFields_CBQC() {
		Condition condition = conditionCache.get(conditionName_CBQC);
		clearConditionFields_CBQC();
		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getLeftSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition_CBQC.setSelected(true);
				cBoxLeft_CBQC.setEnabled(true);
				cBoxLeft_CBQC.setSelectedItem(((Condition) condition.getLeftSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getLeftSideValue()).startsWith("pop:crwl:cons:")) {
				leftExistingCondition_CBQC.setSelected(true);
				cBoxLeft_CBQC.setEnabled(true);
				cBoxLeft_CBQC.setSelectedItem(condition.getLeftSideValue());
			} else {
				leftNewCondition_CBQC.setSelected(true);
				leftOperandTF_CBQC.setEnabled(true);
				leftOperandTF_CBQC.setText((String) condition.getLeftSideValue());
			}
		}

		if(condition.getLeftSideValue() instanceof Condition) {
			if((((Condition) condition.getRightSideValue()).getName()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition_CBQC.setSelected(true);
				cBoxRight_CBQC.setEnabled(true);
				cBoxRight_CBQC.setSelectedItem(((Condition) condition.getRightSideValue()).getName());
			}
		} else if(condition.getLeftSideValue() instanceof String) {
			if(((String) condition.getRightSideValue()).startsWith("pop:crwl:cons:")) {
				rightExistingCondition_CBQC.setSelected(true);
				cBoxRight_CBQC.setEnabled(true);
				cBoxRight_CBQC.setSelectedItem(condition.getRightSideValue());
			} else {
				rightNewCondition_CBQC.setSelected(true);
				rightOperandTF_CBQC.setEnabled(true);
				rightOperandTF_CBQC.setText((String) condition.getRightSideValue());
			}
		}

		cBoxOperator_CBQC.setSelectedItem(condition.getOperator().toString());
		superConditionCBX_CBQC.setSelected(condition.isSuperCondition());
		bCreate_CBQC.setEnabled(false);
	}
	//CodeBlock - Qualifying Conditions - End

	//WebPage - Config - Step 3 - Start
	private JPanel getWebPageConfigStepThreePanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		JPanel resultSetGroupPanel = new JPanel();
		resultSetGroupPanel.setLayout(new GridBagLayout());
		PopTitledBorder tBorderResultSet = new PopTitledBorder("Result Set Group");
		resultSetGroupPanel.setBorder(tBorderResultSet);

		PopLabel startTag = new PopLabel("Start Tag");
		dGroupStartTagTF = new PopTextField();
		dGroupStartTagTF.setName("TF_DataGroup_StartTag");
		resultSetGroupPanel.add(startTag, new GridBagConstraints(0, 0, 1, 1, 0.02, 1.0,
			GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
		resultSetGroupPanel.add(dGroupStartTagTF, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.5,
			GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 10), 0, 5));

		PopLabel endTag = new PopLabel("End Tag");
		dGroupEndTagTF = new PopTextField();
		dGroupEndTagTF.setName("TF_DataGroup_EndTag");
		resultSetGroupPanel.add(endTag, new GridBagConstraints(2, 0, 1, 1, 0.02, 1.0,
			GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
		resultSetGroupPanel.add(dGroupEndTagTF, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.5,
			GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 5));

		mainPanel.add(resultSetGroupPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.01,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		JPanel resultSetPanel = new JPanel();
		resultSetPanel.setLayout(new GridBagLayout());
		PopTitledBorder tBorder = new PopTitledBorder("Result Set Configuration");
		resultSetPanel.setBorder(tBorder);
		resultSetTabs = new PopTabbedPane();

		/*
		 * To support Organization crawlers, we will call 'populateTabsForOrgExtraction()' method. Based on the data type we need to
		 * call either populateTabsForBidExtraction() or populateTabsForOrgExtraction().
		 */
		populateTabsForBidExtraction();

		resultSetPanel.add(resultSetTabs, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		mainPanel.add(resultSetPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		JPanel buttonsPanell = new JPanel();
		buttonsPanell.setLayout(new GridBagLayout());

		b_Previous_Config_Step_3 = new PopButton("Previous");
		b_Previous_Config_Step_3.setName("B_WebPage_Step_Three_Previous");
		b_Previous_Config_Step_3.addActionListener(new WebPageConfig_3_Button_Action_Listener());

		b_Next_Config_Step_3 = new PopButton("Done");
		b_Next_Config_Step_3.setName("B_WebPage_Step_Three_Next");
		b_Next_Config_Step_3.addActionListener(new WebPageConfig_3_Button_Action_Listener());

		b_Cancel_Config_Step_3 = new PopButton("Cancel");
		b_Cancel_Config_Step_3.setName("B_WebPage_Step_Three_Cancel");
		b_Cancel_Config_Step_3.addActionListener(new WebPageConfig_3_Button_Action_Listener());

		JPanel invisiblePanell = new JPanel();

		buttonsPanell.add(invisiblePanell, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		buttonsPanell.add(b_Previous_Config_Step_3, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		buttonsPanell.add(b_Next_Config_Step_3, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		buttonsPanell.add(b_Cancel_Config_Step_3, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		mainPanel.add(buttonsPanell, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.1,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(8, 0, 0, 0), 0, 0));

		return mainPanel;
	}

	private void populateTabsForBidExtraction() {
		List<TableDefConfig> tableDefs = configCache.getConfigurations().getTableDefsForBidCrawlers();
		TableDefConfig tableDef = null;
		int noOfTables = tableDefs.size();
		dataTables = new PopTable[noOfTables];
		for(int tableIndex = 0; tableIndex < noOfTables; tableIndex ++) {
			tableDef = tableDefs.get(tableIndex);
			if(tableDef.isDisplayInUI()) {
				resultSetTabs.addTab(tableDef.getDisplayName(), getTablePanel(tableDef, tableIndex));
			} else {
				getTablePanel(tableDef, tableIndex);
			}
		}
	}
	
	private JPanel getTablePanel(TableDefConfig tableDef, int tableIndex) {
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridBagLayout());
		PopTable table = new PopTable(resultSetRowData, resultSetColumnNames, "ResultSet");
		table.setName(tableDef.getName());
		tablePanel.add(getResultTableScrollPane(table), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		populateTable(table, tableDef);
		dataTables[tableIndex] = table;
		return tablePanel;
	}
	
	private void populateTable(PopTable table, TableDefConfig tableDef) {
		List<ColumnDefConfig> columnDefs = tableDef.getColumnDefinition();
		DatumDefinition datum = null;
		for(ColumnDefConfig columnDef : columnDefs) {
			datum = new DatumDefinition(columnDef.getName(), columnDef.getDataType(), columnDef.getVariableName(), Constants.HTML);
			datumCache.put(datum.getUniqueName(), datum);
			table.addRow(datum.toArray());
		}
	}

	private JScrollPane getResultTableScrollPane(PopTable tableName) {
		tableName.setPreferredScrollableViewportSize(new Dimension(200, 200));
		tableName.setColumnSelectionAllowed(true);
//		tableName.getColumnModel().getColumn(0).setCellRenderer(new ResultSetTableCheckBoxCell());
//		tableName.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new PopCheckBox()));
		tableName.getColumnModel().getColumn(7).setCellEditor(new ResultSetTableCellComboBox(new String[]{"Yes", "No"}));
		tableName.getColumnModel().getColumn(8).setCellEditor(new ResultSetTableCellComboBox(new String[]{"Yes", "No"}));

		JScrollPane scrollPane = new JScrollPane(tableName);
		tableName.setFillsViewportHeight(true);

		return scrollPane;
	}
	//WebPage - Config - Step 3 - End

	//WebPage - Config - Step 2 - Start
	private JPanel getWebPageConfigStepTwoPanel() {
		//JPanel codePanel = new JPanel();
		codePanel = new JPanel();
		codePanel.setLayout(new GridBagLayout());

		//PopTreeNode treeNode = new PopTreeNode("Start", PopTreeNodeType.None);
		tree = new PopTree();
		tree.setContainingFrame(this);
		popTreeScrollPane = new JScrollPane(tree);

		codePanel.add(popTreeScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		buttonsPanell = new JPanel();
		buttonsPanell.setLayout(new GridBagLayout());

		b_Previous_Config_Step_2 = new PopButton("Previous");
		b_Previous_Config_Step_2.setName("B_WebPage_Step_Two_Previous");
		b_Previous_Config_Step_2.addActionListener(new WebPageConfig_2_Button_Action_Listener());

		//Done will be renamed as Next in next version, functionality of Done will be used by the 'Done' button in step 3
		b_Next_Config_Step_2 = new PopButton("Done");
		updateDoneButtonText();
		b_Next_Config_Step_2.setName("B_WebPage_Step_Two_Next");
		b_Next_Config_Step_2.addActionListener(new WebPageConfig_2_Button_Action_Listener());

		b_Cancel_Config_Step_2 = new PopButton("Cancel");
		b_Cancel_Config_Step_2.setName("B_WebPage_Step_Two_Cancel");
		b_Cancel_Config_Step_2.addActionListener(new WebPageConfig_2_Button_Action_Listener());

		JPanel invisiblePanell = new JPanel();

		buttonsPanell.add(invisiblePanell, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		buttonsPanell.add(b_Previous_Config_Step_2, new GridBagConstraints(1, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		buttonsPanell.add(b_Next_Config_Step_2, new GridBagConstraints(2, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		buttonsPanell.add(b_Cancel_Config_Step_2, new GridBagConstraints(3, 0, 1, 1, 0.01, 1.0,
			GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		codePanel.add(buttonsPanell, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.1,
			GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));

		return codePanel;
	}

	private void updateDoneButtonText() {
		if(sourceIDTF != null) {
			String sourceID = sourceIDTF.getText();
			CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
			if(crawlerDef != null) {
				WebPages webPages = crawlerDef.getWebPages();
				if(webPages != null) {
					WebPageDefinition webPageDef = webPages.getWebPage(uniqueNameOfWebPage);
					if(webPageDef != null) {
						if(webPageDef.getType().equals("Result")) {
							b_Next_Config_Step_2.setText("Next");
						}
					}
				}
			}
		}
	}

	private void wpConfig_Step_2() {
		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
		WebPageDefinition webPageDef = crawlerDef.getWebPage(uniqueNameOfWebPage);
		if(! webPageDef.isValidWebPageDefinition()) {
			if(webPageDef.getType().equals("ResultLinks")) {
				JOptionPane.showMessageDialog(this, "A Result Links Web Flow should invoke CollectResultLinks function at least once.");
			} else if(webPageDef.getType().equals("Result")) {
				JOptionPane.showMessageDialog(this, "A Result Web Flow should invoke ParseResults function at least once.");
			}
			return;
		}

		if(webPageDef.getType().equals("Result")) {
			mainTabPane.setEnabledAt(0, false);
			mainTabPane.setEnabledAt(1, false);
			mainTabPane.setEnabledAt(2, false);
			mainTabPane.setEnabledAt(3, false);
			mainTabPane.setEnabledAt(4, false);
			mainTabPane.setEnabledAt(5, false);
			mainTabPane.setEnabledAt(6, true);
			if(webPageDef.getDataGroups().size() > 0) {
				resetConfigWebFlowStep_Three();
				updateDataGroups(webPageDef.getDataGroups());
			}
			mainTabPane.setSelectedIndex(6);
		} else {
			mainTabPane.setEnabledAt(0, true);
			mainTabPane.setEnabledAt(1, true);
			mainTabPane.setEnabledAt(2, true);
			mainTabPane.setEnabledAt(3, true);
			mainTabPane.setEnabledAt(4, false);
			mainTabPane.setEnabledAt(5, false);
			mainTabPane.setEnabledAt(6, false);
			addOrUpdateCurrentWebFlowToWebPagesTable();
			resetConfigWebFlow();
			sameWebPage = false;
			editWebFlow = false;
			webPagesTable.clearSelection();
			mainTabPane.setSelectedIndex(3);
		}
	}

	private void updateDataGroups(List<DataGroupDefinition> dataGroupDefs) {
		DataGroupDefinition dataGroupDef = dataGroupDefs.get(0);
		dGroupStartTagTF.setText(dataGroupDef.getStartTag());
		dGroupEndTagTF.setText(dataGroupDef.getEndTag());

		List<DataSetDefinition> dataSets = dataGroupDef.getDataSets();
		for(DataSetDefinition dataSet : dataSets) {
			populateDataSetTable(getPopTableByName(dataSet.getTableName()), dataSet);
		}
	}
	
	private PopTable getPopTableByName(String tableName) {
		if(dataTables != null) {
			for(PopTable table : dataTables) {
				if(table.getName().equals(tableName)) {
					return table;
				}
			}
		}
		return null;
	}

	private void populateDataSetTable(PopTable table, DataSetDefinition dataSet) {
		List<DatumDefinition> datumDefs = dataSet.getData();
		for(DatumDefinition datumDef : datumDefs) {
			if(datumDef.isConfigurable()) {
				table.addRow(datumDef.toArray());
			}
		}
	}

	private void wpConfig_Step_3() {
		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
		WebPageDefinition webPageDef = crawlerDef.getWebPage(uniqueNameOfWebPage);
		boolean validation = true;
		if(webPageDef.getType().equals("Result")) {
			DataGroupDefinition dataGroupDef = new DataGroupDefinition();
			dataGroupDef.setStartTag(dGroupStartTagTF.getText());
			dataGroupDef.setEndTag(dGroupEndTagTF.getText());
			if(dataTables != null) {
				for(PopTable table : dataTables) {
					if(validation) {
						if(! updateDataSet(dataGroupDef, table)) {
							validation = false;
						}
					}
				}
				if(validation) {
					webPageDef.replaceDataGroup(dataGroupDef);
				}
			}
		}
		if(validation) {
			mainTabPane.setEnabledAt(0, true);
			mainTabPane.setEnabledAt(1, true);
			mainTabPane.setEnabledAt(2, true);
			mainTabPane.setEnabledAt(3, true);
			mainTabPane.setEnabledAt(4, false);
			mainTabPane.setEnabledAt(5, false);
			mainTabPane.setEnabledAt(6, false);

			addOrUpdateCurrentWebFlowToWebPagesTable();
			resetConfigWebFlow();
			sameWebPage = false;
			editWebFlow = false;

			mainTabPane.setSelectedIndex(3);
		}
	}

	private boolean updateDataSet(DataGroupDefinition dataGroupDef, PopTable table) {
		DataSetDefinition dataSet = new DataSetDefinition(table.getName(), null);
		boolean validation = populateDataSet(table, dataSet, 0);
		if(table.getName().equals(Constants.POP_BID_TABLE)) {
			populateAdditionalDataForBidTable(dataSet);
		}
		TableDefConfig tableDef = configCache.getConfigurations().getTableDefByName(table.getName());
		List<ColumnDefConfig> defaultColVals = tableDef.getDefaultValueColumns();
		for(ColumnDefConfig colDef : defaultColVals) {
			dataSet.addAdditionalData(colDef.getName(), colDef.getDefaultValue(), colDef.isPassByValue());
		}
		dataGroupDef.addDataSet(dataSet);
		return validation;
	}
	
	private void populateAdditionalDataForBidTable(DataSetDefinition dataSet) {
		dataSet.addDatum(new DatumDefinition(DatumIdentifier.ProposalType));
		dataSet.addDatum(new DatumDefinition(DatumIdentifier.SourceURL));
		dataSet.addDatum(new DatumDefinition(DatumIdentifier.SourceID));
		dataSet.addDatum(new DatumDefinition(DatumIdentifier.SourceName));
		dataSet.addDatum(new DatumDefinition(DatumIdentifier.SiteName));
		dataSet.addDatum(new DatumDefinition(DatumIdentifier.ScanID));
	}

	private boolean populateDataSet(PopTable table, DataSetDefinition dataSetDef, int tabIndex) {
		DatumDefinition datumDef = null;
		String fieldID = null;
		String fieldName = null;
		String dataType = null;
		String variableName = null;
		String htmlSource = null;
		String startTag = null;
		String endTag = null;
		boolean entireString = false;
		boolean afterTag = false;
		int noOfRows = table.getDefaultModel().getRowCount();
		for(int rowIndex = 0; rowIndex < noOfRows; rowIndex ++) {
			fieldID = (String) table.getDefaultModel().getValueAt(rowIndex, 0);
			fieldName = (String) table.getDefaultModel().getValueAt(rowIndex, 1);
			dataType = (String) table.getDefaultModel().getValueAt(rowIndex, 2);
			variableName = (String) table.getDefaultModel().getValueAt(rowIndex, 3);
			if(! (variableName != null && variableName.length() > 0)) {
				JOptionPane.showMessageDialog(this, "Variable Name is mandatory.");
				table.changeSelection(rowIndex, 3, false, false);
				resultSetTabs.setSelectedIndex(tabIndex);
				return false;
			}
			htmlSource = (String) table.getDefaultModel().getValueAt(rowIndex, 4);
			if(htmlSource != null && htmlSource.length() > 0) {
				if(htmlSource.contains("pop:crwl:datum:name:")) {
					htmlSource = datumCache.get(htmlSource).getFieldName();
				}
			} else {
				JOptionPane.showMessageDialog(this, "HTML Source is mandatory.");
				table.changeSelection(rowIndex, 4, false, false);
				resultSetTabs.setSelectedIndex(tabIndex);
				return false;
			}

			startTag = (String) table.getDefaultModel().getValueAt(rowIndex, 5);
			endTag = (String) table.getDefaultModel().getValueAt(rowIndex, 6);
			entireString = ((String) table.getDefaultModel().getValueAt(rowIndex, 7)) == "Yes" ? true : false;
			afterTag = ((String) table.getDefaultModel().getValueAt(rowIndex, 8)) == "Yes" ? true : false;

			datumDef = datumCache.get(fieldID);
			if(datumDef == null) {
				datumDef = new DatumDefinition(fieldName, dataType, variableName, htmlSource);
			} else {
				datumDef.setFieldName(fieldName);
				datumDef.setDataType(dataType);
				datumDef.setVariableName(variableName);
				datumDef.setHtmlSource(htmlSource);
			}
			datumDef.setStartTag(startTag);
			datumDef.setEndTag(endTag);
			datumDef.setEntireString(entireString);
			datumDef.setAfterTag(afterTag);

			datumCache.put(fieldID, datumDef);

			dataSetDef.addDatum(datumDef);
		}
		return true;
	}

	private void cancelConfigWP_Step_2() {
		cancelConfigWP();
	}

	private void goToWPConfig_Step_1() {
		sameWebPage = true;
		mainTabPane.setEnabledAt(0, false);
		mainTabPane.setEnabledAt(1, false);
		mainTabPane.setEnabledAt(2, false);
		mainTabPane.setEnabledAt(3, false);
		mainTabPane.setEnabledAt(4, true);
		mainTabPane.setEnabledAt(5, false);
		mainTabPane.setEnabledAt(6, false);

		mainTabPane.setSelectedIndex(4);
	}

	private void goToWPConfig_Go_To_Step_2() {
		sameWebPage = true;
		mainTabPane.setEnabledAt(0, false);
		mainTabPane.setEnabledAt(1, false);
		mainTabPane.setEnabledAt(2, false);
		mainTabPane.setEnabledAt(3, false);
		mainTabPane.setEnabledAt(4, false);
		mainTabPane.setEnabledAt(5, true);
		mainTabPane.setEnabledAt(6, false);

		mainTabPane.setSelectedIndex(5);
	}

	private void addOrUpdateCurrentWebFlowToWebPagesTable() {
		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
		WebPageDefinition webPageDef = crawlerDef.getWebPage(uniqueNameOfWebPage);
		if(editWebFlow) {
			webPagesTable.setValueAt(webPageDef.getName(), editWebPageRowNumber, Constants.WEBPAGE_SUMMARY_TABLE_NAME);
			webPagesTable.setValueAt(webPageDef.getType(), editWebPageRowNumber, Constants.WEBPAGE_SUMMARY_TABLE_TYPE);
			webPagesTable.setValueAt(webPageDef.getBrowser(), editWebPageRowNumber, Constants.WEBPAGE_SUMMARY_TABLE_BROWSER_TO_EXECUTE);
			webPagesTable.setValueAt(webPageDef.getNavigationOrder(), editWebPageRowNumber, Constants.WEBPAGE_SUMMARY_TABLE_NAVIGATION_ORDER);
			webPagesTable.setValueAt(webPageDef.isExecuteOnce(), editWebPageRowNumber, Constants.WEBPAGE_SUMMARY_TABLE_EXECUTE_ONCE);
		} else {
			webPagesTable.addRow(new Object[] {webPageDef.getName(), webPageDef.getType(), webPageDef.getBrowser(),
				webPageDef.getNavigationOrder(), webPageDef.isExecuteOnce()});
		}
	}

	private void resetConfigWebFlow() {
		resetConfigWebFlowStep_One();
		resetConfigWebFlowStep_Three();
		resetConfigWebFlowStep_Two();
	}

	private void resetConfigWebFlowStep_One() {
		wpNameTF.setText("");
		wpTypeCMB.setSelectedIndex(-1);
		wpNavOrderTF.setText("");
		wpBrowserCMB.setSelectedIndex(-1);
		wpExecuteOnceCBX.setSelected(false);
		wpUrlTF.setText("");

		((DefaultTableModel) conditionsTable_WPQC.getModel()).getDataVector().removeAllElements();
		conditionsTable_WPQC.updateUI();

		((DefaultTableModel) conditionsTable_CBQC.getModel()).getDataVector().removeAllElements();
		conditionsTable_CBQC.updateUI();
	}

	private void resetConfigWebFlowStep_Two() {
		mainTabPane.remove(6);
		mainTabPane.remove(5);
		mainTabPane.add("Config Web Flow - Step 2", getWebPageConfigStepTwoPanel());
		mainTabPane.add("Config Web Flow - Step 3", getWebPageConfigStepThreePanel());
		mainTabPane.setEnabledAt(5, false);
		mainTabPane.setEnabledAt(6, false);
	}
	
	private void resetConfigWebFlowStep_Three() {
		if(dataTables != null) {
			for(PopTable table : dataTables) {
				((DefaultTableModel) table.getModel()).getDataVector().removeAllElements();
				table.updateUI();
			}
		}
	}
	//WebPage - Config - Step 2 - End

	//Tab Handling - Start
	private void updatePrimaryBrowserInfo() {
		int noOfRows = conditionsTable_PBC.getDefaultModel().getRowCount();
		if(noOfRows > 0) {
			if(!validateConditions(conditionsTable_PBC)) {
				int noOfSuperConditions = getNumberOfSuperConditions(conditionsTable_PBC);
				if(noOfSuperConditions > 1) {
					JOptionPane.showMessageDialog(this, "Primary Browser Conditions Definition can have only one Super Condition.");
				} else if(noOfSuperConditions <= 0) {
					JOptionPane.showMessageDialog(this, "Primary Browser Conditions Definition should have one Super Condition.");
				}
				mainTabPane.setSelectedIndex(0);
			}
		}

		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);

		BrowserDefinition browserDef = new BrowserDefinition();
		browserDef.setWhichBrowser(WhichBrowser.Primary);
		browserDef.setConditions(getConditionDefinitions(conditionsTable_PBC));
		browserDef.setDownloadImages(downloadImagesCKB_PBC.isSelected());
		browserDef.setDownloadJavaScripts(downloadScriptsCKB_PBC.isSelected());
		browserDef.setSecuredWebPage(securedWebPageCKB_PBC.isSelected());

		crawlerDef.setPrimaryBrowserDefinition(browserDef);
		crawlerDef.setInvokeBasePBC(invokePBC_CKB.isSelected());

		crawlerCache.put(sourceID, crawlerDef);
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

	private void updateSecondaryBrowserInfo() {
		int noOfRows = conditionsTable_SBC.getDefaultModel().getRowCount();
		if(noOfRows > 0) {
			if(!validateConditions(conditionsTable_SBC)) {
				int noOfSuperConditions = getNumberOfSuperConditions(conditionsTable_SBC);
				if(noOfSuperConditions > 1) {
					JOptionPane.showMessageDialog(this, "Secondary Browser Conditions Definition can have only one Super Condition.");
				} else if(noOfSuperConditions <= 0) {
					JOptionPane.showMessageDialog(this, "Secondary Browser Conditions Definition should have one Super Condition.");
				}
				mainTabPane.setSelectedIndex(1);
			}
		}

		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);

		BrowserDefinition browserDef = new BrowserDefinition();
		browserDef.setWhichBrowser(WhichBrowser.Secondary);
		browserDef.setConditions(getConditionDefinitions(conditionsTable_SBC));
		browserDef.setDownloadImages(downloadImagesCKB_SBC.isSelected());
		browserDef.setDownloadJavaScripts(downloadScriptsCKB_SBC.isSelected());
		browserDef.setSecuredWebPage(securedWebPageCKB_SBC.isSelected());

		crawlerDef.setSecondaryBrowserDefinition(browserDef);
		crawlerDef.setInvokeBaseSBC(invokeSBC_CKB.isSelected());

		crawlerCache.put(sourceID, crawlerDef);
	}

	private void updateTertiaryBrowserInfo() {
		int noOfRows = conditionsTable_TBC.getDefaultModel().getRowCount();
		if(noOfRows > 0) {
			if(!validateConditions(conditionsTable_TBC)) {
				int noOfSuperConditions = getNumberOfSuperConditions(conditionsTable_TBC);
				if(noOfSuperConditions > 1) {
					JOptionPane.showMessageDialog(this, "Tertiary Browser Conditions Definition can have only one Super Condition.");
				} else if(noOfSuperConditions <= 0) {
					JOptionPane.showMessageDialog(this, "Tertiary Browser Conditions Definition should have one Super Condition.");
				}
				mainTabPane.setSelectedIndex(2);
			}
		}

		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);

		BrowserDefinition browserDef = new BrowserDefinition();
		browserDef.setWhichBrowser(WhichBrowser.Tertiary);
		browserDef.setConditions(getConditionDefinitions(conditionsTable_TBC));
		browserDef.setDownloadImages(downloadImagesCKB_TBC.isSelected());
		browserDef.setDownloadJavaScripts(downloadScriptsCKB_TBC.isSelected());
		browserDef.setSecuredWebPage(securedWebPageCKB_TBC.isSelected());

		crawlerDef.setTertiaryBrowserDefinition(browserDef);
		crawlerDef.setInvokeBaseTBC(invokeTBC_CKB.isSelected());

		crawlerCache.put(sourceID, crawlerDef);
	}

	private void updateWebPageSummary() {
		//JOptionPane.showMessageDialog(this, "I am in updateWebPageSummary method..");
	}

	private boolean updateWebPageStepOne() {
		if(! validateWebPageStepOne()) {
			mainTabPane.setEnabledAt(0, false);
			mainTabPane.setEnabledAt(1, false);
			mainTabPane.setEnabledAt(2, false);
			mainTabPane.setEnabledAt(3, false);
			mainTabPane.setEnabledAt(4, true);
			mainTabPane.setEnabledAt(5, false);
			mainTabPane.setEnabledAt(6, false);
			mainTabPane.setSelectedIndex(4);
			return false;
		}
		return true;
	}

	private boolean validateWebPageStepOne() {
		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
		if(crawlerDef == null) {
			crawlerDef = new CrawlerDefinition();
			codeGenUtil.populateDefaultConstructors(crawlerDef);
			crawlerDef.setSourceID(sourceID);
			crawlerCache.put(sourceID, crawlerDef);
		}

		WebPages webPages = crawlerDef.getWebPages();
		if(webPages == null) {
			webPages = new WebPages();
		}

		WebPageDefinition webPageDef = null;
		if(sameWebPage) {
			webPageDef = webPages.getWebPage(uniqueNameOfWebPage);
			if(webPageDef == null) {
				webPageDef = new WebPageDefinition();
			}
		} else {
			webPageDef = new WebPageDefinition();
		}

		if((wpNameTF.getText() != null) && (wpNameTF.getText().trim().length() > 0)) {
			//webPageDef.setName(wpNameTF.getText());
			if(sameWebPage) {
				if(!webPageDef.getName().equalsIgnoreCase(wpNameTF.getText())) {
					if(validateWebPageName(wpNameTF.getText())) {
						webPageDef.setName(wpNameTF.getText());
					} else {
						JOptionPane.showMessageDialog(this, "Web Flow Name should be unique.\nAlready a Web Flow is defined with Name "
							+ wpNameTF.getText() + ".");
						wpNameTF.requestFocus();
						return false;
					}
				}
			} else {
				if(validateWebPageName(wpNameTF.getText())) {
					webPageDef.setName(wpNameTF.getText());
				} else {
					JOptionPane.showMessageDialog(this, "Web Flow Name should be unique.\nAlready a Web Flow is defined with Name "
						+ wpNameTF.getText() + ".");
					wpNameTF.requestFocus();
					return false;
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Web Flow Name is a mandatory field.");
			wpNameTF.requestFocus();
			return false;
		}

		if((wpTypeCMB.getSelectedItem() != null) && (((String) wpTypeCMB.getSelectedItem()).trim().length() > 0)) {
			webPageDef.setType((String) wpTypeCMB.getSelectedItem());
			if(webPageDef.getType().equals("Result")) {
				b_Next_Config_Step_2.setText("Next");
			} else {
				b_Next_Config_Step_2.setText("Done");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Web Flow Type is a mandatory field.");
			wpTypeCMB.requestFocus();
			return false;
		}

		if((wpNavOrderTF.getText() != null) && (wpNavOrderTF.getText().trim().length() > 0)) {
			if(sameWebPage) {
				if(!webPageDef.getNavigationOrder().equalsIgnoreCase(wpNavOrderTF.getText())) {
					if(validateNavigationOrder(wpNavOrderTF.getText())) {
						webPageDef.setNavigationOrder(wpNavOrderTF.getText());
					} else {
						JOptionPane.showMessageDialog(this, "Navigation Order should be unique.\nAlready a Web Flow exists with Navigation Order "
							+ wpNavOrderTF.getText() + ".");
						wpNavOrderTF.requestFocus();
						return false;
					}
				}
			} else {
				if(validateNavigationOrder(wpNavOrderTF.getText())) {
					webPageDef.setNavigationOrder(wpNavOrderTF.getText());
				} else {
					JOptionPane.showMessageDialog(this, "Navigation Order should be unique.\nAlready a Web Flow is defined with Navigation Order "
						+ wpNavOrderTF.getText() + ".");
					wpNavOrderTF.requestFocus();
					return false;
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Navigation Order is a mandatory field.");
			wpNavOrderTF.requestFocus();
			return false;
		}

		browserTypeOfWebPage = (String) wpBrowserCMB.getSelectedItem();
		if((browserTypeOfWebPage != null) && (browserTypeOfWebPage.trim().length() > 0)) {
			webPageDef.setBrowser(browserTypeOfWebPage);
		} else {
			JOptionPane.showMessageDialog(this, "Browser To Execute is a mandatory field.");
			wpBrowserCMB.requestFocus();
			return false;
		}

		if((wpUrlTF.getText() != null) && (wpUrlTF.getText().trim().length() > 0)) {
			webPageDef.setUrl(wpUrlTF.getText());
		} else {
			JOptionPane.showMessageDialog(this, "URL is a mandatory field.");
			wpUrlTF.requestFocus();
			return false;
		}

		webPageDef.setExecuteOnce(wpExecuteOnceCBX.isSelected());

		//web page qualifying conditions
		Conditions wpConditions = getWebPageConditions();
		webPageDef.setConditions(wpConditions);
//		if(wpConditions == null || wpConditions.size() == 0) {
//			webPageConditionTabs.setSelectedIndex(0);
//			return false;
//		} else {
//			webPageDef.setConditions(wpConditions);
//		}

		//code block qualifying conditions
		Conditions cbConditions = getCodeBlockConditions();
		//MainIfDefinition mainIfDef = new MainIfDefinition();
		MainIfDefinition mainIfDef = webPageDef.getMainIF();
		if(mainIfDef == null) {
			mainIfDef = new MainIfDefinition();
		}
		if(cbConditions == null) {
			webPageConditionTabs.setSelectedIndex(1);
			return false;
		} else {
			mainIfDef.setConditions(cbConditions);
		}
		webPageDef.setMainIF(mainIfDef);

		//setting unique name WebPageDefinition to be edited
		uniqueNameOfWebPage = webPageDef.getUniqueName();
		tree.setUniqueNameOfWebPage(uniqueNameOfWebPage);
		tree.setWebPageName(webPageDef.getName());
		tree.setSourceID(sourceID);
		tree.setBrowserTypeOfWebPage(browserTypeOfWebPage);

		if(sameWebPage) {
			crawlerDef.replaceWebPage(webPageDef);
		} else {
			webPages.addWebPage(webPageDef);
			crawlerDef.setWebPages(webPages);
		}

		crawlerCache.put(sourceID, crawlerDef);

		return true;
	}

	private boolean validateNavigationOrder(String navOrder) {
		boolean valid = true;
		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
		WebPages webPages = crawlerDef.getWebPages();
		if(webPages != null) {
			List<WebPageDefinition> webPageDefs = webPages.getWebPage();
			for(WebPageDefinition wpDef : webPageDefs) {
				if(wpDef.getNavigationOrder().equalsIgnoreCase(navOrder)) {
					valid = false;
					break;
				}
			}
		}
		return valid;
	}

	private boolean validateWebPageName(String webPageName) {
		boolean valid = true;
		String sourceID = sourceIDTF.getText();
		CrawlerDefinition crawlerDef = crawlerCache.get(sourceID);
		WebPages webPages = crawlerDef.getWebPages();
		if(webPages != null) {
			List<WebPageDefinition> webPageDefs = webPages.getWebPage();
			for(WebPageDefinition wpDef : webPageDefs) {
				if(wpDef.getName().equalsIgnoreCase(webPageName)) {
					valid = false;
					break;
				}
			}
		}
		return valid;
	}

	private Conditions getCodeBlockConditions() {
		Conditions conditions = new Conditions();
		int noOfRows = conditionsTable_CBQC.getDefaultModel().getRowCount();
		if(noOfRows > 0) {
			if(!validateConditions(conditionsTable_CBQC)) {
				int noOfSuperConditions = getNumberOfSuperConditions(conditionsTable_CBQC);
				if(noOfSuperConditions > 1) {
					JOptionPane.showMessageDialog(this, "\'Code Block - Qualifying Conditions\' should have only one Super Condition.");
				} else if(noOfSuperConditions <= 0) {
					JOptionPane.showMessageDialog(this, "There should be atleast one Super Condition in \'Code Block - Qualifying Conditions\'.");
				}
				return null;
			}
			conditions = getConditionDefinitions(conditionsTable_CBQC);
		}
		return conditions;
	}

	private Conditions getWebPageConditions() {
		Conditions conditions = new Conditions();
		int noOfRows = conditionsTable_WPQC.getDefaultModel().getRowCount();
		if(noOfRows > 0) {
			if(!validateConditions(conditionsTable_WPQC)) {
				int noOfSuperConditions = getNumberOfSuperConditions(conditionsTable_WPQC);
				if(noOfSuperConditions > 1) {
					JOptionPane.showMessageDialog(this, "\'Web Flow - Qualifying Conditions\' should have only one Super Condition.");
				} else if(noOfSuperConditions <= 0) {
					JOptionPane.showMessageDialog(this, "There should be atleast one Super Condition in \'Web Flow - Qualifying Conditions\'.");
				}
				return null;
			}
			conditions = getConditionDefinitions(conditionsTable_WPQC);
		}/* else {
			JOptionPane.showMessageDialog(this, "There should be atleast one Super Condition in \'Web Flow - Qualifying Conditions\'.");
			webPageConditionTabs.setSelectedIndex(0);
			return null;
		}*/
		return conditions;
	}

	private void updateWebPageStepTwo() {

	}
	//Tab Handling - End

	//WebPage Type variables - Setter & Getter methods - Start
//	private boolean isCollectResultLinkAdded() {
//		return collectResultLinkAdded;
//	}
//
//	private void setCollectResultLinkAdded(boolean collectResultLinkAdded) {
//		this.collectResultLinkAdded = collectResultLinkAdded;
//	}
//
//	private boolean isParseResultAdded() {
//		return parseResultAdded;
//	}
//
//	private void setParseResultAdded(boolean parseResultAdded) {
//		this.parseResultAdded = parseResultAdded;
//	}
	//WebPage Type variables - Setter & Getter methods - End

	public static void main(String[] args) throws Exception {
		CodeGenerator codeGen = new CodeGenerator();
		codeGen.setVisible(true);
	}

	class CodeGenWindowAdapter extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
	        System.exit(-1);
		}

	}

	class PBC_Con_CBX_Left_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("PBC_CBX1")) {
					cBoxLeft_PBC.setEnabled(true);
					leftOperandTF_PBC.setEnabled(false);
					con_LO_Selected_CB_PBC = "PBC_CBX1";
				} else if(((JCheckBox) e.getSource()).getName().equals("PBC_CBX2")) {
					cBoxLeft_PBC.setEnabled(false);
					leftOperandTF_PBC.setEnabled(true);
					con_LO_Selected_CB_PBC = "PBC_CBX2";
				}
			}
		}

	}

	class PBC_Con_CBX_Right_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("PBC_CBX3")) {
					cBoxRight_PBC.setEnabled(true);
					rightOperandTF_PBC.setEnabled(false);
					con_RO_Selected_CB_PBC = "PBC_CBX3";
				} else if(((JCheckBox) e.getSource()).getName().equals("PBC_CBX4")) {
					cBoxRight_PBC.setEnabled(false);
					rightOperandTF_PBC.setEnabled(true);
					con_RO_Selected_CB_PBC = "PBC_CBX4";
				}
			}
		}

	}

	class PBC_Con_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("PBC_B_Create")) {
				createCondition_PBC();
			} else if(buttonClicked.equals("PBC_B_Update")) {
				updateCondition_PBC();
			} else if(buttonClicked.equals("PBC_B_Delete")) {
				deleteCondition_PBC();
			} else if(buttonClicked.equals("PBC_B_Clear")) {
				clearCondition_PBC();
			}
		}

	}

	class PBC_Condition_Table_Mouse_Adapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				JTable conTable = (JTable) e.getSource();
				conTableRowNumber_PBC = conTable.getSelectedRow();
				if(conTableRowNumber_PBC > -1) {
					conditionName_PBC = (String) conTable.getModel().getValueAt(conTableRowNumber_PBC, 0);
					bUpdate_PBC.setEnabled(true);
					bDelete_PBC.setEnabled(true);
					populateConditionFields_PBC();
				}
			}

		}

	}

	class SBC_Con_CBX_Left_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("SBC_CBX1")) {
					cBoxLeft_SBC.setEnabled(true);
					leftOperandTF_SBC.setEnabled(false);
					con_LO_Selected_CB_SBC = "SBC_CBX1";
				} else if(((JCheckBox) e.getSource()).getName().equals("SBC_CBX2")) {
					cBoxLeft_SBC.setEnabled(false);
					leftOperandTF_SBC.setEnabled(true);
					con_LO_Selected_CB_SBC = "SBC_CBX2";
				}
			}
		}

	}

	class SBC_Con_CBX_Right_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("SBC_CBX3")) {
					cBoxRight_SBC.setEnabled(true);
					rightOperandTF_SBC.setEnabled(false);
					con_RO_Selected_CB_SBC = "SBC_CBX3";
				} else if(((JCheckBox) e.getSource()).getName().equals("SBC_CBX4")) {
					cBoxRight_SBC.setEnabled(false);
					rightOperandTF_SBC.setEnabled(true);
					con_RO_Selected_CB_SBC = "SBC_CBX4";
				}
			}
		}

	}

	class SBC_Con_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("SBC_B_Create")) {
				createCondition_SBC();
			} else if(buttonClicked.equals("SBC_B_Update")) {
				updateCondition_SBC();
			} else if(buttonClicked.equals("SBC_B_Delete")) {
				deleteCondition_SBC();
			} else if(buttonClicked.equals("SBC_B_Clear")) {
				clearCondition_SBC();
			}
		}

	}

	class SBC_Condition_Table_Mouse_Adapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				JTable conTable = (JTable) e.getSource();
				conTableRowNumber_SBC = conTable.getSelectedRow();
				if(conTableRowNumber_SBC > -1) {
					conditionName_SBC = (String) conTable.getModel().getValueAt(conTableRowNumber_SBC, 0);
					bUpdate_SBC.setEnabled(true);
					bDelete_SBC.setEnabled(true);
					populateConditionFields_SBC();
				}
			}

		}

	}

	class TBC_Con_CBX_Left_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("TBC_CBX1")) {
					cBoxLeft_TBC.setEnabled(true);
					leftOperandTF_TBC.setEnabled(false);
					con_LO_Selected_CB_TBC = "TBC_CBX1";
				} else if(((JCheckBox) e.getSource()).getName().equals("TBC_CBX2")) {
					cBoxLeft_TBC.setEnabled(false);
					leftOperandTF_TBC.setEnabled(true);
					con_LO_Selected_CB_TBC = "TBC_CBX2";
				}
			}
		}

	}

	class TBC_Con_CBX_Right_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("TBC_CBX3")) {
					cBoxRight_TBC.setEnabled(true);
					rightOperandTF_TBC.setEnabled(false);
					con_RO_Selected_CB_TBC = "TBC_CBX3";
				} else if(((JCheckBox) e.getSource()).getName().equals("TBC_CBX4")) {
					cBoxRight_TBC.setEnabled(false);
					rightOperandTF_TBC.setEnabled(true);
					con_RO_Selected_CB_TBC = "TBC_CBX4";
				}
			}
		}

	}

	class TBC_Con_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("TBC_B_Create")) {
				createCondition_TBC();
			} else if(buttonClicked.equals("TBC_B_Update")) {
				updateCondition_TBC();
			} else if(buttonClicked.equals("TBC_B_Delete")) {
				deleteCondition_TBC();
			} else if(buttonClicked.equals("TBC_B_Clear")) {
				clearCondition_TBC();
			}
		}

	}

	class TBC_Condition_Table_Mouse_Adapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				JTable conTable = (JTable) e.getSource();
				conTableRowNumber_TBC = conTable.getSelectedRow();
				if(conTableRowNumber_TBC > -1) {
					conditionName_TBC = (String) conTable.getModel().getValueAt(conTableRowNumber_TBC, 0);
					bUpdate_TBC.setEnabled(true);
					bDelete_TBC.setEnabled(true);
					populateConditionFields_TBC();
				}
			}

		}

	}

	class WebPages_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("B_Create_WebPage")) {
				createWebPage();
			} else if(buttonClicked.equals("B_Update_WebPage")) {
				updateWebPage();
			} else if(buttonClicked.equals("B_Delete_WebPage")) {
				deleteWebPage();
			} else if(buttonClicked.equals("B_Reset_WebPage")) {
				clearWebPage();
			}
		}

	}

	class WPQC_Con_CBX_Left_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("WPQC_CBX1")) {
					cBoxLeft_WPQC.setEnabled(true);
					leftOperandTF_WPQC.setEnabled(false);
					con_LO_Selected_CB_WPQC = "WPQC_CBX1";
				} else if(((JCheckBox) e.getSource()).getName().equals("WPQC_CBX2")) {
					cBoxLeft_WPQC.setEnabled(false);
					leftOperandTF_WPQC.setEnabled(true);
					con_LO_Selected_CB_WPQC = "WPQC_CBX2";
				}
			}
		}

	}

	class WPQC_Con_CBX_Right_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("WPQC_CBX3")) {
					cBoxRight_WPQC.setEnabled(true);
					rightOperandTF_WPQC.setEnabled(false);
					con_RO_Selected_CB_WPQC = "WPQC_CBX3";
				} else if(((JCheckBox) e.getSource()).getName().equals("WPQC_CBX4")) {
					cBoxRight_WPQC.setEnabled(false);
					rightOperandTF_WPQC.setEnabled(true);
					con_RO_Selected_CB_WPQC = "WPQC_CBX4";
				}
			}
		}

	}

	class WPQC_Con_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("WPQC_B_Create")) {
				createCondition_WPQC();
			} else if(buttonClicked.equals("WPQC_B_Update")) {
				updateCondition_WPQC();
			} else if(buttonClicked.equals("WPQC_B_Delete")) {
				deleteCondition_WPQC();
			} else if(buttonClicked.equals("WPQC_B_Clear")) {
				clearCondition_WPQC();
			}
		}

	}

	class WPQC_Condition_Table_Mouse_Adapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				JTable conTable = (JTable) e.getSource();
				conTableRowNumber_WPQC = conTable.getSelectedRow();
				if(conTableRowNumber_WPQC > -1) {
					conditionName_WPQC = (String) conTable.getModel().getValueAt(conTableRowNumber_WPQC, 0);
					bUpdate_WPQC.setEnabled(true);
					bDelete_WPQC.setEnabled(true);
					populateConditionFields_WPQC();
				}
			}

		}

	}

	class CBQC_Con_CBX_Left_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("CBQC_CBX1")) {
					cBoxLeft_CBQC.setEnabled(true);
					leftOperandTF_CBQC.setEnabled(false);
					con_LO_Selected_CB_CBQC = "CBQC_CBX1";
				} else if(((JCheckBox) e.getSource()).getName().equals("CBQC_CBX2")) {
					cBoxLeft_CBQC.setEnabled(false);
					leftOperandTF_CBQC.setEnabled(true);
					con_LO_Selected_CB_CBQC = "CBQC_CBX2";
				}
			}
		}

	}

	class CBQC_Con_CBX_Right_Listener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox) e.getSource()).isSelected()) {
				if(((JCheckBox) e.getSource()).getName().equals("CBQC_CBX3")) {
					cBoxRight_CBQC.setEnabled(true);
					rightOperandTF_CBQC.setEnabled(false);
					con_RO_Selected_CB_CBQC = "CBQC_CBX3";
				} else if(((JCheckBox) e.getSource()).getName().equals("CBQC_CBX4")) {
					cBoxRight_CBQC.setEnabled(false);
					rightOperandTF_CBQC.setEnabled(true);
					con_RO_Selected_CB_CBQC = "CBQC_CBX4";
				}
			}
		}

	}

	class CBQC_Con_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("CBQC_B_Create")) {
				createCondition_CBQC();
			} else if(buttonClicked.equals("CBQC_B_Update")) {
				updateCondition_CBQC();
			} else if(buttonClicked.equals("CBQC_B_Delete")) {
				deleteCondition_CBQC();
			} else if(buttonClicked.equals("CBQC_B_Clear")) {
				clearCondition_CBQC();
			}
		}

	}

	class CBQC_Condition_Table_Mouse_Adapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				JTable conTable = (JTable) e.getSource();
				conTableRowNumber_CBQC = conTable.getSelectedRow();
				if(conTableRowNumber_CBQC > -1) {
					conditionName_CBQC = (String) conTable.getModel().getValueAt(conTableRowNumber_CBQC, 0);
					bUpdate_CBQC.setEnabled(true);
					bDelete_CBQC.setEnabled(true);
					populateConditionFields_CBQC();
				}
			}
		}

	}

	class WebPageConfig_1_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			try {
				if(buttonClicked.equals("B_WebPage_Next")) {
					wpConfigStep_1();
				} else if(buttonClicked.equals("B_WebPage_Cancel")) {
					cancelConfigWP();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	class WebPageConfig_2_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("B_WebPage_Step_Two_Previous")) {
				goToWPConfig_Step_1();
			} else if(buttonClicked.equals("B_WebPage_Step_Two_Next")) {
				wpConfig_Step_2();
			} else if(buttonClicked.equals("B_WebPage_Step_Two_Cancel")) {
				cancelConfigWP_Step_2();
			}
		}

	}

	class WebPageConfig_3_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			if(buttonClicked.equals("B_WebPage_Step_Three_Previous")) {
				goToWPConfig_Go_To_Step_2();
			} else if(buttonClicked.equals("B_WebPage_Step_Three_Next")) {
				wpConfig_Step_3();
			} else if(buttonClicked.equals("B_WebPage_Step_Three_Cancel")) {
				cancelConfigWP_Step_2();
			}
		}

	}

	class Main_Button_Action_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonClicked = ((JButton) e.getSource()).getName();
			try {
				if(buttonClicked.equals("B_Save_And_Generate")) {
					saveAndGenerateCrawler();
				} else if(buttonClicked.equals("B_Reset")) {
				    int response = JOptionPane.showConfirmDialog(null, "Do you want to reset Crawler definition?", "Confirm",
				        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {
						resetCrawlerConfig();
					}
				} else if(buttonClicked.equals("B_Save")) {
					saveCrawler();
				} else if(buttonClicked.equals("B_Load")) {
					loadCrawlerDefinition();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	class WebPage_Key_Adapter implements KeyListener {
		int keyPressed = -1;

		@Override
		public void keyPressed(KeyEvent e) {
			char c = e.getKeyChar();
			keyPressed = e.getKeyCode();
			if(! (c == '0' | c == '1' | c == '2' | c == '3' | c == '4' | c == '5' | c == '6' | c == '7' | c == '8' | c == '9' | keyPressed == 8)) {
				e.consume();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			char c = e.getKeyChar();
			keyPressed = e.getKeyCode();
			if(! (c == '0' | c == '1' | c == '2' | c == '3' | c == '4' | c == '5' | c == '6' | c == '7' | c == '8' | c == '9' | keyPressed == 8)) {
				e.consume();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			char c = e.getKeyChar();
			keyPressed = e.getKeyCode();
			if(! (c == '0' | c == '1' | c == '2' | c == '3' | c == '4' | c == '5' | c == '6' | c == '7' | c == '8' | c == '9' | keyPressed == 8)) {
				e.consume();
			}
		}

	}

	class MainTab_ChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if(selectedTabOfMainTabPane == ((JTabbedPane) e.getSource()).getSelectedIndex()) {
				return;
			}
			if(validateCodeGen()) {
				switch (selectedTabOfMainTabPane) {
					case 0:
						updatePrimaryBrowserInfo();
					break;

					case 1:
						updateSecondaryBrowserInfo();
					break;

					case 2:
						updateTertiaryBrowserInfo();
					break;

					case 3:
						//do nothing
						//updateWebPageSummary();
					break;

					case 4:
						//do nothing
						//updateWebPageStepOne();
					break;

					case 5:
						updateWebPageStepTwo();
					break;

					default:
					break;
				}
				selectedTabOfMainTabPane = ((JTabbedPane) e.getSource()).getSelectedIndex();
				if(selectedTabOfMainTabPane == 3) {
					updateWebPageSummary();
				}
			} else {
				((JTabbedPane) e.getSource()).setSelectedIndex(selectedTabOfMainTabPane);
				return;
			}
		}

	}

	class ResultSetTableCellComboBox extends DefaultCellEditor {
		private static final long serialVersionUID = 7086768876318455664L;

		public ResultSetTableCellComboBox(String[] items) {
			super(new PopComboBox(items));
		}

	}

	class ResultSetTableCheckBoxCell implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			PopCheckBox cBox = new PopCheckBox();
			if(value.equals("true")) {
				cBox.setSelected(true);
			} else {
				cBox.setSelected(false);
			}
			return cBox;
		}

	}

}