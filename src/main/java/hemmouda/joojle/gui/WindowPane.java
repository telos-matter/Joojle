package hemmouda.joojle.gui;

import javax.swing.*;
import java.awt.*;

/**
 * <p>Base class of the different Panels that are shown
 * as the ContentPane in the main Window.</p>
 * <p>It's these panels that guide
 * the application by telling the window
 * what to show next.</p>
 * <p>Since there is only ever one window, the implementations
 * could be singletons that reset the fields if they
 * are re-used but that's too much work.</p>
 */
public abstract class WindowPane extends JPanel {

    /**
     * The main window
     */
    protected hemmouda.joojle.gui.Window parent;

    public WindowPane (Window parent) {
        super();

        this.parent = parent;
    }

    /**
     * @return the size the parent should use.
     */
    public abstract Dimension getPreferredWindowSize ();

    /**
     * @return the title the parent should use.
     */
    public abstract String getPreferredTitle ();

}
