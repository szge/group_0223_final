import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EventManager {

    private ArrayList<Event> store; //Stores all the events

    public EventManager(ArrayList<Event> store) {
        this.store = store;
    }


    /**
     * postpone the event by setting it's start and end as null, while maintaining its duration
     * @param event
     */
    public void postpone(Event event){
        event.postpone();
    }

    /**
     * Gets all the postponed events
     * @return
     */
    public ArrayList<Event> getPostponed(){
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : this.store) {
            if (event.getStartDateTime() == null) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Get the id for an event
     * @param event
     * @return
     */
    public int getId(Event event){
        return event.getId();
    }

    /**
     * Gets the event that has a certain id
     * @param id
     * @return
     */
    public Event getEvent(int id){
        for (Event event : this.store) {
            if (event.getId() == id) {
                return event;
            }
        }
        return null;
    }

    /**
     * Creates a new event and adds it to the store of events already available
     * @param name
     * @param start
     * @param end
     * @return the new event
     */
    public Event createEvent(String name, LocalDateTime start, LocalDateTime end) {
        Event event = new Event(name, start, end);
        this.store.add(event);
        return event;
    }

    /**
     * removes an event from the list of events
     * @param event
     */
    public void deleteEvent(Event event) {
        this.store.remove(event);
    }

    /**
     * changes the start time of an event
     * @param event
     * @param newStart
     */
    public void editEventStart(Event event, LocalDateTime newStart) {
        event.editStart(newStart);
    }

    /**
     * changes the ending of an event
     * @param event
     * @param newEnd
     */
    public void editEventEnd(Event event, LocalDateTime newEnd) {
        event.editEnd(newEnd);
    }

    /**
     * changes the name of an event
     * @param event
     * @param newName
     */
    public void editName(Event event, String newName) {
        event.editName(newName);
    }

    /**
     * Adds a tag to an event
     * @param event
     * @param content
     */
    public void addTag(Event event, String content) {
        event.addTag(content);
    }

    /**
     * Removes a tag from an event
     * @param event
     * @param content
     */
    public void removeTag(Event event, String content) {
        event.removeTag(content);
    }

    /**
     * Returns the tags for an event
     * @param event
     * @return
     */
    public ArrayList<String> getTag(Event event){
        return event.getTags();
    }

    /**
     * Deletes an alert from an event's list of alerts
     * @param event
     * @param alert
     */
    public void deleteAlert(Event event, Alert alert) {
        event.removeAlert(alert);
    }

    /**
     * Adds an alert to an event's list of alerts
     * @param event
     * @param alert
     */
    public void addAlert(Event event, Alert alert) {
        event.addAlert(alert);
    }

    /**
     * Removes an event's memo
     * @param event
     */
    public void deleteMemo(Event event) {
        event.removeMemo();
    }

    /**
     * Adds a memo to an event's list of memos
     * @param event
     * @param memo
     */
    public void addMemo(Event event, Memo memo) {
        event.addMemo(memo);
    }

    /**
     * Get the memo for an event
     * @param event
     * @return
     */
    public Memo getMemo(Event event){
        return event.getMemo();
    }

    /**
     * @author Danial
     *
     * Iterates through events and returns the ones
     * whose end date is before today.
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getPastEvents(){
        LocalDate now = LocalDate.now();
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: this.store){
            if(now.isAfter(timetoDate(event.getEndDateTime()))){
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;

    }
    /**
     * @author Danial
     *
     * Iterates through events and returns the ones
     * whose start date is after today.
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getFutureEvents(){
        LocalDate now = LocalDate.now();
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: this.store){
            if(now.isBefore(timetoDate(event.getStartDateTime()))){
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;

    }
    /**
     * @author Danial
     *
     * Iterates through events and returns the ones
     * whose start date is today or before today and their
     * end date is today or after today. In other words
     * events that are currently in process
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getCurrentEvents(){
        LocalDate now = LocalDate.now();
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: this.store){
            if(!(now.isBefore(timetoDate(event.getStartDateTime()))) &&
                    !(now.isAfter(timetoDate(event.getEndDateTime())))){
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;

    }
    /**
     * @author Danial
     *
     * Iterates through events and returns the ones
     * associated with the memo provided
     * @param memo The memo whose events are to be retrieved
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getEventsByMemo(Memo memo){
        ArrayList<Integer> eventids = memo.getEvents();
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: this.store){
            if(eventids.contains((Integer) event.getId())) {
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;
    }
    /**
     * @author Danial
     *
     * Iterates through events and returns the ones
     * associated with the tag provided
     * @param tag The tag whose events are to be retrieved
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getEventsByTag(String tag){
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: this.store){
            if(event.getTags().contains(tag)) {
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;
    }

    /**
     * @author Danial
     *
     * Converts a LocalDateTime object to its corresponding
     * LocalDate object
     * @param time LocalDateTime object to be converted
     * @return
     * LocalDate
     */
    private  LocalDate timetoDate(LocalDateTime time){
        int year = time.getYear();
        int month = time.getMonthValue();
        int day = time.getDayOfMonth();
        return LocalDate.of(year, month, day);
    }

    /**
     * Get all the events happening within a time span
     * @param start
     * @param end
     * @return
     */
    public ArrayList<Event> eventsByTime(LocalDateTime start, LocalDateTime end){
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : this.store) {
            if (event.withinTime(start, end)) {
                events.add(event);
            }
        }
        return events;
    }


}
