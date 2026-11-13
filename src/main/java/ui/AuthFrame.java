package ui;

import util.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public abstract class AuthFrame extends JFrame {
    protected static final int W = 820, H = 580;
    protected final JPanel form = new JPanel();

    protected AuthFrame(String heading, String subtitle) {
        setUndecorated(true);
        setSize(W, H);
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, W, H, 28, 28));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(buildLeft(heading, subtitle));
        root.add(buildRight());
        setContentPane(root);
    }

    private JPanel buildLeft(String heading, String subtitle) {
        GradientPanel left = new GradientPanel(Theme.PURPLE, Theme.BLUE);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        left.add(Theme.label("SecureAuth", Theme.HEADING, Color.WHITE));
        left.add(Theme.glue());
        left.add(Theme.label(heading, new Font("Segoe UI", Font.BOLD, 34), Color.WHITE));
        left.add(Theme.gap(12));
        left.add(Theme.label("<html><body style='width:260px'>" + subtitle + "</body></html>",
                Theme.PLAIN, new Color(240, 235, 255)));
        left.add(Theme.glue());
        left.add(Theme.label("\u00A9 2026 SecureAuth", Theme.SMALL, new Color(225, 215, 250)));

        Theme.makeDraggable(this, left);
        return left;
    }

    private JPanel buildRight() {
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(Color.WHITE);
        right.add(new TitleBar(this, Theme.MUTED), BorderLayout.NORTH);

        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(0, 45, 30, 45));
        right.add(form, BorderLayout.CENTER);
        return right;
    }

    /** A checkbox that reveals/hides the given password fields. */
    protected JCheckBox showPasswordBox(JPasswordField... fields) {
        JCheckBox box = new JCheckBox("Show password");
        box.setOpaque(false);
        box.setFont(Theme.SMALL);
        box.setForeground(Theme.MUTED);
        box.setFocusPainted(false);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        box.addActionListener(e -> {
            for (JPasswordField f : fields) {
                f.setEchoChar(box.isSelected() ? (char) 0 : '\u2022');
            }
        });
        return box;
    }

    /** A clickable text link. */
    protected JLabel linkLabel(String html, Runnable action) {
        JLabel l = Theme.label(html, Theme.SMALL, Theme.MUTED);
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        l.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { action.run(); }
        });
        return l;
    }
}