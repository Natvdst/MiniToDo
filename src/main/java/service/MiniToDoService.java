package service;

import model.Aufgabe;

import java.util.List;

public interface MiniToDoService {

    List<Aufgabe> findAll();
    void save(Aufgabe newAufgabe);
    void delete(Aufgabe aufgabe);


}
