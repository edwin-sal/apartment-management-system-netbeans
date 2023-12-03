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
	LoginPage login;
	HomePage home = new HomePage();
	
	

	login = new LoginPage();
	login.setLoginPageCallback(new LoginPageCallback() {
	@Override
	public void onLoginPageDisposed() {
	    home.setVisible(true);	 
	    }
	});
	
	login.setVisible(true);
    }
}
    