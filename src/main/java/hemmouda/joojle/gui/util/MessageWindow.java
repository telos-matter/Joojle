package hemmouda.joojle.gui.util;

import hemmouda.joojle.gui.Window;

import javax.swing.*;
import java.awt.*;

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

    public static void showInfo (String message, String title) {
        JOptionPane.showMessageDialog(
                null,
                message,
                Window.WINDOW_TITLE_PREFIX + title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows the info message in a JScrollPane
     * The message supports HTML. Font size must
     * be specified with the HTML.
     */
    public static void showLargeInfo (String message, String title) {
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");
        textPane.setText(message);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(800, 500));

        JOptionPane.showMessageDialog(
                null,
                scrollPane,
                Window.WINDOW_TITLE_PREFIX + title,
                JOptionPane.INFORMATION_MESSAGE);
    }

}
