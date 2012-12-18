package com.poplicus.crawler.codegen.gui.components;

import javax.swing.JCheckBox;

public class PopCheckBox extends JCheckBox {
	
	private static final long serialVersionUID = 670320027017121505L;
	
	private static FontManager fontManager = FontManager.getInstance();

	public PopCheckBox() {
		super();
		this.setFont(fontManager.getCheckBoxFont());
	}
	
	public PopCheckBox(String text) {
		super(text);
		this.setFont(fontManager.getCheckBoxFont());
	}


}
