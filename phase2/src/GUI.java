import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GUI extends JFrame {
    // TODO: Fix stupid bug, replication: Login Danial:abcd, Calendars -> 1, Search Events -> Tab, ghost buttons????

    static JFrame f;

    static JPanel panelMain, panelSidebar, panelContent;

    static JButton buttonLoginLogout, buttonRegister, buttonAlerts, buttonCreateCalendars, buttonCalendars, buttonDeleteCalendar, buttonSearchEvents;

    static JLabel labelUsername;

    static String currUser;

    public static CalendarManager calendarManager = new CalendarManager();

    private static final String QUOTE_FILEPATH = "phase2/src/quotes.txt";

    public static void start() {
        // Create the main JFrame.
        f = new JFrame("panel");

        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());

        // Create sidebar panel and fill it with contents.
        panelSidebar = new JPanel();
        panelSidebar.setLayout(new GridLayout(0, 1));
        panelSidebar.setSize(100, panelMain.getHeight());
        labelUsername = new JLabel("Not Logged In!", SwingConstants.CENTER);
        buttonLoginLogout = new JButton("Login/Logout");

        buttonLoginLogout.addActionListener(e -> login());

        buttonRegister = new JButton("Register");
        buttonRegister.addActionListener(e -> register());

        buttonAlerts = new JButton("Alerts");
        buttonAlerts.setEnabled(false);
        buttonAlerts.addActionListener(e -> tabAlerts());

        buttonCalendars = new JButton("Calendars");
        buttonCalendars.setEnabled(false);
        buttonCalendars.addActionListener(e -> tabCalendars());

        buttonCreateCalendars = new JButton("Create Calendar");
        buttonCreateCalendars.setEnabled(false);
        buttonCreateCalendars.addActionListener(e -> tabCreateCalendar());
        
        buttonDeleteCalendar = new JButton("Delete Recent Calendar");
        buttonDeleteCalendar.setEnabled(false);
        buttonDeleteCalendar.addActionListener(e -> deleteCalendar());

        buttonSearchEvents = new JButton("Search Events");
        buttonSearchEvents.setEnabled(false);
        buttonSearchEvents.addActionListener(e -> tabSearchEvents());

        panelSidebar.add(labelUsername);
        panelSidebar.add(buttonLoginLogout);
        panelSidebar.add(buttonRegister);
        panelSidebar.add(buttonAlerts);
        panelSidebar.add(buttonCalendars);
        panelSidebar.add(buttonCreateCalendars);
        panelSidebar.add(buttonDeleteCalendar);
        panelSidebar.add(buttonSearchEvents);

        panelSidebar.setSize(300, panelMain.getHeight());
        panelSidebar.setBackground(Color.LIGHT_GRAY);
        panelMain.add(panelSidebar, BorderLayout.WEST);

        // Create contents panel and fill it with contents.

        panelContent = new JPanel();

        //panelContent.setBackground(Color.BLUE);

        panelMain.add(panelContent, BorderLayout.CENTER);


        // add panel to frame
        f.add(panelMain);

        // set the size of frame
        f.setSize(900, 600);

        f.setVisible(true);
    }

    private static void enableButtons(){
        buttonAlerts.setEnabled(true);
        buttonCalendars.setEnabled(true);
        buttonCreateCalendars.setEnabled(true);
        buttonDeleteCalendar.setEnabled(true);
        buttonSearchEvents.setEnabled(true);
    }

    private static void deleteCalendar() {
        calendarManager.deleteCalendar();
    }

    public static void login() {
        // TODO: Refresh current contents

        // Create popup window and contents
        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(new GridLayout(4, 1));
        JTextField textFieldUsername = new JTextField(20);
        JPasswordField textFieldPassword = new JPasswordField(20);
        panelLogin.add(new JLabel("Username:"));
        panelLogin.add(textFieldUsername);
        panelLogin.add(new JLabel("Password:"));
        panelLogin.add(textFieldPassword);

        String username, password;

        int result = JOptionPane.showConfirmDialog(null, panelLogin,
                "Please Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            username = textFieldUsername.getText();
            password = new String(textFieldPassword.getPassword());

            int code = calendarManager.login(username, password);

            if (code == -1) {
                // This username does not exist in the database
                JOptionPane.showMessageDialog(null, "The user was not found. Please try again.");
                login();
            } else if (code == -2) {
                // The password is incorrect
                JOptionPane.showMessageDialog(null, "Sorry, the password is incorrect. Please try again.");
                login();
            } else {
                // Successful login
                JOptionPane.showMessageDialog(null, "Login successful. Welcome " + username);
                currUser = username;
                labelUsername.setText("Welcome " + username);
                enableButtons();
                quote();
            }
        }
    }

    public static void register() {
        JPanel panelRegister = new JPanel();
        panelRegister.setLayout(new GridLayout(6, 1));
        JTextField textFieldUsername = new JTextField(20);
        JPasswordField textFieldPassword = new JPasswordField(20);
        JPasswordField textFieldConfirmPassword = new JPasswordField(20);
        panelRegister.add(new JLabel("Username:"));
        panelRegister.add(textFieldUsername);
        panelRegister.add(new JLabel("Password:"));
        panelRegister.add(textFieldPassword);
        panelRegister.add(new JLabel("Confirm Password:"));
        panelRegister.add(textFieldConfirmPassword);

        String username, password, confirmPassword;

        int result = JOptionPane.showConfirmDialog(null, panelRegister,
                "Please Register", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            username = textFieldUsername.getText();
            password = new String(textFieldPassword.getPassword());
            confirmPassword = new String(textFieldConfirmPassword.getPassword());

            if (!password.equals(confirmPassword)) {
                // The password and confirm password fields do not match
                JOptionPane.showMessageDialog(null, "The passwords do not match. Please try again.");
                register();
            } else {
                if (calendarManager.createNewUser(username, password)) {
                    calendarManager.login(username, password);
                    // Successful register
                    JOptionPane.showMessageDialog(null, "User successfully created.");
                    // Log in user
                    JOptionPane.showMessageDialog(null, "Login successful. Welcome " + username);
                    currUser = username;
                    labelUsername.setText("Welcome " + username);
                    enableButtons();
                } else {
                    // This username already exists
                    JOptionPane.showMessageDialog(null, "This username already exists. Please try again or login.");
                    register();
                }
            }
        }
    }

    public static void tabAlerts() {
        JPanel tab = new JPanel();
        for (Alert a : calendarManager.getAlerts()) {
            // TODO: this
        }
        panelMain.add(tab, BorderLayout.CENTER);
        panelMain.revalidate();
        f.revalidate();
    }

    public static JPanel panelAlert(Alert a) {
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // natural height, maximum width
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel alertDesc = new JLabel("Filler text \n HELLO \n Line 3");
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.ipady = 60;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(alertDesc, c);

        JButton button = new JButton("Clear alert");
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 0;
        button.addActionListener(e -> clearAlert(a));
        pane.add(button, c);

        return pane;
    }

    private static void clearAlert(Alert a) {
    }

    /**
     * Given an event, returns the panel representation of the event, displaying all relevant information about an event, and giving options for editing and sharing.
     *
     * @param ev The event to be represented
     * @return The panel representation of the event
     */
    public static JPanel panelEvent(Event ev) {
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());

        JButton button;
        GridBagConstraints c = new GridBagConstraints();
        // natural height, maximum width
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel eventDesc = new JLabel("Filler text \n HELLO \n Line 3");
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.ipady = 60;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(eventDesc, c);

        button = new JButton("Send");
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 0;
        button.addActionListener(e -> sendEvent(ev));
        pane.add(button, c);

        button = new JButton("Edit");
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.ipady = 0;
        button.addActionListener(e -> editEvent(ev));
        pane.add(button, c);

        button = new JButton("Memos");
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 1;
        c.ipady = 0;
        pane.add(button, c);

        return pane;
    }

    private static void editEvent(Event ev) {
    }

    //sends this event to another user
    private static void sendEvent(Event ev) {


    }

    public static void tabCalendars() {
        int numCalendars = calendarManager.getNumCalendars();
        Object[] choices = new Object[numCalendars];

        for (int i = 0; i < numCalendars; i++) {
            choices[i] = i + 1;
        }

        String s = String.valueOf(JOptionPane.showInputDialog(null, "Choose the calendar:", null, JOptionPane.PLAIN_MESSAGE, null, choices, null));

        JPanel tab = new JPanel(new BorderLayout());


        // Make an Add Events button at the top
        JButton addEventButton = new JButton("Add New Event(s)");
        addEventButton.addActionListener(e -> addEvent());
        tab.add(addEventButton, BorderLayout.NORTH);

        // Make a Scroll Pane for the body, listing all the events

        JPanel eventsList = new JPanel(new GridLayout(0, 1));

        for (Event e : calendarManager.getEvents()){
            eventsList.add(panelEvent(e));
        }

//        for (int i = 0; i < 10; i++){
//            eventsList.add(panelEvent(null));
//        }

        JScrollPane scrollTab = new JScrollPane(eventsList);

        tab.add(scrollTab, BorderLayout.CENTER);

        panelMain.add(tab, BorderLayout.CENTER);
        panelMain.revalidate();
        f.setTitle("Calendar " + s);
        f.revalidate();
    }

