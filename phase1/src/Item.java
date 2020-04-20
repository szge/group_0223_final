import java.lang.reflect.Array;
import java.util.ArrayList;

 public class Item {

    public String name;
    public ArrayList<Event> events;

    public Item(String name){this.name = name;}

    public void addEvent(Event event){this.events.add(event);}

    public void removeEvent(Event event){this.events.remove(event);}

    public void changeName(String name){this.name = name;}

    public String toString() {
        return name;
    }

    public ArrayList<Event> getEvents(){
        return this.events;
    }
}
