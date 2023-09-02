package com.joojle;

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

public class Joojle {
	
	public static void main(String[] args) throws Exception {
        String jarFilePath = "/Users/telos_matter/Documents/l3chir_root/eTrack_root/dependencies/LGoodDatePicker-11.2.1.jar";
        Loader.loadFrom(jarFilePath);
//		Loader.loadAllFromClass(String.class);
		
//		for (Function function : Loader.LOADED_METHODS) {
//			System.out.println(function);
//		}
        System.out.println(Loader.LOADED_METHODS.size());
        
        int max = 20;
        int count = 0;
        for (Result result:  Searcher.search("int(int)", Loader.LOADED_METHODS)) {
        	System.out.println(result);
        	count++;
        	if (count >= max) {
        		break;
        	}
        }
	}
	
}



