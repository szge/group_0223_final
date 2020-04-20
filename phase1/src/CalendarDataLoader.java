import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CalendarDataLoader {
    /**
     * @author Danial
     *
     * Converts the JSONObject, time, into a LocalDateTime object
     * @param  time JSONObject to be converted
     * @return
     * LocalDateTime object
     */
    private  LocalDateTime makeLocalDateTime(JSONObject time){
        int year = ((Long)time.get("year")).intValue();
        int month = ((Long)time.get("month")).intValue();
        int day = ((Long)time.get("day")).intValue();
        int hour = ((Long)time.get("hour")).intValue();
        int min = ((Long)time.get("min")).intValue();
        return (LocalDateTime.of(year, month, day, hour, min));
    }
    /**
     * @author Danial
     *
     * Loads the Arraylists in variables with Event, Memo,
     * Alert, and Series objects using JSONArray filled with
     * JSONOBjects from toBeLoaded
     * @param  toBeLoaded ArrayList containing JSONArrays
     *                   to be converted
     * @param variables ArrayList of ArrayLists to be filled with Event, Memo,
     *      * Alert, and Series objects
     */
    public void loadData(ArrayList<JSONArray> toBeLoaded, ArrayList<ArrayList> variables) {
        loadMemos(toBeLoaded.get(0), variables.get(0));
        loadAlerts(toBeLoaded.get(1), variables.get(1));
        loadEvents(toBeLoaded.get(2), variables.get(2), variables.get(0), variables.get(1));
        loadSeries(toBeLoaded.get(3), variables.get(3), variables.get(2));
    }
    /**
     * @author Danial
     *
     * Turns the JSONObjects from jsonmemos into Memo
     * objects, and adds them to memos
     * @param  jsonmemos JSONArray containing JSONObjects
     *                   to be converted
     * @param memos An Array to be filled with Memo objects
     */
    private void loadMemos(JSONArray jsonmemos, ArrayList<Memo> memos){
        for(Object jmemo: jsonmemos){
            JSONObject jsonmemo = (JSONObject) jmemo;
            String content = (String)jsonmemo.get("content");
            memos.add(new Memo(content));
        }
    }
    /**
     * @author Danial
     *
     * Turns the JSONObjects from jsonevents into Event
     * objects, and adds them to events
     * @param  jsonevents JSONArray containing JSONObjects
     *                   to be converted
     * @param events An Array to be filled with Event objects
     */
    public void loadEvents(JSONArray jsonevents, ArrayList<Event> events, ArrayList<Memo> memos,
                                  ArrayList<Alert> alerts){
        for(Object jevent: jsonevents){
            JSONObject jsonevent = (JSONObject) jevent;
            String name = (String)jsonevent.get("name");
            JSONObject stime = (JSONObject) jsonevent.get("start time");
            JSONObject etime = (JSONObject) jsonevent.get("end time");
            JSONArray tags = (JSONArray) jsonevent.get("tags");
            JSONArray alertids = (JSONArray) jsonevent.get("alert ids");
            LocalDateTime eventStartTime = makeLocalDateTime(stime);
            LocalDateTime eventEndTime = makeLocalDateTime(etime);
            Event createdEvent = new Event(name, eventStartTime, eventEndTime);
            if(jsonevent.get("memo id") != null) {
                for (Memo m : memos) {
                    if (m.getId() == ((Long) jsonevent.get("memo id")).intValue()){
                        createdEvent.addMemo(m);
                    }
                }
            }
            for(Object t: tags){
                createdEvent.addTag((String) t);
            }
            for(Object id: alertids){
                for(Alert alert: alerts){
                    if((Integer)id == alert.getId()){
                        createdEvent.addAlert(alert);
                    }
                }
            }
            events.add(createdEvent);
        }
    }
    /**
     * @author Danial
     *
     * Turns the JSONObjects from jsonseries into Series
     * objects, and adds them to series
     * @param  jsonseries JSONArray containing JSONObjects
     *                   to be converted
     * @param series An Array to be filled with Series objects
     */
    private void loadSeries(JSONArray jsonseries, ArrayList<Series> series, ArrayList<Event> events){
        for(Object js: jsonseries){
            JSONObject jsonse = (JSONObject) js;
            String name = (String)jsonse.get("name");
            Series createdSerie = new Series(name);
            for(Object id: (JSONArray)jsonse.get("event ids")){
                for(Event event: events){
                    if(((Long)id).intValue() == event.getId()){
                        createdSerie.addEvent(event);
                    }
                }
            }
            series.add(createdSerie);
        }
    }
    /**
     * @author Danial
     *
     * Turns the JSONObjects from jsonseries into Alert
     * objects, and adds them to alerts
     * @param  jsonalerts JSONArray containing JSONObjects
     *                   to be converted
     * @param alerts An Array to be filled with Alert objects
     */
    private void loadAlerts(JSONArray jsonalerts, ArrayList<Alert> alerts){
        for(Object jalert: jsonalerts){
            JSONObject jsonalert = (JSONObject) jalert;
            String name = (String)jsonalert.get("name");
            JSONObject time = (JSONObject) jsonalert.get("time");
            LocalDateTime alertTime = makeLocalDateTime(time);
            alerts.add(new Alert(name, alertTime));
        }
    }
//    /**
//     * @author Danial
//     *
//     * Turns the JSONObjects from jsonalertSeries into AlertSeries
//     * objects, and adds them to alertSeries
//     * @param  jsonalertSeries JSONArray containing JSONObjects
//     *                   to be converted
//     * @param alertSeries An Array to be filled with AlertSeries objects
//     */
//    private static void loadAlertSeries(JSONArray jsonalertSeries, ArrayList<AlertSeries> alertSeries,
//                                        ArrayList<Alert> alerts) {
//        for (Object jsonAS: jsonalertSeries) {
//            JSONObject jsonAlertSeries = (JSONObject) jsonAS;
//            String name = (String) jsonAlertSeries.get("name");
//            AlertSeries createdAlertSeries = new AlertSeries(name);
//            for(Object id: (JSONArray)jsonAlertSeries.get("Alert ids")){
//                for(Alert alert: alerts){
//                    if(((Long)id).intValue() == alert.getId()){
//                        createdAlertSeries.addAlert(alert);
//                    }
//                }
//            }
//            alertSeries.add(createdAlertSeries);
//        }
//    }
}
