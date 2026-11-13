package ui;

import dao.UserDAO;
import model.User;
import util.Theme;

import javax.swing.*;

public class LoginFrame extends AuthFrame {
    private final JTextField userField = new JTextField();
    private final JPasswordField passField = new JPasswordField();
    private final JLabel status = Theme.label(" ", Theme.SMALL, Theme.ERROR);
    private final UserDAO dao = new UserDAO();

    public LoginFrame() {
        super("Welcome Back!",
                "Sign in to access your dashboard, manage your account and stay secure.");

        Theme.styleField(userField);
        Theme.styleField(passField);
        passField.setEchoChar('\u2022');

        ModernButton loginBtn = new ModernButton("LOGIN", Theme.PURPLE, Theme.BLUE);
        loginBtn.addActionListener(e -> doLogin());

        JLabel registerLink = linkLabel(
                "<html>Don't have an account? <b><font color='#6A11CB'>Sign up</font></b></html>",
                () -> { new RegisterFrame().setVisible(true); dispose(); });

        form.add(Theme.glue());
        form.add(Theme.label("Sign In", Theme.TITLE, Theme.TEXT));
        form.add(Theme.gap(4));
        form.add(Theme.label("Enter your credentials to continue", Theme.SMALL, Theme.MUTED));
        form.add(Theme.gap(28));
        form.add(Theme.label("USERNAME", Theme.CAPTION, Theme.MUTED));
        form.add(userField);
        form.add(Theme.gap(18));
        form.add(Theme.label("PASSWORD", Theme.CAPTION, Theme.MUTED));
        form.add(passField);
        form.add(Theme.gap(10));
        form.add(showPasswordBox(passField));
        form.add(Theme.gap(6));
        form.add(status);
        form.add(Theme.gap(8));
        form.add(loginBtn);
        form.add(Theme.gap(18));
        form.add(registerLink);
        form.add(Theme.glue());

        getRootPane().setDefaultButton(loginBtn); // Enter key = login
    }

    private void doLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            status.setText("Please fill in all fields.");
            return;
        }
        User user = dao.login(username, password);
        if (user == null) {
            status.setText("Invalid username or password.");
            passField.setText("");
            return;
        }
        new DashboardFrame(user).setVisible(true);
        dispose();
    }
}