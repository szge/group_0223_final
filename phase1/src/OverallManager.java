//Author:Arsham
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.*;

public class OverallManager{

    private MemoManager memoManager;
    private AlertManager alertManager;
    private EventManager eventManager;
    private SeriesManager seriesManager;

    public OverallManager(ArrayList<ArrayList> data) {
        this.eventManager = new EventManager(data.get(0));
        this.seriesManager = new SeriesManager(data.get(3));
        this.memoManager = new MemoManager(data.get(1));
        this.alertManager = new AlertManager(data.get(2));
    }

    //Event methods
    public void postpone(Event event){
        this.eventManager.postpone(event);
    }

    public ArrayList<Event> getPostponed(){
        return this.eventManager.getPostponed();
    }

    public void createEvent(String name, LocalDateTime start, LocalDateTime end) {
        //creates an event
        this.eventManager.createEvent(name, start, end);
    }

    public void deleteEvent(Event event) {
        //deletes an event
        for (int i = 0; i < event.getAlerts().size(); i++) {
            this.alertManager.removeAlert(event.getAlerts().get(i));
        }
        this.seriesManager.deleteEvent(event);
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
        this.eventManager.addAlert(event, this.alertManager.addReturnAlert(name, when));
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

    public ArrayList<Alert> getRemainingAlerts(Alert alert){
        //returns a list of alerts that haven't happened yet
        return this.alertManager.remainingAlert(alert);
    }

    //Memo
    public void addMemo(Event event, String content){
        //adds creates a memo with message "content" to event
        this.eventManager.addMemo(event, this.memoManager.addMemo(this.eventManager.getId(event), content));
    }

    public void removeEventMemo(Event event){
        //removes a memo from an event
        this.memoManager.removeEventMemo(this.eventManager.getMemo(event), this.eventManager.getId(event));
    }

    public void editEventMemo(Event event, Memo memo, String content){
        //edits the memo for an event
        this.eventManager.addMemo(event, this.memoManager.addMemo(this.eventManager.getId(event), content));
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

    public void createSerialEvent(String name){
        this.seriesManager.addSerialEvent(name);
    }

    public void createSerialEvent(LocalDateTime startStart, LocalDateTime startEnd, LocalDateTime absoluteEnd,
                                  Duration repetition, String name){
        this.createSerialEvent(name);
        while (startEnd.isBefore(absoluteEnd)) {
            this.seriesManager.addEvent(this.seriesManager.seriesGetter(name)
                    ,this.eventManager.createEvent(name, startStart, startEnd));
            startStart = startStart.plus(repetition);
            startEnd = startEnd.plus(repetition);
        }
    }


    public void addSerialEvent(Series series, Event event){
        //creates a series of events with a certain memo
        series.addEvent(event);
    }

    public void deleteSeries(Series series){
        this.seriesManager.deleteSerialEvent(series);
    }

    public void deleteSerialEvent(Series series, Event event){
        this.seriesManager.deleteEvent(series, event);
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

    //search methods
    public ArrayList<Event> getEventsByTag(String tag){
         return this.eventManager.getEventsByTag(tag);
    }
    public Event getEvent(int id){return this.eventManager.getEvent(id);}
    public ArrayList<Event> getEventsByMemo(Memo memo){
        return this.eventManager.getEventsByMemo(memo);
    }
    public ArrayList<Event> getCurrentEvents(){
        return this.eventManager.getCurrentEvents();
    }
    public ArrayList<Event> getFutureEvents(){
        return this.eventManager.getFutureEvents();
    }
    public ArrayList<Event> getPastEvents(){
        return this.eventManager.getPastEvents();
    }
    public ArrayList<Event> getEventsByDate(LocalDate date){
        return this.eventManager.getEventsByDate(date);
    }

    // feature for time
    public ArrayList<Event> getEventsbyHour(LocalDateTime start) {
        LocalDateTime end = start.plusHours(1);
        return this.eventManager.eventsByTime(start, end);
    }

    public ArrayList<Event> getEventsbyDay(LocalDateTime start) {
        LocalDateTime end = start.plusDays(1);
        return this.eventManager.eventsByTime(start, end);
    }

    public ArrayList<Event> getEventsbyWeek(LocalDateTime start) {
        LocalDateTime end = start.plusWeeks(1);
        return this.eventManager.eventsByTime(start, end);
    }

    public ArrayList<Event> getEventsbyMonth(LocalDateTime start) {
        LocalDateTime end = start.plusMonths(1);
        return this.eventManager.eventsByTime(start, end);
    }

    public void dayMonthHoliday(int month, int day, int years, String name){
        LocalDateTime start = LocalDateTime.now();
        for (int i = 0; i < years; i++) {
            start = start.plusYears(1);
            start = start.withMonth(month);
            start = start.withDayOfMonth(day);
            this.createEvent(name, start, start.plusDays(1));
        }
    }

    public void dayWeekMothHoliday(int month, int week, DayOfWeek day, int year, String name){
        LocalDateTime start = LocalDateTime.now().withMonth(month).withDayOfMonth(1);
        for (int i = 0; i < year; i++) {
            for (int i = 0; i < week - 1; i++) {
                start = start.plusWeeks(1);
            }
            while (!start.getDayOfWeek().equals(DayOfWeek.MONDAY)){
                start = start.minusDays(1);
            }
            while (!start.getDayOfWeek().equals(day)){
                start = start.plusDays(1);
            }
            this.createEvent(name, start, start.plusDays(1));
            start = start.plusYears(1);
        }

    }


}
