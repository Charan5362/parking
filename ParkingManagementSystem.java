import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParkingManagementSystem {

    private JFrame frame;
    private JPanel mainPanel, centerPanel, bottomPanel;
    private JLabel titleLabel, slotNumberLabel, vehicleTypeLabel, licensePlateNumberLabel;
    private JTextField slotNumberTextField, vehicleTypeTextField, licensePlateNumberTextField;
    private JButton parkButton, unparkButton, exitButton;
    private DefaultListModel<String> parkingSlotsListModel;
    private JButton viewDetailsButton, clearListButton;
    private JTextArea detailsTextArea;
    private JScrollPane detailsScrollPane;
    private JList<String> parkingSlotsList;
    
    

    private List<ParkingSlot> parkingSlots = new ArrayList<>();

    public ParkingManagementSystem() {
        // Initialize the frame
        frame = new JFrame("Parking Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);

        // Initialize the main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Initialize the center panel
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 2, 10, 10));

        // Initialize the title label
        titleLabel = new JLabel("Parking Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Initialize the slot number label
        slotNumberLabel = new JLabel("Slot Number:");
        slotNumberLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        // Initialize the slot number text field
        slotNumberTextField = new JTextField(10);

        // Initialize the vehicle type label
        vehicleTypeLabel = new JLabel("Vehicle Type:");
        vehicleTypeLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        // Initialize the vehicle type text field
        vehicleTypeTextField = new JTextField(10);

        // Initialize the license plate number label
        licensePlateNumberLabel = new JLabel("License Plate Number:");
       licensePlateNumberLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        // Initialize the license plate number text field
        licensePlateNumberTextField = new JTextField(10);

        // Initialize the park button
        parkButton = new JButton("Park");
        parkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parkVehicle();
            }
        });

        // Initialize the unpark button
        unparkButton = new JButton("Unpark");
        unparkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unparkVehicle();
                // viewDetails();
            }
        });

        // Initialize the exit button
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Initialize the parking slots list
 // Initialize the parking slots list
parkingSlotsList = new JList<>();
parkingSlotsListModel = new DefaultListModel<>();
parkingSlotsList.setModel(parkingSlotsListModel);

