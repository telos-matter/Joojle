package hemmouda.joojle.api.core.methodinfo;

import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;

/**
 * The visibility of the method.
 */
public enum MethodVisibility {

    PUBLIC,
    PRIVATE,
    PROTECTED,
    DEFAULT;

    /**
     * @return the given executable visibility.
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
            // If none of the above then it's default
            return DEFAULT;
        }
    }

}
