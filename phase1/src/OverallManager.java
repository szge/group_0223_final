import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.*;

public class OverallManager{

    /**
     * Author: Arsham Moradi
     */
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

    /**
     * Postpones an event
     * @param event
     */
    public void postpone(Event event){
        this.eventManager.postpone(event);
    }

    /**
     * Gets a list of all postponed events
     * @return a list of postponed events
     */
    public ArrayList<Event> getPostponed(){
        return this.eventManager.getPostponed();
    }

    /**
     * Creates an event
     * @param name
     * @param start
     * @param end
     */
    public void createEvent(String name, LocalDateTime start, LocalDateTime end) {
        this.eventManager.createEvent(name, start, end);
    }

    /**
     * Deletes an event
     * @param event
     */
    public void deleteEvent(Event event) {
        for (int i = 0; i < event.getAlerts().size(); i++) {
            this.alertManager.removeAlert(event.getAlerts().get(i));
        }
        this.seriesManager.deleteEvent(event);
        this.memoManager.removeEventMemo(event.getMemo(), event.getId());
        this.eventManager.deleteEvent(event);
    }

    /**
     * Edits an event's name
     * @param event
     * @param content
     */
    public void editEventName(Event event, String content){
        this.eventManager.editName(event, content);
    }

    /**
     * Edits an event's starting time
     * @param event
     * @param newStart
     */
    public void EditEventStart(Event event, LocalDateTime newStart){
        this.eventManager.editEventStart(event, newStart);
    }

    /**
     * Edits the end of an event
     * @param event
     * @param newEnd
     */
    public void EditEventEnd(Event event, LocalDateTime newEnd){
        this.eventManager.editEventEnd(event, newEnd);
    }

    /**
     * Adds a tag to an event
     * @param event
     * @param content
     */
    public void addTag(Event event, String content){
        this.eventManager.addTag(event, content);
    }

    /**
     * Removes a tag from an event
     * @param event
     * @param content
     */
    public void removeTag(Event event, String content) {
        this.eventManager.removeTag(event, content);
    }

    /**
     * Edits a tag for an event
     * @param event
     * @param oldTag
     * @param newTag
     */
    public void editTag(Event event, String oldTag, String newTag) {
        this.eventManager.removeTag(event, oldTag);
        this.eventManager.addTag(event, newTag);
    }

    /**
     * Gets the tags for an event
     * @param event
     * @return
     */
    public ArrayList<String> getTags(Event event){
        return this.eventManager.getTag(event);
    }

    /**
     * Adds an alert to an event
     * @param event
     * @param name
     * @param when
     */
    public void addAlert(Event event, String name, LocalDateTime when){
        this.eventManager.addAlert(event, this.alertManager.addReturnAlert(name, when));
    }

    /**
     * Removes an alert from the associated event and in general
     * @param event
     * @param alert
     */
    public void removeAlert(Event event, Alert alert) {
        this.eventManager.deleteAlert(event, alert);
        this.alertManager.removeAlert(alert);
    }

    /**
     * Edits the name of an alert
     * @param alert
     * @param name
     */
    public void editAlertName(Alert alert, String name){
        this.alertManager.editName(alert, name);
    }

    /**
     * Edits an alert's time
     * @param alert
     * @param when
     */
    public void editAlertTime( Alert alert, LocalDateTime when){
        this.alertManager.editAlertTime(alert, when);
    }

    /**
     * Gets a list of remaining alerts
     * @param alert
     * @return
     */
    public ArrayList<Alert> getRemainingAlerts(Alert alert){
        return this.alertManager.remainingAlert(alert);
    }

    /**
     * Adds a memo to an alert after creating it
     * @param event
     * @param content
     */
    public void addMemo(Event event, String content){
        this.eventManager.addMemo(event, this.memoManager.addMemo(this.eventManager.getId(event), content));
    }

    /**
     * Removes an event from a memo's list of events
     * @param event
     */
    public void removeEventMemo(Event event){
        this.memoManager.removeEventMemo(this.eventManager.getMemo(event), this.eventManager.getId(event));
    }

    /**
     * Edits the memo for an event.
     * @param event
     * @param content
     */
    public void editEventMemo(Event event, String content){
        this.eventManager.addMemo(event, this.memoManager.addMemo(this.eventManager.getId(event), content));
    }

    /**
     * Deletes a memo and removes it from all events that have it as their memo
     * @param memo
     */
    public void deleteMemo(Memo memo){
        for (int i = 0; i < this.memoManager.memoEvent(memo).size(); i++) {
            this.eventManager.deleteMemo(this.eventManager.getEvent(this.memoManager.memoEvent(memo).get(i)));
        }
        this.memoManager.deleteMemo(memo);
    }

