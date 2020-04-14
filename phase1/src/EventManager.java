//author: Arsham
import java.util.ArrayList;
import java.time.*;

public class EventManager {

    private ArrayList<Event> store;

    public EventManager(ArrayList<Event> store) { this.store = store; }


    //Phase 2 methods
    /**
     * postpone the event by setting it's start and end as null, while maintaining its duration
     * @param id
     */
    public void postponeIndef(int id){
        this.getEvent(id).postpone();
    }

    /**
     * Creates another event with the same name, start and ending as the event of the given id
     * @param id
     * @return the event created
     */
    public Event duplicateEvent(int id){
        Event event = this.getEvent(id);
        return this.createEvent(event.getName(), event.getStartDateTime(), event.getEndDateTime());
    }


    //1)Basics for events
    /**
     * Creates a new event and adds it to the store of events already available
     * @param name
     * @param start
     * @param end
     * @return the new event
     */
    public Event createEvent(String name, LocalDateTime start, LocalDateTime end) {
        //creates an event
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


    //2)Tag methods

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
    } //remove a tag

    /**
     * edits a tag by changing it to another
     * @param event
     * @param oldTag
     * @param newTag
     */
    public void editTag(Event event, String oldTag, String newTag) {
        //edit a tag by removing the old tag and adding a new tag
        event.removeTag(oldTag);
        event.addTag(newTag);
    }

    //3)Alert methods
    public void deleteAlert(Event event, Alert alert) {
        event.removeAlert(alert);
    } //deleting an alert from an event

    public void addAlert(Event event, Alert alert) {
        event.addAlert(alert);
    } //add an alert



    //4)Memo methods
    public void deleteMemo(Event event) {
        event.removeMemo();
    } //delete a memo

    public void addMemo(Event event, Memo memo) {
        event.addMemo(memo);
    } //add/replace a memo

    //5)Getter methods
    public Event getEvent(int id){
        //get an event with id
        for (int i = 0; i < this.store.size(); i++) {
            if (this.store.get(i).getId() == id){
                return this.store.get(i);
            }
        }
        return null;
    }

    public int getEvent(Event event){
        return event.getId();
    } //get the id of an event

    public Memo getMemo(Event event){
        return event.getMemo();
    } //get the memo of an event

    //search methods
    public ArrayList<Event> getEventsByDate(LocalDate date){
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: this.store){
            if(date.equals(timetoDate(event.getStartDateTime()))){
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;
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

}
