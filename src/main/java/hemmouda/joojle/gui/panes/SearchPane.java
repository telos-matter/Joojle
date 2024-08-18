package hemmouda.joojle.gui.panes;

import hemmouda.joojle.api.core.MethodRecord;
import hemmouda.joojle.gui.Window;
import hemmouda.joojle.gui.panes.WindowPane;

import java.awt.*;
import java.util.List;

/**
 * The main pane that lets the user search
 * and see results
 */
public class SearchPane extends WindowPane {

    /**
     * The loaded methods on which the search
     * and filtering will be preformed
     */
    private List<MethodRecord> loadedMethods;
    /**
     * The path of the JAR file that has been loaded.
     * Only used to display information
     */
    private String jarFilePath;

    public SearchPane(Window parent, String jarFilePath, List <MethodRecord> loadedMethods) {
        super(parent);

        this.jarFilePath = jarFilePath;
        this.loadedMethods = loadedMethods;

        init();
    }

    /**
     * This boi thicc.
     */
    private void init () {

    }

    @Override
    public Dimension getPreferredWindowSize() {
        return new Dimension(800, 600);
    }

    @Override
    public String getPreferredTitle() {
        return jarFilePath;
    }
}
