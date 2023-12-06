import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author edwin
 */
public class Tenant extends javax.swing.JFrame {
    static Connection conn;
    static PreparedStatement pst;
    
    private String firstName, lastName, middleName;
    private String contactNumber;
    private String pin;
    private String contract;
    private String gender;
    private String registrationDate;
    private String rentStatus;
    private int age;	    
    private int roomId;
    private int tenantId;
    private int contractInt;

    /**
     * Creates new form AddExpensePage
     */
    public Tenant() {
	initComponents();
	setLocationRelativeTo(null);
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }	
    
    // Set value of the first name
    public void setFirstName() {
	firstName = firstNameInput.getText();
    }
    
    // Set value of the last name
    public void setLastName() {
	lastName = lastNameInput.getText();
    }
    
    // Set value of the middle name
    public void setMiddleName() {
	middleName = middleNameInput.getText();
    }
    
    // Set value of the contact number
    public void setContactNumber() {
	contactNumber = contactNumberInput.getText();
    }
    
    // Set value of the pin
    public void setPin() {
	pin = pinInput.getText();
    }
    
    // Set value of the contract
    public void setContractInt() {
	contract = (String) contractBox.getSelectedItem();
	
	switch(contract) {
	    case "1 month":
		contractInt = 1;
		break;
	    case "3 months":
		contractInt = 3;
		break;
	    case "6 months":
		contractInt = 6;
		break;
	    case "9 months":
		contractInt = 9;
		break;
	    case "12 months":
		contractInt = 12;
		break;
	}
    }
    
    // Set value of the gender
    public void setGender() {
        if (maleRadio.isSelected()) {
            gender = "Male";
        } else if (femaleRadio.isSelected()) {
	    gender = "Female";
	}
    }
    
    
    // Set value of the contract
    public void setAge() {
	age = (int) ageSpinner.getValue();
    }
    
    // Set value of the room ID
    public void setRoomId() {
	roomId =  Integer.parseInt(roomIdInput.getText());
    }
    
    // Set value of the registration date
    public void setRegistrationDate() {
	registrationDate = new Main().getDateTime();
    }
    
    // Get the value of the first name
    public String getFirstName() {
	return firstName;
    }
    
    // Get the value of the last name
    public String getLastName() {
	return lastName;
    }
    
    // Get the value of the middle name
    public String getMiddleName() {
	return middleName;   
    }
    
    // Get the value of the contact number
    public String getContactNumber() {
	return contactNumber;
    }	
    
    // Get the value of the pin
    public String getPin() {
	return pin;
    }
    
    // Get the value of the contract
    public String getContract() {
	return contract;
    }	
    
    // Get the value of the gender
    public String getGender() {
	return gender;	
    }
    
    // Get the value of the age
    public int getAge() {
	return age;
    }
    
    // Get the value of the room ID
    public int getRoomId() {
	return roomId;
    }
    
    // Get the value of the registration date
    public String getRegistrationDate() {
	return registrationDate;
    }
    
//    // Get the value of the room price
//    public Double getRoomPrice() {
//	return roomPrice;
//    }
    
    // Get the value of the tenant id
    public int getTenantId() {
	return tenantId;
    }
    
    // Get the value of the contract int
    public int getContractInt() {
	return contractInt;
    }
    
    // Clear text for the registration inputs 
    public void clearInputs() {
	lastNameInput.setText("");
	firstNameInput.setText("");
	middleNameInput.setText("");
	ageSpinner.setValue(1);
	contactNumberInput.setText("");
	pinInput.setText("");
	roomIdInput.setText("");
	contractBox.setSelectedItem("1 month");
	genderGroup.clearSelection();
    }
    
    // Add tenants to the database
    public void registerTenant() {
	setFirstName();
	setLastName();
	setMiddleName();
	setContactNumber();
	setPin();
	setAge();
	setRoomId();
	setRegistrationDate();
	setContractInt();
	rentStatus = "Paid";	
	
	conn = ConnectXamppMySQL.conn();
	
	//  SQL query to register tenant
	String registerTenantQuery = "INSERT INTO tenants(room_id, tenant_pin, tenant_first_name, tenant_last_name, tenant_middle_name, contact_number, gender, age, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	try (PreparedStatement statement = conn.prepareStatement(registerTenantQuery)) {
	    statement.setInt(1, getRoomId());
	    statement.setString(2, getPin());
	    statement.setString(3,getFirstName());
	    statement.setString(4,getLastName());
	    statement.setString(5,getMiddleName());
	    statement.setString(6,getContactNumber());
	    statement.setString(7,getGender());
	    statement.setInt(8,getAge());
	    statement.setString(9, getRegistrationDate());
    
	// Execute the query
	statement.executeUpdate();
	JOptionPane.showMessageDialog(null, "Tenant registered Succesfully!");
	} catch (SQLException e) {
	    // Handle any SQL errors
	    e.printStackTrace();
	}
	
