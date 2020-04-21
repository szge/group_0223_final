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
        return String.format("Name: %s\nStart time: %s\nEnd time: %s\nID: %d\nMemo: %s", name, startDateTime.toString(), endDateTime.toString(), id, String.valueOf(memo));
    }

    public String getName() {
        return name;
    }

    // return the start date and time of this Event
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime){
        this.startDateTime = startDateTime;
    }

    // return the end date and time of this Event
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
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
     *
     * @return
     */
    public Memo getMemo(){
        return this.memo;
    }

    /**
     *
     * @param newName
     */
    public void editName(String newName) {
        name = newName;
    }

    /**
     *
     * @param memo
     */
    public void editMemo(Memo memo) {
        this.memo = memo;
    }

    /**
     *
     * @param start
     */
    public void editStart(LocalDateTime start) {
        if (start.isBefore(endDateTime)){
            startDateTime = start;
        }
    }

    /**
     *
     * @param end
     */
    public void editEnd(LocalDateTime end){
        if (end.isAfter(this.startDateTime)){
            this.endDateTime = end;
        }
    }

    /**
     *
     */
    public void removeMemo(){
        this.memo = null;
    }

    /**
     *
     * @param alert
     */
    public void removeAlert(Alert alert){
        this.alerts.remove(alert);
    }

    /**
     *
     */
    public void postpone(){
        this.startDateTime = null;
        this.endDateTime = null;
    }
}
