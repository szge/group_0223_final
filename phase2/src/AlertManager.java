//author: Arsham
import java.time.*;
import java.util.ArrayList;

public class AlertManager {

    private ArrayList<Alert> store; //holds a list of the Memos

    public AlertManager(ArrayList<Alert> store) {
        this.store = store;
    }


    public void addAlert(String name, LocalDateTime when){
        //create and alert
        Alert newAlert = new Alert(name, when);
        this.store.add(newAlert);
    }

    public ArrayList<Alert> addSerialAlert(String name, LocalDateTime start, LocalDateTime finish, Duration repetition){
        ArrayList<Alert> alerts = new ArrayList<>();
        while(start.isBefore(finish)){
            alerts.add(this.addReturnAlert(name, start));
            start = start.plus(repetition);
        }
        return alerts;
    }

    public void editName(Alert alert, String content){
        //edits the name of an alert
        alert.changeName(content);
    }

    public Alert addReturnAlert(String name, LocalDateTime when){
        //create and alert
        Alert newAlert = new Alert(name, when);
        this.store.add(newAlert);
        return newAlert;
    }

    public void removeAlert(Alert alert){
        this.store.remove(alert);
    } //remove an alert

    public void editAlertTime(Alert alert, LocalDateTime when){
        alert.changeTime(when);
    }// change alert time

    public ArrayList<Alert> remainingAlert(Alert alert) {
        ArrayList<Alert> alerts = new ArrayList<>();
        for (int i = 0; i < this.store.size(); i++) {
            if (this.store.get(i).getLocalDateTime().isAfter(alert.getLocalDateTime())){
                alerts.add(this.store.get(i));
            }
        }
        return alerts;
    }

    }



