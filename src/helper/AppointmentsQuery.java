package helper;

import model.Appointment;
import model.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public abstract class AppointmentsQuery {

    public static int insert( String Title, String Description, String Location, String Type, Timestamp Start, Timestamp End, int Customer_ID, int User_ID, int Contact_ID) throws SQLException {
        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,Title);
        ps.setString(2,Description);
        ps.setString(3, Location);
        ps.setString(4, Type);
        ps.setTimestamp(5, Start);
        ps.setTimestamp(6, End);
        ps.setInt(7, Customer_ID);
        ps.setInt(8, User_ID);
        ps.setInt(9, Contact_ID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int update(int Appointment_ID, String Title, String Description, String Location, String Type, Timestamp Start, Timestamp End, int Customer_ID, int User_ID, int Contact_ID) throws SQLException {
        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,Title);
        ps.setString(2,Description);
        ps.setString(3, Location);
        ps.setString(4, Type);
        ps.setTimestamp(5, Start);
        ps.setTimestamp(6, End);
        ps.setInt(7, Customer_ID);
        ps.setInt(8, User_ID);
        ps.setInt(9, Contact_ID);
        ps.setInt(10, Appointment_ID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int delete(int Appointment_ID) throws SQLException{
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,Appointment_ID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static void select() throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Database.clearAllAppointments();
        while(rs.next()){
            int Appointment_ID = rs.getInt("Appointment_ID");
            String Title = rs.getString("Title");
            String Description = rs.getString("Description");
            String Location = rs.getString("Location");
            String Type = rs.getString("Type");
            Timestamp Start = rs.getTimestamp("Start");
            Timestamp End = rs.getTimestamp("End");
            int Customer_ID = rs.getInt("Customer_ID");
            int User_ID = rs.getInt("User_ID");
            int Contact_ID = rs.getInt("Contact_ID");

            LocalDateTime startDT = Start.toLocalDateTime().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDateTime();
            LocalDateTime endDT = End.toLocalDateTime().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDateTime();




            Database.addAppointment(new Appointment(Appointment_ID,Title, Description, Location, Type, startDT, endDT, Customer_ID, User_ID, Contact_ID));

        }
    }

    public static void select(String view) throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS";
        if (view != null){
            if (view.equals("week")){
                sql = "SELECT * FROM APPOINTMENTS WHERE week(Start) = week(now())";
            } else if (view.equals("month")){
                sql = "SELECT * FROM APPOINTMENTS WHERE month(Start) = month(now())";
            }

        }

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Database.clearAllAppointments();
        while(rs.next()){
            int Appointment_ID = rs.getInt("Appointment_ID");
            String Title = rs.getString("Title");
            String Description = rs.getString("Description");
            String Location = rs.getString("Location");
            String Type = rs.getString("Type");
            Timestamp Start = rs.getTimestamp("Start");
            Timestamp End = rs.getTimestamp("End");
            int Customer_ID = rs.getInt("Customer_ID");
            int User_ID = rs.getInt("User_ID");
            int Contact_ID = rs.getInt("Contact_ID");

            LocalDateTime startDT = Start.toLocalDateTime().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDateTime();
            LocalDateTime endDT = End.toLocalDateTime().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDateTime();




            Database.addAppointment(new Appointment(Appointment_ID,Title, Description, Location, Type, startDT, endDT, Customer_ID, User_ID, Contact_ID));

        }
    }


    public static int select(int Customer_ID) throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1,Customer_ID);

        ResultSet rs = ps.executeQuery();
        Database.clearAllAppointments();
        int rowsAffected = 0;
        while(rs.next()){
            int Appointment_ID = rs.getInt("Appointment_ID");
            String Title = rs.getString("Title");
            String Description = rs.getString("Description");
            String Location = rs.getString("Location");
            String Type = rs.getString("Type");
            Timestamp Start = rs.getTimestamp("Start");
            Timestamp End = rs.getTimestamp("End");
            int User_ID = rs.getInt("User_ID");
            int Contact_ID = rs.getInt("Contact_ID");

            LocalDateTime startDT = Start.toLocalDateTime().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDateTime();
            LocalDateTime endDT = End.toLocalDateTime().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDateTime();


            Database.addAppointment(new Appointment(Appointment_ID,Title, Description, Location, Type, startDT, endDT, Customer_ID, User_ID, Contact_ID));
            rowsAffected++;
        }
        return rowsAffected;

    }

    public static int selectReport(int Customer_ID) throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1,Customer_ID);

        ResultSet rs = ps.executeQuery();
        Database.clearReportAppointments();
        int rowsAffected = 0;
        while(rs.next()){
            int Appointment_ID = rs.getInt("Appointment_ID");
            String Title = rs.getString("Title");
            String Description = rs.getString("Description");
            String Location = rs.getString("Location");
            String Type = rs.getString("Type");
            Timestamp Start = rs.getTimestamp("Start");
            Timestamp End = rs.getTimestamp("End");
            int User_ID = rs.getInt("User_ID");
            int Contact_ID = rs.getInt("Contact_ID");

            LocalDateTime startDT = Start.toLocalDateTime().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDateTime();
            LocalDateTime endDT = End.toLocalDateTime().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDateTime();


            Database.addReportAppointment(new Appointment(Appointment_ID,Title, Description, Location, Type, startDT, endDT, Customer_ID, User_ID, Contact_ID));
            rowsAffected++;
        }
        return rowsAffected;

    }

    public static void selectReport() throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Database.clearReportAppointments();
        while(rs.next()){
            int Appointment_ID = rs.getInt("Appointment_ID");
            String Title = rs.getString("Title");
            String Description = rs.getString("Description");
            String Location = rs.getString("Location");
            String Type = rs.getString("Type");
            Timestamp Start = rs.getTimestamp("Start");
            Timestamp End = rs.getTimestamp("End");
            int Customer_ID = rs.getInt("Customer_ID");
            int User_ID = rs.getInt("User_ID");
            int Contact_ID = rs.getInt("Contact_ID");

            LocalDateTime startDT = Start.toLocalDateTime().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDateTime();
            LocalDateTime endDT = End.toLocalDateTime().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDateTime();




            Database.addReportAppointment(new Appointment(Appointment_ID,Title, Description, Location, Type, startDT, endDT, Customer_ID, User_ID, Contact_ID));

        }
    }



    /*

    public static void select(int Contact_ID) throws SQLException{
        String sql = "SELECT * FROM CUSTOMERS WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1,Contact_ID);

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            // int Contact_ID = rs.getInt("Contact_ID");
            String Contact_Name = rs.getString("Contact_Name");
            String Email = rs.getString("Email");
            System.out.println(Contact_ID + " | " + Contact_Name + " | " + Email);
        }
    }
    */

}
