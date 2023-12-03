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
	
//	String query = "INSERT INTO users(id, name, email, password) VALUES(2, 'kath', 'kath@gmail.com', 'kath')";
//	
//	try {
//	    pst = conn.prepareStatement(query);
//	    pst.execute();
//	    JOptionPane.showMessageDialog(null, "Query added!");
//	} catch(SQLException e) {
//	    JOptionPane.showMessageDialog(null, e);
//	}

	LoginPage login = new LoginPage();
	
    }
}
