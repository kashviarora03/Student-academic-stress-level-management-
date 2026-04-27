import javax.swing.*;
import java.awt.*;

public class barchart extends JPanel {

    int high, medium, low;

    public barchart(int high, int medium, int low) {
        this.high = high;
        this.medium = medium;
        this.low = low;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[] values = {high, medium, low};
        String[] labels = {"High", "Medium", "Low"};
        Color[] colors = {
                new Color(255, 99, 132),
                new Color(255, 159, 64),
                new Color(75, 192, 192)
        };

        int max = Math.max(high, Math.max(medium, low));

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int base = panelHeight - 70;
        int maxBarHeight = panelHeight - 150;

        int barWidth = 70;
        int gap = 70;

        int totalWidth = (barWidth * values.length) + (gap * (values.length - 1));
        int startX = (panelWidth - totalWidth) / 2;

        // Axes
        g2.setColor(Color.GRAY);
        g2.drawLine(startX - 30, base, startX - 30, base - maxBarHeight);
        g2.drawLine(startX - 30, base, startX + totalWidth + 20, base);

        for (int i = 0; i < values.length; i++) {

            int height = (max == 0) ? 0 : (values[i] * maxBarHeight / max);
            int x = startX + i * (barWidth + gap);

            // Bar
            g2.setColor(colors[i]);
            g2.fillRoundRect(x, base - height, barWidth, height, 20, 20);

            // Centered value
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            String valueText = String.valueOf(values[i]);
            int textWidth = fm.stringWidth(valueText);
            g2.drawString(valueText, x + (barWidth - textWidth) / 2, base - height - 5);

            // Label
            String label = labels[i];
            int labelWidth = fm.stringWidth(label);
            g2.drawString(label, x + (barWidth - labelWidth) / 2, base + 20);
        }

        // Title
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.drawString("Stress Level Distribution", panelWidth / 2 - 110, 30);
    }

    public static void showChart(int high, int medium, int low) {
        JFrame frame = new JFrame("📊 Stress Graph");
        frame.setSize(520, 420);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.add(new barchart(high, medium, low));
        frame.setVisible(true);
    }
}