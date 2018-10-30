import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
/*
 * 
 * Program: 	Operacje na kolekcjach
 * 
 * Plik:		ViewAnimalList.java
 * 
 * Autor:		Tymoteusz Frankiewicz
 *
 * Data:		pazdziernik 2018
 * 
 * Zawiera klas� ViewAnimalList, kt�ra jest 
 * dostosowan� tabelk� do wy�wietlania zwierz�t
 * 
 * */
public class ViewAnimalsList extends JScrollPane{
	private static final long serialVersionUID = 1L;
	
	
	private GroupOfAnimals list;
	private JTable table;
	private DefaultTableModel model;
	public ViewAnimalsList(GroupOfAnimals list, int width, int height) {
		this.list = list;
		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createTitledBorder("Zwierz�ta"));
		
		String[] tableHeader = {"Rodzaj", "Gatunek", "Wiek", "Ilo�� ko�czyn"};
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
		this.setViewportView(table);
	}
	
	public void refreshView() {
		model.setRowCount(0);
		for(Animal a: list) {
			if(a != null) {
				String[] row = {a.getKind().toString(), a.getSpecies(), Integer.toString(a.getAge()), Integer.toString(a.getLegsNumber())};
				model.addRow(row);
			}
		}
	}
	
	public int getSelectedIndex() {
		int index = table.getSelectedRow();
		if(index < 0) {
			JOptionPane.showMessageDialog(this, "��dane zwierze nie jest zaznaczone", "B��d", JOptionPane.ERROR_MESSAGE);
		} 
		return index;
	}
	
	public GroupOfAnimals getGroup() {
		return list;
	}

}