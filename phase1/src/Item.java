import java.lang.reflect.Array;
import java.util.ArrayList;

 public class Item {

    public String name;
    public ArrayList<Event> events;

     /**
      * Author: Arsham Moradi
      * sets up an to do list item
      * @param name of the item/thing to be done
      */
    public Item(String name){this.name = name;}

     /**
      * Author: Arsham Moradi
      * Adds an event to the list of events an item is associated with
      * @param event events to be added to the item
      */
    public void addEvent(Event event){this.events.add(event);}

     /**
      * Author: Arsham Moradi
      * removes an event from an item's list of events
      * @param event event to be removed from the list of an item's events
      */
    public void removeEvent(Event event){this.events.remove(event);}

     /**
      * Author: Arsham Moradi
      * Changes the name of an item
      * @param name new name for the item
      */
    public void changeName(String name){this.name = name;}

     /**
      * Author: Arsham Moradi
      * @return the name for the item
      */
    public String toString() {
        return name;
    }

     /**
      * Author: Arsham Moradi
      * @return the list of events for an item
      */
    public ArrayList<Event> getEvents(){
        return this.events;
    }
}
