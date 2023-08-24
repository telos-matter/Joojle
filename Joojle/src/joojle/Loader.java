package joojle;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Loader {

	/**
	 * Where all the loaded classes are  kept
	 */
	public static final List<Class<?>> LOADED_CLASSES = new LinkedList<>();
	
	/**
	 * Same as {@link #loadFrom(String, Collection)}
	 * but uses {@link #LOADED_CLASSES}
	 */
	public static void loadFrom (String jarFilePath) throws Exception {
		loadFrom(jarFilePath, LOADED_CLASSES);
	}
	
	/**
	 * Loads all the available classes of the passed in jar file path
	 * into <code>classes</code>
	 * 
	 * @param jarFilePath
	 * @param classes
	 * @throws Exception - If something went wrong
	 */
	public static void loadFrom (String jarFilePath, Collection<Class<?>> classes) throws Exception {
		assert classes != null;
		assert jarFilePath != null;
		
		try (JarFile jarFile = new JarFile(jarFilePath)) {
			Enumeration <JarEntry> entries = jarFile.entries();
			
			URL[] urls = { new URL("jar:file:" + jarFilePath + "!/") };
			URLClassLoader classLoader = URLClassLoader.newInstance(urls);
			
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class")) {
					String className = entry.getName()
							.replace("/", ".")
							.substring(0, entry.getName().length() - 6); // Remove ".class"
					
					try {
						Class<?> loadedClass = classLoader.loadClass(className);
						classes.add(loadedClass);
					} catch (ClassNotFoundException e) {
						System.err.println(String.format("Couldn't load this class: `%s`\nReason: `%s`", className, e.getMessage()));
					}
				}
			}
		}
	}
	
}
