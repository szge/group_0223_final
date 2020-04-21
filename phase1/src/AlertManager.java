import java.time.*;
import java.util.ArrayList;

/**
 * Author: Arsham Moradi
 */
public class AlertManager {

    private ArrayList<Alert> store; //holds a list of the Memos

    public AlertManager(ArrayList<Alert> store) {
        this.store = store;
    }

    /**
     * Creates an alert with a specific name and time
     * @param name
     * @param when
     */
    public void addAlert(String name, LocalDateTime when){
        Alert newAlert = new Alert(name, when);
        this.store.add(newAlert);
    }

    /**
     * Creates a serial alert beginning at start and adding duration until it reaches the end all with name of "name"
     * @param name
     * @param start
     * @param finish
     * @param repetition
     * @return  a list of alerts created
     */
    public ArrayList<Alert> addSerialAlert(String name, LocalDateTime start, LocalDateTime finish, Duration repetition){
        ArrayList<Alert> alerts = new ArrayList<>();
        while(start.isBefore(finish)){
            alerts.add(this.addReturnAlert(name, start));
            start = start.plus(repetition);
        }
        return alerts;
    }

    /**
     * Edits the name of an alert
     * @param alert
     * @param content
     */
    public void editName(Alert alert, String content){
        alert.changeName(content);
    }

    /**
     * Creates an alert and returns it
     * @param name
     * @param when
     * @return
     */
    public Alert addReturnAlert(String name, LocalDateTime when){
        Alert newAlert = new Alert(name, when);
        this.store.add(newAlert);
        return newAlert;
    }

    /**
     * Removes an alert
     * @param alert
     */
    public void removeAlert(Alert alert){
        this.store.remove(alert);
    }

    /**
     * Edits an alert time
     * @param alert
     * @param when
     */
    public void editAlertTime(Alert alert, LocalDateTime when){
        alert.changeTime(when);
    }

    /**
     * Returns all the remaining alerts
     * @param alert
     * @return
     */
    public ArrayList<Alert> remainingAlert(Alert alert) {
        ArrayList<Alert> alerts = new ArrayList<>();
        for (Alert value : this.store) {
            if (value.getLocalDateTime().isAfter(alert.getLocalDateTime())) {
                alerts.add(value);
            }
        }
        return alerts;
    }

}



