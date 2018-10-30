/*
 *  Program: Operacje na obiektach klasy Animal
 *     Plik: AnimalException.java
 *           definicja wyj¹tku AnimalException
 *
 *    Autor: Tymoteusz Frankiewicz
 *     Data:  pazdziernik 2018 r.
 */

class AnimalException extends Exception {

    private static final long serialVersionUID = 1L;

    public AnimalException(String message) {
        super(message);
    }

}