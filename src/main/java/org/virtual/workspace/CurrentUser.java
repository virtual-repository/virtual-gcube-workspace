package org.virtual.workspace;

import javax.inject.Inject;

public class CurrentUser {

	private final String username;
	
	@Inject
	public CurrentUser(String useusername) {
		this.username=useusername;
	}
	
	public String name() {
		return username;
	}
	
}
