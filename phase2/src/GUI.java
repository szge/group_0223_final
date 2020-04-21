import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class GUI extends JFrame {

    static JFrame f;

    static JPanel panelMain, panelSidebar, panelContent;

    static JButton buttonLoginLogout, buttonRegister, buttonAlerts, buttonCreateCalendars, buttonCalendars, buttonDeleteCalendar, buttonSearchEvents;

    static JLabel labelUsername;

    static String currUser;

    public static CalendarManager calendarManager = new CalendarManager();
    
    public static boolean loggedIn = false;

    private static final String QUOTE_FILEPATH = "phase2/src/quotes.txt";
    private static final String QUOTE_FILEPATH2 = "src/quotes.txt";

    public static void start() {
        // Create the main JFrame.
        f = new JFrame("Calendar Program");

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
        buttonAlerts.addActionListener(e -> tabAlerts());

        buttonCalendars = new JButton("Calendars");
        buttonCalendars.addActionListener(e -> tabCalendars());

        buttonCreateCalendars = new JButton("Create Calendar");
        buttonCreateCalendars.addActionListener(e -> tabCreateCalendar());

        buttonDeleteCalendar = new JButton("Delete Recent Calendar");
        buttonDeleteCalendar.addActionListener(e -> deleteCalendar());

        buttonSearchEvents = new JButton("Search Events in Calendar");
        buttonSearchEvents.addActionListener(e -> tabSearchEvents());

        panelSidebar.add(labelUsername);
        panelSidebar.add(buttonLoginLogout);
        panelSidebar.add(buttonRegister);
        panelSidebar.add(buttonAlerts);
        panelSidebar.add(buttonCalendars);
        panelSidebar.add(buttonCreateCalendars);
        panelSidebar.add(buttonDeleteCalendar);
        panelSidebar.add(buttonSearchEvents);

        disableButtons();

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
    
    private static void disableButtons(){
        buttonAlerts.setEnabled(false);
        buttonCalendars.setEnabled(false);
        buttonCreateCalendars.setEnabled(false);
        buttonDeleteCalendar.setEnabled(false);
        buttonSearchEvents.setEnabled(false);
    }

    private static void deleteCalendar() {
        calendarManager.deleteCalendar();
    }

    public static void login() {
        if(!loggedIn) {
            loggedIn = true;

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

            int result = JOptionPane.showConfirmDialog(f, panelLogin,
                    "Please Login", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                username = textFieldUsername.getText();
                password = new String(textFieldPassword.getPassword());

                int code = calendarManager.login(username, password);

                if (code == -1) {
                    // This username does not exist in the database
                    JOptionPane.showMessageDialog(f, "The user was not found. Please try again.");
                    login();
                } else if (code == -2) {
                    // The password is incorrect
                    JOptionPane.showMessageDialog(f, "Sorry, the password is incorrect. Please try again.");
                    login();
                } else {
                    // Successful login
                    JOptionPane.showMessageDialog(f, "Login successful. Welcome " + username);
                    currUser = username;
                    labelUsername.setText("Welcome " + username);
                    enableButtons();
                    quote();
                }
            }
        } else {
            loggedIn = false;
            calendarManager.logout();
            calendarManager = new CalendarManager();
            disableButtons();
            f.setTitle("Calendar Program");
            labelUsername.setText("Not Logged In!");
            refreshPanelMain();
            //clear contents
            panelMain.add(new JPanel(), BorderLayout.CENTER);
            panelMain.revalidate();
            JOptionPane.showMessageDialog(f, "Logout successful.");
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
        refreshPanelMain();
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

        JTextArea eventDesc = new JTextArea(ev.toString());
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

        return pane;
    }

    private static void createAlertsAlerts(Event ev) {
    }

    private static void editEvent(Event ev) {
        JDialog dialog = new JDialog(f, "Event Editor", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new GridLayout(0, 1));

        dialog.add(new JLabel("Name:"));
        JTextField textEditName = new JTextField(String.valueOf(ev.getName()));
        dialog.add(textEditName);
        JTextField textEditStartDateTime = new JTextField(String.valueOf(ev.getStartDateTime()));
        dialog.add(new JLabel("Start Date-Time:"));
        dialog.add(textEditStartDateTime);
        dialog.add(new JLabel("End Date-Time:"));
        JTextField textEditEndDateTime = new JTextField(String.valueOf(ev.getEndDateTime()));
        dialog.add(textEditEndDateTime);

        JPanel buttons = new JPanel(new GridLayout(1, 2));
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(e -> dialog.dispose());
        buttons.add(buttonCancel);
        JButton buttonConfirm = new JButton("Confirm");
        buttonConfirm.addActionListener(e -> {
            try {
                LocalDateTime start = LocalDateTime.parse(textEditStartDateTime.getText());
                LocalDateTime end = LocalDateTime.parse(textEditEndDateTime.getText());

                if(start.compareTo(end) > 0){
                    JOptionPane.showMessageDialog(dialog, "The start date and time cannot be later than the end date and time");
                } else {
                    ev.editName(textEditName.getText());
                    ev.setStartDateTime(start);
                    ev.setEndDateTime(end);

                    dialog.dispose();
                    tabCalendars();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        buttons.add(buttonConfirm);
        dialog.add(buttons);

        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(f);
        dialog.setVisible(true);
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

        String s = String.valueOf(JOptionPane.showInputDialog(f, "Choose the calendar:", null, JOptionPane.PLAIN_MESSAGE, null, choices, null));

        JPanel tab = new JPanel(new BorderLayout());


        // Make an Add Events panel at the top
        JPanel addEventPanel = new JPanel(new GridLayout(1, 2));

        JButton addEventButton = new JButton("Add New Event");
        addEventButton.addActionListener(e -> addEvent());
        addEventPanel.add(addEventButton);

        JButton addEventSeriesButton = new JButton("Add Event Series");
        addEventSeriesButton.addActionListener(e -> addEvents());
        addEventPanel.add(addEventSeriesButton);

        tab.add(addEventPanel, BorderLayout.NORTH);

        // Make a Scroll Pane for the body, listing all the events

        JPanel eventsList = new JPanel(new GridLayout(0, 1));

        for (Event e : calendarManager.getEvents()){
            eventsList.add(panelEvent(e));
        }

        JScrollPane scrollTab = new JScrollPane(eventsList);

        tab.add(scrollTab, BorderLayout.CENTER);

        refreshPanelMain();
        panelMain.add(tab, BorderLayout.CENTER);
        panelMain.revalidate();
        f.setTitle("Calendar " + s);
        f.revalidate();
    }

    private static void refreshPanelMain() {
        panelMain.removeAll();
        panelMain.add(panelSidebar, BorderLayout.WEST);
    }

    private static void addEvent() {
        JDialog dialog = new JDialog(f, "Event Editor", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new GridLayout(0, 1));

        dialog.add(new JLabel("Name:"));
        JTextField textEditName = new JTextField("New Event");
        dialog.add(textEditName);

        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        dialog.add(new JLabel("Start Date:"));
        // date needs to be an array since it must be final, just access it using date[0]
        final Date[] startDate = new Date[1];
        JDateChooser startDateChooser = new JDateChooser(currentDate);
        dialog.add(startDateChooser);

        dialog.add(new JLabel("Start Time:"));

        JPanel panelStartTime = new JPanel(new GridLayout(1, 5));
        String[] hours = new String[24];
        for (int i = 0; i < hours.length; i++){
            hours[i] = String.valueOf(i);
        }

        String[] minutes = new String[60];
        for (int i = 0; i < minutes.length; i++){
            minutes[i] = String.valueOf(i);
        }
        // Empty spacing to left
        panelStartTime.add(new JLabel());
        JComboBox startHours = new JComboBox(hours);
        // Set default time to current
        startHours.setSelectedIndex(currentDateTime.getHour());
        panelStartTime.add(startHours);
        panelStartTime.add(new JLabel(":", SwingConstants.CENTER));
        JComboBox startMinutes = new JComboBox(minutes);
        startMinutes.setSelectedIndex(currentDateTime.getMinute());
        panelStartTime.add(startMinutes);
        panelStartTime.add(new JLabel());
        dialog.add(panelStartTime);

        dialog.add(new JLabel("End Date:"));
        // date needs to be an array since it must be final, just access it using date[0]
        final Date[] endDate = new Date[1];
        JDateChooser endDateChooser = new JDateChooser(currentDate);
        dialog.add(endDateChooser);

        dialog.add(new JLabel("End Time:"));

        JPanel panelEndTime = new JPanel(new GridLayout(1, 5));
        // Empty spacing to left
        panelEndTime.add(new JLabel());
        JComboBox endHours = new JComboBox(hours);
        endHours.setSelectedIndex(currentDateTime.getHour());
        panelEndTime.add(endHours);
        panelEndTime.add(new JLabel(":", SwingConstants.CENTER));
        JComboBox endMinutes = new JComboBox(minutes);
        endMinutes.setSelectedIndex(currentDateTime.getMinute());
        panelEndTime.add(endMinutes);
        panelEndTime.add(new JLabel());
        dialog.add(panelEndTime);

        JPanel buttons = new JPanel(new GridLayout(1, 2));
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(e -> dialog.dispose());
        buttons.add(buttonCancel);
        JButton buttonConfirm = new JButton("Confirm");
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = textEditName.getText();
                    // Convert from Date to LocalDateTime
                    LocalDateTime startLocalDateTime = startDateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    // Set hours from selection
                    startLocalDateTime = startLocalDateTime.withHour(startHours.getSelectedIndex());
                    startLocalDateTime = startLocalDateTime.withMinute(startMinutes.getSelectedIndex());
                    LocalDateTime endLocalDateTime = endDateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    endLocalDateTime = endLocalDateTime.withHour(endHours.getSelectedIndex());
                    endLocalDateTime = endLocalDateTime.withMinute(endMinutes.getSelectedIndex());

                    if(startLocalDateTime.compareTo(endLocalDateTime) > 0){
                        JOptionPane.showMessageDialog(dialog, "The start date and time cannot be later than the end date and time");
                    }
                    else{
                        calendarManager.createEvent(name, startLocalDateTime, endLocalDateTime);
                        dialog.dispose();
                        tabCalendars();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        buttons.add(buttonConfirm);
        dialog.add(buttons);

        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(f);
        dialog.setVisible(true);
    }

    private static void addEvents() {
    }

    public static void tabCreateCalendar() {
        calendarManager.addCalendar();
    }

    public static void tabSearchEvents() {
        Object[] choices = {"Start Date", "Series"};

        String s = (String) JOptionPane.showInputDialog(f, "Search events by:", null, JOptionPane.PLAIN_MESSAGE, null, choices, null);

        ArrayList<Event> eventArrayList = new ArrayList<Event>();
        try {
            if (s.equals("Start Date")) {
                JDialog dialog = new JDialog(f, "Date Picker Dialog", Dialog.ModalityType.APPLICATION_MODAL);
                dialog.setLayout(new GridLayout(3, 1));
                dialog.add(new JLabel("Please pick a date:"));

                JDateChooser dateChooser = new JDateChooser();
                dialog.add(dateChooser);

                JButton inputButton = new JButton("Confirm");

                // date needs to be an array since it must be final, just access it using date[0]
                final Date[] date = new Date[1];

                inputButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        date[0] = dateChooser.getDate();
                        if (date[0] != null) {
                            dialog.dispose();
                        }
                    }
                });
                dialog.add(inputButton);
                dialog.setSize(300, 100);
                dialog.setLocationRelativeTo(f);
                dialog.setVisible(true);
                System.out.println("Date selection: " + date[0]);

                // Convert deprecated Date to LocalDate
                LocalDate dateFormat = date[0].toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                System.out.println(dateFormat);

                eventArrayList = calendarManager.getEventsByDate(dateFormat);
            } else if (s.equals("Series")) {

            }

            JPanel eventsList = new JPanel(new GridLayout(0, 1));

            for (Event ev : eventArrayList) {
                eventsList.add(panelEvent(ev));
            }

            JScrollPane tab = new JScrollPane(eventsList);

            refreshPanelMain();
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
            System.out.println("Trying " + QUOTE_FILEPATH);
            File myObj = new File(QUOTE_FILEPATH);
            System.out.println("Success! Found " + QUOTE_FILEPATH);
            Scanner myReader = new Scanner(myObj);
            ArrayList<String> data = new ArrayList<>();
            while (myReader.hasNextLine()) {
                data.add(myReader.nextLine());
            }
            myReader.close();
            JOptionPane.showMessageDialog(f, data.get((int) (Math.random() * data.size())), "Quote of the day", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            System.out.println("File path not found. for " + QUOTE_FILEPATH);
            System.out.println("Trying " + QUOTE_FILEPATH2);
            try {
                File myObj = new File(QUOTE_FILEPATH2);
                System.out.println("Success! Found " + QUOTE_FILEPATH2);
                Scanner myReader = new Scanner(myObj);
                ArrayList<String> data = new ArrayList<>();
                while (myReader.hasNextLine()) {
                    data.add(myReader.nextLine());
                }
                myReader.close();
                JOptionPane.showMessageDialog(f, data.get((int) (Math.random() * data.size())), "Quote of the day", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e2) {
                System.out.println("File path not found. for " + QUOTE_FILEPATH2);
                e2.printStackTrace();
            } catch (IndexOutOfBoundsException e2) {
                System.out.println("Oops, looks like the line in " + QUOTE_FILEPATH2 + " is out of bounds");
                e2.printStackTrace();
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Oops, looks like the line in " + QUOTE_FILEPATH + " is out of bounds");
            e.printStackTrace();
        }
    }
}