    /**
     * Edits a memo
     * @param memo
     * @param content
     */
    public void editMemo(Memo memo, String content){
        this.memoManager.editName(memo, content);
    }

    /**
     * Creates a series of events
     * @param name
     */
    public void createSerialEvent(String name){
        this.seriesManager.addSerialEvent(name);
    }

    /**
     * Creates a serial event as described in phase 2
     * @param startStart
     * @param startEnd
     * @param absoluteEnd
     * @param repetition
     * @param name
     */
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

    /**
     * Adds an event to a series
     * @param series
     * @param event
     */
    public void addSerialEvent(Series series, Event event){
        series.addEvent(event);
    }

    /**
     * Deletes a series
     * @param series
     */
    public void deleteSeries(Series series){
        this.seriesManager.deleteSerialEvent(series);
    }

    /**
     * Deletes an event from a series
     * @param series
     * @param event
     */
    public void deleteSerialEvent(Series series, Event event){
        this.seriesManager.deleteEvent(series, event);
    }

    /**
     * Creates frequent alerts
     * @param event
     * @param name
     * @param start
     * @param finish
     * @param repetition
     */
    public void addSerialAlerts(Event event, String name, LocalDateTime start,
                                LocalDateTime finish, Duration repetition){
        ArrayList<Alert> alerts = this.alertManager.addSerialAlert(name, start, finish, repetition);
        for (Alert alert : alerts) {
            this.eventManager.addAlert(event, alert);
        }
    }

    /**
     * Performs similarly to the method in event manager
     * @param tag
     * @return
     */
    public ArrayList<Event> getEventsByTag(String tag){
         return this.eventManager.getEventsByTag(tag);
    }

    /**
     * Performs similarly to the method in event manager
     * @param id
     * @return
     */
    public Event getEvent(int id){return this.eventManager.getEvent(id);}

    /**
     * Performs similarly to the method in event manager
     * @param memo
     * @return
     */
    public ArrayList<Event> getEventsByMemo(Memo memo){
        return this.eventManager.getEventsByMemo(memo);
    }

    /**
     * Performs similarly to the method in event manager
     * @return
     */
    public ArrayList<Event> getCurrentEvents(){
        return this.eventManager.getCurrentEvents();
    }

    /**
     * Performs similarly to the method in event manager
     * @return
     */
    public ArrayList<Event> getFutureEvents(){
        return this.eventManager.getFutureEvents();
    }

    /**
     * Performs similarly to the method in event manager
     * @return
     */
    public ArrayList<Event> getPastEvents(){
        return this.eventManager.getPastEvents();
    }

    /**
     * Gets all the events within an hour's period
     * @param start
     * @return
     */
    public ArrayList<Event> getEventsbyHour(LocalDateTime start) {
        LocalDateTime end = start.plusHours(1);
        return this.eventManager.eventsByTime(start, end);
    }

    /**
     * Gets all the events within a day's period
     * @param start
     * @return
     */
    public ArrayList<Event> getEventsbyDay(LocalDateTime start) {
        LocalDateTime end = start.plusDays(1);
        return this.eventManager.eventsByTime(start, end);
    }

    /**
     * Gets all the events within a week's period
     * @param start
     * @return
     */
    public ArrayList<Event> getEventsbyWeek(LocalDateTime start) {
        LocalDateTime end = start.plusWeeks(1);
        return this.eventManager.eventsByTime(start, end);
    }

    /**
     * Gets all the events within a month' period
     * @param start
     * @return
     */
    public ArrayList<Event> getEventsbyMonth(LocalDateTime start) {
        LocalDateTime end = start.plusMonths(1);
        return this.eventManager.eventsByTime(start, end);
    }

    /**
     * Feature method to create methods such as a certain day of a month
     * @param month
     * @param day
     * @param years
     * @param name
     */
    public void dayMonthHoliday(int month, int day, int years, String name){
        LocalDateTime start = LocalDateTime.now();
        for (int i = 0; i < years; i++) {
            start = start.plusYears(1);
            start = start.withMonth(month);
            start = start.withDayOfMonth(day);
            this.createEvent(name, start, start.plusDays(1));
        }
    }

    /**
     * Feature method to create events such as third friday of a given month for a certain # of years
     * @param month
     * @param week
     * @param day
     * @param year
     * @param name
     */
    public void dayWeekMothHoliday(int month, int week, DayOfWeek day, int year, String name){
        LocalDateTime start = LocalDateTime.now().withMonth(month).withDayOfMonth(1);
        for (int i = 0; i < year; i++) {
            for (int j = 0; j < week - 1; j++) {
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
