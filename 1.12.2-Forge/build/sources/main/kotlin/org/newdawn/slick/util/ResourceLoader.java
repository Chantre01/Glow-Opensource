package org.newdawn.slick.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple wrapper around resource loading should anyone decide to change
 * their minds how this is meant to work in the future.
 * 
 * @author Kevin Glass
 */
public class ResourceLoader {
	/** The list of locations to be searched */
	private static ArrayList locations = new ArrayList();

	public static InputStream getResourceAsStream(String ref) {
		InputStream in = null;
		
		for (int i=0;i<locations.size();i++) {
			ResourceLocation location = (ResourceLocation) locations.get(i);
			in = location.getResourceAsStream(ref);
			if (in != null) {
				break;
			}
		}
		
		if (in == null)
		{
			throw new RuntimeException("Resource not found: "+ref);
		}
			
		return new BufferedInputStream(in);
	}
	
	/**
	 * Check if a resource is available from any given resource loader
	 * 
	 * @param ref A reference to the resource that should be checked
	 * @return True if the resource can be located
	 */
	public static boolean resourceExists(String ref) {
		URL url = null;
		
		for (int i=0;i<locations.size();i++) {
			ResourceLocation location = (ResourceLocation) locations.get(i);
			url = location.getResource(ref);
			if (url != null) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Get a resource as a URL
	 * 
	 * @param ref The reference to the resource to retrieve
	 * @return A URL from which the resource can be read
	 */
	public static URL getResource(String ref) {

		URL url = null;
		
		for (int i=0;i<locations.size();i++) {
			ResourceLocation location = (ResourceLocation) locations.get(i);
			url = location.getResource(ref);
			if (url != null) {
				break;
			}
		}
		
		if (url == null)
		{
			throw new RuntimeException("Resource not found: "+ref);
		}
			
		return url;
	}
}