// Set selection mode to SINGLE_SELECTION
parkingSlotsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        // Initialize the bottom panel
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Add the components to the center panel
        centerPanel.add(slotNumberLabel);
        centerPanel.add(slotNumberTextField);
        centerPanel.add(vehicleTypeLabel);
        centerPanel.add(vehicleTypeTextField);
        centerPanel.add(licensePlateNumberLabel);
        centerPanel.add(licensePlateNumberTextField);

        // Add the components to the bottom panel
        bottomPanel.add(parkButton);
        bottomPanel.add(unparkButton);
        bottomPanel.add(exitButton);

        // Add the panels to the main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        frame.add(mainPanel);

        // Display the frame
        frame.setVisible(true);

        // Load the parking slots from the file

        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewDetails();
            }
        });

        // Initialize the clear list button
        clearListButton = new JButton("Clear List");
        clearListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearParkingList();
            }
        });

        // Initialize the details text area
        detailsTextArea = new JTextArea(10, 30);
        detailsTextArea.setEditable(false);

        // Initialize the details scroll pane
        detailsScrollPane = new JScrollPane(detailsTextArea);
        mainPanel.add(detailsScrollPane, BorderLayout.EAST);
        // Add the components to the bottom panel
        bottomPanel.add(parkButton);
        bottomPanel.add(unparkButton);
        bottomPanel.add(viewDetailsButton);
        bottomPanel.add(clearListButton);
        bottomPanel.add(exitButton);

        // Add the details scroll pane to the main panel

        
       

        // Existing code...

        // Load the parking slots from the file
        loadParkingSlots();
    }

    private void parkVehicle() {
        // Get the slot number, vehicle type, and license plate number from the text fields
        String slotNumber = slotNumberTextField.getText();
        String vehicleType = vehicleTypeTextField.getText();
        String licensePlateNumber = licensePlateNumberTextField.getText();

        // Check if the slot number is valid
        if (!isValidSlotNumber(slotNumber)) {
            JOptionPane.showMessageDialog(frame, "Invalid slot number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the slot is available
        if (!isSlotAvailable(slotNumber)) {
            JOptionPane.showMessageDialog(frame, "Slot is not available!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new parking slot
        ParkingSlot parkingSlot = new ParkingSlot(slotNumber, vehicleType, licensePlateNumber);

        // Add the parking slot to the list of parking slots
        parkingSlots.add(parkingSlot);

        // Add the slot number to the parking slots list model
        parkingSlotsListModel.addElement(slotNumber);

        // Save the parking slots to the file
        saveParkingSlots();

        // Clear the text fields
        slotNumberTextField.setText("");
        vehicleTypeTextField.setText("");
        licensePlateNumberTextField.setText("");

        // Display a success message
        JOptionPane.showMessageDialog(frame, "Vehicle parked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void unparkVehicle() {
        // System.exit(0);
        // Get the slot number from the user
        String slotNumber = JOptionPane.showInputDialog(frame, "Enter Slot Number to Unpark:");

        // Check if a slot number is provided
        if (slotNumber == null || slotNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No slot number provided!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Find the parking slot with the specified slot number
        ParkingSlot parkingSlot = findParkingSlotBySlotNumber(slotNumber);

        // Check if the parking slot is found
        if (parkingSlot == null) {
            JOptionPane.showMessageDialog(frame, "Invalid slot number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Remove the parking slot from the list of parking slots
        parkingSlots.remove(parkingSlot);

        // Remove the slot number from the parking slots list model
        parkingSlotsListModel.removeElement(slotNumber);

        // Save the parking slots to the file
        saveParkingSlots();

        // Display a success message
        JOptionPane.showMessageDialog(frame, "Vehicle unparked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    
    private void viewDetails() {
        // Get the selected slot number from the list
        String slotNumber = JOptionPane.showInputDialog(frame, "Enter Slot Number to View:");
        if (slotNumber == null || slotNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No slot number provided!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ParkingSlot parkingSlot = findParkingSlotBySlotNumber(slotNumber);

        // Check if the parking slot is found
        if (parkingSlot == null) {
            JOptionPane.showMessageDialog(frame, "Invalid slot number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println(parkingSlot);
    
        // Get the parking slot by the selected index
        // selectedParkingSlots =  parkingSlots.get(slotNumber);
    
        // Display details in the text area
        detailsTextArea.setText("");
        // detailsTextArea.append("Slot Number: " + selectedParkingSlot.getSlotNumber() + "\n");
        // detailsTextArea.append("Vehicle Type: " + selectedParkingSlot.getVehicleType() + "\n");
        // detailsTextArea.append("License Plate: " + selectedParkingSlot.getLicensePlateNumber() + "\n");
    }
    



    private boolean isValidSlotNumber(String slotNumber) {
        return slotNumber.matches("[A-Z0-9]{1,10}");
    }

    private boolean isSlotAvailable(String slotNumber) {
        for (ParkingSlot parkingSlot : parkingSlots) {
            if (parkingSlot.getSlotNumber().equals(slotNumber)) {
                return false;
            }
        }
        return true;
    }

    private ParkingSlot findParkingSlotBySlotNumber(String slotNumber) {
        for (ParkingSlot parkingSlot : parkingSlots) {
            if (parkingSlot.getSlotNumber().equals(slotNumber)) {
                return parkingSlot;
            }
        }
        return null;
    }

    // ParkingSlot class to represent individual parking slots
    class ParkingSlot {
        private String slotNumber;
        private String vehicleType;
        private String licensePlateNumber;

        public ParkingSlot(String slotNumber, String vehicleType, String licensePlateNumber) {
            this.slotNumber = slotNumber;
            this.vehicleType = vehicleType;
            this.licensePlateNumber = licensePlateNumber;
        }

        public String getSlotNumber() {
            return slotNumber;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public String getLicensePlateNumber() {
            return licensePlateNumber;
        }
    }

    private void loadParkingSlots() {
        try (Scanner scanner = new Scanner(new File("parking_slots.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    ParkingSlot parkingSlot = new ParkingSlot(parts[0], parts[1], parts[2]);
                    parkingSlots.add(parkingSlot);
                    parkingSlotsListModel.addElement(parts[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveParkingSlots() {
        try (FileWriter writer = new FileWriter("parking_slots.txt")) {
            for (ParkingSlot parkingSlot : parkingSlots) {
                String line = String.format("%s,%s,%s%n",
                        parkingSlot.getSlotNumber(),
                        parkingSlot.getVehicleType(),
                        parkingSlot.getLicensePlateNumber());
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // private void viewDetails() {
    //     // Get the selected slot number from the list
    //     String selectedSlot = parkingSlotsList.getSelectedValue();

    //     // Check if a slot is selected
    //     if (selectedSlot == null) {
    //         JOptionPane.showMessageDialog(frame, "No slot selected!", "Error", JOptionPane.ERROR_MESSAGE);
    //         return;
    //     }

    //     // Find the parking slot by the selected slot number
    //     ParkingSlot selectedParkingSlot = findParkingSlotBySlotNumber(selectedSlot);

    //     // Display details in the text area
    //     detailsTextArea.setText("");
    //     if (selectedParkingSlot != null) {
    //         detailsTextArea.append("Slot Number: " + selectedParkingSlot.getSlotNumber() + "\n");
    //         detailsTextArea.append("Vehicle Type: " + selectedParkingSlot.getVehicleType() + "\n");
    //         detailsTextArea.append("License Plate: " + selectedParkingSlot.getLicensePlateNumber() + "\n");
    //     }
    // }
    // private void clearParkingList() {
    //     // Clear the list model and parking slots
    //     parkingSlotsListModel.clear();
    //     parkingSlots.clear();

    //     // Save the updated parking slots to the file
    //     saveParkingSlots();

    //     // Clear the details text area
    //     detailsTextArea.setText("");

    //     // Display a success message
    //     JOptionPane.showMessageDialog(frame, "Parking list cleared!", "Success", JOptionPane.INFORMATION_MESSAGE);
    // }

    private void clearParkingList() {
        // Clear the list model and parking slots
        parkingSlotsListModel.clear();
        parkingSlots.clear();

        // Save the updated parking slots to the file
        saveParkingSlots();

        // Clear the details text area
        detailsTextArea.setText("");

        // Display a success message
        JOptionPane.showMessageDialog(frame, "Parking list cleared!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ParkingManagementSystem());
    }
}
