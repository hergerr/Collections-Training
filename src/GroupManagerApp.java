import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
* Program: 	Aplikacja okienkowa z GUI, kt�ra umorzliwai zarz�dzanie grupami obiekt�w
*			klasy Animal
*
* Plik:		GroupManagerApp.java
*
* Autor:	Tymoteusz Frankiewicz
*
* Data: 	Pazdziernik 2018
* 
* Zawiera klase GroupManagerApp i ViewGroupList
*/

public class GroupManagerApp extends JFrame implements ActionListener, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String GREETING_MESSAGE = "Program do zarz�dzania grupami zwierz�t " + "- wersja okienkowa\n\n"
			+ "Autor: Tymoteusz Frankiewicz\n" + "Data:  pa�dziernik 2017 r.\n";

	private static final String ALL_GROUPS_FILE = "lista_grup.bin";

	public static void main(String[] args) {
		new GroupManagerApp();
	}


	private List<GroupOfAnimals> currentList = new ArrayList<GroupOfAnimals>();

	JMenuBar menuBar = new JMenuBar();
	JMenu menuGroups = new JMenu("Grupy");
	JMenu menuSpecialGroups = new JMenu("Grupy specjalne");
	JMenu menuAbout = new JMenu("O programie");

	JMenuItem menuNewGroup = new JMenuItem("Utw�rz grup�");
	JMenuItem menuEditGroup = new JMenuItem("Edytuj grup�");
	JMenuItem menuDeleteGroup = new JMenuItem("Usu� grup�");
	JMenuItem menuLoadGroup = new JMenuItem("za�aduj grup� z pliku");
	JMenuItem menuSaveGroup = new JMenuItem("Zapisz grup� do pliku");

	JMenuItem menuGroupUnion = new JMenuItem("Po��czenie grup");
	JMenuItem menuGroupIntersection = new JMenuItem("Cz�� wsp�lna grup");
	JMenuItem menuGroupDifference = new JMenuItem("R�nica grup");
	JMenuItem menuGroupSymmetricDiff = new JMenuItem("R�nica symetryczna grup");
	JMenuItem menuAuthor = new JMenuItem("Autor");

	JButton buttonNewGroup = new JButton("Utw�rz");
	JButton buttonEditGroup = new JButton("Edytuj");
	JButton buttonDeleteGroup = new JButton(" Usu� ");
	JButton buttonLoadGroup = new JButton("Otw�rz");
	JButton buttonSavegroup = new JButton("Zapisz");

	JButton buttonUnion = new JButton("Suma");
	JButton buttonIntersection = new JButton("Iloczyn");
	JButton buttonDifference = new JButton("R�nica");
	JButton buttonSymmetricDiff = new JButton("R�nica symetryczna");

	JPanel panel = new JPanel();

	ViewGroupList viewList;

	public GroupManagerApp() {
		this.setTitle("GroupManager - zarz�dzanie grupami zwierz�t");
		this.setSize(450, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {

			// gdy naciskamy guzik w aplikacji (wywolanie dispose())
			@Override
			public void windowClosed(WindowEvent event) {
				try {
					saveGroupListToFile(ALL_GROUPS_FILE);
					JOptionPane.showMessageDialog(null, "Dane zosta�y zapisane do pliku " + ALL_GROUPS_FILE);
				} catch (AnimalException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
				}
			}

			// gdy naciskamy czerwony krzyzyk
			@Override
			public void windowClosing(WindowEvent e) {
				windowClosed(e);
			}

		});

		try {
			loadGroupListFromFile(ALL_GROUPS_FILE);
			JOptionPane.showMessageDialog(null, "Dane zosta�y wczytane z pliku " + ALL_GROUPS_FILE);
		} catch (AnimalException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
		}

		this.setJMenuBar(menuBar);
		menuBar.add(menuGroups);
		menuBar.add(menuSpecialGroups);
		menuBar.add(menuAbout);

		menuGroups.add(menuNewGroup);
		menuGroups.add(menuEditGroup);
		menuGroups.add(menuDeleteGroup);
		menuGroups.addSeparator();
		menuGroups.add(menuLoadGroup);
		menuGroups.add(menuSaveGroup);

		menuSpecialGroups.add(menuGroupUnion);
		menuSpecialGroups.add(menuGroupIntersection);
		menuSpecialGroups.add(menuGroupDifference);
		menuSpecialGroups.add(menuGroupSymmetricDiff);

		menuAbout.add(menuAuthor);

		menuNewGroup.addActionListener(this);
		menuEditGroup.addActionListener(this);
		menuDeleteGroup.addActionListener(this);
		menuLoadGroup.addActionListener(this);
		menuSaveGroup.addActionListener(this);
		menuGroupUnion.addActionListener(this);
		menuGroupIntersection.addActionListener(this);
		menuGroupDifference.addActionListener(this);
		menuGroupSymmetricDiff.addActionListener(this);
		menuAuthor.addActionListener(this);

		buttonNewGroup.addActionListener(this);
		buttonEditGroup.addActionListener(this);
		buttonDeleteGroup.addActionListener(this);
		buttonLoadGroup.addActionListener(this);
		buttonSavegroup.addActionListener(this);
		buttonUnion.addActionListener(this);
		buttonIntersection.addActionListener(this);
		buttonDifference.addActionListener(this);
		buttonSymmetricDiff.addActionListener(this);

		viewList = new ViewGroupList(currentList, 400, 250);
		viewList.refreshView();

		panel.add(viewList);
		panel.add(buttonNewGroup);
		panel.add(buttonEditGroup);
		panel.add(buttonDeleteGroup);
		panel.add(buttonLoadGroup);
		panel.add(buttonSavegroup);
		panel.add(buttonUnion);
		panel.add(buttonIntersection);
		panel.add(buttonDifference);
		panel.add(buttonSymmetricDiff);

		this.add(panel);
		this.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	void loadGroupListFromFile(String file_name) throws AnimalException {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file_name))) {
			currentList = (List<GroupOfAnimals>) in.readObject();
		} catch (FileNotFoundException e) {
			throw new AnimalException("Nie odnaleziono pliku " + file_name);
		} catch (Exception e) {
			throw new AnimalException("Wyst�pi� b��d podczas odczytu danych z pliku.");
		}
	}

	void saveGroupListToFile(String file_name) throws AnimalException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file_name))) {
			out.writeObject(currentList);
		} catch (FileNotFoundException e) {
			throw new AnimalException("Nie odnaleziono pliku " + file_name);
		} catch (IOException e) {
			throw new AnimalException("Wyst�pi� b��d podczas zapisu danych do pliku.");
		}
	}

	private GroupOfAnimals chooseGroup(Window parent, String message) {
		Object[] groups = currentList.toArray();
		GroupOfAnimals group = (GroupOfAnimals) JOptionPane.showInputDialog(parent, message, "Wybierz grup�",
				JOptionPane.QUESTION_MESSAGE, null, groups, null);
		return group;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();

		try {
			if (source == menuNewGroup || source == buttonNewGroup) {
				GroupOfAnimals group = GroupOfAnimalsWindowDialog.createNewGroupOfAnimals(this);
				if (group != null) {
					currentList.add(group);
				}
			}

			if (source == menuEditGroup || source == buttonEditGroup) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfAnimals> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					new GroupOfAnimalsWindowDialog(this, iterator.next());
				}
			}

			if (source == menuDeleteGroup || source == buttonDeleteGroup) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfAnimals> iterator = currentList.iterator();
					while (index-- >= 0)
						iterator.next();
					iterator.remove();
				}
			}

			if (source == menuLoadGroup || source == buttonLoadGroup) {
				JFileChooser chooser = new JFileChooser(".");
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					GroupOfAnimals group = GroupOfAnimals.readFromFile(chooser.getSelectedFile().getName());
					currentList.add(group);
				}
			}

			if (source == menuSaveGroup || source == buttonSavegroup) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfAnimals> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					GroupOfAnimals group = iterator.next();

					JFileChooser chooser = new JFileChooser(".");
					int returnVal = chooser.showSaveDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						GroupOfAnimals.printToFile(chooser.getSelectedFile().getName(), group);
					}
				}
			}

			if (source == menuGroupUnion || source == buttonUnion) {
				String message1 = "SUMA GRUP\n\n" + "Tworzenie grupy zawieraj�cej wszystkie osoby z grupy pierwszej\n"
						+ "oraz wszystkie osoby z grupy drugiej.\n" + "Wybierz pierwsz� grup�:";
				String message2 = "SUMA GRUP\n\n" + "Tworzenie grupy zawieraj�cej wszystkie osoby z grupy pierwszej\n"
						+ "oraz wszystkie osoby z grupy drugiej.\n" + "Wybierz drug� grup�:";
				GroupOfAnimals group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfAnimals group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add(GroupOfAnimals.createGroupUnion(group1, group2));
			}

			if (source == menuGroupIntersection || source == buttonIntersection) {
				String message1 = "ILOCZYN GRUP\n\n"
						+ "Tworzenie grupy zwierz�t, kt�re nale�� zar�wno do grupy pierwszej,\n"
						+ "jak i do grupy drugiej.\n" + "Wybierz pierwsz� grup�:";
				String message2 = "ILOCZYN GRUP\n\n"
						+ "Tworzenie grupy os�b, kt�re nale�� zar�wno do grupy pierwszej,\n"
						+ "jak i do grupy drugiej.\n" + "Wybierz drug� grup�:";
				GroupOfAnimals group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfAnimals group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add(GroupOfAnimals.createGroupIntersection(group1, group2));
			}

			if (source == menuGroupDifference || source == buttonDifference) {
				String message1 = "RӯNICA GRUP\n\n" + "Tworzenie grupy os�b, kt�re nale�� do grupy pierwszej\n"
						+ "i nie ma ich w grupie drugiej.\n" + "Wybierz pierwsz� grup�:";
				String message2 = "RӯNICA GRUP\n\n" + "Tworzenie grupy os�b, kt�re nale�� do grupy pierwszej\n"
						+ "i nie ma ich w grupie drugiej.\n" + "Wybierz drug� grup�:";
				GroupOfAnimals group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfAnimals group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add(GroupOfAnimals.createGroupDifference(group1, group2));
			}

			if (source == menuGroupSymmetricDiff || source == buttonSymmetricDiff) {
				String message1 = "RӯNICA SYMETRYCZNA GRUP\n\n"
						+ "Tworzenie grupy zawieraj�cej osoby nale��ce tylko do jednej z dw�ch grup,\n"
						+ "Wybierz pierwsz� grup�:";
				String message2 = "RӯNICA SYMETRYCZNA GRUP\n\n"
						+ "Tworzenie grupy zawieraj�cej osoby nale��ce tylko do jednej z dw�ch grup,\n"
						+ "Wybierz drug� grup�:";
				GroupOfAnimals group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfAnimals group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add(GroupOfAnimals.createGroupSymmetricDiff(group1, group2));
			}

			if (source == menuAuthor) {
				JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
			}
		} catch (AnimalException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
		}

		viewList.refreshView();

	}

}


