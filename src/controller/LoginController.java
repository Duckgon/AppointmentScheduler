package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Database;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


public class LoginController implements Initializable {
    public TextField username;
    public TextField password;
    public Button login;
    public Label error;
    public Label timezone;
    public Label usernameLbl;
    public Label passwordLbl;
    public Label loginLbl;
    public Label timezoneLbl;
    private LocalDateTime dateTime;

    public ResourceBundle rb = ResourceBundle.getBundle("main/Nat", Locale.getDefault());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Database.setUserID(0);

        //get date and time
        String date = new SimpleDateFormat("yyyy_MM_dd HH:mm").format(Calendar.getInstance().getTime());
        //System.out.println(date);

        //get current time zone
        timezone.setText(TimeZone.getDefault().getID());



        if(Locale.getDefault().getLanguage().equals("fr")){
            System.out.println(rb.getString("hello") + " " + rb.getString("world"));
        }
        login.setText(rb.getString("Login") );
        loginLbl.setText(rb.getString("Login"));
        usernameLbl.setText(rb.getString("Username")+ ":");
        passwordLbl.setText(rb.getString("Password")+ ":");
        timezoneLbl.setText(rb.getString("Timezone")+ ":");


    }

    public void onLogin(ActionEvent actionEvent) throws IOException {

        if (Objects.equals(username.getText(), "test") && Objects.equals(password.getText(), "test")){
            Database.setUserID(1);

            try {
                FileWriter writer = new FileWriter("login_activity.txt", true);
                writer.append("\nUser " + username.getText().toString() + " successfully logged in at " + Timestamp.valueOf(LocalDateTime.now()) + " (Timezone: " + TimeZone.getDefault().getID() +")");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = FXMLLoader.load(getClass().getResource(("/view/MainScreen.fxml")));
            Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Main Screen");
            stage.setScene(new Scene(root, 1110, 628));
            stage.show();

        } else if (Objects.equals(username.getText(), "admin") && Objects.equals(password.getText(), "admin")){
            Database.setUserID(2);

            try {
                FileWriter writer = new FileWriter("login_activity.txt", true);
                writer.append("\nUser " + username.getText().toString() + " successfully logged in at " + Timestamp.valueOf(LocalDateTime.now()) + " (Timezone: " + TimeZone.getDefault().getID() +")");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = FXMLLoader.load(getClass().getResource(("/view/MainScreen.fxml")));
            Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Main Screen");
            stage.setScene(new Scene(root, 1110, 628));
            stage.show();
        } else{

            try {
                FileWriter writer = new FileWriter("login_activity.txt", true);
                writer.append("\nUser " + username.getText().toString() + " gave invalid log-in at " + Timestamp.valueOf(LocalDateTime.now()) + " (Timezone: " + TimeZone.getDefault().getID() +")");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            password.setText("");
            error.setText(rb.getString("Incorrect")+" "+rb.getString("user")+" "+rb.getString("or")+" "+rb.getString("pass")+"!");
        }
    }


}