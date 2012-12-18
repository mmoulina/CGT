package com.poplicus.crawler.codegen.gui.components;

import javax.swing.JFrame;

public class PopFrame extends JFrame {
	
	private static final long serialVersionUID = 3069386677708714983L;
	
	private PopTree rootTree = null;
	
	public PopFrame() {
		super();
	}
	
	public void setRootTree(PopTree rootTree) {
		this.rootTree = rootTree; 
	}
	
	public PopTree getRootTree() {
		return rootTree;
	}

}
