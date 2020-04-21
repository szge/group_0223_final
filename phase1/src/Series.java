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
     * Adds an event to a series
     * @param newEvent event to be added to the series
     */

    public void addEvent(Event newEvent) {
        if (!events.contains(newEvent)) {
            events.add(newEvent);
        }
    }

    /**
     * Author: Arsham Moradi
     * Removes an event from a series
     * @param event event to be removed
     */

    public void removeEvent(Event event){
        if (events.contains(event)) {
            events.remove(event);
        }
    }

    /**
     * Author: Arsham Moradi
     * Checks whether an event is a part of a series
     * @param event event who's existence is going to be checked
     * @return whether or not event is in the series
     */
    public boolean contains(Event event){
        return events.contains(event);
    }

    /**
     * Author: Arsham Moradi
     * Changes the name of a series
     * @param name new name of the series
     */
    public void changeName(String name){
        this.name = name;
    }

}
