package com.poplicus.crawler.codegen.gui.components;

import javax.swing.JComboBox;

public class PopComboBox extends JComboBox {
	
	private static final long serialVersionUID = -8693231543504559519L;
	
	private static FontManager fontManager = FontManager.getInstance();

	public PopComboBox() {
		super();
		this.setFont(fontManager.getComboFontPlain());
	}
	
	public PopComboBox(String[] items) {
		super(items);
		this.setFont(fontManager.getComboFontPlain());
	}
	
}
