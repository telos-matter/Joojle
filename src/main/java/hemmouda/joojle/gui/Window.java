package hemmouda.joojle.gui;

import hemmouda.joojle.api.core.MethodRecord;
import hemmouda.joojle.gui.panes.LoadingPane;
import hemmouda.joojle.gui.panes.SearchPane;
import hemmouda.joojle.gui.panes.SelectionPane;
import hemmouda.joojle.gui.panes.WindowPane;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class Window extends JFrame {

    public static final String WINDOW_TITLE_PREFIX = "Joojle";

    public Window () {
        super();

        // Set the look and feel to System if possible
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Default setup
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * To show the latest changes
     */
    private void refreshUI () {
        revalidate();
        repaint();
    }

    /**
     * Show this windowPane instead.
     */
    private void showWindow (WindowPane pane) {
        // Formate the title
        String title = WINDOW_TITLE_PREFIX + " - " + pane.getPreferredTitle();
        // Update it
        setTitle(title);

        // Clear everything.
        // Not really needed since we change
        // the content pane, but oh well.
        getContentPane().removeAll();
        // Set the window size that the pane wants
        setSize(pane.getPreferredWindowSize());
        // And now set the pane
        setContentPane(pane);

        // Finally refresh
        refreshUI();
    }

    /**
     * Show the selection pane on the window
     */
    public void showSelectionWindow () {
        var pane = new SelectionPane(this);
        showWindow(pane);
    }

    /**
     * Show the loading window while the
     * jar file is loading.
     */
    public void showLoadingWindow (File jarFile) {
        var pane = new LoadingPane(this, jarFile);
        showWindow(pane);

        pane.startLoadingJarFile();
    }

    public void showSearchWindow (String jarFilePath, List<MethodRecord> loadedMethods) {
        var pane = new SearchPane(this, jarFilePath, loadedMethods);
        showWindow(pane);
    }

}
