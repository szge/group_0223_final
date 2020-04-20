import java.util.ArrayList;

public class Memo {

    private String contents;
    private ArrayList<Integer> events = new ArrayList<>();

    private int id;
    private static int numMemos = 0;


    public Memo(String contents) {
        numMemos++;
        id = numMemos;
        this.contents = contents;
        events = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getEvents() {
        return events;
    }

    public String toString() {
        return contents;
    }

    public static void bringDownNum() {
        numMemos --;
    }

    /**
     * Author: Arsham Moradi
     * @param id id of the event being added
     *
     */
    public void addEvent(Integer id) {
        if (!events.contains(id)) {
            events.add(id);
        }
    }

    /**
     * Author: Arsham Moradi
     * @param id id of the event being removed
     * Removing the event with the given id from the Memo's list of event
     */
    public void removeEvent(int id){
        this.events.remove(this.events.indexOf(id));
    }

    /**
     * Author: Arsham Moradi
     * @param content the name which the memo's name is being changed to
     * Changes the name of the Memo to the given string
     */
    public void changeName(String content){
        this.contents = content;
    }
}
