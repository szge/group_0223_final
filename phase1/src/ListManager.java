import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ListManager {

    public HashMap<String, ArrayList<Item>> store;


    public void addItem(String name, Item item){
        store.get(name).add(item);
    }

    public void itemDone(String name, Item item){
        store.get(name).remove(item);
    }

    public Item createItem(String name){
        return new Item(name);
    }

    public void addEvent(Item item, Event event){
        item.addEvent(event);
    }

    public void removeEvent(Item item, Event event){
        item.removeEvent(event);
    }

    public void changeName(Item item, String name){
        item.changeName(name);
    }

    public Set<String> getKeys(){
        return this.store.keySet();
    }
}
