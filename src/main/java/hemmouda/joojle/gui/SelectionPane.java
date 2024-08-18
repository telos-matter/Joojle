package hemmouda.joojle.gui;

import hemmouda.joojle.api.JarLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

/**
 * The selection pane to select or drop
 * a JAR file.
 */
public class SelectionPane extends WindowPane {

    public SelectionPane (Window parent) {
        super (parent);
        init();
    }

    private void init () {
        // Simple layout
        setLayout(new BorderLayout());

        // Where to drop the file
        JLabel dropTargetLabel = new JLabel("Drop a JAR file here or click 'Choose JAR file' to select one.", SwingConstants.CENTER);
        add(dropTargetLabel, BorderLayout.CENTER);

        // Setting up the drop target
        new DropTarget(dropTargetLabel, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dropEvent) {
                try {
                    // Copy the file
                    dropEvent.acceptDrop(DnDConstants.ACTION_COPY);
                    // Get the file
                    Transferable transferable = dropEvent.getTransferable();
                    java.util.List<File> droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if (!droppedFiles.isEmpty()) {
                        File file = droppedFiles.get(0);
                        parent.showLoadingWindow(file);
                    }

                } catch (Exception e) {
                    ErrorWindow.show("Couldn't drop the file because of: " +e.getLocalizedMessage());
                }
            }
        });

        // Set up file selector too
        JButton chooseFileButton = new JButton("Choose JAR file");
        chooseFileButton.addActionListener(e -> {
            // Create the file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            // Set it up to only accept JARs
            var filter = new FileNameExtensionFilter("JAR files", JarLoader.JAR_FILE_EXTENSION);
            fileChooser.setFileFilter(filter);
            // Show the window
            int acceptValue = fileChooser.showOpenDialog(null);
            if (acceptValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                parent.showLoadingWindow(file);
            }
        });
        add(chooseFileButton, BorderLayout.SOUTH);
    }

    @Override
    public Dimension getPreferredWindowSize() {
        return new Dimension(600, 300);
    }

    @Override
    public String getPreferredTitle() {
        return "Select or drop a JAR file";
    }
}
