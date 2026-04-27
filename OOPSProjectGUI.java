import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OOPSProjectGUI {

    public static void main(String[] args) {

        JFrame frame = new JFrame("✨ Student Stress Tracker");
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 247, 250));

        // Title
        JLabel title = new JLabel("Student Stress Tracker", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(title, BorderLayout.NORTH);

        // Input Panel
        JPanel panel = new JPanel(new GridLayout(6, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(Color.WHITE);

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField studyField = new JTextField();
        JTextField sleepField = new JTextField();
        JTextField assignField = new JTextField();
        JTextField dateField = new JTextField();

        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Age:")); panel.add(ageField);
        panel.add(new JLabel("Study Hours:")); panel.add(studyField);
        panel.add(new JLabel("Sleep Hours:")); panel.add(sleepField);
        panel.add(new JLabel("Assignments:")); panel.add(assignField);
        panel.add(new JLabel("Date (dd-mm-yyyy):")); panel.add(dateField);

        frame.add(panel, BorderLayout.WEST);

        // Output
        JTextArea output = new JTextArea();
        output.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        output.setEditable(false);
        output.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(output);
        scroll.setPreferredSize(new Dimension(380, 400));
        frame.add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 247, 250));

        JButton addBtn = new JButton("Add Entry");
        JButton viewBtn = new JButton("View All");
        JButton statsBtn = new JButton("Statistics");
        JButton exitBtn = new JButton("Exit");

        styleButton(addBtn);
        styleButton(viewBtn);
        styleButton(statsBtn);
        styleButton(exitBtn);

        exitBtn.setBackground(new Color(220, 53, 69));

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(statsBtn);
        buttonPanel.add(exitBtn);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Backend
        StudentDatabase db = new StudentDatabase();
        StressCalculator sc = new StressCalculator();
        SuggestionSystem ss = new SuggestionSystem();

        // ➕ ADD
        addBtn.addActionListener(e -> {
            try {

                if (nameField.getText().isEmpty() ||
                    ageField.getText().isEmpty() ||
                    studyField.getText().isEmpty() ||
                    sleepField.getText().isEmpty() ||
                    assignField.getText().isEmpty() ||
                    dateField.getText().isEmpty()) {

                    output.setText("❌ Please fill all fields!");
                    return;
                }

                Student s = new Student(
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        Integer.parseInt(studyField.getText()),
                        Integer.parseInt(sleepField.getText()),
                        Integer.parseInt(assignField.getText()),
                        dateField.getText()
                );

                db.addStudent(s);

                int score = sc.calculateScore(s);
                String level = sc.getStressLevel(score);
                String suggestion = ss.giveSuggestion(level);

                output.setText("✅ Added Successfully!\n\n" +
                        "Name: " + s.getName() +
                        "\nDate: " + s.getDate() +
                        "\n\nScore: " + score +
                        "\nLevel: " + level +
                        "\n\nSuggestion:\n" + suggestion);

                // 🔥 Clear fields
                nameField.setText("");
                ageField.setText("");
                studyField.setText("");
                sleepField.setText("");
                assignField.setText("");
                dateField.setText("");

            } catch (Exception ex) {
                output.setText("❌ Invalid input!");
            }
        });

        // 📊 VIEW
        viewBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("📊 All Records:\n\n");

            for (Student s : db.getAllStudents()) {
                int score = sc.calculateScore(s);
                String level = sc.getStressLevel(score);

                sb.append(s.getName())
                  .append(" | ").append(s.getDate())
                  .append(" | Score: ").append(score)
                  .append(" (").append(level).append(")\n");
            }

            output.setText(sb.toString());
        });

        // 📈 STATS + CHART 🔥
        statsBtn.addActionListener(e -> {

            ArrayList<Student> data = db.getAllStudents();

            if (data.isEmpty()) {
                output.setText("No data available.");
                return;
            }

            int total = 0, high = 0, medium = 0, low = 0;

            for (Student s : data) {
                int score = sc.calculateScore(s);
                total += score;

                if (score > 60) high++;
                else if (score > 30) medium++;
                else low++;
            }

            double avg = (double) total / data.size();

            output.setText("📈 Statistics:\n\n" +
                    "Average Score: " + avg +
                    "\nHigh: " + high +
                    "\nMedium: " + medium +
                    "\nLow: " + low);

            // 🔥 SHOW BAR CHART
            barchart.showChart(high, medium, low);
        });

        // EXIT
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    static void styleButton(JButton btn) {
        btn.setBackground(new Color(100, 149, 237));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
}