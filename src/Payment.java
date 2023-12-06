
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Payment extends javax.swing.JFrame {
    private int contract;
    
    static Connection conn;
    static PreparedStatement pst;
    
    public Payment() {
	initComponents();
	setLocationRelativeTo(null);
    }
    
    // Set value of contract
    public void setContract() {
	switch((String)contractBox.getSelectedItem()) {
	    case "1 month":
		contract = 1;
		break;
	    case "3 months":
		contract = 3;
	    case "6 months":
		contract = 6;
		break;
	    case "9 months":
		contract = 9;
		break;
	    case "12 months":
		contract = 12;
		break;
	}
    }
    
    // Get value of contract
    public int getContract() {
	return contract;
    }
    
    // Method to register payment for registration
    public void setRegistrationPayment(Double roomPrice, int tenantId, int roomId, String pin, int contract, String paymentType) {
	
	conn = ConnectXamppMySQL.conn();
	
	//  SQL query to register tenant
	String registerPaymentQuery = "INSERT INTO payment(tenant_id, room_id, payment_date, amount, payment_type, month_contract) VALUES (?, ?, ?, ?, ?, ?)";

	try (PreparedStatement statement = conn.prepareStatement(registerPaymentQuery)) {
	    statement.setInt(1, tenantId);
	    statement.setInt(2, roomId);
	    statement.setString(3, new Main().getDateTime());
	    statement.setDouble(4, roomPrice);
	    statement.setString(5, paymentType);
	    statement.setInt(6, contract);
	    
    
	// Execute the query
	statement.executeUpdate();
	JOptionPane.showMessageDialog(null, "Payment registration Success!");
	} catch (SQLException e) {
	    // Handle any SQL errors
	    e.printStackTrace();
	}
    }
    
    // Method to register payment for renewal
    // Method to register payment
    public void setRenewalPayment() {
	setContract();
	Room room = new Room();
	room.setRoomPrice(getContract(), Integer.parseInt(roomIdInput.getText()));
	
	conn = ConnectXamppMySQL.conn();
	
	//  SQL query to register tenant
	String registerPaymentQuery = "INSERT INTO payment(tenant_id, room_id, payment_date, amount, payment_type, month_contract) VALUES (?, ?, ?, ?, ?, ?)";

	try (PreparedStatement statement = conn.prepareStatement(registerPaymentQuery)) {
	    statement.setString(1, tenantIdInput.getText());
	    statement.setString(2, roomIdInput.getText());
	    statement.setString(3, new Main().getDateTime());
	    statement.setDouble(4, room.getRoomPrice());
	    statement.setString(5, "Renewal");
	    statement.setInt(6, getContract());
	    
    
	// Execute the query
	statement.executeUpdate();
	JOptionPane.showMessageDialog(null, "Payment renewal Success!");
	} catch (SQLException e) {
	    // Handle any SQL errors
	    e.printStackTrace();
	}
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgPanel = new javax.swing.JPanel();
        loginInputPanel = new javax.swing.JPanel();
        loginLabel = new javax.swing.JLabel();
        userIdLabel = new javax.swing.JLabel();
        userPinLabel = new javax.swing.JLabel();
        logoutPaymentButton = new javax.swing.JButton();
        userIdLabel1 = new javax.swing.JLabel();
        roomIdLabel = new javax.swing.JLabel();
        contractBox = new javax.swing.JComboBox<>();
        roomIdInput = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        tenantIdInput = new javax.swing.JTextField();
        submitPaymentButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 2));

        bgPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        loginInputPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 28)); // NOI18N
        loginLabel.setForeground(new java.awt.Color(51, 51, 51));
        loginLabel.setText("Send your Payment");
        loginInputPanel.add(loginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 290, 50));

        userIdLabel.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        userIdLabel.setText("User ID");
        loginInputPanel.add(userIdLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 120, 30));

        userPinLabel.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        userPinLabel.setText("Contract");
        loginInputPanel.add(userPinLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 120, 30));

        logoutPaymentButton.setBackground(new java.awt.Color(255, 255, 254));
        logoutPaymentButton.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        logoutPaymentButton.setForeground(new java.awt.Color(204, 0, 0));
        logoutPaymentButton.setText("Logout");
        logoutPaymentButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logoutPaymentButton.setFocusable(false);
        logoutPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutPaymentButtonActionPerformed(evt);
            }
        });
        loginInputPanel.add(logoutPaymentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 420, 60));

        userIdLabel1.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        userIdLabel1.setText("Pin");
        loginInputPanel.add(userIdLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 120, 30));

        roomIdLabel.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        roomIdLabel.setText("Room ID");
        loginInputPanel.add(roomIdLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 160, 120, 30));

        contractBox.setBackground(new java.awt.Color(255, 255, 254));
        contractBox.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        contractBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 month", "3 months", "6 months", "9 months", "12 months" }));
        contractBox.setFocusable(false);
        loginInputPanel.add(contractBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 200, 50));
        loginInputPanel.add(roomIdInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 200, 50));
        loginInputPanel.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, 200, 50));
        loginInputPanel.add(tenantIdInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 200, 50));

        submitPaymentButton.setBackground(new java.awt.Color(255, 255, 254));
        submitPaymentButton.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        submitPaymentButton.setForeground(new java.awt.Color(0, 153, 51));
        submitPaymentButton.setText("Submit Payment");
        submitPaymentButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        submitPaymentButton.setFocusable(false);
        submitPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitPaymentButtonActionPerformed(evt);
            }
        });
        loginInputPanel.add(submitPaymentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 420, 60));

        bgPanel.add(loginInputPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 210, 460, 420));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Profiles/gcash.png"))); // NOI18N
        bgPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 220, -1, -1));

        jLabel5.setFont(new java.awt.Font("Poppins ExtraBold", 0, 80)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Profiles/rent_ez_White.png"))); // NOI18N
        jLabel5.setText("RentEZ");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        bgPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 70, 460, 140));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/login/apartment.png"))); // NOI18N
        bgPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(bgPanel);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutPaymentButtonActionPerformed
        // TODO add your handling code here:
	int logoutConfirmation = JOptionPane.showConfirmDialog(null, "Do you want to logout?");
	if(logoutConfirmation == JOptionPane.YES_OPTION) {
	    dispose();
	    new LoginPage().setVisible(true);
	}
    }//GEN-LAST:event_logoutPaymentButtonActionPerformed

    private void submitPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitPaymentButtonActionPerformed
        // TODO add your handling code here:
	setRenewalPayment();
    }//GEN-LAST:event_submitPaymentButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	/* Set the Nimbus look and feel */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
	/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
	 */
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if ("Nimbus".equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		new Payment().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bgPanel;
    private javax.swing.JComboBox<String> contractBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPanel loginInputPanel;
    private javax.swing.JLabel loginLabel;
    private javax.swing.JButton logoutPaymentButton;
    private javax.swing.JTextField roomIdInput;
    private javax.swing.JLabel roomIdLabel;
    private javax.swing.JButton submitPaymentButton;
    private javax.swing.JTextField tenantIdInput;
    private javax.swing.JLabel userIdLabel;
    private javax.swing.JLabel userIdLabel1;
    private javax.swing.JLabel userPinLabel;
    // End of variables declaration//GEN-END:variables
}
