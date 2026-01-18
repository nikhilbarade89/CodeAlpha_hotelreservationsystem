package ui;

import service.HotelService;
import model.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;

public class MainFrame extends JFrame {

    private HotelService service = new HotelService();
    private JTextArea output = new JTextArea();

    public MainFrame() {
        setTitle("Hotel Reservation System");
        setSize(600, 500);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(3, 2));
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"User", "Admin"});
        JTextField userField = new JTextField();

        top.add(new JLabel("Role:"));
        top.add(roleBox);
        top.add(new JLabel("Enter your name:"));
        top.add(userField);


        JPanel center = new JPanel(new GridLayout());

        JTextField roomField = new JTextField(8);
        roomField.setPreferredSize(new Dimension(120,25));
        JTextField resField = new JTextField();

        JButton searchBtn = new JButton("Search Rooms");
        JButton reserveBtn = new JButton("Reserve & Pay");
        JButton cancelBtn = new JButton("Cancel Reservation");
        JButton adminViewBtn = new JButton("View All Bookings");
        adminViewBtn.setBackground(Color.black);
        adminViewBtn.setForeground(Color.white);
        reserveBtn.setBackground(Color.GREEN);
        cancelBtn.setBackground(Color.RED);

        top.add(new JLabel("Room No:"));
        top.add(roomField);
        center.add(reserveBtn);
        center.add(cancelBtn);
        center.add(searchBtn);
        center.add(adminViewBtn);
        center.setBackground(Color.LIGHT_GRAY);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.SOUTH);
        add(new JScrollPane(output), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            output.setText("");
            for (Room r : service.getRooms()) {
                output.append("Room " + r.getRoomNumber() + " | " +
                        r.getCategory() + " | ₹" + r.getPrice() + " | " +
                        (r.isAvailable() ? "Available" : "Booked") + "\n");
            }
        });
        roleBox.addItemListener(e ->{
            if(e.getStateChange()== ItemEvent.SELECTED){
                output.setText("");
                userField.setText("");
                roomField.setText("");

            }
        });

        reserveBtn.addActionListener(e -> {
            try {
                String id = service.reserveRoom(userField.getText(),
                        Integer.parseInt(roomField.getText()));
                JOptionPane.showMessageDialog(this,
                        id != null ? "Payment Successful\nReservation ID: " + id
                                : "Room Not Available");
                userField.setText("");
                roomField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        });

        cancelBtn.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog("Enter Reservation ID");
                boolean ok = service.cancelReservation(id, userField.getText());
                JOptionPane.showMessageDialog(this,
                        ok ? "Cancelled Successfully" : "Not Found");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        adminViewBtn.addActionListener(e -> {
            if (!roleBox.getSelectedItem().equals("Admin")) {
                JOptionPane.showMessageDialog(this, "Admin Access Only");
                return;
            }
            output.setText("");
            try {
                for (String s : service.getAllReservations())
                    output.append(s + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}