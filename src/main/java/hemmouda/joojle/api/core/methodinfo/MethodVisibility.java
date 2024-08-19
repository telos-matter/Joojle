package hemmouda.joojle.api.core.methodinfo;

import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;

/**
 * The visibility of the method.
 */
public enum MethodVisibility {

    PUBLIC,
    PRIVATE,
    PROTECTED;

    /**
     * @return the given executable visibility.
     * Or <code>null</code> if none match.
     */
    public static MethodVisibility getVisibility (Executable executable) {
        var mod = executable.getModifiers();
        
        if (Modifier.isPublic(mod)) {
            return PUBLIC;
        } else if (Modifier.isPrivate(mod)) {
            return PRIVATE;
        } else if (Modifier.isProtected(mod)) {
            return PROTECTED;
        } else {
            return null;
        }
    }

}
