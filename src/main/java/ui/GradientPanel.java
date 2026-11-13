package ui;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private final Color c1, c2;

    public GradientPanel(Color c1, Color c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // decorative translucent circles
        g2.setColor(new Color(255, 255, 255, 30));
        g2.fillOval(-60, getHeight() - 180, 260, 260);
        g2.fillOval(getWidth() - 120, -80, 220, 220);
        g2.setColor(new Color(255, 255, 255, 20));
        g2.fillOval(getWidth() - 200, getHeight() - 120, 200, 200);
        g2.dispose();
    }
}