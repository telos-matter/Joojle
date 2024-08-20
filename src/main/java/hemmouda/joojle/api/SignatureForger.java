package hemmouda.joojle.api;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class that generates
 * the signatures of the methods
 * and treats the user queries.
 */
public class SignatureForger {

    // Needed later on
    private static final List<String> BOUNDED_WILDCARDS = List.of(" extends ", " super ");

    /**
     * @return the signature of an {@link Executable}
     */
    public static String forgeSignature (Executable executable) {
        StringBuilder signature = new StringBuilder();

        // returnType
        String returnType = getTypeName(executable.getAnnotatedReturnType());
        signature.append(returnType);

        // returnType(
        signature.append('(');

        // returnType(param1Type,param2Type,...
        AnnotatedType[] parameters = executable.getAnnotatedParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            signature.append(getTypeName(parameters[i]));
            if (i < parameters.length -1) {
                signature.append(',');
            }
        }

        // returnType(param1Type,param2Type,...)
        signature.append(')');

        return simplifyQuery(signature.toString());
    }

    /**
     * Simplifies a query or a signature
     * of a method by removing unnecessary stuff.
     * Useful in order to make the fuzzy search go
     * a little more zoom zoom.
     */
    public static String simplifyQuery (String query) {
        // So far, all I can think about is removing white space
        // Changing the case is no go, because there could be
        // a class called FooBar and another class called
        // Foobar. Two different things.
        return query.replace(" ", "");
    }

    /**
     * Retrieves the typeName from an {@link AnnotatedType}
     * and cleans it.
     */
    private static String getTypeName (AnnotatedType annotatedType) {
        String type = annotatedType.getType().getTypeName();

        // Map<K, V> -> Map<K,V>
        type = type.replace(", ", ",");

        return cleanTypeName(type);
    }

    /**
     * <p>Cleaning here means getting the simple name
     * of a type. For example:
     * <strong>java.lang.String -> String</strong>
     * or
     * <strong>Foo$SubClass -> SubClass</strong>
     * </p>
     * <p>
     * The hardest types to clean are typed ones with nested classes, e.g.:
     * <br><strong>
     * java.util.Map&lt;T super Foo$Subclass, Bar$Subclass&gt;
     * </strong><br>
     * which should get cleaned to
     * <br><strong>
     * Map&lt;T super SubClass, SubClass&gt;
     * </strong><br>
     * It could also be double nested / typed with extends and all..
     * Here is an extreme example, suppose a class <code>Foo</code>
     * has two subclasses. The first one is
     * called <code>SubClass1</code> that
     * takes two generic types. And the second one
     * is called <code>SubClass2</code>. A method
     * could have this kind of parameter or
     * return type:
     * <br><strong>
     * java.util.Map&lt;Foo$Subclass1&lt;Foo$Subclass2, Foo$Subclass2&gt;, ? extends Foo$Subclass1&lt;Foo$Subclass2, Foo$Subclass2&gt;&gt;
     * </strong><br>
     * which should get cleaned to
     * <br><strong>
     * Map&lt;Subclass1&lt;Subclass2, Subclass2&gt;, ? extends Subclass1&lt;Subclass2, Subclass2&gt;&gt;
     * </strong><br>
     * </p>
     * <p>The simplest are the primitives or arrays.
     * They are already clean.</p>
     */
    private static String cleanTypeName (String typeName) {
        // Typed
        if (typeName.contains("<")) {

            StringBuilder stringBuilder = new StringBuilder();

            // java.util.Map<K,V> -> java.util.Map
            String clazz = typeName.substring(0, typeName.indexOf('<'));
            // java.util.Map<K,V> -> K,V
            // Could also be Map<Map<K,V>,List<T>> -> Map<K,V>,List<T>
            String generics = typeName.substring(typeName.indexOf('<') + 1, typeName.length() - 1);

            // java.util.Map -> Map
            clazz = cleanTypeName(clazz);
            // Map
            stringBuilder.append(clazz);

            // Map<
            stringBuilder.append('<');

            // Iterate over the generic types
            ArrayList<String> genericTypes = splitGenerics(generics);
            for (int i = 0; i < genericTypes.size(); i++) {
                String genericType = genericTypes.get(i);

                boolean hasBound = false;
                // ` extends ` and ` super `
                for (String bound : BOUNDED_WILDCARDS) {
                    int index = genericType.indexOf(bound);
                    // If it has a bound
                    if (index != -1) {
                        // Map<? extends
                        stringBuilder.append(genericType, 0, index + bound.length());
                        // ? extends foo.bar.Buz -> foo.bar.Buz
                        String genericTypeClass = genericType.substring(index + bound.length() +1);
                        // foo.bar.Buz -> Buz
                        genericTypeClass = cleanTypeName(genericTypeClass);
                        // Map<? extends Buz
                        stringBuilder.append(genericTypeClass);

                        hasBound = true;
                        break;
                    }
                }
                if (!hasBound) {
                    // java.util.String -> String
                    String genericTypeClass = cleanTypeName(genericType);
                    // Map<String
                    stringBuilder.append(genericTypeClass);
                }

                if (i < genericTypes.size() - 1) {
                    // Map<String,? extends Buz
                    stringBuilder.append(',');
                }
            }

            // Map<String,? extends Buz>
            stringBuilder.append('>');
            return stringBuilder.toString();

        // Nested, or synthesized
        } else if (typeName.contains("$")) {
            // Synthesized methods are like so: $x, where x is a number.
            // But who is out there working with synthesized methods?

            // foo.Bar$Buz -> Buz
            return typeName.substring(typeName.lastIndexOf('$') + 1);

        // Simple class inside packages
        } else if (typeName.contains(".")) {

            // java.util.String -> String
            return typeName.substring(typeName.lastIndexOf('.') + 1);

        // Primitives, void, arrays, or even generic types name like `T`
        } else {
            return typeName;
        }
    }

    /**
     * <p>Splits the generic types on `,`.</p>
     * <p>Would receive something like <strong>K,V</strong> and
     * return a list containing <strong>K</strong>
     * and <strong>V</strong>.</p>
     * <p>Of course they can be nested like so:
     * <br><strong>
     * Map&lt;List&lt;T&gt;,V&gt;,Map&lt;K,V&gt;
     * </strong><br>
     * in which case it would return:
     * <br><strong>
     * Map&lt;List&lt;T&gt;,V&gt;
     * </strong>
     * and
     * <strong>
     * Map&lt;K,V&gt;
     * </strong>
     */
    private static ArrayList<String> splitGenerics(String generics) {
        var list = new ArrayList<String>();
        StringBuilder buffer =  new StringBuilder();

        // Depth of nested generics <>
        int depth = 0;
        for (int i = 0; i < generics.length(); i++) {
            char c = generics.charAt(i);
            buffer.append(c);

            switch (c) {
                // Split if this is the top level
                case ',':
                    if (depth == 0) {
                        // Remove the added `,`
                        String genericType = buffer.substring(0, buffer.length());
                        list.add(genericType);
                        // Clear the buffer
                        buffer.setLength(0);
                    }
                    break;

                // Go down a level
                case '<':
                    depth++;
                    break;

                // Go up a level
                case '>':
                    depth--;
                    break;
            }

            if (depth < 0) {
                throw new AssertionError("How did you go up?");
            }
        }

        if (depth != 0) {
            throw new AssertionError("How did you mess-up the count?");
        }

        // Add what's left
        list.add(buffer.toString());
        return list;
    }

}
