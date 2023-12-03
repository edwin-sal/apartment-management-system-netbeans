import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.*;

public class Main {
    static Connection conn;
    static PreparedStatement pst;
    
    public static void main(String[] args) {
	conn = ConnectXamppMySQL.conn();
	
//	String query = "INSERT INTO system_configuration (config_id, system_id, system_pin) VALUES (1, 1010, 0000)";
//	
//	try {
//	    pst = conn.prepareStatement(query);
//	    pst.execute();
//	    JOptionPane.showMessageDialog(null, "Query success!!!");
//	} catch(SQLException e) {
//	    JOptionPane.showMessageDialog(null, e);
//	}

	LoginPage login = new LoginPage();
	
    }
}
