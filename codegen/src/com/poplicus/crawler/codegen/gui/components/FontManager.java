package com.poplicus.crawler.codegen.gui.components;

import java.awt.Font;

public class FontManager {
	
	private static FontManager fontManager = null;
	
	private FontManager() {
		
	}
	
	public Font getBorderFontBold() {
		Font font = new Font("Tahoma", Font.BOLD, 11);
		return font;
	}
	
	public Font getBorderFontPlain() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}

	public Font getLabelFont() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}

	public Font getTextFieldFont() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}

	public Font getTabbedPaneFont() {
		Font font = new Font("Tahoma", Font.BOLD, 11);
		return font;
	}

	public Font getTableHeaderFont() {
		Font font = new Font("Tahoma", Font.BOLD, 11);
		return font;
	}

	public Font getTableCellFont() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}

	public Font getCheckBoxFont() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}
	
	public Font getButtonFontPlain() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}

	public Font getComboFontPlain() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}
	
	public Font getTreeFontPlain() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}

	public Font getPopupFontPlain() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}
	
	public Font getMenuItemFontPlain() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}

	public Font getRadioButtonFontPlain() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}

	public Font getOptionPaneMessageFontPlain() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}

	public Font getOptionPaneButtonFontPlain() {
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}

	public static FontManager getInstance() {
		if(fontManager == null) {
			fontManager = new FontManager();
		}
		return fontManager;
	}

}
