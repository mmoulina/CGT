package com.poplicus.crawler.codegen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Imports {
	
	private static Imports instance = null;
	
	private Map<Object, String> imports = new HashMap<Object, String>();
	
	private Map<Object, String> importsCollection = new HashMap<Object, String>();
	
	private Imports() {
		//importsCollection would be populated as and when required
		/*importsCollection.put(key, value);
		importsCollection.put(key, value);
		importsCollection.put(key, value);
		importsCollection.put(key, value);
		importsCollection.put(key, value);*/
	}
	
	public static Imports getInstance() {
		if(instance == null) {
			instance = new Imports();
		}
		return instance;
	}
	
	public void addImport(Object key, String value) {
		if(!imports.containsKey(key)) {
			if(importsCollection.containsKey(key)) {
				imports.put(key, importsCollection.get(key));
			} else {
				if(value != null) {
					importsCollection.put(key, value);
					imports.put(key, value);
				}
			}
		}
	}
	
	private StringBuffer getDefaultImports() {
		StringBuffer code = new StringBuffer();
		code.append("using System;").append(Constants.NEW_LINE);
		code.append("using System.Collections.Generic;").append(Constants.NEW_LINE);
		code.append("using csExWB;").append(Constants.NEW_LINE);
		code.append("using Functions;").append(Constants.NEW_LINE);
		return code;
	}
	
	private StringBuffer getAdditionalImports() {
		StringBuffer code = new StringBuffer();
		Set<Object> keySet = imports.keySet();
		Iterator<Object> iterator = keySet.iterator();
		while(iterator.hasNext()){
			code.append(Constants.USING_SPACE).append(imports.get(iterator.next())).append(Constants.NEW_LINE);
		}
		return code;
	}
	
	public StringBuffer toStringBuffer() {
		StringBuffer code = new StringBuffer();
		code.append(getDefaultImports());
		code.append(getAdditionalImports());
		code.append(Constants.NEW_LINE);
		return code;
	}
	
}
