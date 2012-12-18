package com.poplicus.crawler.codegen.gui.components;

import javax.swing.JButton;

public class PopButton extends JButton {
	
	private static final long serialVersionUID = 5942793574166461696L;
	
	private static FontManager fontManager = FontManager.getInstance();

	public PopButton() {
		super();
		this.setFont(fontManager.getButtonFontPlain());
	}
	
	public PopButton(String text) {
		super(text);
		this.setFont(fontManager.getButtonFontPlain());
	}

}
