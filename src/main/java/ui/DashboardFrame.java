package ui;

import dao.UserDAO;
import model.User;
import util.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.List;

public class DashboardFrame extends JFrame {
    private static final int W = 980, H = 620;
    private static final SimpleDateFormat FMT = new SimpleDateFormat("dd MMM yyyy, hh:mm a");

    private final User user;
    private final UserDAO dao = new UserDAO();
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private DefaultTableModel tableModel;
    private JTable table;

    public DashboardFrame(User user) {
        this.user = user;
        setUndecorated(true);
        setSize(W, H);
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, W, H, 28, 28));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(Theme.BG);
        right.add(new TitleBar(this, Theme.MUTED), BorderLayout.NORTH);

        content.setOpaque(false);
        content.add(buildHome(), "home");
        content.add(buildSettings(), "settings");
        if (user.isAdmin()) content.add(buildUsers(), "users");
        right.add(content, BorderLayout.CENTER);

        root.add(right, BorderLayout.CENTER);
        setContentPane(root);
    }

    // ---------- Sidebar ----------
    private JPanel buildSidebar() {
        GradientPanel side = new GradientPanel(new Color(30, 20, 70), new Color(80, 30, 150));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setPreferredSize(new Dimension(230, H));
        side.setBorder(BorderFactory.createEmptyBorder(30, 22, 25, 22));

        side.add(Theme.label("SecureAuth", Theme.HEADING, Color.WHITE));
        side.add(Theme.gap(28));
        side.add(Theme.label(user.getUsername(), Theme.BOLD, Color.WHITE));
        side.add(Theme.gap(2));
        side.add(Theme.label(user.getRole().toUpperCase() + " ACCOUNT",
                Theme.CAPTION, new Color(200, 185, 235)));
        side.add(Theme.gap(30));

        side.add(navButton("Home", () -> cards.show(content, "home")));
        side.add(navButton("Settings", () -> cards.show(content, "settings")));
        if (user.isAdmin()) {
            side.add(navButton("Manage Users", () -> { loadUsers(); cards.show(content, "users"); }));
        }

        side.add(Theme.glue());
        ModernButton logout = new ModernButton("LOGOUT", Theme.RED, Theme.ORANGE);
        logout.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        side.add(logout);

        Theme.makeDraggable(this, side);
        return side;
    }

    private JButton navButton(String text, Runnable action) {
        JButton b = new JButton(text);
        Color normal = new Color(215, 205, 240);
        b.setFont(Theme.PLAIN);
        b.setForeground(normal);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.addActionListener(e -> action.run());
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setForeground(Color.WHITE); }
            @Override public void mouseExited(MouseEvent e) { b.setForeground(normal); }
        });
        return b;
    }

    // ---------- Home page ----------
    private JPanel buildHome() {
        JPanel p = page();
        p.add(header("Welcome back, " + user.getUsername() + "!",
                "Here is a quick overview of your account."), BorderLayout.NORTH);

        String last = user.getLastLogin() == null
                ? "First login" : FMT.format(user.getLastLogin());
        String email = user.getEmail() == null ? "Not set" : user.getEmail();

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);
        grid.add(card("ROLE", user.getRole().toUpperCase(), Theme.PURPLE));
        grid.add(card("EMAIL", email, Theme.BLUE));
        grid.add(card("LAST LOGIN", last, Theme.ORANGE));
        grid.add(card("REGISTERED USERS", String.valueOf(dao.countUsers()), Theme.RED));

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(grid, BorderLayout.NORTH);
        p.add(wrap, BorderLayout.CENTER);
        return p;
    }

    // ---------- Settings page (change password) ----------
    private JPanel buildSettings() {
        JPanel p = page();
        p.add(header("Settings", "Change your account password."), BorderLayout.NORTH);

        JPasswordField oldF = new JPasswordField();
        JPasswordField newF = new JPasswordField();
        JPasswordField confF = new JPasswordField();
        for (JPasswordField f : new JPasswordField[]{oldF, newF, confF}) {
            Theme.styleField(f);
        }
        JLabel status = Theme.label(" ", Theme.SMALL, Theme.ERROR);
        ModernButton save = new ModernButton("UPDATE PASSWORD", Theme.PURPLE, Theme.BLUE);

        save.addActionListener(e -> {
            String o = new String(oldF.getPassword());
            String n = new String(newF.getPassword());
            String c = new String(confF.getPassword());
            status.setForeground(Theme.ERROR);
            if (o.isEmpty()) {
                status.setText("Enter your current password.");
            } else if (n.length() < 6) {
                status.setText("New password must be at least 6 characters.");
            } else if (!n.equals(c)) {
                status.setText("New passwords do not match.");
            } else {
                String err = dao.changePassword(user.getId(), o, n);
                if (err == null) {
                    status.setForeground(Theme.SUCCESS);
                    status.setText("Password updated successfully.");
                    oldF.setText(""); newF.setText(""); confF.setText("");
                } else {
                    status.setText(err);
                }
            }
        });

        JPanel box = whitePanel(null);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        box.add(Theme.label("CURRENT PASSWORD", Theme.CAPTION, Theme.MUTED));
        box.add(oldF);
        box.add(Theme.gap(16));
        box.add(Theme.label("NEW PASSWORD", Theme.CAPTION, Theme.MUTED));
        box.add(newF);
        box.add(Theme.gap(16));
        box.add(Theme.label("CONFIRM NEW PASSWORD", Theme.CAPTION, Theme.MUTED));
        box.add(confF);
        box.add(Theme.gap(8));
        box.add(status);
        box.add(Theme.gap(8));
        box.add(save);
        box.setPreferredSize(new Dimension(420, box.getPreferredSize().height));

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.setOpaque(false);
        wrap.add(box);
        p.add(wrap, BorderLayout.CENTER);
        return p;
    }

    // ---------- Admin: manage users ----------
    private JPanel buildUsers() {
        JPanel p = page();
        p.add(header("Manage Users", "View and remove registered accounts."), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Username", "Email", "Role", "Last Login"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(Theme.PLAIN);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(230, 222, 250));
        table.setSelectionForeground(Theme.TEXT);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.LINE));
        scroll.getViewport().setBackground(Color.WHITE);

        ModernButton refresh = new ModernButton("REFRESH", Theme.BLUE, Theme.PURPLE);
        refresh.setPreferredSize(new Dimension(150, 40));
        refresh.addActionListener(e -> loadUsers());

        ModernButton delete = new ModernButton("DELETE SELECTED", Theme.RED, Theme.ORANGE);
        delete.setPreferredSize(new Dimension(190, 40));
        delete.addActionListener(e -> deleteSelected());

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        bar.add(refresh);
        bar.add(delete);

        p.add(scroll, BorderLayout.CENTER);
        p.add(bar, BorderLayout.SOUTH);
        loadUsers();
        return p;
    }

    private void loadUsers() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        List<User> users = dao.getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{
                    u.getId(), u.getUsername(),
                    u.getEmail() == null ? "-" : u.getEmail(),
                    u.getRole(),
                    u.getLastLogin() == null ? "-" : FMT.format(u.getLastLogin())});
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a user first.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        if (id == user.getId()) {
            JOptionPane.showMessageDialog(this, "You cannot delete your own account.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
                "Delete user '" + tableModel.getValueAt(row, 1) + "'?",
                "Confirm delete", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            dao.deleteUser(id);
            loadUsers();
        }
    }

    // ---------- Small UI helpers ----------
    private JPanel page() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(10, 35, 30, 35));
        return p;
    }

    private JPanel header(String title, String subtitle) {
        JPanel h = new JPanel();
        h.setOpaque(false);
        h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));
        h.add(Theme.label(title, Theme.TITLE, Theme.TEXT));
        h.add(Theme.gap(4));
        h.add(Theme.label(subtitle, Theme.PLAIN, Theme.MUTED));
        h.add(Theme.gap(25));
        return h;
    }

    private JPanel card(String title, String value, Color accent) {
        JPanel c = whitePanel(accent);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(BorderFactory.createEmptyBorder(20, 26, 20, 18));
        c.add(Theme.label(title, Theme.CAPTION, Theme.MUTED));
        c.add(Theme.gap(8));
        c.add(Theme.label(value, Theme.HEADING, Theme.TEXT));
        return c;
    }

    /** White rounded panel; if accent is not null, draws a coloured stripe on the left. */
    private JPanel whitePanel(Color accent) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                if (accent != null) {
                    g2.setColor(accent);
                    g2.fillRoundRect(0, 18, 5, getHeight() - 36, 5, 5);
                }
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }
}