package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.poplicus.crawler.codegen.utilities.config.FunctionDefConfig;
import com.poplicus.crawler.codegen.utilities.config.ParameterDefConfig;
import com.poplicus.crawler.codegen.utilities.config.SupportingFunctionDefConfig;

/**
 ******************************************************************************************************************************* 
 *Usage.                                                                                                                       *
 *                                                                                                                             *
 *Very Important - ArgumentDefinition(s) should be added in the same order as the arguments are defined in a method signature. *
 *                                                                                                                             *
 *For E.g. Below is the Text.TextBeforeTag1UntilTag2(string input, string tag1, string tag2)                                   *
 *                                                                                                                             *
 *  ClassDefinition c1 = new ClassDefinition("Functions", "Text");                                                             *
 *	MethodDefinition c1M1 = new MethodDefinition("TextBeforeTag1UntilTag2");                                                   *
 *	ArgumentDefinition c1M1A1 = new ArgumentDefinition(ArgumentType.string, "input");                                          *
 *	ArgumentDefinition c1M1A2 = new ArgumentDefinition(ArgumentType.string, "tag1");                                           *
 *  ArgumentDefinition c1M1A3 = new ArgumentDefinition(ArgumentType.string, "tag2");                                           *
 *	c1M1.addArgument(c1M1A1);                                                                                                  *
 *	c1M1.addArgument(c1M1A2);                                                                                                  *
 *	c1M1.addArgument(c1M1A3);                                                                                                  *
 *  c1.addMethodDefinition(c1M1);                                                                                              *
 *	classDefs.put(c1.getFullyQualifiedClassName(), c1);                                                                        *
 *                                                                                                                             *
 *******************************************************************************************************************************
 */
public class ClassDefinitions {
	
	private static Map<String, ClassDefinition> classDefs = new HashMap<String, ClassDefinition>();
	
