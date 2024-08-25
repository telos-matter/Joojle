package hemmouda.joojle.gui.panes.searchpane.util;

import hemmouda.joojle.api.core.methodinfo.MethodKind;
import hemmouda.joojle.api.core.methodinfo.MethodScope;
import hemmouda.joojle.api.core.methodinfo.MethodVisibility;

import java.awt.*;
import java.util.Map;

/**
 * Groups a bunch of constants that are used by
 * {@link hemmouda.joojle.gui.panes.searchpane.SearchPane}
 */
public class Const {

    public static final int FONT_SIZE = 14;

    public static final Map<MethodKind, Color> METHOD_KIND_COLOR = Map.of(
            MethodKind.METHOD, new Color(10, 10, 230),
            MethodKind.CONSTRUCTOR, new Color(255, 140, 0)
    );

    public static final Map<MethodVisibility, Color> METHOD_VISIBILITY_COLOR = Map.of(
            MethodVisibility.PUBLIC, Color.green,
            MethodVisibility.PRIVATE, Color.red,
            MethodVisibility.PROTECTED, Color.orange
    );

    public static final Map<MethodScope, Color> METHOD_SCOPE_COLOR = Map.of(
            MethodScope.INSTANCE, new Color(0, 128, 128),
            MethodScope.STATIC, new Color(128, 0, 128)
    );

}
