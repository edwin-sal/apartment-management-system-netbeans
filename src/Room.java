import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Room {
    static Connection conn;
    static PreparedStatement pst;
    
    private double roomPrice;
    
    // Set the price of the room
    public void setRoomPrice(int contract, int roomId) {
	conn = ConnectXamppMySQL.conn();
	
	// Query to retrieve room price
	String sql = "SELECT room_price FROM rooms WHERE room_id = " + roomId;
	
	try (Statement statement = conn.createStatement()) {
	   ResultSet resultSet = statement.executeQuery(sql);
	    if (resultSet.next()) {
	    roomPrice = Double.parseDouble(resultSet.getString("room_price"));
//	    System.out.println("Room price" + roomPrice);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
	// Multiply based on contract
	switch(contract) {
	    case 1:
		roomPrice *= 1;
		break;
	    case 3:
		roomPrice *= 3;
		break;
	    case 6:
		roomPrice *= 6;
		break;
	    case 9:
		roomPrice *= 9;
		break;
	    case 12:
		roomPrice *= 12;
		break;
	}
    }
    
    // Get the price of the room
    public double getRoomPrice() {
	return roomPrice;
    }
    
    // Set the room status
    public void setRoomStatus(String status, int roomId) {
	new Main().runSqlQuery("UPDATE rooms SET room_status = '" + status + "' WHERE room_id = " + roomId);
    }
}
