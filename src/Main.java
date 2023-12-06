import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    static Connection conn;
    static PreparedStatement pst;
    
    // A method that returns the current date and time
    public String getDateTime() {
	LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
	System.out.println(formattedDateTime);
        return formattedDateTime;
    }
    
    // Method for running basic SQL queries
    public void runSqlQuery(String query) {
	conn = ConnectXamppMySQL.conn();

	try {
	    pst = conn.prepareStatement(query);
	    pst.execute();
	} catch(SQLException e) {
		JOptionPane.showMessageDialog(null, e);
	}
    }
    
    public static void main(String[] args) {
	conn = ConnectXamppMySQL.conn();
	LoginPage login;
	HomePage home = new HomePage();
	Payment payment = new Payment();

	login = new LoginPage();
	login.setVisible(true);
	
//	login = new LoginPage();
//	login.setLoginPageCallback(new LoginPageCallback() {
//	@Override
//	public void onLoginPageDisposed() {
//	    home.setVisible(true);	 
//	    }
//	public void onLoginPageHidden() {
//	    payment.setVisible(true);	 
//	    }
//	});
//	
//	login.setVisible(true);
    }
}
    