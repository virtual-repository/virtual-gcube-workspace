package org.virtual.workspace.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Properties;
import org.virtualrepository.Property;

public class Context {
	
	public static final String username = "user.name";
	
	public static Property user(String name) {
		return new Property("user.name", name);
	}

	private static Logger log = LoggerFactory.getLogger(Context.class);
	
	private static InheritableThreadLocal<Properties> properties = new InheritableThreadLocal<Properties>();
	
	protected Context() {};
	
	public static Properties properties() {
		Properties props = properties.get();
		
		if (props==null) {
			props = new Properties();
			properties.set(props);
		}
		
		return props;
	}
	
	public static void reset() {
		log.debug("resetting contextual properties in thread {}",Thread.currentThread().getId());
		properties.remove();
	}
	
	
	
}
