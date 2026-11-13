import com.formdev.flatlaf.FlatLightLaf;
import ui.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        // table header colours
        UIManager.put("TableHeader.background", new Color(106, 17, 203));
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}