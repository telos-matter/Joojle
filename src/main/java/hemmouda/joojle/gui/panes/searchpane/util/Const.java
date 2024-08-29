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

    public static final String HELP_CMD = "/help";
    public static final String FLIP_COLOR_CMD = "/color";

    public static final int FONT_SIZE = 14;

    public static final Map<MethodKind, Color> METHOD_KIND_COLOR = Map.of(
            MethodKind.METHOD, new Color(10, 10, 230),
            MethodKind.CONSTRUCTOR, new Color(255, 140, 0)
    );

    public static final Map<MethodVisibility, Color> METHOD_VISIBILITY_COLOR = Map.of(
            MethodVisibility.PUBLIC, Color.green,
            MethodVisibility.PRIVATE, Color.red,
            MethodVisibility.PROTECTED, Color.orange,
            MethodVisibility.DEFAULT, Color.gray
    );

    public static final Map<MethodScope, Color> METHOD_SCOPE_COLOR = Map.of(
            MethodScope.INSTANCE, new Color(0, 128, 128),
            MethodScope.STATIC, new Color(128, 0, 128)
    );

    public static final String HTML_HELP_MESSAGE = """
            <html>
            <p style="font-size:%dpx;">
            <b>Joojle</b> is a utility application that allows you to search and
            look for a specific method, or constructor, within a JAR file,
            using the method signature or its potential name.
            <br>
            <br>
            Within the search field, type the method signature, followed by
            a colon "<code style="font-size:%dpx;">:</code>", followed by what you think the method name may be.
            <br>
            You can also only give the signature, or only the name by
            prefixing it with a colon. The signature or the name need not be exact
            because a fuzzy search is performed.
            <br>
            <br>
            The query for the method signature resembles that of the method
            definition:
            <br>
            <code style="font-size:%dpx;">returnType (param1Type, param2Type <K,V>, ..)</code>
            where you only need to specify the simple name of the type. And
            if it takes a generic type, then you describe it same as how you would
            when defining it.
            <br>
            Here are a couple of examples:
            </p>
            <ul style="font-size:%dpx;">
            <li>
            You are looking for a method that returns nothing and takes an array
            of ints. And you suspect it may be called processArray. The query
            could look like:
            <br><code style="font-size:%dpx;">void (int []) : processArray</code>
            </li>
            <br>
            <li>
            You are looking for a method that returns a List of type <code style="font-size:%dpx;">T</code>
            and takes a Map that maps that type to an Integer. And you have no
            idea what it may be called. The query could look like this:
            <br>
            <code style="font-size:%dpx;">List &lt;T&gt; (Map &lt;? extends T, Integer&gt;)</code>
            </li>
            <br>
            <li>
            You are looking for a method that you suspect could be named
            <code style="font-size:%dpx;">getFoo</code>. The query could look like this:
            <br>
            <code style="font-size:%dpx;">: getFoo</code>
            </li>
            </ul>
            <p style="font-size:%dpx;">
            If you'd want more examples about the query structure, then you can hold
            the mouse still on a method to see the tooltip that shows
            the query that best describes that method.
            <br>
            The return type of constructors is the type they are instantiating.
            <br>
            <br>
            You can also further filter the results according to kind,
            visibility, and scope.
            <br>
            <br>
            If the application is a bit sluggish, then you can turn off
            the color formatting by typing <code style="font-size:%dpx;">%s</code>.
            <br>
            <br>
            More information could be found in the
            Github repo: https://github.com/telos-matter/Joojle
            </p>
            </html>
            """.replace("%d", String.valueOf(FONT_SIZE)).formatted(FLIP_COLOR_CMD);
}
