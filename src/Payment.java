
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Payment extends javax.swing.JFrame {
    static Connection conn;
    static PreparedStatement pst;
    
    public Payment() {
	initComponents();
	setLocationRelativeTo(null);
    }
    
    // Method to register payment
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

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgPanel = new javax.swing.JPanel();
        loginInputPanel = new javax.swing.JPanel();
        loginLabel = new javax.swing.JLabel();
        userIdLabel = new javax.swing.JLabel();
        userPinLabel = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        userIdLabel1 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        userIdLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        userIdInput1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
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
        loginInputPanel.add(userIdLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 120, 30));

        userPinLabel.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        userPinLabel.setText("Contract");
        loginInputPanel.add(userPinLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 120, 30));

        loginButton.setBackground(new java.awt.Color(239, 134, 128));
        loginButton.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Submit Payment");
        loginButton.setFocusable(false);
        loginInputPanel.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 420, 60));

        userIdLabel1.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        userIdLabel1.setText("Pin");
        loginInputPanel.add(userIdLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, 120, 30));

        jPasswordField2.setEditable(false);
        jPasswordField2.setEnabled(false);
        jPasswordField2.setFocusable(false);
        loginInputPanel.add(jPasswordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 220, 200, 50));

        userIdLabel2.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        userIdLabel2.setText("Total");
        loginInputPanel.add(userIdLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 120, 30));

        jComboBox1.setBackground(new java.awt.Color(255, 255, 254));
        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 month", "3 months", "6 months", "9 months", "12 months" }));
        jComboBox1.setFocusable(false);
        loginInputPanel.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 200, 50));
        loginInputPanel.add(userIdInput1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 200, 50));
        loginInputPanel.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, 200, 50));

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
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel loginInputPanel;
    private javax.swing.JLabel loginLabel;
    private javax.swing.JTextField userIdInput1;
    private javax.swing.JLabel userIdLabel;
    private javax.swing.JLabel userIdLabel1;
    private javax.swing.JLabel userIdLabel2;
    private javax.swing.JLabel userPinLabel;
    // End of variables declaration//GEN-END:variables
}
