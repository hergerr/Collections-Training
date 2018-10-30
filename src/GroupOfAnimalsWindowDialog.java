import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GroupOfAnimalsWindowDialog extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	JMenuBar menuBar = new JMenuBar();
	JMenu listGroup = new JMenu("Lista zwierz¹t");
	JMenu sortGroup = new JMenu("Sortowanie");
	JMenu propertiesGroup = new JMenu("W³aœciwoœci");
	JMenu aboutProgramm = new JMenu("O programie");
	
	JMenuItem menuNewAnimal = new JMenuItem("Dodaj nowe zwierze");
	JMenuItem menuEditAnimal = new JMenuItem("Edytuj zwierze");
	JMenuItem menuRemoveAnimal = new JMenuItem("Usuñ zwierze");
	JMenuItem menuReadAnimal = new JMenuItem("Wczytaj zwierze z pliku");
	JMenuItem menuSaveAnimal = new JMenuItem("Zapisz zwierze do pliku");
	
	JMenuItem menuSortBySpeciesAlphabetically = new JMenuItem("Sortuj alfabetycznie (gatunek)");
	JMenuItem menuSortByAge = new JMenuItem("Sortuj wg. wieku");
	JMenuItem menuSortByKindAlphabetically = new JMenuItem("Sortuj alfabetycznie (rodzaj)");
	JMenuItem menuSortByLegsNumber = new JMenuItem("Sortuj wg. iloœci koñczyn");
	
	JMenuItem menuChangeName = new JMenuItem("Zmieñ nazwe");
	JMenuItem menuChangeCollection = new JMenuItem("Zmieñ kolekcje");
	
	JLabel labelGroupName = new JLabel("Nazwa grupy: ");
	JTextField textFieldGroupName = new JTextField(25);
	
	JLabel labelCollectionKind = new JLabel("Rodzaj kolekcji: ");
	JTextField textFieldCollectionKind = new JTextField(25);
	
	JButton buttonNewAnimal = new JButton("Dodaj nowe zwierze");
	JButton buttonEditAnimal = new JButton("Edytuj zwierze");
	JButton buttonRemoveAnimal = new JButton("Usuñ zwierze");
	JButton buttonReadAnimalFromFile = new JButton("Wczytaj zwierze z pliku");
	JButton buttonSaveAnimalToFile = new JButton("Zapisz zwierze do pliku");
	ViewAnimalsList viewAnimalsList;
	Animal animal;
	GroupOfAnimals group;
	
	JPanel panel = new JPanel();
	
	//konstruktor okna dialogowego
	public GroupOfAnimalsWindowDialog(GroupManagerApp groupManagerApp, GroupOfAnimals group) {
		super(groupManagerApp, Dialog.ModalityType.DOCUMENT_MODAL);
		this.setTitle("Modyfikacja grupy zwierz¹t");
		this.setSize(450, 450);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setJMenuBar(menuBar);
		
		this.group = group;
		menuBar.add(listGroup);
		menuBar.add(sortGroup);
		menuBar.add(propertiesGroup);
		menuBar.add(aboutProgramm);
		
		listGroup.add(menuNewAnimal);
		listGroup.add(menuEditAnimal);
		listGroup.add(menuRemoveAnimal);
		listGroup.addSeparator();
		listGroup.add(menuReadAnimal);
		listGroup.add(menuSaveAnimal);
		
		sortGroup.add(menuSortBySpeciesAlphabetically);
		sortGroup.add(menuSortByAge);
		sortGroup.add(menuSortByKindAlphabetically);
		sortGroup.add(menuSortByLegsNumber);
		
		propertiesGroup.add(menuChangeName);
		propertiesGroup.add(menuChangeCollection);
		
		menuNewAnimal.addActionListener(this);
		menuEditAnimal.addActionListener(this);
		menuRemoveAnimal.addActionListener(this);
		menuReadAnimal.addActionListener(this);
		menuSaveAnimal.addActionListener(this);
		menuSortBySpeciesAlphabetically.addActionListener(this);
		menuSortByAge.addActionListener(this);
		menuSortByKindAlphabetically.addActionListener(this);
		menuSortByLegsNumber.addActionListener(this);
		menuChangeName.addActionListener(this);
		menuChangeCollection.addActionListener(this);
		aboutProgramm.addActionListener(this);
		
		buttonNewAnimal.addActionListener(this);
		buttonEditAnimal.addActionListener(this);
		buttonRemoveAnimal.addActionListener(this);
		buttonReadAnimalFromFile.addActionListener(this);
		buttonSaveAnimalToFile.addActionListener(this);
		
		textFieldGroupName.setText(group.getName());
		textFieldCollectionKind.setText(group.getType().toString());
		
		panel.add(labelGroupName);
		panel.add(textFieldGroupName);
		panel.add(labelCollectionKind);
		panel.add(textFieldCollectionKind);
		viewAnimalsList = new ViewAnimalsList(group, 400, 250);
		viewAnimalsList.refreshView();
		panel.add(viewAnimalsList);
		panel.add(buttonNewAnimal);
		panel.add(buttonEditAnimal);
		panel.add(buttonRemoveAnimal);
		panel.add(buttonReadAnimalFromFile);
		panel.add(buttonSaveAnimalToFile);
		
		textFieldGroupName.setEditable(false);
		textFieldCollectionKind.setEditable(false);
		
		
		this.add(panel);
		this.setVisible(true);
	}


	// Tutaj wykonuje operacje inicjalizacji nowej grupy zwierz¹t
	public static GroupOfAnimals createNewGroupOfAnimals(GroupManagerApp groupManagerApp) {
		String newGroupName = JOptionPane.showInputDialog(groupManagerApp, "Podaj nazwe grupy");
		
		GroupType newCollectionType = (GroupType) JOptionPane.showInputDialog(null, "", "Title",
		        JOptionPane.QUESTION_MESSAGE, null, GroupType.values(),"Regular");
		GroupOfAnimals newGroup;
		try {
			newGroup = new GroupOfAnimals(newCollectionType, newGroupName);
			new GroupOfAnimalsWindowDialog(groupManagerApp, newGroup);  
			return newGroup;
		} catch (AnimalException e) {
			JOptionPane.showMessageDialog(groupManagerApp, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}






	@Override
	public void actionPerformed(ActionEvent e) {
		Object eventSource = e.getSource();
		try {
			if(eventSource == buttonNewAnimal || eventSource == menuNewAnimal) {
				animal = AnimalWindowDialog.createNewAnimal(this);
				if(animal != null) viewAnimalsList.getGroup().add(animal);
			}
			if(eventSource == buttonEditAnimal || eventSource == menuEditAnimal) {
				int index = viewAnimalsList.getSelectedIndex();
				while(index >= 0) {
					Iterator<Animal> iterator = group.iterator();
					while(index-- > 0)
						iterator.next();
					new AnimalWindowDialog(this, iterator.next());
				}
			}
			if(eventSource == buttonRemoveAnimal || eventSource == menuRemoveAnimal) {
				int index = viewAnimalsList.getSelectedIndex();
				while(index >= 0) {
					Iterator<Animal> iterator = group.iterator();
					while(index-- >= 0)
						iterator.next();
					iterator.remove();
				}
			}
			
			//Tutaj dla wygody korzystam z serializacji obiektów
			if(eventSource == buttonReadAnimalFromFile || eventSource == menuReadAnimal) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					animal = Animal.serializeFromBinaryFile(chooser.getSelectedFile().toString());
					group.add(animal);
				}
			}
			if(eventSource == buttonSaveAnimalToFile || eventSource == menuSaveAnimal) {
				int index = viewAnimalsList.getSelectedIndex();
				if (index >= 0) {
					Iterator<Animal> iterator = group.iterator();
					while(index-- > 0)
						iterator.next();
					animal = iterator.next();
					JFileChooser chooser = new JFileChooser();
					int returnVal = chooser.showSaveDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						Animal.serializeToBinaryFile(chooser.getSelectedFile().toString(), animal);
					}
				}
			}
			if(eventSource == menuSortBySpeciesAlphabetically) {
				try {
					group.sortSpecies();
				} catch(AnimalException exception) {
					JOptionPane.showMessageDialog(this, exception.getMessage(), "B³ad", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			if(eventSource == menuSortByKindAlphabetically) {
				try {
					group.sortKind();	
				} catch(AnimalException exception) {
					JOptionPane.showMessageDialog(this, exception.getMessage(), "B³ad", JOptionPane.ERROR_MESSAGE);
				}
			
			}
			if(eventSource == menuSortByAge) {
				try {
					group.sortAge();
				} catch(AnimalException exception) {
					JOptionPane.showMessageDialog(this, exception.getMessage(), "B³ad", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			if(eventSource == menuSortByLegsNumber) {
				try {
					group.sortLegsNumber();
				} catch(AnimalException exception) {
					JOptionPane.showMessageDialog(this, exception.getMessage(), "B³ad", JOptionPane.ERROR_MESSAGE);
				}

			}
			if(eventSource == menuChangeName) {
				String newCollectionName = JOptionPane.showInputDialog(this, "Podaj now¹ nazwe kolekcji: ");
				group.setName(newCollectionName);
				textFieldGroupName.setText(newCollectionName);
			}
			if(eventSource == menuChangeCollection) {
				GroupType newCollectionType = (GroupType) JOptionPane.showInputDialog(null, "", "Title",
				        JOptionPane.QUESTION_MESSAGE, null, GroupType.values(),"Regular");
				group.setType(newCollectionType);
				textFieldCollectionKind.setText(newCollectionType.getTypeName());
			}
			if(eventSource == aboutProgramm) {
				
			}
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		viewAnimalsList.refreshView();
	}
	
}