package service;

import model.Aufgabe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MiniToDoCSVService implements MiniToDoService {
    @Override
    public List<Aufgabe> findAll() {
        List<Aufgabe> contactList = new ArrayList<>();
        File file = new File("miniToDo.csv");
        if (!file.exists()) {
            // Erste Ausführung: Datei noch nicht vorhanden -> leere Liste zurückgeben
            return contactList;
        }
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line == null) {
                    continue;
                }
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    // Leere Zeilen oder Kommentar-/Headerzeilen überspringen
                    continue;
                }

                String[] arr = line.split(",", -1); // -1: leere Felder beibehalten
                if (arr.length < 5) {
                    System.err.println("Überspringe ungültige Zeile (zu wenige Spalten): " + line);
                    continue;
                }

                String idStr = arr[0].trim();
                if (idStr.isEmpty()) {
                    System.err.println("Überspringe Zeile mit leerer ID: " + line);
                    continue;
                }

                int id;
                try {
                    id = Integer.parseInt(idStr);
                } catch (NumberFormatException nfe) {
                    System.err.println("Überspringe Zeile mit ungültiger ID \"" + idStr + "\": " + line);
                    continue;
                }

                String aufgabe = arr[1].trim();
                Date startDate = parseDate(arr[2].trim());
                Date targetDate = parseDate(arr[3].trim());
                boolean checkbox = Boolean.parseBoolean(arr[4].trim());

                System.out.printf("%s - %s - %s - %s - %s%n", id, aufgabe, startDate, targetDate, checkbox);

                Aufgabe a = new Aufgabe(id, aufgabe, startDate, targetDate, checkbox);
                contactList.add(a);
            }
        } catch (FileNotFoundException e) {
            // Sollte durch file.exists() bereits abgefangen sein, aber zur Sicherheit:
            return contactList;
        }
        return contactList;
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    static {
        DATE_FORMAT.setLenient(false);
    }

    private Date parseDate(String dateStr) {
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Ungültiges Datum: " + dateStr, e);
        }
    }

    @Override
    public void save(Aufgabe newAufgabe) {
        System.out.println("Aufgabe " + newAufgabe.getId());

        // TODO: Aufgabe in CSV schreiben
        // FileHandler.saveCSV(newContact) -> Vorschlag zur Verbesserung
//        File file = new File("data/miniToDo.csv");
//        file.getParentFile().mkdirs(); // erstellt Ordner, falls nötig
        int newId = 1;
        List<Aufgabe> existing = findAll();
        if (!existing.isEmpty()) {
            newId = existing.get(existing.size() - 1).getId() + 1;
        }
        newAufgabe.setId(newId);

        // Datum formatieren
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String start = dateFormat.format(newAufgabe.getStartDate());
        String target = dateFormat.format(newAufgabe.getTargetDate());

        // Aufgabe an CSV anhängen
        try (FileWriter fw = new FileWriter("miniToDo.csv", true)) {
            String line = String.format("%d,%s,%s,%s,%s%n",
                    newAufgabe.getId(),
                    newAufgabe.getTaskName(),
                    start,
                    target,
                    newAufgabe.isCheckbox()
            );
            fw.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Arbeitsverzeichnis: " + System.getProperty("user.dir"));
    }


    @Override
    public void delete(Aufgabe aufgabe) {
        if (aufgabe == null) {
            return;
        }
        File file = new File("miniToDo.csv");
        if (!file.exists()) {
            // Nichts zu löschen
            return;
        }

        List<Aufgabe> all = findAll();
        boolean removed = all.removeIf(a -> a.getId() == aufgabe.getId());
        if (!removed) {
            // Keine Übereinstimmung gefunden -> keine Änderung
            return;
        }

    }
}



