import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CalendarDataSaver {
    /**
     * @author Danial
     *
     * Converts arrays of Event, Memo, Alert, Series objects
     * into JSONArrays and puts them in the JSONObject user
     * @param events ArrayList of Event objects
     * @param memos ArrayList of Memo objects
     * @param alerts ArrayList of Alert objects
     * @param series ArrayList of Series objects
     * @param obj JSONObject representing the Json file data
     * @return True if saving process was successful, False otherwise
     * ArrayList<Event>
     */
    public void saveData (ArrayList<Event> events, ArrayList<Memo> memos, ArrayList<Alert> alerts,
                             ArrayList<Series> series, JSONObject obj,
                          String username){
        JSONObject user = new JSONObject();
        user.put("Events", saveEvents(events));
        user.put("Memos", saveMemos(memos));
        user.put("Alerts", saveAlerts(alerts));
        user.put("Series", saveSeries(series));
        obj.put(username, user);
        try(FileWriter file = new FileWriter("phase2/src/ProgramData.json")) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Danial
     *
     * Converts LocalDateTime object to its corresponding
     * JSONobject
     * @param time LocalDateTime to be converted
     * @return corresponding
     *      * JSONobject
     */
    private JSONObject TimeToJSON(LocalDateTime time){
        JSONObject obj = new JSONObject();
        obj.put("year", time.getYear());
        obj.put("month", time.getMonthValue());
        obj.put("day", time.getDayOfMonth());
        obj.put("hour", time.getHour());
        obj.put("min", time.getMinute());
        return obj;

    }

    /**
     *
     * Convert an ArrayList of Events into a JSONArray
     * @param events
     * @return corresponding JSONArray
     */
    public JSONArray saveEvents(ArrayList<Event> events){
        JSONArray array = new JSONArray();
        for(Event event:events){
            JSONObject obj = new JSONObject();
            obj.put("name", event.getName());
            obj.put("id", event.getId());
            obj.put("start time", TimeToJSON(event.getStartDateTime()));
            obj.put("end time", TimeToJSON(event.getEndDateTime()));
            JSONArray tags = new JSONArray();
            for (String tag: event.getTags()){
                tags.add(tag);
            }
            obj.put("tags", tags);
            if(event.getMemo()!= null) {
                obj.put("memo id", event.getMemo().getId());
            }else {
                obj.put("memo id", null);
            }
            JSONArray alertIds = new JSONArray();
            for (Alert alert: event.getAlerts()){
                alertIds.add(alert.getId());
            }
            obj.put("alert ids", alertIds);
            array.add(obj);
        }
        return array;
    }

    /**
     *
     * Convert an ArrayList of Memos into a JSONArray
     * @param memos
     * @return corresponding JSONArray
     */
    private JSONArray saveMemos(ArrayList<Memo> memos){
        JSONArray array = new JSONArray();
        for(Memo memo:memos){
            JSONObject obj = new JSONObject();
            obj.put("content", memo.toString());
            array.add(obj);
        }
        return array;
    }

//    /**
//     *
//     * Convert an ArrayList of AlertSeries into a JSONArray
//     * @param AlertSe
//     * @return corresponding JSONArray
//     */
//    private JSONArray saveAlertSeries(ArrayList<AlertSeries> AlertSe){
//        JSONArray array = new JSONArray();
//        for(AlertSeries alerts: AlertSe){
//            JSONObject obj = new JSONObject();
//            JSONArray alertids = new JSONArray();
//            alertids.addAll(alerts.getAlerts());
//            obj.put("name", alerts.getName());
//            obj.put("Alert ids", alertids);
//            array.add(obj);
//        }
//        return array;
//    }

    /**
     *
     * Convert an ArrayList of Alerts into a JSONArray
     * @param alerts
     * @return corresponding JSONArray
     */
    private JSONArray saveAlerts(ArrayList<Alert> alerts){
        JSONArray array = new JSONArray();
        for(Alert alert:alerts){
            JSONObject obj = new JSONObject();
            obj.put("name", alert.getName());
            obj.put("time", TimeToJSON(alert.getLocalDateTime()));
            array.add(obj);
        }
        return array;
    }

    /**
     *
     * Convert an ArrayList of Series into a JSONArray
     * @param series
     * @return corresponding JSONArray
     */
    private JSONArray saveSeries(ArrayList<Series> series){
        JSONArray array = new JSONArray();
        for(Series s: series){
            JSONObject obj = new JSONObject();
            obj.put("name", s.getName());
            JSONArray eventids = new JSONArray();
            eventids.addAll(s.getEvents());
            obj.put("event ids", eventids);
            array.add(obj);
        }
        return array;
    }
}