//    private static void clearPanelContent() {
//        Component[] componentList = panelContent.getComponents();
//
//        //Loop through the components
//        for(Component c : componentList){
//            panelContent.remove(c);
//        }
//
//        panelMain.remove(panelContent);
//
//        panelContent = new JPanel();
//
//        panelMain.add(panelContent, BorderLayout.CENTER);
//        panelMain.revalidate();
//        f.revalidate();
//    }

    private static void addEvent() {
    }

    public static void tabCreateCalendar() {
        calendarManager.addCalendar();
    }

    public static void tabSearchEvents() {
        Object[] choices = {"Tag", "Date", "Memo", "Series"};

        String s = (String) JOptionPane.showInputDialog(null, "Search events by:", null, JOptionPane.PLAIN_MESSAGE, null, choices, null);

        ArrayList<Event> eventArrayList = new ArrayList<Event>();
        try {
            if (s.equals("Tag")) {

            } else if (s.equals("Date")) {

            } else if (s.equals("Memo")) {

            } else if (s.equals("Series")) {

            }

            JPanel eventsList = new JPanel(new GridLayout(0, 1));

            for (int i = 0; i < 10; i++) {
                eventsList.add(panelEvent(null));
            }

            JScrollPane tab = new JScrollPane(eventsList);

            panelMain.add(tab, BorderLayout.CENTER);
            panelMain.revalidate();
            f.revalidate();
        } catch (NullPointerException e) {
            System.out.println("You probably quit without pressing okay.");
            e.printStackTrace();
        }
    }

    // Display quote of the day popup
    public static void quote() {
        try {
            File myObj = new File(QUOTE_FILEPATH);
            Scanner myReader = new Scanner(myObj);
            ArrayList<String> data = new ArrayList<>();
            while (myReader.hasNextLine()) {
                data.add(myReader.nextLine());
            }
            myReader.close();

            JOptionPane.showMessageDialog(null, data.get((int) (Math.random() * data.size())), "Quote of the day", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            System.out.println("File path for quotes.txt not found.");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Oops, looks like the line in quotes.txt is out of bounds");
            e.printStackTrace();
        }
    }
}