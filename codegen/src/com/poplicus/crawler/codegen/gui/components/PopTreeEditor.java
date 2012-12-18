package com.poplicus.crawler.codegen.gui.components;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.TreeCellEditor;

public class PopTreeEditor implements TreeCellEditor {

	@Override
	public void addCellEditorListener(CellEditorListener arg0) {
		System.out.println("<PopTreeEditor> Inside addCellEditorListener..");
	}

	@Override
	public void cancelCellEditing() {
		System.out.println("<PopTreeEditor> Inside cancelCellEditing..");
	}

	@Override
	public Object getCellEditorValue() {
		System.out.println("<PopTreeEditor> Inside getCellEditorValue..");

		
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject arg0) {
		System.out.println("<PopTreeEditor> Inside isCellEditable..");
		return true;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener arg0) {
		System.out.println("<PopTreeEditor> Inside removeCellEditorListener..");
	}

	@Override
	public boolean shouldSelectCell(EventObject arg0) {
		System.out.println("<PopTreeEditor> Inside shouldSelectCell..");
		return false;
	}

	@Override
	public boolean stopCellEditing() {
		System.out.println("<PopTreeEditor> Inside stopCellEditing..");
		return false;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree arg0, Object arg1,
												boolean arg2, boolean arg3, boolean arg4, int arg5) {
		System.out.println("<PopTreeEditor> Inside getTreeCellEditorComponent..");
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new GridBagLayout());

		JButton testButton = new JButton("Testing..");
		
		testPanel.add(testButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
				GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		
		testPanel.setSize(250, 250);
		testPanel.setVisible(true);	
		
		return testPanel;
		
	}
	
}
