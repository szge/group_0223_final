import java.util.ArrayList;

/**
 * Author: Arsham Moradi
 */
 public class SeriesManager {

    private ArrayList<Series> store; //Stores all the series for the series manager

    public SeriesManager(ArrayList<Series> store){
        this.store = store;
    }

     /**
      * Creates a new series
      * @param name
      */
    public void addSerialEvent(String name){
        Series series = new Series(name);
        this.store.add(series);
    }

     /**
      * Adds an event to a series
      * @param series
      * @param event
      */
    public void addEvent(Series series, Event event){
        series.addEvent(event);
    }

     /**
      * Deletes an event from any existing series
      * @param event
      */
     public void deleteEvent(Event event){
         for (int i = 0; i < this.store.size(); i++) {
             if (this.store.get(i).contains(event)){
                 this.deleteEvent(store.get(i), event);
             }
         }
     }

     /**
      * Deletes an event from a series
      * @param series
      * @param event
      */
     public void deleteEvent(Series series, Event event){
         series.removeEvent(event);
     }

     /**
      * Getter for a series by name
      * @param name
      * @return returns the series with a given name
      */
     public Series seriesGetter(String name){
         for (Series series : this.store) {
             if (series.getName().equals(name)) {
                 return series;
             }
         }
         return null;
     }

     /**
      * Deletes a series
      * @param series
      */
    public void deleteSerialEvent(Series series){
        this.store.remove(series);
    }

     /**
      * Edits the name of a series
      * @param series
      * @param name
      */
    public void editName(Series series, String name){
        series.changeName(name);
    }

 }

