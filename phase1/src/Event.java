import java.time.*;
import java.util.ArrayList;

public class Event {

    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Duration duration;

    private Memo memo = null;

    private int id;
    private static int numEvents = 0;

    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<Alert> alerts = new ArrayList<>();

    public Event(String name, LocalDateTime start, LocalDateTime end) {
        numEvents++;
        id = numEvents;
        this.name = name;
        this.startDateTime = start;
        this.endDateTime = end;
        this.duration = Duration.between(start, end);
    }

     // add tag to this Events ArrayList of tags
    public void addTag(String tag) {
        tags.add(tag);
    }

   // add newMemo to this Event, if this Event is already associated with a Memo, update it
    public void addMemo(Memo newMemo) {
        if (memo == null) {
            memo = newMemo;
            newMemo.addEvent(id);
        } else {
            editMemo(newMemo);
        }
    }

    // currently this supports an event having two alerts at the same time
    // an event can have multiple individual alerts as well as frequent alerts
    public void addAlert(Alert newAlert) {
        if (!newAlert.getLocalDateTime().isAfter(endDateTime)) {
            alerts.add(newAlert);
        }
    }

    public static void bringDownNum() {
        numEvents --;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<Alert> getAlerts() {
        return alerts;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return String.format("E# %d %s @ %s until %s", id, name, startDateTime.toString(), endDateTime.toString());
    }

    public String getName() {
        return name;
    }

    // return the start date and time of this Event
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    // return the end date and time of this Event
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    // return the duration of this Event
    public Duration getDuration() {
        return duration;
    }

    // remove the tag with content
    public void removeTag(String content){
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).equals(content)){
                tags.remove(i);
            }
        }
    }

    /**
     * Author: Arsham Moradi
     * Getter for memo of an event
     * @return the memo for this event
     */
    public Memo getMemo(){
        return this.memo;
    }

    /**
     * Author: Arsham Moradi
     * Editing name of an event
     * @param newName new name of the event
     */
    public void editName(String newName) {
        name = newName;
    }

    /**
     *Author: Arsham Moradi
     * Edits the memo of an event
     * @param memo new memo for the event
     */
    public void editMemo(Memo memo) {
        this.memo = memo;
    }

    /**
     * Author: Arsham Moradi
     * Changes the start of an event
     * @param start new start for the event
     */
    public void editStart(LocalDateTime start) {
        if (start.isBefore(endDateTime)){
            startDateTime = start;
        }
    }

    /**
     * Author: Arsham Moradi
     * Changes the end of an event
     * @param end new end for an event
     */
    public void editEnd(LocalDateTime end){
        if (end.isAfter(this.startDateTime)){
            this.endDateTime = end;
        }
    }

    /**
     * Author: Arsham Moradi
     * Removes the memo of an event by setting it to null
     */
    public void removeMemo(){
        this.memo = null;
    }

    /**
     * Author: Arsham Moradi
     * Removes an alert from an event's list of alerts
     * @param alert alert that is going to be removed
     */
    public void removeAlert(Alert alert){
        this.alerts.remove(alert);
    }

    /**
     * Author: Arsham Moradi
     * Postpones an event by setting its start and end to null, while maintaining the duration
     */
    public void postpone(){
        this.startDateTime = null;
        this.endDateTime = null;
    }

    /**
     * Author: Arsham Moradi
     * Used to determine whether an event is happening in a time-span
     * @param start start of the period in question
     * @param end end of the period in question
     * @return whether or not the event is happening between the start and the finish line
     */

    public boolean withinTime(LocalDateTime start, LocalDateTime end){
        return ( ((this.startDateTime.isBefore(start)) && (this.endDateTime.isAfter(start))) ||
                ((this.startDateTime.isAfter(start)) && (this.startDateTime.isBefore(end))) ||
                (this.startDateTime.isEqual(start)));
    }
}
