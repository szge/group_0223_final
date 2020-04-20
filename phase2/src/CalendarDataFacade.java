import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

//import com.sun.xml.internal.fastinfoset.util.PrefixArray;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class CalendarDataFacade {
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<Memo> memos = new ArrayList<Memo>();
    private ArrayList<Alert> alerts = new ArrayList<Alert>();
    private ArrayList<Series> series = new ArrayList<Series>();
    private CalendarDataLoader loader = new CalendarDataLoader();
    private CalendarDataSaver saver = new CalendarDataSaver();
    private JSONObject jfile;
    public String usern;

    public CalendarDataFacade() {
        String filename = "src/ProgramData.json";
        File file = new File(filename);
        JSONParser parser = new JSONParser();

        //Note added by Alex: 25/03
        //Handling exceptions here since they weren't handled before
        // They need to be handled at this level or else I'll need to handle them at a higher level
        try {
            FileReader reader = new FileReader(file.getAbsolutePath());
            Object obj = parser.parse(reader);
            jfile = (JSONObject) obj;
        } catch (IOException e) {
            System.out.println("IOException thrown.");
        } catch (ParseException e) {
            System.out.println("ParseException thrown.");
        }

    }

    /**
     * @author Danial
     *
     * Returns all events
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getEvents() {
        return events;
    }
    /**
     * @author Danial
     *
     * Returns all memos
     * @return
     * ArrayList<Memo>
     */
    public ArrayList<Memo> getMemos() {
        return memos;
    }
    /**
     * @author Danial
     *
     * Returns all alerts
     * @return
     * ArrayList<Alert>
     */
    public ArrayList<Alert> getAlerts() {
        return alerts;
    }
    /**
     * @author Danial
     *
     * Returns all series
     * @return
     * ArrayList<Series>
     */
    public ArrayList<Series> getSeries() {
        return series;
    }

    /**
     * @author Danial
     *
     * Converts a LocalDateTime object to its corresponding
     * LocalDate object
     * @param time LocalDateTime object to be converted
     * @return
     * LocalDate
     */
    private  LocalDate timetoDate(LocalDateTime time){
        int year = time.getYear();
        int month = time.getMonthValue();
        int day = time.getDayOfMonth();
        return LocalDate.of(year, month, day);
    }
    /**
     * @author Danial
     *
     * Logs in the user with the given user name by
     * loading all the user's events, memos, alerts, series,
     * and alert series from the JSON file
     * @param username username of the user
     */
    public void login(String username) throws FileNotFoundException {

        ArrayList<JSONArray> toBeLoaded = new ArrayList<JSONArray>();
        try {
            JSONObject user = (JSONObject) jfile.get(username);
            toBeLoaded.add((JSONArray) user.get("Memos"));
            toBeLoaded.add((JSONArray) user.get("Alerts"));
            toBeLoaded.add((JSONArray) user.get("Events"));
            toBeLoaded.add((JSONArray) user.get("Series"));
            toBeLoaded.add((JSONArray) user.get("Alert Series"));
            usern = username;
            ArrayList<ArrayList> attributes = new ArrayList<ArrayList>();
            attributes.add(memos);
            attributes.add(alerts);
            attributes.add(events);
            attributes.add(series);
            loader.loadData(toBeLoaded, attributes);
        } catch (NullPointerException e) {
            System.out.println("Login attempt user not found in database. Creating new user profile in database.");
            addNewUser(username);
        }
    }
    /**
     * @author Danial
     *
     * Creates a new user object in the JSON file with the
     * username as the key word
     * @param username username of the new user
     */
    public void addNewUser(String username){
        saver.saveData(new ArrayList<Event>(), new ArrayList<Memo>(), new ArrayList<Alert>(), new ArrayList<Series>(),
                jfile, username);
    }
    /**
     * @author Danial
     *
     * Saves everything to the JSON file and resets everything
     */
    public void logout(){
        saver.saveData(events, memos, alerts, series, jfile, usern);
        events = new ArrayList<Event>();
        memos = new ArrayList<Memo>();
        alerts = new ArrayList<Alert>();
        series = new ArrayList<Series>();
        jfile = null;
        usern = null;

    }
    /**
     * @author Danial
     *
     * Adds an Even to a different user, other than the one that is logged in
     * @param event The event to be added
     * @param username The username of the user we want to add the event to
     */
    public void addEventToUser(Event event, String username) throws IOException {
        ArrayList<Event> toBeLoaded = new ArrayList<Event>();
        JSONObject user = (JSONObject) jfile.get(username);
        JSONArray events = (JSONArray) user.get("Events");
        loader.loadEvents(events, toBeLoaded, new ArrayList<Memo>(),
                new ArrayList<Alert>());
        Event newEvent = new Event(event.getName(), event.getStartDateTime(), event.getEndDateTime());
        for(String t: event.getTags()) {
            newEvent.addTag(t);
        }
        toBeLoaded.add(newEvent);
        Event.bringDownNum();
        user.put("Events",saver.saveEvents(toBeLoaded));
        jfile.put(username, user);
        try(FileWriter file = new FileWriter("src/ProgramData.json")) {
            file.write(jfile.toJSONString());
        }
    }


    public ArrayList<Event> getEventsByDate(LocalDate date){
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: events){
            if(date.equals(timetoDate(event.getStartDateTime()))){
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;
    }
    /**
     * @author Danial
     *
     * Iterates through events and returns the ones
     * whose end date is before today.
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getPastEvents(){
        LocalDate now = LocalDate.now();
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: events){
            if(now.isAfter(timetoDate(event.getEndDateTime()))){
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;

    }
    /**
     * @author Danial
     *
     * Iterates through events and returns the ones
     * whose start date is after today.
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getFutureEvents(){
        LocalDate now = LocalDate.now();
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: events){
            if(now.isBefore(timetoDate(event.getStartDateTime()))){
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;

    }
    /**
     * @author Danial
     *
     * Iterates through events and returns the ones
     * whose start date is today or before today and their
     * end date is today or after today. In other words
     * events that are currently in process
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getCurrentEvents(){
        LocalDate now = LocalDate.now();
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: events){
            if(!(now.isBefore(timetoDate(event.getStartDateTime()))) &&
                    !(now.isAfter(timetoDate(event.getEndDateTime())))){
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;

    }
    /**
     * @author Danial
     *
     * Iterates through events and returns the ones
     * associated with the memo provided
     * @param memo The memo whose events are to be retrieved
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getEventsByMemo(Memo memo){
        ArrayList<Integer> eventids = memo.getEvents();
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: events){
            if(eventids.contains((Integer) event.getId())) {
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;
    }
    /**
     * @author Danial
     *
     * Iterates through events and returns the ones
     * associated with the tag provided
     * @param tag The tag whose events are to be retrieved
     * @return
     * ArrayList<Event>
     */
    public ArrayList<Event> getEventsByTag(String tag){
        ArrayList<Event> eventsToReturn = new ArrayList<Event>();
        for(Event event: events){
            if(event.getTags().contains(tag)) {
                eventsToReturn.add(event);
            }
        }
        return eventsToReturn;
    }

//    public static void main(String[] args) throws IOException, ParseException {
//      CalendarDataFacade cal = new CalendarDataFacade();
//      cal.login("Danial");
//      cal.events.add(new Event("TEST", LocalDateTime.now(), LocalDateTime.now()));
//        cal.addNewUser("That guy");
//        cal.addEventToUser(new Event("TEST", LocalDateTime.now(), LocalDateTime.now()), "That guy");
//      cal.logout();
//
//    }
}