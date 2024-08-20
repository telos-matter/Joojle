package hemmouda.joojle.gui.util;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.util.Map;

/**
 * A JComboBox that is used specifically
 * for the filters in the {@link hemmouda.joojle.gui.panes.SearchPane}
 */
public class FilterComboBox <T extends Enum<T>> extends JComboBox<String> {

    private static final Color DEFAULT_VALUE_COLOR = Color.black;

    private final T [] enumValues;

    /**
     * The color to use for each enum value
     * when it's selected
     */
    private Map<T, Color> valueColor;

    /**
     * The method to call when the
     * user selects something
     */
    private Runnable onSelectionChange;

    public FilterComboBox (T [] values, String defaultValue, Map<T, Color> valueColor, Runnable onSelectionChange) {
        super(getCapitalizedValues(values, defaultValue));

        this.enumValues = values;
        this.valueColor = valueColor;
        this.onSelectionChange = onSelectionChange;

        init();
    }

    private void init () {
        // Set default color
        setForeground(DEFAULT_VALUE_COLOR);

        // Add action listener to change
        // color depending on selection
        // and to call onSelectionChange method
        addActionListener(event -> {
            Color color = DEFAULT_VALUE_COLOR;
            T selected = getSelectedEnum();
            if (selected != null) {
                color = valueColor.get(selected);
            }
            setForeground(color);

            onSelectionChange.run();
        });

        // Add Popup listener to show default value colors
        // when selecting
        addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                setForeground(DEFAULT_VALUE_COLOR);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    /**
     * @return the selected enum or
     * <code>null</code> if the default
     * value is selected
     */
    public T getSelectedEnum () {
        String selectedValue = ((String) getSelectedItem()).toUpperCase();

        for (T enumValue : enumValues) {
            if (enumValue.toString().toUpperCase().equals(selectedValue)) {
                return enumValue;
            }
        }

        return null;
    }

    /**
     * Utility method to return capitalized
     * versions of the enum values alongside
     * the default value
     */
    private static <T extends Enum<T>> String [] getCapitalizedValues (T [] values, String defaultValue) {
        var capitalized = new String [values.length +1];
        capitalized[0] = defaultValue;

        for (int i = 1; i < capitalized.length; i++) {
            String value = values[i -1].toString();
            value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
            capitalized[i] = value;
        }

        return capitalized;
    }

}
