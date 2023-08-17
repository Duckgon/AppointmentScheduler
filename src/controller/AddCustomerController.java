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
import model.Database;

import java.io.IOException;
import java.sql.SQLException;

/** This screen will allow users to add parts. **/
public class AddCustomerController extends Application {

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
        ObservableList<String> country = FXCollections.observableArrayList("United States", "United Kingdom", "Canada");
        countryCombo.setItems(country);


    }
    /** Once the user is ready, the onSave method will check for errors and datatype incompatibilities. After everything is validated, the new part is added.**/
    public void onSave(ActionEvent actionEvent) throws IOException, SQLException {






        // Check to see if all blank spaces are filled
        if (nameTxt.getText().trim() == "" || addressTxt.getText().trim() == "" || postalTxt.getText().trim() == "" || phoneTxt.getText().trim() == "" || countryCombo.getSelectionModel().getSelectedItem() == null || divisionCombo.getSelectionModel().getSelectedItem() == null){
            error.setText("Error: Not all parameters are filled!");
        }
        else {

            String name = nameTxt.getText();
            String address = addressTxt.getText();
            String postal = postalTxt.getText();
            String phone = phoneTxt.getText();
            int divisionID = Integer.parseInt(divisionCombo.getSelectionModel().getSelectedItem().toString().substring(0,3).trim());

            CustomersQuery.insert(name,address,postal,phone,divisionID);
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
