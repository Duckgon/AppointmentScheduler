package main;

import helper.AppointmentsQuery;
import helper.CustomersQuery;
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root,342,406 ));
        stage.show();
    }

    public static void main(String[] args) throws SQLException{
        JDBC.openConnection();
        CustomersQuery.select();
        AppointmentsQuery.select();
        launch(args);
    }
}