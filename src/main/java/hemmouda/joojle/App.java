package hemmouda.joojle;

import hemmouda.joojle.gui.Window;

import javax.swing.*;

public interface App {

    public static void main(String[] args)  {
        // Set the look and feel to System if possible
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        // And then set tooltip dismiss delay
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        // Start
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            window.showSelectionWindow();
            window.setVisible(true);
        });
    }

}