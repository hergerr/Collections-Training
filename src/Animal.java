import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
/*
 *  Program: Operacje na obiektach klasy Animal
 *     Plik: Animal.java
 *           definicja typu wyliczeniowego Kind
 *           definicja publicznej klasy Animal
 *           
 *           Dodatkowo zosta³a wyposa¿ona w mo¿liwosc serializacji z zapisem do pliku
 *           binarnego
 *
 *    Autor: Tymoteusz Frankiewicz
 *     Data:  pazdziernik 2018 r.
 */

enum Kind{
	FISH("ryba"),
	MAMMAL("ssak"),
	AMPHIBIAN("p³az"),
	REPTILE("gad"),
	BIRD("ptak"),
	UNKNOWN("nieznane");
	
	private String kindName;
	Kind(String kindName) {
		this.setKindName(kindName);
	}
	
	public String getKindName() {
		return kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	@Override
	public String toString() {
		return getKindName();
	}
}




public class Animal implements Serializable, Comparable<Animal>{
	private static final long serialVersionUID = 3L;
	private int age;
	private int legsNumber;
	private String species;
	private Kind kind;
	
	public Animal(int age, int legsNumber, String species) throws AnimalException{
		setAge(age);
		setLegsNumber(legsNumber);
		setSpecies(species);
		kind = Kind.UNKNOWN;
	}
	
	public Animal(String species) {
		this.species = species;
	}
	
	public int getAge() {
		return this.age;
	}
	
	
	public int getLegsNumber() {
		return this.legsNumber;
	}
	
	public String getSpecies() {
		return this.species;
	}
	
	public Kind getKind() {
		return this.kind;
	}
	
	public void setAge(int age) throws AnimalException {
		if(age >= 0 && age <= 300) {
			this.age = age;
		} else throw new AnimalException("Wiek nie mieœci siê w przedziale [0, 300]");
	}
	
	public void setLegsNumber(int legsNumber) throws AnimalException{
		if(legsNumber > 0 && legsNumber <= 1000) {
			this.legsNumber = legsNumber;
		} else throw new AnimalException("Liczba koñczyn nie mieœci siê w przedziale [0, 1000]");
	}
	
	public void setSpecies(String species) throws AnimalException{
		if(species != null && !species.equals("")) {
			this.species = species;
		} else throw new AnimalException("Zosta³a podana pusta linijka");
	}
	
	public void setKind(Kind kind) {
		this.kind = kind;
	}
	
	public void setKind(String kind) throws AnimalException{
		if(kind != null && !kind.equals("")) {
			for(Kind k : Kind.values()) {
				if(k.getKindName().equals(kind)) {
					this.kind = k;
					return;
				}
			}
		} else {
			this.kind = Kind.UNKNOWN;
			return;
		}	
		
		throw new AnimalException("Nie ma takiego rodzaju");
	}
	
	@Override
	public String toString(){
		return this.kind + " " + this.species;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + legsNumber;
		result = prime * result + ((species == null) ? 0 : species.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Animal other = (Animal) obj;
		if (age != other.age)
			return false;
		if (kind != other.kind)
			return false;
		if (legsNumber != other.legsNumber)
			return false;
		if (species == null) {
			if (other.species != null)
				return false;
		} else if (!species.equals(other.species))
			return false;
		return true;
	}
	
	
	public static void printToFile(PrintWriter writer, Animal animal) {
		writer.println(animal.species + "#" + animal.kind + "#" + animal.legsNumber + "#" + animal.age);
	}
	
	public static void printToFile(String fileName, Animal animal){
        try (PrintWriter writer = new PrintWriter(fileName + ".txt")) { //try-with-resources
            printToFile(writer, animal);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	public static Animal readFromFile(BufferedReader reader) throws AnimalException{
		try {
			String line = reader.readLine();
			String[] txt = line.split("#");
			Animal animal = new Animal(txt[0]);
			animal.setKind(txt[1]);
			animal.setLegsNumber(Integer.parseInt(txt[2]));
			animal.setAge(Integer.parseInt(txt[3]));
			return animal;
		} catch(IOException e) {
			e.printStackTrace();
			throw new AnimalException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}
	}
	
	public static Animal readFromFile(String file) throws AnimalException{
		try(BufferedReader reader = new BufferedReader(new FileReader(new File(file)))){
			return Animal.readFromFile(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new AnimalException("Nie znaleziono pliku.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new AnimalException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		} catch (Exception e) {
        	e.printStackTrace();
            throw new AnimalException("Wyst¹pi³ nieznany b³¹d");
        }
		
	}
	
	//odczyt z zserializowanego pliku .bin
	public static Animal serializeFromBinaryFile(String file) throws AnimalException{
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Animal animal = (Animal) inputStream.readObject();
            return animal;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new AnimalException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new AnimalException("Wyst¹pi³ b³¹d we/wy");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new AnimalException("Nie znaleziono klasy");
        } catch (Exception e) {
        	e.printStackTrace();
            throw new AnimalException("Wyst¹pi³ nieznany b³¹d");
        }
        
	}
	
	//zapis z serializacj¹ do .bin
	public static void serializeToBinaryFile( String file, Animal animal) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file + ".bin"))) {
            outputStream.writeObject(animal);
        } catch (IOException e) {
            e.printStackTrace();
        }

	}


	@Override
	public int compareTo(Animal o) {
		return this.getSpecies().compareTo(o.getSpecies());
	}
	

}