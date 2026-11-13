package ui;

import util.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private final Color c1, c2;
    private boolean hover;

    public ModernButton(String text, Color c1, Color c2) {
        super(text);
        this.c1 = c1;
        this.c2 = c2;
        setFont(Theme.BOLD);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(200, 44));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color a = hover ? c1.brighter() : c1;
        Color b = hover ? c2.brighter() : c2;
        g2.setPaint(new GradientPaint(0, 0, a, getWidth(), 0, b));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.dispose();
        super.paintComponent(g);
    }
}