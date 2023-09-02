package com.joojle;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Loader {

	/**
	 * Where all the loaded {@link Method}s and {@link Constructor}s are kept
	 */
	public static final List<Function> LOADED_METHODS = new LinkedList<>();
	
	/**
	 * Loads all the available {@link Method}s and {@link Constructor}s
	 * of all the available {@link Class}es of the passed in jar file path
	 * 
	 * @param jarFilePath
	 * @throws Exception - If something went wrong
	 */
	public static void loadFrom (String jarFilePath) throws Exception {
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
						loadAllFromClass(loadedClass);
					} catch (ClassNotFoundException e) {
						System.err.println(String.format("Couldn't load this class: `%s`\nReason: `%s`", className, e.getMessage()));
					}
				}
			}
		}
	}
	
	// TODO make private, only public to be able to test
	/**
	 * Loads all the available {@link Method}s
	 * and {@link Constructor}s from the {@link Class}
	 * 
	 * @param clazz
	 */
	public static void loadAllFromClass (Class<?> clazz) {
		assert clazz != null;
		
		for (Method method : clazz.getDeclaredMethods()) {
			LOADED_METHODS.add(new Function(method, false));
		}
		
		for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
			LOADED_METHODS.add(new Function(constructor, true));
		}
	}
	
}
