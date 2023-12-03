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
	
//	LoginPage login = new LoginPage();
//	login.setVisible(true);
	String query = "INSERT INTO users(id, name, email, password) VALUES(1, 'edwin', 'edwin@gmail.com', 'edwin123')";
	
	try {
	    pst = conn.prepareStatement(query);
	    pst.execute();
	    JOptionPane.showMessageDialog(null, "Table created!");
	} catch(SQLException e) {
	    JOptionPane.showMessageDialog(null, e);
	}
	
    }
}
