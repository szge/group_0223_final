//Author:Arsham
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.*;

public class OverallManager{

    private MemoManager memoManager;
    private AlertManager alertManager;
    private SeriesManager seriesManager;
    private EventManager eventManager;

    public OverallManager(ArrayList<ArrayList> data) {
        this.eventManager = new EventManager(data.get(1));
        this.seriesManager = new SeriesManager(this.eventManager, data.get(4));
        this.memoManager = new MemoManager(data.get(2));
        this.alertManager = new AlertManager(data.get(5), data.get(3));
    }

    //Event methods
    public void postponeIndef(int id){
        this.eventManager.postponeIndef(id);
    }

    public Event duplicateEvent(int id){
        return this.eventManager.duplicateEvent(id);
    }

    public Event getEvent(int id){
        //returns the event by its id
        return this.eventManager.getEvent(id);
    }

    public void createEvent(String name, LocalDateTime start, LocalDateTime end) {
        //creates an event
        this.eventManager.createEvent(name, start, end);
    }

    public void createEvent(String name, LocalDateTime start, LocalDateTime end, String content) {
        //creates an event that is entered with a memo
        Event event = this.eventManager.createEvent(name, start, end);
        Memo memo = this.memoManager.addMemo(event.getId(), content);
        this.eventManager.addMemo(event, memo);
    }

    public void deleteEvent(Event event) {
        //deletes an event
        for (int i = 0; i < event.getAlerts().size(); i++) {
            this.alertManager.removeAlert(event.getAlerts().get(i));
        }
        this.memoManager.removeEventMemo(event.getMemo(), event.getId());
    }

    public void editEventName(Event event, String content){
        //edit the name of an event
        this.eventManager.editName(event, content);
    }

    public void EditEventStart(Event event, LocalDateTime newStart){
        //edit the start time of an event
        this.eventManager.editEventStart(event, newStart);
    }

    public void EditEventEnd(Event event, LocalDateTime newEnd){
        //edit the end time of an event
        this.eventManager.editEventEnd(event, newEnd);
    }

    public void addTag(Event event, String content){
        //add a tag to an event
        this.eventManager.addTag(event, content);
    }

    public void removeTag(Event event, String content) {
        //remove tag of an event with the tag of content
        this.eventManager.removeTag(event, content);
    }

    public void editTag(Event event, String oldTag, String newTag) {
        //change a certain tag for an event
        this.eventManager.removeTag(event, oldTag);
        this.eventManager.addTag(event, newTag);
    }

    //Alerts
    public void addAlert(Event event, String name, LocalDateTime when){
        //adds an alert to an event
        this.eventManager.addAlert(event, this.alertManager.addAlert(name, when));
    }

    public void removeAlert(Event event, Alert alert) {
        //removes an alert from an event
        this.eventManager.deleteAlert(event, alert);
        this.alertManager.removeAlert(alert);
    }

    public void editAlertName(Alert alert, String name){
        //edits the name of an alert
        this.alertManager.editName(alert, name);
    }

    public void editAlertTime( Alert alert, LocalDateTime when){
        //edits the alert time for an event
        this.alertManager.editAlertTime(alert, when);
    }

    public ArrayList<Alert> getRemainingAlerts(int id){
        //returns a list of alerts that haven't happened yet
        return this.alertManager.remainingAlert(id);
    }

    //Memo
    public void addMemo(Event event, String content){
        //adds creates a memo with message "content" to event
        this.eventManager.addMemo(event, this.memoManager.addMemo(this.eventManager.getEvent(event), content));
    }

    public void removeEventMemo(Event event){
        //removes a memo from an event
        this.memoManager.removeEventMemo(this.eventManager.getMemo(event), this.eventManager.getEvent(event));
    }

    public void editEventMemo(Event event, Memo memo, String content){
        //edits the memo for an event
        this.eventManager.addMemo(event, this.memoManager.addMemo(this.eventManager.getEvent(event), content));
    }

    public void deleteMemo(Memo memo){
        //deletes a memo completely
        for (int i = 0; i < this.memoManager.memoEvent(memo).size(); i++) {
            this.eventManager.deleteMemo(this.eventManager.getEvent(this.memoManager.memoEvent(memo).get(i)));
        }
        this.memoManager.deleteMemo(memo);
    }

    public void editMemo(Memo memo, String content){
        //edits a memo completely affecting all the events that have it
        this.memoManager.editName(memo, content);
    }

    //Serial Events
    public void addSerialEvent(LocalDateTime startStart, LocalDateTime startEnd,
                               Duration repetition, LocalDateTime absoluteEnd, String name){
        //creates a series of events
        this.seriesManager.createSerialEvent(startStart, startEnd, repetition, absoluteEnd, name);
    }

    public void addSerialEvent(LocalDateTime startStart, LocalDateTime startEnd,
                               Duration repetition, LocalDateTime absoluteEnd, String name, String content){
        //creates a series of events with a certain memo
        Series series = this.seriesManager.createSerialEvent(startStart, startEnd, repetition, absoluteEnd, name);
        for (int i = 0; i < series.getEvents().size(); i++) {
            this.addMemo(this.eventManager.getEvent(series.getEvents().get(i)), content);
        }
    }

    public void deleteSerialEvent(Event event){
        //deletes a series of event that includes event
        this.seriesManager.deleteSerialEvent(this.seriesManager.seriesGetter(event));
    }

    public void editNameSerialEvent(Event event, String name){
        //changes the name of a series of events
        this.seriesManager.editName(this.seriesManager.seriesGetter(event), name);
    }

    //Serial Alerts
    public void addSerialAlerts(Event event, String name, LocalDateTime start,
                                LocalDateTime finish, Duration repetition){
        //creates serial alerts for an event
        ArrayList<Alert> alerts = this.alertManager.addSerialAlert(name, start, finish, repetition);
        for (int i = 0; i < alerts.size(); i++) {
            this.eventManager.addAlert(event, alerts.get(i));
        }
    }

    public void deleteSerialAlerts(Event event, Alert alert){
        //deletes serial events containing alert for event
        ArrayList<Alert> alerts = this.alertManager.removeSerialAlert(alert);
        for (int i = 0; i < alerts.size(); i++) {
            this.eventManager.deleteAlert(event, alerts.get(i));
        }
    }

}
