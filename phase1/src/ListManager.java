import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ListManager {

    public HashMap<String, ArrayList<Item>> store;

    /**
     * Author: Arsham Moradi
     * Adds a to-do list item to a specific list
     * @param name of the list
     * @param item item to be added
     */
    public void addItem(String name, Item item){
        store.get(name).add(item);
    }

    /**
     * Author: Arsham Moradi
     * Removes the done item from the list
     * @param name of the list
     * @param item that is done
     */
    public void itemDone(String name, Item item){
        store.get(name).remove(item);
    }

    /**
     * Author: Arsham Moradi
     * Creates a new item
     * @param name the name of the item to be created
     * @return the item created
     */
    public Item createItem(String name){
        return new Item(name);
    }

    /**
     * Author: Arsham Moradi
     * Adds an event to an item
     * @param item item operated on
     * @param event event added to the item's list of events
     */
    public void addEvent(Item item, Event event){
        item.addEvent(event);
    }

    /**
     * Author: Arsham Moradi
     * Removes an event from an item
     * @param item item operated on
     * @param event event to be removed from item's list of events
     */
    public void removeEvent(Item item, Event event){
        item.removeEvent(event);
    }

    /**
     * Author: Arsham Moradi
     * @param item item operated on
     * @param name new name for the item
     */
    public void changeName(Item item, String name){
        item.changeName(name);
    }

    /**
     * Author: Arsham Moradi
     * @return the name of the lists
     */
    public Set<String> getKeys(){
        return this.store.keySet();
    }
}
