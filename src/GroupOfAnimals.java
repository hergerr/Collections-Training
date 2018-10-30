import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

/*
 * 
 * Program: 	Operacje na kolekcjach
 * 
 * Plik:		GroupOfAnimals.java
 * 
 * Autor:		Tymoteusz Frankiewicz
 *
 * Data:		pazdziernik 2018
 * 
 * Zawiera klasê GroupOfAnimals i typ wyliczeniowy GroupType
 * Dodatkowo zaimplementowane funckje do operacji na zbiorach
 * 
 * */

enum GroupType{
	VECTOR("Lista (klasa Vector)"),
	ARRAY_LIST("Lista (klasa ArrayList)"),
	LINKED_LIST("Lista (klasa LinkedList)"),
	HASH_SET("Zbiór (klasa HashSet)"),
	TREE_SET("Zbiór (klasa TreeSet)");
	
	String typeName;
	
	GroupType(String typeName){
		this.typeName = typeName;
	}
	
	@Override
	public String toString() {
		return typeName;
	}
	
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public static GroupType find(String typeName) {
		for(GroupType type : values()) {
			if(type.getTypeName().equals(typeName)) {
				return type;
			}
		}
		return null;
	}
	
	public Collection<Animal> createCollection() throws AnimalException{
		switch(this) {
		case VECTOR: return new Vector<Animal>();
		case ARRAY_LIST: return new ArrayList<Animal>();
		case HASH_SET: return new HashSet<Animal>();
		case LINKED_LIST: return new LinkedList<Animal>();
		case TREE_SET: return new TreeSet<Animal>();
		default: throw new AnimalException("Podany typ nie zosta³ zaimplementowany");
		}
	}
}



public class GroupOfAnimals implements Iterable<Animal>, Serializable{

	private static final long serialVersionUID = 1L;

	private String name;
	private GroupType type;
	private Collection<Animal> collection;
	
	public GroupOfAnimals(GroupType type, String name) throws AnimalException{
		setName(name);
		if(type == null) {
			throw new AnimalException("Nieprawid³owy typ kolekcji");
		}
		this.type = type;
		collection = this.type.createCollection();
	}
	
	public GroupOfAnimals(String typeName, String name) throws AnimalException{
		setName(name);
		GroupType type = GroupType.find(typeName);
		if(type == null) {
			throw new AnimalException("Nieprawid³owy typ kolekcji");
		}
		this.type = type;
		collection = this.type.createCollection();
	}
	
	
	public String getName() {
		return name;
	}



	public void setName(String name) throws AnimalException{
		if(name == null || name.equals("")) {
			throw new AnimalException("Nazwa grupy musi byæ okreœlona");
		}
		this.name = name;
	}



	public GroupType getType() {
		return type;
	}



	public void setType(GroupType type) throws AnimalException{
		if(type == null) throw new AnimalException("Typ kolekcji musi byæ okreœlony");
		if(this.type == type) return;
		
		Collection<Animal> oldCollection = collection;
		collection = type.createCollection();
		this.type = type;
		for(Animal animal: oldCollection)
			collection.add(animal);
	}
	
	public void setType(String typeName) throws AnimalException {
		for(GroupType type : GroupType.values()) {
			if(type.toString().equals(typeName)) {
				setType(type);
				return;
			}
		}
		throw new AnimalException("Nie ma takiego typu kolekcji");
	}
	

	@Override
	public Iterator<Animal> iterator() {
		return collection.iterator();
	}
	
	public int size() {
		return collection.size();
	}
	
	public boolean add(Animal a) {
		return collection.add(a);
	}
	

	public void sortSpecies() throws AnimalException{
		if(type == GroupType.HASH_SET || type == GroupType.TREE_SET) 
			throw new AnimalException("Kolekcje typu SET nie mog¹ byæ sortowane");
		Collections.sort((List<Animal>)collection);
	}
	
	public void sortAge() throws AnimalException{
		if(type == GroupType.HASH_SET || type == GroupType.TREE_SET) 
			throw new AnimalException("Kolekcje typu SET nie mog¹ byæ sortowane");
		Collections.sort((List<Animal>)collection, new Comparator<Animal>(){

			
			//zwraca ujemn¹ - mniejsze jest a1
			//zwraca 0 - równe a1
			//zwraca 1 - wiêksze jest a1
			@Override
			public int compare(Animal a1, Animal a2) {
				return a1.getAge() - a2.getAge();
			}
			
		});
	}
	
