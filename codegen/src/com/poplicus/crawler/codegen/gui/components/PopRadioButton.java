package com.poplicus.crawler.codegen.gui.components;

import javax.swing.JRadioButton;

public class PopRadioButton extends JRadioButton {

	private static final long serialVersionUID = -1030298765220656665L;
	
	private static FontManager fontManager = FontManager.getInstance();

	public PopRadioButton() {
		super();
		this.setFont(fontManager.getRadioButtonFontPlain());
	}
	
	public PopRadioButton(String text) {
		super(text);
		this.setFont(fontManager.getRadioButtonFontPlain());
	}

}
