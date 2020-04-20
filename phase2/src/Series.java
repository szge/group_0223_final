import java.util.ArrayList;

public class Series {
    private String name;
    private ArrayList<Event> events = new ArrayList<>();
    private int id;
    private static int numSeries = 0;

    public Series(String name) {
        numSeries ++;
        id = numSeries;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }


    /**
     * Author: Arsham Moradi
     * @param newEvent
     */

    public void addEvent(Event newEvent) {
        if (!events.contains(newEvent)) {
            events.add(newEvent);
        }
    }

    public void removeEvent(Event event){
        if (events.contains(event)) {
            events.remove(event);
        }
    }

    public boolean contains(Event event){
        return events.contains(event);
    }

    public void changeName(String name){
        this.name = name;
    }

}
