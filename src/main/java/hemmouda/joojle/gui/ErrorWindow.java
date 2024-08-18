package hemmouda.joojle.gui;

import javax.swing.*;

public class ErrorWindow {

    public static void show (String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                Window.WINDOW_TITLE_PREFIX + " - Error occurred",
                JOptionPane.ERROR_MESSAGE);
    }

}
