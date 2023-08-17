package helper;

import model.Customer;
import model.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CustomersQuery {

    public static int insert( String Customer_Name, String Address, String Postal_Code, String Phone, int Division_ID) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,Customer_Name);
        ps.setString(2,Address);
        ps.setString(3, Postal_Code);
        ps.setString(4, Phone);
        ps.setInt(5, Division_ID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int update(int Customer_ID,String Customer_Name, String Address, String Postal_Code, String Phone, int Division_ID) throws SQLException {
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,Customer_Name);
        ps.setString(2,Address);
        ps.setString(3,Postal_Code);
        ps.setString(4,Phone);
        ps.setInt(5,Division_ID);
        ps.setInt(6,Customer_ID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int delete(int Customer_ID) throws SQLException{
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,Customer_ID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static void select() throws SQLException {
        String sql = "SELECT * FROM CUSTOMERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Database.clearAllCustomers();
        while(rs.next()){
            int Customer_ID = rs.getInt("Customer_ID");
            String Customer_Name = rs.getString("Customer_Name");
            String Address = rs.getString("Address");
            String Postal_Code = rs.getString("Postal_Code");
            String Phone = rs.getString("Phone");
            int Division_ID = rs.getInt("Division_ID");

            Database.addCustomer(new Customer(Customer_ID,Customer_Name,Address,Postal_Code,Phone,Division_ID));

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