	public void sortLegsNumber() throws AnimalException{
		if(type == GroupType.HASH_SET || type == GroupType.TREE_SET) 
			throw new AnimalException("Kolekcje typu SET nie mog¹ byæ sortowane");
		Collections.sort((List<Animal>)collection, new Comparator<Animal>() {

			@Override
			public int compare(Animal o1, Animal o2) {
				return o1.getLegsNumber() - o2.getLegsNumber();
			}
			
		});
	}

	
	public void sortKind() throws AnimalException{
		if(type == GroupType.HASH_SET || type == GroupType.TREE_SET) 
			throw new AnimalException("Kolekcje typu SET nie mog¹ byæ sortowane");
		Collections.sort((List<Animal>) collection, new Comparator<Animal>() {

			@Override
			public int compare(Animal o1, Animal o2) {
				return o1.getKind().toString().compareTo(o2.getKind().toString());
			}
			
		});
	}



	@Override
	public String toString() {
		return "[name=" + name + ", type=" + type + "]";
	}
		

    public static void printToFile(PrintWriter writer, GroupOfAnimals group) {
        writer.println(group.getName());
        writer.println(group.getType());
        for (Animal animal : group.collection)
            Animal.printToFile(writer, animal);
    }


    public static void printToFile(String fileName, GroupOfAnimals group) throws AnimalException {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            printToFile(writer, group);
        } catch (FileNotFoundException e){
            throw new AnimalException("Nie odnaleziono pliku " + fileName);
        }
    }

    public static GroupOfAnimals readFromFile(BufferedReader reader) throws AnimalException{
        try {
            String groupName = reader.readLine();
            String typeName = reader.readLine();
            GroupOfAnimals groupOfAnimals = new GroupOfAnimals(typeName, groupName);

            Animal animal;
            while((animal = Animal.readFromFile(reader)) != null)
                groupOfAnimals.collection.add(animal);
            return groupOfAnimals;
        } catch(IOException e){
            throw new AnimalException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
        }
    }


    public static GroupOfAnimals readFromFile(String fileName) throws AnimalException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            return GroupOfAnimals.readFromFile(reader);
        } catch (FileNotFoundException e){
            throw new AnimalException("Nie odnaleziono pliku " + fileName);
        } catch(IOException e){
            throw new AnimalException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
        }
    }
    
    //Funkcje robi¹ce operacje na zbiorach
    
    public static GroupOfAnimals createGroupUnion(GroupOfAnimals g1,GroupOfAnimals g2) throws AnimalException {
        String name = "(" + g1.name + " OR " + g2.name +")";
        GroupType type;
        if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
            type = g2.type;
        } else {
            type = g1.type;
        }
        GroupOfAnimals group = new GroupOfAnimals(type, name);
        group.collection.addAll(g1.collection);
        group.collection.addAll(g2.collection);
        return group;
    }

    public static GroupOfAnimals createGroupIntersection(GroupOfAnimals g1,GroupOfAnimals g2) throws AnimalException {
        String name = "(" + g1.name + " AND " + g2.name +")";
        GroupType type;
        if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
            type = g2.type;
        } else {
            type = g1.type;
        }
        GroupOfAnimals group = new GroupOfAnimals(type, name);

        for(Animal a: g1.collection) {
        	if(g2.collection.contains(a)) {
        		group.collection.add(a);
        	}
        }
     
        return group;
    }

    public static GroupOfAnimals createGroupDifference(GroupOfAnimals g1,GroupOfAnimals g2) throws AnimalException {
        String name = "(" + g1.name + " SUB " + g2.name +")";
        GroupType type;
        if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
            type = g2.type;
        } else {
            type = g1.type;
        }
        GroupOfAnimals group = new GroupOfAnimals(type, name);

        for(Animal a: g1.collection) {
        	if(g2.collection.contains(a)) {
        		continue;
        	} else {
        		group.collection.add(a);
        	}
        }

        return group;
    }


    public static GroupOfAnimals createGroupSymmetricDiff(GroupOfAnimals g1,GroupOfAnimals g2) throws AnimalException {
        String name = "(" + g1.name + " XOR " + g2.name +")";
        GroupType type;
        if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
            type = g2.type;
        } else {
            type = g1.type;
        }

        GroupOfAnimals group = new GroupOfAnimals(type, name);
        
        for(Animal a: g1.collection) {
        	if(g2.collection.contains(a)) {
        		continue;
        	} else {
        		group.collection.add(a);
        	}
        }

        for(Animal a: g2.collection) {
        	if(g1.collection.contains(a)) {
        		continue;
        	} else {
        		group.collection.add(a);
        	}
        }
        

        return group;
    }
	
}






 