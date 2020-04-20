
 //Author: Arsham
 import java.time.Duration;
 import java.time.LocalDateTime;
 import java.util.ArrayList;

 public class SeriesManager {

    private ArrayList<Series> store;

    public SeriesManager(ArrayList<Series> store){
        this.store = store;
    }

    public void addSerialEvent(String name){
        Series series = new Series(name);
        this.store.add(series);
    }

    public void addEvent(Series series, Event event){
        series.addEvent(event);
    }

     public void deleteEvent(Event event){
         for (int i = 0; i < this.store.size(); i++) {
             if (this.store.get(i).contains(event)){
                 this.deleteEvent(store.get(i), event);
             }
         }
     }

     public void deleteEvent(Series series, Event event){
         series.removeEvent(event);
     }

     public Series seriesGetter(String name){
         for (Series series : this.store) {
             if (series.getName().equals(name)) {
                 return series;
             }
         }
         return null;
     }

    public void deleteSerialEvent(Series series){
        this.store.remove(series);
    }

    public void editName(Series series, String name){
        series.changeName(name);
    }

    public ArrayList<Event> eventsGetter(Series series, Event event){
        return series.getEvents();
    }
 }

