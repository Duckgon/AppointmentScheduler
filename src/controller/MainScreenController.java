package controller;

import helper.AppointmentsQuery;
import helper.CustomersQuery;
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
import model.Customer;
import model.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static model.Database.getAllAppointments;


public class MainScreenController implements Initializable {
    public Label timezone;
    public Button addCustomer;
    public Button updateCustomer;
    public Button deleteCustomer;
    public TableView<Customer> customerTable;
    public TableColumn<Customer, Integer> customerID;
    public TableColumn<Customer, String> customerName;
    public TableColumn<Customer, String> address;
    public TableColumn<Customer, String> postalCode;
    public TableColumn<Customer, String> phoneNumber;
    public TableColumn<Customer, Integer> divisionID;
    public Label error;
    public Button addAppointment;
    public Button updateAppointment;
    public Button deleteAppointment;
    public Button report;
    public Button logout;
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
    public RadioButton viewAll;
    public RadioButton viewMonth;
    public RadioButton viewWeek;
    public String viewstatus;
    public Label notificationTxt;
    public Button menu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        
        if (Database.getUserID() == 1){
            user.setText("Test User");
        } else{
            user.setText("Admin User");
        }

        //Setup and connect customer table columns
        customerTable.setItems(Database.getAllCustomers());
        customerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        divisionID.setCellValueFactory(new PropertyValueFactory<>("division"));

        //Setup and connect appointment table columns
        appointmentTable.setItems(Database.getAllAppointments());
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
        //System.out.println(date);

        //get current time zone
        timezone.setText(TimeZone.getDefault().getID());

        // Convert local current time to UTC or EST


        //Timestamp test
        Timestamp timestamp = Timestamp.valueOf("2023-04-28 11:44:00");
        //System.out.println("Timestamp: " + timestamp);


