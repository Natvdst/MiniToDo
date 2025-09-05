package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Aufgabe;
import service.MiniToDoCSVService;
import service.MiniToDoService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class MiniToDoController {

    private MiniToDoService service = new MiniToDoCSVService() ;
//        @Override
//        public List<Aufgabe> findAll() {
//            return tableView.getItems(); // aktuell nur mit Tabelle arbeiten
//        }
//
//        @Override
//        public void save(Aufgabe aufgabe) {
//            tableView.getItems().add(aufgabe);
//        }
//
//        @Override
//        public void delete(Aufgabe id) {
//            tableView.getItems().remove(id);
//        }


        @FXML
        private TextField newTaskField;

        @FXML
        private DatePicker newTargetDate;

        @FXML
        private Button addTaskButton;

        @FXML
        private TableView<Aufgabe> tableView;

        @FXML
        private TableColumn<Aufgabe, Date> startColumn;

        @FXML
        private TableColumn<Aufgabe, Date> targetColumn;

        @FXML
        private TableColumn<Aufgabe, String> taskColumn;

        @FXML
        private TableColumn<Aufgabe, Boolean> checkBoxColumn;

        @FXML
        void initialize() {
            System.out.println("init Controller");
            setupTable();
        }

        private void setupTable() {

            taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
            startColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            targetColumn.setCellValueFactory(new PropertyValueFactory<>("targetDate"));

            // Checkbox klickbar machen
            checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().checkboxProperty());
            checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));

            // WICHTIG: Editierbarkeit aktivieren, damit CheckBox klickbar ist
            tableView.setEditable(true);
            checkBoxColumn.setEditable(true);

            // Datum hübsch formatiert anzeigen
            startColumn.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : new java.text.SimpleDateFormat("yyyy-MM-dd").format(item));
                }
            });
            targetColumn.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : new java.text.SimpleDateFormat("yyyy-MM-dd").format(item));
                }
            });

            // Placeholder, falls keine Daten vorhanden sind
            tableView.setPlaceholder(new Label("Keine Aufgaben vorhanden"));

            // Daten laden (robust mit Fehlerbehandlung)
            try {
                tableView.getItems().setAll(service.findAll());
            } catch (RuntimeException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Laden der Aufgaben:\n" + ex.getMessage()).showAndWait();
                tableView.getItems().clear();
            }

//            taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
//            startColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
//            targetColumn.setCellValueFactory(new PropertyValueFactory<>("targetDate"));
//            checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
//            checkBoxColumn.setCellFactory(tc -> new CheckBoxTableCell<>());
//            checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().checkboxProperty());
//            checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));
//
//            tableView.getItems().setAll(service.findAll());

        }

        @FXML
        void addTask(ActionEvent event) {
            String taskName = newTaskField.getText();
            LocalDate targetLocalDate = newTargetDate.getValue();

            if (taskName == null || taskName.isEmpty() || targetLocalDate == null) {
                System.out.println("Bitte Task und Datum eingeben!");
                new Alert(Alert.AlertType.WARNING, "Bitte Task und Datum eingeben!").showAndWait();
                return;
            }

            // Umwandlung LocalDate -> java.util.Date
            Date startDate = new Date(); // jetzt
            Date targetDate = Date.from(targetLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Aufgabe aufgabe = new Aufgabe(taskName, startDate, targetDate);
            service.save(aufgabe);

            // Tabelle aktualisieren
            try {
                tableView.getItems().setAll(service.findAll());
            } catch (RuntimeException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Aktualisieren der Aufgaben:\n" + ex.getMessage()).showAndWait();
            }

            // Eingabefelder zurücksetzen
            newTaskField.clear();
            newTargetDate.setValue(null);
        }


    }
