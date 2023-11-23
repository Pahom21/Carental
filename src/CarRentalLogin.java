import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarRentalApp {
    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Car Rental System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        usernameField.setPreferredSize(new Dimension(100, 10)); // Adjust the width and height here


        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(100, 10)); // Adjust the width and height here

        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement your login logic here
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                //Connect to the database
                Connection connection = DatabaseConnector.connect();

                // Check user credentials and role
                String role = checkCredentials(connection, username, password);

                // Close the database connection
                DatabaseConnector.close();


                if (role.equals("Admin")) {
                    showAdminView(frame);
                } else if (role.equals("User")) {
                    showUserView(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password. Please try again.");
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(loginButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    // Check user credentials in the database
    private static String checkCredentials(Connection connection, String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            //Query Execution
            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if a matching user was found
            if(resultSet.next()){
                // If a matching user is found, return their role
                return resultSet.getString("role");
            }else{
                // If no matching user is found, return "invalid"
                return "invalid";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Invalid";
        }
    }
    // Show the admin view
    private static void showAdminView(JFrame frame) {
        // Create and display the admin view (e.g., a new JFrame or JPanel)
        // You can replace this with your admin UI code
        frame.getContentPane().removeAll();
        frame.add(new JLabel("Admin View"));
        frame.revalidate();
        frame.repaint();
    }

    // Show the user view
    private static void showUserView(JFrame frame) {
        // Create and display the user view (e.g., a new JFrame or JPanel)
        // You can replace this with your user UI code
        frame.getContentPane().removeAll();
        frame.add(new JLabel("User View"));
        frame.revalidate();
        frame.repaint();
    }

}
