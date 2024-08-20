package hemmouda.joojle.gui;

import hemmouda.joojle.gui.Window;

import javax.swing.*;
import java.awt.*;

/**
 * TODO mention that it could have been done with reseting the objects or reusing them
 * but whatever, not that much of memory use
 * also say that the pane communicates with the window to tell it
 * what to do next
 */
public abstract class WindowPane extends JPanel {

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
