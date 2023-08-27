package joojle;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import joojle.Joojle.Subclass2.Subclass2Subclass;

public class Joojle {
	
	public static void main(String[] args) throws Exception {
//        String jarFilePath = "/Users/telos_matter/Documents/l3chir_root/eTrack_root/dependencies/LGoodDatePicker-11.2.1.jar";
//        Loader.loadFrom(jarFilePath);
        
		Loader.loadAllFromClass(Joojle.class);
		
		for (Function function : Loader.LOADED_METHODS) {
			System.out.println(function);
		}
        System.out.println(Loader.LOADED_METHODS.size());
	}
	
	public Joojle () {
		
	}
	
	public Subclass2Subclass l () {
		return null;
	}
	
	public static List<?> test (int a, boolean b) {
		return null;
	}
	
	public static List<?> test (int a, char b) {
		return null;
	}
	
	public static <T extends Object> Map<?,T> test2 (T a) {
		return null;
	}
	

	public static Subclass  test3 (Subclass a, Subclass b) {
		return null;
	}
	
	public static Collection<? super Subclass<Subclass2, Subclass2>> test4 (Map<? super Subclass<Subclass2, Subclass2>, ? extends Subclass<Subclass2, List<Subclass2Subclass>>> c) {
		return null;
	}
	
	public static class Subclass <K,V> extends Joojle {
		
	}
	
	public static class Subclass2 extends Joojle {
		public static class Subclass2Subclass {
			
		}
	}
	
}



