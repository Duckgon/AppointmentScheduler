package controller;

import helper.AppointmentsQuery;
import helper.ContactsQuery;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Database;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.TimeZone;

import static model.Database.getAllAppointments;

/** This screen will allow users to add parts. **/
public class UpdateAppointmentController extends Application {


    public Button save;
    public Button cancel;
    public Label Date;
    public TextField idTxt;
    public TextField customerIdTxt;
    public TextField userIdTxt;
    public TextField titleTxt;
    public TextField descriptionTxt;
    public TextField locationTxt;
    public TextField typeTxt;
    public Label error;
    public Spinner<Integer> startH;
    public Spinner<Integer> startM;
    public Spinner<Integer> endM;
    public Spinner<Integer> endH;
    public DatePicker datefield;
    public ComboBox contactCombo;
    public Label timezone;

    public SpinnerValueFactory<Integer> starthour = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23);
    public SpinnerValueFactory<Integer> startmin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59);

    public SpinnerValueFactory<Integer> endhour = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23);
    public SpinnerValueFactory<Integer> endmin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59);


    /** Main method.**/
    public static void main(String[] args) {
        launch(args);
    }
    /**Start method.**/
    @Override
    public void start(Stage primaryStage){

    }
    /** The screen disables the ID text field at initialization**/
    public void initialize() throws SQLException {
        idTxt.setDisable(true);
        idTxt.setText("Auto-generated");

        userIdTxt.setDisable(true);
        userIdTxt.setText(Integer.toString(Database.getUserID()));

        customerIdTxt.setDisable(true);



        startH.setValueFactory(starthour);
        startM.setValueFactory(startmin);

        endH.setValueFactory(endhour);
        endM.setValueFactory(endmin);

        ContactsQuery.select();
        contactCombo.setItems(Database.getAllContacts());

        datefield.setValue(LocalDate.now());

        int year = datefield.getValue().getYear();
        int month = datefield.getValue().getMonthValue();
        int day = datefield.getValue().getDayOfMonth();

        // Get business hours
        ZonedDateTime businessStartZDT = ZonedDateTime.of(year, month, day, 8, 0, 0, 0, ZoneId.of("US/Eastern"));
        ZonedDateTime businessEndZDT = businessStartZDT.plusHours(14);

        // Convert business hours to local time
        LocalTime businessStartLocal = Database.toLocal(businessStartZDT).toLocalTime();
        LocalTime businessEndLocal = Database.toLocal(businessEndZDT).toLocalTime();

        //get current time zone
        timezone.setText(TimeZone.getDefault().getID() + " (Opening Hours: " + businessStartLocal + " - " + businessEndLocal + ")");



    }
    /** Once the user is ready, the onSave method will check for errors and datatype incompatibilities. After everything is validated, the new part is added.**/
    public void onSave(ActionEvent actionEvent) throws IOException, SQLException {
        error.setText("");

        // Check to see if all blank spaces are filled
        if (titleTxt.getText().trim() == "" || descriptionTxt.getText().trim() == "" || locationTxt.getText().trim() == "" || typeTxt.getText().trim() == "" || contactCombo.getValue() == null || datefield.getValue() == null || startH.getValue() == null || startM.getValue() == null || endH.getValue() == null || endM.getValue() == null){
            error.setText("Error: Not all parameters are filled!");
        }
        else {
            // Collect values for dates
            int year = datefield.getValue().getYear();
            int month = datefield.getValue().getMonthValue();
            int day = datefield.getValue().getDayOfMonth();
            int starth = startH.getValue();
            int startm = startM.getValue();
            int endh = endH.getValue();
            int endm = endM.getValue();

            // Get meeting
            LocalTime startLocal = LocalTime.of(starth,startm);
            LocalTime endLocal = LocalTime.of(endh,endm);

            // Get business hours
            ZonedDateTime businessStartZDT = ZonedDateTime.of(year, month, day, 8, 0, 0, 0, ZoneId.of("US/Eastern"));
            ZonedDateTime businessEndZDT = businessStartZDT.plusHours(14);

            // Convert business hours to local time
            LocalTime businessStartLocal = Database.toLocal(businessStartZDT).toLocalTime();
            LocalTime businessEndLocal = Database.toLocal(businessEndZDT).toLocalTime();



            // Make sure that meeting is at LEAST one minute long
            if (startLocal.equals(endLocal)){
                error.setText("Error: Meeting must be at least one minute!");
            }
            else {

                if (businessStartLocal.isBefore(businessEndLocal)){
                    if (startLocal.isAfter(endLocal) || (startLocal.isBefore(businessStartLocal) || startLocal.isAfter(businessEndLocal) || endLocal.isBefore(businessStartLocal) || endLocal.isAfter(businessEndLocal))){

                        error.setText("Error: Meeting exceeds business hours! (Opening Hours: " + businessStartLocal + " - " + businessEndLocal + ")" );
                    }
                    else{
                        if (checkAppointmentOverlap()){
                            error.setText("Error: There is an appointment overlap!");
                        } else{
                            updateAppointment();

                            Parent root = FXMLLoader.load(getClass().getResource(("/view/MainScreen.fxml")));
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            stage.setTitle("Main Screen");
                            stage.setScene(new Scene(root, 1110, 628));
                            stage.show();
                        }

                    }

                } else if (businessStartLocal.isAfter(businessEndLocal)){
                    if (endLocal.isAfter(businessEndLocal) && endLocal.isBefore(startLocal) || startLocal.isAfter(endLocal) && startLocal.isBefore(businessStartLocal) || startLocal.isBefore(businessEndLocal.plusMinutes(1)) && endLocal.isAfter(businessStartLocal.minusMinutes(1)) || (startLocal.isBefore(businessStartLocal)) && startLocal.isAfter(businessEndLocal) ||endLocal.isBefore(businessStartLocal) && endLocal.isAfter(businessEndLocal)){

                        error.setText("Error: Meeting exceeds business hours! (Opening Hours: " + businessStartLocal + " - " + businessEndLocal + ")" );
                    }
                    else{
                        if (checkAppointmentOverlap()){
                            error.setText("Error: There is an appointment overlap!");
                        } else{
                            updateAppointment();

                            Parent root = FXMLLoader.load(getClass().getResource(("/view/MainScreen.fxml")));
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            stage.setTitle("Main Screen");
                            stage.setScene(new Scene(root, 1110, 628));
                            stage.show();
                        }
                    }
                }
            }
        }
    }

    public boolean checkAppointmentOverlap() throws SQLException{

        // Select appointments query based on customer ID only
        AppointmentsQuery.select(Integer.parseInt(customerIdTxt.getText()));
        System.out.println("Customer ID: " + Integer.parseInt(customerIdTxt.getText()));

        // Collect values for dates
        int year = datefield.getValue().getYear();
        int month = datefield.getValue().getMonthValue();
        int day = datefield.getValue().getDayOfMonth();
        int starth = startH.getValue();
        int startm = startM.getValue();
        int endh = endH.getValue();
        int endm = endM.getValue();


        ZonedDateTime startZDT = ZonedDateTime.of(year, month, day, starth, startm, 0, 0, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime endZDT = ZonedDateTime.of(year, month, day, endh, endm, 0, 0, ZoneId.of(TimeZone.getDefault().getID()));

        LocalDateTime startLDT = startZDT.toLocalDateTime();
        LocalDateTime endLDT = endZDT.toLocalDateTime();

        if (startZDT.isAfter(endZDT)) {
            endZDT = endZDT.plusDays(1);
        }

        // Remove the appointment with the same id (Only applies to modify menu)
        for (int i = 0; i < getAllAppointments().size(); i++) {
            if (getAllAppointments().get(i).getId() == Integer.parseInt(idTxt.getText())) {
                getAllAppointments().remove(i);
            }
        }

        // Loop over each iteration in Appointments Query
        for (int i = 0; i < getAllAppointments().size(); i++){

            // Get the zone date time from each selected.
            LocalDateTime starti = getAllAppointments().get(i).getStart();
            LocalDateTime endi = getAllAppointments().get(i).getEnd();


            System.out.println("Starting Appointment (Starti): " + starti);
            System.out.println("Ending Appointment (Endi): " + endi);

            System.out.println("Entered Starting data (startLDT): " + startLDT);
            System.out.println("Entered Ending data (EndLDT): " + endLDT);


            System.out.println("First Condition First Part: " + (starti.isAfter(startLDT) || starti.isEqual(startLDT)));
            System.out.println("First Condition Second Part: " + (starti.isBefore(endLDT)));
            if ( (starti.isAfter(startLDT) || starti.isEqual(startLDT)) && starti.isBefore(endLDT)){
                return true;
            }


            System.out.println("Second Condition First Part: " + endi.isAfter(startLDT));
            System.out.println("Second Condition Second Part: " + (endi.isBefore(endLDT) || endi.isEqual(endLDT)));
            if (endi.isAfter(startLDT) && (endi.isBefore(endLDT) || endi.isEqual(endLDT))){
                return true;
            }

            System.out.println("Third Condition First Part: " + (starti.isBefore(startLDT)||starti.isEqual(startLDT)));
            System.out.println("Third Condition Second Part: " + (endi.isAfter(endLDT)||endi.isEqual(endLDT)));
            if ((starti.isBefore(startLDT)||starti.isEqual(startLDT)) && (endi.isAfter(endLDT)||endi.isEqual(endLDT))){
                return true;
            }


        }

        return false;
    }
    public void updateAppointment() throws SQLException {

        // Collect values for dates
        int year = datefield.getValue().getYear();
        int month = datefield.getValue().getMonthValue();
        int day = datefield.getValue().getDayOfMonth();
        int starth = startH.getValue();
        int startm = startM.getValue();
        int endh = endH.getValue();
        int endm = endM.getValue();

        ZonedDateTime startZDT = ZonedDateTime.of(year, month, day, starth, startm, 0, 0, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime endZDT = ZonedDateTime.of(year, month, day, endh, endm, 0, 0, ZoneId.of(TimeZone.getDefault().getID()));
        if (startZDT.isAfter(endZDT)) {
            endZDT = endZDT.plusDays(1);
        }

        int id = Integer.parseInt(idTxt.getText());
        String title = titleTxt.getText();
        String description = descriptionTxt.getText();
        String location = locationTxt.getText();
        String type = typeTxt.getText();
        Timestamp start = Timestamp.valueOf(startZDT.toLocalDateTime());
        Timestamp end = Timestamp.valueOf(endZDT.toLocalDateTime());
        int customerID = Integer.parseInt(customerIdTxt.getText());
        int userID = Integer.parseInt(userIdTxt.getText());
        int contactID = Integer.parseInt(contactCombo.getSelectionModel().getSelectedItem().toString().substring(0,3).trim());

        AppointmentsQuery.update(id,title, description, location, type, start, end, customerID,userID,contactID);
        AppointmentsQuery.select();



    }


    /** This cancels the current progress and sends the user back to the main screen. **/
    public void onCancel(ActionEvent actionEvent) throws IOException, SQLException {
        AppointmentsQuery.select();
        Parent root = FXMLLoader.load(getClass().getResource(("/view/MainScreen.fxml")));
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Screen");
        stage.setScene(new Scene(root, 1110, 628));
        stage.show();
    }
    public void sendAppointment(Appointment appointment) {
        idTxt.setText(String.valueOf(appointment.getId()));
        customerIdTxt.setText(String.valueOf(appointment.getCustomerID()));
        titleTxt.setText(String.valueOf(appointment.getTitle()));
        descriptionTxt.setText(String.valueOf(appointment.getDescription()));
        locationTxt.setText(String.valueOf(appointment.getLocation()));
        typeTxt.setText(String.valueOf(appointment.getType()));
        contactCombo.getSelectionModel().select(appointment.getContactID()-1);
        datefield.setValue(appointment.getStart().toLocalDate());
        starthour.setValue(appointment.getStart().toLocalTime().getHour());
        startmin.setValue(appointment.getStart().toLocalTime().getMinute());
        endhour.setValue(appointment.getEnd().toLocalTime().getHour());
        endmin.setValue(appointment.getEnd().toLocalTime().getMinute());



    }

}