        //Timestamp to ZoneTimeDate
        ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.of("GMT"));
        //System.out.println("Zonedatetime: " + zonedDateTime);

        // ZoneTimeDate to Timestamp
        Timestamp lol = Timestamp.valueOf(zonedDateTime.toLocalDateTime());
        //System.out.println("Timestamp Again: " + lol);

        boolean warn = false;
        for (int i = 0; i < getAllAppointments().size(); i++) {
            if (LocalDateTime.now().plusMinutes(15).isAfter(getAllAppointments().get(i).getStart()) && (LocalDateTime.now().isBefore(getAllAppointments().get(i).getStart()))) {
                warn = true;
            }
        }

        if(warn){
            Alert alert = new Alert(Alert.AlertType.WARNING,"You have an upcoming appointment within the next 15 minutes.");
            Optional<ButtonType> result = alert.showAndWait();
            notificationTxt.setText("You have an upcoming appointment.");
            notificationTxt.setStyle("-fx-text-fill: red;");
        } else{
            notificationTxt.setText("You have no upcoming appointments.");
            notificationTxt.setStyle("-fx-text-fill: blue;");
        }






    }


    public static void main(String[] args) throws SQLException{

    }

    public void onAddCustomer(ActionEvent actionEvent) throws SQLException, IOException {

        Parent root = FXMLLoader.load(getClass().getResource(("/view/AddCustomer.fxml")));
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Customer");
        stage.setScene(new Scene(root, 604, 589));
        stage.show();

        //int rowsAffected = CustomersQuery.insert("Duckgon", "Hello@gmail.com", "2", "2", 2);
        //int rowsAffected = ContactsQuery.update(4,"WHYEHYEHYSE");
        //int rowsAffected = CustomersQuery.delete(4);

/*
        if(rowsAffected > 0){
            System.out.println("Insert Successful");
        } else{
            System.out.println("Insert Failed!");
        }

        // Print Contacts
        //ContactsQuery.select(2);
*/
    }


    public void onUpdateCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        try {
            // Creates new loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(("/view/UpdateCustomer.fxml")));
            loader.load();

            // Sender of data
            UpdateCustomerController MPController = loader.getController();
            //Sends selected row to modify screen
            MPController.sendCustomer(customerTable.getSelectionModel().getSelectedItem());


            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setTitle("Update Customer");
            stage.setScene(new Scene(scene));
            stage.show();
        }catch (NullPointerException NPE){
            error.setText("Error: You must select a customer from the customers table!");
        }
    }

    public void onDeleteCustomer(ActionEvent actionEvent) throws SQLException{
        //Deleted selected item
        if (customerTable.getSelectionModel().getSelectedItem() != null) {


            if (AppointmentsQuery.select(customerTable.getSelectionModel().getSelectedItem().getId())<1){
                // Create a new alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will delete the customer.");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK){

                    error.setText("A customer has been deleted (Customer ID: " + customerTable.getSelectionModel().getSelectedItem().getId() +")");
                    CustomersQuery.delete(customerTable.getSelectionModel().getSelectedItem().getId());
                    CustomersQuery.select();
                }
            } else {
                error.setText("Error: You must delete all associated appointments from the customer! (Customer ID: " + customerTable.getSelectionModel().getSelectedItem().getId() +")");
            }

            AppointmentsQuery.select(viewstatus);


        } else{
            error.setText("Error: You must select a customer from the customers table!");
        }

    }

    public void onAddAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        if (customerTable.getSelectionModel().getSelectedItem() != null){
            // Creates new loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(("/view/AddAppointment.fxml")));
            loader.load();

            // Sender of data
            AddAppointmentController MPController = loader.getController();
            //Sends selected row to modify screen
            MPController.sendCustomer(customerTable.getSelectionModel().getSelectedItem());


            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setTitle("Add Appointment");
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            error.setText("Error: You must select a customer from the customers table!");
        }

    }

    public void onUpdateAppointment(ActionEvent actionEvent) throws IOException {
        try {
            // Creates new loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(("/view/UpdateAppointment.fxml")));
            loader.load();

            // Sender of data
            UpdateAppointmentController MPController = loader.getController();
            //Sends selected row to modify screen
            MPController.sendAppointment(appointmentTable.getSelectionModel().getSelectedItem());


            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setTitle("Update Appointment");
            stage.setScene(new Scene(scene));
            stage.show();
        }catch (NullPointerException NPE){
            error.setText("Error: You must select an appointment from the appointment table!");
        }
    }

    public void onDeleteAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        //Deleted selected item
        if (appointmentTable.getSelectionModel().getSelectedItem() != null) {

            // Create a new alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will delete the customer's appointment.");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK){

                error.setText("An appointment has been deleted (Appointment ID: " + appointmentTable.getSelectionModel().getSelectedItem().getId() +") (Appointment Type: " + appointmentTable.getSelectionModel().getSelectedItem().getType() + ")");
                AppointmentsQuery.delete(appointmentTable.getSelectionModel().getSelectedItem().getId());
                AppointmentsQuery.select(viewstatus);

                boolean warn = false;
                for (int i = 0; i < getAllAppointments().size(); i++) {
                    if (LocalDateTime.now().plusMinutes(15).isAfter(getAllAppointments().get(i).getStart()) && (LocalDateTime.now().isBefore(getAllAppointments().get(i).getStart()))) {
                        warn = true;
                    }
                }

                if(warn){
                    notificationTxt.setText("You have an upcoming appointment.");
                    notificationTxt.setStyle("-fx-text-fill: red;");
                } else{
                    notificationTxt.setText("You have no upcoming appointments.");
                    notificationTxt.setStyle("-fx-text-fill: blue;");
                }
            }

        } else{
            error.setText("Error: You must select an appointment from the appointment table!");
        }
    }

    public void onReport(ActionEvent actionEvent) throws IOException, SQLException {
        AppointmentsQuery.select();
        AppointmentsQuery.selectReport();
        Parent root = FXMLLoader.load(getClass().getResource(("/view/Reports.fxml")));
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Reports");
        stage.setScene(new Scene(root, 1110,715));
        stage.show();
    }

    public void onLogout(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(("/view/login.fxml")));
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(new Scene(root, 342,406));
        stage.show();
    }

    public void onViewAll(ActionEvent actionEvent) throws SQLException {
        viewstatus = "all";
        AppointmentsQuery.select(viewstatus);
    }

    public void onViewMonth(ActionEvent actionEvent) throws SQLException {
        viewstatus = "month";
        AppointmentsQuery.select(viewstatus);

    }


    public void onViewWeek(ActionEvent actionEvent) throws SQLException {
        viewstatus = "week";
        AppointmentsQuery.select(viewstatus);
    }


}