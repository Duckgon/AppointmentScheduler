package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;
import java.util.TimeZone;

/** This class provides general functions and methods for all products and parts in the program.**/
public class Database {

    // Creates an observable list for all customers
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    // Creates an observable list for all appointments
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    // Creates an observable list for all appointments
    private static ObservableList<Appointment> reportAppointments = FXCollections.observableArrayList();


    // Creates an observable list for divisions
    private static ObservableList<String> allDivisions = FXCollections.observableArrayList();

    // Creates an observable list for contacts
    private static ObservableList<String> allContacts = FXCollections.observableArrayList();

    // Creates a userID variable
    private static int userID = 0;

    // Creates starting business hours


    public static ObservableList<Appointment> getReportAppointments() {
        return reportAppointments;
    }

    public static void setReportAppointments(ObservableList<Appointment> reportAppointments) {
        Database.reportAppointments = reportAppointments;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        Database.userID = userID;
    }

    // Generate unique customer ids
    private static int customerId = 0;

    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }


    public static ObservableList<String> getAllDivisions() {
        return allDivisions;
    }

    public static ObservableList<String> getAllContacts() {
        return allContacts;
    }



    public static void setAllDivisions(ObservableList<String> allDivisions) {
        Database.allDivisions = allDivisions;
    }

    public static void clearAllDivisions() {
        Database.allDivisions.clear();
    }

    public static void clearAllContacts() {
        Database.allContacts.clear();
    }

    public static void clearAllCustomers() {
        Database.allCustomers.clear();
    }
    public static void clearAllAppointments() {
        Database.allAppointments.clear();
    }
    public static void clearReportAppointments() {
        Database.reportAppointments.clear();
    }

    // Creates a new customer
    public static void addCustomer(Customer newCustomer){
        allCustomers.add(newCustomer);
    }
    // Creates a new Appointment
    public static void addAppointment(Appointment newAppointment){
        allAppointments.add(newAppointment);
    }

    public static void addReportAppointment(Appointment newAppointment){
        reportAppointments.add(newAppointment);
    }

    // Creates a new division
    public static void addDivision(String newDivision){
        allDivisions.add(newDivision);
    }

    // Generates unique part ids (auto-generated keys)
    public static int genCustomerId(int i){
        if (i == 1){
            customerId++;
        } else{
            customerId--;
        }

        return customerId;
    }

    // TIME CONVERSIONS


    // Timestamp -> Local
    public static ZonedDateTime toLocal(ZonedDateTime ZDT){

        // Get Zone ID
        ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());

        // Convert Est to local
        ZonedDateTime localZDT = ZDT.withZoneSameInstant(zoneId);


        return localZDT;
    }


    // Timestamp -> EST
    public static ZonedDateTime toEst(ZonedDateTime ZDT){

        // Get Zone ID
        ZoneId zoneId = ZoneId.of("US/Eastern");

        // Convert Est to local
        ZonedDateTime localZDT = ZDT.withZoneSameInstant(zoneId);


        return localZDT;
    }

    // Timestamp -> GMT
    public static ZonedDateTime toGmt(ZonedDateTime ZDT){

        // Get Zone ID
        ZoneId zoneId = ZoneId.of("GMT");

        // Convert Est to local
        ZonedDateTime localZDT = ZDT.withZoneSameInstant(zoneId);


        return localZDT;
    }



}
