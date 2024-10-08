package hemmouda.joojle.api;

import hemmouda.joojle.api.core.MethodRecord;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * A utility class that loads
 * the methods within a JAR.
 */
public class JarLoader {

	/**
	 * The JAR files extension
	 */
	public static final String JAR_FILE_EXTENSION = "jar";

	/**
	 * Lambda functions name pattern
	 */
	private static final Pattern LAMBDA_PATTERN = Pattern.compile("\\$\\d+$");

	/**
	 * Loads all the available {@link Method}s and {@link Constructor}s
	 * of all the available {@link Class}es within the given
	 * jar file.
	 *
	 * @throws Exception if something went wrong.
	 */
	public static List<MethodRecord> load (String jarFilePath) throws Throwable {
		if (jarFilePath == null) {
			throw new AssertionError("JarFilePath cannot be null.");
		}

		var list = new ArrayList<MethodRecord>();

		// Don't ask me about this neither.
		URL[] urls = {new URI("jar:file:" + jarFilePath + "!/").toURL()};

		try (JarFile jarFile = new JarFile(jarFilePath);
			 URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls)) {

			var entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				var entry = entries.nextElement();

				// If it's a class
				if (entry.getName().endsWith(".class")) {
					// Get the class' fully qualified name
					String className = entry.getName()
							.replace("/", ".")
							.substring(0, entry.getName().length() - 6); // Remove `.class`

					// Load the class
					Class<?> loadedClass = urlClassLoader.loadClass(className);
					if (loadedClass == null) {
						throw new AssertionError("Normally it should never be null");
					}
					// Add its methods
					list.addAll(loadAllFromClass(loadedClass));
				}
			}
		}

		return list;
	}
	
	/**
	 * Loads all the available {@link Method}s
	 * and {@link Constructor}s from the {@link Class}
	 * and returns them as a {@link List}
	 * of {@link MethodRecord}
	 */
	private static List<MethodRecord> loadAllFromClass (Class<?> clazz) {
		var list = new ArrayList<MethodRecord>();

		// Iterate over the methods
		for (Method method : clazz.getDeclaredMethods()) {
			if (!shouldSkip(method)) {
				list.add(new MethodRecord(method, false));
			}
		}

		// Iterate over the constructors
		for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
			if (!shouldSkip(constructor)) {
				list.add(new MethodRecord(constructor, true));
			}
		}
		
		return list;
	}

	/**
	 * We skip synthesized executables and
	 * lambda expressions.
	 */
	private static boolean shouldSkip (Executable executable) {
		return executable.isSynthetic() ||
				LAMBDA_PATTERN.matcher(executable.getName()).find();
	}

}
