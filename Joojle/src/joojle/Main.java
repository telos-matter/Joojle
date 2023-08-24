package joojle;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {
	
	public static void main(String[] args) throws Exception {
		List<Class<?>> classes = new LinkedList<>();
        String jarFilePath = "/Users/telos_matter/Documents/l3chir_root/eTrack_root/dependencies/LGoodDatePicker-11.2.1.jar";
        Loader.loadFrom(jarFilePath, classes);
		System.out.println(classes);
	}

	
}



