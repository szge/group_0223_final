//Author:Arsham
import java.util.ArrayList;


public class MemoManager {



    private ArrayList<Memo> store; //holds  a list of memos

    public MemoManager(ArrayList<Memo> store){
        this.store = store;
    }

    /**
     * Author: Arsham Moradi
     * Adds an event to a memo's list of event after creating that memo
     * @param id of the event being added
     * @param content content of the memo
     * @return the memo created
     */
    public Memo addMemo(int id, String content){
        Memo newMemo = this.getContent(content);
        if (!this.contains(content)){
            newMemo = new Memo(content);
            this.store.add(newMemo);
        }
        newMemo.addEvent(id);
        return newMemo;
    }

    /**
     * Author: Arsham Moradi
     * Deletes a memo
     * @param memo memo being deleted
     */
    public void deleteMemo(Memo memo){
        this.store.remove(memo);
    }

    /**
     * Author: Arsham Moradi
     * Searches to see if a memo exists with a given content
     * @param content content of the memo being checked
     * @return whether a memo exists with that content
     */
    private boolean contains(String content){
        for (Memo memo : this.store) {
            if (memo.toString().equals(content)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Author: Arsham Moradi
     * Fetches a memo with a given content
     * @param content content a memo is being matched with
     * @return memo with the content
     */
    private Memo getContent(String content){
        for (Memo memo : this.store) {
            if (memo.toString().equals(content)) {
                return memo;
            }
        }
        return null;
    }

    /**
     * Author: Arsham Moradi
     * @param memo memo being operated on
     * @param id id of the event being removed from the memo
     */
    public void removeEventMemo(Memo memo, int id){
        memo.removeEvent(id);
    }

    /**
     * Author: Arsham Moradi
     * Edits the content of a memo
     * @param memo memo fo the event which content is being edited
     * @param content new content for the memo
     */
    public void editName(Memo memo, String content){
        memo.changeName(content);
    } //edits the name of a memo

    /**
     * Author: Arsham Moradi
     * Returns the memos for an event
     * @param memo memo who's events are being returned
     * @return the events of a memo
     */
    public ArrayList<Integer> memoEvent(Memo memo){
        return memo.getEvents();
    }

}