	// Process the payment of registration
	Room room = new Room();
	room.setRoomPrice(contractInt, roomId);
	Double roomPrice = room.getRoomPrice();
	setTenantId();
	new Payment().setRegistrationPayment(roomPrice, getTenantId(), getRoomId(), getPin(), getContractInt(), "Registration");
	room.setRoomStatus("Rented", roomId);
	setTenantIdOfRoom();
	
	clearInputs();
    }
    
    // Public void set tenant id of the room of the tenant
    public void setTenantIdOfRoom() {
	String query = "UPDATE rooms SET tenant_id = '" + getTenantId() + "' WHERE room_id = " + getRoomId();
	
	new Main().runSqlQuery(query);
    }
    
    
    // Retrieve tetnant id
    public void setTenantId() {
	conn = ConnectXamppMySQL.conn();
	
	// Query to retrieve tenant id
	String query = "SELECT tenant_id FROM tenants WHERE tenant_first_name = ? AND tenant_last_name = ? AND tenant_middle_name = ?";
	try (PreparedStatement statement = conn.prepareStatement(query)) {
	    statement.setString(1, getFirstName());
	    statement.setString(2, getLastName());
	    statement.setString(3, getMiddleName());
        
	    ResultSet resultSet = statement.executeQuery();
	    if (resultSet.next()) {
		tenantId = resultSet.getInt("tenant_id");
		System.out.println("Tenant ID: " + tenantId);
	    } else {
		System.out.println("Tenant not found.");
	    }
	} catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    // Technically does not remove tenant from the database (for data information purposes)
    // EDIT - This will not actually remove tenant info from that database
    public void removeTenant() {
	int tenant_id = Integer.valueOf(JOptionPane.showInputDialog(null, "Enter Tenant ID to be removed"));
	String updateRoomQuery = "UPDATE rooms SET tenant_id = NULL, room_status = 'Available' WHERE tenant_id = " + tenant_id;
	String updateTenantQuery = "DELETE FROM tenants WHERE tenant_id = " + tenant_id;

	int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this tenant??");
	if(confirmation == JOptionPane.YES_OPTION) {
	    new Main().runSqlQuery(updateRoomQuery);
	    new Main().runSqlQuery(updateTenantQuery);
	    JOptionPane.showMessageDialog(null, "Tenant ID: " + tenant_id + " succesfully removed!");
	}   
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        genderGroup = new javax.swing.ButtonGroup();
        expenseInputPanel = new javax.swing.JPanel();
        firstNameLabel = new javax.swing.JLabel();
        ageLabel = new javax.swing.JLabel();
        contactNumberLabel = new javax.swing.JLabel();
        genderLabel = new javax.swing.JLabel();
        contactNumberInput = new javax.swing.JTextField();
        lastNameInput = new javax.swing.JTextField();
        lastNameLabel = new javax.swing.JLabel();
        firstNameInput = new javax.swing.JTextField();
        ageSpinner = new javax.swing.JSpinner();
        middleNameLabel = new javax.swing.JLabel();
        middleNameInput = new javax.swing.JTextField();
        roomIdInput = new javax.swing.JTextField();
        roomIdLabel = new javax.swing.JLabel();
        contractBox = new javax.swing.JComboBox<>();
        pinLabel = new javax.swing.JLabel();
        contractLabel = new javax.swing.JLabel();
        femaleRadio = new javax.swing.JRadioButton();
        maleRadio = new javax.swing.JRadioButton();
        pinInput = new javax.swing.JPasswordField();
        registerTenantLabel = new javax.swing.JLabel();
        registerTenantGoBackButton = new javax.swing.JButton();
        registerTenantButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        expenseInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        expenseInputPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        expenseInputPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        firstNameLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        firstNameLabel.setText("First Name");
        expenseInputPanel.add(firstNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 120, 30));

        ageLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        ageLabel.setText("Age");
        expenseInputPanel.add(ageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 120, 30));

        contactNumberLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        contactNumberLabel.setText("Contact Number");
        expenseInputPanel.add(contactNumberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 120, 30));

        genderLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        genderLabel.setText("Gender");
        expenseInputPanel.add(genderLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 420, 120, 30));
        expenseInputPanel.add(contactNumberInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 340, 40));
        expenseInputPanel.add(lastNameInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 150, 40));

        lastNameLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        lastNameLabel.setText("Last Name");
        expenseInputPanel.add(lastNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 120, 30));
        expenseInputPanel.add(firstNameInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 150, 40));
        expenseInputPanel.add(ageSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, 150, 40));

        middleNameLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        middleNameLabel.setText("Middle Name");
        expenseInputPanel.add(middleNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 120, 30));
        expenseInputPanel.add(middleNameInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 150, 40));
        expenseInputPanel.add(roomIdInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 350, 150, 40));

        roomIdLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        roomIdLabel.setText("Room ID");
        expenseInputPanel.add(roomIdLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 120, 30));

        contractBox.setBackground(new java.awt.Color(255, 255, 254));
        contractBox.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        contractBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 month", "3 months", "6 months", "9 months", "12 months" }));
        contractBox.setFocusable(false);
        expenseInputPanel.add(contractBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 150, 40));

        pinLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        pinLabel.setText("PIN");
        expenseInputPanel.add(pinLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 120, 30));

        contractLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        contractLabel.setText("Contract");
        expenseInputPanel.add(contractLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 120, 30));

        femaleRadio.setBackground(new java.awt.Color(255, 255, 255));
        genderGroup.add(femaleRadio);
        femaleRadio.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        femaleRadio.setText("Female");
        femaleRadio.setContentAreaFilled(false);
        femaleRadio.setFocusPainted(false);
        femaleRadio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                femaleRadioMouseClicked(evt);
            }
        });
        femaleRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                femaleRadioActionPerformed(evt);
            }
        });
        expenseInputPanel.add(femaleRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 450, 80, -1));

        maleRadio.setBackground(new java.awt.Color(255, 255, 255));
        genderGroup.add(maleRadio);
        maleRadio.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        maleRadio.setText("Male");
        maleRadio.setContentAreaFilled(false);
        maleRadio.setFocusPainted(false);
        maleRadio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                maleRadioMouseClicked(evt);
            }
        });
        maleRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maleRadioActionPerformed(evt);
            }
        });
        expenseInputPanel.add(maleRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 450, -1, -1));
        expenseInputPanel.add(pinInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 140, 40));

        registerTenantLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 28)); // NOI18N
        registerTenantLabel.setText("Register Tenant");

        registerTenantGoBackButton.setBackground(new java.awt.Color(254, 254, 254));
        registerTenantGoBackButton.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        registerTenantGoBackButton.setForeground(new java.awt.Color(204, 0, 0));
        registerTenantGoBackButton.setText("Back");
        registerTenantGoBackButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registerTenantGoBackButton.setFocusable(false);
        registerTenantGoBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerTenantGoBackButtonActionPerformed(evt);
            }
        });

        registerTenantButton.setBackground(new java.awt.Color(254, 254, 254));
        registerTenantButton.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        registerTenantButton.setForeground(new java.awt.Color(0, 153, 0));
        registerTenantButton.setText("Register Tenant");
        registerTenantButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registerTenantButton.setFocusable(false);
        registerTenantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerTenantButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(registerTenantGoBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(registerTenantButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(expenseInputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(registerTenantLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(registerTenantLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(expenseInputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerTenantButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(registerTenantGoBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void registerTenantGoBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerTenantGoBackButtonActionPerformed
        // TODO add your handling code here:
	dispose();
    }//GEN-LAST:event_registerTenantGoBackButtonActionPerformed

    private void registerTenantButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerTenantButtonActionPerformed
        // TODO add your handling code here:
	registerTenant();
    }//GEN-LAST:event_registerTenantButtonActionPerformed

    private void maleRadioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maleRadioMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_maleRadioMouseClicked

    private void femaleRadioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_femaleRadioMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_femaleRadioMouseClicked

    private void femaleRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_femaleRadioActionPerformed
        // TODO add your handling code here:
	setGender();
    }//GEN-LAST:event_femaleRadioActionPerformed

    private void maleRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maleRadioActionPerformed
        // TODO add your handling code here:
	setGender();
    }//GEN-LAST:event_maleRadioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	try {
	UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
	    // Handle any exceptions
	     ex.printStackTrace();
	}

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		new Tenant().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ageLabel;
    private javax.swing.JSpinner ageSpinner;
    private javax.swing.JTextField contactNumberInput;
    private javax.swing.JLabel contactNumberLabel;
    private javax.swing.JComboBox<String> contractBox;
    private javax.swing.JLabel contractLabel;
    private javax.swing.JPanel expenseInputPanel;
    private javax.swing.JRadioButton femaleRadio;
    private javax.swing.JTextField firstNameInput;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.ButtonGroup genderGroup;
    private javax.swing.JLabel genderLabel;
    private javax.swing.JTextField lastNameInput;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JRadioButton maleRadio;
    private javax.swing.JTextField middleNameInput;
    private javax.swing.JLabel middleNameLabel;
    private javax.swing.JPasswordField pinInput;
    private javax.swing.JLabel pinLabel;
    private javax.swing.JButton registerTenantButton;
    private javax.swing.JButton registerTenantGoBackButton;
    private javax.swing.JLabel registerTenantLabel;
    private javax.swing.JTextField roomIdInput;
    private javax.swing.JLabel roomIdLabel;
    // End of variables declaration//GEN-END:variables
}
