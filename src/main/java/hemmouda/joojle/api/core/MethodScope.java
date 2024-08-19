package hemmouda.joojle.api.core;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * The scope of the method.
 * A member instance or a class instance.
 */
public enum MethodScope {

    INSTANCE,
    STATIC;

    /**
     * @return the scope of the given executable
     */
    public static MethodScope getScope (Executable executable) {
        var mod = executable.getModifiers();
        return (Modifier.isStatic(mod))? STATIC : INSTANCE;
        // If it's not static then it must be an
        // instance method. Right?
    }

}
