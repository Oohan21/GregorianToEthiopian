import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GregorianToEthiopian extends JFrame {
    private JTextField gregorianYearField;
    private JTextField gregorianMonthField;
    private JTextField gregorianDayField;
    private JButton convertButton;
    private JButton resetButton;
    private JTextArea resultArea;

    public GregorianToEthiopian() {
        setTitle("Gregorian to Ethiopian Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        initComponents();
        addComponents();
        addActionListeners();
    }
    private void initComponents() {
        gregorianYearField = new JTextField(10);
        gregorianMonthField = new JTextField(10);
        gregorianDayField = new JTextField(10);
        convertButton = new JButton("Convert");
        resetButton = new JButton("Reset");
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);

        // Set text color to Magenta
        gregorianYearField.setForeground(Color.magenta);
        gregorianMonthField.setForeground(Color.magenta);
        gregorianDayField.setForeground(Color.magenta);
        resultArea.setForeground(Color.magenta);
    }

    private void addComponents() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Gregorian Year:"));
        inputPanel.add(gregorianYearField);
        inputPanel.add(new JLabel("Gregorian Month:"));
        inputPanel.add(gregorianMonthField);
        inputPanel.add(new JLabel("Gregorian Day:"));
        inputPanel.add(gregorianDayField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(convertButton);
        buttonPanel.add(resetButton);

        JPanel resultPanel = new JPanel();
        resultPanel.add(new JScrollPane(resultArea));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        // Set background color to light blue
        inputPanel.setBackground(new Color(23, 216, 193)); // Light blue
        buttonPanel.setBackground(new Color(23, 216, 193)); // Light blue
        resultPanel.setBackground(new Color(23, 216, 193)); // Light blue
        mainPanel.setBackground(new Color(23, 216, 193)); // Light blue

        add(mainPanel);
    }

    private void addActionListeners() {
        convertButton.addActionListener(e -> convertToEthiopian());

        resetButton.addActionListener(e -> clearFields());
    }

    private void convertToEthiopian() {
        try {
            int gregorianYear = Integer.parseInt(gregorianYearField.getText());
            int gregorianMonth = Integer.parseInt(gregorianMonthField.getText());
            int gregorianDay = Integer.parseInt(gregorianDayField.getText());
            if(gregorianMonth>12) {
                JOptionPane.showMessageDialog(this, "There are 12 months only in GC!! Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if(gregorianDay>31){
                JOptionPane.showMessageDialog(this, "There are a maximum of 31 days in GC!! Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);

            }

            int[] ethiopianDate = GregorianToEthiopianConverter.convertGregorianToEthiopian(gregorianYear, gregorianMonth , gregorianDay);
            resultArea.setText("\tEthiopian Date: " + ethiopianDate[0] + "/" + ethiopianDate[1] + "/" + ethiopianDate[2]);
        } catch (NumberFormatException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        gregorianYearField.setText("");
        gregorianMonthField.setText("");
        gregorianDayField.setText("");
        resultArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GregorianToEthiopian().setVisible(true));
    }
}

class GregorianToEthiopianConverter {
    public static int[] convertGregorianToEthiopian(int gregorianYear, int gregorianMonth, int gregorianDay) throws IOException {
        // Calculate Julian Day Number (JDN) for the given Gregorian date
        int jdn = calculateJDN(gregorianYear, gregorianMonth, gregorianDay);
        int joffset = 1723856;
        // Calculate Ethiopian year, month, and day from JDN
        int r = (jdn - joffset) % 1461;
        int n = r % 365 + 365 * (r / 1460);
        int ethiopianYear = 4 * ((jdn - joffset) / 1461) + r / 365 - r / 1460;
        int ethiopianMonth = n / 30 + 1;
        int ethiopianDay = n % 30 + 1;

        // Adjust month and day for the 13th month in Ethiopian calendar
        if (ethiopianMonth == 13) {
            gregorianMonth = 12;
            ethiopianDay = gregorianDay - 5;
        }
if (gregorianMonth == 2 && gregorianDay == 29) {
            if (ethiopianMonth == 6 && ethiopianDay == 22) {
                throw new IOException("Invalid input!");
            } else{
                return new int[]{ethiopianDay, ethiopianMonth, ethiopianYear};

            }
}
        if (gregorianMonth==2 && gregorianDay>29) {
            throw new IOException("Invalid input!");
        }
        return new int[]{ethiopianDay, ethiopianMonth, ethiopianYear};

    }

        public static int calculateJDN ( int year, int month, int day){
            // The Julian Day Number (JDN) calculation
            int a = (14 - month) / 12;
            int y = year + 4800 - a;
            int m = month + 12 * a - 3;
            return day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045;
        }
    }