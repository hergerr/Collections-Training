import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 *  Program: Operacje na kolekcjach
 *     Plik: AnimalWindowDialog.java
 *     
 *           Dialog umo¿liwiaj¹cy dodawanie/edycje pojedynczego zwierzêcia
 *           
 *
 *    Autor: Tymoteusz Frankiewicz
 *     Data:  pazdziernik 2018 r.
 */

public class AnimalWindowDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Animal animal;
	
	JLabel kindLabel = new JLabel("Rodzaj: ");
	JLabel speciesLabel = new JLabel("Gatunek: ");
	JLabel ageLabel = new JLabel("Wiek: ");
	JLabel legsNumberLabel = new JLabel("Liczba koñczyn: ");
	
	JComboBox<Kind> kindComboBox =  new JComboBox<Kind>(Kind.values());
	JTextField speciesTextField = new JTextField(10);
	JTextField ageTextField = new JTextField(10);
	JTextField legsNumberTextField = new JTextField(10);
	
	JButton OKButton = new JButton("OK");
	JButton cancelButton = new JButton("Anuluj");
	
	public AnimalWindowDialog(Window parent, Animal animal) {
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		this.setSize(140, 300);
		this.setLocationRelativeTo(parent);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.animal = animal;
		if(animal == null) {
			this.setTitle("Nowe zwierze");
		} else {
			this.setTitle(animal.toString());
			kindComboBox.setSelectedItem(animal.getKind());
			speciesTextField.setText(animal.getSpecies());
			ageTextField.setText(Integer.toString(animal.getAge()));
			legsNumberTextField.setText(Integer.toString(animal.getLegsNumber()));
		}
		
		OKButton.addActionListener(this);
		cancelButton.addActionListener(this);
		JPanel panel = new JPanel();
		
		panel.add(kindLabel);
		panel.add(kindComboBox);
		
		panel.add(speciesLabel);
		panel.add(speciesTextField);
		
		panel.add(ageLabel);
		panel.add(ageTextField);
		
		panel.add(legsNumberLabel);
		panel.add(legsNumberTextField);
		
		panel.add(OKButton);
		panel.add(cancelButton);
		
		this.add(panel);
		this.setVisible(true);
	}

	public static Animal createNewAnimal(Window window) {
		AnimalWindowDialog dialog = new AnimalWindowDialog(window, null);
		return dialog.animal;
	}

	public static void changeAnimalData(GroupOfAnimalsWindowDialog groupOfAnimalsWindowDialog, Animal animal) {
		new AnimalWindowDialog(groupOfAnimalsWindowDialog, animal);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if (source == OKButton) {
			try {
				if (animal == null) {
					animal = new Animal(Integer.parseInt(ageTextField.getText()), Integer.parseInt(legsNumberTextField.getText()), speciesTextField.getText());
				} else {
					animal.setAge(Integer.parseInt(ageTextField.getText()));
					animal.setLegsNumber(Integer.parseInt(legsNumberTextField.getText()));
					animal.setSpecies(speciesTextField.getText());
				}
				
				animal.setKind((Kind)kindComboBox.getSelectedItem());
				dispose();
			} catch (AnimalException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Nieznany b³¹d", JOptionPane.ERROR_MESSAGE);
			}
		}

		if (source == cancelButton) {
			dispose();
		}
		
	}

}
