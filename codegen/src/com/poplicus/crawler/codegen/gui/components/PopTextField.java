package com.poplicus.crawler.codegen.gui.components;

import javax.swing.JTextField;

public class PopTextField extends JTextField {

	private static final long serialVersionUID = -6335251823403762120L;
	
	private static FontManager fontManager = FontManager.getInstance();
	
	public PopTextField() {
		super();
		this.setFont(fontManager.getTextFieldFont());
	}
	
//	processKe
//	@Override
//	protected void processKeyEvent(KeyEvent keyEvent) {
//		int keyCode = keyEvent.getKeyCode();
//		System.out.println("<KeyCode> " + keyCode);
//		if(!(keyCode == 48 || keyCode == 49)) {
//			keyEvent.consume();
//		}
//	}

}
