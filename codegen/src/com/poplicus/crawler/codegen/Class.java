package com.poplicus.crawler.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Class {
	
	private Indentation indentation = new Indentation(Indentation.INDENTATION_LEVEL_1);

	private String packageName = null;
	
	private String name = null;
	
	private String superclassName = null;
	
	private String accessModifier = "public" ;
	
	private List<Constructor> constructors = new ArrayList<Constructor>();
	
	private List<Method> methods = new ArrayList<Method>();
	
	private List<Variable> instanceVariables = new ArrayList<Variable>();
	
	private StringBuffer classCode = null;
	
	private StringBuffer importCode = null;
	
	private StringBuffer beginingOfClassCode = null;
	
	private StringBuffer instanceVariablesCode = null;
	
	private StringBuffer constructorCode = null;
	
	private StringBuffer methodCode = null;
	
	private StringBuffer endOfClassCode = null;
	
	private List<String> warningMessages = new ArrayList<String>();
	
	public Imports imports = null;
	
	private boolean securityAlertPopup = true;
	
	private String generatedClassFileName = null;
	
	public boolean isSecurityAlertPopup() {
		return securityAlertPopup;
	}

	public void setSecurityAlertPopup(boolean securityAlertPopup) {
		this.securityAlertPopup = securityAlertPopup;
	}

	private void initialize() {
		imports = Imports.getInstance();
	}
	
	public List<String> getWarningMessages() {
		return warningMessages;
	}

	public String getAccessModifier() {
		return accessModifier;
	}

	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}
	
	public String getSuperclassName() {
		return superclassName;
	}

	public void setSuperclassName(String superclassName) {
		this.superclassName = superclassName;
	}
	
	public Class() {
		initialize();
	}
	
	public Class(String packageName, String name) {
		this.packageName = packageName;
		this.name = name;
		initialize();
	}
	
	public Class(String packageName, String name, String superClassName) {
		this.packageName = packageName;
		this.name = name;
		this.superclassName = superClassName;
		initialize();
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addConstructor(Constructor constructor) {
		if(constructor != null) {
			constructor.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
		}
		this.constructors.add(constructor);
	}
	
	public void addInstanceVariable(Variable instanceVariable) {
		if(instanceVariable != null) {
			instanceVariable.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
		}
		this.instanceVariables.add(instanceVariable);
	}
	
	public void addMethod(Method method) {
		if(method != null) {
			method.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
		}
		this.methods.add(method);
	}
	
	public boolean generateCode() throws ClassDefinitionException, ArgumentValueDefinitionException, 
				   						 LineOfCodeException, ActionDefinitionException, RHSTaskException,
				   						 FileGenerationException {
		boolean success = false;

		classCode = new StringBuffer();
		importCode = new StringBuffer();
		beginingOfClassCode = new StringBuffer();
		constructorCode = new StringBuffer();
		methodCode = new StringBuffer();
		endOfClassCode = new StringBuffer();
		
		//import code
		importCode.append(Imports.getInstance().toStringBuffer());
		
		//generate class code
		beginingOfClassCode.append("namespace DataCrawler").append("\n{\n");
		beginingOfClassCode.append(indentation.getTabs()).append(accessModifier).append(" class ").append(name);
		
		if(superclassName != null) {
			beginingOfClassCode.append(" : ").append(superclassName);
		}
		beginingOfClassCode.append("\n").append(indentation.getTabs()).append("{");
		
		//create constructors
		generateConstructorCode();
		
		//create methods
		generateMethodCode();
		
		//declare instance variables
		generateInstanceVariableCode();
		
		//closing the class and package
		endOfClassCode.append("\n").append(indentation.getTabs()).append("}").append("\n}");
		
		//import code -- this needs to be populated at last. need to strike a logic.. He He He...
		importCode.append("");
		
		//form the class physically
		classCode.append(importCode).append(beginingOfClassCode).append(instanceVariablesCode);
		classCode.append(constructorCode).append(methodCode);
		
		//function to close the security alert popup control
		if(securityAlertPopup){
			classCode.append(addSecurityAlertPopup());
		}
		
		classCode.append(endOfClassCode);
		
		writeToFile();
		success = true;
		return success;
	}
	
	private void generateMethodCode() 
			throws ClassDefinitionException, ArgumentValueDefinitionException, 
				   LineOfCodeException, ActionDefinitionException, RHSTaskException {
		for(Method method : this.methods) {
			methodCode.append(Constants.NEW_LINE);
			methodCode.append(method.toStringBuffer());
			methodCode.append(Constants.NEW_LINE);
		}
	}
	
	/**
	 * Generates code for instance variables. An instance variable can be declared anywhere in a method.
	 * Invoking generateInstanceVariableCode() method in generateCode() method of Class.java ensures that instance variables are generated.
	 * 
	 */
	public void generateInstanceVariableCode() {
		StringBuffer finalInstVariables = new StringBuffer();
		StringBuffer nonFinalInstVariables = new StringBuffer();
		instanceVariablesCode = new StringBuffer();
		for(Variable var : this.instanceVariables) {
			if(var.isFinal()) {
				finalInstVariables.append(var.toStringBuffer());
			} else {
				nonFinalInstVariables.append(var.toStringBuffer());
			}
		}
		instanceVariablesCode.append(finalInstVariables).append(Constants.NEW_LINE).append(nonFinalInstVariables);
	}
	
	private void generateConstructorCode() {
		for(Constructor con : this.constructors) {
			constructorCode.append(Constants.NEW_LINE);
			constructorCode.append(con.toStringBuffer());
			constructorCode.append(Constants.NEW_LINE);
		}
	}
	
	private StringBuffer addSecurityAlertPopup() {
		StringBuffer code = new StringBuffer();
		code.append(Constants.NEW_LINE);
		code.append(Constants.TAB_TAB).append("protected override void ClickPopup()");
		code.append(Constants.NEW_LINE).append(Constants.TAB_TAB).append("{");
		code.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("IntPtr WindowHandle = FindWindow(null, \"Security Alert\");");
		code.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("if (WindowHandle == IntPtr.Zero) return;");
		code.append(Constants.NEW_LINE).append("");
		code.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("IntPtr ButtonHandle = FindWindowEx(WindowHandle, IntPtr.Zero, \"button\", \"&Yes\");");
		code.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("if (ButtonHandle == IntPtr.Zero) return;");
		code.append(Constants.NEW_LINE).append("");
		code.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("SetActiveWindow(WindowHandle);");
		code.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("SendNotifyMessage(ButtonHandle, Constants.BM_CLICK, IntPtr.Zero, IntPtr.Zero);");		
		code.append(Constants.NEW_LINE).append(Constants.TAB_TAB).append("}");
		
		return code;
	}
	
	private void writeToFile() throws FileGenerationException {
		File classFile = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		String oldFileDateandTime = null;
		try {
			if(validateFolderStructure()) {
				classFile = new File("C:\\Poplicus\\CodeGen\\Source\\" + name + ".cs");
				if(classFile.exists()){
					oldFileDateandTime = (new SimpleDateFormat("MM_dd_yyyy_hh_mm_ss")).format((new Date(classFile.lastModified())));
					classFile.renameTo(new File("C:\\Poplicus\\CodeGen\\Source\\" + name + "_" 
						+ oldFileDateandTime + ".cs"));
				}
				if(classFile.createNewFile()) {
					fw = new FileWriter(classFile);
					bw = new BufferedWriter(fw);
					bw.write(classCode.toString());
					bw.close();
				}
				generatedClassFileName = classFile.getAbsolutePath();
			} else {
				throw new FileGenerationException("Exception occcurred while writing source file to C:\\Poplicus\\CodeGen\\Source.");
			}
		} catch(FileGenerationException exFileGen) {
			throw exFileGen;
		} catch(Exception ex) {
			throw new FileGenerationException(ex.getMessage(), ex);
		}	
	}
	
	public boolean validateFolderStructure() throws FileGenerationException {
		boolean valid = true;
		File file = null;
		try {
			file = new File("C:\\Poplicus\\CodeGen\\Source");
			if(!file.exists()) {
				file.mkdirs();
			}
		} catch(Exception ex) {
			valid = false;
			throw new FileGenerationException("Could not create folder structure C:\\Poplicus\\CodeGen\\Source.");
		}
		return valid;
	}
	
	public String getGeneratedClassFileName() {
		return this.generatedClassFileName;
	}
	
}
