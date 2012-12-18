package com.poplicus.crawler.codegen.gui.components;

import javax.swing.JTabbedPane;

public class PopTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 8552186267613050621L;

	private static FontManager fontManager = FontManager.getInstance();

	public PopTabbedPane() {
		super();
		this.setFont(fontManager.getTabbedPaneFont());
	}

}
