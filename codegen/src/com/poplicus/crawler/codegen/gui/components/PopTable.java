package com.poplicus.crawler.codegen.gui.components;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PopTable extends JTable {

	private static final long serialVersionUID = -3375344999696363388L;
	
	private static FontManager fontManager = FontManager.getInstance();
	
	private DefaultTableModel dtModel = null;
	
	private String name = null;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PopTable() {
		super();
		this.getTableHeader().setFont(fontManager.getTableHeaderFont());
		this.setFont(fontManager.getTableCellFont());
	}
	
	public PopTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		dtModel = new PopTableModel();
		dtModel.setColumnIdentifiers(columnNames);
		this.setModel(dtModel);
		this.getTableHeader().setFont(fontManager.getTableHeaderFont());
		this.setFont(fontManager.getTableCellFont());
	}
	
	public PopTable(Object[][] rowData, Object[] columnNames, String tableType) {
		super(rowData, columnNames);
		if(tableType != null) {
			if(tableType.equalsIgnoreCase("Parameter")) {
				dtModel = new PopParameterTableModel();
			} else if(tableType.equalsIgnoreCase("ResultSet")) {
				dtModel = new PopResultSetTableModel();
			} else {
				dtModel = new PopTableModel();
			}
		} else {
			dtModel = new PopTableModel();
		}
		dtModel.setColumnIdentifiers(columnNames);
		this.setModel(dtModel);
		this.getTableHeader().setFont(fontManager.getTableHeaderFont());
		this.setFont(fontManager.getTableCellFont());

	}

	public void addColumn(String columnName) {
		((DefaultTableModel) this.getModel()).addColumn(columnName);
	}
	
	public void addRow(Object[] row) {
		dtModel.addRow(row);
	}
	
	public DefaultTableModel getDefaultModel() {
		return dtModel;
	}
	
	class PopTableModel extends DefaultTableModel {
		
		private static final long serialVersionUID = 6123215401512672352L;

		public PopTableModel() {
			super();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}

	class PopParameterTableModel extends DefaultTableModel {
		
		private static final long serialVersionUID = 6123215401512672352L;

		public PopParameterTableModel() {
			super();
		}
		
		public boolean isCellEditable(int row, int col) {
			if(col == 2 || col == 3) {
				return true;
			}
			return false;
		}
		
	}

	class PopResultSetTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 7337080834675648928L;

		public PopResultSetTableModel() {
			super();
		}
		
		public boolean isCellEditable(int row, int col) {
			if(col < 3) {
				return false;
			}
			return true;
		}
		
	}

}