	public static void populateClassDefinitions(List<FunctionDefConfig> functionDefs) {
		ClassDefinition classDef = null;
		MethodDefinition methodDef = null;
		MethodDefinition supportMethodDef = null;
		List<ParameterDefConfig> params = null;
		List<ParameterDefConfig> supportParams = null;
		List<SupportingFunctionDefConfig> supportingFunctions = null;
		ArgumentDefinition argDef = null;
		ArgumentDefinition supportArgDef = null;
		try {
			for(FunctionDefConfig functionDef : functionDefs) {
				classDef = classDefs.get(functionDef.getPackageName() + Constants.DOT + functionDef.getClassName());
				if(classDef == null) {
					classDef = new ClassDefinition(functionDef.getPackageName(), functionDef.getClassName(), 
						functionDef.getClassAccessType(), functionDef.getVariableReferenceName());
				}
				methodDef = new MethodDefinition(functionDef.getName(), functionDef.getName(), functionDef.isStaticAccess());
				params = functionDef.getParameters();
				for(ParameterDefConfig param : params) {
					argDef = new ArgumentDefinition(param.getType(), param.getName());
					methodDef.addArgument(argDef);
				}
				supportingFunctions = functionDef.getSupportingFunctions();
				for(SupportingFunctionDefConfig supDef : supportingFunctions) {
					supportMethodDef = new MethodDefinition(supDef.getName(), supDef.getName(), true);
					supportParams = supDef.getParameters();
					for(ParameterDefConfig sParam : supportParams) {
						supportArgDef = new ArgumentDefinition(sParam.getName(), sParam.getName());
						supportMethodDef.addArgument(supportArgDef);
					}
					methodDef.addSupportingMethod(supportMethodDef);
				}
				classDef.addMethodDefinition(methodDef);
				classDefs.put(functionDef.getPackageName() + Constants.DOT + functionDef.getClassName(), classDef);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/*static {
		try {
			//Functions.Text Definition
			ClassDefinition c1 = new ClassDefinition("Functions", "Text", ClassAccessType.static_access, null);
			
			MethodDefinition c1M1 = new MethodDefinition("TextBeforeTag1UntilTag2", "TextBeforeTag1UntilTag2", true);
			ArgumentDefinition c1M1A1 = new ArgumentDefinition("string", "input");
			ArgumentDefinition c1M1A2 = new ArgumentDefinition("string", "tag1");
			ArgumentDefinition c1M1A3 = new ArgumentDefinition("string", "tag2");
			c1M1.addArgument(c1M1A1);
			c1M1.addArgument(c1M1A2);
			c1M1.addArgument(c1M1A3);
			c1.addMethodDefinition(c1M1);

			MethodDefinition c1M2 = new MethodDefinition("TextBetweenTags", "TextBetweenTags", true);
			ArgumentDefinition c1M2A1 = new ArgumentDefinition("string", "input");
			ArgumentDefinition c1M2A2 = new ArgumentDefinition("string", "tag1");
			ArgumentDefinition c1M2A3 = new ArgumentDefinition("string", "tag2");
			c1M2.addArgument(c1M2A1);
			c1M2.addArgument(c1M2A2);
			c1M2.addArgument(c1M2A3);
			c1.addMethodDefinition(c1M2);
			
			MethodDefinition c1M3 = new MethodDefinition("ReplaceTag1WithTag2", "ReplaceTag1WithTag2", true);
			ArgumentDefinition c1M3A1 = new ArgumentDefinition("string", "input");
			ArgumentDefinition c1M3A2 = new ArgumentDefinition("string", "tag1");
			ArgumentDefinition c1M3A3 = new ArgumentDefinition("string", "tag2");
			c1M3.addArgument(c1M3A1);
			c1M3.addArgument(c1M3A2);
			c1M3.addArgument(c1M3A3);
			c1.addMethodDefinition(c1M3);
			
			MethodDefinition c1M4 = new MethodDefinition("GetTextStringBetweenTags", "GetTextStringBetweenTags", true);
			ArgumentDefinition c1M4A1 = new ArgumentDefinition("string", "value");
			ArgumentDefinition c1M4A2 = new ArgumentDefinition("string", "startTag");
			ArgumentDefinition c1M4A3 = new ArgumentDefinition("string", "endTag");
			c1M4.addArgument(c1M4A1);
			c1M4.addArgument(c1M4A2);
			c1M4.addArgument(c1M4A3);
			c1.addMethodDefinition(c1M4);

			classDefs.put(c1.getFullyQualifiedClassName(), c1);
			
			//csExWB.cEXWB Definition
			ClassDefinition c2 = new ClassDefinition("csExWB", "cEXWB", ClassAccessType.instance_access, "_primaryBrowser");
			MethodDefinition c2M1 = new MethodDefinition("SelectOptionItemWithOnChange", "SelectOptionItemWithOnChange", false);
			ArgumentDefinition c2M1A1 = new ArgumentDefinition("string", "selectName");
			ArgumentDefinition c2M1A2 = new ArgumentDefinition("string", "optionValue");
			ArgumentDefinition c2M1A3 = new ArgumentDefinition("string", "optionText");
			c2M1.addArgument(c2M1A1);
			c2M1.addArgument(c2M1A2);
			c2M1.addArgument(c2M1A3);
			c2.addMethodDefinition(c2M1);			
			
			MethodDefinition c2M2 = new MethodDefinition("AutomationTask_PerformEnterData", "AutomationTask_PerformEnterData", false);
			ArgumentDefinition c2M2A1 = new ArgumentDefinition("string", "inputname");
			ArgumentDefinition c2M2A2 = new ArgumentDefinition("string", "strValue");			
			c2M2.addArgument(c2M2A1);
			c2M2.addArgument(c2M2A2);			
			c2.addMethodDefinition(c2M2);
			
			MethodDefinition c2M3 = new MethodDefinition("SetNextFileDownloadPathAndName", "SetNextFileDownloadPathAndName", false);
			ArgumentDefinition c2M3A1 = new ArgumentDefinition("string", "pathandFilename");						
			c2M3.addArgument(c2M3A1);						
			c2.addMethodDefinition(c2M3);
			
			MethodDefinition c2M4 = new MethodDefinition("LocationUrl", "LocationUrl", false);								
			c2.addMethodDefinition(c2M4);
			classDefs.put(c2.getFullyQualifiedClassName(), c2);		

			//System.DateTime
			ClassDefinition c3 = new ClassDefinition("System", "DateTime", ClassAccessType.static_instance_access, "dateTime");
			MethodDefinition c3M1 = new MethodDefinition("Now.AddDays", "Now.AddDays", true);
			ArgumentDefinition c3M1A1 = new ArgumentDefinition("double", "value");
			MethodDefinition c3M1S1 = new MethodDefinition("ToString","ToString", true);
			ArgumentDefinition c3M1S1A1 = new ArgumentDefinition("string", "format");
			c3M1S1.addArgument(c3M1S1A1);
			c3M1.addArgument(c3M1A1);
			c3M1.addSupportingMethod(c3M1S1);
			c3.addMethodDefinition(c3M1);
			classDefs.put(c3.getFullyQualifiedClassName(), c3);		
			
			//Functions.FileOperation.
			ClassDefinition c4 = new ClassDefinition("Functions", "FileOperation", ClassAccessType.static_instance_access, "fileOperation");
			MethodDefinition c4M1 = new MethodDefinition("GetRootPath", "GetRootPath", true);
			ArgumentDefinition c4M1A1 = new ArgumentDefinition("string", "folderName");		
			c4M1.addArgument(c4M1A1);		
			c4.addMethodDefinition(c4M1);
			classDefs.put(c4.getFullyQualifiedClassName(), c4);
			
			//Functions.Web.DownloadFile(url, tempPDFFileName);
			ClassDefinition c5 = new ClassDefinition("Functions", "Web", ClassAccessType.static_instance_access, "downloadFile");
			MethodDefinition c5M1 = new MethodDefinition("DownloadFile", "DownloadFile", true);
			ArgumentDefinition c5M1A1 = new ArgumentDefinition("string", "url");	
			ArgumentDefinition c5M1A2 = new ArgumentDefinition("string", "fileName");
			c5M1.addArgument(c5M1A1);	
			c5M1.addArgument(c5M1A2);
			c5.addMethodDefinition(c5M1);
			classDefs.put(c5.getFullyQualifiedClassName(), c5);
			
			//System.IO.File  
			ClassDefinition c6 = new ClassDefinition("System.IO", "File", ClassAccessType.static_instance_access, "file");
			MethodDefinition c6M1 = new MethodDefinition("Delete", "Delete", true);
			ArgumentDefinition c6M1A1 = new ArgumentDefinition("string", "path");		
			c6M1.addArgument(c6M1A1);		
			c6.addMethodDefinition(c6M1);
			
			//File.Exists(fileName);
			MethodDefinition c6M2 = new MethodDefinition("Exists", "Exists", true);
			ArgumentDefinition c6M2A1 = new ArgumentDefinition("string", "path");		
			c6M2.addArgument(c6M2A1);		
			c6.addMethodDefinition(c6M2);			
			classDefs.put(c6.getFullyQualifiedClassName(), c6);
			
			
			//System.Text.RegularExpressions.Regex.Split()
			ClassDefinition c7 = new ClassDefinition("RegularExpressions", "Regex", ClassAccessType.static_instance_access, "regex");
			MethodDefinition c7M1 = new MethodDefinition("Split", "Split", true);
			ArgumentDefinition c7M1A1 = new ArgumentDefinition("string", "input");
			ArgumentDefinition c7M1A2 = new ArgumentDefinition("string", "pattern");
			c7M1.addArgument(c7M1A1);	
			c7M1.addArgument(c7M1A2);
			c7.addMethodDefinition(c7M1);
			
			//Matches()
			MethodDefinition c7M2 = new MethodDefinition("Matches", "Matches", true);
			ArgumentDefinition c7M2A1 = new ArgumentDefinition("string", "input");
			ArgumentDefinition c7M2A2 = new ArgumentDefinition("string", "pattern");
			c7M2.addArgument(c7M2A1);	
			c7M2.addArgument(c7M2A2);
			c7.addMethodDefinition(c7M2);
			
			//Replace()
			MethodDefinition c7M3 = new MethodDefinition("Replace", "Replace", true);
			ArgumentDefinition c7M3A1 = new ArgumentDefinition("string", "input");
			ArgumentDefinition c7M3A2 = new ArgumentDefinition("string", "pattern");
			ArgumentDefinition c7M3A3 = new ArgumentDefinition("string", "replacement");
			c7M3.addArgument(c7M3A1);	
			c7M3.addArgument(c7M3A2);
			c7M3.addArgument(c7M3A3);
			c7.addMethodDefinition(c7M3);
			
			//IsMatch()
			MethodDefinition c7M4 = new MethodDefinition("IsMatch", "IsMatch", true);
			ArgumentDefinition c7M4A1 = new ArgumentDefinition("string", "input");
			ArgumentDefinition c7M4A2 = new ArgumentDefinition("string", "pattern");			
			c7M4.addArgument(c7M4A1);	
			c7M4.addArgument(c7M4A2);			
			c7.addMethodDefinition(c7M4);
			classDefs.put(c7.getFullyQualifiedClassName(), c7);
			
			
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}*/
	
	public static ClassDefinition getClassDefinition(String packageName, String className, String fullyQualifiedClassName) throws ClassDefinitionException {
		if(fullyQualifiedClassName == null && 
			((packageName != null && packageName.length() > 0) && (className != null && className.length() > 0))) {
			fullyQualifiedClassName = packageName + Constants.DOT + className;	
		}
		if(fullyQualifiedClassName != null) {
			return classDefs.get(fullyQualifiedClassName);
		} else {
			throw new ClassDefinitionException("Fully qualified class name is null and it could not be constructed from packageName and className provided.");
		}
	}
	
	public static MethodDefinition getMethodDefinition(String packageName, String className, 
			  										   String fullyQualifiedClassName, String methodNameKey) {
		ClassDefinition classDef = null;
		MethodDefinition methodDef = null;
		String key = null;
		if((fullyQualifiedClassName == null) && (packageName != null && className != null)) {
			fullyQualifiedClassName = packageName + Constants.DOT + className;
		}
		Iterator<String> keys = classDefs.keySet().iterator();
		while(keys.hasNext()) {
			key = keys.next();
			classDef = classDefs.get(key);
			if(classDef.getFullyQualifiedClassName().equals(fullyQualifiedClassName)) {
				methodDef = classDef.getMethodDefinition(methodNameKey);
				break;
			}
		}
		return methodDef;
	}
	
	public static MethodDefinition getSupportingMethodDefinition(String packageName, String className, 
																 String fullyQualifiedClassName, String methodNameKey, 
																 String supportingMethodNameKey) {
		MethodDefinition parentMethod = getMethodDefinition(packageName, className, fullyQualifiedClassName, methodNameKey);
		MethodDefinition supportingMethod = null;
		List<MethodDefinition> supportingMethods = parentMethod.getSupportingMethods();
		int noOfSupportingMethods = supportingMethods.size();
		for(int supportIndex = 0; supportIndex < noOfSupportingMethods; supportIndex ++) {
			supportingMethod = supportingMethods.get(supportIndex);
			if(supportingMethod.getMethodNameKey().equals(supportingMethodNameKey)) {
				break;
			}
		}
		return supportingMethod;
	}
	
	public static List<ArgumentDefinition> getArgumentDefinitions(String packageName, String className, 
																  String fullyQualifiedClassName, String methodName) {
		return getMethodDefinition(packageName, className, fullyQualifiedClassName, methodName).getArgumentDefinitions();
	}
	
	public static List<String> getPackages() {
		List<String> packages = new ArrayList<String>();
		Collection<ClassDefinition> values = classDefs.values();
		Iterator<ClassDefinition> itrValues = values.iterator();
		String packageName = null;
		while(itrValues.hasNext()) {
			packageName = itrValues.next().getPackageName();
			if(packageName != null && ! packages.contains(packageName)) {
				packages.add(packageName);
			}
		}
		return packages;
	}
	
	public static List<String> getClasses(String packageName) {
		List<String> classes = new ArrayList<String>();
		Collection<ClassDefinition> values = classDefs.values();
		Iterator<ClassDefinition> itrValues = values.iterator();
		ClassDefinition classDef = null;
		while(itrValues.hasNext()) {
			classDef = itrValues.next();
			if(classDef.getPackageName().equals(packageName)) {
				classes.add(classDef.getClassName());
			}
		}
		return classes;
	}
	
	public static List<String> getMethods(String packageName, String className) {
		List<String> methods = new ArrayList<String>();
		List<MethodDefinition> methodDefs = null;
		Iterator<MethodDefinition> itrMethodDefs = null;
		String fullyQualifiedClassName = null;
		ClassDefinition classDef = null;
		if(packageName != null && className != null) {
			fullyQualifiedClassName = packageName + Constants.DOT + className;
			classDef = classDefs.get(fullyQualifiedClassName);
			methodDefs = classDef.getMethodDefinitions();
			itrMethodDefs = methodDefs.iterator();
			while(itrMethodDefs.hasNext()) {
				methods.add(itrMethodDefs.next().getMethodNameKey());
			}
		}
		return methods;
	} 
	
	public static List<String> getSupportingMethods(String packageName, String className, String methodName) {
		MethodDefinition methodDef = getMethodDefinition(packageName, className, null, methodName);
		List<MethodDefinition> supportingMethods = null;
		List<String> supportingMethodNames = new ArrayList<String>();
		if(methodDef != null) {
			supportingMethods = methodDef.getSupportingMethods();
			if(supportingMethods != null) {
				for (MethodDefinition mDef : supportingMethods) {
					supportingMethodNames.add(mDef.getMethodNameKey());
				}
			}
		}
		return supportingMethodNames;
	}
	
}
