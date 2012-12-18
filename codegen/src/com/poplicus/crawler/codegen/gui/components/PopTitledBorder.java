package com.poplicus.crawler.codegen.gui.components;

import javax.swing.border.TitledBorder;

public class PopTitledBorder extends TitledBorder {
	
	private static final long serialVersionUID = 4743336296978392550L;
	
	private static FontManager fontManager = FontManager.getInstance();
	
	public PopTitledBorder(String title) {
		super(title);
		this.setTitleFont(fontManager.getBorderFontBold());
	}

}
