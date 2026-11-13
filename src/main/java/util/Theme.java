package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Theme {
    // Colours
    public static final Color PURPLE  = new Color(106, 17, 203);
    public static final Color BLUE    = new Color(37, 117, 252);
    public static final Color ORANGE  = new Color(255, 138, 76);
    public static final Color RED     = new Color(235, 77, 100);
    public static final Color TEXT    = new Color(45, 52, 70);
    public static final Color MUTED   = new Color(130, 138, 160);
    public static final Color ERROR   = new Color(220, 53, 69);
    public static final Color SUCCESS = new Color(39, 174, 96);
    public static final Color BG      = new Color(244, 246, 251);
    public static final Color LINE    = new Color(210, 214, 224);

    // Fonts
    public static final Font TITLE   = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font HEADING = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font BOLD    = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font PLAIN   = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font CAPTION = new Font("Segoe UI", Font.BOLD, 11);

    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    public static Component gap(int height) {
        JComponent c = (JComponent) Box.createVerticalStrut(height);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    public static Component glue() {
        JComponent c = (JComponent) Box.createVerticalGlue();
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    /** Modern "underline" style text field that turns blue when focused. */
    public static void styleField(JTextField f) {
        f.setFont(PLAIN);
        f.setForeground(TEXT);
        f.setCaretColor(BLUE);
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        setUnderline(f, LINE);
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { setUnderline(f, BLUE); }
            @Override public void focusLost(FocusEvent e) { setUnderline(f, LINE); }
        });
    }

    private static void setUnderline(JComponent c, Color color) {
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, color),
                BorderFactory.createEmptyBorder(6, 2, 6, 2)));
    }

    /** Lets the user drag the undecorated window by grabbing this component. */
    public static void makeDraggable(JFrame frame, Component handle) {
        final Point[] offset = new Point[1];
        handle.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                Point s = e.getLocationOnScreen();
                Point f = frame.getLocation();
                offset[0] = new Point(s.x - f.x, s.y - f.y);
            }
        });
        handle.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                Point s = e.getLocationOnScreen();
                frame.setLocation(s.x - offset[0].x, s.y - offset[0].y);
            }
        });
    }
}