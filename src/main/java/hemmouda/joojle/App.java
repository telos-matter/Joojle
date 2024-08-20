package hemmouda.joojle;

import hemmouda.joojle.gui.Window;

import javax.swing.*;

public interface App {

    public static void main(String[] args)  {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            window.showSelectionWindow();
            window.setVisible(true);
        });
    }

}