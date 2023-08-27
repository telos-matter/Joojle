package joojle;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Searcher {

	/**
	 * Performs a fuzzy search using levenshtein distance on the
	 * query (that is assumed to be normalized)
	 * and the signatures of the {@link Function}s
	 * 
	 * @param query
	 * @param set
	 * @return a sorted subset of <code>set</code> that contains the matches
	 */
	public static Collection<Function> search (String query, Collection<Function> set) {
		assert query != null;
		assert set != null;
		
		
		return null;
	}
	
	/**
	 * @param query
	 * @return a normalized query or <code>null</code> if
	 * it failed to
	 * this before search search takes normalized
	 */
	public static String normalizeQuery (String query) {
		assert query != null;
		
		return null;
	}
	
	public static String forgeSignature (Executable executable) {
		StringBuilder signature = new StringBuilder();
		
		String returnType = getTypeName(executable.getAnnotatedReturnType());
		signature.append(returnType);
		
		signature.append('(');
		AnnotatedType [] paramters = executable.getAnnotatedParameterTypes();
		for (int i = 0; i < paramters.length; i++) {
			signature.append(getTypeName(paramters[i]));
			if (i < paramters.length -1) {
				signature.append(',');
			}
		}
		signature.append(')');
		
		return signature.toString();
	}
	
	private static String getTypeName (AnnotatedType annotatedType) {
		assert annotatedType != null;
		String type = annotatedType.getType().getTypeName();
		
		type = type.replace(", ", ","); // Map<K, V> -> Map<K,V>
		
		return cleanTypeName(type);
	}
	
	/**
	 * The hardest ones are typed ones with nested classes, example:
	 * java.util.Map<? extends joojle.Joojle$Subclass, ? extends joojle.Joojle$Subclass>
	 * 
	 * It could also be double nested ext..
	 * java.util.Map<? extends joojle.Joojle$Subclass<joojle.Joojle$Subclass2,joojle.Joojle$Subclass2>,? extends joojle.Joojle$Subclass<joojle.Joojle$Subclass2,joojle.Joojle$Subclass2>>
	 * 
	 * Normal examples:
	 * java.lang.String
	 * joojle.Joojle$Subclass
	 * 
	 * Simplest are the primitives ofc
	 */
	private static String cleanTypeName (String typeName) {
		assert typeName != null;
		
		// Not typed
		if (typeName.indexOf('<') == -1) {
			// Not nested and nor synthesized
			if (typeName.indexOf('$') == -1) {
				// Primitive or void or a type <T> ext
				if (typeName.indexOf('.') == -1) {
					return typeName;
				} else {
					return typeName.substring(typeName.lastIndexOf('.') +1);
				}
			} else {
				// TODO handle synthesized, they end like so $x where x is a number
				return typeName.substring(typeName.lastIndexOf('$') +1);
			}
			
		// Typed
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			
			String clazz = typeName.substring(0, typeName.indexOf('<')); // Map<K,V> -> Map
			String types = typeName.substring(typeName.indexOf('<') +1, typeName.length() -1); // Map<K,V> -> K,V (Could also be Map<Map<E,V>,Map<E,V>> -> Map<E,V>,Map<E,V>)
			
			stringBuilder.append(cleanTypeName(clazz));
			
			stringBuilder.append('<');
			
			ArrayList<String> typesArray = splitTypes(types);
			for (int i = 0; i <  typesArray.size(); i++) {
				String type = typesArray.get(i);
				
				if (type.contains(" extends ")) {
					int typeIndex = type.indexOf(" extends ", 0);
					stringBuilder.append(type.substring(0, typeIndex +9));
					stringBuilder.append(cleanTypeName(type.substring(typeIndex +10)));
				} else if (type.contains(" super ")) {
					int typeIndex = type.indexOf(" super ", 0);
					stringBuilder.append(type.substring(0, typeIndex +7));
					stringBuilder.append(cleanTypeName(type.substring(typeIndex +8)));
				} else {
					stringBuilder.append(cleanTypeName(type));
				}
				
				if (i < typesArray.size() -1) {
					stringBuilder.append(',');
				}
			}
			
			stringBuilder.append('>');
			
			return stringBuilder.toString();
		}
	}
	
	/**
	 * Splits on `,`
	 * Would receive something like K,V and return a list
	 * of K and V. Of course they can be nested like so
	 * Map<List<E>,V>,Map<E,V> and its going to return
	 * Map<List<E>,V> and Map<E,V>
	 */
	private static ArrayList<String> splitTypes (String types) {
		assert types != null;
		
		ArrayList<String> list = new ArrayList<>();
		StringBuilder buffer =  new StringBuilder();
		
		int depth = 0; // Depth of types `<>`
		for (int i = 0; i < types.length(); i++) {
			char c = types.charAt(i);
			buffer.append(c);
			
			switch (c) {
			case ',':
				if (depth >= 0) {
					assert depth == 0 : "Depth can't be greater than 0";
					buffer.setLength(buffer.length() -1); // Remove the added `,`
					list.add(buffer.toString());
					buffer.setLength(0);
				}
				break;
			case '<': 
				depth--;
				break;
			case '>': 
				depth++;
				break;
			}
		}
		
		list.add(buffer.toString()); // What's left
		return list;
	}
}
