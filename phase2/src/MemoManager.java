//Author:Arsham
import java.util.ArrayList;


public class MemoManager {



    private ArrayList<Memo> store; //holds a list of the Memos

    public MemoManager(ArrayList<Memo> store){
        this.store = store;
    }

    public Memo addMemo(int id, String content){
        Memo newMemo = this.getContent(content);
        if (!this.contains(content)){
            newMemo = new Memo(content);
            this.store.add(newMemo);
        }
        newMemo.addEvent(id);
        return newMemo;
    }

    public void removeMemo(String content, int id){

        this.getContent(content).removeEvent(id); //Need a Memo method to remove the id of a Memo
        if (this.getContent(content).getEvents().size() == 0){
            this.store.remove(this.getContent(content));
        }
    }

    public void deleteMemo(Memo memo){
        this.store.remove(memo);
    }

    private boolean contains(String content){
        for (int i = 0; i < this.store.size(); i++) {
            if (this.store.get(i).toString().equals(content)){
                return true;
            }
        }
        return false;
    }


    private Memo getContent(String content){
        for (int i = 0; i < this.store.size(); i++) {
            if (this.store.get(i).toString().equals(content)){
                return this.store.get(i);
            }
        }
        return null;
    }

    public void removeEventMemo(Memo memo, int id){
        memo.removeEvent(id);
    } //remove a event id from a memo

    public void deleteMemo(String content){
        this.store.remove(this.getContent(content));
    } //deletes a memo completely

    public void editName(Memo memo, String content){
        memo.changeName(content);
    } //edits the name of a memo

    public ArrayList<Integer> memoEvent(Memo memo){
        return memo.getEvents();
    } //returns the id of events that have a memo

}

