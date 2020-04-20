/**
 * @author Alex
 *
 * Much of the JSON parsing code was adapted from:
 * https://howtodoinjava.com/library/json-simple-read-write-json-examples/
 */


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserManager {

    private JSONArray userJsonArray;
    private static final String FILE_PATH = "phase2/src/users.json";

    /**
     * @author Alex
     *
     * UserManager constructor
     * Upon creation, it reads from users.json and parses the JSON.
     * User information (username, password, id) is stored in a JSONObject for each user.
     * An ArrayList of user JSONObjects called users stores everything.
     */
    public UserManager()
    {
        try(FileReader reader = new FileReader(FILE_PATH))
        {

            JSONParser jsonParser = new JSONParser();
            Object jsonText = jsonParser.parse(reader);
            JSONArray jsonArray = (JSONArray) jsonText;
            userJsonArray = (JSONArray) jsonText;

        } catch(FileNotFoundException e) {
            System.out.println("Users.txt not found!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @author Alex
     * @param username the username being used to log in
     * @param password the password being used to log in
     * @return
     * int representing the ID of the confirmed user
     * -1 if user does not exist at all
     * -2 if username exists but password is wrong.
     */
    public int login(String username, String password) {
        for(Object u: userJsonArray)
        {
            JSONObject uo = (JSONObject) u;
            if (uo.get("username").equals(username)) {
                if (uo.get("password").equals(password)) {
                    return Integer.parseInt(uo.get("id").toString());
                }
                return -2;
            }
        }
        return -1;
    }


    /**
     * @author Alex
     *
     * Creates a new user.
     * Adds the user to the working ArrayList of users.
     * Also appends this user to the users.json file.
     *
     * @param username the username of the user to add
     * @param password the password of the user
     * @return
     * FALSE if a matching username already exists
     * TRUE if the user is successfully added.
     */
    public Boolean createNewUser(String username, String password)
    {
        for(Object u: userJsonArray)
        {
            JSONObject uo = (JSONObject) u;
            if (uo.get("username").equals(username)) {
                return Boolean.FALSE;
            }
        }

        System.out.println("Create new user" + username + password);

        // if we've reached this point, then the username does not previously exists
        // so we create a new user
        JSONObject newUser = new JSONObject();
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("id", getNextAvailableID());
        newUser.put("Num calendars", 0);
        userJsonArray.add(newUser);

        writeUserJsonFile();

        return Boolean.TRUE;
    }

    /**
     * @author Alex
     *
     * Iterates through the users list and returns the next available ID.
     *
     * Looks through the users list, finds the highest ID, returns the next int up from that.
     * Does not look for the lowest available number.
     *
     * @return the next appropriate ID
     */
    private int getNextAvailableID()
    {
        if (userJsonArray.size() == 0) {
            return 1;
        }
        int curr_max = 0;
        for(Object userObject: userJsonArray)
        {
            JSONObject usr = (JSONObject) userObject;
            int userID = Integer.parseInt(usr.get("id").toString());
            if (userID > curr_max)
            {
                curr_max = userID;
            }
        }
        return (curr_max+1);
    }

    /**
     * @author Jonathan, Alex
     * @param userID ID of user to remove
     */
    public void deleteUserByName(int userID) {
        Object temp = new Object();
        for(Object u: userJsonArray) {
            JSONObject uo = (JSONObject) u;
            if(Integer.parseInt(uo.get("id").toString()) == (userID)){
                temp = u;
                break;
            }
        }
        userJsonArray.remove(temp);

        writeUserJsonFile();
    }

    /**
     * Writes userJsonArray to users.json
     */
    public void writeUserJsonFile() {
        try(FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(userJsonArray.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCalendar(String user) {
        for(Object u: userJsonArray)
        {
            JSONObject uo = (JSONObject) u;
            if (uo.get("username").equals(user)) {
                uo.put("Num calendars", (Long) uo.get("Num calendars") + 1);
            }
        }
        writeUserJsonFile();
    }

    public int getNumCalendars(String user) {
        for(Object u: userJsonArray) {
            JSONObject uo = (JSONObject) u;
            if (uo.get("username").equals(user)) {
                try {
                    return ((Long) uo.get("Num calendars")).intValue();
                } catch (ClassCastException e) {
                    // uo.get("Num calendars") is an integer already
                    return (int) uo.get("Num calendars");
                }
            }
        }
        return 0;
    }

    public void deleteCalendar(String user) {
        for(Object u: userJsonArray)
        {
            JSONObject uo = (JSONObject) u;
            if (uo.get("username").equals(user) && getNumCalendars(user) > 0) {
                uo.put("Num calendars", (Long) uo.get("Num calendars") - 1);
            }
        }
        writeUserJsonFile();
    }
}
