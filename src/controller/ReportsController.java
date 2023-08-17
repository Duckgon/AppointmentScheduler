package controller;

import helper.AppointmentsQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static model.Database.getAllAppointments;


public class ReportsController implements Initializable {
    public Label timezone;

    public Label error;

    public Label user;
    public TableView<Appointment> appointmentTable;
    public TableColumn<Appointment, Integer> appointmentID;
    public TableColumn<Appointment, String> title;
    public TableColumn<Appointment, String> description;
    public TableColumn<Appointment, String> location;
    public TableColumn<Appointment, String> type;
    public TableColumn<Appointment, DateTimeFormatter> start;
    public TableColumn<Appointment, DateTimeFormatter> end;
    public TableColumn<Appointment, Integer> appointmentCustomerID;
    public TableColumn<Appointment, Integer> userID;
    public TableColumn<Appointment, Integer> contactID;
    public Label notificationTxt;
    public Label previousTxt;
    public Button menu;
    public Label monthCount;
    public ComboBox monthCombo;
    public Label typeCount;
    public ComboBox typeCombo;
    public ComboBox customerCombo;
    public Label previousCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){


        // Set month combo
        ObservableList<String> month = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November","December");
        monthCombo.setItems(month);

        // Set Type item combo
        ObservableList<String> typeItems = FXCollections.observableArrayList();
        for (int i = 0; i < getAllAppointments().size(); i++){
            String typeitem = getAllAppointments().get(i).getType();


            boolean same = false;

            // For each added item, check if the string already present. If it is, set variable "same" to true.
            for (int j = 0; j < typeItems.size(); j++){

                if (typeItems.get(j).equals(typeitem)){

                    same = true;
                }
            }

            // If "same" is not true, then add the item.
            if (!same){
                typeItems.add(typeitem);
            }

        }
        typeCombo.setItems(typeItems);


        // Set customer combo

        ObservableList<Integer> customerItems = FXCollections.observableArrayList();
        for (int i = 0; i < getAllAppointments().size(); i++){
            int customeritem = getAllAppointments().get(i).getCustomerID();


            boolean same = false;

            // For each added item, check if the string already present. If it is, set variable "same" to true.
            for (int j = 0; j < customerItems.size(); j++){

                if (customerItems.get(j).equals(customeritem)){

                    same = true;
                }
            }

            // If "same" is not true, then add the item.
            if (!same){
                customerItems.add(customeritem);
            }

        }
        customerCombo.setItems(customerItems);


        typeCombo.getSelectionModel().selectFirst();
        monthCombo.getSelectionModel().selectFirst();

        //Setup and connect appointment table columns
        appointmentTable.setItems(Database.getReportAppointments());
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        contactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));



        //get date and time
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        previousTxt.setText("(Appointments that ended before: " + date + ")");

        //get current time zone
        timezone.setText(TimeZone.getDefault().getID());

        // Get number of rows based on month
        int monthNumber = 0;
        for (int i = 0; i < getAllAppointments().size(); i++){
            if ((monthCombo.getSelectionModel().getSelectedIndex()+1) == getAllAppointments().get(i).getStart().getMonthValue()){
                monthNumber++;
            }
        }
        monthCount.setText("Count: " + monthNumber);

        // Get number of rows based on type
        int typeNumber = 0;
        for (int i = 0; i < getAllAppointments().size(); i++){
            if ((typeCombo.getSelectionModel().getSelectedItem().toString().equals(getAllAppointments().get(i).getType()))){
                typeNumber++;
            }
        }
        typeCount.setText("Count: " + typeNumber);

        // Get number of rows that ended before today's date
        int previousNumber = 0;
        for (int i = 0; i < getAllAppointments().size(); i++){
            if (getAllAppointments().get(i).getEnd().isBefore(LocalDateTime.now())){
                previousNumber++;
            }
        }
        previousCount.setText("Count: " + previousNumber);






    }


    public static void main(String[] args) throws SQLException{

    }


    public void onMenu(ActionEvent actionEvent) throws IOException, SQLException {
        AppointmentsQuery.select();
        Parent root = FXMLLoader.load(getClass().getResource(("/view/MainScreen.fxml")));
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Screen");
        stage.setScene(new Scene(root, 1110, 628));
        stage.show();
    }

    public void onMonthCombo(ActionEvent actionEvent) throws SQLException {
        int monthNumber = 0;
        for (int i = 0; i < getAllAppointments().size(); i++){
            if ((monthCombo.getSelectionModel().getSelectedIndex()+1) == getAllAppointments().get(i).getStart().getMonthValue()){
                monthNumber++;
            }
        }
        monthCount.setText("Count: " + monthNumber);
    }

    public void onTypeCombo(ActionEvent actionEvent) throws SQLException {
        // Get number of rows based on type
        int typeNumber = 0;
        for (int i = 0; i < getAllAppointments().size(); i++){
            if ((typeCombo.getSelectionModel().getSelectedItem().toString().equals(getAllAppointments().get(i).getType()))){
                typeNumber++;
            }
        }
        typeCount.setText("Count: " + typeNumber);
    }

    public void onCustomerCombo(ActionEvent actionEvent) throws SQLException {
        AppointmentsQuery.selectReport(Integer.parseInt(customerCombo.getSelectionModel().getSelectedItem().toString()));
    }
}