package ui;

import util.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TitleBar extends JPanel {

    public TitleBar(JFrame frame, Color iconColor) {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 8));

        IconButton minimize = new IconButton("\u2014", iconColor, new Color(0, 0, 0, 25), iconColor);
        minimize.addActionListener(e -> frame.setState(Frame.ICONIFIED));

        IconButton close = new IconButton("\u2715", iconColor, new Color(232, 17, 35), Color.WHITE);
        close.addActionListener(e -> System.exit(0));

        add(minimize);
        add(close);
        Theme.makeDraggable(frame, this);
    }

    private static class IconButton extends JButton {
        private boolean hover;
        private final Color hoverBg, normalFg, hoverFg;

        IconButton(String text, Color fg, Color hoverBg, Color hoverFg) {
            super(text);
            this.normalFg = fg;
            this.hoverBg = hoverBg;
            this.hoverFg = hoverFg;
            setForeground(fg);
            setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
            setPreferredSize(new Dimension(42, 30));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    hover = true; setForeground(IconButton.this.hoverFg); repaint();
                }
                @Override public void mouseExited(MouseEvent e) {
                    hover = false; setForeground(normalFg); repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (hover) {
                g.setColor(hoverBg);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            super.paintComponent(g);
        }
    }
}