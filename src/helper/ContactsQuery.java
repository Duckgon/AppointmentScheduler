package helper;

import model.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ContactsQuery {

    public static int insert( String Contact_Name, String Email) throws SQLException {
        String sql = "INSERT INTO CONTACTS (Contact_Name, Email) VALUES(?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,Contact_Name);
        ps.setString(2,Email);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    public static int update(int Contact_ID,String Contact_Name) throws SQLException {
        String sql = "UPDATE CONTACTS SET Contact_Name = ? WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,Contact_Name);
        ps.setInt(2,Contact_ID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int delete(int Contact_ID) throws SQLException{
        String sql = "DELETE FROM CONTACTS WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,Contact_ID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static void select() throws SQLException{
        String sql = "SELECT * FROM CONTACTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        Database.clearAllContacts();
        while(rs.next()){
            int Contact_ID = rs.getInt("Contact_ID");
            String Contact_Name = rs.getString("Contact_Name");
            String Email = rs.getString("Email");
            Database.getAllContacts().add(Contact_ID + "  -  (" + Contact_Name + ") ["+ Email + "]");
        }
    }
    public static void select(int Contact_ID) throws SQLException{
        String sql = "SELECT * FROM CONTACTS WHERE Contact_ID = ?";
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
}
