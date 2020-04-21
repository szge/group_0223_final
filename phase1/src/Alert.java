import java.time.*;

public class Alert {

    private String name;
    private LocalDateTime dateTime;

    private int id;
    private static int numAlerts = 0;

    public boolean userAlerted = false;

    public Alert(String name, LocalDateTime when) {
        numAlerts++;
        id = numAlerts;
        this.name = name;
        dateTime = when;
    }

    public String toString() {
        return String.format("%s at %s",  name, dateTime.toString());
    }

    public int getId() {
        return id;
    }

    public static void bringDownNum() {
        numAlerts --;
    }

    public LocalDate getLocalDate() {
        return this.dateTime.toLocalDate();
    }

    public LocalTime getLocalTime() {
        return this.dateTime.toLocalTime();
    }

    public LocalDateTime getLocalDateTime() {
        return dateTime;
    }

    /**
     * Author : Arsham Moradi
     * Changes the time for the Alert
     * @param content New name of the Alert
     */
    public void changeName(String content){
        this.name  = content;
    }

    /**
     * Author : Arsham Moradi
     * Changes the time for the Alert
     * @param time New time for the alert
     */
    public void changeTime(LocalDateTime time){
        this.dateTime = time;
    }

    /**
     * Author : Arsham Moradi
     * Returns the name of this alert
     * @return  The name of this alert
     */
    public String getName(){
        return this.name;
    }
}
