package ui;

import dao.UserDAO;
import util.Theme;

import javax.swing.*;

public class RegisterFrame extends AuthFrame {
    private final JTextField userField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField passField = new JPasswordField();
    private final JPasswordField confirmField = new JPasswordField();
    private final JLabel status = Theme.label(" ", Theme.SMALL, Theme.ERROR);
    private final UserDAO dao = new UserDAO();

    public RegisterFrame() {
        super("Create Account",
                "Join SecureAuth in seconds. Your password is protected with BCrypt hashing.");

        Theme.styleField(userField);
        Theme.styleField(emailField);
        Theme.styleField(passField);
        Theme.styleField(confirmField);
        passField.setEchoChar('\u2022');
        confirmField.setEchoChar('\u2022');

        ModernButton registerBtn = new ModernButton("REGISTER", Theme.PURPLE, Theme.BLUE);
        registerBtn.addActionListener(e -> doRegister());

        JLabel backLink = linkLabel(
                "<html>Already have an account? <b><font color='#6A11CB'>Sign in</font></b></html>",
                this::goToLogin);

        form.add(Theme.glue());
        form.add(Theme.label("Sign Up", Theme.TITLE, Theme.TEXT));
        form.add(Theme.gap(18));
        form.add(Theme.label("USERNAME", Theme.CAPTION, Theme.MUTED));
        form.add(userField);
        form.add(Theme.gap(12));
        form.add(Theme.label("EMAIL", Theme.CAPTION, Theme.MUTED));
        form.add(emailField);
        form.add(Theme.gap(12));
        form.add(Theme.label("PASSWORD", Theme.CAPTION, Theme.MUTED));
        form.add(passField);
        form.add(Theme.gap(12));
        form.add(Theme.label("CONFIRM PASSWORD", Theme.CAPTION, Theme.MUTED));
        form.add(confirmField);
        form.add(Theme.gap(8));
        form.add(showPasswordBox(passField, confirmField));
        form.add(Theme.gap(4));
        form.add(status);
        form.add(Theme.gap(6));
        form.add(registerBtn);
        form.add(Theme.gap(14));
        form.add(backLink);
        form.add(Theme.glue());

        getRootPane().setDefaultButton(registerBtn);
    }

    private void goToLogin() {
        new LoginFrame().setVisible(true);
        dispose();
    }

    private void doRegister() {
        String username = userField.getText().trim();
        String email = emailField.getText().trim();
        String pass = new String(passField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (username.length() < 3) {
            status.setText("Username must be at least 3 characters.");
        } else if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$")) {
            status.setText("Enter a valid email address.");
        } else if (pass.length() < 6) {
            status.setText("Password must be at least 6 characters.");
        } else if (!pass.equals(confirm)) {
            status.setText("Passwords do not match.");
        } else {
            String error = dao.register(username, email, pass);
            if (error != null) {
                status.setText(error);
            } else {
                JOptionPane.showMessageDialog(this, "Account created! Please log in.");
                goToLogin();
            }
        }
    }
}