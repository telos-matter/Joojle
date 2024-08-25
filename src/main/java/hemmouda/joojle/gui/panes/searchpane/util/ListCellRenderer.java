package hemmouda.joojle.gui.panes.searchpane.util;

import hemmouda.joojle.api.core.MethodRecord;

import javax.swing.*;
import java.awt.*;

/**
 * A renderer for the JList used in
 * {@link hemmouda.joojle.gui.panes.searchpane.SearchPane}
 */
public class ListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        MethodRecord method = (MethodRecord) value;
        label.setText(value.toString());
        return label;
    }

}
