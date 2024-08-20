package hemmouda.joojle.gui.util;

import hemmouda.joojle.gui.Window;

import javax.swing.*;

/**
 * A simple window to show messages.
 */
public class MessageWindow {

    public static void showError(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                Window.WINDOW_TITLE_PREFIX + " - An error occurred",
                JOptionPane.ERROR_MESSAGE);
    }

}
