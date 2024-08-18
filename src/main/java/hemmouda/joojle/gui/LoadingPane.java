package hemmouda.joojle.gui;

import hemmouda.joojle.api.JarLoader;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * The loading pane to show when the JAR
 * file is being loaded.
 */
public class LoadingPane extends WindowPane {

    /**
     * The loaded JAR file.
     */
    private File jarFile;
    /**
     * The main label to show the information.
     */
    private JLabel displayLabel;

    public LoadingPane(Window parent, File jarFile) {
        super (parent);

        this.jarFile = jarFile;

        init();
    }

    private void init () {
        // Simple layout
        setLayout(new BorderLayout());

        // Add the display label
        displayLabel = new JLabel("The JAR file is being processed..", SwingConstants.CENTER);
        add(displayLabel, BorderLayout.CENTER);
    }

    /**
     * Called to start loading the JAR file
     */
    public void startLoadingJarFile () {
        try {
            // Try and load the JAR file
            var list = JarLoader.load(jarFile.getAbsolutePath());
            // If all went well, display it and go to next window
            displayLabel.setText("The JAR file finished processing");
            parent.showSearchWindow(jarFile.getPath(), list);
        } catch (Throwable throwable) {
            // If an error occurred, show the message
            ErrorWindow.show("The provided JAR file could not be processed because of: " +throwable.getLocalizedMessage());
            // And go back to the selection window
            parent.showSelectionWindow();
        }
    }

    @Override
    public Dimension getPreferredWindowSize() {
        return new Dimension(600, 300);
    }

    @Override
    public String getPreferredTitle() {
        return "Processing JAR file";
    }
}
