/**



import java.time.*;
import java.util.ArrayList;

package sample;

import java.time.Duration;
import java.util.ArrayList;

public class AlertSeries {
    private String name;
    private ArrayList<Alert> alerts;

    private int id;
    private static int numAlertSeries = 0;
    private Duration duration;

    public AlertSeries(String name, Duration duration) {
        numAlertSeries ++;
        id = numAlertSeries;
        this.name = name;
        this.alerts = new ArrayList<>();
        this.duration = duration;
    }

    public void addAlert(Alert alert){
        this.alerts.add(alert);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Alert> getAlerts() {
        return alerts;
    }

    public static void bringDownNum() {
        numAlertSeries --;
    }

    // remove an alert
    public void removeAlert(int id){
        alerts.remove(id);
    }

    public void editDuration(Duration duration) {
        this.duration = duration;
    }

    public void editName(String name) {
        this.name = name;
    }
}
 */
