import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	String[] columnNames = { "id", "firstname", "lastname", "number",
			"address", "city", "zipcode" };
	List<List<Object>> data = new ArrayList<List<Object>>();

	public MyTableModel(List<List<Object>> person) {
		this.data = person;
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		return data.get(row).get(column);
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Class<?> getColumnClass(int column) {
		if (data.get(0).get(column) != null) {
			return getValueAt(0, column).getClass();
		} else {
			return String.class;
		}
	}

	public void setValueAt(Object value, int row, int column) {
		/*
		 * change table when user edits, also
		 * update database to same values
		 */
		System.out.println("SETTING VALUE");
		data.get(row).set(column, value);
		ConnectDB db = new ConnectDB();
		db.edit(value, (row + 1), columnNames[column]);
		fireTableCellUpdated(row, column);
	}

	public boolean isCellEditable(int row, int col) {
		if (col == 0) {
			return false;
		}
		return true;
	}
}
