package controller;

import helper.CustomersQuery;
import helper.DivisionQuery;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import model.Database;

import java.io.IOException;
import java.sql.SQLException;

/** This screen will allow users to add parts. **/
public class UpdateCustomerController extends Application {

    public Label typeText;
    public Button save;
    public Button cancel;

    //Text fields
    public TextField idTxt;
    public TextField nameTxt;
    public TextField addressTxt;
    public TextField postalTxt;
    public TextField phoneTxt;
    public ComboBox countryCombo;
    public ComboBox<String> divisionCombo;
    public Label error;
    ObservableList<String> country = FXCollections.observableArrayList("United States", "United Kingdom", "Canada");

    /** Main method.**/
    public static void main(String[] args) {
        launch(args);
    }
    /**Start method.**/
    @Override
    public void start(Stage primaryStage){

    }
    /** The screen disables the ID text field at initialization**/
    public void initialize(){
        idTxt.setDisable(true);
        idTxt.setText("Auto-generated");
        countryCombo.setItems(country);


    }
    /** Once the user is ready, the onSave method will check for errors and datatype incompatibilities. After everything is validated, the new part is added.**/
    public void onSave(ActionEvent actionEvent) throws IOException, SQLException {
        // Check to see if all blank spaces are filled
        if (nameTxt.getText().trim() == "" || addressTxt.getText().trim() == "" || postalTxt.getText().trim() == "" || phoneTxt.getText().trim() == "" || countryCombo.getSelectionModel().getSelectedItem() == null || divisionCombo.getSelectionModel().getSelectedItem() == null){
            error.setText("Error: Not all parameters are filled!");
        }
        else {
            int id = Integer.parseInt(idTxt.getText());
            String name = nameTxt.getText();
            String address = addressTxt.getText();
            String postal = postalTxt.getText();
            String phone = phoneTxt.getText();
            int divisionID = Integer.parseInt(divisionCombo.getSelectionModel().getSelectedItem().toString().substring(0,3).trim());

            CustomersQuery.update(id,name,address,postal,phone,divisionID);
            CustomersQuery.select();


            Parent root = FXMLLoader.load(getClass().getResource(("/view/MainScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Main Screen");
            stage.setScene(new Scene(root, 1110, 628));
            stage.show();

            }
        }

    /** This cancels the current progress and sends the user back to the main screen. **/
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(("/view/MainScreen.fxml")));
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Screen");
        stage.setScene(new Scene(root, 1110, 628));
        stage.show();
    }
    /** This receives the data from the main screen. **/
    public void sendCustomer(Customer customer) throws SQLException{

        idTxt.setText(String.valueOf(customer.getId()));
        nameTxt.setText(String.valueOf(customer.getName()));
        addressTxt.setText(String.valueOf(customer.getAddress()));
        postalTxt.setText(String.valueOf(customer.getPostalCode()));
        phoneTxt.setText(String.valueOf(customer.getPhone()));

        int division = customer.getDivision();
        Database.clearAllDivisions();
        divisionCombo.setItems(Database.getAllDivisions());
        //United States, Canada, United Kingdom respectively
        if (customer.getDivision()<=54){
            // Select the proper country and division query
            countryCombo.getSelectionModel().select(0);
            DivisionQuery.select(1);

            // Select the first division ID
            divisionCombo.getSelectionModel().selectFirst();

            // While the division ID does NOT equal to the first 3 character options, move on to the next option.
            while (Integer.parseInt(divisionCombo.getSelectionModel().getSelectedItem().toString().substring(0,3).trim()) != division){
               divisionCombo.getSelectionModel().selectNext();
            }


        } else if (customer.getDivision()>54 && customer.getDivision()<=72){
            // Select the proper country and division query
            countryCombo.getSelectionModel().select(2);
            DivisionQuery.select(3);

            // Select the first division ID
            divisionCombo.getSelectionModel().selectFirst();

            // While the division ID does NOT equal to the first 3 character options, move on to the next option.
            while (Integer.parseInt(divisionCombo.getSelectionModel().getSelectedItem().toString().substring(0,3).trim()) != division){
                divisionCombo.getSelectionModel().selectNext();
            }

        } else if (customer.getDivision()>=101){
            // Select the proper country and division query
            countryCombo.getSelectionModel().select(1);
            DivisionQuery.select(2);

            // Select the first division ID
            divisionCombo.getSelectionModel().selectFirst();

            // While the division ID does NOT equal to the first 3 character options, move on to the next option.
            while (Integer.parseInt(divisionCombo.getSelectionModel().getSelectedItem().toString().substring(0,3).trim()) != division){
                divisionCombo.getSelectionModel().selectNext();
            }

        }

    }

    public void onCountryCombo(ActionEvent actionEvent) throws SQLException {
        Database.clearAllDivisions();

        if (countryCombo.getSelectionModel().getSelectedItem().toString()=="United States"){
            DivisionQuery.select(1);
        }
        else if (countryCombo.getSelectionModel().getSelectedItem().toString()=="United Kingdom") {
            DivisionQuery.select(2);
        }
        else if (countryCombo.getSelectionModel().getSelectedItem().toString()=="Canada"){
            DivisionQuery.select(3);
        }

        divisionCombo.setItems(Database.getAllDivisions());
    }


}
