package hemmouda.joojle.api;

import hemmouda.joojle.api.core.MethodRecord;
import hemmouda.joojle.api.core.methodinfo.MethodScope;
import hemmouda.joojle.api.core.methodinfo.MethodKind;
import hemmouda.joojle.api.core.methodinfo.MethodVisibility;

import java.util.LinkedList;
import java.util.List;

/**
 * A utility class that filters
 * the methods according
 * to
 * {@link MethodKind},
 * {@link MethodVisibility}, and
 * {@link MethodScope}.
 */
public class Filter {

    /**
     * Filters and return a new list
     * that only contain the methods
     * that match the given "filters".
     * If a "filter" is <code>null</code>
     * it's skipped.
     */
    public static List<MethodRecord> filter (
            List<MethodRecord> list,
            MethodKind kind,
            MethodVisibility visibility,
            MethodScope scope) {

        // Make a new copy. That is a LinkedList because
        // we will be doing a lot of modifications
        var filtered = new LinkedList<>(list);

        if (kind != null) {
            filtered.removeIf(method -> method.getKind() != kind);
        }
        if (visibility != null) {
            filtered.removeIf(method -> method.getVisibility() != visibility);
        }
        if (scope != null) {
            filtered.removeIf(method -> method.getScope() != scope);
        }

        return filtered;
    }

}
