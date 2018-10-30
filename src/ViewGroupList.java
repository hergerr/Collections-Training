import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

class ViewGroupList extends JScrollPane {
	private static final long serialVersionUID = 1L;

	private List<GroupOfAnimals> list;
	private JTable table;
	private DefaultTableModel model;

	public ViewGroupList(List<GroupOfAnimals> list, int width, int height) {
		this.list = list;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createTitledBorder("Lista grup:"));

		String[] tableHeader = { "Nazwa grupy", "Typ kolekcji", "Liczba zwierz¹t" };
		model = new DefaultTableModel(tableHeader, 0);
		table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
		};

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		setViewportView(table);
	}

	public void refreshView() {
		model.setRowCount(0);
		for (GroupOfAnimals g : list) {
			if(g != null) {
				String[] row = {g.getName(), g.getType().toString(), Integer.toString(g.size())};
				model.addRow(row);
			}
		}
	}
	
	public int getSelectedIndex() {
		int index = table.getSelectedRow();
		if(index < 0) {
			JOptionPane.showMessageDialog(this, "¯¹dana grupa nie jest zaznaczona", "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}

}