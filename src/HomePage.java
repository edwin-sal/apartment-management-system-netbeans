
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author edwin
 */
public class HomePage extends javax.swing.JFrame {
    static Connection conn;
    static PreparedStatement pst;

    /**
     * Creates new form HomePage
     */
    public HomePage() {
	initComponents();
	setLocationRelativeTo(null);
	setResizable(false);
//	setSize(1370, 720);
//	jPanel1.setVisible(false);
	sidebarHoverEffect(dashboardButton, "dashboard_icon.png", "dashboard_icon_white.png");
	sidebarHoverEffect(viewTenantButton, "view_tenants_icon.png", "view_tenants_icon_white.png");
	sidebarHoverEffect(viewRoomButton, "view_rooms_icon.png", "view_rooms_icon_white.png");
	sidebarHoverEffect(incomeReportButton, "money_icon.png", "money_icon_white.png");
	sidebarHoverEffect(settingsButton, "settings_icon.png", "settings_icon_white.png");
	addDate();
	addTime();
	setAvailableRoomsCardLabel();
	setRentedRoomsCardLabel();
	populateLatestTransactionTable();
	populateLatestTenantTable();
	populateRegisteredTenantsTable();
	popoulateAddedRoomsTable();
	populateTransactionHistoryTable();
//	setVisible(false);
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
    
   // Add hover effect for the sidebar buttons
    public void sidebarHoverEffect(JButton button, String iconName, String whiteIconName) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(44, 52, 66));
                button.setForeground(Color.white);
                button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/sidebar_icons/" + whiteIconName))); 

            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 68, 77));
                button.setForeground(new Color(175, 190, 203));
                button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/sidebar_icons/" + iconName))); 
            }
        });
    }
    
    // Hide all content panels
    public void hideContentPanels() {
	dashboardPanel.setVisible(false);
	viewRegisteredTenantsPanel.setVisible(false);
	viewAddedRoomsPanel.setVisible(false);
	incomeReportPanel.setVisible(false);
	settingsPanel.setVisible(false);
    }
    
    // Add date in the title bar
    public void addDate() {
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("MM-d-yyyy");
	
	String dd = sdf.format(date);
	dateLabel.setText(dd);
    }
    
    // Add time in the title bar
    public void addTime() {
        Timer t = new Timer(1000, new ActionListener() { // Set the timer delay to 1000 milliseconds (1 second)
            @Override
            public void actionPerformed(ActionEvent ae) {
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
                Date date = new Date();
                String time = sdf.format(date);
                timeLabel.setText(time);
            }
        });

        t.start(); // Start the timer
    }
    
    // A method that returns the current date and time
    public String getDateTime() {
	LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
    }
    
    // Add rooms to the database
    public void addRoom() {
	conn = ConnectXamppMySQL.conn();
	
	// Values to be inserted
	int roomId = Integer.parseInt(roomIdInput.getText()); 
	String roomType = (String) roomTypeBox.getSelectedItem();
	int roomCapacity = Integer.parseInt((String) roomCapacityBox.getSelectedItem());
	String dateTime = getDateTime(); 
	double roomPrice = Double.parseDouble((String)roomPriceInput.getText());
	
	//SQL query
	String query = "INSERT INTO rooms(room_id, room_type, room_capacity, date_added, room_price) VALUES (?, ?, ?, ?, ?)";

	try (PreparedStatement statement = conn.prepareStatement(query)) {
	    statement.setInt(1, roomId);
	    statement.setString(2, roomType);
	    statement.setInt(3, roomCapacity);
	    statement.setString(4, dateTime);
	    statement.setDouble(5, roomPrice);
    
	// Execute the query
	statement.executeUpdate();
	JOptionPane.showMessageDialog(null, "Room added Succesfully!");
	} catch (SQLException e) {
	    // Handle any SQL errors
	    e.printStackTrace();
	}
    }
    
    // Method for removing room
    public void removeRoom() {
	int roomId = Integer.valueOf(JOptionPane.showInputDialog(null, "Enter the room ID to be removed"));
	String query = "DELETE FROM rooms WHERE room_id=" + roomId;
	runSqlQuery(query);
	JOptionPane.showMessageDialog(null, "Room ID: " + roomId + " succesfully removed!");
    }
    
    // Create method to set text for the available rooms card
    public void setAvailableRoomsCardLabel() {
	int availableRoomsCount = 0;
	
	// Query to retrieve tenant id
	String sql = "SELECT COUNT(room_id) FROM rooms WHERE room_status = 'Available'";
	conn = ConnectXamppMySQL.conn();
	
	try (Statement statement = conn.createStatement()) {
	   ResultSet resultSet = statement.executeQuery(sql);
	    if (resultSet.next()) {
	    availableRoomsCount = resultSet.getInt(1);
	   System.out.println("Available rooms count: " + availableRoomsCount);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
	
	availableRoomsLabel.setText(String.valueOf(availableRoomsCount));
    }
    
    // Create method to set text for the occupied rooms card
    public void setRentedRoomsCardLabel() {
	int rentedRoomsCount = 0;
	
	// Query to retrieve tenant id
	String sql = "SELECT COUNT(room_id) FROM rooms WHERE room_status = 'Rented'";
	conn = ConnectXamppMySQL.conn();
	
	try (Statement statement = conn.createStatement()) {
	   ResultSet resultSet = statement.executeQuery(sql);
	    if (resultSet.next()) {
	    rentedRoomsCount = resultSet.getInt(1);
	    System.out.println("Rented rooms count: " + rentedRoomsCount);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
	
	rentedRoomsLabel.setText(String.valueOf(rentedRoomsCount));
    }
	
    
    // Refresh frame
    public void refreshFrame() {
	repaint();
	revalidate();
    }
    
    
    // Populate the latest transaction table
    public void populateLatestTransactionTable() {
	
	String query = "SELECT payment_id, tenant_id, payment_date, amount FROM payment ORDER BY payment_date DESC";
	try {
            conn = ConnectXamppMySQL.conn();

            // Create the statement
            Statement statement = conn.createStatement();

            // Execute the query and obtain the result set
            ResultSet resultSet = statement.executeQuery(query);

            // Create the DefaultTableModel with column names
            DefaultTableModel model = (DefaultTableModel) latestTransactionTable.getModel();

            // Iterate through the result set and populate the model
            while (resultSet.next()) {
                Object[] rowData = new Object[4]; // Assuming 4 columns
                rowData[0] = resultSet.getObject("payment_id");
                rowData[1] = resultSet.getObject("tenant_id");
                rowData[2] = resultSet.getObject("payment_date");
		rowData[3] = resultSet.getObject("amount");
                model.addRow(rowData);
            }
            
            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
	} catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Populate the latest tenant table
    public void populateLatestTenantTable() {
	
	String query = "SELECT tenant_id, room_id, tenant_first_name, tenant_last_name, registration_date FROM tenants ORDER BY registration_date DESC";
	try {
            conn = ConnectXamppMySQL.conn();

            // Create the statement
            Statement statement = conn.createStatement();

            // Execute the query and obtain the result set
            ResultSet resultSet = statement.executeQuery(query);

            // Create the DefaultTableModel with column names
            DefaultTableModel model = (DefaultTableModel) latestTenantTable.getModel();

            // Iterate through the result set and populate the model
            while (resultSet.next()) {
                Object[] rowData = new Object[5]; // Assuming 5 columns
                rowData[0] = resultSet.getObject("tenant_id");
                rowData[1] = resultSet.getObject("room_id");
                rowData[2] = resultSet.getObject("tenant_first_name");
		rowData[3] = resultSet.getObject("tenant_last_name");
		rowData[4] = resultSet.getObject("registration_date");
                model.addRow(rowData);
            }
            
            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
	} catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    // Populate the registered tenants table
    public void populateRegisteredTenantsTable() {
	
	String query = "SELECT tenant_id, room_id, tenant_first_name, tenant_last_name, tenant_middle_name, contact_number, gender, age, registration_date, rent_status FROM tenants ORDER BY tenant_id ASC";
	try {
            conn = ConnectXamppMySQL.conn();

            // Create the statement
            Statement statement = conn.createStatement();

            // Execute the query and obtain the result set
            ResultSet resultSet = statement.executeQuery(query);

            // Create the DefaultTableModel with column names
            DefaultTableModel model = (DefaultTableModel) tenantInfoTable.getModel();

            // Iterate through the result set and populate the model
            while (resultSet.next()) {
                Object[] rowData = new Object[10]; // Assuming 10 columns
                rowData[0] = resultSet.getObject("tenant_id");
                rowData[1] = resultSet.getObject("room_id");
                rowData[2] = resultSet.getObject("tenant_first_name");
		rowData[3] = resultSet.getObject("tenant_last_name");
		rowData[4] = resultSet.getObject("tenant_middle_name");
		rowData[5] = resultSet.getObject("contact_number");
		rowData[6] = resultSet.getObject("gender");
		rowData[7] = resultSet.getObject("age");
		rowData[8] = resultSet.getObject("registration_date");
		rowData[9] = resultSet.getObject("rent_status");
                model.addRow(rowData);
            }
            
            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
	} catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Populate the added rooms table
    public void popoulateAddedRoomsTable() {
	
	String query = "SELECT room_id, tenant_id, room_type, room_capacity, room_status, date_added FROM rooms ORDER BY room_id ASC";
	try {
            conn = ConnectXamppMySQL.conn();

            // Create the statement
            Statement statement = conn.createStatement();

            // Execute the query and obtain the result set
            ResultSet resultSet = statement.executeQuery(query);

            // Create the DefaultTableModel with column names
            DefaultTableModel model = (DefaultTableModel) roomInfoTable.getModel();

            // Iterate through the result set and populate the model
            while (resultSet.next()) {
                Object[] rowData = new Object[6]; // Assuming 10 columns
                rowData[0] = resultSet.getObject("room_id");
                rowData[1] = resultSet.getObject("tenant_id");
                rowData[2] = resultSet.getObject("room_type");
		rowData[3] = resultSet.getObject("room_capacity");
		rowData[4] = resultSet.getObject("room_status");
		rowData[5] = resultSet.getObject("date_added");
                model.addRow(rowData);
            }
            
            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
	} catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Populate the transaction history table
    public void populateTransactionHistoryTable() {
	
	String query = "SELECT payment_id, tenant_id, room_id, payment_type, month_contract, payment_date FROM payment ORDER BY payment_id ASC";
	try {
            conn = ConnectXamppMySQL.conn();

            // Create the statement
            Statement statement = conn.createStatement();

            // Execute the query and obtain the result set
            ResultSet resultSet = statement.executeQuery(query);

            // Create the DefaultTableModel with column names
            DefaultTableModel model = (DefaultTableModel) transactionHistoryTable.getModel();

            // Iterate through the result set and populate the model
            while (resultSet.next()) {
                Object[] rowData = new Object[6]; // Assuming 10 columns
                rowData[0] = resultSet.getObject("payment_id");
                rowData[1] = resultSet.getObject("tenant_id");
                rowData[2] = resultSet.getObject("room_id");
		rowData[3] = resultSet.getObject("payment_type");
		rowData[4] = resultSet.getObject("month_contract");
		rowData[5] = resultSet.getObject("payment_date");
                model.addRow(rowData);
            }
            
            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
	} catch (SQLException e) {
            e.printStackTrace();
        }
	
    }
    
    // Confirms if user input is matched with system credentials
    public boolean verifyInput(int userId, int userPin) {
	String systemId, systemPin;
	int configId;
	conn = ConnectXamppMySQL.conn();
	
	// Query to retrieve tenant id
	String query = "SELECT config_id FROM system_configuration WHERE system_id = ? AND system_pin = ?";
	try (PreparedStatement statement = conn.prepareStatement(query)) {
	    statement.setInt(1, userId);
	    statement.setInt(2, userPin);
        
	    ResultSet resultSet = statement.executeQuery();
	    if (resultSet.next()) {
		configId = resultSet.getInt("config_id");
		JOptionPane.showMessageDialog(null, "Verification success!");
		return true;
	    } else {
		JOptionPane.showMessageDialog(null, "Invalid system ID or PIN","Account not Found", JOptionPane.WARNING_MESSAGE);
	    }
	    } catch (SQLException e) {
		e.printStackTrace();
	}
	return false;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebarPanel = new javax.swing.JPanel();
        dashboardButton = new javax.swing.JButton();
        viewTenantButton = new javax.swing.JButton();
        viewRoomButton = new javax.swing.JButton();
        incomeReportButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();
        titlebarPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        dashboardPanel = new javax.swing.JPanel();
        cardsPanel = new javax.swing.JPanel();
        tenantsCard = new javax.swing.JPanel();
        tenantIcon = new javax.swing.JLabel();
        tenantCountLabel = new javax.swing.JLabel();
        tenantSubLabel = new javax.swing.JLabel();
        roomsCard = new javax.swing.JPanel();
        roomIcon = new javax.swing.JLabel();
        availableRoomsLabel = new javax.swing.JLabel();
        roomSubLabel = new javax.swing.JLabel();
        occupiedRooms = new javax.swing.JPanel();
        rentedRoomsCountLabel = new javax.swing.JLabel();
        rentedRoomsLabel = new javax.swing.JLabel();
        rentedRoomsIcon = new javax.swing.JLabel();
        earningsCard = new javax.swing.JPanel();
        earningsSubLabel = new javax.swing.JLabel();
        earningsCount = new javax.swing.JLabel();
        earningsIcon = new javax.swing.JLabel();
        tablesPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        latestTransactionTable = new javax.swing.JTable();
        latestTenantLabel = new javax.swing.JLabel();
        latestTransacationLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        latestTenantTable = new javax.swing.JTable();
        dashboardLabel = new javax.swing.JLabel();
        viewRegisteredTenantsPanel = new javax.swing.JPanel();
        registeredTenantsLabel = new javax.swing.JLabel();
        tenantInfoPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tenantInfoTable = new javax.swing.JTable();
        removeTenantButton = new javax.swing.JButton();
        registerTenantButton = new javax.swing.JButton();
        orderByBox = new javax.swing.JComboBox<>();
        orderByLabel = new javax.swing.JLabel();
        sortByBox = new javax.swing.JComboBox<>();
        sortByLabel = new javax.swing.JLabel();
        viewAddedRoomsPanel = new javax.swing.JPanel();
        tenantInfoPanel1 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        roomInfoTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        roomPriceInput = new javax.swing.JTextField();
        roomPriceLabel = new javax.swing.JLabel();
        roomCapacityLabel = new javax.swing.JLabel();
        addRoomButton = new javax.swing.JButton();
        removeRoomButton = new javax.swing.JButton();
        roomTypeLabel = new javax.swing.JLabel();
        roomTypeBox = new javax.swing.JComboBox<>();
        roomCapacityBox = new javax.swing.JComboBox<>();
        roomIdInput = new javax.swing.JTextField();
        orderByBox1 = new javax.swing.JComboBox<>();
        orderByLabel1 = new javax.swing.JLabel();
        sortByBox1 = new javax.swing.JComboBox<>();
        sortByLabel1 = new javax.swing.JLabel();
        registeredTenantsLabel2 = new javax.swing.JLabel();
        incomeReportPanel = new javax.swing.JPanel();
        transactionHistoryLabel = new javax.swing.JLabel();
        incomeReportCardsPanel = new javax.swing.JPanel();
        grossIncomeCard = new javax.swing.JPanel();
        grossIncomeIcon = new javax.swing.JLabel();
        grossIncomeLabel = new javax.swing.JLabel();
        grossIncomeSubLabel = new javax.swing.JLabel();
        expensesCard = new javax.swing.JPanel();
        expensesLabel = new javax.swing.JLabel();
        expensesSubLabel = new javax.swing.JLabel();
        expensesIcon = new javax.swing.JLabel();
        netIncomeCard = new javax.swing.JPanel();
        netIncomeLabel = new javax.swing.JLabel();
        netIncomeSubLabel = new javax.swing.JLabel();
        netIncomeIcon = new javax.swing.JLabel();
        incomeReportLabel = new javax.swing.JLabel();
        transactionHistoryPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        transactionHistoryTable = new javax.swing.JTable();
        clearExpensesButton = new javax.swing.JButton();
        addExpensesButton = new javax.swing.JButton();
        rentEzIcon = new javax.swing.JLabel();
        settingsPanel = new javax.swing.JPanel();
        settingsLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        changeSystemPinButton = new javax.swing.JButton();
        resetDatabaseButton = new javax.swing.JButton();
        changeSystemIdButton = new javax.swing.JButton();
        resetDatabaseLabel = new javax.swing.JLabel();
        changeSystemIdLabel = new javax.swing.JLabel();
        changeSystemPinLabel = new javax.swing.JLabel();
        bkup = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        sidebarPanel.setBackground(new java.awt.Color(52, 68, 77));

        dashboardButton.setBackground(new java.awt.Color(52, 68, 77));
        dashboardButton.setFont(new java.awt.Font("Archivo SemiBold", 0, 18)); // NOI18N
        dashboardButton.setForeground(new java.awt.Color(175, 190, 203));
        dashboardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/sidebar_icons/dashboard_icon.png"))); // NOI18N
        dashboardButton.setText("Dashboard");
        dashboardButton.setBorderPainted(false);
        dashboardButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        dashboardButton.setFocusable(false);
        dashboardButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dashboardButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        dashboardButton.setIconTextGap(10);
        dashboardButton.setInheritsPopupMenu(true);
        dashboardButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashboardButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dashboardButtonMouseExited(evt);
            }
        });
        dashboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardButtonActionPerformed(evt);
            }
        });

        viewTenantButton.setBackground(new java.awt.Color(52, 68, 77));
        viewTenantButton.setFont(new java.awt.Font("Archivo SemiBold", 0, 18)); // NOI18N
        viewTenantButton.setForeground(new java.awt.Color(175, 190, 203));
        viewTenantButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/sidebar_icons/view_tenants_icon.png"))); // NOI18N
        viewTenantButton.setText("View Registered Tenants");
        viewTenantButton.setToolTipText("");
        viewTenantButton.setBorderPainted(false);
        viewTenantButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        viewTenantButton.setFocusable(false);
        viewTenantButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        viewTenantButton.setIconTextGap(10);
        viewTenantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewTenantButtonActionPerformed(evt);
            }
        });

        viewRoomButton.setBackground(new java.awt.Color(52, 68, 77));
        viewRoomButton.setFont(new java.awt.Font("Archivo SemiBold", 0, 18)); // NOI18N
        viewRoomButton.setForeground(new java.awt.Color(175, 190, 203));
        viewRoomButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/sidebar_icons/view_rooms_icon.png"))); // NOI18N
        viewRoomButton.setText("View Added Rooms");
        viewRoomButton.setBorderPainted(false);
        viewRoomButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        viewRoomButton.setFocusable(false);
        viewRoomButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        viewRoomButton.setIconTextGap(10);
        viewRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewRoomButtonActionPerformed(evt);
            }
        });

        incomeReportButton.setBackground(new java.awt.Color(52, 68, 77));
        incomeReportButton.setFont(new java.awt.Font("Archivo SemiBold", 0, 18)); // NOI18N
        incomeReportButton.setForeground(new java.awt.Color(175, 190, 203));
        incomeReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/sidebar_icons/money_icon.png"))); // NOI18N
        incomeReportButton.setText("Income Report");
        incomeReportButton.setBorderPainted(false);
        incomeReportButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        incomeReportButton.setFocusable(false);
        incomeReportButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        incomeReportButton.setIconTextGap(10);
        incomeReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incomeReportButtonActionPerformed(evt);
            }
        });

        settingsButton.setBackground(new java.awt.Color(52, 68, 77));
        settingsButton.setFont(new java.awt.Font("Archivo SemiBold", 0, 18)); // NOI18N
        settingsButton.setForeground(new java.awt.Color(175, 190, 203));
        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/sidebar_icons/settings_icon.png"))); // NOI18N
        settingsButton.setText("Settings");
        settingsButton.setBorderPainted(false);
        settingsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        settingsButton.setFocusable(false);
        settingsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        settingsButton.setIconTextGap(10);
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sidebarPanelLayout = new javax.swing.GroupLayout(sidebarPanel);
        sidebarPanel.setLayout(sidebarPanelLayout);
        sidebarPanelLayout.setHorizontalGroup(
            sidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(sidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewTenantButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(viewRoomButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(incomeReportButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(settingsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
            .addGroup(sidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sidebarPanelLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(dashboardButton, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        sidebarPanelLayout.setVerticalGroup(
            sidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarPanelLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(viewTenantButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(viewRoomButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(incomeReportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(sidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sidebarPanelLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(dashboardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 579, Short.MAX_VALUE)))
        );

        titlebarPanel.setBackground(new java.awt.Color(239, 134, 128));
        titlebarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Poppins Black", 0, 40)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/other_icons/keys_icon.png"))); // NOI18N
        jLabel5.setText("RentEZ");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel5.setIconTextGap(3);
        titlebarPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 9, 320, 60));

        dateLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 18)); // NOI18N
        dateLabel.setForeground(new java.awt.Color(255, 255, 255));
        dateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dateLabel.setText("00/00/00");
        dateLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        titlebarPanel.add(dateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 40, 150, 30));

        timeLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 24)); // NOI18N
        timeLabel.setForeground(new java.awt.Color(255, 255, 255));
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeLabel.setText("0:0:0 PM/AM");
        timeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        titlebarPanel.add(timeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 10, 150, 30));

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/other_icons/refresh_icon.png"))); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setFocusable(false);
        titlebarPanel.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 13, 90, 50));

        contentPanel.setBackground(new java.awt.Color(184, 208, 201));
        contentPanel.setLayout(new java.awt.CardLayout());

        cardsPanel.setLayout(new java.awt.GridLayout(1, 0, 15, 0));

        tenantsCard.setBackground(new java.awt.Color(0, 193, 234));
        tenantsCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tenantIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/cards_icons/people_icon.png"))); // NOI18N
        tenantsCard.add(tenantIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 120, 100));

        tenantCountLabel.setFont(new java.awt.Font("Poppins Black", 0, 42)); // NOI18N
        tenantCountLabel.setForeground(new java.awt.Color(255, 255, 255));
        tenantCountLabel.setText("999");
        tenantsCard.add(tenantCountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 50));

        tenantSubLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 18)); // NOI18N
        tenantSubLabel.setForeground(new java.awt.Color(255, 255, 255));
        tenantSubLabel.setText("<html>Registered<br>Tenants</html>");
        tenantsCard.add(tenantSubLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 60));

        cardsPanel.add(tenantsCard);

        roomsCard.setBackground(new java.awt.Color(246, 154, 57));
        roomsCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        roomIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/cards_icons/available_rooms_icon.png"))); // NOI18N
        roomsCard.add(roomIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 130, 140));

        availableRoomsLabel.setFont(new java.awt.Font("Poppins Black", 0, 42)); // NOI18N
        availableRoomsLabel.setForeground(new java.awt.Color(255, 255, 255));
        availableRoomsLabel.setText("999");
        roomsCard.add(availableRoomsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 50));

        roomSubLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 18)); // NOI18N
        roomSubLabel.setForeground(new java.awt.Color(255, 255, 255));
        roomSubLabel.setText("<html>Available<br>Rooms</html>");
        roomsCard.add(roomSubLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 60));

        cardsPanel.add(roomsCard);

        occupiedRooms.setBackground(new java.awt.Color(225, 73, 63));
        occupiedRooms.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rentedRoomsCountLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 18)); // NOI18N
        rentedRoomsCountLabel.setForeground(new java.awt.Color(255, 255, 255));
        rentedRoomsCountLabel.setText("<html>Rented<br>Rooms</html>");
        occupiedRooms.add(rentedRoomsCountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 60));

        rentedRoomsLabel.setFont(new java.awt.Font("Poppins Black", 0, 42)); // NOI18N
        rentedRoomsLabel.setForeground(new java.awt.Color(255, 255, 255));
        rentedRoomsLabel.setText("999");
        occupiedRooms.add(rentedRoomsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 50));

        rentedRoomsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/cards_icons/occupied_rooms_icon.png"))); // NOI18N
        occupiedRooms.add(rentedRoomsIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 130, 140));

        cardsPanel.add(occupiedRooms);

        earningsCard.setBackground(new java.awt.Color(0, 166, 99));
        earningsCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        earningsSubLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 18)); // NOI18N
        earningsSubLabel.setForeground(new java.awt.Color(255, 255, 255));
        earningsSubLabel.setText("<html>Monthly<br>Earnings</html>");
        earningsCard.add(earningsSubLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 60));

        earningsCount.setFont(new java.awt.Font("Poppins Black", 0, 42)); // NOI18N
        earningsCount.setForeground(new java.awt.Color(255, 255, 255));
        earningsCount.setText("999");
        earningsCard.add(earningsCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 50));

        earningsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/cards_icons/monthly_earnings_icon.png"))); // NOI18N
        earningsCard.add(earningsIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 120, 100));

        cardsPanel.add(earningsCard);

        tablesPanel.setBackground(new java.awt.Color(255, 255, 255));
        tablesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        latestTransactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Payment ID", "Tenant ID", "Payment Date", "Ammount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(latestTransactionTable);
        if (latestTransactionTable.getColumnModel().getColumnCount() > 0) {
            latestTransactionTable.getColumnModel().getColumn(0).setResizable(false);
            latestTransactionTable.getColumnModel().getColumn(1).setResizable(false);
            latestTransactionTable.getColumnModel().getColumn(2).setResizable(false);
            latestTransactionTable.getColumnModel().getColumn(3).setResizable(false);
        }

        tablesPanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 470, 290));

        latestTenantLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 34)); // NOI18N
        latestTenantLabel.setText("Latest Tenant");
        tablesPanel.add(latestTenantLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 500, 50));

        latestTransacationLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 34)); // NOI18N
        latestTransacationLabel.setText("Latest Transaction");
        tablesPanel.add(latestTransacationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 470, 50));

        latestTenantTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tenant ID", "Room ID", "First Name", "Last Name", "Registration Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(latestTenantTable);
        if (latestTenantTable.getColumnModel().getColumnCount() > 0) {
            latestTenantTable.getColumnModel().getColumn(0).setMinWidth(60);
            latestTenantTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            latestTenantTable.getColumnModel().getColumn(0).setMaxWidth(60);
            latestTenantTable.getColumnModel().getColumn(1).setMinWidth(60);
            latestTenantTable.getColumnModel().getColumn(1).setPreferredWidth(60);
            latestTenantTable.getColumnModel().getColumn(1).setMaxWidth(60);
            latestTenantTable.getColumnModel().getColumn(2).setMinWidth(90);
            latestTenantTable.getColumnModel().getColumn(2).setPreferredWidth(90);
            latestTenantTable.getColumnModel().getColumn(2).setMaxWidth(90);
            latestTenantTable.getColumnModel().getColumn(3).setMinWidth(90);
            latestTenantTable.getColumnModel().getColumn(3).setPreferredWidth(90);
            latestTenantTable.getColumnModel().getColumn(3).setMaxWidth(90);
        }

        tablesPanel.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 60, 470, 290));

        dashboardLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 24)); // NOI18N
        dashboardLabel.setText("Dashboard");

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dashboardLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cardsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 995, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tablesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(dashboardLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(cardsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tablesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        contentPanel.add(dashboardPanel, "card7");

        registeredTenantsLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 24)); // NOI18N
        registeredTenantsLabel.setForeground(new java.awt.Color(51, 51, 51));
        registeredTenantsLabel.setText("View Registered Tenants");

        tenantInfoPanel.setBackground(new java.awt.Color(255, 255, 255));
        tenantInfoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tenantInfoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tenant ID", "Room ID", "First Name", "Last Name", "Middle Name", "Contact Number", "Gender", "Age", "Registration Date", "Status"
            }
        ));
        jScrollPane5.setViewportView(tenantInfoTable);

        tenantInfoPanel.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 930, 420));

        removeTenantButton.setBackground(new java.awt.Color(255, 255, 254));
        removeTenantButton.setText("Remove Tenant");
        removeTenantButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        removeTenantButton.setFocusable(false);
        removeTenantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeTenantButtonActionPerformed(evt);
            }
        });

        registerTenantButton.setBackground(new java.awt.Color(255, 255, 254));
        registerTenantButton.setText("Register Tenant");
        registerTenantButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registerTenantButton.setFocusable(false);
        registerTenantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerTenantButtonActionPerformed(evt);
            }
        });

        orderByBox.setBackground(new java.awt.Color(255, 255, 254));
        orderByBox.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        orderByBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ascending", "Descending" }));
        orderByBox.setFocusable(false);
        orderByBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderByBoxActionPerformed(evt);
            }
        });

        orderByLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        orderByLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/other_icons/order_by_icon.png"))); // NOI18N
        orderByLabel.setText("Order by");
        orderByLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        sortByBox.setBackground(new java.awt.Color(255, 255, 254));
        sortByBox.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        sortByBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tenant ID", "First Name", "Last Name", "Due Date", "Status" }));
        sortByBox.setFocusable(false);
        sortByBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortByBoxActionPerformed(evt);
            }
        });

        sortByLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        sortByLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/other_icons/sort_icon.png"))); // NOI18N
        sortByLabel.setText("Sort by");
        sortByLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout viewRegisteredTenantsPanelLayout = new javax.swing.GroupLayout(viewRegisteredTenantsPanel);
        viewRegisteredTenantsPanel.setLayout(viewRegisteredTenantsPanelLayout);
        viewRegisteredTenantsPanelLayout.setHorizontalGroup(
            viewRegisteredTenantsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewRegisteredTenantsPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(viewRegisteredTenantsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(viewRegisteredTenantsPanelLayout.createSequentialGroup()
                        .addComponent(registerTenantButton, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeTenantButton, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(viewRegisteredTenantsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(viewRegisteredTenantsPanelLayout.createSequentialGroup()
                            .addComponent(registeredTenantsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sortByLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(sortByBox, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(orderByLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(orderByBox, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(tenantInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 989, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        viewRegisteredTenantsPanelLayout.setVerticalGroup(
            viewRegisteredTenantsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewRegisteredTenantsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(viewRegisteredTenantsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(viewRegisteredTenantsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sortByBox, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sortByLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(viewRegisteredTenantsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(registeredTenantsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(orderByBox, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(orderByLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tenantInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(viewRegisteredTenantsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeTenantButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(registerTenantButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        contentPanel.add(viewRegisteredTenantsPanel, "card2");

        tenantInfoPanel1.setBackground(new java.awt.Color(255, 255, 255));
        tenantInfoPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        roomInfoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Room ID", "Tenant ID", "Room Type", "Room Capacity", "Room Status", "Date Added"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(roomInfoTable);

        tenantInfoPanel1.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 700, 510));

        jLabel1.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        jLabel1.setText("Room ID");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tenantInfoPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 30, 100, 30));
        tenantInfoPanel1.add(roomPriceInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 150, 220, 40));

        roomPriceLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        roomPriceLabel.setText("Room Price");
        roomPriceLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tenantInfoPanel1.add(roomPriceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 120, 100, 30));

        roomCapacityLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        roomCapacityLabel.setText("Room Capacity");
        roomCapacityLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tenantInfoPanel1.add(roomCapacityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 220, 120, 30));

        addRoomButton.setBackground(new java.awt.Color(255, 255, 254));
        addRoomButton.setText("Add Room");
        addRoomButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addRoomButton.setFocusable(false);
        addRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRoomButtonActionPerformed(evt);
            }
        });
        tenantInfoPanel1.add(addRoomButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 420, 220, 50));

        removeRoomButton.setBackground(new java.awt.Color(255, 255, 254));
        removeRoomButton.setText("Remove Room");
        removeRoomButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        removeRoomButton.setFocusable(false);
        removeRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRoomButtonActionPerformed(evt);
            }
        });
        tenantInfoPanel1.add(removeRoomButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 480, 220, 50));

        roomTypeLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 15)); // NOI18N
        roomTypeLabel.setText("Room Type");
        roomTypeLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tenantInfoPanel1.add(roomTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 320, 120, 30));

        roomTypeBox.setBackground(new java.awt.Color(255, 255, 254));
        roomTypeBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        roomTypeBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Bedroom", "2 Bedroom" }));
        roomTypeBox.setFocusable(false);
        tenantInfoPanel1.add(roomTypeBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 350, 220, 40));

        roomCapacityBox.setBackground(new java.awt.Color(255, 255, 254));
        roomCapacityBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        roomCapacityBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        roomCapacityBox.setFocusable(false);
        tenantInfoPanel1.add(roomCapacityBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 250, 220, 40));
        tenantInfoPanel1.add(roomIdInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 60, 220, 40));

        orderByBox1.setBackground(new java.awt.Color(255, 255, 254));
        orderByBox1.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        orderByBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ascending", "Descending" }));
        orderByBox1.setFocusable(false);
        orderByBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderByBox1ActionPerformed(evt);
            }
        });

        orderByLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        orderByLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/other_icons/order_by_icon.png"))); // NOI18N
        orderByLabel1.setText("Order by");
        orderByLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        sortByBox1.setBackground(new java.awt.Color(255, 255, 254));
        sortByBox1.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        sortByBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Room ID", "Date Added", "Room Status", "Room Type" }));
        sortByBox1.setFocusable(false);
        sortByBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortByBox1ActionPerformed(evt);
            }
        });

        sortByLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        sortByLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/other_icons/sort_icon.png"))); // NOI18N
        sortByLabel1.setText("Sort by");
        sortByLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        registeredTenantsLabel2.setFont(new java.awt.Font("Archivo SemiBold", 0, 24)); // NOI18N
        registeredTenantsLabel2.setForeground(new java.awt.Color(51, 51, 51));
        registeredTenantsLabel2.setText("View Added Rooms");

        javax.swing.GroupLayout viewAddedRoomsPanelLayout = new javax.swing.GroupLayout(viewAddedRoomsPanel);
        viewAddedRoomsPanel.setLayout(viewAddedRoomsPanelLayout);
        viewAddedRoomsPanelLayout.setHorizontalGroup(
            viewAddedRoomsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewAddedRoomsPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(viewAddedRoomsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(viewAddedRoomsPanelLayout.createSequentialGroup()
                        .addComponent(registeredTenantsLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sortByLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortByBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(orderByLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(orderByBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tenantInfoPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 989, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        viewAddedRoomsPanelLayout.setVerticalGroup(
            viewAddedRoomsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewAddedRoomsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(viewAddedRoomsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(viewAddedRoomsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(orderByBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(orderByLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sortByBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sortByLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(registeredTenantsLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tenantInfoPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        contentPanel.add(viewAddedRoomsPanel, "card2");

        transactionHistoryLabel.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        transactionHistoryLabel.setText("Transaction History");

        incomeReportCardsPanel.setBackground(new java.awt.Color(255, 255, 255));
        incomeReportCardsPanel.setLayout(new java.awt.GridLayout(1, 0, 15, 0));

        grossIncomeCard.setBackground(new java.awt.Color(8, 96, 153));
        grossIncomeCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        grossIncomeIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/cards_icons/gross_icon_darker.png"))); // NOI18N
        grossIncomeCard.add(grossIncomeIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 130, 110));

        grossIncomeLabel.setFont(new java.awt.Font("Poppins Black", 0, 30)); // NOI18N
        grossIncomeLabel.setForeground(new java.awt.Color(255, 255, 255));
        grossIncomeLabel.setText("999");
        grossIncomeLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        grossIncomeCard.add(grossIncomeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 210, 50));

        grossIncomeSubLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 20)); // NOI18N
        grossIncomeSubLabel.setForeground(new java.awt.Color(255, 255, 255));
        grossIncomeSubLabel.setText("<html>Gross<br>Income</br>");
        grossIncomeCard.add(grossIncomeSubLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 170, 70));

        incomeReportCardsPanel.add(grossIncomeCard);

        expensesCard.setBackground(new java.awt.Color(201, 36, 57));
        expensesCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expensesLabel.setFont(new java.awt.Font("Poppins Black", 0, 30)); // NOI18N
        expensesLabel.setForeground(new java.awt.Color(255, 255, 255));
        expensesLabel.setText("999");
        expensesLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        expensesCard.add(expensesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 210, 50));

        expensesSubLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 20)); // NOI18N
        expensesSubLabel.setForeground(new java.awt.Color(255, 255, 255));
        expensesSubLabel.setText("<html>Expenses<br></br>");
        expensesCard.add(expensesSubLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 170, 70));

        expensesIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/cards_icons/expenses_icon.png"))); // NOI18N
        expensesCard.add(expensesIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 130, 110));

        incomeReportCardsPanel.add(expensesCard);

        netIncomeCard.setBackground(new java.awt.Color(0, 166, 99));
        netIncomeCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        netIncomeLabel.setFont(new java.awt.Font("Poppins Black", 0, 30)); // NOI18N
        netIncomeLabel.setForeground(new java.awt.Color(255, 255, 255));
        netIncomeLabel.setText("999");
        netIncomeLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        netIncomeCard.add(netIncomeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 210, 50));

        netIncomeSubLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 20)); // NOI18N
        netIncomeSubLabel.setForeground(new java.awt.Color(255, 255, 255));
        netIncomeSubLabel.setText("<html>Net<br>Income</br>");
        netIncomeCard.add(netIncomeSubLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 170, 70));

        netIncomeIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Icons/cards_icons/net_icon.png"))); // NOI18N
        netIncomeCard.add(netIncomeIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 130, 110));

        incomeReportCardsPanel.add(netIncomeCard);

        incomeReportLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 24)); // NOI18N
        incomeReportLabel.setText("Income Report");

        transactionHistoryPanel.setBackground(new java.awt.Color(255, 255, 255));
        transactionHistoryPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        transactionHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Payment ID", "Tenant ID", "Room ID", "Payment Type", "Month Contract", "Payment Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(transactionHistoryTable);

        transactionHistoryPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 770, 280));

        clearExpensesButton.setBackground(new java.awt.Color(255, 255, 254));
        clearExpensesButton.setText("Clear Expenses");
        clearExpensesButton.setFocusable(false);
        clearExpensesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearExpensesButtonActionPerformed(evt);
            }
        });

        addExpensesButton.setBackground(new java.awt.Color(255, 255, 254));
        addExpensesButton.setText("Add Expenses");
        addExpensesButton.setFocusable(false);
        addExpensesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addExpensesButtonActionPerformed(evt);
            }
        });

        rentEzIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Profiles/logo_shadow.png"))); // NOI18N
        rentEzIcon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout incomeReportPanelLayout = new javax.swing.GroupLayout(incomeReportPanel);
        incomeReportPanel.setLayout(incomeReportPanelLayout);
        incomeReportPanelLayout.setHorizontalGroup(
            incomeReportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, incomeReportPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(incomeReportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(incomeReportLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(incomeReportCardsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 996, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(incomeReportPanelLayout.createSequentialGroup()
                        .addComponent(transactionHistoryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(incomeReportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(incomeReportPanelLayout.createSequentialGroup()
                                .addComponent(rentEzIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(clearExpensesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addExpensesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(transactionHistoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );
        incomeReportPanelLayout.setVerticalGroup(
            incomeReportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(incomeReportPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(incomeReportLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(incomeReportCardsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(transactionHistoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(incomeReportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(incomeReportPanelLayout.createSequentialGroup()
                        .addComponent(rentEzIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(addExpensesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clearExpensesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(transactionHistoryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        contentPanel.add(incomeReportPanel, "card6");

        settingsLabel.setBackground(new java.awt.Color(51, 51, 51));
        settingsLabel.setFont(new java.awt.Font("Archivo SemiBold", 0, 24)); // NOI18N
        settingsLabel.setText("Settings");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        changeSystemPinButton.setBackground(new java.awt.Color(255, 255, 254));
        changeSystemPinButton.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        changeSystemPinButton.setText("System PIN");
        changeSystemPinButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        changeSystemPinButton.setFocusable(false);
        changeSystemPinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSystemPinButtonActionPerformed(evt);
            }
        });
        jPanel1.add(changeSystemPinButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 130, 390, 80));

        resetDatabaseButton.setBackground(new java.awt.Color(204, 0, 0));
        resetDatabaseButton.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        resetDatabaseButton.setForeground(new java.awt.Color(255, 255, 255));
        resetDatabaseButton.setText("RESET DATABASE");
        resetDatabaseButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        resetDatabaseButton.setFocusable(false);
        resetDatabaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetDatabaseButtonActionPerformed(evt);
            }
        });
        jPanel1.add(resetDatabaseButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 410, 390, 80));

        changeSystemIdButton.setBackground(new java.awt.Color(255, 255, 254));
        changeSystemIdButton.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        changeSystemIdButton.setText("System ID");
        changeSystemIdButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        changeSystemIdButton.setFocusable(false);
        changeSystemIdButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSystemIdButtonActionPerformed(evt);
            }
        });
        jPanel1.add(changeSystemIdButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 390, 80));

        resetDatabaseLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        resetDatabaseLabel.setForeground(new java.awt.Color(51, 51, 51));
        resetDatabaseLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        resetDatabaseLabel.setText("Reset database");
        jPanel1.add(resetDatabaseLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 360, 390, 50));

        changeSystemIdLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        changeSystemIdLabel.setForeground(new java.awt.Color(51, 51, 51));
        changeSystemIdLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        changeSystemIdLabel.setText("Change System Id");
        jPanel1.add(changeSystemIdLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 390, 50));

        changeSystemPinLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        changeSystemPinLabel.setForeground(new java.awt.Color(51, 51, 51));
        changeSystemPinLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        changeSystemPinLabel.setText("Change System PIN");
        jPanel1.add(changeSystemPinLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 390, 50));

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(settingsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 931, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(settingsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        contentPanel.add(settingsPanel, "card4");

        bkup.setBackground(new java.awt.Color(190, 201, 203));
        bkup.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        contentPanel.add(bkup, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(titlebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidebarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(titlebarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sidebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardButtonActionPerformed
        // TODO add your handling code here:
	hideContentPanels();
	dashboardPanel.setVisible(true);
	refreshFrame();
	
    }//GEN-LAST:event_dashboardButtonActionPerformed

    private void viewTenantButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewTenantButtonActionPerformed
        // TODO add your handling code here:
	hideContentPanels();
	viewRegisteredTenantsPanel.setVisible(true);
	refreshFrame();
    }//GEN-LAST:event_viewTenantButtonActionPerformed

    private void viewRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewRoomButtonActionPerformed
        // TODO add your handling code here:
	hideContentPanels();
	viewAddedRoomsPanel.setVisible(true);
	refreshFrame();
    }//GEN-LAST:event_viewRoomButtonActionPerformed

    private void incomeReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incomeReportButtonActionPerformed
        // TODO add your handling code here:
	hideContentPanels();
	incomeReportPanel.setVisible(true);
	refreshFrame();
    }//GEN-LAST:event_incomeReportButtonActionPerformed

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        // TODO add your handling code here:
	hideContentPanels();
	settingsPanel.setVisible(true);
	refreshFrame();
    }//GEN-LAST:event_settingsButtonActionPerformed

    private void dashboardButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardButtonMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_dashboardButtonMouseEntered

    private void dashboardButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardButtonMouseExited

    }//GEN-LAST:event_dashboardButtonMouseExited

    private void addExpensesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addExpensesButtonActionPerformed
        // TODO add your handling code here:
	new AddExpensePage().setVisible(true);
    }//GEN-LAST:event_addExpensesButtonActionPerformed

    private void removeTenantButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeTenantButtonActionPerformed
	new Tenant().removeTenant();

    }//GEN-LAST:event_removeTenantButtonActionPerformed

    private void registerTenantButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerTenantButtonActionPerformed
        // TODO add your handling code here:
	new Tenant().setVisible(true);
	setRentedRoomsCardLabel();
	setAvailableRoomsCardLabel();
    }//GEN-LAST:event_registerTenantButtonActionPerformed

    private void orderByBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderByBoxActionPerformed
        // TODO add your handling code here:
	String selectedValue = (String) orderByBox.getSelectedItem();
	System.out.print(selectedValue);
    }//GEN-LAST:event_orderByBoxActionPerformed

    private void sortByBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sortByBoxActionPerformed

    private void addRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRoomButtonActionPerformed
        // TODO add your handling code here:
	addRoom();
	setAvailableRoomsCardLabel();
	setRentedRoomsCardLabel();
    }//GEN-LAST:event_addRoomButtonActionPerformed

    private void orderByBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderByBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_orderByBox1ActionPerformed

    private void sortByBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sortByBox1ActionPerformed

    private void removeRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRoomButtonActionPerformed
        // TODO add your handling code here:
	removeRoom();
	setAvailableRoomsCardLabel();
	setRentedRoomsCardLabel();
    }//GEN-LAST:event_removeRoomButtonActionPerformed

    private void dashboardButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_dashboardButtonMouseClicked

    private void changeSystemIdButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSystemIdButtonActionPerformed
        // TODO add your handling code here:
	String newSystemId = JOptionPane.showInputDialog(null, "Please enter the new System ID");
	int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to change the System ID?");
	if(confirmation == JOptionPane.YES_OPTION) {
	    String query = "UPDATE system_configuration SET system_id = '" + newSystemId + "' WHERE config_id = 1";
	    new Main().runSqlQuery(query);
	    JOptionPane.showMessageDialog(null, "System ID changed succesfully!");
	}
    }//GEN-LAST:event_changeSystemIdButtonActionPerformed

    private void changeSystemPinButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSystemPinButtonActionPerformed
        // TODO add your handling code here:
	int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to change the System PIN?");
	if(confirmation == JOptionPane.YES_OPTION) {
	    String newSystemPin = JOptionPane.showInputDialog(null, "Please enter the new System PIN");
	    String query = "UPDATE system_configuration SET system_pin = '" + newSystemPin + "' WHERE config_id = 1";
	    new Main().runSqlQuery(query);
	    JOptionPane.showMessageDialog(null, "System PIN changed succesfully!");
	}
    }//GEN-LAST:event_changeSystemPinButtonActionPerformed

    private void clearExpensesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearExpensesButtonActionPerformed
        // TODO add your handling code here:
	String query = "UPDATE system_configuration SET expenses = 0 WHERE config_id = 1";
	new Main().runSqlQuery(query);
	JOptionPane.showMessageDialog(null, "Expenses succesfully cleared!");
    }//GEN-LAST:event_clearExpensesButtonActionPerformed

    private void resetDatabaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetDatabaseButtonActionPerformed
        // TODO add your handling code here:
	int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to RESET THE DATABASE?");
	if(confirmation == JOptionPane.YES_OPTION) {
	    int userId = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter the System ID"));
	    int userPin = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter the System PIN"));
	    if(verifyInput(userId, userPin)) {
		JOptionPane.showMessageDialog(null, "System database will now reset!");
//		new Main().runSqlQuery("TRUNCATE TABLE tenants");
//		new Main().runSqlQuery("TRUNCATE TABLE rooms");
//		new Main().runSqlQuery("TRUNCATE TABLE payment");
	    }
	}
    }//GEN-LAST:event_resetDatabaseButtonActionPerformed

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
		new HomePage().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addExpensesButton;
    private javax.swing.JButton addRoomButton;
    private javax.swing.JLabel availableRoomsLabel;
    private javax.swing.JPanel bkup;
    private javax.swing.JPanel cardsPanel;
    private javax.swing.JButton changeSystemIdButton;
    private javax.swing.JLabel changeSystemIdLabel;
    private javax.swing.JButton changeSystemPinButton;
    private javax.swing.JLabel changeSystemPinLabel;
    private javax.swing.JButton clearExpensesButton;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton dashboardButton;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JPanel earningsCard;
    private javax.swing.JLabel earningsCount;
    private javax.swing.JLabel earningsIcon;
    private javax.swing.JLabel earningsSubLabel;
    private javax.swing.JPanel expensesCard;
    private javax.swing.JLabel expensesIcon;
    private javax.swing.JLabel expensesLabel;
    private javax.swing.JLabel expensesSubLabel;
    private javax.swing.JPanel grossIncomeCard;
    private javax.swing.JLabel grossIncomeIcon;
    private javax.swing.JLabel grossIncomeLabel;
    private javax.swing.JLabel grossIncomeSubLabel;
    private javax.swing.JButton incomeReportButton;
    private javax.swing.JPanel incomeReportCardsPanel;
    private javax.swing.JLabel incomeReportLabel;
    private javax.swing.JPanel incomeReportPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel latestTenantLabel;
    private javax.swing.JTable latestTenantTable;
    private javax.swing.JLabel latestTransacationLabel;
    private javax.swing.JTable latestTransactionTable;
    private javax.swing.JPanel netIncomeCard;
    private javax.swing.JLabel netIncomeIcon;
    private javax.swing.JLabel netIncomeLabel;
    private javax.swing.JLabel netIncomeSubLabel;
    private javax.swing.JPanel occupiedRooms;
    private javax.swing.JComboBox<String> orderByBox;
    private javax.swing.JComboBox<String> orderByBox1;
    private javax.swing.JLabel orderByLabel;
    private javax.swing.JLabel orderByLabel1;
    private javax.swing.JButton registerTenantButton;
    private javax.swing.JLabel registeredTenantsLabel;
    private javax.swing.JLabel registeredTenantsLabel2;
    private javax.swing.JButton removeRoomButton;
    private javax.swing.JButton removeTenantButton;
    private javax.swing.JLabel rentEzIcon;
    private javax.swing.JLabel rentedRoomsCountLabel;
    private javax.swing.JLabel rentedRoomsIcon;
    private javax.swing.JLabel rentedRoomsLabel;
    private javax.swing.JButton resetDatabaseButton;
    private javax.swing.JLabel resetDatabaseLabel;
    private javax.swing.JComboBox<String> roomCapacityBox;
    private javax.swing.JLabel roomCapacityLabel;
    private javax.swing.JLabel roomIcon;
    private javax.swing.JTextField roomIdInput;
    private javax.swing.JTable roomInfoTable;
    private javax.swing.JTextField roomPriceInput;
    private javax.swing.JLabel roomPriceLabel;
    private javax.swing.JLabel roomSubLabel;
    private javax.swing.JComboBox<String> roomTypeBox;
    private javax.swing.JLabel roomTypeLabel;
    private javax.swing.JPanel roomsCard;
    private javax.swing.JButton settingsButton;
    private javax.swing.JLabel settingsLabel;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JComboBox<String> sortByBox;
    private javax.swing.JComboBox<String> sortByBox1;
    private javax.swing.JLabel sortByLabel;
    private javax.swing.JLabel sortByLabel1;
    private javax.swing.JPanel tablesPanel;
    private javax.swing.JLabel tenantCountLabel;
    private javax.swing.JLabel tenantIcon;
    private javax.swing.JPanel tenantInfoPanel;
    private javax.swing.JPanel tenantInfoPanel1;
    private javax.swing.JTable tenantInfoTable;
    private javax.swing.JLabel tenantSubLabel;
    private javax.swing.JPanel tenantsCard;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JPanel titlebarPanel;
    private javax.swing.JLabel transactionHistoryLabel;
    private javax.swing.JPanel transactionHistoryPanel;
    private javax.swing.JTable transactionHistoryTable;
    private javax.swing.JPanel viewAddedRoomsPanel;
    private javax.swing.JPanel viewRegisteredTenantsPanel;
    private javax.swing.JButton viewRoomButton;
    private javax.swing.JButton viewTenantButton;
    // End of variables declaration//GEN-END:variables
}
