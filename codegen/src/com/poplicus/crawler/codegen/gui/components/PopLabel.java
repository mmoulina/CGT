package com.poplicus.crawler.codegen.gui.components;

import javax.swing.JLabel;

public class PopLabel extends JLabel {
	
	private static final long serialVersionUID = 365469880519470716L;
	
	private static FontManager fontManager = FontManager.getInstance();
	
	public PopLabel() {
		super();
		this.setFont(fontManager.getLabelFont());
	}
	
	public PopLabel(String text) {
		super(text);
		this.setFont(fontManager.getLabelFont());
	}
	
}
