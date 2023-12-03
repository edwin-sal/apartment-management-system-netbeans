import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectXamppMySQL {
    public static Connection conn() {
	try {
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    String url = "jdbc:mysql://localhost:3306/rent_ez_db?zeroDateTimeBehavior=CONVERT_TO_NULL";
	    String username = "admin";
	    String password = "1234";
	    Connection conn = DriverManager.getConnection(url, username, password);
	    return conn;
	} catch (ClassNotFoundException|SQLException e) {
	    JOptionPane.showMessageDialog(null, e);
	}
	return null;
    }
}
