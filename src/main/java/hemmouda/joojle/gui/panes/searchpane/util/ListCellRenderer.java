package hemmouda.joojle.gui.panes.searchpane.util;

import hemmouda.joojle.api.core.MethodRecord;
import hemmouda.joojle.api.core.methodinfo.MethodKind;
import hemmouda.joojle.api.core.methodinfo.MethodScope;
import hemmouda.joojle.api.core.methodinfo.MethodVisibility;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A renderer for the JList used in
 * {@link hemmouda.joojle.gui.panes.searchpane.SearchPane}
 */
public class ListCellRenderer extends DefaultListCellRenderer {

    private boolean colorful = true;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        MethodRecord method = (MethodRecord) value;
        label.setToolTipText(method.getPerfectQuery());
        label.setText(formatMethod(method));

        return label;
    }

    private String formatMethod (MethodRecord method) {
        String str = formatVisibility(method) +
                " " +
                formatReturnType(method) +
                " " +
                formatName(method) +
                " " +
                formatParams(method);


        if (colorful) {
            str =  "<html>" + str + "</html>";
        }

        return str.strip();
    }

    private String formatVisibility (MethodRecord method) {
        String mod = method.getModifiersString();

        if (colorful) {
            // Color the visibility if it exists
            for (MethodVisibility visibility : MethodVisibility.values()) {
                String value = visibility.toString().toLowerCase();
                if (mod.contains(value)) {
                    Color color = Const.METHOD_VISIBILITY_COLOR.get(visibility);
                    mod = mod.replace(value, wrapInColoredSpan(value, color));
                    break;
                }
            }

            // Color static if it exists
            String value = MethodScope.STATIC.toString().toLowerCase();
            if (mod.contains(value)) {
                Color color = Const.METHOD_SCOPE_COLOR.get(MethodScope.STATIC);
                mod = mod.replace(value, wrapInColoredSpan(value, color));
            }
        }

        return mod;
    }

    private String formatReturnType (MethodRecord method) {
        return method.getReturnTypeString();
    }

    private String formatName(MethodRecord method) {
        String name = method.getDescriptiveNameString();

        if (colorful) {
            // Color name depending on scope and kind
            Color color = null;
            if (method.getScope() == MethodScope.STATIC) {
                color = Const.METHOD_SCOPE_COLOR.get(MethodScope.STATIC);
            } else if (method.getKind() == MethodKind.CONSTRUCTOR) {
                color = Const.METHOD_KIND_COLOR.get(MethodKind.CONSTRUCTOR);
            } else {
                color = Const.METHOD_SCOPE_COLOR.get(MethodScope.INSTANCE);
            }

            name = wrapInColoredSpan(name, color);
        }

        return name;
    }

    public String formatParams (MethodRecord method) {
        if (!colorful) {
            return method.getParamsString().stream()
                    .collect(Collectors.joining(", ", "(", ")"));
        } else {
            // Can't substitute because span contains the delimiters
            final Color color = Const.REST_COLOR;
            return method.getParamsString().stream()
                    .collect(Collectors.joining(
                            wrapInColoredSpan(", ", color),
                            wrapInColoredSpan("(", color),
                            wrapInColoredSpan(")", color)));
        }
    }

    /**
     * Wraps the given string inside
     * a colored html span.
     */
    private static String wrapInColoredSpan (String s, Color color) {
        return "<span style='color:rgb(%d,%d,%d)'>%s</span>".formatted(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                s
        );
    }

    public void flipColorful () {
        colorful = !colorful;
    }

}
