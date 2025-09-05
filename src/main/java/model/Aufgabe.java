package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Date;

public class Aufgabe {

    private int id;
    private String taskName;
    private Date startDate;
    private Date targetDate;
//    private BooleanProperty checkbox = new SimpleBooleanProperty(false);
    private final BooleanProperty checkbox = new SimpleBooleanProperty(false);





    public Aufgabe(int id, String taskName, Date startDate, Date targetDate, boolean checked) {
            this.id = id;
            this.taskName = taskName;
            this.startDate = startDate;
            this.targetDate = targetDate;
            this.checkbox.set(checked);
        }

        public Aufgabe(String taskName, Date startDate, Date targetDate) {
            this.taskName = taskName;
            this.startDate = startDate;
            this.targetDate = targetDate;

        }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getTaskName() {return taskName;}

    public void setTaskName(String taskName) {this.taskName = taskName;}

    public Date getStartDate() {return startDate;}

    public void setStartDate(Date startDate) {this.startDate = startDate;}

    public Date getTargetDate() {return targetDate;}

    public void setTargetDate(Date targetDate) {this.targetDate = targetDate;}

    public final boolean isCheckbox() { return checkbox.get(); }
    public final void setCheckbox(boolean value) { checkbox.set(value); }
    public final BooleanProperty checkboxProperty() { return checkbox; }




}
